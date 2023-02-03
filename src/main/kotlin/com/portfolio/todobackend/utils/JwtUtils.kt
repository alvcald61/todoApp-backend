package com.portfolio.todobackend.utils

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import java.io.Serializable
import java.security.Key
import java.util.*
import javax.crypto.spec.SecretKeySpec


@Service
class JwtUtil(
    @Value("\${jwt.expire.time}")
    private val EXPIRE_DATE : Int? = null,
    @Value("\${jwt.issuer}")
    private val issuer : String? = null,
    @Value("\${jwt.secret}")
    private val secret : String? = null
) : Serializable {
    companion object {
        private const val bearer : String = "Bearer "
        fun getCleanToken(token: String):String{
            return token.substringAfter(bearer).trim()
        }
    }

   fun getHmacKey() = SecretKeySpec(Base64.getDecoder().decode(secret),SignatureAlgorithm.HS256.jcaName)
    
    
    fun getClaimsFromToken(token: String): Claims {
        
        return Jwts.parserBuilder().setSigningKey(getHmacKey()).build().parseClaimsJws(token).body
    }

    fun generateToken(email: String, name: String): String {
        return Jwts.builder()
            .claim("email", email)
            .claim("name", name)
            .setSubject(email)
            .setIssuer(issuer)
            .setIssuedAt(Date(System.currentTimeMillis()))  
            .setExpiration(Date(System.currentTimeMillis() + EXPIRE_DATE!!))
            .signWith(getHmacKey())
            .compact() 
        
    }
    
    fun isValidToken(jwtString:String, userDetails: UserDetails):Boolean{
        val userEmail = getEmailFromToken(jwtString)
        val expiration = getExpirationDateFromToken(jwtString)
        val now = Date()
        if (now.after(expiration) && userEmail != userDetails.username) {
            return false
        }
        return true
    }
    
    fun getEmailFromToken(token: String): String {
        return getClaimsFromToken(token.substring(7))["email"] as String
    }
    
    fun getExpirationDateFromToken(token: String): Date {
        return getClaimsFromToken(token.substring(7)).expiration
    }
    
}