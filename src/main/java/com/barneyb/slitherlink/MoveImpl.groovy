package com.barneyb.slitherlink

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
/**
 *
 *
 * @author barneyb
 */
@EqualsAndHashCode(excludes = ['strategy'])
@ToString(includePackage = false)
class MoveImpl implements Move {

    final EdgeCoord edge
    final EdgeState state
    transient final Strategy strategy

    MoveImpl(Strategy strategy, EdgeCoord edge, EdgeState state) {
        this.edge = edge
        this.state = state
        this.strategy = strategy
    }

}
