package com.barneyb.slitherlink.io

import com.barneyb.slitherlink.BLANK
import com.barneyb.slitherlink.ONE
import com.barneyb.slitherlink.THREE
import com.barneyb.slitherlink.TWO
import com.barneyb.slitherlink.ZERO
import org.junit.Test
import kotlin.test.assertEquals

class KrazydadKtTest {

    @Test
    fun cornerOfSomething() {
        val p = krazydad("2223..02....33..")
        println(p)
        assertEquals(TWO, p.humanCell(0, 0).clue)
        assertEquals(TWO, p.humanCell(0, 1).clue)
        assertEquals(TWO, p.humanCell(0, 2).clue)
        assertEquals(THREE, p.humanCell(0, 3).clue)
        assertEquals(BLANK, p.humanCell(1, 0).clue)
        assertEquals(BLANK, p.humanCell(1, 1).clue)
        assertEquals(ZERO, p.humanCell(1, 2).clue)
        assertEquals(TWO, p.humanCell(1, 3).clue)
        assertEquals(BLANK, p.humanCell(2, 0).clue)
        assertEquals(BLANK, p.humanCell(2, 1).clue)
        assertEquals(BLANK, p.humanCell(2, 2).clue)
        assertEquals(BLANK, p.humanCell(2, 3).clue)
        assertEquals(THREE, p.humanCell(3, 0).clue)
        assertEquals(THREE, p.humanCell(3, 1).clue)
        assertEquals(BLANK, p.humanCell(3, 2).clue)
        assertEquals(BLANK, p.humanCell(3, 3).clue)
    }

    @Test
    fun twoByFour() {
        val p = krazydad(2, 4, ".11.....")
        println(p)
        assertEquals(BLANK, p.humanCell(0, 0).clue)
        assertEquals(ONE, p.humanCell(0, 1).clue)
        assertEquals(ONE, p.humanCell(0, 2).clue)
        assertEquals(BLANK, p.humanCell(0, 3).clue)
        assertEquals(BLANK, p.humanCell(1, 0).clue)
        assertEquals(BLANK, p.humanCell(1, 1).clue)
        assertEquals(BLANK, p.humanCell(1, 2).clue)
        assertEquals(BLANK, p.humanCell(1, 3).clue)
    }

    @Test
    fun fourByTwo() {
        val p = krazydad(4, 2, ".11.....")
        println(p)
        assertEquals(BLANK, p.humanCell(0, 0).clue)
        assertEquals(ONE, p.humanCell(0, 1).clue)
        assertEquals(ONE, p.humanCell(1, 0).clue)
        assertEquals(BLANK, p.humanCell(1, 1).clue)
        assertEquals(BLANK, p.humanCell(2, 0).clue)
        assertEquals(BLANK, p.humanCell(2, 1).clue)
        assertEquals(BLANK, p.humanCell(3, 0).clue)
        assertEquals(BLANK, p.humanCell(3, 1).clue)
    }

}
