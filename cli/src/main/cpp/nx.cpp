#include <cstdlib>
#include <iostream>
#include <string>
#include <vector>

static void help() {
    std::cout << "NexTerm CLI (nx)\n\n"
              << "Usage: nx <command> [arguments]\n\n"
              << "  help\n  version\n  doctor\n  shell\n"
              << "  session list|open|close <id>\n"
              << "  package search|install|remove|update|list\n"
              << "  api health\n";
}

int main(int argc, char** argv) {
    if (argc < 2) { help(); return 0; }
    std::string cmd = argv[1];

    if (cmd == "help" || cmd == "--help") { help(); return 0; }

    if (cmd == "version" || cmd == "--version") {
        std::cout << "NexTerm CLI 0.1.0\n";
        return 0;
    }

    if (cmd == "doctor") {
        std::cout << "NexTerm doctor\n"
                  << "  CLI:        OK\n"
                  << "  userspace:  pending\n"
                  << "  package DB: pending\n"
                  << "  API:        configurable\n";
        return 0;
    }

    if (cmd == "shell") {
        const char* shell = std::getenv("NEXTERM_SHELL");
        if (!shell) shell = "/system/bin/sh";
        std::cout << "Starting NexTerm shell: " << shell << "\n";
        return std::system(shell);
    }

    if (cmd == "session") {
        if (argc < 3) { std::cerr << "missing session command\n"; return 2; }
        std::string sub = argv[2];
        if (sub == "list") { std::cout << "No active sessions.\n"; return 0; }
        if (sub == "open") { std::cout << "Opening NexTerm session service...\n"; return 0; }
        if (sub == "close" && argc >= 4) {
            std::cout << "Closing session " << argv[3] << "\n"; return 0;
        }
    }

    if (cmd == "package") {
        if (argc < 3) { std::cerr << "missing package command\n"; return 2; }
        std::string sub = argv[2];
        if ((sub == "search" || sub == "install" || sub == "remove") && argc >= 4) {
            std::cout << "NexTerm package " << sub << ": " << argv[3] << "\n"; return 0;
        }
        if (sub == "update") { std::cout << "Updating NexTerm package indexes...\n"; return 0; }
        if (sub == "list") { std::cout << "Installed package database is empty.\n"; return 0; }
    }

    if (cmd == "api" && argc >= 3 && std::string(argv[2]) == "health") {
        std::cout << "NexTerm API: not configured\n"; return 0;
    }

    std::cerr << "Unknown NexTerm command. Run: nx help\n";
    return 2;
}
