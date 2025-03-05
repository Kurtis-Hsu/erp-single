package com.vireosci.erp.configuration

import com.baomidou.mybatisplus.annotation.DbType
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Validation
import jakarta.validation.Validator
import org.mybatis.spring.annotation.MapperScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

// 配置相关

/**
 * 基础配置
 */
@Configuration
class BaseConfiguration
{
    /**
     * 替换 spring 默认的对象映射工具
     */
    @Bean
    fun objectMapper() = ObjectMapper().apply {
        setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.PUBLIC_ONLY)
        setSerializationInclusion(JsonInclude.Include.NON_NULL)
        disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    }

    @Bean
    fun validator(): Validator = Validation
        .byDefaultProvider()
        .configure()
        .addProperty("hibernate.validator.fail_fast", "true") // 配置快速失败
        .buildValidatorFactory()
        .use { it.validator }
}

/**
 * Web MVC 配置
 */
@Configuration
class WebMvcConfiguration : WebMvcConfigurer
{
    /**
     * 关闭跨域拦截
     */
    override fun addCorsMappings(registry: CorsRegistry)
    {
        registry
            .addMapping("/**")
            .allowedMethods("*")
            .allowedHeaders("*")
            .allowedOriginPatterns("*")
            .allowCredentials(true)
            .maxAge(3600)
    }
}

/**
 * 数据库相关配置
 */
@Configuration
@MapperScan("com.vireosci.erp.mapper")
class DatabaseConfiguration
{
    /**
     * 配置 MP 拦截器插件
     */
    @Bean
    fun mybatisPlusInterceptor() = MybatisPlusInterceptor().apply {
        addInnerInterceptor(PaginationInnerInterceptor(DbType.POSTGRE_SQL))
    }

    /**
     * 配置 RedisTemplate
     */
    @Bean
    fun redisTemplate(connectionFactory: RedisConnectionFactory) = RedisTemplate<String, Any>().apply {
        setConnectionFactory(connectionFactory)
        keySerializer = StringRedisSerializer()
        valueSerializer = GenericJackson2JsonRedisSerializer()
    }
}

@Configuration
class SecurityConfiguration
{
    /**
     * 密码编码器
     */
    @Bean fun passwordEncoder() = PasswordEncoderFactories.createDelegatingPasswordEncoder()!!

    /**
     * HTTP 安全配置
     */
    @Bean
    fun securityFilterChain(security: HttpSecurity) = security
        .authorizeHttpRequests {
            // val allowList = arrayOf("")
            it.anyRequest().authenticated()
        }
        .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.NEVER) }
        .cors { it.disable() }
        .csrf { it.disable() }
        // .formLogin { it.disable() }
        .headers { it.cacheControl { it.disable() } }
        .addFilterBefore(
            object : OncePerRequestFilter()
            {
                override fun doFilterInternal(
                    request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain
                )
                {
                }
            }, UsernamePasswordAuthenticationFilter::class.java
        )
        .build()!!
}
