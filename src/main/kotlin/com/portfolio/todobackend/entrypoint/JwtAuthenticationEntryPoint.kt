package com.portfolio.todobackend.entrypoint

import org.springframework.context.annotation.Configuration
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import java.io.IOException
import java.io.Serializable
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component("jwtAuthenticationEntryPoint")
class JwtAuthenticationEntryPoint : AuthenticationEntryPoint, Serializable {

    companion object {
        private const val serialVersionUID = -7858869558953243875L
    }

    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authException: AuthenticationException?
    ) {
        response?.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
        response?.status = HttpServletResponse.SC_UNAUTHORIZED
        response?.contentType = "application/json"
        response?.characterEncoding = "UTF-8"
        response?.writer?.write("{\"error\": \"Unauthorized\"}")
    }
}