package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.assertStrategy
import org.junit.Test

class RegionsKtTest {

    @org.junit.Ignore("not implemented yet")
    @Test
    fun endParityInClosableRegion() {
        // take from KrazyDad's touch 10x10 volume 1, book 24, puzzle 7
        /*
        There are two equivalent potential marks to make, both are in the goal
        puzzle. Only one is needed, as the other will be forced by the rules.
        Unclear which one is the "right" one to make.
         */
        assertStrategy(
            ::endParityInClosableRegion,
            """
                .---. x .---. x .---.
                |   | 3 |   |   | 3 x
                .   .---.   .   .---.
                |   x 2 x   | 1 x   |
                .---.---.   . x .   .
                x   x         2
                .---. x .   .   .   .
                | 3 |               |
                . x .   .   .   . x .
                | 2   1 x   x   |   |
                .   . x .---. x .---.
                    |   | 3 | 2 x 2
                . x . x . x .---.---.
            """, """
                .---. x .---. x .---.
                |   | 3 |   |   | 3 x
                .   .---.   .   .---.
                |   x 2 x   | 1 x   |
                .---.---.   . x .   .
                x   x         2
                .---. x .   .   .   .
                | 3 |               |
                . x .---.   .   . x .
                | 2 x 1 x   x   |   |
                .   . x .---. x .---.
                    |   | 3 | 2 x 2
                . x . x . x .---.---.
            """
        )
    }

}