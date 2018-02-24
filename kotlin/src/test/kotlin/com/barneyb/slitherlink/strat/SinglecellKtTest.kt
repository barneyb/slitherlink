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
class SinglecellKtTest {

    @Test
    fun clueSatisfied() {
        /* · │ ─ ×
        ·   ·   ·

        ·───·   ·
        × 1 ×
        · × ·   ·
         */
        val p = krazydad("..1.")
        p.state(2, 1, ON)
        Assert.assertEquals(
            setOf(
                Move(p.edge(3, 0), OFF),
                Move(p.edge(3, 2), OFF),
                Move(p.edge(4, 1), OFF)
            ),
            clueSatisfied(p).toSet()
        )
    }

    @Test
    fun needAllRemaining() {
        /* · │ ─ ×
        ·   ·   ·

        ·   ·   ·
        × 1 ×
        · × ·   ·
         */
        val p = krazydad("..1.")
        p.state(3, 0, OFF)
        p.state(3, 2, OFF)
        p.state(4, 1, OFF)
        Assert.assertEquals(
            setOf(
                Move(p.edge(2, 1), ON)
            ),
            needAllRemaining(p).toSet()
        )
    }

    @Test
    fun reachOneShortOfSatisfiedMustStay() {
        /* · │ ─ ×
        ·   ·   ·   ·
                │
        ·   ·   ·   ·
            │     2
        ·   ·   · × ·
        × 1
        · × ·   ·   ·
         */
        val p = krazydad(".....21..")
        p.state(1, 4, ON)
        p.state(3, 2, ON)
        p.state(4, 5, OFF)
        p.state(5, 0, OFF)
        p.state(6, 1, OFF)
        Assert.assertEquals(
            setOf(
                // one
                Move(p.edge(4, 3), OFF),
                // two
                Move(p.edge(2, 3), OFF),
                Move(p.edge(3, 6), ON)
            ),
            reachOneShortOfSatisfiedMustStay(p).toSet()
        )
    }

    @Test
    fun pinchedTwoMustStay() {
        /* · │ ─ ×
        ·   ·   ·   ·
            │
        ·   ·   ·   ·
              2
        ·   ·   ·───·

        ·   ·   ·   ·
         */
        val p = krazydad("....2....")
        p.state(1, 2, ON)
        p.state(4, 5, ON)
        Assert.assertEquals(
            setOf(
                Move(p.edge(2, 1), OFF),
                Move(p.edge(5, 4), OFF)
            ),
            pinchedTwoMustStay(p).toSet()
        )
    }

}
