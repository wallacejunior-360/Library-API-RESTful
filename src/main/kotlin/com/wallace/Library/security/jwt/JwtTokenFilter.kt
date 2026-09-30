package com.wallace.Library.security.jwt

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtTokenFilter(
    private val tokenProvider: JwtTokenProvider
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        // 1. Extrai o token do cabeçalho
        val token = tokenProvider.resolveToken(request)

        try {
            // 2. Se o token existir e for válido, autentica no contexto do Spring
            if (token != null && tokenProvider.validateToken(token)) {
                val auth = tokenProvider.getAuthentication(token)
                if (auth != null) {
                    org.springframework.security.core.context.SecurityContextHolder.getContext().authentication = auth
                }
            }
        } catch (e: Exception) {
            // Evita que o erro morra silenciosamente limpando o contexto e logando a falha
            org.springframework.security.core.context.SecurityContextHolder.clearContext()
            logger.error("Erro na autenticação JWT: ${e.message}")
        }

        // IMPORTANTE: Esta linha DEVE ser executada sempre, mesmo se o token for nulo!
        // Se ela for esquecida ou não for alcançada, a requisição morre com Status 200 em branco.
        filterChain.doFilter(request, response)
    }
}