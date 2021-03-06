package com.barneyb.slitherlink.viz

import com.barneyb.slitherlink.Puzzle
import com.barneyb.slitherlink.PuzzleItem
import java.awt.BorderLayout
import javax.swing.JFrame
import javax.swing.SwingUtilities

/**
 *
 * @author bboisvert
 */
class SnapshotViewer(private val p: Puzzle) {
    private val highlights = mutableMapOf<String, Set<PuzzleItem>>()

    fun highlight(label: String, items: Collection<PuzzleItem>): SnapshotViewer {
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
            frame.location = nextFrameLocation()
            val p = GridPanel(p, highlights.values)
            frame.contentPane.add(p, BorderLayout.CENTER)
            if (highlights.size > 1) {
                val l = Legend(highlights.keys)
                frame.contentPane.add(l, BorderLayout.SOUTH)
            }
            frame.pack()
            frame.isVisible = true
        }
    }

}
