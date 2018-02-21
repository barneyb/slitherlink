package com.barneyb.slitherlink.io

import com.barneyb.slitherlink.Move
import com.barneyb.slitherlink.OFF
import com.barneyb.slitherlink.WEST
import com.barneyb.slitherlink.strat.AdjacentOnesOnEdge
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 *
 *
 * @author barneyb
 */
class AdjacentOnesOnEdgeTest {

    @Test
    fun d() {
        val p = krazydad(2, 4, "11.....1")
        assertEquals(
                listOf(Move(p.humanEdge(0, 1, WEST), OFF)),
                AdjacentOnesOnEdge().nextMoves(p)
        )
    }
}
