package com.barneyb.slitherlink

import org.junit.Test

/**
 *
 *
 * @author barneyb
 */
class PuzzleTest {

    @Test
    void d() {
        def p = new Puzzle(2, 2)
            .cell(0, 1, 3)
            .cell(1, 0, 1)
            .edge(0, 0, Puzzle.Dir.NORTH, Puzzle.EdgeState.ON)
            .edge(0, 0, Puzzle.Dir.WEST, Puzzle.EdgeState.ON)
            .edge(0, 0, Puzzle.Dir.SOUTH, Puzzle.EdgeState.ON)
            .edge(0, 1, Puzzle.Dir.NORTH, Puzzle.EdgeState.ON)
            .edge(0, 1, Puzzle.Dir.EAST, Puzzle.EdgeState.ON)
            .edge(0, 1, Puzzle.Dir.SOUTH, Puzzle.EdgeState.ON)
            .edge(2, 0, Puzzle.Dir.NORTH, Puzzle.EdgeState.OFF) // silly
            .edge(1, 0, Puzzle.Dir.WEST, Puzzle.EdgeState.OFF)
            .edge(1, 1, Puzzle.Dir.WEST, Puzzle.EdgeState.OFF) // silly
        println p
        // this looks sorta janky.
        assert "·───·───·\n" +
               "│     3 │\n" +
               "·───·───·\n" +
               "× 1 ×    \n" +
               "· × ·   ·\n" == p.toString()
    }

}
