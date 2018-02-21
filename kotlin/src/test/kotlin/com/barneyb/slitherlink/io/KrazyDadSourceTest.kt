package com.barneyb.slitherlink.io

import com.barneyb.slitherlink.BLANK
import org.junit.Test
import kotlin.test.assertEquals

/**
 *
 *
 * @author barneyb
 */
class KrazyDadSourceTest {

    @Test
    fun cornerOfSomething() {
        val p = krazydad("2223..02....33..")
        println(p)
        assertEquals(2, p.humanCell(0, 0).clue)
        assertEquals(2, p.humanCell(0, 1).clue)
        assertEquals(2, p.humanCell(0, 2).clue)
        assertEquals(3, p.humanCell(0, 3).clue)
        assertEquals(BLANK, p.humanCell(1, 0).clue)
        assertEquals(BLANK, p.humanCell(1, 1).clue)
        assertEquals(0, p.humanCell(1, 2).clue)
        assertEquals(2, p.humanCell(1, 3).clue)
        assertEquals(BLANK, p.humanCell(2, 0).clue)
        assertEquals(BLANK, p.humanCell(2, 1).clue)
        assertEquals(BLANK, p.humanCell(2, 2).clue)
        assertEquals(BLANK, p.humanCell(2, 3).clue)
        assertEquals(3, p.humanCell(3, 0).clue)
        assertEquals(3, p.humanCell(3, 1).clue)
        assertEquals(BLANK, p.humanCell(3, 2).clue)
        assertEquals(BLANK, p.humanCell(3, 3).clue)
    }

    @Test
    fun twoByFour() {
        val p = krazydad(2, 4, ".11.....")
        println(p)
        assertEquals(BLANK, p.humanCell(0, 0).clue)
        assertEquals(1, p.humanCell(0, 1).clue)
        assertEquals(1, p.humanCell(0, 2).clue)
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
        assertEquals(1, p.humanCell(0, 1).clue)
        assertEquals(1, p.humanCell(1, 0).clue)
        assertEquals(BLANK, p.humanCell(1, 1).clue)
        assertEquals(BLANK, p.humanCell(2, 0).clue)
        assertEquals(BLANK, p.humanCell(2, 1).clue)
        assertEquals(BLANK, p.humanCell(3, 0).clue)
        assertEquals(BLANK, p.humanCell(3, 1).clue)
    }

}
