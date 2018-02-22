package com.barneyb.slitherlink

import org.junit.Test

class PuzzleTest {

    @Test
    fun simpleConstructAndPrint() {
        val p = twoByTwo()
        p.humanEdge(0, 0, NORTH).state = ON
        p.humanEdge(0, 0, WEST).state = ON
        p.humanEdge(0, 0, SOUTH).state = ON
        p.humanEdge(0, 1, NORTH).state = ON
        p.humanEdge(0, 1, EAST).state = ON
        p.humanEdge(0, 1, SOUTH).state = ON
        p.humanEdge(2, 0, NORTH).state = OFF // silly
        p.humanEdge(1, 0, WEST).state = OFF
        p.humanEdge(1, 1, WEST).state = OFF // silly
        println(p)

        // this looks sorta janky.
        assert("·───·───·\n" +
               "│     3 │\n" +
               "·───·───·\n" +
               "× 1 × 1  \n" +
               "· × ·   ·\n" == p.toString())
    }
}

fun twoByTwo(): Puzzle {
    val p = Puzzle(2, 2)
    p.humanCell(0, 1).clue = 3
    p.humanCell(1, 0).clue = 1
    p.humanCell(1, 1).clue = 1
    return p
}
