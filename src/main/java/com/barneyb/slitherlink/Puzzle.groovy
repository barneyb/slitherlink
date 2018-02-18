package com.barneyb.slitherlink

import groovy.transform.EqualsAndHashCode
import groovy.transform.Memoized
import groovy.transform.PackageScope
import groovy.transform.ToString
import groovy.transform.TupleConstructor
/**
 * I represent a Slitherlink puzzle. Rows and columns refer to the cells, not
 * the dots (so a 10x10 puzzle has 100 cells and 121 dots).
 *
 * @author barneyb
 */
class Puzzle {

    final int rows
    final int cols

    @PackageScope
    final int[] cells
    @PackageScope
    final EdgeState[] horizontalEdges
    @PackageScope
    final EdgeState[] verticalEdges

    Puzzle(rows, cols) {
        this.rows = rows
        this.cols = cols
        this.cells = new int[rows * cols];
        Arrays.setAll(this.cells, { i -> BLANK })
        this.horizontalEdges = new EdgeState[(rows + 1) * cols]
        Arrays.setAll(this.horizontalEdges, { i -> EdgeState.UNKNOWN })
        this.verticalEdges = new EdgeState[rows * (cols + 1)]
        Arrays.setAll(this.verticalEdges, { i -> EdgeState.UNKNOWN })
    }

    static final int BLANK = -1
    static final char DOT = '·'
    static final char VERT = '│'
    static final char HORIZ = '─'
    static final char TICK = '×'

    private boolean _solved = false

