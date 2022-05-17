package com.geekbrains.potd

import org.junit.Test
import org.junit.Assert.*

class MiscTests {

    @Test
    fun joinToString() {
        val lines = arrayListOf(
            "hello,",
            "friends!",
            "how's life?",
        )
        val joined = lines.joinToString(separator = "", transform = null)
        assertEquals(joined, "hello,friends!how's life?")
    }

}