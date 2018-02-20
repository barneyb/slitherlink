package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.EdgeState
import com.barneyb.slitherlink.Move
import com.barneyb.slitherlink.MultiMoveStrategy
import com.barneyb.slitherlink.Puzzle
/**
 *
 *
 * @author barneyb
 */
class ThreeFromXorPair extends AbstractXorPairStrategy implements MultiMoveStrategy {

    @Override
    List<Move> nextMoves(Puzzle p) {
        def mc = null
        for (fc in getXorEdgePairs(p)) {
            if (fc.cell.hasCell(fc.dot)) {
                def other = fc.cell.cell(fc.dot)
                if (other && other.clue == 3) {
                    mc = Utils.edges(other.edges() - fc.dot.edges(), EdgeState.ON)
                    if (mc) return mc
                }
            }
        }
        mc
    }

}
