package com.barneyb.slitherlink

import com.barneyb.slitherlink.strat.AdjacentThrees
import com.barneyb.slitherlink.strat.ClueSatisfied
import com.barneyb.slitherlink.strat.TwoInCorner

/**
 *
 *
 * @author barneyb
 */

val allStrategies = arrayOf<Strategy>(
        ClueSatisfied(),
        AdjacentThrees(),
        TwoInCorner()
)

fun solve(p: Puzzle): SolveState {
    val strategies = mutableListOf(*allStrategies)
    val trace = mutableListOf<SolveTraceItem>()
    val overallStart = System.currentTimeMillis()
    do {
        var moved = false
        val itr = strategies.iterator()
        while (itr.hasNext()) {
            val s = itr.next()
            val start = System.currentTimeMillis()
            val moves = s.nextMoves(p)
            val elapsed = System.currentTimeMillis() - start
            if (moves == null) {
                trace += SolveTraceItem(s.name, 0, elapsed)
            } else {
                trace += SolveTraceItem(s.name, moves.size, elapsed)
                for (m in moves) {
                    try {
                        m.edge.state = m.state
                    } catch (e: Exception) {
                        println("${s.name} did something stupid: $m")
                        throw e
                    }
                }
                moved = true
            }
            if (s is StatelessStrategy) {
                itr.remove()
            }
        }
    } while (moved)
    return SolveState(p, trace, System.currentTimeMillis() - overallStart)
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
