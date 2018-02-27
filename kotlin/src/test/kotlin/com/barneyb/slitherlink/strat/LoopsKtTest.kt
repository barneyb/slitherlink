package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.Move
import com.barneyb.slitherlink.OFF
import com.barneyb.slitherlink.ON
import com.barneyb.slitherlink.assertStrategy
import com.barneyb.slitherlink.io.krazydad
import org.junit.Assert
import org.junit.Test

class LoopsKtTest {

    @Test
    fun singleLoop_noPrematureClose() {
        /* · │ ─ ×
        ·───·   ·   ·
        │   │
        ·   ·   ·───·
        │     2     │
        ·───·   ·   ·
                │   │
        ·   ·   ·───·
         */
        val p = krazydad("....2....")
        p.state(0, 1, ON)
        p.state(1, 0, ON)
        p.state(1, 2, ON)
        p.state(3, 0, ON)
        p.state(4, 1, ON)

        p.state(2, 5, ON)
        p.state(3, 6, ON)
        p.state(5, 4, ON)
        p.state(5, 6, ON)
        p.state(6, 5, ON)
        Assert.assertEquals(
            setOf(
                Move(p.edge(3, 2), OFF),
                Move(p.edge(3, 4), OFF)
            ),
            singleLoop(p).toSet()
        )
    }

    @Test
    fun singleLoop_toWin() {
        /* · │ ─ ×
        ·───·   ·   ·
        │   │
        ·   ·───·───·
        │           │
        ·───·   ·   ·
                │   │
        ·   ·   ·───·
         */
        val p = krazydad(".........")
        p.state(0, 1, ON)
        p.state(1, 0, ON)
        p.state(1, 2, ON)
        p.state(3, 0, ON)
        p.state(4, 1, ON)

        p.state(2, 3, ON)

        p.state(2, 5, ON)
        p.state(3, 6, ON)
        p.state(5, 4, ON)
        p.state(5, 6, ON)
        p.state(6, 5, ON)
        Assert.assertEquals(
            setOf(
                Move(p.edge(4, 3), ON)
            ),
            singleLoop(p).toSet()
        )
    }

    @Test
    fun singleLoop_cantWinYet() {
        /* · │ ─ ×
        ·───·   ·   ·
        │   │
        ·   ·   ·   ·
        │         2
        ·───·   ·   ·

        ·   ·   ·   ·
         */
        val p = krazydad(".....2...")
        p.state(0, 1, ON)
        p.state(1, 0, ON)
        p.state(1, 2, ON)
        p.state(3, 0, ON)
        p.state(4, 1, ON)

        Assert.assertEquals(
            setOf(
                Move(p.edge(3, 2), OFF)
            ),
            singleLoop(p).toSet()
        )
    }

    @org.junit.Ignore("not ready yet")
    @Test
    fun singleLoopWithAdditionalRulesApplied() {
        assertStrategy(
            ::singleLoop,
            """
                +---+   +   +
                | 3       2
                +   +---+   +
                    x 3 |
                +   +---+   +
                x 1
                + x +   +   +
            """, """
                +---+   +   +
                | 3       2
                +   +---+   +
                    x 3 |
                + x +---+   +
                x 1
                + x +   +   +
            """
        )
    }

}
