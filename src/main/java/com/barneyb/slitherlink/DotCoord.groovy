package com.barneyb.slitherlink

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
/**
 * These match the coord of the cell to their southeast.
 *
 * @author barneyb
 */
@EqualsAndHashCode(excludes = ["p"])
@ToString(includePackage = false)
class DotCoord {
    final int r
    final int c
    final transient Puzzle p

    DotCoord(int r, int c) {
        this.r = r
        this.c = c
    }

    CellCoord toCell() {
        toCell(false, false)
    }

    CellCoord toCell(boolean north, boolean west) {
        new CellCoord(north ? r - 1 : r, west ? c - 1 : c)
    }

}
