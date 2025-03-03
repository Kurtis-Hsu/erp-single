package com.vireosci.erp

import com.baomidou.mybatisplus.annotation.DbType
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import jakarta.validation.Validation
import jakarta.validation.Validator
import org.mybatis.spring.annotation.MapperScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

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
 * 数据库相关配置
 */
@Configuration
@MapperScan("com.vireosci.erp.mapper")
class DatabaseConfiguration
{
    @Bean fun mybatisPlusInterceptor() = MybatisPlusInterceptor().also {
        it.addInnerInterceptor(PaginationInnerInterceptor(DbType.MYSQL))
    }
}