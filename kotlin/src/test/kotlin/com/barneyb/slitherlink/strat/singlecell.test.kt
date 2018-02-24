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
class SinglecellTest {

    @Test
    fun clueSatisfied() {
        /* · │ ─ ×
        ·   ·   ·

        ·───·   ·
        × 1 ×
        · × ·   ·
         */
        val p = krazydad("..1.")
        p.edge(2, 1).state = ON
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
        p.edge(3, 0).state = OFF
        p.edge(3, 2).state = OFF
        p.edge(4, 1).state = OFF
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
        p.edge(1, 4).state = ON
        p.edge(3, 2).state = ON
        p.edge(4, 5).state = OFF
        p.edge(5, 0).state = OFF
        p.edge(6, 1).state = OFF
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
        p.edge(1, 2).state = ON
        p.edge(4, 5).state = ON
        Assert.assertEquals(
            setOf(
                Move(p.edge(2, 1), OFF),
                Move(p.edge(5, 4), OFF)
            ),
            pinchedTwoMustStay(p).toSet()
        )
    }

}
