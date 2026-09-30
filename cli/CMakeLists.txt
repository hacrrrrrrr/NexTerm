cmake_minimum_required(VERSION 3.22.1)
project(nexterm_cli LANGUAGES CXX)
add_executable(nx src/main/cpp/nx.cpp)
target_compile_features(nx PRIVATE cxx_std_17)
