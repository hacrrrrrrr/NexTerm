package com.hacrrrrrrr.nexterm
import android.app.Activity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import java.io.File
class MainActivity : Activity() {
    private lateinit var terminal: TerminalView
    private var pty: PtyProcess? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setBackgroundColor(0xFF090B10.toInt()) }
        terminal = TerminalView(this).apply { layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f) }
        val input = EditText(this).apply {
            setSingleLine(true)
            setTextColor(0xFFE6E6E6.toInt())
            setHintTextColor(0xFF777777.toInt())
            hint = "type a command"
            typeface = android.graphics.Typeface.MONOSPACE
            textSize = 18f
            setBackgroundColor(0xFF11141C.toInt())
            setOnEditorActionListener { _, _, _ -> pty?.write(text.toString() + "\n"); text.clear(); true }
        }
        root.addView(terminal)
        root.addView(input, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        setContentView(root)
        pty = PtyProcess(File(filesDir, "home"), { data -> runOnUiThread { terminal.append(data) } }, { code ->
            runOnUiThread { terminal.append("\n[NexTerm] shell exited: $code\n".toByteArray()) }
        })
        terminal.onKey = { pty?.write(it) }
        pty?.start()
    }
    override fun onDestroy() { pty?.close(); super.onDestroy() }
}
