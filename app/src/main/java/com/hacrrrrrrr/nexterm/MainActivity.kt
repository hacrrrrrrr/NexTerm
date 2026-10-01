package com.hacrrrrrrr.nexterm

import android.app.Activity
import android.os.Bundle
import android.view.KeyEvent
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import java.io.File

class MainActivity : Activity() {
    private lateinit var terminal: TerminalView
    private var pty: PtyProcess? = null
    private val history = ArrayList<String>()
    private var historyIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(0xFF090B10.toInt())
        }

        terminal = TerminalView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f
            )
        }

        val input = EditText(this).apply {
            setSingleLine(true)
            setTextColor(0xFFE6E6E6.toInt())
            setHintTextColor(0xFF777777.toInt())
            hint = "command"
            typeface = android.graphics.Typeface.MONOSPACE
            textSize = 17f
            setBackgroundColor(0xFF11141C.toInt())
            setOnEditorActionListener { _, _, _ ->
                submit(text.toString())
                true
            }
            setOnKeyListener { _, keyCode, event ->
                if (event.action != KeyEvent.ACTION_DOWN) return@setOnKeyListener false
                when (keyCode) {
                    KeyEvent.KEYCODE_DPAD_UP -> {
                        if (history.isNotEmpty()) {
                            historyIndex = (historyIndex - 1).coerceAtLeast(0)
                            setText(history[historyIndex])
                            setSelection(length())
                        }
                        true
                    }
                    KeyEvent.KEYCODE_DPAD_DOWN -> {
                        if (history.isNotEmpty()) {
                            historyIndex = (historyIndex + 1).coerceAtMost(history.size)
                            setText(if (historyIndex < history.size) history[historyIndex] else "")
                            setSelection(length())
                        }
                        true
                    }
                    else -> false
                }
            }
        }

        val toolbar = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setBackgroundColor(0xFF11141C.toInt())
        }

        val clear = Button(this).apply {
            text = "Clear"
            setOnClickListener { terminal.clearScreen() }
        }

        val focus = Button(this).apply {
            text = "Terminal"
            setOnClickListener { terminal.requestFocus() }
        }

        toolbar.addView(clear, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))
        toolbar.addView(focus, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))

        root.addView(terminal)
        root.addView(input)
        root.addView(toolbar)
        setContentView(root)

        pty = PtyProcess(
            File(filesDir, "home"),
            { data -> runOnUiThread { terminal.append(data) } },
            { code -> runOnUiThread { terminal.append("\n[NexTerm] shell exited: $code\n".toByteArray()) } }
        )

        terminal.onKey = { pty?.write(it) }
        terminal.addOnLayoutChangeListener { _, left, top, right, bottom, _, _, _, _ ->
            val cols = ((right - left) / 18).coerceIn(8, 500)
            val rows = ((bottom - top) / 36).coerceIn(2, 500)
            pty?.resize(rows, cols)
        }

        pty?.start()
    }

    private fun submit(command: String) {
        val value = command.trim()
        if (value.isEmpty()) return
        history.remove(value)
        history.add(value)
        if (history.size > 100) history.removeAt(0)
        historyIndex = history.size
        pty?.write(value + "\n")
        currentFocus?.let { if (it is EditText) it.text.clear() }
    }

    override fun onDestroy() {
        pty?.close()
        super.onDestroy()
    }
}