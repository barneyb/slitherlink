package com.barneyb.slitherlink

import org.junit.Test
/**
 *
 *
 * @author barneyb
 */
class PuzzleTest {

    static Puzzle initial() {
        new Puzzle(2, 2)
            .cell(0, 1, 3)
            .cell(1, 0, 1)
            .cell(1, 1, 1)
    }

    @Test
    void simpleConstructAndPrint() {
        def p = initial()
            .edge(0, 0, Dir.NORTH, EdgeState.ON)
            .edge(0, 0, Dir.WEST, EdgeState.ON)
            .edge(0, 0, Dir.SOUTH, EdgeState.ON)
            .edge(0, 1, Dir.NORTH, EdgeState.ON)
            .edge(0, 1, Dir.EAST, EdgeState.ON)
            .edge(0, 1, Dir.SOUTH, EdgeState.ON)
            .edge(2, 0, Dir.NORTH, EdgeState.OFF) // silly
            .edge(1, 0, Dir.WEST, EdgeState.OFF)
            .edge(1, 1, Dir.WEST, EdgeState.OFF) // silly
        println p
        // this looks sorta janky.
        assert "·───·───·\n" +
               "│     3 │\n" +
               "·───·───·\n" +
               "× 1 × 1  \n" +
               "· × ·   ·\n" == p.toString()
    }

    @Test
    void move() {
        def p = new Puzzle(2, 2)
        assert EdgeState.UNKNOWN == p.edge(0, 0, Dir.NORTH)
        p.move(new MoveImpl(new EdgeCoord(0, 0, Dir.NORTH), EdgeState.ON))
        assert EdgeState.ON == p.edge(0, 0, Dir.NORTH)
    }

}
