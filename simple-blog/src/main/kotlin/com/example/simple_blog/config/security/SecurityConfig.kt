package com.example.simple_blog.config.security

import com.example.simple_blog.domain.member.MemberRepository
import com.example.simple_blog.util.func.responseData
import com.example.simple_blog.util.value.CmResDTO
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import mu.KotlinLogging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity(debug = false)
class SecurityConfig(
    private val authenticationConfiguration : AuthenticationConfiguration,
    private val objectMapper: ObjectMapper,
    private val memberRepository: MemberRepository
) {

    private val log = KotlinLogging.logger {}


    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .cors { }
            .headers { it.frameOptions { frame -> frame.disable() } }
            .formLogin { it.disable() }
            .httpBasic { it.disable() }
            .sessionManagement { session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .cors { it.configurationSource(corsConfig()) }

        // 필터는 DSL 밖에서 등록
        http.addFilterAt(loginFilter(), UsernamePasswordAuthenticationFilter::class.java)
        http.addFilterBefore(authenticationFilter(), CustomBasicAuthenticationFilter::class.java)

        // 예외 핸들링
        http.exceptionHandling {
            it.authenticationEntryPoint(CustomAuthenticationEntryPoint())
            it.accessDeniedHandler(CustomAccessDeniedHandler())
        }

        // 인가 설정
        http.authorizeHttpRequests {
            it
                .requestMatchers("/login", "/logout", "/api/member/join").permitAll()
                .requestMatchers("/api/members/check-email", "/api/members/check-nickname").permitAll() // ✅ 추가
                .requestMatchers("/api/member/email/**").permitAll()
                .requestMatchers("/api/members/recent").permitAll()
                .requestMatchers("/api/members/me").authenticated()
                .requestMatchers("/api/members/**").hasRole("USER")
                .requestMatchers(HttpMethod.GET, "/api/posts", "/api/posts/*").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/posts/form", "/api/posts/*/form").hasRole("USER")
                .requestMatchers(HttpMethod.POST, "/api/posts").hasRole("USER")
                .requestMatchers(HttpMethod.PUT, "/api/posts/*").hasRole("USER")
                .requestMatchers(HttpMethod.DELETE, "/api/posts/*").hasRole("USER")
                .anyRequest().permitAll()
        }

        http.logout {
            it.logoutUrl("/logout").logoutSuccessHandler(CustomLogoutSuccessHandler(objectMapper))  // GET 요청이 아니라 POST 요청이어야 로그아웃 됨
        }

        return http.build()
    }

    class CustomLogoutSuccessHandler(
        private val om:ObjectMapper
    ) : LogoutSuccessHandler{

        private  val log = KotlinLogging.logger {}

        override fun onLogoutSuccess(
            request: HttpServletRequest,
            response: HttpServletResponse,
            authentication: Authentication?
        ) {
            log.info{ "logout 성공!!" }
            val context = SecurityContextHolder.getContext()
            context.authentication = null
            SecurityContextHolder.clearContext()

            val cmResDto = CmResDTO(HttpStatus.OK, "logout 성공!", null)

            responseData(response, om.writeValueAsString(cmResDto))
        }

    }


    class CustomFailureHandler : AuthenticationFailureHandler {

        private  val log = KotlinLogging.logger {}

        override fun onAuthenticationFailure(
            request: HttpServletRequest,
            response: HttpServletResponse,
            exception: AuthenticationException?
        ) {
            log.info { "login 실패!!!" }
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "인증 실패")
        }

    }

    class CustomSuccessHandler : AuthenticationSuccessHandler {

        private  val log = KotlinLogging.logger {}

        override fun onAuthenticationSuccess(
            request: HttpServletRequest,
            response: HttpServletResponse,
            authentication: Authentication?
        ) {
            log.info { "login 성공!!!" }
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "인증 성공")
        }

    }


    class CustomAuthenticationEntryPoint : AuthenticationEntryPoint {

        private  val log = KotlinLogging.logger {}

        override fun commence(
            request: HttpServletRequest,
            response: HttpServletResponse,
            authException: AuthenticationException?
        ) {
            log.info { " AuthenticationEntryPoint!!!" }
            response.sendError(HttpServletResponse.SC_FORBIDDEN)
        }

    }

    class CustomAccessDeniedHandler : AccessDeniedHandler {

        private  val log = KotlinLogging.logger {}

        override fun handle(
            request: HttpServletRequest,
            response: HttpServletResponse,
            accessDeniedException: AccessDeniedException?
        ) {
            log.info { " AccessDeniedHandler!!!" }
            response.sendError(HttpServletResponse.SC_FORBIDDEN)
        }

    }

    @Bean
    fun authenticationFilter() : CustomBasicAuthenticationFilter{
        return CustomBasicAuthenticationFilter(
            authenticationManager = authenticationManager(),
            memberRepository = memberRepository,
            om = objectMapper
        )
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
        authenticationFilter.setFilterProcessesUrl("/login")
        authenticationFilter.setAuthenticationFailureHandler(CustomFailureHandler())
        authenticationFilter.setAuthenticationSuccessHandler(CustomSuccessHandler())

        return authenticationFilter

    }

    @Bean
    fun corsConfig(): CorsConfigurationSource {
        val config = CorsConfiguration()
        config.allowCredentials = true
        config.addAllowedOriginPattern("http://localhost:3000")
        config.addAllowedMethod("*")
        config.addAllowedHeader("*")
        config.addExposedHeader("authorization")

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", config)

        return source
    }

}
