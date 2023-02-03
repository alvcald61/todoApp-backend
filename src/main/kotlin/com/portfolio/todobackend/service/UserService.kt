package com.portfolio.todobackend.service

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.portfolio.todobackend.model.User
import com.portfolio.todobackend.service.dto.LoginDto
import com.portfolio.todobackend.service.dto.TodoDto
import com.portfolio.todobackend.service.dto.UserDto

interface UserService {
    fun getUser(email: String): UserDto
    fun listAll(): List<UserDto>
    fun registerUser(userDto: UserDto): UserDto
    fun delete(id: String): Boolean
    fun addTodo(email: String, todo: TodoDto): UserDto
    fun removeTodo(email: String, todoId: String)
    fun completeTodo(email: String, todoId: String)
    fun getTodos(email: String): List<TodoDto> 
    fun login(email: String, password: String?): LoginDto
    fun getUserOrRegister(token: GoogleIdToken): User
    fun registerGoogleUser(token: GoogleIdToken): User
    fun validateGoogleJwt(jwt: String?): GoogleIdToken
    fun generateJwt(user: User): LoginDto
}