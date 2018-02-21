package com.barneyb.slitherlink

/**
 *
 *
 * @author barneyb
 */

fun solve(p: Puzzle): SolveState {
    val trace = mutableListOf<SolveTraceItem>()
    val startedAt = System.currentTimeMillis()
    return SolveState(p, trace, System.currentTimeMillis() - startedAt)
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
