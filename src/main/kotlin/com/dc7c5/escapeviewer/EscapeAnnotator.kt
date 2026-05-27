package com.dc7c5.escapeviewer

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiLiteralExpression
import java.util.regex.Pattern
import com.dc7c5.escapeviewer.settings.EscapeSettings

class EscapeAnnotator : Annotator {
    // No longer needed - using project service directly

    // Simple regex for \xHH and \uXXXX
    private val escapePattern = Pattern.compile("(\\\\x[0-9a-fA-F]{2}|\\\\u[0-9a-fA-F]{4})")

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        val project = element.project
        val isRaw = project.service<EscapeRendererService>().isRawMode()

        if (!isRaw) return

        val text = when (element) {
            is PsiLiteralExpression -> element.text
            else -> element.text   // Kotlin, Python, JavaScript, etc. (language-specific registration ensures correct elements)
        }

        if (!text.contains('\\')) return

        val matcher = escapePattern.matcher(text)
        while (matcher.find()) {
            val start = element.textRange.startOffset + matcher.start()
            val end = element.textRange.startOffset + matcher.end()
            val range = TextRange(start, end)
            val escape = matcher.group(1)

            val display = when {
                escape.startsWith("\\x") -> "<A${escape.substring(2)}>"
                escape.startsWith("\\u") -> "<U${escape.substring(2)}>"
                else -> escape
            }

            holder.newAnnotation(HighlightSeverity.INFORMATION, "Raw: $display")
                .range(range)
                .textAttributes(TextAttributesKey.createTextAttributesKey("ESCAPE_RAW", com.intellij.openapi.editor.DefaultLanguageHighlighterColors.STRING))
                .create()
        }
    }
}