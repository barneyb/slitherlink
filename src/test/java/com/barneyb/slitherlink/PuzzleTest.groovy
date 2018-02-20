package com.barneyb.slitherlink

import org.junit.Test
/**
 *
 *
 * @author barneyb
 */
class PuzzleTest {

    static Puzzle initial() {
        def p = new Puzzle(2, 2)
        p.cellCoord(0, 1).clue = 3
        p.cellCoord(1, 0).clue = 1
        p.cellCoord(1, 1).clue = 1
        p
    }

    @Test
    void simpleConstructAndPrint() {
        def p = initial()
        p.edgeCoord(0, 0, Dir.NORTH).state = EdgeState.ON
        p.edgeCoord(0, 0, Dir.WEST).state = EdgeState.ON
        p.edgeCoord(0, 0, Dir.SOUTH).state = EdgeState.ON
        p.edgeCoord(0, 1, Dir.NORTH).state = EdgeState.ON
        p.edgeCoord(0, 1, Dir.EAST).state = EdgeState.ON
        p.edgeCoord(0, 1, Dir.SOUTH).state = EdgeState.ON
        p.edgeCoord(2, 0, Dir.NORTH).state = EdgeState.OFF // silly
        p.edgeCoord(1, 0, Dir.WEST).state = EdgeState.OFF
        p.edgeCoord(1, 1, Dir.WEST).state = EdgeState.OFF // silly
        println p
        // this looks sorta janky.
        assert "·───·───·\n" +
               "│     3 │\n" +
               "·───·───·\n" +
               "× 1 × 1  \n" +
               "· × ·   ·\n" == p.toString()
    }

}
