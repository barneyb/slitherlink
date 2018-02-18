package com.barneyb.slitherlink

import groovy.transform.Immutable
import groovy.transform.ToString

/**
 * These match the coord of the cell to their southeast.
 *
 * @author barneyb
 */
@Immutable
@ToString(includePackage = false)
class DotCoord {
    int r
    int c


}
