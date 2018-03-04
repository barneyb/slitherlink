package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.Edge
import com.barneyb.slitherlink.Move
import com.barneyb.slitherlink.OFF
import com.barneyb.slitherlink.ON
import com.barneyb.slitherlink.Puzzle
import kotlin.coroutines.experimental.buildSequence

/**
 * If a region can be closed with a single [Edge], check how many ends are
 * in the region. If it's an odd number, the edge is [OFF]. If it's an even
 * number, the edge is [ON]. A region is defind as "all the cells reachable
 * without crossing a known (ON or OFF) edge."
 */
fun endParityInClosableRegion(p: Puzzle) = buildSequence<Move> {
}
