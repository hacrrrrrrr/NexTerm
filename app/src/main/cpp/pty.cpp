#include <jni.h>
#include <android/log.h>
#include <errno.h>
#include <pty.h>
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
    const char* shellC = env->GetStringUTFChars(shell, nullptr);
    const char* homeC = env->GetStringUTFChars(home, nullptr);
    const char* pathC = env->GetStringUTFChars(path, nullptr);
    int master = -1;
    struct winsize ws{};
    ws.ws_col = 80; ws.ws_row = 24;
    pid_t pid = forkpty(&master, nullptr, nullptr, &ws);
    if (pid < 0) {
        LOGE("forkpty failed: %s", strerror(errno));
        env->ReleaseStringUTFChars(shell, shellC);
        env->ReleaseStringUTFChars(home, homeC);
        env->ReleaseStringUTFChars(path, pathC);
        return -1;
    }
    if (pid == 0) {
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
    return static_cast<jint>(pid);
}
extern "C" JNIEXPORT jint JNICALL Java_com_hacrrrrrrr_nexterm_PtyProcess_nativeRead(
    JNIEnv* env, jobject self, jbyteArray out) {
    int fd = env->GetIntField(self, fdField(env, self));
    if (fd < 0) return -1;
    jsize len = env->GetArrayLength(out);
    jbyte* buf = new jbyte[len];
    ssize_t n = read(fd, buf, len);
    if (n > 0) env->SetByteArrayRegion(out, 0, static_cast<jsize>(n), buf);
    delete[] buf;
    if (n < 0 && (errno == EIO || errno == EBADF)) return -1;
    return static_cast<jint>(n);
}
extern "C" JNIEXPORT jint JNICALL Java_com_hacrrrrrrr_nexterm_PtyProcess_nativeWrite(
    JNIEnv* env, jobject self, jbyteArray data, jint len) {
    int fd = env->GetIntField(self, fdField(env, self));
    if (fd < 0) return -1;
    jsize actual = env->GetArrayLength(data);
    if (len > actual) len = actual;
    jbyte* bytes = env->GetByteArrayElements(data, nullptr);
    ssize_t n = write(fd, bytes, static_cast<size_t>(len));
    env->ReleaseByteArrayElements(data, bytes, JNI_ABORT);
    return static_cast<jint>(n);
}
extern "C" JNIEXPORT void JNICALL Java_com_hacrrrrrrr_nexterm_PtyProcess_nativeResize(
    JNIEnv* env, jobject self, jint rows, jint cols) {
    int fd = env->GetIntField(self, fdField(env, self));
    if (fd < 0) return;
    struct winsize ws{};
    ws.ws_row = static_cast<unsigned short>(rows);
    ws.ws_col = static_cast<unsigned short>(cols);
    ioctl(fd, TIOCSWINSZ, &ws);
}
extern "C" JNIEXPORT void JNICALL Java_com_hacrrrrrrr_nexterm_PtyProcess_nativeClose(
    JNIEnv* env, jobject self) {
    int fd = env->GetIntField(self, fdField(env, self));
    if (fd >= 0) {
        close(fd);
        env->SetIntField(self, fdField(env, self), -1);
    }
}
