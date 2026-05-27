package com.dc7c5.escapeviewer.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

@Service(Service.Level.PROJECT)
@State(name = "EscapeViewerSettings", storages = [Storage("escapeViewer.xml")])
class EscapeSettings : PersistentStateComponent<EscapeSettings> {
    var isRawMode: Boolean = false
    var enabledLanguages: Set<String> = setOf("JAVA", "kotlin", "Python", "JavaScript", "TypeScript", "Go", "PHP", "C#")

    override fun getState(): EscapeSettings = this
    override fun loadState(state: EscapeSettings) {
        XmlSerializerUtil.copyBean(state, this)
    }
}