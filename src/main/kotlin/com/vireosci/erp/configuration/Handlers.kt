package com.vireosci.erp.configuration

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler
import com.vireosci.erp.util.log
import org.apache.ibatis.reflection.MetaObject
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.time.LocalDateTime
import java.util.concurrent.BlockingDeque
import java.util.concurrent.LinkedBlockingDeque

// 处理器

/**
 * Mybatis-plus 处理器
 *
 * 实现在逻辑删除的同时设置删除时间
 */
@Component
class DeleteTimeHandler : MetaObjectHandler
{
    override fun insertFill(meta: MetaObject)
    {
    }

    override fun updateFill(meta: MetaObject)
    {
        // 逻辑删除时自动填充删除时间
        if (meta.hasGetter("deleted") && getFieldValByName("deleted", meta) != null)
            strictUpdateFill<LocalDateTime, LocalDateTime>(
                meta, "deletedTime", { LocalDateTime.now() }, LocalDateTime::class.java
            )
    }
}

class GlobalExceptionHandler : ResponseEntityExceptionHandler()
{
    /**
     * 兜底方法，处理所有未处理异常
     */
    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun unhandled(e: Exception) = unhandledQueue.offerLast(e)

    override fun handleExceptionInternal(
        ex: Exception, body: Any?, headers: HttpHeaders, statusCode: HttpStatusCode, request: WebRequest
    ): ResponseEntity<Any>? =
        super.handleExceptionInternal(ex, body, headers, statusCode, request).apply {
            log.debug("\u001B[33m-- exception: \u001B[0m\n${ex.message}")
        }

    private val unhandledQueue: BlockingDeque<Exception> = LinkedBlockingDeque()

    // @formatter:off
    init
    {
        Thread.ofVirtual()
            .name("unhandled-exception-handler")
            .start {
                while (true)
                    log.error("\u001B[31m-- unhandled exception: \u001B[0m\n${unhandledQueue.takeFirst().stackTraceToString()}")
            }
    }
}
