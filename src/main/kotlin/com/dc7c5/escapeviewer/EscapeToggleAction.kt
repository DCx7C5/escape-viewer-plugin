package com.dc7c5.escapeviewer

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service

class EscapeToggleAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val service = project.service<EscapeRendererService>()
        service.toggle()
    }

    override fun update(e: AnActionEvent) {
        val project = e.project
        val service = project?.service<EscapeRendererService>()
        val isRaw = service?.isRawMode() ?: false
        e.presentation.text = if (isRaw) "Show Rendered Characters" else "Show Raw Escapes (<AXX> / <UXXXX>)"
        e.presentation.description = "Toggle escape sequence display mode"
    }
}