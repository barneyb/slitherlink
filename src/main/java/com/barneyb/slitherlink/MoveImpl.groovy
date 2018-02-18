package com.barneyb.slitherlink

import groovy.transform.EqualsAndHashCode
import groovy.transform.TupleConstructor

/**
 *
 *
 * @author barneyb
 */
@TupleConstructor
@EqualsAndHashCode
class MoveImpl implements Move {

    final EdgeCoord edge
    final EdgeState state

}
