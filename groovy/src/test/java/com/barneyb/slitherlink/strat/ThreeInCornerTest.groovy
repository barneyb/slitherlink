package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.Dir
import com.barneyb.slitherlink.MoveImpl
import com.barneyb.slitherlink.Puzzle
import org.junit.Test

import static org.junit.Assert.*

/**
 *
 *
 * @author barneyb
 */
class ThreeInCornerTest {

    @Test
    void d() {
        def p = new Puzzle(2, 2)
        p.humanCellCoord(0, 0).clue = 3
        assertEquals(
            new ThreeInCorner().nextMove(p),
            new MoveImpl(p.humanEdgeCoord(0, 0, Dir.NORTH), Puzzle.ON)
        )
        p.humanEdgeCoord(0, 0, Dir.NORTH).state = Puzzle.ON
        assertEquals(
            new ThreeInCorner().nextMove(p),
            new MoveImpl(p.humanEdgeCoord(0, 0, Dir.WEST), Puzzle.ON)
        )
    }

}
