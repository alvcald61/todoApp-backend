package com.portfolio.todobackend.service.impl

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.portfolio.todobackend.mapper.Mapper
import com.portfolio.todobackend.model.LoginMethod
import com.portfolio.todobackend.model.User
import com.portfolio.todobackend.repository.UserRepository
import com.portfolio.todobackend.service.UserService
import com.portfolio.todobackend.service.dto.LoginDto
import com.portfolio.todobackend.service.dto.TodoDto
import com.portfolio.todobackend.service.dto.UserDto
import com.portfolio.todobackend.utils.JwtUtil
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.nio.file.attribute.UserPrincipalNotFoundException
import java.util.UUID.randomUUID

@Service
class UserServiceImpl(
    @Value("\${google.client.id}")
    private val clientId: String,
    val userRepository: UserRepository,
    val mapper: Mapper,
    val passwordEncoder: BCryptPasswordEncoder,
    val authenticationManager: AuthenticationManager,
    val jwtUtil: JwtUtil
) : UserService {
    override fun getUser(email: String): UserDto {
        val user = userRepository.findByEmail(email) ?: throw ClassNotFoundException("User $email not found")
        return mapper.userToDto(user)
    }


    private fun getVerifier(): GoogleIdTokenVerifier {
        val transportNetHttpTransport = NetHttpTransport()
        val jsonFactory = GsonFactory()
        return GoogleIdTokenVerifier.Builder(transportNetHttpTransport, jsonFactory)
            .setAudience(listOf(clientId))
            .build()
    }

    override fun listAll(): List<UserDto> {
        val users = userRepository.findAll()
        return users.map(mapper::userToDto)
    }

    override fun registerUser(userDto: UserDto): UserDto {
        validateUserExistence(userDto.email!!)
        var user: User = mapper.dtoToUser(userDto)
        user.password = passwordEncoder.encode(user.password)
        user.loginMethod = LoginMethod.LOCAL
        user = userRepository.save(user)
        return mapper.userToDto(user)
    }

    fun validateUserExistence(email: String) {
        if (userRepository.findByEmail(email) != null) {
            throw IllegalArgumentException("User already exists")
        }
    }

    override fun delete(id: String): Boolean {
        userRepository.deleteById(id)
        return true
    }

    override fun addTodo(email: String, todo: TodoDto): UserDto {
        val user = userRepository.findByEmail(email) ?: throw ClassNotFoundException("User not found")
        todo.id = randomUUID().toString()
        user.todos.add(mapper.dtoToTodo(todo))
        return mapper.userToDto(userRepository.save(user))
    }

    override fun removeTodo(email: String, todoId: String) {
        val user = userRepository.findByEmail(email) ?: throw ClassNotFoundException("User not found")
        user.todos.removeIf { todo -> todo.id == todoId }
        userRepository.save(user)
    }

    override fun completeTodo(email: String, todoId: String) {
        val user = userRepository.findByEmail(email) ?: throw ClassNotFoundException("User not found")
        val todo = user.todos.find { todo -> todo.id == todoId }
        todo?.isDone = true
        userRepository.save(user)
    }

    override fun getTodos(email: String): List<TodoDto> {
        val user = userRepository.findByEmail(email) ?: throw ClassNotFoundException("User not found")
        return user.todos.map(mapper::todoToDto)
    }

    override fun login(email: String, password: String?): LoginDto {
        authenticationManager.authenticate(UsernamePasswordAuthenticationToken(email, password))
        val user = userRepository.findByEmail(email) ?: throw ClassNotFoundException("User not found")
//        if (user.password != passwordEncoder.encode(password)) {
//            throw UserPrincipalNotFoundException("Wrong password")
//        }
        val jwt = jwtUtil.generateToken(email = user.email!!, user.name!!)
        return LoginDto(email = user.email, jwt = jwt, name = user.name)
    }

    override fun getUserOrRegister(token: GoogleIdToken): User {
        return userRepository.findByEmail(token.payload.email) ?: registerGoogleUser(token)
    }

    override fun registerGoogleUser(token: GoogleIdToken): User {
        //TODO: sen random password to email and ask user to change it
        val email = token.payload.email
        val name = token.payload["name"] as String
        val randomPassword = passwordEncoder.encode(randomUUID().toString())
        var user = User(email = email, name = name, loginMethod = LoginMethod.GOOGLE, password = randomPassword)
        user = userRepository.save(user)
        return user
    }

    override fun validateGoogleJwt(jwt: String?): GoogleIdToken {
        if (jwt == null) {
            throw Exception("No token provided")
        }
        val token = JwtUtil.getCleanToken(jwt)
        val verifier = getVerifier()
        val idToken = verifier.verify(token) ?: throw Exception("Invalid token")
        validateExpirationDate(idToken)
        return idToken

    }

    private fun validateExpirationDate(idToken: GoogleIdToken) {
        if (!idToken.verifyExpirationTime(System.currentTimeMillis(), 10000)) {
            throw Exception("Token expired")
        }
    }

    override fun generateJwt(user: User): LoginDto {
        val jwt = jwtUtil.generateToken(email = user.email!!, user.name!!)
        return LoginDto(email = user.email, jwt = jwt, name = user.name)
    }
    
    

}