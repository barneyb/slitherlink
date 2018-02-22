package com.barneyb.slitherlink

import org.junit.Test

class PuzzleTest {

    @Test
    fun simpleConstructAndPrint() {
        val p = twoByTwo()
        p.edge(0, 1).state = ON
        p.edge(0, 3).state = ON
        p.edge(1, 0).state = ON
        p.edge(1, 4).state = ON
        p.edge(2, 1).state = ON
        p.edge(2, 3).state = ON
        p.edge(3, 0).state = OFF
        p.edge(3, 2).state = OFF
        p.edge(4, 1).state = OFF
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
    p.cell(1, 3).clue = THREE
    p.cell(3, 1).clue = ONE
    p.cell(3, 3).clue = ONE
    return p
}
