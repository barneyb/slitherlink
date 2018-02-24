package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.Move
import com.barneyb.slitherlink.OFF
import com.barneyb.slitherlink.ON
import com.barneyb.slitherlink.io.krazydad
import org.junit.Assert
import org.junit.Test

/**
 *
 *
 * @author barneyb
 */
class SingledotTest {

    @Test
    fun noBranching() {
        /* · │ ─ ×
        ·   ·   ·
            ×
        ·───· × ·
            │
        ·   ·   ·
         */
        val p = krazydad("....")
        p.edge(2, 1).state = ON
        p.edge(3, 2).state = ON
        Assert.assertEquals(
            setOf(
                Move(p.edge(1, 2), OFF),
                Move(p.edge(2, 3), OFF)
            ),
            noBranching(p).toSet()
        )
    }

    @Test
    fun singleUnknownEdge() {
        /* · │ ─ ×
        ·   ·   ·
            ×
        ·───·   ·
        ×   ×
        ·   ·   ·
         */
        val p = krazydad("....")
        p.edge(1, 2).state = OFF
        p.edge(2, 1).state = ON
        p.edge(3, 0).state = OFF
        p.edge(3, 2).state = OFF
        Assert.assertEquals(
            setOf(
                Move(p.edge(1, 0), ON),
                Move(p.edge(2, 3), ON),
                Move(p.edge(4, 1), OFF)
                // 0,1 and 4,3 will get caught on the next round
            ),
            singleUnknownEdge(p).toSet()
        )
    }

}
