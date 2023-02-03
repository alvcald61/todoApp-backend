package com.portfolio.todobackend.controller

import com.portfolio.todobackend.service.UserService
import com.portfolio.todobackend.service.dto.LoginDto
import com.portfolio.todobackend.service.dto.TodoDto
import com.portfolio.todobackend.service.dto.UserDto
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = ["*"])
class UserController(
    val userService: UserService
) {


    @GetMapping("/{email}")
    @ResponseStatus(HttpStatus.FOUND)
    fun getUserByUsername(@PathVariable email: String) = userService.getUser(email)

    @GetMapping("/{email}/todos")
    @ResponseStatus(HttpStatus.OK)
    fun getTodosByUserByUsername(@PathVariable email: String) = userService.getTodos(email)


    @DeleteMapping("/{email}")
    @ResponseStatus(HttpStatus.OK)
    fun deleteUser(@PathVariable email: String) = userService.delete(email)

    @PostMapping("/addTodo/{email}")
    @ResponseStatus(HttpStatus.CREATED)
    fun addTodoToUser(@PathVariable email: String, @RequestBody todo: TodoDto) = userService.addTodo(email, todo)

    @DeleteMapping("/deleteTodo/{email}/{todoId}")
    @ResponseStatus(HttpStatus.OK)
    fun deleteTodoFromUser(@PathVariable email: String, @PathVariable todoId: String) =
        userService.removeTodo(email, todoId)

    @PutMapping("/completeTodo/{email}/{todoId}")
    @ResponseStatus(HttpStatus.OK)
    fun completeTodoFromUser(@PathVariable email: String, @PathVariable todoId: String) =
        userService.completeTodo(email, todoId)


}