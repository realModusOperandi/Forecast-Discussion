package util

import java.util.*

fun findEndOfTitle(body: List<String>, startOfTitle: Int): Int {
    return findEndOfHeading(body, startOfTitle)
}

fun findEndOfHeading(body: List<String>, i: Int): Int {
    return findEndOfParagraph(body, i)
}

fun findEndOfParagraph(body: List<String>, i: Int): Int {
    var paragraphEnd = i
    while (paragraphEnd < body.size && body[paragraphEnd] != "") {
        paragraphEnd++
    }
    return paragraphEnd
}

fun formatHeading(headingText: String): String {
    val heading = titleCasePrefix(headingText)

    return if (heading.endsWith("...")) {
        heading.substring(1, heading.indexOf("..."))
    } else {
        heading.substring(1).replace("...", ": ").replace("(", "").replace(")", "")
    }
}

fun titleCasePrefix(headingText: String): String {
    if (headingText.contains("WATCHES/WARNINGS/ADVISORIES")) {
        val heading = headingText.substring(4, headingText.indexOf("..."))
        return "${headingText.substring(0, 4)}${heading.lowercase(Locale.getDefault())}..."
    }
    val prefix = headingText.substring(1, headingText.indexOf("..."))
    val suffix = headingText.substring(headingText.indexOf("..."))
    return ".${prefix.lowercase(Locale.getDefault())
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }}$suffix"
}

fun formatBody(body: String): String {
    return body.trim().trim('&').trim().replace('`', '\'')
}

val footers = arrayOf("oh hey", "hey", "what's up", "hope the weather's real nice bb", "don't get rained on", "nice", "storm's comin", "it'll blow over eventually", "better lay low", "hail, traveler")
var states: Map<String, String> = mapOf("MN" to "Minnesota", "WI" to "Wisconsin", "IA" to "Iowa", "LS" to "Lake Superior", "AZ" to "Arizona", "CA" to "California")
