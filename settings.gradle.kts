rootProject.name = "vireosci-erp-single"

dependencyResolutionManagement {
    versionCatalogs {
        // Mybatis-plus
        val mpGroup = "com.baomidou"
        create("libs") {
            // versions
            version("kotlin", "2.1.10")
            version("spring-boot", "3.4.3")
            version("spring-boot-dependency-management", "1.1.7")
            version("mybatis-plus", "3.5.10.1")
            version("jasypt", "3.0.5")

            // Mybatis-plus
            library("mp-core", mpGroup, "mybatis-plus-spring-boot3-starter").versionRef("mybatis-plus")
            library("mp-parser", mpGroup, "mybatis-plus-jsqlparser").versionRef("mybatis-plus")
            bundle("mp", listOf("mp-core", "mp-parser"))

            // Jasypt encryption/decryption tool
            library("jasypt", "com.github.ulisesbocchio", "jasypt-spring-boot-starter").versionRef("jasypt")

            // plugins
            plugin("kotlin", "org.jetbrains.kotlin.jvm").versionRef("kotlin")
            plugin("kotlin-spring", "org.jetbrains.kotlin.plugin.spring").versionRef("kotlin")
            plugin("spring-boot", "org.springframework.boot").versionRef("spring-boot")
            // spring-boot-dependency-management
            plugin("spring-boot-dependency-management", "io.spring.dependency-management")
                .versionRef("spring-boot-dependency-management")
        }
    }
}
