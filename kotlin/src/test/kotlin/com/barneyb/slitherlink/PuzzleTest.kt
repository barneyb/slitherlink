package com.barneyb.slitherlink

import org.junit.Test

class PuzzleTest {

    @Test
    fun simpleConstructAndPrint() {
        val p = twoByTwo()
        p.state(0, 1, ON)
        p.state(0, 3, ON)
        p.state(1, 0, ON)
        p.state(1, 4, ON)
        p.state(2, 1, ON)
        p.state(2, 3, ON)
        p.state(3, 0, OFF)
        p.state(3, 2, OFF)
        p.state(4, 1, OFF)
        println(p)

        // this looks sorta janky.
        //@formatter:off
        assert("·───·───·\n" +
               "│     3 │\n" +
               "·───·───·\n" +
               "× 1 × 1  \n" +
               "· × ·   ·\n" == p.toString())
        //@formatter:on
    }
}

fun twoByTwo(): Puzzle {
    val p = Puzzle(2, 2)
    p.clue(1, 3, THREE)
    p.clue(3, 1, ONE)
    p.clue(3, 3, ONE)
    return p
}
