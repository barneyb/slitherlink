package com.barneyb.slitherlink.read
import com.barneyb.slitherlink.grid.Grid
import com.barneyb.slitherlink.grid.GridType
import com.barneyb.slitherlink.grid.SquareGridFactory

import java.util.regex.Pattern
/**
 * I support reading a game string a la Simon Tantham's Loopy.
 *
 * <p>For example: params "<tt>7x7t0</tt>" and descriptor
 * "<tt>3a1a32e3b32b1c0a112a2323a3a1i2b3</tt>" indicate the following 7x7
 * square grid as below:
 *
 * <pre>
 *     +---+---+---+---+---+---+---+
 *     | 3 |   | 1 |   | 3 | 2 |   |
 *     +---+---+---+---+---+---+---+
 *     |   |   |   |   | 3 |   |   |
 *     +---+---+---+---+---+---+---+
 *     | 3 | 2 |   |   | 1 |   |   |
 *     +---+---+---+---+---+---+---+
 *     |   | 0 |   | 1 | 1 | 2 |   |
 *     +---+---+---+---+---+---+---+
 *     | 2 | 3 | 2 | 3 |   | 3 |   |
 *     +---+---+---+---+---+---+---+
 *     | 1 |   |   |   |   |   |   |
 *     +---+---+---+---+---+---+---+
 *     |   |   |   | 2 |   |   | 3 |
 *     +---+---+---+---+---+---+---+
 * </pre>
 *
 * <p>The params string must be "<tt>int</tt> 'x' <tt>int</tt> 't' <tt>int</tt> [ 'd' <tt>char</tt>  ]",
 * where the first two <tt>int</tt>s are the grid width and height,
 * respectively.  The final int is the grid type.  The trailing optional
 * <tt>d</tt> and <tt>char</tt> indicate the difficulty and are only needed
 * when creating from a seed (not a descriptor).  They may be present, but will
 * always be ignored.
 *
 * <p>The descriptor string indicates the clues on the faces, in grid order.
 * Numeric values are clues, letter values are an alphabetic encoding of runs
 * of empty faces (e.g., "<tt>c</tt>" means three sequential empty faces).  No
 * support is available for grids which have faces with more than 10 edges (and
 * would thus potentially require two-digit clues).
 */
class LoopyReader implements Reader {

    static final Pattern PARAMS_PATTERN = ~/([1-9][0-9]*)x([1-9][0-9]*)t([0-9]+)(d[a-z])?/

    final int width
    final int height
    final GridType type
    final String descriptor

    LoopyReader(String params, String desc) {
        if (params == null) {
            throw new IllegalArgumentException("You may not supply null params")
        }
        def m = PARAMS_PATTERN.matcher(params)
        if (! m.find()) {
            throw new IllegalArgumentException("Malformed parameters: '$params'")
        }
        width = m.group(1) as Integer
        height = m.group(2) as Integer
        def typeIndex = m.group(3) as Integer
        if (typeIndex >= GridType.enumConstants.length) {
            throw new IllegalArgumentException("Unknown grid type: $typeIndex")
        }
        type = GridType.enumConstants[typeIndex]
        this.descriptor = desc
    }

    @Override
    Grid read() {
        if (type != GridType.SQUARE) {
            throw new UnsupportedOperationException("Non-square grids are not supported")
        }
        def g = new SquareGridFactory().create(width, height)
        // todo: fill in the clues...
        g
    }

}

