package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.Move
import com.barneyb.slitherlink.OFF
import com.barneyb.slitherlink.ON
import com.barneyb.slitherlink.io.krazydad
import org.junit.Assert
import org.junit.Ignore
import org.junit.Test

class LoopsTest {

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
        p.edge(0, 1).state = ON
        p.edge(1, 0).state = ON
        p.edge(1, 2).state = ON
        p.edge(3, 0).state = ON
        p.edge(4, 1).state = ON

        p.edge(2, 5).state = ON
        p.edge(3, 6).state = ON
        p.edge(5, 4).state = ON
        p.edge(5, 6).state = ON
        p.edge(6, 5).state = ON
        Assert.assertEquals(
            setOf(
                Move(p.edge(3, 2), OFF),
                Move(p.edge(3, 4), OFF)
            ),
            singleLoop(p).toSet()
        )
    }

    @Ignore("not yet supported")
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
        p.edge(0, 1).state = ON
        p.edge(1, 0).state = ON
        p.edge(1, 2).state = ON
        p.edge(3, 0).state = ON
        p.edge(4, 1).state = ON

        p.edge(2, 3).state = ON

        p.edge(2, 5).state = ON
        p.edge(3, 6).state = ON
        p.edge(5, 4).state = ON
        p.edge(5, 6).state = ON
        p.edge(6, 5).state = ON
        Assert.assertEquals(
            setOf(
                Move(p.edge(4, 3), ON)
            ),
            singleLoop(p).toSet()
        )
    }

    @Ignore("not yet supported")
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
        p.edge(0, 1).state = ON
        p.edge(1, 0).state = ON
        p.edge(1, 2).state = ON
        p.edge(3, 0).state = ON
        p.edge(4, 1).state = ON

        Assert.assertEquals(
            setOf(
                Move(p.edge(3, 2), OFF)
            ),
            singleLoop(p).toSet()
        )
    }

}
