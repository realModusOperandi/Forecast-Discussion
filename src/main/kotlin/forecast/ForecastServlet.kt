package forecast

import generate.DiscussionPage
import generate.PageReader
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import java.util.Random
import javax.mail.internet.ContentType
import javax.servlet.ServletException
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.ws.rs.core.MediaType

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
        val page = PageReader.read(body)
        sendResponse(response, DiscussionPage.createPage(page, office, servletContext.contextPath))
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

    private fun sendResponse(response: HttpServletResponse, page: String) {
        try {
            BufferedWriter(response.writer).use { out -> out.write(page) }
        } catch (e: IOException) {
            e.printStackTrace()
        }
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
        val footers = arrayOf("oh hey", "hey", "what's up", "hope the weather's real nice bb", "don't get rained on", "nice", "storm's comin", "it'll blow over eventually", "better lay low", "hail, traveler")
        @JvmStatic
        var states: Map<String, String> = mapOf("MN" to "Minnesota", "WI" to "Wisconsin", "IA" to "Iowa", "LS" to "Lake Superior", "AZ" to "Arizona", "CA" to "California")
    }
}

