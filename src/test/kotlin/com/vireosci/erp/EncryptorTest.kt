package com.vireosci.erp

import org.jasypt.encryption.StringEncryptor
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class EncryptorTest
{
    @Autowired val encryptor: StringEncryptor? = null

    @Value("\${encrypted.data}")
    var encryptedData: String? = null

    val originData = ""
    @Test fun encryptedData() = sign(encryptor?.encrypt(originData))

    @Test fun decryptedData() = sign(encryptedData)
}