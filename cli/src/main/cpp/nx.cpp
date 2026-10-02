#include <cstdlib>
#include <filesystem>
#include <fstream>
#include <iostream>
#include <string>

namespace fs = std::filesystem;

static constexpr const char* VERSION = "0.6.0";

static fs::path state_dir() {
    if (const char* home = std::getenv("HOME")) return fs::path(home) / ".nexterm";
    if (const char* appdata = std::getenv("APPDATA")) return fs::path(appdata) / "NexTerm";
    return fs::current_path() / ".nexterm";
}

static void help() {
    std::cout << "NexTerm CLI (nx)\n\n"
              << "Usage: nx <command> [arguments]\n\n"
              << "  help\n  version\n  doctor\n  shell\n"
              << "  session list|open [id]|close <id>\n"
              << "  package search|install|remove|update|list\n"
              << "  api health\n";
}

static int session_command(int argc, char** argv) {
    const auto dir = state_dir() / "sessions";
    fs::create_directories(dir);
    if (argc < 3) { std::cerr << "missing session command\n"; return 2; }

    const std::string sub = argv[2];
    if (sub == "list") {
        bool any = false;
        for (const auto& e : fs::directory_iterator(dir)) {
            if (e.is_regular_file()) {
                any = true;
                std::cout << e.path().stem().string() << "\n";
            }
        }
        if (!any) std::cout << "No active sessions.\n";
        return 0;
    }

    if (sub == "open") {
        const std::string id = argc >= 4 ? argv[3] : "local-" + std::to_string(std::rand());
        std::ofstream(dir / (id + ".session")) << "id=" << id << "\n";
        std::cout << "Opened session " << id << "\n";
        return 0;
    }

    if (sub == "close" && argc >= 4) {
        const auto file = dir / (std::string(argv[3]) + ".session");
        if (!fs::remove(file)) {
            std::cerr << "Session not found: " << argv[3] << "\n";
            return 1;
        }
        std::cout << "Closed session " << argv[3] << "\n";
        return 0;
    }

    std::cerr << "Unknown session command. Run: nx help\n";
    return 2;
}

static int package_command(int argc, char** argv) {
    const auto dir = state_dir() / "packages";
    fs::create_directories(dir);
    if (argc < 3) { std::cerr << "missing package command\n"; return 2; }

    const std::string sub = argv[2];
    if (sub == "list") {
        bool any = false;
        for (const auto& e : fs::directory_iterator(dir)) {
            if (e.path().extension() == ".nexpkg") {
                any = true;
                std::cout << e.path().stem().string() << "\n";
            }
        }
        if (!any) std::cout << "No installed packages.\n";
        return 0;
    }

    if ((sub == "search" || sub == "install" || sub == "remove") && argc >= 4) {
        const std::string name = argv[3];
        if (sub == "search") {
            std::cout << "Local package search: " << name << "\n";
            return 0;
        }
        const auto file = dir / (name + ".nexpkg");
        if (sub == "install") {
            std::ofstream out(file);
            out << "name=" << name << "\nversion=0\n";
            std::cout << "Installed local package " << name << "\n";
            return 0;
        }
        if (fs::remove(file)) {
            std::cout << "Removed package " << name << "\n";
            return 0;
        }
        std::cerr << "Package not installed: " << name << "\n";
        return 1;
    }

    if (sub == "update") {
        std::cout << "Package index update requires a configured NexTerm repository.\n";
        return 0;
    }

    std::cerr << "Unknown package command. Run: nx help\n";
    return 2;
}

int main(int argc, char** argv) {
    if (argc < 2) { help(); return 0; }
    const std::string cmd = argv[1];

    if (cmd == "help" || cmd == "--help") { help(); return 0; }

    if (cmd == "version" || cmd == "--version") {
        std::cout << "NexTerm CLI " << VERSION << "\n";
        return 0;
    }

    if (cmd == "doctor") {
        std::cout << "NexTerm doctor\n"
                  << "  CLI:        OK\n"
                  << "  state:      " << state_dir() << "\n"
                  << "  userspace:  " << (std::getenv("NEXTERM_PREFIX") ? "configured" : "not configured") << "\n";
        return 0;
    }

    if (cmd == "shell") {
        const char* shell = std::getenv("NEXTERM_SHELL");
        if (!shell) shell = "/system/bin/sh";
        return std::system(shell);
    }

    if (cmd == "session") return session_command(argc, argv);
    if (cmd == "package") return package_command(argc, argv);

    if (cmd == "api" && argc >= 3 && std::string(argv[2]) == "health") {
        const char* endpoint = std::getenv("NEXTERM_API_BASE");
        std::cout << "NexTerm API endpoint: " << (endpoint ? endpoint : "not configured") << "\n";
        return 0;
    }

    std::cerr << "Unknown NexTerm command. Run: nx help\n";
    return 2;
}
