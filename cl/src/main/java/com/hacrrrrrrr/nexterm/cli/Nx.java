package com.hacrrrrrrr.nexterm.cli;
public final class Nx {
 public static void main(String[] args){
  if(args.length==0){usage();return;}
  switch(args[0]){
   case "version":System.out.println("NexTerm CLI 0.1.0");break;
   case "doctor":System.out.println("NexTerm CLI: runtime checks");break;
   case "shell":System.out.println("Use a NexTerm terminal session for an interactive shell.");break;
   case "session":case "package":case "api":System.out.println("nx "+String.join(" ",args));break;
   default:usage();
  }
 }
 private static void usage(){System.out.println("nx version|doctor|shell|session|package|api");}
}
