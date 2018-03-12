package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.Cell
import com.barneyb.slitherlink.Move
import com.barneyb.slitherlink.OFF
import com.barneyb.slitherlink.ON
import com.barneyb.slitherlink.Puzzle
import com.barneyb.slitherlink.UNKNOWN
import java.util.*
import kotlin.coroutines.experimental.buildSequence

/**
 * Given a cell-defined region, if there is exactly one end on the border that
 * has unknown edges both into and out of the region, the parity of the number
 * of in-only ends on the border indicates which direction it must turn.
 */
fun onlyInOrOutEndOnRegionBoundary(p: Puzzle) = buildSequence<Move> {
    val regions = getCellDefinedRegions(p)
    if (regions.size == 1) {
        // nothing to do
        return@buildSequence
    }
    for (r in regions.sortedBy { it.size }) {
        val borderEdges = r
            .map {
                it.edges
                    .minus((r - it)
                        .map { it.edges }
                        .flatten()
                    )
            }
            .flatten()
            .toSet()
        val borderDots = borderEdges
            .map {
                it.dots
            }
            .flatten()
            .toSet()
        val regionEdges = r.map { it.edges }.flatten().toSet()
        val flipFlops = borderDots
            .filter { d ->
                val unknowns = d.edges(UNKNOWN)
                unknowns.any { regionEdges.contains(it) } && unknowns.any { ! regionEdges.contains(it) }
            }
            .toSet() // needed for Evidence; they're already distinct
        if (flipFlops.size != 1) {
            // multiple flip-flop points, so can't make a determination w/ counts alone
            continue
        }
        val regionEndDots = borderDots
            .filter {
                // it's an end
                it.edges(ON).size == 1
            }
            .filter {
                // it can stay in the region.
                regionEdges.intersect(it.edges(UNKNOWN)).isNotEmpty()
            }
        val dot = flipFlops.first()
        val target = dot.edges(UNKNOWN).intersect(regionEdges)
        val state = if (regionEndDots.size % 2 == 0) ON else OFF
        setTo(target, state, mapOf(
            "region" to r,
            "border" to borderEdges,
            "flip-flop" to flipFlops
        ))
        break
    }
}

/**
 * I return all multi-cell regions on the board. A region is defined as "all
 * the cells reachable without crossing a known ([ON] or [OFF]) edge."
 */
private fun getCellDefinedRegions(p: Puzzle): List<Set<Cell>> {
    val regions = mutableListOf<Set<Cell>>()
    val visited = mutableSetOf<Cell>()
    for (start in p.cells()) {
        if (!visited.add(start)) {
            // already in there
            continue
        }
        val region = getCellDefinedRegion(start)
        visited.addAll(region)
        if (region.size > 1) {
            regions.add(region)
        }
    }
    return regions
}

private fun getCellDefinedRegion(start: Cell): Set<Cell> {
    val region = mutableSetOf(start)
    val queue: Queue<Cell> = LinkedList(region)
    queue.add(start)
    while (queue.isNotEmpty()) {
        val c = queue.poll()!!
        for (e in c.edges(UNKNOWN)) {
            if (e.hasOpposedCell(c)) {
                val other = e.opposedCell(c)
                if (region.add(other)) {
                    queue.add(other)
                }
            }
        }
    }
    return region
}
