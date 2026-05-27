package com.dc7c5.escapeviewer.settings

import com.intellij.openapi.options.BoundConfigurable
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.dsl.builder.bindSelected
import com.intellij.ui.dsl.builder.panel

class EscapeSettingsConfigurable : BoundConfigurable("Escape Viewer") {
    private val settings = com.intellij.openapi.components.service<EscapeSettings>()

    override fun createPanel(): DialogPanel = panel {
        row {
            checkBox("Enable Raw Escape Mode by default")
                .bindSelected(settings::isRawMode)
        }
        row {
            label("Supported languages:")
        }
        row {
            // Simple multi-select simulation
            checkBox("Java / Kotlin").bindSelected({ "JAVA" in settings.enabledLanguages }, { if (it) settings.enabledLanguages += "JAVA" else settings.enabledLanguages -= "JAVA" })
            checkBox("Python").bindSelected({ "Python" in settings.enabledLanguages }, { if (it) settings.enabledLanguages += "Python" else settings.enabledLanguages -= "Python" })
            checkBox("JavaScript / TypeScript").bindSelected({ "JavaScript" in settings.enabledLanguages }, { if (it) settings.enabledLanguages += "JavaScript" else settings.enabledLanguages -= "JavaScript" })
        }
    }

    override fun apply() {
        super.apply()
    }
}