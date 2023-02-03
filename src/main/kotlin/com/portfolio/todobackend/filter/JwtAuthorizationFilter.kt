package com.portfolio.todobackend.filter

import com.portfolio.todobackend.service.impl.JwtUserDetailsService
import com.portfolio.todobackend.utils.JwtUtil
import io.jsonwebtoken.ExpiredJwtException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
class JwtAuthorizationFilter(
    private val jwtUtil : JwtUtil,
    private val jwtUserDetailsService : JwtUserDetailsService 
) : OncePerRequestFilter() {
    

    
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = request.getHeader("Authorization")
        var username : String? = null
        if(token == null){
            filterChain.doFilter(request, response)
            return
        }
        if(token.startsWith("Bearer ")){
            try {
                username = jwtUtil.getEmailFromToken(token)
            } catch (e: IllegalArgumentException) {
                println("Unable to get JWT Token")
            } catch (e: ExpiredJwtException) {
                println("JWT Token has expired")
            }
        } else {
            println("JWT Token does not begin with Bearer String")
            throw Exception("JWT Token does not begin with Bearer String")
        }
        if (username != null && SecurityContextHolder.getContext().authentication == null) {
            val userDetails: UserDetails = jwtUserDetailsService.loadUserByUsername(username)
            if (jwtUtil.isValidToken(token, userDetails)) {
                val usernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.authorities
                )
                usernamePasswordAuthenticationToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = usernamePasswordAuthenticationToken
            }
        }
        filterChain.doFilter(request, response)
    }

 
}