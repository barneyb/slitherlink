package com.barneyb.slitherlink.viz

import com.barneyb.slitherlink.Cell
import com.barneyb.slitherlink.Dot
import com.barneyb.slitherlink.Edge
import com.barneyb.slitherlink.OFF
import com.barneyb.slitherlink.ON
import com.barneyb.slitherlink.Puzzle
import com.barneyb.slitherlink.PuzzleItem
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import javax.swing.JPanel

/**
 *
 * @author bboisvert
 */
class GridPanel(
    var puzzle: Puzzle,
    var highlights: Collection<Set<PuzzleItem>>? = null
) : JPanel() {

    init {
        preferredSize = Dimension(500, 500)
        background = Color.WHITE
    }

    override fun paintComponent(g: Graphics?) {
        super.paintComponent(g)
        if (g == null || g !is Graphics2D) return
        val d = Dims(size, puzzle)
        g.font = g.font.deriveFont(d.fontSize.toFloat())
        g.drawRect(d.ox / 2, d.oy / 2, d.width + d.ox, d.height + d.oy)
        highlights?.forEachIndexed { i, group ->
            g.paint = highlightColors[i % highlightColors.size]
            for (it in group) when (it) {
                is Dot  ->
                    g.fillRect(
                        d.ox + it.c * d.dx - d.edgePad * 2,
                        d.oy + it.r * d.dy - d.edgePad * 2,
                        d.edgePad * 4,
                        d.edgePad * 4
                    )
                is Edge ->
                    if (it.horizontal)
                        g.fillRect(
                            d.ox + (it.c - 1) * d.dx + d.edgePad,
                            d.oy + it.r * d.dy - d.edgePad,
                            d.dx * 2 - d.edgePad * 2,
                            d.edgePad * 2
                        )
                    else
                        g.fillRect(
                            d.ox + it.c * d.dx - d.edgePad,
                            d.oy + (it.r - 1) * d.dy + d.edgePad,
                            d.edgePad * 2,
                            d.dy * 2 - d.edgePad * 2
                        )
                is Cell ->
                    g.fillRect(
                        d.ox + (it.c - 1) * d.dx + d.edgePad / 2,
                        d.oy + (it.r - 1) * d.dy + d.edgePad / 2,
                        d.dx * 2 - d.edgePad,
                        d.dy * 2 - d.edgePad
                    )
            }
        }
        g.paint = Color.BLACK
        g.stroke = BasicStroke(1f)
        drawOffEdges(g, d)
        g.stroke = BasicStroke(d.dotRadius.toFloat())
        drawOnEdges(g, d)
        drawDots(g, d)
        drawClues(g, d)
    }

    private fun drawClues(g: Graphics2D, d: Dims) {
        for (c in puzzle.clueCells()) {
            val str = c.clue.toString()
            val b = g.fontMetrics.getStringBounds(str, g)
            g.drawString(
                str,
                d.ox + c.c * d.dx - (b.width / 2).toInt(),
                d.oy + c.r * d.dy + (b.height / 3).toInt() // '3' is a kludge for ascent
            )
        }
    }

    private fun drawOnEdges(g: Graphics2D, d: Dims) {
        for (e in puzzle.edges(ON)) {
            if (e.horizontal)
                g.drawLine(
                    d.ox + (e.c - 1) * d.dx + d.edgePad,
                    d.oy + e.r * d.dy,
                    d.ox + (e.c + 1) * d.dx - d.edgePad,
                    d.oy + e.r * d.dy
                )
            else
                g.drawLine(
                    d.ox + e.c * d.dx,
                    d.oy + (e.r - 1) * d.dy + d.edgePad,
                    d.ox + e.c * d.dx,
                    d.oy + (e.r + 1) * d.dy - d.edgePad
                )
        }
    }

    private fun drawOffEdges(g: Graphics2D, d: Dims) {
        for (e in puzzle.edges(OFF)) {
            g.drawLine(
                d.ox + e.c * d.dx - d.edgePad,
                d.oy + e.r * d.dy - d.edgePad,
                d.ox + e.c * d.dx + d.edgePad,
                d.oy + e.r * d.dy + d.edgePad
            )
            g.drawLine(
                d.ox + e.c * d.dx - d.edgePad,
                d.oy + e.r * d.dy + d.edgePad,
                d.ox + e.c * d.dx + d.edgePad,
                d.oy + e.r * d.dy - d.edgePad
            )
        }
    }

    private fun drawDots(g: Graphics2D, d: Dims) {
        for (dot in puzzle.dots()) {
            g.drawOval(
                d.ox + dot.c * d.dx - d.dotRadius,
                d.oy + dot.r * d.dy - d.dotRadius,
                d.dotRadius * 2,
                d.dotRadius * 2
            )
        }
    }

}