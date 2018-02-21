package com.barneyb.slitherlink

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 *
 *
 * @author barneyb
 */
@EqualsAndHashCode
@ToString(includePackage = false)
class SolveTrace {
    final String source
    final int moveCount
    final long elapsed

    SolveTrace(Object strategy, int moveCount, long elapsed) {
        this.source = strategy.getClass().simpleName
        this.moveCount = moveCount
        this.elapsed = elapsed
    }
}
