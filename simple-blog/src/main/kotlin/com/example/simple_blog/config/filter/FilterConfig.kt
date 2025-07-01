package com.example.simple_blog.config.filter

import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FilterConfig {

    // @Bean
    fun registMyAuthentionFilter(): FilterRegistrationBean<MyAuthentionFilter> {

       val bean = FilterRegistrationBean(MyAuthentionFilter())

        bean.addUrlPatterns("/api/*")
        bean.order = 0

        return bean

    }

}