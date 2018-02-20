package com.barneyb.slitherlink

import groovy.transform.EqualsAndHashCode
import groovy.transform.PackageScope

import static com.barneyb.slitherlink.Dir.*
/**
 *
 *
 * @author barneyb
 */
@EqualsAndHashCode(excludes = ["p"])
class EdgeCoord {
    final int r
    final int c
    @PackageScope
    transient final Puzzle p

    /** I speak grid coordinates */
    protected EdgeCoord(Puzzle p, int r, int c) {
        this.r = r
        this.c = c
        this.p = p
        validate()
    }

    /** I speak human coordinates */
    EdgeCoord(int humanRow, int humanCol, Dir dir) {
        if (dir == EAST) {
            dir = WEST
            humanCol += 1
        }
        if (dir == SOUTH) {
            dir = NORTH
            humanRow += 1
        }
        this.r = humanRow * 2 + (dir == NORTH ? 0 : 1)
        this.c = humanCol * 2 + (dir == WEST ? 0 : 1)
        validate()
    }

    private void validate() {
        if (p != null) {
            if (r < 0 || r >= p.gridRows || c < 0 || c > p.gridCols) {
                throw new IllegalStateException("$this isn't on the board")
            }
        }
        if (r % 2 == c % 2) {
            throw new IllegalArgumentException("$this isn't a valid edge (same parity)")
        }
    }

    @Override
    String toString() {
        new StringBuilder(getClass().simpleName)
            .append("(")
            .append((r / 2) as int)
            .append(", ")
            .append((c / 2) as int)
            .append(", ")
            .append(r % 2 == 0 ? NORTH : WEST)
            .append(")")
            .toString()
    }

    boolean isTopRow() {
        r == 0
    }

    boolean isBottomRow() {
        r == p.gridRows - 1
    }

    boolean isLeftCol() {
        c == 0
    }

    boolean isRightCol() {
        c == p.gridCols - 1
    }

    int getState() {
        p.grid[index()]
    }

    private int index() {
        r * p.gridCols + c
    }

    void setState(int state) {
        def curr = this.state
        if (curr == state) return
        if (curr != Puzzle.UNKNOWN) {
            throw new IllegalArgumentException("$this is $curr, you can't set it $state")
        }
        p.grid[index()] = state
    }

    List<CellCoord> clueCells() {
        cells().findAll {
            ! it.blank
        }
    }

    List<CellCoord> cells() {
        p.cells(this)
    }

    List<DotCoord> dots() {
        p.dots(this)
    }

}
