package com.dc7c5.escapeviewer

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.editor.EditorFactory
import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer
import com.dc7c5.escapeviewer.settings.EscapeSettings

@Service(Service.Level.PROJECT)
class EscapeRendererService(private val project: Project) {
    private val settings = project.service<EscapeSettings>()
    private val listeners = mutableListOf<() -> Unit>()

    fun isRawMode(): Boolean = settings.isRawMode

    fun setRawMode(enabled: Boolean) {
        if (settings.isRawMode != enabled) {
            settings.isRawMode = enabled
            notifyListenersAndRestart()
        }
    }

    fun toggle() {
        settings.isRawMode = !settings.isRawMode
        notifyListenersAndRestart()
    }

    fun addListener(listener: () -> Unit) {
        listeners += listener
    }

    fun removeListener(listener: () -> Unit) {
        listeners -= listener
    }

    private fun notifyListenersAndRestart() {
        listeners.forEach { it() }

        // Trigger re-highlight in all open editors
        EditorFactory.getInstance().allEditors.forEach { editor ->
            if (editor.project == project) {
                DaemonCodeAnalyzer.getInstance(project).restart()
            }
        }
    }
}