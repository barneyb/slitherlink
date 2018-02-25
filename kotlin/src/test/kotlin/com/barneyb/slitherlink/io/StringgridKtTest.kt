package com.barneyb.slitherlink.io

import org.junit.Test
import kotlin.test.assertEquals

val KD_7x7_d1_V1_B1_P1_ASCII = """
            . x .---.---. x .---. x . x .
            x 2 | 2 x 2 | 3 | 3 | 1 x   x
            .---. x . x .---. x . x .---.
            |   x   x 0 x 2 x   | 3 |   |
            .---.---. x .---. x .---. x .
            x   x   |   |   |   x   x   |
            .---. x . x . x . x .---. x .
            | 3 | 3 |   |   | 3 | 3 |   |
            . x .---. x . x .---. x . x .
            |   x   x 2 |   x 2 x   |   |
            .---. x .---. x .---.---. x .
            x 3 |   | 2 x   | 2 x 2 x   |
            .---. x . x .---. x .---. x .
            | 3 x   | 2 |   x   | 3 |   |
            .---.---. x .---.---. x .---.
        """.trimIndent()

class StringgridKtTest {

    @Test
    fun unicode() {
        assert_KD_7x7_d1_V1_B1_P1(stringgrid(KD_7x7_d1_V1_B1_P1_RAW))
    }

    @Test
    fun ascii() {
        assert_KD_7x7_d1_V1_B1_P1(stringgrid(KD_7x7_d1_V1_B1_P1_ASCII))
    }

    @Test
    fun serialize() {
        val p = stringgrid(KD_7x7_d1_V1_B1_P1_ASCII)
        assertEquals(KD_7x7_d1_V1_B1_P1_RAW, stringgrid(p))
        assertEquals(KD_7x7_d1_V1_B1_P1_RAW, unicodegrid(p))
        assertEquals(KD_7x7_d1_V1_B1_P1_ASCII, asciigrid(p))
    }

}
