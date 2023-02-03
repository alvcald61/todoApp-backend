package com.portfolio.todobackend.service.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.portfolio.todobackend.service.dto.TodoDto

data class UserDto (
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    val id: String? = null, 
    val name: String? = null,
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    val password: String? = null,
    val todos: MutableList<TodoDto>? = mutableListOf(),
    val email: String? = null
    )
        