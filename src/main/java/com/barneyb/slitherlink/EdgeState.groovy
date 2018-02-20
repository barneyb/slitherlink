package com.barneyb.slitherlink

/**
 *
 *
 * @author barneyb
 */
enum EdgeState {
    UNKNOWN, ON, OFF

    static EdgeState fromInt(int state) {
        switch (state) {
            case Puzzle.UNKNOWN: return UNKNOWN
            case Puzzle.ON: return ON
            case Puzzle.OFF: return OFF
            default: throw new IllegalArgumentException("bad edge state: $state")
        }
    }

    int toInt() {
        switch (this) {
            case UNKNOWN: return Puzzle.UNKNOWN
            case ON: return Puzzle.ON
            case OFF: return Puzzle.OFF
            default: throw new IllegalArgumentException("bad edge state: ${this}")
        }
    }

}
