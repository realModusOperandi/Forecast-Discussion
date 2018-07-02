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

fun findEndOfWarnings(body: List<String>, start: Int): Int {
    var sectionEnd = start
    while (sectionEnd < body.size && body[sectionEnd] != "&&") {
        sectionEnd++
    }
    return sectionEnd
}