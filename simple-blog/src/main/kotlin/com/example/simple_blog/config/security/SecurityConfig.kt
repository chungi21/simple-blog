package com.example.simple_blog.config.security

import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity(debug = false)
class SecurityConfig(
    private val authenticationConfiguration : AuthenticationConfiguration,
    private val objectMapper: ObjectMapper
) {

    private val log = KotlinLogging.logger {}

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .headers { headers -> headers.frameOptions { it.disable() } }
            .formLogin { it.disable() }
            .httpBasic { it.disable() }
            .sessionManagement { session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .cors { cors ->
                cors.configurationSource(corsConfig())
            }
            .addFilterAt(loginFilter(), UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }

    @Bean
    fun passwordEncoder() : BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }


    @Bean
    fun authenticationManager() : AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }

    @Bean
    fun loginFilter() : UsernamePasswordAuthenticationFilter {

        val authenticationFilter = CustomUserNameAuthenticationFilter(objectMapper)
        authenticationFilter.setAuthenticationManager(authenticationManager())

        return authenticationFilter

    }


    @Bean
    fun corsConfig(): CorsConfigurationSource {
        val config = CorsConfiguration()
        config.allowCredentials = true
        config.addAllowedOriginPattern("*")
        config.addAllowedMethod("*")
        config.addAllowedHeader("*")
        config.addExposedHeader("authorization")

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", config)

        return source
    }
}
