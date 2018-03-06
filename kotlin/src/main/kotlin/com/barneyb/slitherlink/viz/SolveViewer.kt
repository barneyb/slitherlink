package com.barneyb.slitherlink.viz

import com.barneyb.slitherlink.SolveState
import java.awt.BorderLayout
import java.awt.Point
import javax.swing.JFrame
import javax.swing.SwingUtilities

/**
 *
 * @author bboisvert
 */
class SolveViewer(private val ss: SolveState) {

    fun show() {
        SwingUtilities.invokeLater {
            val frame = JFrame("Slitherlink Visualizer")
            frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
            frame.location = Point(300, 200)
            val p = GridPanel(ss.result);
            frame.contentPane.add(p, BorderLayout.CENTER)
            frame.pack()
            frame.isVisible = true;
        }
    }

}