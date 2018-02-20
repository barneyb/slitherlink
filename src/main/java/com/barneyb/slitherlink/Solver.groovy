package com.barneyb.slitherlink

import com.barneyb.slitherlink.strat.AdjacentOnesOnEdge
import com.barneyb.slitherlink.strat.AdjacentThrees
import com.barneyb.slitherlink.strat.ClueSatisfied
import com.barneyb.slitherlink.strat.ForcedToOne
import com.barneyb.slitherlink.strat.KittyCornerThrees
import com.barneyb.slitherlink.strat.NeedAllRemaining
import com.barneyb.slitherlink.strat.NoBranching
import com.barneyb.slitherlink.strat.OneInCorner
import com.barneyb.slitherlink.strat.OneWithEdgePair
import com.barneyb.slitherlink.strat.ReachClueOneShortOfSatisfied
import com.barneyb.slitherlink.strat.SingleEgress
import com.barneyb.slitherlink.strat.SingleEgressFromFinalCorner
import com.barneyb.slitherlink.strat.SingleIngress
import com.barneyb.slitherlink.strat.SingleLoop
import com.barneyb.slitherlink.strat.ThreeFromFinalCorner
import com.barneyb.slitherlink.strat.ThreeInCorner
import com.barneyb.slitherlink.strat.ThreeWithEdgePair
import com.barneyb.slitherlink.strat.TwoInCorner
import groovy.transform.TupleConstructor
/**
 *
 * @author bboisvert
 */
class Solver {

    static final Collection<MultiMoveStrategy> STRATEGIES = [
        new OneInCorner(),
        new TwoInCorner(),
        new ThreeInCorner(), // unneeded cuzza ThreeWithEdgePair, though _slightly_ faster
        new KittyCornerThrees(),
        new AdjacentThrees(),
        new AdjacentOnesOnEdge(),

        new NoBranching(),
        new ClueSatisfied(),
        new NeedAllRemaining(),
        new SingleIngress(),
        new SingleEgress(),
        new SingleLoop(),
        new ForcedToOne(),
        new ReachClueOneShortOfSatisfied(),
        new OneWithEdgePair(),
        new ThreeWithEdgePair(),
        new SingleEgressFromFinalCorner(),
        new ThreeFromFinalCorner(),
    ].collect {
        if (it instanceof MultiMoveStrategy)
            it
        else if (it instanceof StaticStrategy)
            new StaticMMSAdapter(it as SingleMoveStrategy)
        else
            new MMSAdapter(it as SingleMoveStrategy)
    }.asImmutable()

    private static interface SAdapter {
        SingleMoveStrategy getDelegate()
    }

    @TupleConstructor
    private static class MMSAdapter implements MultiMoveStrategy, SAdapter {

        final SingleMoveStrategy delegate

        @Override
        List<Move> nextMoves(Puzzle p) {
            def m = delegate.nextMove(p)
            m == null ? null : [m]
        }

    }

    @TupleConstructor
    private static class StaticMMSAdapter implements MultiMoveStrategy, StaticStrategy, SAdapter {

        final SingleMoveStrategy delegate

        @Override
        List<Move> nextMoves(Puzzle p) {
            def m = delegate.nextMove(p)
            m == null ? null : [m]
        }

    }

    SolveState solve(PuzzleSource ps) {
        solve(ps.load())
    }

    SolveState solve(Puzzle p) {
        def strats = new ArrayList<MultiMoveStrategy>(STRATEGIES)
        def trace = [] as List<SolveTrace>
        int moveCount = 0
        boolean moved = true
        def startTotal = System.currentTimeMillis()
        while (moved) {
            moved = false;
            for (def itr = strats.iterator(); itr.hasNext();) {
                def s = itr.next()
                def start = System.currentTimeMillis()
                def ms = s.nextMoves(p)
                def elapsed = System.currentTimeMillis() - start
                trace << new SolveTrace(s instanceof SAdapter ? s.delegate : s, ms?.size() ?: 0, elapsed)
                if (ms != null) {
                    if (ms.empty) {
                        throw new IllegalStateException(s.getClass().simpleName + " gave an empty move Collection.")
                    }
                    for (m in ms) {
                        if (m == null) {
                            throw new IllegalArgumentException("${s.getClass().simpleName} gave a null move.")
                        }
                        try {
                            m.edge.state = m.state
                        } catch (Exception e) {
                            println "${s.getClass().simpleName} did something stupid: $e.message"
                            throw e
                        }
                        moveCount += 1
                    }
                    moved = true
                    break
                }
                if (s instanceof StaticStrategy) {
                    itr.remove()
                }
            }
        }
        def elapsedTotal = System.currentTimeMillis() - startTotal
        new SolveState(p, trace.asImmutable(), elapsedTotal)
    }

}
