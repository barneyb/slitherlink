package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.Move
import com.barneyb.slitherlink.OFF
import com.barneyb.slitherlink.ON
import com.barneyb.slitherlink.Puzzle
import com.barneyb.slitherlink.io.krazydad
import com.barneyb.slitherlink.solve
import org.junit.Ignore
import org.junit.Test

/**
 * taken from https://en.wikipedia.org/wiki/Slitherlink, a simple check that
 * all the strategies listed are implemented somewhere. The test methods are
 * in the same order as the page, as of 2018-02-24.
 */
class WikipediaTest {

    // Corners

    @Test
    fun oneInCorner() {
        val p = krazydad("1...")
        assertMoves(
            p, setOf(
                Move(p.edge(0, 1), OFF),
                Move(p.edge(1, 0), OFF)
            )
        )
    }

    @Test
    fun threeInCorner() {
        val p = krazydad("3...")
        assertMoves(
            p, setOf(
                Move(p.edge(0, 1), ON),
                Move(p.edge(1, 0), ON)
            )
        )
    }

    @Test
    fun twoInCorner() {
        val p = krazydad("2...")
        assertMoves(
            p, setOf(
                Move(p.edge(0, 3), ON),
                Move(p.edge(3, 0), ON)
            )
        )
    }

    // Rules for squares with 1

    @Test
    fun forcedToOneMeansOppositeAreOff() {
        val p = krazydad("...1")
        p.edge(2, 1).state = ON
        p.edge(1, 2).state = OFF
        assertMoves(
            p, setOf(
                Move(p.edge(3, 4), OFF),
                Move(p.edge(4, 3), OFF)
            )
        )
    }

    @Test
    fun reachOneWithOppositeOffMeansTurnIn() {
        val p = krazydad("...1")
        p.edge(2, 1).state = ON
        p.edge(3, 4).state = OFF
        p.edge(4, 3).state = OFF
        assertMoves(
            p, setOf(
                Move(p.edge(1, 2), OFF)
            )
        )
    }

    @Test
    fun kittyCornerOnesWithInnerCornerOffMeansOtherOnesInnerCornerIsOff() {
        val p = krazydad(".....1....1.....")
        p.edge(3, 4).state = OFF
        p.edge(4, 3).state = OFF
        assertMoves(
            p, setOf(
                Move(p.edge(4, 5), OFF),
                Move(p.edge(5, 4), OFF)
            )
        )
    }

    @Test
    fun kittyCornerOnesWithOuterCornerOffMeansOtherOnesOuterCornerIsOff() {
        val p = krazydad(".....1....1.....")
        p.edge(2, 3).state = OFF
        p.edge(3, 2).state = OFF
        assertMoves(
            p, setOf(
                Move(p.edge(5, 6), OFF),
                Move(p.edge(6, 5), OFF)
            )
        )
    }

    // A rule for squares with 2

    @Test
    fun reachTwoWithOneOppositeEdgeOff() {
        val p = krazydad(3, 4, "......2.....")
        p.edge(2, 5).state = OFF
        p.edge(4, 3).state = ON
        assertMoves(
            p, setOf(
                Move(p.edge(3, 6), ON),
                Move(p.edge(5, 4), OFF)
            )
        )
    }

    // Rules for squares with 3

    @Test
    fun threeAdjacentToZeroMakesAHat() {
        val p = krazydad(4, 5, ".......3....0.......")
        assertMoves(
            p, setOf(
                Move(p.edge(4, 3), ON),
                Move(p.edge(3, 4), ON),
                Move(p.edge(2, 5), ON),
                Move(p.edge(3, 6), ON),
                Move(p.edge(4, 7), ON)
            )
        )
    }

    @Test
    fun threesAdjacentCommonAndOutsideEdgesOnAndCommonMustTurn() {
        val p = krazydad(3, 4, ".....33.....")
        assertMoves(
            p, setOf(
                Move(p.edge(3, 2), ON),
                Move(p.edge(3, 4), ON),
                Move(p.edge(3, 6), ON),
                Move(p.edge(1, 4), OFF),
                Move(p.edge(5, 4), OFF)
            )
        )
    }

    @Test
    fun threeKittyCornerZeroInternalEdgesOn() {
        val p = krazydad("......3..0......")
        assertMoves(
            p, setOf(
                Move(p.edge(3, 4), ON),
                Move(p.edge(4, 5), ON)
            )
        )
    }

    @Test
    fun reachThreeMustStayAndOppositeAreOn() {
        val p = krazydad(3, 4, "......3.....")
        p.edge(4, 3).state = ON
        assertMoves(
            p, setOf(
                Move(p.edge(2, 5), ON),
                Move(p.edge(3, 6), ON),
                Move(p.edge(5, 4), OFF)
            )
        )
    }

    // Diagonals of 3s and 2s

    @Test
    fun kittyCornerThrees() {
        val p = krazydad(".....3....3.....")
        assertMoves(
            p, setOf(
                Move(p.edge(2, 3), ON),
                Move(p.edge(3, 2), ON),
                Move(p.edge(5, 6), ON),
                Move(p.edge(6, 5), ON)
            )
        )
    }

    @Test
    fun kittyCornerThreesWithTwo() {
        val p = krazydad("......3.....2.....3......")
        assertMoves(
            p, setOf(
                Move(p.edge(2, 3), ON),
                Move(p.edge(3, 2), ON),
                Move(p.edge(7, 8), ON),
                Move(p.edge(8, 7), ON)
            )
        )
    }

