package com.dc7c5.escapeviewer

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.StatusBarWidgetFactory
import com.intellij.util.Consumer
import java.awt.event.MouseEvent
import javax.swing.Icon
import com.intellij.icons.AllIcons

class EscapeStatusBarWidgetFactory : StatusBarWidgetFactory {
    override fun getId(): String = "EscapeViewerStatus"
    override fun getDisplayName(): String = "Escape Viewer"
    override fun isAvailable(project: Project): Boolean = true
    override fun createWidget(project: Project): StatusBarWidget = EscapeStatusBarWidget(project)
    override fun disposeWidget(widget: StatusBarWidget) {}
    override fun canBeEnabledOn(statusBar: StatusBar): Boolean = true
}

class EscapeStatusBarWidget(private val project: Project) : StatusBarWidget {
    private val service = project.service<EscapeRendererService>()

    override fun ID(): String = "EscapeViewerStatus"
    override fun getPresentation(): StatusBarWidget.WidgetPresentation = object : StatusBarWidget.TextPresentation {
        override fun getText(): String = if (service.isRawMode()) "RAW" else "RENDERED"
        override fun getTooltipText(): String = "Escape Viewer: Click to toggle"
        override fun getAlignment(): Float = 0.0f
        override fun getClickConsumer(): Consumer<MouseEvent>? = Consumer {
            service.toggle()
        }
    }
    override fun install(statusBar: StatusBar) {}
    override fun dispose() {}
}