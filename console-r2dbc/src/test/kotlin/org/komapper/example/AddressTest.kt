package org.komapper.example

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.komapper.core.dsl.Meta

class AddressTest {

    @Test
    fun test() {
        assertNotNull(Meta.address)
    }
}
