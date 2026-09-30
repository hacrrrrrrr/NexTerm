package com.hacrrrrrrr.nexterm.pkg;
public final class NxPkg {
 public static void main(String[] args){
  if(args.length==0){usage();return;}
  switch(args[0]){
   case "search":case "install":case "remove":case "update":case "info":case "list":
    System.out.println("nxpkg: "+String.join(" ",args)); break;
   default: usage();
  }
 }
 private static void usage(){System.out.println("nxpkg search|install|remove|update|info|list");}
}
