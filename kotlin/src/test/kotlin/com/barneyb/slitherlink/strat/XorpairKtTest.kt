package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.Move
import com.barneyb.slitherlink.OFF
import com.barneyb.slitherlink.ON
import com.barneyb.slitherlink.assertStrategy
import com.barneyb.slitherlink.io.krazydad
import org.junit.Assert
import org.junit.Test

class XorpairKtTest {

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
        p.state(4, 1, OFF)
        p.state(5, 2, ON)
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
        p.state(2, 3, OFF)
        p.state(3, 4, OFF)
        p.state(4, 1, OFF)
        Assert.assertEquals(
            setOf(
                Move(p.edge(5, 2), ON)
            ),
            singleXorPairEgress(p).toSet()
        )
    }

    @Test
    fun threeTouchedByXorPair() {
        assertStrategy(
            ::threeTouchedByXorPair,
            """
                ·   ·   ·
                  3
                ·   ·   ·
                      1 ×
                ·   · × ·
            """, """
                ·---·   ·
                | 3
                ·   ·   ·
                      1 ×
                ·   · × ·
            """
        )
        assertStrategy(
            ::threeTouchedByXorPair,
            """
            ·   ·   ·   ·   ·
                  3
            ·   ·   ·   ·   ·
                      2
            ·   · × ·   ·   ·
                    ×
            ·   ·   ·   ·   ·
            """, """
            ·   ·---·   ·   ·
                | 3
            ·   ·   ·   ·   ·
                      2
            ·   · × ·   ·   ·
                    ×
            ·   ·   ·   ·   ·
            """
        )
    }

}
