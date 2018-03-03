package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.Move
import com.barneyb.slitherlink.Puzzle
import com.barneyb.slitherlink.UNKNOWN
import kotlin.coroutines.experimental.buildSequence

/**
 * If exactly one end on the border of a region has a [UNKNOWN] edge both into
 * and out of the region, the parity of the total number of ends on the
 * region's border indicates whether the target end must turn in or out.
 *
 * A region is defind as "all the cells reachable without crossing a known (ON
 * or OFF) edge."
 */
fun onlyInOrOutEndOnRegionBoundary(p: Puzzle) = buildSequence<Move> {
}
