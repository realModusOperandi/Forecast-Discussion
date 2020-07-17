package util

import style.StyleServlet
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

fun getStylesheet(styleResource: InputStream): String {
    val style = StringBuilder()
    val br = BufferedReader(InputStreamReader(styleResource))
    br.lines().map { it.trimEnd() }.forEachOrdered { style.append(it.replace(StyleServlet.BACKGROUND_IMAGE, "images/background.jpg")).append("\n") }
    return style.toString()
}