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
class MoveImpl implements Move {

    final EdgeCoord edge
    final EdgeState state

    @Deprecated
    MoveImpl(strategy, EdgeCoord edge, EdgeState state) {
        this(edge, state)
        if (strategy != null) {
            println("WARN: $strategy passed itself to MoveImpl")
        }
    }

    MoveImpl(EdgeCoord edge, EdgeState state) {
        this.edge = edge
        this.state = state
    }

}
