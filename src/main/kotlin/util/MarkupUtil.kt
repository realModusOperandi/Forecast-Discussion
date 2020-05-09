@file:JvmName("MarkupUtil")
package util

fun endPage(page: String): String {
    return addEndHTML(addEndBody(page))
}

fun startHTML(input: String): String {
    return "$input<html>\n"
}

fun addEndHTML(page: String): String {
    return "$page</html>\n"
}

fun addStartHead(input: String): String {
    return "$input<head>\n"
}

fun addEndHead(input: String): String {
    return "$input</head>\n"
}

fun addHTMLTitle(input: String, title: String): String {
    return "$input<title>$title</title>\n"
}

fun addMobileViewport(input: String, width: Int): String {
    return "$input<meta name=\"viewport\" content=\"width=$width, initial-scale=1.0\">\n"
}

fun addMobileViewport(input: String, widthValue: String): String {
    return "$input<meta name=\"viewport\" content=\"width=$widthValue, initial-scale=1.0\">\n"
}

fun addStylesheetLink(input: String, styleURL: String): String {
    return "$input<link rel=\"stylesheet\" type=\"text/css\" href=\"$styleURL\"/>\n"
}

fun addWebFont(input: String, fontName: String): String {
    return "$input<link rel=\"stylesheet\" href=\"https://fonts.googleapis.com/css?family=$fontName\"/>\n"
}

fun addStartBody(input: String): String {
    return "$input<body>\n"
}

fun addEndBody(page: String): String {
    return "$page</body>\n"
}

fun addStartDiv(input: String, id: String? = null, className: String? = null): String {
    return addStartElement(input, "<div", id, className)
}

fun addEndDiv(input: String): String {
    return "$input</div>\n"
}

fun addStartNav(input: String, id: String? = null, className: String? = null): String {
    return addStartElement(input, "<nav", id, className)
}

fun addEndNav(input: String): String {
    return "$input</nav>\n"
}

fun addURL(input: String, url: String, text: String): String {
    return "$input<a href=\"$url\">$text</a>"
}

private fun addStartElement(input: String, element: String, id: String?, className: String?): String {
    var elem = element
    if (id != null) {
        elem = "$elem id=\"$id\""
    }
    if (className != null) {
        elem = "$elem class=\"$className\""
    }
    elem = "$elem>\n"
    return input + elem
}