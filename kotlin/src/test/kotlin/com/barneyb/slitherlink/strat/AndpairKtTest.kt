package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.assertStrategy
import org.junit.Test

class AndpairKtTest {

    @Test
    fun threeWithEdgePair() {
        assertStrategy(
            ::threeWithEdgePair,
            """
            ·   ·   ·   ·
                    ×
            ·   ·   · × ·
                  3
            ·   ·   ·   ·

            ·   ·   ·   ·
            """, """
            ·   ·   ·   ·
                    ×
            ·   ·---· × ·
                  3 |
            ·   ·   ·   ·

            ·   ·   ·   ·
            """
        )
    }

    @Test
    fun oneWithEdgePair() {
        assertStrategy(
            ::oneWithEdgePair,
            """
            ·   ·   ·   ·
                    ×
            ·   ·   · × ·
                  1
            ·   ·   ·   ·

            ·   ·   ·   ·
            """, """
            ·   ·   ·   ·
                    ×
            ·   · x · × ·
                  1 x
            ·   ·   ·   ·

            ·   ·   ·   ·
            """
        )
    }

    @Test
    fun twoWithEdgePairHasWhiskers() {
        assertStrategy(
            ::twoWithEdgePairHasWhiskers,
            """
            ·   ·   ·   ·
                ×   ×
            ·   ·   · × ·
                  2
            ·   ·   ·---·

            ·   ·   ·   ·
            """, """
            ·   ·   ·   ·
                ×   ×
            ·---·   · × ·
                  2
            ·   ·   ·---·
                    x
            ·   ·   ·   ·
            """
        )
    }

    @Test
    fun twoWithEdgePairRepelsAtOtherCorner() {
        assertStrategy(
            ::twoWithEdgePairRepelsAtOtherCorner,
            """
            ·   ·   ·   ·
                    ×
            ·   ·   · × ·
                  2
            ·---·   ·   ·

            ·   ·   ·   ·
            """, """
            ·   ·   ·   ·
                    ×
            ·   ·   · × ·
                  2
            ·---·   ·   ·
                |
            ·   ·   ·   ·
            """
        )
    }

    @Test
    fun twoWithEdgePairAndNoConstraintsPullsAtCorner() {
        assertStrategy(
            ::twoWithEdgePairAndNoConstraintsPullsAtCorner,
            """
            ·   ·   ·   ·
                  ?   x |
            ·   ·   ·   ·
                  x   2
            ·   ·---·   ·
                        ×
            ·   ·   ·   ·
            """, """
            ·   ·   ·   ·
                  ? | x |
            ·   ·---·   ·
                  x   2 |
            ·   ·---·---·
                        ×
            ·   ·   ·   ·
            """
        )
    }

}
