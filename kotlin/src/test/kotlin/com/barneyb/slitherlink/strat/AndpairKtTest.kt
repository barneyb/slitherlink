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
            ·   · ? · × ·
                  3 ?
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
            ·   · ! · × ·
                  1 !
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
            · ? ·   · × ·
                  2
            ·   ·   ·---·
                    !
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
                ?
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
                    ?   |
            ·   · ? ·   ·
                      2 ?
            ·   ·---· ? ·
                        ×
            ·   ·   ·   ·
            """
        )
    }

}
