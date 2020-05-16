package util

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

fun formatHeading(heading: String): String {
    return if (heading.endsWith("...")) {
        heading.substring(1, heading.indexOf("..."))
    } else {
        heading.substring(1).replace("...", ": ").replace("(", "").replace(")", "")
    }
}

fun formatBody(body: String): String {
    return body.trim().trim('&').trim().replace('`', '\'')
}

val footers = arrayOf("oh hey", "hey", "what's up", "hope the weather's real nice bb", "don't get rained on", "nice", "storm's comin", "it'll blow over eventually", "better lay low", "hail, traveler")
var states: Map<String, String> = mapOf("MN" to "Minnesota", "WI" to "Wisconsin", "IA" to "Iowa", "LS" to "Lake Superior", "AZ" to "Arizona", "CA" to "California")