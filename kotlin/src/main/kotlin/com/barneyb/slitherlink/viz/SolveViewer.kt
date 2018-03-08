package com.barneyb.slitherlink.viz

import com.barneyb.slitherlink.Edge
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
            val trace = ss.trace
                .filter {
                    it.moveCount > 0
                }
            val table = TraceTable(trace, { i ->
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
                    gridPanel.highlights = listOf(
                        currentMoves,
                        setOf(curr.illegalMove.edge)
                    )
                } else if (curr.moveCount == 1) {
                    val m = curr.moves.first()
                    p.move(m)
                    gridPanel.highlights = listOf(
                        setOf(m.edge)
                    ) + m.evidence.values
                } else {
                    val currentMoves = mutableSetOf<Edge>()
                    val currentEvidence = mutableSetOf<PuzzleItem>()
                    for (m in curr.moves) {
                        p.move(m)
                        currentMoves.add(m.edge)
                        if (m.evidenceBased) {
                            for ((_, items) in m.evidence) {
                                currentEvidence.addAll(items)
                            }
                        }
                    }
                    gridPanel.highlights = listOf(
                        currentMoves,
                        currentEvidence
                    )
                }
                gridPanel.puzzle = p
                gridPanel.repaint()
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
