package com.vireosci.erp

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import java.net.InetAddress

// 通用工具

/** 获取当前类的日志记录器 */
inline val <reified T> T.log get() = LoggerFactory.getLogger(T::class.java)

// IP

/** 本机IP */
val LOCAL_IP: String get() = InetAddress.getLocalHost().hostAddress

private val ip_headers =
    mutableListOf<String?>(
        "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR", "X-Real-IP"
    )

/** 远程请求的IP */
val HttpServletRequest.ip: String
    get()
    {
        getHeader("X-Forwarded-For").let { if (it.isValidIp) return if (it.contains(",")) it.split(",")[0] else it }

        ip_headers.forEach { getHeader(it)?.let { if (it.isValidIp) return it } }

        return if (remoteAddr.isValidIp) remoteAddr else "127.0.0.1"
    }

private val String?.isValidIp get() = StringUtils.hasText(this) && !"unknown".equals(this, ignoreCase = true)

/**
 * Spring 工具
 */
@Component
class SpringUtil @Autowired constructor(
    webApplicationContext: ApplicationContext,
    /**
     * 请求对象
     */
    val request: HttpServletRequest,
    /**
     * 响应对象
     */
    val response: HttpServletResponse
) : ApplicationContext by webApplicationContext
