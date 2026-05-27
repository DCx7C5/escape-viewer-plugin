package com.dc7c5.escapeviewer

import com.intellij.codeInsight.hints.*
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile
import com.intellij.openapi.components.service
import com.intellij.codeInsight.hints.presentation.PresentationFactory
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiLiteralExpression
import java.util.regex.Pattern
import javax.swing.JComponent
import javax.swing.JPanel

@Suppress("UnstableApiUsage")
class EscapeInlayHintsProvider : InlayHintsProvider<EscapeInlayHintsProvider.Settings> {

    data class Settings(var enabled: Boolean = true)

    override val key: SettingsKey<Settings> =
        SettingsKey("EscapeInlayHintsProvider")

    override fun getCollectorFor(file: PsiFile, editor: Editor, settings: Settings, sink: InlayHintsSink): InlayHintsCollector? {
        val project = file.project
        val isRaw = project.service<EscapeRendererService>().isRawMode()
        if (!isRaw) return null

        return object : InlayHintsCollector {
            private val escapePattern = Pattern.compile("(\\\\x[0-9a-fA-F]{2}|\\\\u[0-9a-fA-F]{4})")

            override fun collect(element: PsiElement, editor: Editor, sink: InlayHintsSink): Boolean {
                val text = when (element) {
                    is PsiLiteralExpression -> element.text
                    else -> element.text   // Kotlin, Python, JavaScript, etc. (language-specific registration ensures correct elements)
                }

                if (!text.contains('\\')) return true

                val matcher = escapePattern.matcher(text)
                while (matcher.find()) {
                    val startOffset = element.textRange.startOffset + matcher.start()
                    val escape = matcher.group(1)
                    val display = when {
                        escape.startsWith("\\x") -> "<A${escape.substring(2)}>"
                        escape.startsWith("\\u") -> "<U${escape.substring(2)}>"
                        else -> escape
                    }

                    // Add inlay right after the escape sequence
                    val factory = PresentationFactory(editor)
                    val presentation = factory.text(display)
                    sink.addInlineElement(startOffset + escape.length, true, presentation, false)
                }
                return true
            }
        }
    }

    override fun createSettings(): Settings = Settings()

    override val name: String = "Escape Viewer Inlays"

    override val previewText: String? = null

    override fun createConfigurable(settings: Settings): ImmediateConfigurable =
        object : ImmediateConfigurable {
            override val cases: List<ImmediateConfigurable.Case> = emptyList()
            override val mainCheckboxText: String = "Enable Escape Inlays"
            override fun createComponent(listener: ChangeListener): JComponent = JPanel()
            override fun reset() {}
        }
}