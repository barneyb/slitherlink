package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.assertStrategy
import org.junit.Test

class RegionsKtTest {

    @Test
    fun onlyInOrOutEndOnRegionBoundary() {
        // take from KrazyDad's tough 10x10 volume 1, book 24, puzzle 7
        assertStrategy(
            ::onlyInOrOutEndOnRegionBoundary,
            """
                .   .---.---.   .---.
                    |   x   |   x   |
                .   .---. x . x .   .
                        |   | 2
                .---. x . x .   .   .
                |   |   |           |
                . x . ? .   .   . x .
                |       x   x   |   |
                . x . x .---. x .---.
                    x   |   | 2 x
                .   .   . x .---.---.
            """
        )
    }

    val krazyspec = """
        1.2...221.
        .22.221.2.
        322.2..2.2
        .3...22.2.
        2..2..1.12
        2...2.23..
        3.0.021..2
        .22..22..2
        .3.2..3.23
        ..2..2..13
        """

    @org.junit.Ignore("not written yet")
    @Test
    fun candidateOne() {
        // tough 10x10 book 24 puzzle 6 (paper, but not online)
        val testOne = """
            +   +---+---+ x +   +   +---+   +   +   +
              1 |     2 |             2   2   1
            +   +---+   + x +   +   + ! +   +   +   +
                  2 | 2 |   | 2   2   1 x     2     |
            +---+   +   + x +   +   + x +---+   +   +
            | 3 | 2 | 2 |     2         | 2       2
            +   +   +   + x +   +   + x +   +---+   +
            |   | 3 |   |         2 | 2 |   | 2 x   |
            +   +---+ x +   +   +   +   +---+   + x +
            | 2     x     2         | 1       1   2 |
            +---+---+ x +   +   +   +   +---+---+---+
              2     |         2 |   | 2 | 3         x
            +---+---+ x +   + x +---+   +---+---+---+
            | 3     x 0 x   x 0 x 2   1           2 |
            +---+---+ x +---+ x +---+---+---+   +   +
                  2 | 2 |   |   | 2   2     |     2 |
            +---+   +   +   +   +   +---+   +   +---+
            |   | 3 |   | 2 |   |   | 3 |   | 2 | 3
            +   +---+   +   +   +   +   +---+   +---+
            |         2 |   |   | 2 |         1   3 |
            +---+---+---+   +---+   +---+---+---+---+
            """
    }

    @org.junit.Ignore("not written yet")
    @Test
    fun candidateTwo() {
        // tough 10x10 book 24 puzzle 6 (paper, but not online)
        val testTwo = """
            +   +---+---+ x +   +   +---+   +   +   +
              1 |     2 |             2   2   1
            +   +---+   + x +   +   + x +   +   +   +
                  2 | 2 |   | 2 ? 2 | 1 x     2     |
            +---+   +   + x +   +   + x +---+   +   +
            | 3 | 2 | 2 |     2         | 2       2
            +   +   +   + x +   +   + x +   +---+   +
            |   | 3 |   |         2 | 2 |   | 2 x   |
            +   +---+ x +   +   +   +   +---+   + x +
            | 2     x     2         | 1       1   2 |
            +---+---+ x +   +   +   +   +---+---+---+
              2     |         2 |   | 2 | 3         x
            +---+---+ x +   + x +---+   +---+---+---+
            | 3     x 0 x   x 0 x 2   1           2 |
            +---+---+ x +---+ x +---+---+---+   +   +
                  2 | 2 |   |   | 2   2     |     2 |
            +---+   +   +   +   +   +---+   +   +---+
            |   | 3 |   | 2 |   |   | 3 |   | 2 | 3
            +   +---+   +   +   +   +   +---+   +---+
            |         2 |   |   | 2 |         1   3 |
            +---+---+---+   +---+   +---+---+---+---+
            """
    }

}