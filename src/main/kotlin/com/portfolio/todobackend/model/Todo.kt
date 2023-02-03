package com.portfolio.todobackend.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*
import javax.validation.constraints.NotBlank

@Document(collection = "todo")
data class Todo(
    @Id
    var id: String?,
    @field:NotBlank
    var title: String,
    @field:NotBlank
    var description: String,
    var isDone: Boolean,
    var creationDate: Date,
)
