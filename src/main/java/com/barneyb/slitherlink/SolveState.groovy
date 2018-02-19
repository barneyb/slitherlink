package com.barneyb.slitherlink

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
/**
 *
 * @author bboisvert
 */
@EqualsAndHashCode
@ToString(includePackage = false)
class SolveState {

    final Puzzle puzzle
    final List<SolveTrace> trace
    final long totalElapsed

    final boolean solved
    final int moveCount
    final long elapsed

    SolveState(Puzzle puzzle, List<SolveTrace> trace, long totalElapsed) {
        this.puzzle = puzzle
        this.solved = puzzle.solved
        this.trace = trace
        this.totalElapsed = totalElapsed
        this.moveCount = trace*.moveCount.sum(0)
        this.elapsed = trace*.elapsed.sum(0l)
    }
}
