package com.wallace.Library.controller

import com.wallace.Library.data.vo.v1.AccountCredentialsVO
import com.wallace.Library.services.AuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Authentication Endpoint")
@RestController
@RequestMapping("/auth")
class AuthController {

    @Autowired
    lateinit var authService: AuthService

    @Operation(summary = "Get Auth Token")
    @PostMapping("/signin")
    fun signin(@RequestBody data: AccountCredentialsVO?) : ResponseEntity<*> {
        return if (data!!.username.isNullOrBlank() || data.password.isNullOrBlank()) {
            ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid username or password!")
        } else {
            authService.signin(data!!)
        }
    }

    @Operation(summary = "Refresh Auth Token")
    @PutMapping("/refresh/{username}")
    fun refreshToken(
        @PathVariable("username") username: String?,
        @RequestHeader("Authorization") refreshToken: String?
    ) : ResponseEntity<*> {
        return if (refreshToken.isNullOrBlank() || username.isNullOrBlank()) {
            ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Invalid client request!")
        } else {
            authService.refreshToken(username, refreshToken)
        }
    }
}