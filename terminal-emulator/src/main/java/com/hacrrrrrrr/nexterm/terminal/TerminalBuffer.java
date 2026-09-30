package com.hacrrrrrrr.nexterm.terminal;
import java.util.ArrayDeque;
public final class TerminalBuffer {
 private final ArrayDeque<String> lines=new ArrayDeque<>(); private String current=""; private final int maxLines;
 public TerminalBuffer(int maxLines){this.maxLines=Math.max(100,maxLines);}
 public synchronized void append(String text){for(int i=0;i<text.length();i++){char c=text.charAt(i);if(c=='\n'){lines.addLast(current);current="";while(lines.size()>maxLines)lines.removeFirst();}else if(c=='\r'){}else if(c=='\b'&&!current.isEmpty())current=current.substring(0,current.length()-1);else if(c>=0x20)current+=c;}}
 public synchronized String[] snapshot(){String[] a=lines.toArray(new String[0]);String[] out=new String[a.length+1];System.arraycopy(a,0,out,0,a.length);out[a.length]=current;return out;}
 public synchronized void clear(){lines.clear();current="";}
}
