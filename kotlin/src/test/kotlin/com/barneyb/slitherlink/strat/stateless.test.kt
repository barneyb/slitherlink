package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.Move
import com.barneyb.slitherlink.OFF
import com.barneyb.slitherlink.ON
import com.barneyb.slitherlink.io.krazydad
import org.junit.Assert
import org.junit.Test

class StatelessTest {

    @Test
    fun adjacentThrees() {
        /* · │ ─ ×
        ·───·   ·   ·   ·
          3         ×
        ·───· × ·   ·   ·
          3     │ 3 │ 3 │
        ·───·   ·   ·   ·
         */
        val p = krazydad(2, 4, "3...3.33")
        Assert.assertEquals(
            setOf(
                Move(p.edge(0, 1), ON),
                Move(p.edge(2, 1), ON),
                Move(p.edge(4, 1), ON),
                Move(p.edge(2, 3), OFF),

                Move(p.edge(3, 4), ON),
                Move(p.edge(3, 6), ON),
                Move(p.edge(3, 8), ON),
                Move(p.edge(1, 6), OFF)
            ),
            adjacentThrees(p).toSet()
        )
    }

    @Test
    fun kittyCornerThrees() {
        /* · │ ─ ×
        ·───·   ·───·
        │ 3       3 │
        ·   ·   ·   ·
            │ 3 │
        ·   ·───·   ·
         */
        val p = krazydad(2, 3, "3.3.3.")
        Assert.assertEquals(
            setOf(
                Move(p.edge(0, 1), ON),
                Move(p.edge(0, 5), ON),
                Move(p.edge(1, 0), ON),
                Move(p.edge(1, 6), ON),
                Move(p.edge(3, 2), ON),
                Move(p.edge(3, 4), ON),
                Move(p.edge(4, 3), ON)
            ),
            kittyCornerThrees(p).toSet()
        )
    }

    @Test
    fun adjacentOnesOnEdge() {
        /* · │ ─ ×
        ·   ·   ·
          1 × 1
        · × · × ·
          1 × 1
        ·   ·   ·
         */
        val p = krazydad("1111")
        Assert.assertEquals(
            setOf(
                Move(p.edge(1, 2), OFF),
                Move(p.edge(2, 3), OFF),
                Move(p.edge(3, 2), OFF),
                Move(p.edge(2, 1), OFF)
            ),
            adjacentOnesOnEdge(p).toSet()
        )
    }

    @Test
    fun threeInCorner() {
        /* · │ ─ ×
        ·───·   ·   ·───·
        │ 3           3 │
        ·   ·   ·   ·   ·

        ·   ·   ·   ·   ·

        ·   ·   ·   ·   ·
        │ 3           3 │
        ·───·   ·   ·───·
         */
        val p = krazydad("3..3........3..3")
        Assert.assertEquals(
            setOf(
                Move(p.edge(0, 1), ON),
                Move(p.edge(1, 0), ON),
                Move(p.edge(0, 7), ON),
                Move(p.edge(1, 8), ON),
                Move(p.edge(7, 0), ON),
                Move(p.edge(8, 1), ON),
                Move(p.edge(7, 8), ON),
                Move(p.edge(8, 7), ON)
            ),
            threeInCorner(p).toSet()
        )
    }

    @Test
    fun oneInCorner() {
        /* · │ ─ ×
        · × ·   ·   · × ·
        × 1           1 ×
        ·   ·   ·   ·   ·

        ·   ·   ·   ·   ·

        ·   ·   ·   ·   ·
        × 1           1 ×
        · × ·   ·   · × ·
         */
        val p = krazydad("1..1........1..1")
        Assert.assertEquals(
            setOf(
                Move(p.edge(0, 1), OFF),
                Move(p.edge(1, 0), OFF),
                Move(p.edge(0, 7), OFF),
                Move(p.edge(1, 8), OFF),
                Move(p.edge(7, 0), OFF),
                Move(p.edge(8, 1), OFF),
                Move(p.edge(7, 8), OFF),
                Move(p.edge(8, 7), OFF)
            ),
            oneInCorner(p).toSet()
        )
    }

    @Test
    fun twoInCorner() {
        /* · │ ─ ×
        ·   ·───·───·   ·
          2           2
        ·   ·   ·   ·   ·
        │               │
        ·   ·   ·   ·   ·
        │               │
        ·   ·   ·   ·   ·
          2           2
        ·   ·───·───·   ·
         */
        val p = krazydad("2..2........2..2")
        Assert.assertEquals(
            setOf(
                Move(p.edge(0, 3), ON),
                Move(p.edge(0, 5), ON),
                Move(p.edge(3, 0), ON),
                Move(p.edge(3, 8), ON),
                Move(p.edge(5, 0), ON),
                Move(p.edge(5, 8), ON),
                Move(p.edge(8, 3), ON),
                Move(p.edge(8, 5), ON)
            ),
            twoInCorner(p).toSet()
        )
    }

}
