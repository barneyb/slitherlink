package com.barneyb.slitherlink

import groovy.transform.EqualsAndHashCode
import groovy.transform.Immutable
import groovy.transform.Memoized
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

    SolveState(Puzzle puzzle, List<SolveTrace> trace, long totalElapsed) {
        this.puzzle = puzzle
        this.trace = trace
        this.totalElapsed = totalElapsed
    }

    @Immutable
    static class StratStat {
        String source
        List<SolveTrace> trace

        long getTotalElapsed() {
            trace*.elapsed.sum(0l) as long
        }

        long getBatchElapsed() {
            trace.findAll {
                it.moveCount > 0
            }*.elapsed.sum(0l) as Long
        }

        int getMoveCount() {
            trace*.moveCount.sum(0) as int
        }

        long getBatchCount() {
            trace.count { it.moveCount > 0 }
        }

        long getCallCount() {
            trace.size()
        }
    }

    @Memoized
    List<StratStat> getTraceStats() {
        trace.groupBy { it.source }
        .collect { source, trace ->
            new StratStat(source, trace)
        }.sort { -it.totalElapsed }
    }

    boolean isSolved() {
        puzzle.solved
    }

    long getOverhead() {
        totalElapsed - strategyElapsed
    }

    long getStrategyElapsed() {
        trace*.elapsed.sum(0l) as long
    }

    int getMoveCount() {
        trace*.moveCount.sum(0) as int
    }

    long getBatchCount() {
        trace.count { it.moveCount > 0 }
    }

    long getCallCount() {
        trace.size()
    }

}
