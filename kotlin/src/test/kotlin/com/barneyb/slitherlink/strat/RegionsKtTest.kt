package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.assertStrategy
import org.junit.Test

class RegionsKtTest {

    @org.junit.Ignore("not implemented yet")
    @Test
    fun onlyInOrOutEndOnRegionBoundary() {
        // take from KrazyDad's tough 10x10 volume 1, book 24, puzzle 7
        assertStrategy(
            ::onlyInOrOutEndOnRegionBoundary,
            ("""
                .   .---.---.   .---.
                    |   x   |   x   |
                .   .---. x . x .   .
                        |   | 2
                .---. x . x .   .   .
                |   |   |           |
                . x .   . ! .   . x .
                |       x   x   |   |
                . x . x .---. x .---.
                    x   |   | 2 x
                .   .   . x .---.---.
            """)
        )
    }

}