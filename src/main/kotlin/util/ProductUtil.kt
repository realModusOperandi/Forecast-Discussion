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