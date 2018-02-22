package com.barneyb.slitherlink.io

import com.barneyb.slitherlink.Move
import com.barneyb.slitherlink.OFF
import com.barneyb.slitherlink.ON
import com.barneyb.slitherlink.strat.forcedToOne
import org.junit.Assert
import org.junit.Test

/**
 *
 *
 * @author barneyb
 */
class ForcedToOneTest {

    @Test
    fun d() {
        /*
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
        println(p)
        Assert.assertEquals(
                setOf(
                        Move(p.edge(2, 3), OFF),
                        Move(p.edge(3, 4), OFF)
                ),
                forcedToOne(p)
        )
    }

}
