package com.portfolio.todobackend.repository

import com.portfolio.todobackend.model.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : MongoRepository<User, String> {
    fun findByEmail(email: String): User?
}