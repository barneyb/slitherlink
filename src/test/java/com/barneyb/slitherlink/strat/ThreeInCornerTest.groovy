package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.Dir
import com.barneyb.slitherlink.EdgeState
import com.barneyb.slitherlink.MoveImpl
import com.barneyb.slitherlink.Puzzle
import org.junit.Test

import static junit.framework.Assert.*
/**
 *
 *
 * @author barneyb
 */
class ThreeInCornerTest {

    @Test
    void d() {
        def p = new Puzzle(2, 2)
        p.cellCoord(0, 0).clue = 3
        assertEquals(
            new ThreeInCorner().nextMove(p),
            new MoveImpl(null, p.edgeCoord(0, 0, Dir.NORTH), EdgeState.ON)
        )
        p.edgeCoord(0, 0, Dir.NORTH).state = EdgeState.ON
        assertEquals(
            new ThreeInCorner().nextMove(p),
            new MoveImpl(null, p.edgeCoord(0, 0, Dir.WEST), EdgeState.ON)
        )
    }

}
