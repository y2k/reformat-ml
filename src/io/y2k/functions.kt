package io.y2k

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.codeStyle.CodeStyleManager

class ReformatMlAction : AnAction("Format ML") {

    override fun actionPerformed(event: AnActionEvent) {
        val project = event.getData(PlatformDataKeys.PROJECT)!!
        val styleManager = CodeStyleManager.getInstance(project)
        val editor = event.getData(PlatformDataKeys.EDITOR)!!
        val data = event.getData(PlatformDataKeys.PSI_FILE)!!
        val doc = editor.document

        WriteCommandAction.runWriteCommandAction(project) {
            val offset = editor.scrollingModel.verticalScrollOffset
            styleManager.reformat(data)
            val text = doc.text
            val formatted = format(text)
            if (text != formatted) {
                doc.setText(formatted)
                editor.scrollingModel.disableAnimation()
                editor.scrollingModel.scrollVertically(offset)
                editor.scrollingModel.enableAnimation() } } } }

private fun format(text: String) =
    text.replace(Regex("^( *)} finally", RegexOption.MULTILINE), "$1}\n$1finally")
        .replace(Regex("^( *)} catch", RegexOption.MULTILINE), "$1}\n$1catch")
        .replace(Regex("^( *)} else", RegexOption.MULTILINE), "$1}\n$1else")
        .replace(Regex("\n *}"), " }")
