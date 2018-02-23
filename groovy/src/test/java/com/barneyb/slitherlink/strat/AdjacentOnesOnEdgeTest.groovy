package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.Dir
import com.barneyb.slitherlink.MoveImpl
import com.barneyb.slitherlink.Puzzle
import com.barneyb.slitherlink.io.KrazyDadSource
import org.junit.Test

import static org.junit.Assert.*

/**
 *
 *
 * @author barneyb
 */
class AdjacentOnesOnEdgeTest {

    @Test
    void onesOnEdge() {
        def p = new KrazyDadSource(2, 4, ".11.....").load()
        assertEquals(
            new AdjacentOnesOnEdge().nextMove(p),
            new MoveImpl(p.humanEdgeCoord(0, 2, Dir.WEST), Puzzle.OFF)
        )
    }

}
