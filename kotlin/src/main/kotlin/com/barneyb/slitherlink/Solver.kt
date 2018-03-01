package com.barneyb.slitherlink

import com.barneyb.slitherlink.strat.*
import kotlin.reflect.KFunction

/**
 * These [Strategy]s operates only on the puzzle itself (clues and dimensions)
 * and thus need not be invoked a multiple times for a given puzzle. Note that
 * this implies _all_ moves for a puzzle must be returned as a single
 * [Sequence]; they cannot be batched.
 */
val puzzleOnlyStrategies: Collection<Strategy> = listOf(
    ::adjacentThrees,
    ::adjacentOnesOnEdge,
    ::kittyCornerThrees,
    ::oneInCorner,
    ::twoInCorner,
    ::threeInCorner
)

/**
 * These [Strategy]s are mechanical enforcement of the rules of the game. That
 * is, there isn't any thinking, just noticing.
 */
val ruleStrategies: Collection<Strategy> = listOf(
    ::clueSatisfied,
    ::singleUnknownEdge,
    ::needAllRemaining,
    ::noBranching,
    ::singleLoop
)

/**
 * These [Strategy]s operates on both the puzzle itself and the state of
 * the edges, and thus are invoked multiple times during solving. Finding a
 * move often turns up multiple moves, so batches may be returned, but this is
 * optional as long as unreturned moves will be found by subsequent invocation.
 */
val stateBasedStrategies: Collection<Strategy> = ruleStrategies + listOf(
    ::forcedToOne,
    ::singleXorPairEgress,
    ::reachOneShortOfSatisfiedMustStay,
    ::threeWithEdgePair,
    ::oneWithEdgePair,
    ::twoWithEdgePairHasWhiskers,
    ::threeTouchedByXorPair,
    ::pinchedTwoMustStay,
    ::twoWithEdgePairRepelsAtOtherCorner,
    ::twoWithEdgePairAndNoConstraintsPullsAtCorner,
    ::cantForceIllegalMove
)

fun solve(p: Puzzle): SolveState {
    val trace = mutableListOf<SolveTraceItem>()
    val (_, elapsed) = time {
        // these ones can each be blindly executed in order, once.
        for (strat in puzzleOnlyStrategies) {
            trace.add(nextBatch(p, strat))
            if (p.isSolved()) return@time
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
                if (p.isSolved()) return@time
                if (moved) break
            }
        } while (moved)
    }
    return SolveState(p, trace, elapsed)
}

private fun nextBatch(p: Puzzle, s: Strategy): SolveTraceItem {
    val name = (s as KFunction<*>).name
    val (moves, elapsed) = time { s(p) }
    var moveCount = 0
    for (m in moves) {
        try {
            p.move(m)
        } catch (e: Exception) {
            println("$name did something stupid: $m")
            throw e
        }
        moveCount += 1
        if (p.isSolved()) break
    }
    return SolveTraceItem(name, moveCount, elapsed)
}

private fun <T> time(work: () -> T): Pair<T, Long> {
    val start = System.nanoTime()
    val result = work()
    val elapsed = System.nanoTime() - start
    return Pair(result, elapsed / 1000)
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

    val solved = puzzle.isSolved()

    val byStrategy = trace.groupBy { it.source }
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

    override val totalElapsed
        get() = strategyElapsed

}
