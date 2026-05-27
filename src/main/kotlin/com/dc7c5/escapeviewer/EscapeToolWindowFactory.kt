package com.dc7c5.escapeviewer

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.dsl.builder.Align
import com.intellij.ui.dsl.builder.panel
import com.intellij.util.ui.JBFont
import com.intellij.util.ui.JBUI
import java.awt.Color
import javax.swing.JButton
import javax.swing.JToggleButton

class EscapeToolWindowFactory : ToolWindowFactory {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val service = project.service<EscapeRendererService>()

        val toggleButton = JToggleButton().apply {
            font = JBFont.label().deriveFont(16f)
            isFocusable = false
            preferredSize = JBUI.size(180, 60)
        }

        fun updateUI() {
            val raw = service.isRawMode()
            toggleButton.isSelected = raw
            toggleButton.text = if (raw) "RAW MODE ON" else "RAW MODE OFF"

            if (raw) {
                toggleButton.background = JBUI.CurrentTheme.Focus.focusColor()
                toggleButton.foreground = Color.WHITE
            } else {
                toggleButton.background = null
                toggleButton.foreground = null
            }
        }

        toggleButton.addActionListener {
            service.setRawMode(toggleButton.isSelected)
            updateUI()
        }

        // Listen for external changes (toolbar, status bar, settings, etc.)
        val listener: () -> Unit = {
            // Update on EDT
            javax.swing.SwingUtilities.invokeLater {
                updateUI()
            }
        }
        service.addListener(listener)

        // Initial state
        updateUI()

        val contentPanel = panel {
            row {
                label("Escape Viewer")
                    .applyToComponent {
                        font = JBFont.label().deriveFont(18f).asBold()
                    }
                    .align(Align.CENTER)
            }
            row {
                cell(toggleButton)
                    .align(Align.CENTER)
            }
            row {
                label("Click to toggle between rendered text\nand raw escape sequences (<AXX> / <UXXXX>)")
                    .applyToComponent { foreground = JBUI.CurrentTheme.Label.disabledForeground() }
                    .align(Align.CENTER)
            }
            row {
                label("Ctrl+Shift+E also works anywhere")
                    .applyToComponent { foreground = JBUI.CurrentTheme.Label.disabledForeground() }
                    .align(Align.CENTER)
            }
        }

        val content = toolWindow.contentManager.factory.createContent(contentPanel, "", false)
        toolWindow.contentManager.addContent(content)

        // Clean up listener when tool window is closed
        content.setDisposer {
            service.removeListener(listener)
        }
    }
}
