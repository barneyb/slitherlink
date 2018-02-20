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
        p.humanCellCoord(0, 1).clue = 3
        p.humanCellCoord(1, 0).clue = 1
        p.humanCellCoord(1, 1).clue = 1
        p
    }

    @Test
    void simpleConstructAndPrint() {
        def p = initial()
        p.humanEdgeCoord(0, 0, Dir.NORTH).state = EdgeState.ON
        p.humanEdgeCoord(0, 0, Dir.WEST).state = EdgeState.ON
        p.humanEdgeCoord(0, 0, Dir.SOUTH).state = EdgeState.ON
        p.humanEdgeCoord(0, 1, Dir.NORTH).state = EdgeState.ON
        p.humanEdgeCoord(0, 1, Dir.EAST).state = EdgeState.ON
        p.humanEdgeCoord(0, 1, Dir.SOUTH).state = EdgeState.ON
        p.humanEdgeCoord(2, 0, Dir.NORTH).state = EdgeState.OFF // silly
        p.humanEdgeCoord(1, 0, Dir.WEST).state = EdgeState.OFF
        p.humanEdgeCoord(1, 1, Dir.WEST).state = EdgeState.OFF // silly
        println p
        // this looks sorta janky.
        assert "·───·───·\n" +
               "│     3 │\n" +
               "·───·───·\n" +
               "× 1 × 1  \n" +
               "· × ·   ·\n" == p.toString()
    }

}
