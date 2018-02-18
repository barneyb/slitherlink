package com.barneyb.slitherlink

import org.junit.Test

import static junit.framework.Assert.assertEquals

/**
 *
 *
 * @author barneyb
 */
class ThreeInCornerTest {

    @Test
    void d() {
        def p = new Puzzle(2, 2)
            .cell(0, 0, 3)
        assertEquals(
            new ThreeInCorner().nextMove(p),
            new MoveImpl(new EdgeCoord(0, 0, Dir.NORTH), EdgeState.ON)
        )
        p.edge(0, 0, Dir.NORTH, EdgeState.ON)
        assertEquals(
            new ThreeInCorner().nextMove(p),
            new MoveImpl(new EdgeCoord(0, 0, Dir.WEST), EdgeState.ON)
        )
    }

}
