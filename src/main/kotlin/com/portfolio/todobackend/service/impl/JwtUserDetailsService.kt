package com.portfolio.todobackend.service.impl

import com.portfolio.todobackend.repository.UserRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class JwtUserDetailsService(
    val userRepository: UserRepository
) : UserDetailsService {
    
    override fun loadUserByUsername(username: String?): UserDetails {
        val user = userRepository.findByEmail(username!!) ?: throw IllegalArgumentException("User not found")
        return User(user.email, user.password, emptyList())
    }
}