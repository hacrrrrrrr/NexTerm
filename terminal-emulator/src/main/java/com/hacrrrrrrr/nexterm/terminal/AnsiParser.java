package com.hacrrrrrrr.nexterm.terminal;
public final class AnsiParser {
 public interface Sink{void text(String s);void control(String sequence);}
 private final StringBuilder esc=new StringBuilder(); private boolean inEsc;
 public void feed(String input,Sink sink){for(int i=0;i<input.length();i++){char c=input.charAt(i);if(!inEsc&&c==0x1b){inEsc=true;esc.setLength(0);esc.append(c);continue;}if(inEsc){esc.append(c);if((c>='@'&&c<='~')||esc.length()>64){sink.control(esc.toString());inEsc=false;}}else sink.text(String.valueOf(c));}}
}
