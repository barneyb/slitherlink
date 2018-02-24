package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.Move
import com.barneyb.slitherlink.OFF
import com.barneyb.slitherlink.ON
import com.barneyb.slitherlink.io.krazydad
import org.junit.Assert
import org.junit.Test

class XorpairTest {

    @Test
    fun forcedToOne() {
        /* · │ ─ ×
        ·   ·   ·   ·

        ·   · × ·   ·
              1 ×
        · × ·   ·   ·
            │
        ·   ·   ·   ·
         */
        val p = krazydad("....1....")
        p.edge(4, 1).state = OFF
        p.edge(5, 2).state = ON
        Assert.assertEquals(
            setOf(
                Move(p.edge(2, 3), OFF),
                Move(p.edge(3, 4), OFF)
            ),
            forcedToOne(p).toSet()
        )
    }

    @Test
    fun singleXorPairEgress() {
        /* · │ ─ ×
        ·   ·   ·   ·

        ·   · × ·   ·
              1 ×
        · × ·   ·   ·
            │
        ·   ·   ·   ·
         */
        val p = krazydad("....1....")
        p.edge(2, 3).state = OFF
        p.edge(3, 4).state = OFF
        p.edge(4, 1).state = OFF
        Assert.assertEquals(
            setOf(
                Move(p.edge(5, 2), ON)
            ),
            singleXorPairEgress(p).toSet()
        )
    }

    @Test
    fun threeTouchedByXorPair() {
        /* · │ ─ ×
        ·───·   ·
        │ 3
        ·   ·   ·
              1 ×
        ·   · × ·
         */
        val p = krazydad("3..1")
        p.edge(3, 4).state = OFF
        p.edge(4, 3).state = OFF
        Assert.assertEquals(
            setOf(
                Move(p.edge(0, 1), ON),
                Move(p.edge(1, 0), ON)
            ),
            threeTouchedByXorPair(p).toSet()
        )
    }

}
