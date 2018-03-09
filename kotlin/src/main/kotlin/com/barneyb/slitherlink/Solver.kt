package com.barneyb.slitherlink

import com.barneyb.slitherlink.strat.*
import kotlin.reflect.KFunction

/**
 * These [Strategy]s operates only on the puzzle itself (clues and dimensions)
 * and thus need not be invoked a multiple times for a given puzzle. Note that
 * this implies _all_ moves for a puzzle must be returned as a single
 * [Sequence]; they cannot be batched.
 */
val puzzleOnlyStrategies: Array<Strategy> = arrayOf(
    ::adjacentThrees
    , ::adjacentOnesOnEdge
    , ::kittyCornerThrees
    , ::oneInCorner
    , ::twoInCorner
    , ::threeInCorner
)

/**
 * These [Strategy]s are mechanical enforcement of the rules of the game. That
 * is, there isn't any thinking, just noticing.
 */
val ruleStrategies: Array<Strategy> = arrayOf(
    ::clueSatisfied
    , ::singleUnknownEdge
    , ::needAllRemaining
    , ::noBranching
    , ::singleLoop
)

/**
 * These [Strategy]s operates on both the puzzle itself and the state of
 * the edges, and thus are invoked multiple times during solving. Finding a
 * move often turns up multiple moves, so batches may be returned, but this is
 * optional as long as unreturned moves will be found by subsequent invocation.
 * Note that the [ruleStrategies] _is_ included in this collection, as all
 * rules strategies are state-based.
 */
val stateBasedStrategies: Array<Strategy> = ruleStrategies + arrayOf(
    ::forcedToOne
    , ::singleXorPairEgress
    , ::reachOneShortOfSatisfiedMustStay
    , ::threeWithEdgePair
    , ::oneWithEdgePair
    , ::twoWithEdgePairHasWhiskers
    , ::threeTouchedByXorPair
    , ::pinchedTwoMustStay
    , ::twoWithEdgePairRepelsAtOtherCorner
    , ::twoWithEdgePairAndNoConstraintsPullsAtCorner
    , ::cantForceIllegalMove
    , ::onlyInOrOutEndOnRegionBoundary
    , ::testAllButOnes
)

fun solve(p: Puzzle): SolveState {
    val start = p.scratch()
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
                if (t.hasException) {
                    return@time
                }
                moved = moved || t.moveCount > 0
                if (p.isSolved()) return@time
                if (moved) break
            }
        } while (moved)
    }
    return SolveState(start, p.scratch(), trace, elapsed)
}

private fun nextBatch(p: Puzzle, s: Strategy): SolveTraceItem {
    val name = (s as KFunction<*>).name
    val moves = mutableListOf<Move>()
    val (err, elapsed) = time {
        for (m in s(p)) {
            try {
                p.move(m)
            } catch (e: IllegalMoveException) {
                println("$name did something stupid: $m")
                return@time e
            }
            moves.add(m)
            if (p.isSolved()) break
        }
        null
    }
    return SolveTraceItem(name, moves, elapsed, err)
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
    val start: Puzzle,
    val result: Puzzle,
    trace: List<SolveTraceItem>,
    override val totalElapsed: Long
) : BaseState(trace) {

    val solved = result.isSolved()
    val hasException = trace.last().hasException

    val byStrategy = trace.groupBy { it.source }
        .map { (source, stis) ->
            StrategyState(source, stis)
        }
}

data class SolveTraceItem(
    val source: String,
    val moves: Collection<Move>,
    val elapsed: Long,
    val exception: IllegalMoveException? = null
) {
    val hasException = exception != null
    val moveCount  = moves.size

    val illegalMove get() = exception!!.move
}

class StrategyState(
    val source: String,
    trace: List<SolveTraceItem>
) : BaseState(trace) {

    override val totalElapsed
        get() = strategyElapsed

}
