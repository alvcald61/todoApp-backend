package com.portfolio.todobackend.controller

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.nio.file.attribute.UserPrincipalNotFoundException


@ControllerAdvice
class ErrorHandlerController : ResponseEntityExceptionHandler() {
    
    @ExceptionHandler
    fun handleConflict(ex: ClassNotFoundException?): ResponseEntity<Any> {
    return ResponseEntity(errorBody(ex?.message!!), HttpStatus.NOT_FOUND)
    }
    
    @ExceptionHandler
    fun handleIllegalArgument(ex: IllegalArgumentException?): ResponseEntity<Any> {
        return ResponseEntity(errorBody(ex?.message!!), HttpStatus.BAD_REQUEST)
    }
    
    @ExceptionHandler
    fun handleUserPrincipalNotFoundException(ex: UserPrincipalNotFoundException?): ResponseEntity<Any> {
        return ResponseEntity(errorBody("Wrong password"), HttpStatus.UNAUTHORIZED)
    }
    
    //@ExceptionHandler(value = [MethodArgumentNotValidException::class])
    
    override fun handleMethodArgumentNotValid(ex: MethodArgumentNotValidException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        val exceptionErrors = (ex as BindException)
            .bindingResult
            .fieldErrors
            .map { x -> x.defaultMessage }
        return ResponseEntity(errorBody(exceptionErrors), HttpHeaders(), HttpStatus.BAD_REQUEST)
    }

    private fun errorBody(error: String): Map<String, Any> {
        val body: MutableMap<String, Any> = LinkedHashMap()
        val errors: MutableList<String> = ArrayList()
        errors.add(error)
        body["errors"] = errors
        return body
    }

    private fun errorBody(exceptionErrors: List<String?>): Map<String, Any> {
        val body: MutableMap<String, Any> = LinkedHashMap()
        body["errors"] = exceptionErrors
        return body
    }
}