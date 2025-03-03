rootProject.name = "vireosci-erp-single"

dependencyResolutionManagement {
    versionCatalogs {
        // Mybatis-plus
        val mpGroup = "com.baomidou"
        create("libs") {
            // Mybatis-plus
            version("mp", "3.5.10.1")
            library("mp-core", mpGroup, "mybatis-plus-spring-boot3-starter").versionRef("mp")
            library("mp-parser", mpGroup, "mybatis-plus-jsqlparser").versionRef("mp")
            bundle("mp", listOf("mp-core", "mp-parser"))

            // plugins
            plugin("kotlin", "org.jetbrains.kotlin.jvm").version("2.1.10") // kotlin
            plugin("kotlin-spring", "org.jetbrains.kotlin.plugin.spring").version("2.1.10") // kotlin-spring
            plugin("spring-boot", "org.springframework.boot").version("3.4.3") // spring-boot
            // spring-boot-dependency-management
            plugin("spring-boot-dependency-management", "io.spring.dependency-management").version("1.1.7")
        }
    }
}
