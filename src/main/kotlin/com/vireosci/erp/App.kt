package com.vireosci.erp

import com.vireosci.erp.util.LOCAL_IP
import org.slf4j.LoggerFactory
import org.springframework.boot.SpringApplication
import org.springframework.boot.SpringApplicationRunListener
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ConfigurableApplicationContext
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@SpringBootApplication
class App

// 启动监听器
class BootListener(app: SpringApplication) : SpringApplicationRunListener
{
    private val logger = LoggerFactory.getLogger(app.mainApplicationClass)

    override fun ready(context: ConfigurableApplicationContext, timeTaken: Duration)
    {
        val env = context.environment

        val projectName = env.getProperty("spring.application.name") ?: "project"
        val startTime = DateTimeFormatter.ofPattern(DEFAULT_DATETIME_FORMAT).format(LocalDateTime.now())
        val protocol = env.getProperty("server.protocol") ?: "http"
        val port = env.getProperty("server.port") ?: "8080"

        logger.info(
            """

            ---------------------------------------------------------------------------
                (♥◠‿◠)ﾉﾞ [ $projectName ] runs successfully!  ლ(´ڡ`ლ)ﾞ
                Started time: $startTime
                         Url: $protocol://${LOCAL_IP}:$port
                  Components: ${context.beanDefinitionCount}
                 System info: ${System.getProperty("os.name")} ${System.getProperty("os.arch")}
                   Java info: ${System.getProperty("java.vm.name")} v${System.getProperty("java.version")}
                 Kotlin info: v${KotlinVersion.CURRENT}
            ---------------------------------------------------------------------------
            
            """.trimIndent()
        )
    }
}

// @formatter:off
fun main(args: Array<String>) { runApplication<App>(*args) }
