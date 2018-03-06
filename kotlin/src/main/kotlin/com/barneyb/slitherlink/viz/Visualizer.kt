package com.barneyb.slitherlink.viz

import com.barneyb.slitherlink.Puzzle
import com.barneyb.slitherlink.PuzzleItem
import java.awt.BorderLayout
import java.awt.Color
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
            if (highlights.size > 1) {
                val l = Legend(highlights.keys)
                frame.contentPane.add(l, BorderLayout.SOUTH)
            }
            frame.pack()
            frame.isVisible = true;
        }
    }

}

val highlightColors = listOf(
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
