package com.portfolio.todobackend.service.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class LoginDto (
    val email: String? = null,
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    val jwt: String? = null,
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    val password: String?=null,
    val name: String? = null,
    
) 