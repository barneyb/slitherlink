package com.barneyb.slitherlink

/**
 *
 * @author bboisvert
 */
class ReachThree implements Strategy {

    Move nextMove(Puzzle p) {
        def threes = p.clues()
        .findAll { cc, c ->
            c == 3
        }.keySet()
        for (CellCoord cc : threes) {
            def dots = p.dots(cc)
            def ends = dots
            .findAll {
                1 == p.edges(it)
                .count {
                    p.edge(it) == EdgeState.ON
                }
            }
            for (dc in ends) {
                def ecs = []
                switch (dots.indexOf(dc)) {
                    case 0:
                        ecs << new EdgeCoord(cc.r, cc.c, Dir.EAST)
                        ecs << new EdgeCoord(cc.r, cc.c, Dir.SOUTH)
                        break
                    case 1:
                        ecs << new EdgeCoord(cc.r, cc.c, Dir.SOUTH)
                        ecs << new EdgeCoord(cc.r, cc.c, Dir.WEST)
                        break
                    case 2:
                        ecs << new EdgeCoord(cc.r, cc.c, Dir.NORTH)
                        ecs << new EdgeCoord(cc.r, cc.c, Dir.WEST)
                        break
                    case 3:
                        ecs << new EdgeCoord(cc.r, cc.c, Dir.NORTH)
                        ecs << new EdgeCoord(cc.r, cc.c, Dir.EAST)
                        break
                    default:
                        throw new IllegalStateException("$cc has janky dots: $dots")
                }
                for (ec in ecs) {
                    if (p.edge(ec) == EdgeState.UNKNOWN) {
                        return new MoveImpl(ec, EdgeState.ON)
                    } else if (p.edge(ec) == EdgeState.OFF) {
                        throw new IllegalStateException("$ec should be on, but it's of")
                    }
                }
            }
        }
        null
    }

}