    boolean isSolved() {
        if (_solved) return true

        // unsatisfied clue?
        for (cc in clueCells()) {
            def onCount = cc.edges()*.state()
                .count { it == EdgeState.ON }
            if (onCount != cc.clue()) {
                return false
            }
        }

        // branching?
        for (dc in dots()) {
            def onCount = edges(dc)*.state()
                .count { it == EdgeState.ON }
            if (onCount != 0 && onCount != 2) {
                return false
            }
        }

        // multiple segments?
        def edge = null
        def onCount = 0
        for (ec in edges()) {
            if (ec.state() == EdgeState.ON) {
                onCount += 1;
                if (edge == null)
                    edge = ec
            }
        }
        if (edge == null) return false
        def (curr, prev) = dots(edge)
        def stats = findOtherEndHelper(curr, prev, prev)
        def length = stats.edges + 1 // for the "base" edge
        if (onCount != length) {
            println("multi segment: $onCount vs $length")
            return false
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
                def v = edgeCoord(r, c, Dir.NORTH).state()
                (v == EdgeState.ON
                    ? sb.append(HORIZ).append(HORIZ).append(HORIZ)
                    : v == EdgeState.OFF
                        ? sb.append(' ').append(TICK).append(' ')
                        : sb.append(' ').append(' ').append(' '))
                sb.append(DOT)
            }
            sb.append('\n')
            if (r < rows) {
                def v = edgeCoord(r, 0, Dir.WEST).state()
                sb.append(v == EdgeState.ON
                    ? VERT
                    : v == EdgeState.OFF
                        ? TICK
                        : ' ')
                for (int c = 0; c < cols; c++) {
                    def cell = cell(r, c)
                    sb.append(' ')
                    sb.append(cell == BLANK ? ' ' : cell)
                    sb.append(' ')
                    v = edgeCoord(r, c, Dir.EAST).state()
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
        if (d == Dir.EAST) {
            d = Dir.WEST
            c += 1
        }
        if (d == Dir.SOUTH) {
            d = Dir.NORTH
            r += 1
        }
        new EdgeCoord(this, r, c, d)
    }

    @Memoized
    DotCoord dotCoord(int r, int c) {
        new DotCoord(this, r, c)
    }

    int cell(int r, int c) {
        cell(cellCoord(r, c))
    }

    int cell(CellCoord cc) {
        cc.clue()
    }

    Puzzle cell(int r, int c, int clue) {
        cell(cellCoord(r, c), clue)
    }

    Puzzle cell(CellCoord cc, int clue) {
        def curr = cc.clue()
        if (curr != BLANK) {
            throw new IllegalArgumentException("Cell at row $cc.r col $cc.c is already set to $curr")
        }
        cc.clue(clue)
        this
    }

    Puzzle move(Move m) {
        if (_solved) {
            throw new IllegalStateException("You can't move after you've won.")
        }
        try {
            m.edge.state(m.state)
        } catch (Exception e) {
            if (m.strategy) {
                println "${m.strategy.getClass().simpleName} did something stupid"
            }
            throw e
        }
        this
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

    List<CellCoord> clueCells() {
        cells().findAll {
            it.clue() != BLANK
        }
    }

    List<CellCoord> cells() {
        List<CellCoord> ds = new ArrayList<>(cells.length)
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                ds.add(cellCoord(r, c))
            }
        }
        ds
    }

    List<CellCoord> cells(EdgeCoord ec) {
        def cs = []
        if (ec.d == Dir.NORTH) {
            if (ec.r > 0) {
                cs << cellCoord(ec.r - 1, ec.c)
            }
            if (ec.r < rows) {
                cs << cellCoord(ec.r, ec.c)
            }
        } else if (ec.d == Dir.WEST) {
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

    List<EdgeCoord> edges() {
        List<EdgeCoord> ecs = new ArrayList<>(horizontalEdges.length + verticalEdges.length)
        // the main grid
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                ecs << edgeCoord(r, c, Dir.NORTH)
                ecs << edgeCoord(r, c, Dir.WEST)
            }
        }
        // the right edge
        for (int r = 0; r < rows; r++) {
            ecs << edgeCoord(r, cols, Dir.WEST)
        }
        // the bottom edge
        for (int c = 0; c < cols; c++) {
            ecs << edgeCoord(rows, c, Dir.NORTH)
        }
        ecs
    }

    @Memoized
    List<EdgeCoord> edges(CellCoord cc) {
        [
            edgeCoord(cc.r, cc.c, Dir.NORTH),
            edgeCoord(cc.r, cc.c, Dir.EAST),
            edgeCoord(cc.r, cc.c, Dir.SOUTH),
            edgeCoord(cc.r, cc.c, Dir.WEST),
        ].asImmutable()
    }

    @Memoized
    List<EdgeCoord> edges(DotCoord dc) {
        def ecs = [] as List<EdgeCoord>
        if (dc.r > 0) {
            // not at top, so has up
            ecs << edgeCoord(dc.r - 1, dc.c, Dir.WEST)
        }
        if (dc.c < cols) {
            // not at right, so has right
            ecs << edgeCoord(dc.r, dc.c, Dir.NORTH)
        }
        if (dc.r < rows) {
            // not at bottom, so has down
            ecs << edgeCoord(dc.r, dc.c, Dir.WEST)
        }
        if (dc.c > 0) {
            // not at left, so has left
            ecs << edgeCoord(dc.r, dc.c - 1, Dir.NORTH)
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
        if (ec.d == Dir.NORTH) {
            [
                dotCoord(ec.r, ec.c),
                dotCoord(ec.r, ec.c + 1),
            ].asImmutable()
        } else if (ec.d == Dir.WEST) {
            [
                dotCoord(ec.r, ec.c),
                dotCoord(ec.r + 1, ec.c),
            ].asImmutable()
        }
    }

    List<DotCoord> ends() {
        dots()
        .findAll {
            1 == edges(it)
            .count {
                it.state() == EdgeState.ON
            }
        }
    }

    DotCoord findOtherEnd(DotCoord dc) {
        def outbound = edges(dc)
        .findAll {
            it.state() == EdgeState.ON
        }
        if (outbound.size() != 1) {
            throw new IllegalStateException("$dc has ${outbound.size()} outbound edges")
        }
        def dots = dots(outbound.first())
        findOtherEndHelper(
            dots.find { it != dc },
            dc
        ).otherEnd
    }

    @TupleConstructor
    @EqualsAndHashCode
    @ToString(includePackage = false)
    private static class FindOtherEndStats {
        final DotCoord otherEnd
        final int edges
    }

    private FindOtherEndStats findOtherEndHelper(DotCoord curr, DotCoord prev, DotCoord initial = null) {
        for (int i = 0;; i++) {
            def outbound = edges(curr)
            .findAll {
                it.state() == EdgeState.ON
            }
            if (outbound.size() == 1 || curr == initial) {
                return new FindOtherEndStats(curr, i)
            }
            assert outbound.size() == 2
            def itr = outbound.iterator()
            def ds = dots(itr.next()) + dots(itr.next())
            def nexts = ds.findAll {
                it != curr && it != prev
            }
            assert nexts.size() == 1
            prev = curr
            curr = nexts.first()
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
                edgeCoord(0, 0, Dir.NORTH),
                edgeCoord(0, 0, Dir.WEST)
            ].asImmutable(),
            (cellCoord(0, cols - 1)): [
                edgeCoord(0, cols - 1, Dir.NORTH),
                edgeCoord(0, cols - 1, Dir.EAST)
            ].asImmutable(),
            (cellCoord(rows - 1, cols - 1)): [
                edgeCoord(rows - 1, cols - 1, Dir.SOUTH),
                edgeCoord(rows - 1, cols - 1, Dir.EAST)
            ].asImmutable(),
            (cellCoord(rows - 1, 0)): [
                edgeCoord(rows - 1, 0, Dir.SOUTH),
                edgeCoord(rows - 1, 0, Dir.WEST)
            ].asImmutable()
        ].asImmutable()
    }

}
