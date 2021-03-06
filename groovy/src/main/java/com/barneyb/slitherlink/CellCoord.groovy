package com.barneyb.slitherlink

import groovy.transform.EqualsAndHashCode
import groovy.transform.PackageScope

import static com.barneyb.slitherlink.Dir.*
import static com.barneyb.slitherlink.Puzzle.*

/**
 *
 *
 * @author barneyb
 */
@EqualsAndHashCode(excludes = ["p"])
class CellCoord {
    final int r
    final int c
    @PackageScope
    transient final Puzzle p

    /** I speak grid coordinates */
    protected CellCoord(Puzzle p, int r, int c) {
        this.r = r
        this.c = c
        this.p = p
        validate()
    }

    /** I speak human coordinates */
    CellCoord(int humanRow, int humanCol) {
        this.r = humanRow * 2 + 1
        this.c = humanCol * 2 + 1
        validate()
    }

    private void validate() {
        if (p != null) {
            if (r < 0 || r >= p.gridRows || c < 0 || c >= p.gridCols) {
                throw new IllegalArgumentException("$this isn't on the board")
            }
        }
        if (r % 2 == 0) {
            throw new IllegalArgumentException("$this isn't a valid cell (even row)")
        }
        if (c % 2 == 0) {
            throw new IllegalArgumentException("$this isn't a valid cell (even col)")
        }
    }

    @Override
    String toString() {
        new StringBuilder(getClass().simpleName)
            .append("(")
            .append((r - 1) / 2)
            .append(", ")
            .append((c - 1) / 2)
            .append(")")
            .toString()
    }

    boolean isTopRow() {
        r == 1
    }

    boolean isBottomRow() {
        r == p.gridRows - 2
    }

    boolean isLeftCol() {
        c == 1
    }

    boolean isRightCol() {
        c == p.gridCols - 2
    }

    CellCoord cell(Dir d) {
        if (d == NORTH && !topRow) {
            return p.cellCoord(r - 2, c)
        }
        if (d == EAST && !rightCol) {
            return p.cellCoord(r, c + 2)
        }
        if (d == SOUTH && !bottomRow) {
            return p.cellCoord(r + 2, c)
        }
        if (d == WEST && !leftCol) {
            return p.cellCoord(r, c - 2)
        }
        throw new IllegalArgumentException("There's no cell $d of $this")
    }

    boolean hasCell(DotCoord dot) {
        def dr = dot.r - r
        def dc = dot.c - c
        def cr = r + dr * 2
        def cc = c + dc * 2
        cr > 0 && cr < p.gridRows && cc > 0 && cc < p.gridCols
    }

    CellCoord cell(DotCoord dot) {
        def dr = dot.r - r
        def dc = dot.c - c
        if (Math.abs(dr) != 1 || Math.abs(dc) != 1) {
            throw new IllegalArgumentException("$dot isn't on $this")
        }
        return p.cellCoord(r + dr * 2, c + dc * 2)
    }

    EdgeCoord edge(Dir d) {
        switch (d) {
            case NORTH: return p.edgeCoord(r - 1, c)
            case EAST: return p.edgeCoord(r, c + 1)
            case SOUTH: return p.edgeCoord(r + 1, c)
            case WEST: return p.edgeCoord(r, c - 1)
        }
    }

    boolean isBlank() {
        clue == BLANK
    }

    int getClue() {
        p.grid[index()]
    }

    private int index() {
        r * p.gridCols + c
    }

    void setClue(int clue) {
        if (!(clue in [ZERO, ONE, TWO, THREE])) {
            throw new IllegalArgumentException("$clue is not a valid clue (for $this)")
        }
        if (!blank) {
            throw new IllegalStateException("Cell at row $r col $c is already set to ${this.clue}")
        }
        p.grid[index()] = clue
    }

    List<EdgeCoord> edges() {
        p.edges(this)
    }

    List<EdgeCoord> edges(int state) {
        p.edges(this).findAll {
            it.state == state
        }
    }

    List<DotCoord> dots() {
        p.dots(this)
    }
}
