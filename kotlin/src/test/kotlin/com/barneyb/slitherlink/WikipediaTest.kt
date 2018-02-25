package com.barneyb.slitherlink

import com.barneyb.slitherlink.io.stringgrid
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
        assertResult(
            """
            +   +   +
              1
            +   +   +

            +   +   +
            """, """
            + x +   +
            x 1
            +   +   +

            +   +   +
            """
        )
    }

    @Test
    fun threeInCorner() {
        assertResult(
            """
            +   +   +
              3
            +   +   +

            +   +   +
            """, """
            +---+   +
            | 3
            +   +   +

            +   +   +
            """
        )
    }

    @Test
    fun twoInCorner() {
        assertResult(
            """
            +   +   +
              2
            +   +   +

            +   +   +
            """, """
            +   +---+
              2
            +   +   +
            |
            +   +   +
            """
        )
    }

    // Rules for squares with 1

    @Test
    fun forcedToOneMeansOppositeAreOff() {
        assertResult(
            """
            +   +   +
                x
            +---+   +
                  1
            +   +   +
            """, """
            +   +   +
                x
            +---+   +
                  1 x
            +   + x +
            """
        )
    }

    @Test
    fun reachOneWithOppositeOffMeansTurnIn() {
        assertResult(
            """
            +   +   +

            +---+   +
                  1 x
            +   + x +
            """, """
            +   +   +
                x
            +---+   +
                  1 x
            +   + x +
            """
        )
    }

    @Test
    fun kittyCornerOnesWithInnerCornerOffMeansOtherOnesInnerCornerIsOff() {
        assertResult(
            """
            +   +   +   +   +

            +   +   +   +   +
                  1 x
            +   + x +   +   +
                      1
            +   +   +   +   +

            +   +   +   +   +
            """, """
            +   +   +   +   +

            +   +   +   +   +
                  1 x
            +   + x + x +   +
                    x 1
            +   +   +   +   +

            +   +   +   +   +
            """
        )
    }

    @Test
    fun kittyCornerOnesWithOuterCornerOffMeansOtherOnesOuterCornerIsOff() {
        assertResult(
            """
            +   +   +   +   +

            +   + x +   +   +
                x 1
            +   +   +   +   +
                      1
            +   +   +   +   +

            +   +   +   +   +
            """, """
            +   +   +   +   +

            +   + x +   +   +
                x 1
            +   +   +   +   +
                      1 x
            +   +   + x +   +

            +   +   +   +   +
            """
        )
    }

    // A rule for squares with 2

    @Test
    fun reachTwoWithOneOppositeEdgeOff() {
        assertResult(
            """
            +   +   +   +   +

            +   +   + x +   +
                      2
            +   +---+   +   +

            +   +   +   +   +
            """, """
            +   +   +   +   +

            +   +   + x +   +
                      2 |
            +   +---+   +   +
                    x
            +   +   +   +   +
            """
        )
    }

    // Rules for squares with 3

    @Test
    fun threeAdjacentToZeroMakesAHat() {
        assertResult(
            """
            +   +   +   +   +   +

            +   +   +   +   +   +
                      3
            +   +   +   +   +   +
                      0
            +   +   +   +   +   +

            +   +   +   +   +   +
            """, """
            +   +   +   +   +   +

            +   +   +---+   +   +
                    | 3 |
            +   +---+   +---+   +
                      0
            +   +   +   +   +   +

            +   +   +   +   +   +
            """
        )
    }

    @Test
    fun threesAdjacentCommonAndOutsideEdgesOnAndCommonMustTurn() {
        assertResult(
            """
            +   +   +   +   +

            +   +   +   +   +
                  3   3
            +   +   +   +   +

            +   +   +   +   +
            """, """
            +   +   +   +   +
                    x
            +   +   +   +   +
                | 3 | 3 |
            +   +   +   +   +
                    x
            +   +   +   +   +
            """
        )
    }

    @Test
    fun threeKittyCornerZeroInternalEdgesOn() {
        assertResult(
            """
            +   +   +   +   +

            +   +   +   +   +
                      3
            +   +   +   +   +
                  0
            +   +   +   +   +

            +   +   +   +   +
            """, """
            +   +   +   +   +

            +   +   +   +   +
                    | 3
            +   + x +---+   +
                  0 x
            +   +   +   +   +

            +   +   +   +   +
            """
        )
    }

    @Test
    fun reachThreeMustStayAndOppositeAreOn() {
        assertResult(
            """
            +   +   +   +   +

            +   +   +   +   +
                      3
            +   +---+   +   +

            +   +   +   +   +
            """, """
            +   +   +   +   +

            +   +   +---+   +
                      3 |
            +   +---+   +   +
                    x
            +   +   +   +   +
            """
        )
    }

    // Diagonals of 3s and 2s

    @Test
    fun kittyCornerThrees() {
        assertResult(
            """
            +   +   +   +   +

            +   +   +   +   +
                  3
            +   +   +   +   +
                      3
            +   +   +   +   +

            +   +   +   +   +
            """, """
            +   +   +   +   +

            +   +---+   +   +
                | 3
            +   +   +   +   +
                      3 |
            +   +   +---+   +

            +   +   +   +   +
            """
        )
    }

    @Test
    fun kittyCornerThreesWithTwo() {
        assertResult(
            """
            +   +   +   +   +   +

            +   +   +   +   +   +
                  3
            +   +   +   +   +   +
                      2
            +   +   +   +   +   +
                          3
            +   +   +   +   +   +

            +   +   +   +   +   +
            """, """
            +   +   +   +   +   +

            +   +---+   +   +   +
                | 3
            +   +   +   +   +   +
                      2
            +   +   +   +   +   +
                          3 |
            +   +   +   +---+   +

            +   +   +   +   +   +
            """
        )
    }

    @Test
    fun cornerPointingAtTwos() {
        assertResult(
            """
            +   +   +   +   +   +

            +   +   +   +   +   +
                  2
            +   +   +   +   +   +
                      2
            +   +   +   +---+   +
                        |
            +   +   +   +   +   +

            +   +   +   +   +   +
            """, """
            +   +   +   +   +   +

            +   +---+   +   +   +
                | 2
            +   +   +---+   +   +
                    | 2
            +   +   +   +---+   +
                        |
            +   +   +   +   +   +

            +   +   +   +   +   +
            """
        )
    }

    @org.junit.Ignore("not yet implemented")
    @Test
    fun reachTwosCarriesToThree() {
        assertResult(
            """
            +   +   +   +   +   +
                |
            +   +   +   +   +   +
                  2
            +   +   +   +   +   +
                      2
            +   +   +   +   +   +
                          3
            +   +   +   +   +   +

            +   +   +   +   +   +
            """, """
            +   +   +   +   +   +
                |
            + x +   +   +   +   +
                  2
            +   +   +   +   +   +
                      2
            +   +   +   +   +   +
                          3 |
            +   +   +   +---+   +

            +   +   +   +   +   +
            """
        )
    }

    // Diagonals of a 3 and 1

    @Test
    fun oneWithOffCornerPointsAtThree() {
        assertResult(
            """
            +   +   +   +   +

            +   +   +   +   +
                  3
            +   +   +   +   +
                      1 x
            +   +   + x +   +

            +   +   +   +   +
            """, """
            +   +   +   +   +

            +   +---+   +   +
                | 3
            +   +   +   +   +
                      1 x
            +   +   + x +   +

            +   +   +   +   +
            """
        )
    }

    @Test
    fun threeWithOnCornerPointsAtOne() {
        assertResult(
            """
            +   +   +   +   +

            +   +---+   +   +
                | 3
            +   +   +   +   +
                      1
            +   +   +   +   +

            +   +   +   +   +
            """, """
            +   +   +   +   +

            +   +---+   +   +
                | 3
            +   +   +   +   +
                      1 x
            +   +   + x +   +

            +   +   +   +   +
            """
        )
    }

    // Diagonals starting with a 2

    @Test
    fun forceToTwoCarriesToOneAtOtherCorner() {
        assertResult(
            """
            +   +   +   +   +

            +   +   +   +   +
                  1
            +   +   +   +   +
                      2
            +   +   +   + x +
                        |
            +   +   +   +   +
            """, """
            +   +   +   +   +

            +   + x +   +   +
                x 1
            +   +   +   +   +
                      2
            +   +   +   + x +
                        |
            +   +   +   +   +
            """
        )
    }

    @Test
    fun forceToTwoCarriesToTwoAtOtherCorner() {
        assertResult(
            """
            +   +   +   +   +

            + x +   +   +   +
                  2
            +   +   +   +   +
                      2
            +   +   +   +---+
                        x
            +   +   +   +   +
            """, """
            +   +   +   +   +
                |
            + x +   +   +   +
                  2
            +   +   +   +   +
                      2
            +   +   +   +---+
                        x
            +   +   +   +   +
            """
        )
    }

    // A rule for closed regions

    // todo: this is not a good example, as we can solve it via other means
    @Test
    fun evenNumberOfLinesCrossingRegionBoundary() {
        assertResult(
            """
            +   +   +   +   +   +
              1   1       2
            +---+---+   +   +   +
            |         1
            +   +   +   +   +   +
                |         1   3 |
            +   +   +   +   +---+
            | 3   1           1
            +---+   +   +   +   +
              1       3
            +   +   +   +   +   +
            """, """
            +   +   +   +   +   +
              1   1       2
            +---+---+   +   +   +
            |       x 1
            +   +   +   + x +   +
                |         1   3 |
            +   +   +   +   +---+
            | 3   1           1
            +---+   +   +   +   +
              1       3
            +   +   +   +   +   +
            """
        )
    }

    // Jordan curve theorem
    // no examples, just discussion

    // Rules for puzzles that have only 1 solution

    // todo: this is not a good example, as we can solve it via other means
    @Test
    fun flipFloppyTwoMustPullExternalCornerToItself() {
        assertResult(
            """
            +---+   +   +   +   +
            | 3   2   3
            +   +   +   +   +   +
                      2
            +   +   +   +   +   +
              2       2   2     |
            +   +   +   +---+   +
                |   |     3 | 2 |
            +---+   +   +---+   +
            | 3     | 2 | 3     |
            +---+---+   +---+---+
            """, """
            +---+   +   +   +   +
            | 3   2   3
            +   +   +   +   +   +
                |     2
            +   +---+   +   +   +
            | 2       2   2     |
            +---+   +   +---+   +
                |   |     3 | 2 |
            +---+   +   +---+   +
            | 3     | 2 | 3     |
            +---+---+   +---+---+
            """
        )
    }

    @Test
    fun twoInCornerWithNoAdjacentCluesMustTakeCorner() {
        assertResult(
            """
            +   +   +   +

            +   +   +   +

            +   +   +   +
                      2
            +   +   +   +
            """, """
            +   +   +   +

            +   +   +   +
                    |
            +   +---+   +
                      2 |
            +   +   +---+
            """
        )
    }

    // todo: this is not a good example, as we can solve it via other means
    @Test
    fun twoCorrectPathsBetweenPointAreBothIllegal() {
        assertResult(
            """
            +   +   +---+   +
              0   2 |     2
            +   +---+   +   +
                |         2
            +   +   +   +   +
                          2
            +   +   +   +   +
                | 3 | 3 |
            +   +   +   +   +
            """, """
            +   +   +---+   +
              0   2 |     2
            +   +---+   +   +
                |         2
            +   +   +   +   +
                          2
            +   +---+   +   +
                | 3 | 3 |
            +   +   +   +   +
            """
        )
    }

    private fun assertResult(start: String, goal: String) {
        if (start.trim().isEmpty()) throw IllegalArgumentException("you can't have an empty start")
        if (goal.trim().isEmpty()) throw IllegalArgumentException("you can't have an empty goal")
        val p = stringgrid(start)
        solve(p)
        assertMoves(goal, p)
    }

}
