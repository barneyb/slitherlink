package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.Move
import com.barneyb.slitherlink.OFF
import com.barneyb.slitherlink.ON
import com.barneyb.slitherlink.io.krazydad
import org.junit.Assert
import org.junit.Test

class AndpairTest {

    @Test
    fun threeWithEdgePair() {
        /* · │ ─ ×
        ·   ·   ·   ·
                ×
        ·   ·   · × ·
              3
        ·   ·   ·   ·

        ·   ·   ·   ·
         */
        val p = krazydad("....3....")
        p.edge(1, 4).state = OFF
        p.edge(2, 5).state = OFF
        Assert.assertEquals(
            setOf(
                Move(p.edge(2, 3), ON),
                Move(p.edge(3, 4), ON)
            ),
            threeWithEdgePair(p).toSet()
        )
    }

    @Test
    fun oneWithEdgePair() {
        /* · │ ─ ×
        ·   ·   ·   ·
                ×
        ·   ·   · × ·
              1
        ·   ·   ·   ·

        ·   ·   ·   ·
         */
        val p = krazydad("....1....")
        p.edge(1, 4).state = OFF
        p.edge(2, 5).state = OFF
        Assert.assertEquals(
            setOf(
                Move(p.edge(2, 3), OFF),
                Move(p.edge(3, 4), OFF)
            ),
            oneWithEdgePair(p).toSet()
        )
    }

    @Test
    fun twoWithEdgePairHasWhiskers() {
        /* · │ ─ ×
        ·   ·   ·   ·
            ×   ×
        ·   ·   · × ·
              2
        ·   ·   ·───·

        ·   ·   ·   ·
         */
        val p = krazydad("....2....")
        p.edge(1, 2).state = OFF
        p.edge(1, 4).state = OFF
        p.edge(2, 5).state = OFF
        p.edge(4, 5).state = ON
        Assert.assertEquals(
            setOf(
                Move(p.edge(2, 1), ON),
                Move(p.edge(5, 4), OFF)
            ),
            twoWithEdgePairHasWhiskers(p).toSet()
        )
    }

}
