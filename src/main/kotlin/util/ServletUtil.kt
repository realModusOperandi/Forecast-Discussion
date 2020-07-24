package util

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL

fun getProductText(productURL: String): List<String> {
    val url = URL(productURL)
    var contents: List<String> = listOf()
    try {
        BufferedReader(InputStreamReader(url.openStream())).use { `in` -> contents = `in`.lineSequence().toList() }
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return contents
}

fun trimContents(contents: List<String>): List<String> {
    var firstLine = 0
    var lastLine = 0
    for (i in contents.indices) {
        if (contents[i].startsWith("Area Forecast Discussion")) {
            firstLine = i
        } else if (contents[i] == "$$") {
            lastLine = i - 2
        }
    }
    return contents.subList(firstLine, lastLine + 1)
}