    @Test
    fun cornerPointingAtTwos() {
        val p = krazydad("......2.....2............")
        p.edge(6, 7).state = ON
        p.edge(7, 6).state = ON
        assertMoves(
            p, setOf(
                Move(p.edge(2, 3), ON),
                Move(p.edge(3, 2), ON),
                Move(p.edge(4, 5), ON),
                Move(p.edge(5, 4), ON)
            )
        )
    }

    @Ignore("not yet implemented")
    @Test
    fun reachTwosCarriesToThree() {
        val p = krazydad("......2.....2.....3......")
        p.edge(1, 2).state = ON
        assertMoves(
            p, setOf(
                Move(p.edge(7, 8), ON),
                Move(p.edge(8, 7), ON)
            )
        )
    }

    // Diagonals of a 3 and 1

    @Test
    fun oneWithOffCornerPointsAtThree() {
        val p = krazydad(".....3....1.....")
        p.edge(6, 5).state = OFF
        p.edge(5, 6).state = OFF
        assertMoves(
            p, setOf(
                Move(p.edge(2, 3), ON),
                Move(p.edge(3, 2), ON)
            )
        )
    }

    @Test
    fun threeWithOnCornerPointsAtOne() {
        val p = krazydad(".....3....1.....")
        p.edge(2, 3).state = ON
        p.edge(3, 2).state = ON
        assertMoves(
            p, setOf(
                Move(p.edge(6, 5), OFF),
                Move(p.edge(5, 6), OFF)
            )
        )
    }

    // Diagonals starting with a 2

    @Test
    fun forceToTwoCarriesToOneAtOtherCorner() {
        val p = krazydad(".....1....2.....")
        p.edge(7, 6).state = ON
        p.edge(6, 7).state = OFF
        assertMoves(
            p, setOf(
                Move(p.edge(2, 3), OFF),
                Move(p.edge(3, 2), OFF)
            )
        )
    }

    @Test
    fun forceToTwoCarriesToTwoAtOtherCorner() {
        val p = krazydad(".....2....2.....")
        p.edge(2, 1).state = OFF
        p.edge(7, 6).state = OFF
        p.edge(6, 7).state = ON
        assertMoves(
            p, setOf(
                Move(p.edge(1, 2), ON)
            )
        )
    }

    // A rule for closed regions

    @Ignore("not implemented yet")
    @Test
    fun evenNumberOfLinesCrossingRegionBoundary() {
        val p = krazydad("11.2...1.....1331..11.3..")
        p.edge(3, 0).state = ON
        p.edge(2, 1).state = ON
        p.edge(2, 3).state = ON

        p.edge(5, 2).state = ON

        p.edge(7, 0).state = ON
        p.edge(8, 1).state = ON

        p.edge(6, 9).state = ON
        p.edge(5, 10).state = ON
        assertMoves(
            p, setOf(
                Move(p.edge(3, 4), OFF),
                Move(p.edge(5, 8), OFF)
            )
        )
    }

    // Jordan curve theorem
    // no examples, just discussion

    // Rules for puzzles that have only 1 solution

    // todo: this is not a good example, as we can solve it via other means
    @Test
    fun flipFloppyTwoMustPullExternalCornerToItself() {
        /*
        # edges: c00000008b3c73b
        3 2 3 . .
        . . 2 . .
        2 . 2 2 .
        . . . 3 2
        3 . 2 3 .
        */
        val p = krazydad("323....2..2.22....323.23.")
        p.edge(0, 1).state = ON
        p.edge(1, 0).state = ON
        p.edge(5, 10).state = ON
        p.edge(7, 2).state = ON
        p.edge(7, 4).state = ON
        p.edge(6, 7).state = ON
        p.edge(7, 8).state = ON
        p.edge(7, 10).state = ON
        p.edge(8, 1).state = ON
        p.edge(9, 0).state = ON
        p.edge(9, 4).state = ON
        p.edge(8, 7).state = ON
        p.edge(9, 6).state = ON
        p.edge(9, 10).state = ON
        p.edge(10, 1).state = ON
        p.edge(10, 3).state = ON
        p.edge(10, 7).state = ON
        p.edge(10, 9).state = ON
        assertMoves(
            p, setOf(
                Move(p.edge(3, 2), ON),
                Move(p.edge(4, 3), ON),
                Move(p.edge(5, 0), ON),
                Move(p.edge(6, 1), ON)
            )
        )
    }

    @Ignore("not supported yet")
    @Test
    fun twoInCornerWithNoAdjacentCluesMustTakeCorner() {
        val p = krazydad("........2")
        assertMoves(
            p, setOf(
                Move(p.edge(3, 4), ON),
                Move(p.edge(4, 3), ON),
                Move(p.edge(6, 5), ON),
                Move(p.edge(5, 6), ON)
            )
        )
    }

    // todo: this is not a good example, as we can solve it via other means
    @Test
    fun twoCorrectPathsBetweenPointAreBothIllegal() {
        /*
        # edges: 0c180002a
        0 2 . 2
        . . . 2
        . . . 2
        . 3 3 .
        */
        val p = krazydad("02.2...2...2.33.")
        p.edge(0, 5).state = ON
        p.edge(1, 4).state = ON
        p.edge(2, 3).state = ON
        p.edge(3, 2).state = ON
        p.edge(7, 2).state = ON
        p.edge(7, 4).state = ON
        p.edge(7, 6).state = ON
        assertMoves(
            p, setOf(
                Move(p.edge(6, 3), ON)
            )
        )
    }

    private fun assertMoves(p: Puzzle, moves: Set<Move>) {
        solve(p)
        for (m in moves) {
            if (m.edge.state != m.state) {
                println(p)
                throw AssertionError("didn't set ${m.edge} to ${m.state}")
            }
        }
    }

}
