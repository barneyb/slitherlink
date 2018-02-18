package com.barneyb.slitherlink

import groovy.transform.Immutable
import groovy.transform.ToString

/**
 *
 *
 * @author barneyb
 */
@Immutable
@ToString(includePackage = false)
class MoveImpl implements Move {

    final EdgeCoord edge
    final EdgeState state

}
