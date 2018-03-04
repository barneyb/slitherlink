package com.barneyb.slitherlink.io

import com.barneyb.slitherlink.BLANK
import com.barneyb.slitherlink.assert_KD_7x7_d1_V1_B1_P1
import org.junit.Test
import kotlin.test.assertEquals

private const val KD_7x7_d1_V1_B1_P1_PUZZ =
    "222331...02.3........33..33...2.2..3.2.22.3.2..3."
private const val KD_7x7_d1_V1_B1_P1_SOLVED = "69970A5CE4516DBE233ED26CC7ED"

class KrazydadKtTest {

    @Test
    fun withEdges() {
        // KD_Slitherlink_7x7_d1_V1-B1-P1
        val p = krazydad(KD_7x7_d1_V1_B1_P1_PUZZ, KD_7x7_d1_V1_B1_P1_SOLVED)
        println(p)
        assert_KD_7x7_d1_V1_B1_P1(p)
    }

    @Test
    fun serialize() {
        val p = krazydad(KD_7x7_d1_V1_B1_P1_PUZZ, KD_7x7_d1_V1_B1_P1_SOLVED)
        val (puzz, solved) = krazydad(p)
        assertEquals(KD_7x7_d1_V1_B1_P1_PUZZ, puzz)
        assertEquals(KD_7x7_d1_V1_B1_P1_SOLVED, solved)
    }

    @Test
    fun cornerOfSomething() {
        val p = krazydad("2223..02....33..")
        println(p)
        assertEquals(2, p.humanClue(0, 0))
        assertEquals(2, p.humanClue(0, 1))
        assertEquals(2, p.humanClue(0, 2))
        assertEquals(3, p.humanClue(0, 3))
        assertEquals(BLANK, p.humanClue(1, 0))
        assertEquals(BLANK, p.humanClue(1, 1))
        assertEquals(0, p.humanClue(1, 2))
        assertEquals(2, p.humanClue(1, 3))
        assertEquals(BLANK, p.humanClue(2, 0))
        assertEquals(BLANK, p.humanClue(2, 1))
        assertEquals(BLANK, p.humanClue(2, 2))
        assertEquals(BLANK, p.humanClue(2, 3))
        assertEquals(3, p.humanClue(3, 0))
        assertEquals(3, p.humanClue(3, 1))
        assertEquals(BLANK, p.humanClue(3, 2))
        assertEquals(BLANK, p.humanClue(3, 3))
    }

    @Test
    fun twoByFour() {
        val p = krazydad(2, 4, ".11.....")
        println(p)
        assertEquals(BLANK, p.humanClue(0, 0))
        assertEquals(1, p.humanClue(0, 1))
        assertEquals(1, p.humanClue(0, 2))
        assertEquals(BLANK, p.humanClue(0, 3))
        assertEquals(BLANK, p.humanClue(1, 0))
        assertEquals(BLANK, p.humanClue(1, 1))
        assertEquals(BLANK, p.humanClue(1, 2))
        assertEquals(BLANK, p.humanClue(1, 3))
    }

    @Test
    fun fourByTwo() {
        val p = krazydad(4, 2, ".11.....")
        println(p)
        assertEquals(BLANK, p.humanClue(0, 0))
        assertEquals(1, p.humanClue(0, 1))
        assertEquals(1, p.humanClue(1, 0))
        assertEquals(BLANK, p.humanClue(1, 1))
        assertEquals(BLANK, p.humanClue(2, 0))
        assertEquals(BLANK, p.humanClue(2, 1))
        assertEquals(BLANK, p.humanClue(3, 0))
        assertEquals(BLANK, p.humanClue(3, 1))
    }

    @Test
    fun stripWhitespace() {
        val p = krazydad("01231..22..13210")
        assertEquals(p, krazydad("  01231..22..13210  "))
        assertEquals(p, krazydad("0123\n1..2\n2..1\n3210"))
        assertEquals(p, krazydad(
                """
                    0123
                    1..2
                    2..1
                    3210
                """
        ))
        assertEquals(p, krazydad("0123\t1..2\t2..1\t3210"))
    }

}
