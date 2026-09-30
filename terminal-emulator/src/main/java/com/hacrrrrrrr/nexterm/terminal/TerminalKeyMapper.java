package com.hacrrrrrrr.nexterm.terminal;
public final class TerminalKeyMapper{private TerminalKeyMapper(){}public static String arrow(char d){return "\u001b["+d;}public static String enter(){return "\r";}public static String backspace(){return "\u007f";}public static String tab(){return "\t";}}
