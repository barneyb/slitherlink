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
 * If exactly one end on the border of a region has a [UNKNOWN] edge both into
 * and out of the region, the parity of the total number of ends on the
 * region's border indicates whether the target end must turn in or out.
 */
fun onlyInOrOutEndOnRegionBoundary(p: Puzzle) = buildSequence<Move> {
    val regions = getRegions(p)
    if (regions.size == 1) {
        // nothing to do
        return@buildSequence
    }
    for (r in regions.sortedBy { it.size }) {
        val border = r
            .map {
                it.edges
                    .minus((r - it)
                        .map { it.edges }
                        .flatten()
                    )
            }
            .flatten()
            .toSet()
        val edgeDots = border
            .map {
                listOf(
                    Pair(it, it.dots.get(0)),
                    Pair(it, it.dots.get(1))
                )
            }
            .flatten()
        val flipFlops = edgeDots
            .filter { (e, d) ->
                // ensure it's a straight end
                if (d.hasOppositeEdge(e)) {
                    val o = d.opposedEdge(e)
                    if (!border.contains(o)) return@filter false
                    if ((e.on || o.on) && (e.off || o.off)) return@filter true
                    // if they're both off, and the cross path is both unknown, it counts as a flip-flip.
                    if ((e.off && o.off) && d.edges(UNKNOWN).size == 2) return@filter true
                    false
                } else {
                    // against an edge
                    if ((e.off) && d.edges(UNKNOWN).size == 2) return@filter true
                    false
                }
            }
            .filter { (e, d) ->
                // ensure the other two edges are UNKNOWN
                // we already know one is one and one is off
                d.edges(UNKNOWN).size == 2
            }
            .map { it.second }
            .toSet()
        if (flipFlops.size != 1) {
            // multiple flip-flop points, so can't make a determination w/ counts alone
            continue
        }
        val regionEdges = r.map { it.edges }.flatten().toSet()
        val regionEndDots = edgeDots
            .map { it.second }
            .toSet()
            .filter {
                // it's an end
                it.edges(ON).size == 1
            }
            .filter {
                // it can stay in the region.
                // we already know there's exactly one that has a choice
                regionEdges.intersect(it.edges(UNKNOWN)).isNotEmpty()
            }


        // there is a potential egress over against the edge in #medium20x20v1b1p1
        // which is not being considered to say the region is closed.
        // i think it's doing the wrong thing counting internal dots too?
        // Edge(32, 33) is beneath the '1' 5 rows and columns from bottom right


        val dot = flipFlops.first()
        val target = dot.edges(UNKNOWN).intersect(regionEdges)
        val state = if (regionEndDots.size % 2 == 0) ON else OFF
        setTo(target, state)
        break
    }
}

/**
 * I return all multi-cell regions on the board. A region is defined as "all
 * the cells reachable without crossing a known ([ON] or [OFF]) edge."
 */
private fun getRegions(p: Puzzle): List<Set<Cell>> {
    val regions = mutableListOf<Set<Cell>>()
    val visited = mutableSetOf<Cell>()
    for (start in p.cells()) {
        if (!visited.add(start)) {
            // already in there
            continue
        }
        val region = mutableSetOf(start)
        val queue: Queue<Cell> = LinkedList(region)
        queue.add(start)
        while (queue.isNotEmpty()) {
            val c = queue.poll()!!
            visited.add(c)
            for (e in c.edges(UNKNOWN)) {
                if (e.hasOpposedCell(c)) {
                    val other = e.opposedCell(c)
                    if (region.add(other)) {
                        queue.add(other)
                    }
                }
            }
        }
        if (region.size > 1) {
            regions.add(region)
        }
    }
    return regions
}
