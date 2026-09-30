package com.wallace.Library.config

import com.wallace.Library.security.jwt.JwtConfigurer
import com.wallace.Library.security.jwt.JwtTokenFilter
import com.wallace.Library.security.jwt.JwtTokenProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.password.DelegatingPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val tokenProvider: JwtTokenProvider
) {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        val encoders: MutableMap<String, PasswordEncoder> = HashMap()

        val pbkdf2 = Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8()

        encoders["pbkdf2"] = pbkdf2

        val passwordEncoder = DelegatingPasswordEncoder("pbkdf2", encoders)
        passwordEncoder.setDefaultPasswordEncoderForMatches(pbkdf2)

        return passwordEncoder
    }

    @Bean
    fun authenticationManagerBean(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .httpBasic { it.disable() }
            .csrf { it.disable() }
            .sessionManagement { session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authorizeHttpRequests { auth ->
                auth
                    // 1. Rotas do Swagger e OpenAPI totalmente liberadas
                    .requestMatchers(
                        "/v3/api-docs/**",         // Arquivos de configuração JSON/YAML da API
                        "/v3/api-docs.yaml",
                        "/swagger-ui/**",          // Arquivos estáticos da interface visual
                        "/swagger-ui.html",        // Endpoint de redirecionamento antigo
                        "/swagger-resources/**",   // Recursos adicionais do Swagger
                        "/webjars/**"              // Bibliotecas web estáticas
                    ).permitAll()

                    // 2. Rotas de Autenticação da sua API
                    .requestMatchers(
                        "/auth/signin",
                        "/auth/refresh"
                    ).permitAll()

                    // 3. Regras de restrição
                    .requestMatchers("/api/**").authenticated()
                    .requestMatchers("/users").denyAll()
                    .anyRequest().authenticated()
            }
            .cors(Customizer.withDefaults())

        http.apply(JwtConfigurer(tokenProvider))

        return http.build()
    }
}