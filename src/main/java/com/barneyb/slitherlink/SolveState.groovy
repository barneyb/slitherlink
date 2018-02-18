package com.barneyb.slitherlink

import groovy.transform.Canonical
/**
 *
 * @author bboisvert
 */
@Canonical
class SolveState {

    final Puzzle puzzle
    final boolean solved
    final int moveCount

}
