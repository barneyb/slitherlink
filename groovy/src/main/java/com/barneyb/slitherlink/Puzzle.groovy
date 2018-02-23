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

    final int humanRows
    final int humanCols
    final int gridRows
    final int gridCols

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

    Puzzle(humanRows, humanCols) {
        this.humanRows = humanRows
        this.humanCols = humanCols
        this.gridRows = humanRows * 2 + 1
        this.gridCols = humanCols * 2 + 1
        this.grid = new int[(humanRows * 2 + 1) * (humanCols * 2 + 1)]
        assert BLANK == UNKNOWN // sanity!
        Arrays.setAll(this.grid, { i -> BLANK })
    }

    static final int BLANK = -1
    static final int ZERO = 0
    static final int ONE = 1
    static final int TWO = 2
    static final int THREE = 3

    static final int UNKNOWN = -1
    static final int OFF = 0
    static final int ON = 1

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
                .count { it == ON }
            if (onCount != cc.clue) {
                return false
            }
        }

        // branching?
        for (dc in dots()) {
            def onCount = edges(dc)*.state
                .count { it == ON }
            if (onCount != 0 && onCount != 2) {
                return false
            }
        }

        // multiple segments?
        def edge = null
        def onCount = 0
        for (ec in edges()) {
            if (ec.state == ON) {
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
        for (int r = 0; r <= humanRows; r++) {
            sb.append(DOT)
            for (int c = 0; c < humanCols; c++) {
                def v = humanEdgeCoord(r, c, NORTH).state
                (v == ON
                    ? sb.append(HORIZ).append(HORIZ).append(HORIZ)
                    : v == OFF
                    ? sb.append(' ').append(TICK).append(' ')
                    : sb.append(' ').append(' ').append(' '))
                sb.append(DOT)
            }
            sb.append('\n')
            if (r < humanRows) {
                def v = humanEdgeCoord(r, 0, WEST).state
                sb.append(v == ON
                    ? VERT
                    : v == OFF
                    ? TICK
                    : ' ')
                for (int c = 0; c < humanCols; c++) {
                    def cell = humanCellCoord(r, c).clue
                    sb.append(' ')
                    sb.append(cell == BLANK ? ' ' : cell)
                    sb.append(' ')
                    v = humanEdgeCoord(r, c, EAST).state
                    sb.append(v == ON
                        ? VERT
                        : v == OFF
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

    CellCoord humanCellCoord(int r, int c) {
        def cc = new CellCoord(r, c)
        cellCoord(cc.r, cc.c)
    }

    @Memoized
    EdgeCoord edgeCoord(int r, int c) {
        new EdgeCoord(this, r, c)
    }

    EdgeCoord humanEdgeCoord(int r, int c, Dir d) {
        def ec = new EdgeCoord(r, c, d)
        edgeCoord(ec.r, ec.c)
    }

    @Memoized
    DotCoord dotCoord(int r, int c) {
        new DotCoord(this, r, c)
    }

    DotCoord humanDotCoord(int r, int c) {
        def dc = new DotCoord(r, c)
        dotCoord(dc.r, dc.c)
    }

    @Memoized
    List<DotCoord> dots() {
        List<DotCoord> ds = new ArrayList<>((humanRows + 1) * (humanCols + 1))
        for (int r = 0; r <= humanRows; r++) {
            for (int c = 0; c <= humanCols; c++) {
                ds.add(humanDotCoord(r, c))
            }
        }
        ds.asImmutable()
    }

    @Memoized
    List<CellCoord> clueCells() {
        cells().findAll {
            !it.blank
        }
    }

    @Memoized
    List<CellCoord> cells() {
        List<CellCoord> ds = new ArrayList<>(humanRows * humanCols)
        for (int r = 0; r < humanRows; r++) {
            for (int c = 0; c < humanCols; c++) {
                ds.add(humanCellCoord(r, c))
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
        if (ec.r % 2 == 0) {
            // horizontal
            if (!ec.topRow) {
                cs << cellCoord(ec.r - 1, ec.c)
            }
            if (!ec.bottomRow) {
                cs << cellCoord(ec.r + 1, ec.c)
            }
        } else {
            // vertical
            if (!ec.leftCol) {
                cs << cellCoord(ec.r, ec.c - 1)
            }
            if (!ec.rightCol) {
                cs << cellCoord(ec.r, ec.c + 1)
            }
        }
        cs
    }

    @Memoized
    List<EdgeCoord> edges() {
        List<EdgeCoord> ecs = new ArrayList<>(humanRows * (humanCols + 1) + (humanRows + 1) * humanCols)
        // the main grid
        for (int r = 0; r < humanRows; r++) {
            for (int c = 0; c < humanCols; c++) {
                ecs << humanEdgeCoord(r, c, NORTH)
                ecs << humanEdgeCoord(r, c, WEST)
            }
        }
        // the right edge
        for (int r = 0; r < humanRows; r++) {
            ecs << humanEdgeCoord(r, humanCols, WEST)
        }
        // the bottom edge
        for (int c = 0; c < humanCols; c++) {
            ecs << humanEdgeCoord(humanRows, c, NORTH)
        }
        ecs
    }

    @Memoized
    List<EdgeCoord> edges(CellCoord cc) {
        [
            cc.edge(NORTH),
            cc.edge(EAST),
            cc.edge(SOUTH),
            cc.edge(WEST),
        ].asImmutable()
    }

    @Memoized
    List<EdgeCoord> edges(DotCoord dc) {
        def ecs = [] as List<EdgeCoord>
        if (!dc.topRow) {
            // not at top, so has up
            ecs << edgeCoord(dc.r - 1, dc.c)
        }
        if (!dc.rightCol) {
            // not at right, so has right
            ecs << edgeCoord(dc.r, dc.c + 1)
        }
        if (!dc.bottomRow) {
            // not at bottom, so has down
            ecs << edgeCoord(dc.r + 1, dc.c)
        }
        if (!dc.leftCol) {
            // not at left, so has left
            ecs << edgeCoord(dc.r, dc.c - 1)
        }
        ecs.asImmutable()
    }

    @Memoized
    List<DotCoord> dots(CellCoord cc) {
        [
            dotCoord(cc.r - 1, cc.c - 1),
            dotCoord(cc.r - 1, cc.c + 1),
            dotCoord(cc.r + 1, cc.c + 1),
            dotCoord(cc.r + 1, cc.c - 1),
        ].asImmutable()
    }

    @Memoized
    List<DotCoord> dots(EdgeCoord ec) {
        if (ec.r % 2 == 0) {
            // horizontal
            [
                dotCoord(ec.r, ec.c - 1),
                dotCoord(ec.r, ec.c + 1),
            ].asImmutable()
        } else {
            // vertical
            [
                dotCoord(ec.r - 1, ec.c),
                dotCoord(ec.r + 1, ec.c),
            ].asImmutable()
        }
    }

    List<DotCoord> ends() {
        dots().findAll {
            1 == it.edges(ON).size()
        }
    }

    @Memoized
    List<CellCoord> corners() {
        [
            humanCellCoord(0, 0),
            humanCellCoord(0, humanCols - 1),
            humanCellCoord(humanRows - 1, humanCols - 1),
            humanCellCoord(humanRows - 1, 0),
        ].asImmutable()
    }

    @Memoized
    Map<CellCoord, List<EdgeCoord>> cornerEdgeMap() {
        //noinspection GroovyAssignabilityCheck
        [
            (humanCellCoord(0, 0)): [
                humanEdgeCoord(0, 0, NORTH),
                humanEdgeCoord(0, 0, WEST)
            ].asImmutable(),
            (humanCellCoord(0, humanCols - 1)): [
                humanEdgeCoord(0, humanCols - 1, NORTH),
                humanEdgeCoord(0, humanCols - 1, EAST)
            ].asImmutable(),
            (humanCellCoord(humanRows - 1, humanCols - 1)): [
                humanEdgeCoord(humanRows - 1, humanCols - 1, SOUTH),
                humanEdgeCoord(humanRows - 1, humanCols - 1, EAST)
            ].asImmutable(),
            (humanCellCoord(humanRows - 1, 0)): [
                humanEdgeCoord(humanRows - 1, 0, SOUTH),
                humanEdgeCoord(humanRows - 1, 0, WEST)
            ].asImmutable()
        ].asImmutable()
    }

    @Memoized
    Map<CellCoord, List<EdgeCoord>> cornerEgressMap() {
        //noinspection GroovyAssignabilityCheck
        [
            (humanCellCoord(0, 0)): [
                humanEdgeCoord(0, 1, NORTH),
                humanEdgeCoord(1, 0, WEST)
            ],
            (humanCellCoord(0, humanCols - 1)): [
                humanEdgeCoord(0, humanCols - 2, NORTH),
                humanEdgeCoord(1, humanCols - 1, EAST)
            ],
            (humanCellCoord(humanRows - 1, humanCols - 1)): [
                humanEdgeCoord(humanRows - 1, humanCols - 2, SOUTH),
                humanEdgeCoord(humanRows - 2, humanCols - 1, EAST)
            ],
            (humanCellCoord(humanRows - 1, 0)): [
                humanEdgeCoord(humanRows - 1, 1, SOUTH),
                humanEdgeCoord(humanRows - 2, 0, WEST)
            ]
        ].asImmutable()
    }

}
