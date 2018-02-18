package com.barneyb.slitherlink

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor
/**
 *
 *
 * @author barneyb
 */
@TupleConstructor
@EqualsAndHashCode
@ToString(includePackage = false)
class MoveImpl implements Move {

    final EdgeCoord edge
    final EdgeState state

}
