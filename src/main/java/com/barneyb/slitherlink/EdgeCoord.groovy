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
class EdgeCoord {
    int r
    int c
    Dir d

    EdgeCoord canonical() {
        int r = this.r
        int c = this.c
        Dir d = this.d
        boolean mutated = false
        if (d == Dir.EAST) {
            d = Dir.WEST
            c += 1
            mutated = true
        }
        if (d == Dir.SOUTH) {
            d = Dir.NORTH
            r += 1
            mutated = true
        }
        mutated ? new EdgeCoord(r, c, d) : this
    }

    EdgeState state(Puzzle p) {
        if (d == Dir.WEST) {
            p.verticalEdges[r * (p.cols + 1) + c]
        } else if (d == Dir.NORTH) {
            p.horizontalEdges[r * p.cols + c]
        } else {
            throw new IllegalStateException("you can't manipulate state of a non-canonical EdgeCoord")
        }
    }

    void state(Puzzle p, EdgeState state) {
        if (d == Dir.WEST) {
            p.verticalEdges[r * (p.cols + 1) + c] = state
        } else if (d == Dir.NORTH) {
            p.horizontalEdges[r * p.cols + c] = state
        } else {
            throw new IllegalStateException("you can't manipulate state of a non-canonical EdgeCoord")
        }
    }
}
