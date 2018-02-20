package com.barneyb.slitherlink

import groovy.transform.Memoized
import groovy.transform.PackageScope

import static com.barneyb.slitherlink.Dir.*

/**
 * I represent a Slitherlink puzzle. Rows and columns refer to the cells, not
 * the dots (so a 10x10 puzzle has 100 cells and 121 dots).
 *
 * @author barneyb
 */
class Puzzle {

    final int rows
    final int cols

    /*
    `int[] grid` stores the whole puzzle with half-square "dot pitch".

    Given this puzzle:

        ·───·───·
        │   × 3 │
        ·───·───·
        × 1 × 1 ×
        · × · × ·

    The array would be

        [
            ·, ─, ·, ─, ·,
            │,  , ×, 3, │,
            ·, ─, ·, ─, ·,
            ×, 1, ×, 1, ×,
            ·, ×, ·, ×, ·
        ]

    Represented as integers:

        * dots are "valueless", but set to -1 for easier initialization
        * edges are -1 for unknown, 0 for off and 1 for on
        * faces are -1 for unspecified, 0-3 for specified

        [
            -1,  1, -1,  1, -1,
             1, -1,  0,  3,  1,
            -1,  1, -1,  1, -1,
             0,  1,  0,  1,  0,
            -1,  0, -1,  0, -1
        ]

     */
    @PackageScope
    final int[] grid

    @PackageScope
    final EdgeState[] horizontalEdges
    @PackageScope
    final EdgeState[] verticalEdges

    Puzzle(rows, cols) {
        this.rows = rows
        this.cols = cols

        this.horizontalEdges = new EdgeState[(rows + 1) * cols]
        Arrays.setAll(this.horizontalEdges, { i -> EdgeState.UNKNOWN })
        this.verticalEdges = new EdgeState[rows * (cols + 1)]
        Arrays.setAll(this.verticalEdges, { i -> EdgeState.UNKNOWN })

        this.grid = new int[(rows * 2 + 1) * (cols * 2 + 1)]
        assert BLANK == UNKNOWN // sanity!
        Arrays.setAll(this.grid, { i -> BLANK })
    }

    static final int BLANK = -1
    static final int ZERO = 0
    static final int ONE = 1
    static final int TWO = 2
    static final int THREE = 3

    static final int UNKNOWN = -1
    static final int ON = 1
    static final int OFF = 0

    static final char DOT = '·'
    static final char VERT = '│'
    static final char HORIZ = '─'
    static final char TICK = '×'

    private boolean _solved = false

    boolean isSolved() {
        if (_solved) return true

        // unsatisfied clue?
        for (cc in clueCells()) {
            def onCount = cc.edges()*.state
                .count { it == EdgeState.ON }
            if (onCount != cc.clue) {
                return false
            }
        }

        // branching?
        for (dc in dots()) {
            def onCount = edges(dc)*.state
                .count { it == EdgeState.ON }
            if (onCount != 0 && onCount != 2) {
                return false
            }
        }

        // multiple segments?
        def edge = null
        def onCount = 0
        for (ec in edges()) {
            if (ec.state == EdgeState.ON) {
                onCount += 1
                if (edge == null)
                    edge = ec
            }
        }
        if (edge == null) return false
        def (curr, prev) = dots(edge)
        def stats = DotCoord.findOtherEndHelper(curr, prev, prev)
        def length = stats.edges + 1 // for the "base" edge
        if (onCount != length) {
            throw new IllegalStateException("prematurely closed loop (w/ $edge)!")
        }

        _solved = true
        return true
    }

    @Override
    String toString() {
        def sb = new StringBuilder()
        for (int r = 0; r <= rows; r++) {
            sb.append(DOT)
            for (int c = 0; c < cols; c++) {
                def v = edgeCoord(r, c, NORTH).state
                (v == EdgeState.ON
                    ? sb.append(HORIZ).append(HORIZ).append(HORIZ)
                    : v == EdgeState.OFF
                        ? sb.append(' ').append(TICK).append(' ')
                        : sb.append(' ').append(' ').append(' '))
                sb.append(DOT)
            }
            sb.append('\n')
            if (r < rows) {
                def v = edgeCoord(r, 0, WEST).state
                sb.append(v == EdgeState.ON
                    ? VERT
                    : v == EdgeState.OFF
                        ? TICK
                        : ' ')
                for (int c = 0; c < cols; c++) {
                    def cell = cellCoord(r, c).clue
                    sb.append(' ')
                    sb.append(cell == BLANK ? ' ' : cell)
                    sb.append(' ')
                    v = edgeCoord(r, c, EAST).state
                    sb.append(v == EdgeState.ON
                        ? VERT
                        : v == EdgeState.OFF
                            ? TICK
                            : ' ')
                }
                sb.append('\n')
            }
        }
        sb.toString()
    }

    @Memoized
    CellCoord cellCoord(int r, int c) {
        new CellCoord(this, r, c)
    }

    @Memoized
    EdgeCoord edgeCoord(int r, int c, Dir d) {
        new EdgeCoord(this, r, c, d)
    }

    @Memoized
    DotCoord dotCoord(int r, int c) {
        new DotCoord(this, r, c)
    }

