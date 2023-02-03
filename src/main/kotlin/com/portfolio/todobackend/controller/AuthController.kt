package com.portfolio.todobackend.controller

import com.portfolio.todobackend.service.UserService
import com.portfolio.todobackend.service.dto.LoginDto
import com.portfolio.todobackend.service.dto.UserDto
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    val userService: UserService
) {
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun login(@RequestBody userDto: LoginDto) = userService.login(userDto.email!!, userDto.password)

    @PostMapping("/googleLogin")
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun googleLogin(@RequestHeader("google-jwt") googleJwt: String?): LoginDto {
        val token = userService.validateGoogleJwt(googleJwt)
        val user = userService.getUserOrRegister(token)
        return userService.generateJwt(user)
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    fun createUser(@RequestBody userDto: UserDto) = userService.registerUser(userDto)

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAllUsers() = userService.listAll()


}