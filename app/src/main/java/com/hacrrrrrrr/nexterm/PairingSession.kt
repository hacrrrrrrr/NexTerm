package com.hacrrrrrrr.nexterm
import java.security.SecureRandom
import java.util.Base64
data class PairingSession(val id:String,val token:String,val endpoint:String,val expiresAt:Long){
 fun qrPayload():String="nexterm://pair?endpoint="+java.net.URLEncoder.encode(endpoint,"UTF-8")+"&session="+java.net.URLEncoder.encode(id,"UTF-8")+"&token="+java.net.URLEncoder.encode(token,"UTF-8")+"&expires="+expiresAt
}
object PairingSessionManager{
 private val random=SecureRandom()
 fun create(endpoint:String,lifetimeMs:Long=5*60*1000):PairingSession{
  require(endpoint.startsWith("wss://")||endpoint.startsWith("ws://"))
  return PairingSession(randomBytes(12),randomBytes(32),endpoint,System.currentTimeMillis()+lifetimeMs)
 }
 fun isValid(s:PairingSession)=System.currentTimeMillis()<s.expiresAt
 private fun randomBytes(n:Int):String{val b=ByteArray(n);random.nextBytes(b);return Base64.getUrlEncoder().withoutPadding().encodeToString(b)}
}
