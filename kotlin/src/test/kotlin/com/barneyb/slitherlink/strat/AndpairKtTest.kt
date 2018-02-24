package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.Move
import com.barneyb.slitherlink.OFF
import com.barneyb.slitherlink.ON
import com.barneyb.slitherlink.io.krazydad
import org.junit.Assert
import org.junit.Test
import kotlin.test.assertEquals

class AndpairKtTest {

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
        p.state(1, 4, OFF)
        p.state(2, 5, OFF)
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
        p.state(1, 4, OFF)
        p.state(2, 5, OFF)
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
        p.state(1, 2, OFF)
        p.state(1, 4, OFF)
        p.state(2, 5, OFF)
        p.state(4, 5, ON)
        Assert.assertEquals(
            setOf(
                Move(p.edge(2, 1), ON),
                Move(p.edge(5, 4), OFF)
            ),
            twoWithEdgePairHasWhiskers(p).toSet()
        )
    }

    @Test
    fun twoWithEdgePairRepelsAtOtherCorner() {
        /* · │ ─ ×
        ·   ·   ·   ·
                ×
        ·   ·   · × ·
              2
        ·───·   ·   ·

        ·   ·   ·   ·
         */
        val p = krazydad("....2....")
        p.state(1, 4, OFF)
        p.state(2, 5, OFF)
        p.state(4, 1, ON)
        Assert.assertEquals(
            setOf(
                Move(p.edge(5, 2), ON)
            ),
            twoWithEdgePairRepelsAtOtherCorner(p).toSet()
        )
    }

    @Test
    fun twoWithEdgePairAndNoConstraintsPullsAtCorner() {
        /* · │ ─ ×
        ·   ·   ·   ·
              ?   x │
        ·   ·   ·   ·
              x   2
        ·   ·───·   ·
                    ×
        ·   ·   ·   ·
         */
        val p = krazydad(".....2...")
        p.state(1, 6, ON)
        p.state(4, 3, ON)
        p.state(5, 6, OFF)
        println(p)
        assertEquals(
            setOf(
                Move(p.edge(1, 4), ON),
                Move(p.edge(2, 3), ON),
                Move(p.edge(4, 5), ON),
                Move(p.edge(3, 6), ON)
            ),
            twoWithEdgePairAndNoConstraintsPullsAtCorner(p).toSet()
        )
    }

}
