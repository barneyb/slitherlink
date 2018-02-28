package com.barneyb.slitherlink

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
        assertSolveResult(
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
        assertSolveResult(
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
        assertSolveResult(
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
        assertSolveResult(
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
        assertSolveResult(
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
        assertSolveResult(
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
        assertSolveResult(
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
        assertSolveResult(
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
        assertSolveResult(
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
        assertSolveResult(
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
        assertSolveResult(
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
        assertSolveResult(
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
        assertSolveResult(
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
        assertSolveResult(
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
        assertSolveResult(
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

    @Test
    fun reachTwosCarriesToThree() {
        assertSolveResult(
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
        assertSolveResult(
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
        assertSolveResult(
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
        assertSolveResult(
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
        assertSolveResult(
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
        assertSolveResult(
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
        assertSolveResult(
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
        assertSolveResult(
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
        assertSolveResult(
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

}
