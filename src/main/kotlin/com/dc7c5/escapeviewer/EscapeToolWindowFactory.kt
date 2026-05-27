package com.dc7c5.escapeviewer

import com.intellij.icons.AllIcons
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.dsl.builder.Align
import com.intellij.ui.dsl.builder.panel
import com.intellij.util.ui.JBFont
import com.intellij.util.ui.JBUI
import java.awt.Color
import javax.swing.JToggleButton

class EscapeToolWindowFactory : ToolWindowFactory {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val service = project.service<EscapeRendererService>()

        // Use different icons for visual highlight in the sidebar
        val normalIcon = AllIcons.Actions.ToggleVisibility
        val activeIcon = AllIcons.Actions.Show   // Eye icon - stands out when raw mode is on

        val toggleButton = JToggleButton().apply {
            font = JBFont.label().deriveFont(16f)
            isFocusable = false
            preferredSize = JBUI.size(220, 58)
            margin = JBUI.insets(8)
        }

        fun updateUI() {
            val raw = service.isRawMode()

            // Update button
            toggleButton.isSelected = raw
            toggleButton.text = if (raw) "RAW MODE IS ON" else "RAW MODE IS OFF"

            // Strong visual highlight
            if (raw) {
                toggleButton.background = Color(0x2E, 0x7D, 0x32) // strong green
                toggleButton.foreground = Color.WHITE
                toggleButton.font = JBFont.label().deriveFont(16f).asBold()
            } else {
                toggleButton.background = null
                toggleButton.foreground = null
                toggleButton.font = JBFont.label().deriveFont(16f)
            }

            // This is the key for "visual highlight" in the right sidebar:
            // The icon in the tool window stripe itself changes
            toolWindow.setIcon(if (raw) activeIcon else normalIcon)
        }

        toggleButton.addActionListener {
            service.setRawMode(toggleButton.isSelected)
            updateUI()
        }

        // Listen for toggles from toolbar, status bar, keyboard, etc.
        val listener: () -> Unit = {
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
                        font = JBFont.label().deriveFont(20f).asBold()
                    }
                    .align(Align.CENTER)
            }
            row {
                cell(toggleButton)
                    .align(Align.CENTER)
            }
            row {
                label("Shows raw escapes as <AXX> / <UXXXX>")
                    .applyToComponent { foreground = JBUI.CurrentTheme.Label.disabledForeground() }
                    .align(Align.CENTER)
            }
            row {
                label("Also works with Ctrl+Shift+E")
                    .applyToComponent { foreground = JBUI.CurrentTheme.Label.disabledForeground() }
                    .align(Align.CENTER)
            }
        }

        val content = toolWindow.contentManager.factory.createContent(contentPanel, "", false)
        toolWindow.contentManager.addContent(content)

        // Keep the tool window visible in the sidebar
        toolWindow.setToHideOnEmptyContent(false)

        // Cleanup
        content.setDisposer {
            service.removeListener(listener)
        }
    }
}
