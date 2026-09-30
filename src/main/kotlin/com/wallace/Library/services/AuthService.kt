package com.wallace.Library.services

import com.wallace.Library.controller.BookController
import com.wallace.Library.controller.PersonController
import com.wallace.Library.data.vo.v1.AccountCredentialsVO
import com.wallace.Library.data.vo.v1.BookVO
import com.wallace.Library.data.vo.v1.TokenVO
import com.wallace.Library.exceptions.RequiredObjectIsNullException
import com.wallace.Library.exceptions.ResourceNotFoundException
import com.wallace.Library.mapper.DozerMapper
import com.wallace.Library.model.Book
import com.wallace.Library.repository.BookRepository
import com.wallace.Library.repository.UserRepository
import com.wallace.Library.security.jwt.JwtTokenProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.util.logging.Logger


@Service
class AuthService(
    // 1. Todas as dependências injetadas com segurança através do construtor principal
    private val authenticationManager: AuthenticationManager,
    private val tokenProvider: JwtTokenProvider,
    private val repository: UserRepository
) {

    private val logger = Logger.getLogger(AuthService::class.java.name)

    fun signin(data: AccountCredentialsVO): ResponseEntity<*> {
        logger.info("Trying to log user ${data.username}")

        return try {
            val username = data.username
            val password = data.password

            // Realiza a autenticação via Spring Security
            authenticationManager.authenticate(UsernamePasswordAuthenticationToken(username, password))

            // Busca o usuário usando o método correto do seu repositório
            val user = repository.findByUsername(username)
                ?: throw UsernameNotFoundException("Username $username not found!")

            // 2. Agora o tokenProvider está inicializado e criará o token perfeitamente
            // Nota: Altere 'user.roles' para 'user.role' se o campo da sua classe User for no singular
            val tokenResponse: TokenVO = tokenProvider.createAccessToken(username!!, user.roles)

            ResponseEntity.ok(tokenResponse)
        } catch (e: AuthenticationException) {
            throw BadCredentialsException("Invalid username or password supplied!")
        }
    }

    fun refreshToken(username: String, refreshToken: String): ResponseEntity<*> {
        logger.info("Trying get refresh token to user ${username}")

        // Busca o usuário usando o método correto do seu repositório
        val user = repository.findByUsername(username)
            ?: throw UsernameNotFoundException("Username $username not found!")

        // 2. Agora o tokenProvider está inicializado e criará o token perfeitamente
        // Nota: Altere 'user.roles' para 'user.role' se o campo da sua classe User for no singular
        val tokenResponse: TokenVO = tokenProvider.refreshToken(refreshToken)

        return ResponseEntity.ok(tokenResponse)
    }
}









