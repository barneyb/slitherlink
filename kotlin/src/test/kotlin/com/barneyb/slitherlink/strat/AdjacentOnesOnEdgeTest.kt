package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.Move
import com.barneyb.slitherlink.OFF
import com.barneyb.slitherlink.io.krazydad
import org.junit.Assert.assertEquals
import org.junit.Test

class AdjacentOnesOnEdgeTest {

    @Test
    fun d() {
        /* · │ ─ ×
        ·   ·   ·
          1 × 1
        · × · × ·
          1 × 1
        ·   ·   ·
         */
        val p = krazydad("1111")
        println(p)
        assertEquals(
            setOf(
                Move(p.edge(1, 2), OFF),
                Move(p.edge(2, 3), OFF),
                Move(p.edge(3, 2), OFF),
                Move(p.edge(2, 1), OFF)
            ),
            adjacentOnesOnEdge(p).toSet()
        )
    }
}
