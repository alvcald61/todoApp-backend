package com.portfolio.todobackend.model


import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import javax.validation.constraints.NotBlank

@Document(collection = "User")
data class User(
    @Id
    var id: String? = null,
    var name: String? = null,
    @Indexed(unique = true)
    @field:NotBlank(message = "Email is required")
    var email: String? = null,
    var password: String? = null,
    var todos: MutableList<Todo> = mutableListOf(),
    var loginMethod: LoginMethod? = null,
)
