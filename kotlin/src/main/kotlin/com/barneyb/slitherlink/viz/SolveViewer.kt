package com.barneyb.slitherlink.viz

import com.barneyb.slitherlink.Edge
import com.barneyb.slitherlink.Evidence
import com.barneyb.slitherlink.PuzzleItem
import com.barneyb.slitherlink.SolveState
import com.barneyb.slitherlink.SolveTraceItem
import com.barneyb.slitherlink.io.asciigrid
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JFrame
import javax.swing.JScrollPane
import javax.swing.JTable
import javax.swing.ListSelectionModel
import javax.swing.SwingUtilities
import javax.swing.table.AbstractTableModel

/**
 *
 * @author bboisvert
 */
class SolveViewer(private val ss: SolveState) {

    fun show() {
        SwingUtilities.invokeLater {
            val frame = JFrame("Slitherlink Visualizer")
            frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
            frame.location = nextFrameLocation()
            val gridPanel = GridPanel(ss.result)
            if (ss.hasException) {
                gridPanel.background = Color(255, 240, 240)
            }
            frame.contentPane.add(gridPanel, BorderLayout.CENTER)
            val legend = Legend()
            frame.contentPane.add(legend, BorderLayout.SOUTH)

            val trace = ss.trace
                .filter {
                    it.moveCount > 0
                }
            val table = TraceTable(trace, { i ->
                val highlights: MutableMap<String, MutableCollection<PuzzleItem>> = mutableMapOf()
                fun addHighlight(key: String, items: Collection<PuzzleItem>) {
                    if (items.isEmpty()) return
                    if (highlights.containsKey(key)) {
                        highlights.getValue(key).addAll(items)
                    } else {
                        highlights[key] = items.toMutableSet()
                    }
                }
                fun addHighlight(key: String, vararg items: PuzzleItem) {
                    addHighlight(key, items.toMutableSet())
                }
                fun addHighlights(e: Evidence) {
                    e.forEach { key, items ->
                        addHighlight(key, items)
                    }
                }
                val p = ss.start.scratch()
                for (item in trace.subList(0, i)) {
                    for (m in item.moves) {
                        p.move(m)
                    }
                }
                val curr = trace[i]
                if (curr.hasException) {
                    val currentMoves = mutableSetOf<Edge>()
                    for (m in curr.moves) {
                        p.move(m)
                        currentMoves.add(m.edge)
                    }
                    addHighlight("moves", currentMoves)
                    addHighlight("illegal", curr.illegalMove.edge)
                } else if (curr.moveCount == 1) {
                    val m = curr.moves.first()
                    p.move(m)
                    addHighlight("move", m.edge)
                    if (m.evidenceBased) {
                        addHighlights(m.evidence)
                    }
                } else {
                    addHighlight("moves", curr.moves.map { it.edge })
                    for (m in curr.moves) {
                        p.move(m)
                        if (m.evidenceBased) {
                            addHighlights(m.evidence)
                        }
                    }
                }
                legend.setItems(highlights.keys)
                legend.repaint()
                gridPanel.puzzle = p
                gridPanel.highlights = highlights.values
                gridPanel.repaint()
                frame.validate()
            })
            table.fillsViewportHeight = true
            table.addMouseListener(object: MouseAdapter() {
                override fun mouseClicked(e: MouseEvent?) {
                    if (e == null) return
                    if (e.clickCount != 2) return
                    val jt = e.getSource() as JTable
                    if (table !== jt) return
                    val point = e.getPoint()
                    val row = jt.rowAtPoint(point)
                    if (row < 0 || row > trace.size) return
                    // kludge! rely on the fact that the panel's puzzle will
                    // have updated from the first click!
                    val ss = StringSelection(asciigrid(gridPanel.puzzle))
                    Toolkit.getDefaultToolkit()
                        .systemClipboard
                        .setContents(ss, ss)
                }
            })
            frame.contentPane.add(JScrollPane(table), BorderLayout.EAST)
            frame.pack()
            frame.isVisible = true
        }
    }

}

class TraceTable(trace: List<SolveTraceItem>, onSelect: (Int) -> Unit) : JTable() {
    init {
        model = object: AbstractTableModel() {
            override fun getRowCount() = trace.size

            override fun getColumnCount() = 3

            override fun getColumnClass(columnIndex: Int): Class<*> {
                return when (columnIndex) {
                    0 -> String::class.java
                    1 -> Integer::class.java
                    2 -> Integer::class.java
                    else -> String::class.java
                }
            }

            override fun getColumnName(columnIndex: Int): String {
                return when (columnIndex) {
                    0    -> "Strategy"
                    1    -> "Moves"
                    2    -> "Elapsed (ms)"
                    else -> "?"
                }
            }

            override fun getValueAt(rowIndex: Int, columnIndex: Int): Any {
                val r = trace[rowIndex]
                return when (columnIndex) {
                    0 -> r.source
                    1 -> r.moveCount
                    2 -> r.elapsed
                    else -> "?"
                }
            }

        }
        columnModel.getColumn(0).preferredWidth = 100
        columnModel.getColumn(1).preferredWidth = 40
        columnModel.getColumn(2).preferredWidth = 40
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION)
        selectionModel.addListSelectionListener {
            if (it.valueIsAdjusting) return@addListSelectionListener
            val lsm = it.source as ListSelectionModel
            onSelect(lsm.anchorSelectionIndex)
        }
    }
}
