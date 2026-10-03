#include <jni.h>
#include <android/log.h>
#include <errno.h>
#include <pty.h>
#include <signal.h>
#include <string.h>
#include <sys/ioctl.h>
#include <unistd.h>

#define TAG "NexTerm"
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, TAG, __VA_ARGS__)

static jfieldID fdField(JNIEnv* env, jobject self) {
    jclass c = env->GetObjectClass(self);
    return env->GetFieldID(c, "nativeFd", "I");
}

extern "C" JNIEXPORT jint JNICALL Java_com_hacrrrrrrr_nexterm_PtyProcess_nativeSpawn(
    JNIEnv* env, jobject self, jstring shell, jstring home, jstring path) {
    if (!shell || !home || !path) return -1;

    const char* shellC = env->GetStringUTFChars(shell, nullptr);
    const char* homeC = env->GetStringUTFChars(home, nullptr);
    const char* pathC = env->GetStringUTFChars(path, nullptr);
    if (!shellC || !homeC || !pathC) {
        if (shellC) env->ReleaseStringUTFChars(shell, shellC);
        if (homeC) env->ReleaseStringUTFChars(home, homeC);
        if (pathC) env->ReleaseStringUTFChars(path, pathC);
        return -1;
    }

    int master = -1;
    struct winsize ws{};
    ws.ws_col = 80;
    ws.ws_row = 24;

    pid_t child = forkpty(&master, nullptr, nullptr, &ws);
    if (child < 0) {
        LOGE("forkpty failed: %s", strerror(errno));
        env->ReleaseStringUTFChars(shell, shellC);
        env->ReleaseStringUTFChars(home, homeC);
        env->ReleaseStringUTFChars(path, pathC);
        return -1;
    }

    if (child == 0) {
        setenv("HOME", homeC, 1);
        setenv("PATH", pathC, 1);
        setenv("TERM", "xterm-256color", 1);
        setenv("SHELL", shellC, 1);
        setenv("USER", "u0_nexterm", 1);
        execl(shellC, shellC, "-i", nullptr);
        _exit(127);
    }

    env->SetIntField(self, fdField(env, self), master);
    env->ReleaseStringUTFChars(shell, shellC);
    env->ReleaseStringUTFChars(home, homeC);
    env->ReleaseStringUTFChars(path, pathC);
    return static_cast<jint>(child);
}

extern "C" JNIEXPORT jint JNICALL Java_com_hacrrrrrrr_nexterm_PtyProcess_nativeRead(
    JNIEnv* env, jobject self, jbyteArray out) {
    if (!out) return -1;
    const int fd = env->GetIntField(self, fdField(env, self));
    if (fd < 0) return -1;

    const jsize len = env->GetArrayLength(out);
    if (len <= 0) return 0;

    // Avoid a second heap allocation on every read.
    jbyte* buf = env->GetByteArrayElements(out, nullptr);
    if (!buf) return -1;

    const ssize_t n = read(fd, buf, static_cast<size_t>(len));
    env->ReleaseByteArrayElements(out, buf, n > 0 ? 0 : JNI_ABORT);

    if (n < 0 && (errno == EINTR || errno == EAGAIN)) return 0;
    if (n < 0 && (errno == EIO || errno == EBADF)) return -1;
    return static_cast<jint>(n);
}

extern "C" JNIEXPORT jint JNICALL Java_com_hacrrrrrrr_nexterm_PtyProcess_nativeWrite(
    JNIEnv* env, jobject self, jbyteArray data, jint len) {
    if (!data || len <= 0) return 0;
    const int fd = env->GetIntField(self, fdField(env, self));
    if (fd < 0) return -1;

    const jsize actual = env->GetArrayLength(data);
    if (actual <= 0) return 0;
    const jsize count = len > actual ? actual : len;

    jbyte* bytes = env->GetByteArrayElements(data, nullptr);
    if (!bytes) return -1;

    ssize_t n;
    do {
        n = write(fd, bytes, static_cast<size_t>(count));
    } while (n < 0 && errno == EINTR);

    env->ReleaseByteArrayElements(data, bytes, JNI_ABORT);
    return static_cast<jint>(n);
}

extern "C" JNIEXPORT void JNICALL Java_com_hacrrrrrrr_nexterm_PtyProcess_nativeResize(
    JNIEnv* env, jobject self, jint rows, jint cols) {
    const int fd = env->GetIntField(self, fdField(env, self));
    if (fd < 0 || rows < 2 || rows > 500 || cols < 8 || cols > 500) return;

    struct winsize ws{};
    ws.ws_row = static_cast<unsigned short>(rows);
    ws.ws_col = static_cast<unsigned short>(cols);
    if (ioctl(fd, TIOCSWINSZ, &ws) < 0) {
        LOGE("TIOCSWINSZ failed: %s", strerror(errno));
    }
}

extern "C" JNIEXPORT void JNICALL Java_com_hacrrrrrrr_nexterm_PtyProcess_nativeClose(
    JNIEnv* env, jobject self) {
    const jfieldID field = fdField(env, self);
    const int fd = env->GetIntField(self, field);
    if (fd >= 0) {
        close(fd);
        env->SetIntField(self, field, -1);
    }
}
