package com.portfolio.todobackend.mapper

import com.portfolio.todobackend.model.Todo
import com.portfolio.todobackend.model.User
import com.portfolio.todobackend.service.dto.TodoDto
import com.portfolio.todobackend.service.dto.UserDto
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Component
import java.text.SimpleDateFormat

@Component
class Mapper(
    val modelMapper: ModelMapper
) {
    fun userToDto(user: User): UserDto {
        return UserDto(
            id = user.id,
            name = user.name,
            password = user.password,
            user.todos.map(this::todoToDto).toMutableList()
        )
    }

    fun dtoToUser(userDto: UserDto): User {
        val list = userDto.todos?.map(this::dtoToTodo)?.toMutableList()
        return User(
            id = userDto.id,
            name = userDto.name,
            password = userDto.password,
            todos = list ?: mutableListOf(),
            email = userDto.email
        )
    }
    

    fun dtoToTodo(todoDto: TodoDto): Todo {
        return Todo(
            todoDto.id,
            todoDto.title,
            todoDto.description,
            todoDto.isDone,
            SimpleDateFormat("dd/MM/yyyy HH:mm").parse(todoDto.creationDate),
        )
    }

    fun todoToDto(todo: Todo): TodoDto {
        return TodoDto(
            id = todo.id,
            title = todo.title,
            description = todo.description,
            isDone = todo.isDone,
            SimpleDateFormat("dd/MM/yyyy HH:mm").format(todo.creationDate),
        )
    }
}