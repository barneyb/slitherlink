package com.barneyb.slitherlink

import java.awt.BasicStroke
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Point
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.SwingUtilities

class Visualizer(private val p: Puzzle) {
    private val highlights = mutableMapOf<String, Set<PuzzleItem>>()

    fun highlight(label: String, items: Collection<PuzzleItem>): Visualizer {
        highlights[label] = highlights.getOrDefault(label, setOf()) + items
        return this
    }

    fun highlight(items: Collection<PuzzleItem>) =
        highlight("Group ${highlights.size + 1}", items.toSet())

    fun highlight(label: String, vararg items: PuzzleItem) =
        highlight(label, items.toSet())

    fun highlight(vararg items: PuzzleItem) =
        highlight(items.toSet())

    fun show() {
        SwingUtilities.invokeLater {
            val frame = JFrame("Slitherlink Visualizer")
            frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
            frame.location = Point(300, 200)
            val p = GridPanel(p, highlights.values);
            frame.contentPane.add(p, BorderLayout.CENTER)
            val l = Legend(highlights.keys)
            frame.contentPane.add(l, BorderLayout.SOUTH)
            frame.pack()
            frame.isVisible = true;
        }
    }

}

private val highlightColors = listOf(
        Color.YELLOW,
        Color.PINK,
        Color.GREEN,
        Color.ORANGE,
        Color.LIGHT_GRAY,
        Color.CYAN
)
    .map {
        Color(it.red, it.green, it.blue, 127)
    }

private class Legend(
    val highlights: Collection<String>
) : JPanel() {

    init {
        highlights.forEachIndexed { i, it ->
            val c = JLabel(it)
            c.isOpaque = true
            c.background = highlightColors[i % highlightColors.size]
            add(c)
        }
    }
}

private class GridPanel(
    val p: Puzzle,
    val highlights: Collection<Set<PuzzleItem>>
) : JPanel() {

    init {
        preferredSize = Dimension(500, 500)
        background = Color.WHITE
    }

    override fun paintComponent(g: Graphics?) {
        super.paintComponent(g)
        if (g == null || g !is Graphics2D) return
        val d = Dims(size, p)
        g.font = g.font.deriveFont(d.fontSize.toFloat())
        g.drawRect(d.ox / 2, d.oy / 2, d.width + d.ox, d.height + d.oy)
        highlights.forEachIndexed { i, group ->
            g.paint = highlightColors[i % highlightColors.size]
            for (it in group) when (it) {
                is Dot ->
                    g.fillRect(
                        d.ox + it.c * d.dx - d.edgePad,
                        d.oy + it.r * d.dy - d.edgePad,
                        d.edgePad * 2,
                        d.edgePad * 2
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
        g.stroke = BasicStroke(d.dotRadius.toFloat())
        drawDots(g, d)
        drawEdges(g, d)
        drawClues(g, d)
    }

    private fun drawClues(g: Graphics2D, d: Dims) {
        for (c in p.clueCells()) {
            val str = c.clue.toString()
            val b = g.fontMetrics.getStringBounds(str, g)
            g.drawString(
                str,
                d.ox + c.c * d.dx - (b.width / 2).toInt(),
                d.oy + c.r * d.dy + (b.height / 3).toInt() // '3' is a kludge for ascent
            )
        }
    }

    private fun drawEdges(g: Graphics2D, d: Dims) {
        for (e in p.edges()) {
            if (e.unknown) continue
            if (e.on) {
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
            } else { // off
                if (e.horizontal)
                    g.drawLine(
                        d.ox + e.c * d.dx,
                        d.oy + e.r * d.dy - d.edgePad,
                        d.ox + e.c * d.dx,
                        d.oy + e.r * d.dy + d.edgePad
                    )
                else
                    g.drawLine(
                        d.ox + e.c * d.dx - d.edgePad,
                        d.oy + e.r * d.dy,
                        d.ox + e.c * d.dx + d.edgePad,
                        d.oy + e.r * d.dy
                    )
            }
        }
    }

    private fun drawDots(g: Graphics2D, d: Dims) {
        for (dot in p.dots()) {
            g.drawOval(
                d.ox + dot.c * d.dx - d.dotRadius,
                d.oy + dot.r * d.dy - d.dotRadius,
                d.dotRadius * 2,
                d.dotRadius * 2
            )
        }
    }

}

private class Dims(
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
    val edgePad = dotRadius * 4
    val fontSize = dy

    override fun toString(): String {
        return "Dims(dx=$dx, dy=$dy, width=$width, height=$height, ox=$ox, oy=$oy)"
    }

}