    @Memoized
    List<DotCoord> dots() {
        List<DotCoord> ds = new ArrayList<>((rows + 1) * (cols + 1))
        for (int r = 0; r <= rows; r++) {
            for (int c = 0; c <= cols; c++) {
                ds.add(dotCoord(r, c))
            }
        }
        ds.asImmutable()
    }

    @Memoized
    List<CellCoord> clueCells() {
        cells().findAll {
            ! it.blank
        }
    }

    @Memoized
    List<CellCoord> cells() {
        List<CellCoord> ds = new ArrayList<>(rows * cols)
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                ds.add(cellCoord(r, c))
            }
        }
        ds
    }

    List<CellCoord> clueCells(int clue) {
        cells().findAll {
            it.clue == clue
        }
    }

    @Memoized
    List<CellCoord> cells(EdgeCoord ec) {
        def cs = []
        if (ec.d == NORTH) {
            if (ec.r > 0) {
                cs << cellCoord(ec.r - 1, ec.c)
            }
            if (ec.r < rows) {
                cs << cellCoord(ec.r, ec.c)
            }
        } else if (ec.d == WEST) {
            if (ec.c > 0) {
                cs << cellCoord(ec.r, ec.c - 1)
            }
            if (ec.c < rows) {
                cs << cellCoord(ec.r, ec.c)
            }
        } else {
            throw new IllegalStateException("you can't get cells from a non-canonical EdgeCoord")
        }
        cs
    }

    @Memoized
    List<EdgeCoord> edges() {
        List<EdgeCoord> ecs = new ArrayList<>(horizontalEdges.length + verticalEdges.length)
        // the main grid
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                ecs << edgeCoord(r, c, NORTH)
                ecs << edgeCoord(r, c, WEST)
            }
        }
        // the right edge
        for (int r = 0; r < rows; r++) {
            ecs << edgeCoord(r, cols, WEST)
        }
        // the bottom edge
        for (int c = 0; c < cols; c++) {
            ecs << edgeCoord(rows, c, NORTH)
        }
        ecs
    }

    @Memoized
    List<EdgeCoord> edges(CellCoord cc) {
        [
            edgeCoord(cc.r, cc.c, NORTH),
            edgeCoord(cc.r, cc.c, EAST),
            edgeCoord(cc.r, cc.c, SOUTH),
            edgeCoord(cc.r, cc.c, WEST),
        ].asImmutable()
    }

    @Memoized
    List<EdgeCoord> edges(DotCoord dc) {
        def ecs = [] as List<EdgeCoord>
        if (dc.r > 0) {
            // not at top, so has up
            ecs << edgeCoord(dc.r - 1, dc.c, WEST)
        }
        if (dc.c < cols) {
            // not at right, so has right
            ecs << edgeCoord(dc.r, dc.c, NORTH)
        }
        if (dc.r < rows) {
            // not at bottom, so has down
            ecs << edgeCoord(dc.r, dc.c, WEST)
        }
        if (dc.c > 0) {
            // not at left, so has left
            ecs << edgeCoord(dc.r, dc.c - 1, NORTH)
        }
        ecs.asImmutable()
    }

    @Memoized
    List<DotCoord> dots(CellCoord cc) {
        [
            dotCoord(cc.r, cc.c),
            dotCoord(cc.r, cc.c + 1),
            dotCoord(cc.r + 1, cc.c + 1),
            dotCoord(cc.r + 1, cc.c),
        ].asImmutable()
    }

    @Memoized
    List<DotCoord> dots(EdgeCoord ec) {
        if (ec.d == NORTH) {
            [
                dotCoord(ec.r, ec.c),
                dotCoord(ec.r, ec.c + 1),
            ].asImmutable()
        } else if (ec.d == WEST) {
            [
                dotCoord(ec.r, ec.c),
                dotCoord(ec.r + 1, ec.c),
            ].asImmutable()
        }
    }

    List<DotCoord> ends() {
        dots().findAll {
            1 == it.edges(EdgeState.ON).size()
        }
    }

    @Memoized
    List<CellCoord> corners() {
        [
            cellCoord(0, 0),
            cellCoord(0, cols - 1),
            cellCoord(rows - 1, cols - 1),
            cellCoord(rows - 1, 0),
        ].asImmutable()
    }

    @Memoized
    Map<CellCoord, List<EdgeCoord>> cornerEdgeMap() {
        //noinspection GroovyAssignabilityCheck
        [
            (cellCoord(0, 0)): [
                edgeCoord(0, 0, NORTH),
                edgeCoord(0, 0, WEST)
            ].asImmutable(),
            (cellCoord(0, cols - 1)): [
                edgeCoord(0, cols - 1, NORTH),
                edgeCoord(0, cols - 1, EAST)
            ].asImmutable(),
            (cellCoord(rows - 1, cols - 1)): [
                edgeCoord(rows - 1, cols - 1, SOUTH),
                edgeCoord(rows - 1, cols - 1, EAST)
            ].asImmutable(),
            (cellCoord(rows - 1, 0)): [
                edgeCoord(rows - 1, 0, SOUTH),
                edgeCoord(rows - 1, 0, WEST)
            ].asImmutable()
        ].asImmutable()
    }

}
