package com.barneyb.slitherlink.viz

import com.barneyb.slitherlink.Puzzle
import java.awt.Dimension

/**
 *
 * @author bboisvert
 */
class Dims(
    size: Dimension,
    p: Puzzle
) {
    val dx = size.width / p.gridCols
    val dy = size.height / p.gridRows
    val width = dx * (p.gridCols - 1)
    val height = dy * (p.gridRows - 1)
    val ox = (size.width - width) / 2
    val oy = (size.height - height) / 2
    val dotRadius = 2
    val edgePad = Math.min(dotRadius * 4, Math.min(dx, dy) / 5)
    val fontSize = dy

    override fun toString(): String {
        return "Dims(dx=$dx, dy=$dy, width=$width, height=$height, ox=$ox, oy=$oy)"
    }

}