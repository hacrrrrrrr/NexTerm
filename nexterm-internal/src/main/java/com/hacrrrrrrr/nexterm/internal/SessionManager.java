package com.hacrrrrrrr.nexterm.internal;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
public final class SessionManager {
 public static final class Session { public final String id=UUID.randomUUID().toString(); }
 private final ConcurrentHashMap<String,Session> sessions=new ConcurrentHashMap<>();
 public Session create(){Session s=new Session();sessions.put(s.id,s);return s;}
 public Session get(String id){return sessions.get(id);}
 public void remove(String id){sessions.remove(id);}
 public int size(){return sessions.size();}
}
