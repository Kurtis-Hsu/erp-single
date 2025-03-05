package com.vireosci.erp

import org.junit.jupiter.api.Test
import java.util.UUID

class Generator {
    @Test fun generateRandomUUIDStr() = sign(UUID.randomUUID().toString().replace("-", ""))
}