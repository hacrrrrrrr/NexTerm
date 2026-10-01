package com.hacrrrrrrr.nexterm
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.view.KeyEvent
import android.view.View
import kotlin.math.max
class TerminalView(context: Context) : View(context) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply { typeface = Typeface.MONOSPACE; textSize = 30f }
    private val lines = ArrayDeque<String>()
    private var current = StringBuilder()
    var onKey: ((String) -> Unit)? = null
    init { setBackgroundColor(0xFF090B10.toInt()); isFocusableInTouchMode = true }
    fun clearScreen() {\n        lines.clear()\n        current = StringBuilder()\n        invalidate()\n    }\n\n    fun append(bytes: ByteArray) {
        val text = bytes.toString(Charsets.UTF_8)
        var i = 0
        while (i < text.length) {
            when (val c = text[i]) {
                '\u001b' -> {
                    i++
                    if (i < text.length && text[i] == '[') {
                        i++
                        while (i < text.length && !(text[i] in "@-~")) i++
                    }
                }
                '\r' -> {}
                '\n' -> { lines.addLast(current.toString()); current = StringBuilder(); while (lines.size > 2000) lines.removeFirst() }
                '\b' -> if (current.isNotEmpty()) current.deleteCharAt(current.lastIndex)
                else -> if (c >= ' ') current.append(c)
            }
            i++
        }
        invalidate()
    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val lineHeight = paint.fontSpacing
        val visible = max(1, (height / lineHeight).toInt())
        val all = lines.toList() + current.toString()
        val start = max(0, all.size - visible)
        var y = paint.textSize
        for (i in start until all.size) { canvas.drawText(all[i], 12f, y, paint); y += lineHeight }
    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        val s = when (keyCode) {
            KeyEvent.KEYCODE_ENTER -> "\r"
            KeyEvent.KEYCODE_DEL -> "\u007f"
            KeyEvent.KEYCODE_TAB -> "\t"
            KeyEvent.KEYCODE_DPAD_UP -> "\u001b[A"
            KeyEvent.KEYCODE_DPAD_DOWN -> "\u001b[B"
            KeyEvent.KEYCODE_DPAD_LEFT -> "\u001b[D"
            KeyEvent.KEYCODE_DPAD_RIGHT -> "\u001b[C"
            else -> null
        }
        if (s != null) { onKey?.invoke(s); return true }
        return super.onKeyDown(keyCode, event)
    }
}
