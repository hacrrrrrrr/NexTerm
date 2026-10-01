package com.hacrrrrrrr.nexterm
import android.content.Context
import org.json.JSONObject
object PairingStore{
 private const val PREFS="nexterm_pairing"
 fun save(c:Context,s:PairingSession){val j=JSONObject().put("id",s.id).put("token",s.token).put("endpoint",s.endpoint).put("expiresAt",s.expiresAt);c.getSharedPreferences(PREFS,0).edit().putString("session",j.toString()).apply()}
 fun load(c:Context):PairingSession?=runCatching{val j=JSONObject(c.getSharedPreferences(PREFS,0).getString("session",null)?:return null);PairingSession(j.getString("id"),j.getString("token"),j.getString("endpoint"),j.getLong("expiresAt"))}.getOrNull()
 fun clear(c:Context){c.getSharedPreferences(PREFS,0).edit().clear().apply()}
}
