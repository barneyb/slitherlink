package com.barneyb.slitherlink

import com.barneyb.slitherlink.strat.adjacentOnesOnEdge
import com.barneyb.slitherlink.strat.adjacentThrees
import com.barneyb.slitherlink.strat.clueSatisfied
import com.barneyb.slitherlink.strat.kittyCornerThrees
import com.barneyb.slitherlink.strat.needAllRemaining
import com.barneyb.slitherlink.strat.noBranching
import com.barneyb.slitherlink.strat.oneInCorner
import com.barneyb.slitherlink.strat.reachOneShortOfSatisfiedMustStay
import com.barneyb.slitherlink.strat.singleLoop
import com.barneyb.slitherlink.strat.singleUnknownEdge
import com.barneyb.slitherlink.strat.threeInCorner
import com.barneyb.slitherlink.strat.twoInCorner
import kotlin.reflect.KFunction

/**
 *
 *
 * @author barneyb
 */

/**
 * These {@link Strategy}s operates only on the puzzle itself (clues
 * and dimensions) and thus need not be invoked a multiple times for a given
 * puzzle. Note that this implies <em>all</em> moves for a puzzle must be
 * returned as a single {@link Moves} object, they cannot be batched.
 */
val puzzleOnlyStrategies: Collection<Strategy> = listOf(
        ::adjacentThrees,
        ::adjacentOnesOnEdge,
        ::kittyCornerThrees,
        ::oneInCorner,
        ::threeInCorner,
        ::twoInCorner
)

/**
 * These {@link Strategy}s operates on both the puzzle itself and the state of
 * the edges, and thus are invoked multiple times during solving. Finding a
 * move often turns up multiple moves, so batches may be returned, but this is
 * optional as long as unreturned moves will be found by subsequent invocation.
 */
val stateBasedStrategies: Collection<Strategy> = listOf(
        ::clueSatisfied,
        // forced to one
        ::needAllRemaining,
        ::noBranching,
        // one with edge pair
        ::reachOneShortOfSatisfiedMustStay,
        ::singleLoop,
        ::singleUnknownEdge
        // three with edge pair
        // touch opposite corners of two
)

fun solve(p: Puzzle): SolveState {
    val trace = mutableListOf<SolveTraceItem>()
    val (_, elapsed) = time {
        // these ones can each be blindly executed in order, once.
        for (strat in puzzleOnlyStrategies) {
            trace.add(nextBatch(p, strat))
        }
        // these have to be re-invoked as long as solving is progressing
        // as they may be able to find new moves after another strategy
        // has moved.
        do {
            var moved = false
            for (strat in stateBasedStrategies) {
                val t = nextBatch(p, strat)
                trace.add(t)
                moved = moved || t.moveCount > 0
            }
        } while (moved)
    }
    return SolveState(p, trace, elapsed)
}

private fun nextBatch(p: Puzzle, s: Strategy): SolveTraceItem {
    val name = (s as KFunction<*>).name
    val (moves, elapsed) = time { s(p) }
    val moveCount = if (moves == null) {
        0
    } else {
        if (moves.isEmpty()) {
            throw IllegalArgumentException("$name supplied zero moves")
        }
        for (m in moves) {
            try {
                makeMove(m)
            } catch (e: Exception) {
                println("$name did something stupid: $m")
                throw e
            }
        }
        moves.size
    }
    return SolveTraceItem(name, moveCount, elapsed)
}

private fun makeMove(m: Move) {
    if (m.edge.state == m.state) {
        throw IllegalArgumentException("$m is redundant")
    }
    if (m.state == ON) {
        if (m.edge.dots.any { it.edges.count { it.state == ON } == 2 }) {
            throw IllegalArgumentException("$m would create a branch")
        }
        if (m.edge.cells.any { it.edges.count { it.state == ON } == it.clue }) {
            throw IllegalArgumentException("$m would over-satisfy a cell")
        }
    } else {
        if (m.edge.cells.any { it.edges.count { it.state == ON || it.state == UNKNOWN } == it.clue }) {
            throw IllegalArgumentException("$m would under-satisfy a cell")
        }
    }
    m.edge.state = m.state
}

private fun <T> time(work: () -> T): Pair<T, Long> {
    val start = System.currentTimeMillis()
    val result = work()
    val elapsed = System.currentTimeMillis() - start
    return Pair(result, elapsed)
}

abstract class BaseState(
        val trace: List<SolveTraceItem>
) {
    abstract val totalElapsed: Long

    val overhead
        get() = totalElapsed - strategyElapsed
    val strategyElapsed
        get() = trace.map { it.elapsed }.sum()
    val moveCount
        get() = trace.map { it.moveCount }.sum()
    val batchCount
        get() = trace.count { it.moveCount > 0 }
    val batchElapsed
        get() = trace.filter { it.moveCount > 0 }
                .map { it.elapsed }.sum()
    val callCount
        get() = trace.size
}

class SolveState(
        val puzzle: Puzzle,
        trace: List<SolveTraceItem>,
        override val totalElapsed: Long
) : BaseState(trace) {

    val solved
        get() = puzzle.solved

    val byStrategy
        get() = trace.groupBy { it.source }
                .map { (source, stis) ->
                    StrategyState(source, stis)
                }
}

data class SolveTraceItem(
        val source: String,
        val moveCount: Int,
        val elapsed: Long
)

class StrategyState(
        val source: String,
        trace: List<SolveTraceItem>
) : BaseState(trace) {

    override val totalElapsed: Long
        get() = strategyElapsed

}
