package forecast

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import java.util.Random
import javax.servlet.ServletException
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Servlet implementation class CurrentForecastServlet
 */
/**
 * @see HttpServlet#HttpServlet()
 */
@WebServlet(urlPatterns = ["index.html", "/current"])
class CurrentForecastServlet : HttpServlet() {
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    @Throws(ServletException::class, IOException::class)
    override fun doGet(request: HttpServletRequest, response: HttpServletResponse) {
        val parameters = request.parameterMap
        var office = "ARX"
        if (parameters.containsKey("office")) {
            office = parameters["office"]!![0]
        }
        var debug = false
        if (parameters.containsKey("debug")) {
            debug = java.lang.Boolean.valueOf(parameters["debug"]!![0])
        }
        val productURL = "https://forecast.weather.gov/product.php?site=$office&issuedby=$office&product=AFD&format=txt&version=1&glossary=0"
        val contents = getProductText(productURL)
        val body = trimContents(contents)
        if (debug) {
            sendResponse(response, body.stream().reduce("") { a, b -> a + b + "\n" })
        }
        val page = createPage(request, body, office)
        sendResponse(response, page)
    }

    private fun getProductText(productURL: String): List<String> {
        val url = URL(productURL)
        var contents: List<String> = listOf()
        try {
            BufferedReader(InputStreamReader(url.openStream())).use { `in` -> contents = `in`.lineSequence().toList() }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return contents
    }

    private fun trimContents(contents: List<String>): List<String> {
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

    private fun createPage(request: HttpServletRequest, body: List<String>, office: String): String {
        var page = beginPage(request)
        page = createNav(page, office)
        // "Area Forecast Discussion" and following lines of header
        val firstLine = util.findEndOfTitle(body, 0)
        page = createTitle(page, body, 0, firstLine)
        page = createContent(page, body, firstLine)
        page = util.endPage(page)
        return page
    }

    private fun sendResponse(response: HttpServletResponse, page: String) {
        try {
            BufferedWriter(response.writer).use { out -> out.write(page) }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun beginPage(request: HttpServletRequest): String {
        var page = util.startHTML("")
        page = util.addStartHead(page)
        page = util.addHTMLTitle(page, "Area Forecast Discussion")
        page = util.addStylesheetLink(page, "${request.contextPath}/style.css")
        page = util.addWebFont(page, "Sorts+Mill+Goudy")
        page = util.addWebFont(page, "Crimson+Text")
        page = util.addWebFont(page, "EB+Garamond")
        page = util.addWebFont(page, "PT+Serif")
        page = util.addMobileViewport(page, "device-width")
        page = util.addEndHead(page)
        page = util.addStartBody(page)
        return page
    }

    private fun createTitle(page: String, body: List<String>, i: Int, nextLine: Int): String {
        var title = util.addStartDiv("", "titlecontainer")
        title = util.addStartDiv(title, "title")
        title = "$title<h2>Area Forecast Discussion</h2>\n"
        for (j in i + 1..nextLine) {
            title = title + "<em>" + body[j] + "</em>" + "<br/>" + "\n"
        }
        title = util.addEndDiv(title)
        title = util.addEndDiv(title)
        return page + title
    }

    private fun createNav(page: String, office: String): String {
        var page = util.addStartDiv(page, "navcontainer")
        page = util.addStartDiv(page, "nav")
        page = util.addStartDiv(page, "navtitle", "navlarge")
        page = "$page<span>Forecast Discussions</span>"
        page = util.addEndDiv(page)
        page = util.addStartDiv(page, "navtitle-abbrev", "navlarge")
        page += "FD"
        page = util.addEndDiv(page)
        page = util.addStartDiv(page, className = "navsmall")
        page = util.addURL(page, servletContext.contextPath + "?office=ARX", util.addStartDiv("", null, (if (office == "ARX") "current-page" else null)) + "la crosse" + util.addEndDiv(""))
        page = util.addURL(page, servletContext.contextPath + "?office=DLH", util.addStartDiv("", null, (if (office == "DLH") "current-page" else null)) + "duluth" + util.addEndDiv(""))
        page = util.addURL(page, servletContext.contextPath + "?office=PSR", util.addStartDiv("", null, (if (office == "PSR") "current-page" else null)) + "phoenix" + util.addEndDiv(""))
        page = util.addEndDiv(page)
        page = util.addEndDiv(util.addEndDiv(page))
        return page
    }

    private fun createContent(page: String, body: List<String>, firstLine: Int): String {
        var page = util.addStartDiv(page, "container")
        page = util.addStartDiv(page, "content")
        var i = firstLine
        while (i < body.size) {
                        if (body[i].contains("WATCHES/WARNINGS/ADVISORIES")) {
                // The watches/warnings/advisories section has different formatting
                val sectionEnd = util.findEndOfWarnings(body, i)
                page = createWarnings(page, body, i, sectionEnd)
                i = sectionEnd
            } else if (body[i].startsWith(".")) {
                // ".SHORT TERM", ".LONG TERM", etc and following lines
                val sectionEnd = util.findEndOfHeading(body, i)
                page = createHeading(page, body, i, sectionEnd)
                i = sectionEnd
            } else if (body[i] == "&&" || body[i] == "$$" || body[i] == "") {
                // Remove these marks
                i++
                continue
            } else if (body[i - 1] == "") {
                val paragraphEnd = util.findEndOfParagraph(body, i)
                page = createParagraph(page, body, i, paragraphEnd)
                i = paragraphEnd
            }

            i++
        }
        page = createFooter(util.addEndDiv(util.addEndDiv(page)))
        return page
    }

    private fun createParagraph(page: String, body: List<String>, paragraphStart: Int, paragraphEnd: Int): String {
        var paragraph = "<p>" + body[paragraphStart].trim()
        for (j in paragraphStart + 1 until paragraphEnd) {
            paragraph = paragraph + " " + body[j].trim()
        }
        paragraph += "</p>"
        return page + paragraph
    }

    private fun createHeading(page: String, body: List<String>, sectionStart: Int, sectionEnd: Int): String {
        val heading: String = if (body[sectionStart].endsWith("...")) {
            body[sectionStart].substring(1, body[sectionStart].indexOf("..."))
        } else {
            body[sectionStart].substring(1).replace("...", ": ").replace("(", "").replace(")", "")
        }
        var page = "$page<h3>$heading</h3>\n"
        // If there's only one line right after the heading
        // and it doesn't end with a period, it's probably a subheader
        if (sectionEnd - sectionStart == 2) {
            page += if (body[sectionStart + 1].isNotEmpty() && !".,;:?!".contains(body[sectionStart + 1][body[sectionStart + 1].length - 1])) {
                "<em>" + body[sectionStart + 1] + "</em>" + "\n"
            } else {
                "<p>" + body[sectionStart + 1] + "</p>" + "\n"
            }
        } else {
            page += "<p>"
            for (j in sectionStart + 1 until sectionEnd) {
                page += body[j].trim() + " "
            }
            page += "</p>\n"
        }
        return page
    }

    private fun createWarnings(page: String, body: List<String>, sectionStart: Int, sectionEnd: Int): String {
        var page = page + "<h3>" + body[sectionStart].substring(1, body[sectionStart].lastIndexOf("...")).replace("/", ", ") + "</h3>" + "\n"
        page += "<ul>" + "\n"
        for (k in sectionStart until sectionEnd) {
            if (body[k].toLowerCase().startsWith("none")) {
                page += "<li>" + "None." + "</li>" + "\n"
                break
            }
            val parts: Array<String> = body[k].split(("\\.\\.\\.").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (parts.size == 2) {
                page += "<li>" + (if (states.containsKey(parts[0])) states[parts[0]] else parts[0])
                if (parts[1].toLowerCase().startsWith("none")) {
                    page += ": " + "None." + "\n"
                } else {
                    page += "\n" + "<ul>" + "\n"
                    page += "<li>" + parts[1]
                }
            } else if (body[k + 1] == "") {
                page += body[k] + "</li>" + "\n"
            } else if (body[k + 1].contains("...")) {
                page += "</ul>" + "\n"
            } else {
                page += body[k]
            }
        }
        page += "</ul>" + "\n"
        return page
    }

    private fun createFooter(page: String): String {
        val r = Random()
        val footer = ("<div id=\"footercontainer\">\n"
                + "<div id=\"footer\">\n"
                + "<p>" + footers[r.nextInt(footers.size)] + "</p>\n"
                + "</div>\n"
                + "</div>\n")
        return page + footer
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    @Throws(ServletException::class, IOException::class)
    override fun doPost(request: HttpServletRequest, response: HttpServletResponse) {
        doGet(request, response)
    }

    companion object {
        private const val serialVersionUID = 1L
        @JvmStatic
        val footers = arrayOf("oh hey", "hey", "what's up", "hope the weather's real nice bb", "don't get rained on", "nice", "storm's comin", "it'll blow over eventually", "better lay low")
        @JvmStatic
        var states: Map<String, String> = mapOf("MN" to "Minnesota", "WI" to "Wisconsin", "IA" to "Iowa", "LS" to "Lake Superior", "AZ" to "Arizona", "CA" to "California")
    }
}

