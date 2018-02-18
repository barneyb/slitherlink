package com.barneyb.slitherlink

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor
/**
 * These match the coord of the cell to their southeast.
 *
 * @author barneyb
 */
@TupleConstructor
@EqualsAndHashCode(excludes = ["p"])
@ToString(includePackage = false)
class DotCoord {
    final int r
    final int c
    final transient Puzzle p

}
