package forecast

import generate.DiscussionPage
import generate.PageReader
import util.getProductText
import util.trimContents
import java.io.BufferedWriter
import java.io.IOException
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
        val page = PageReader.read(body)
        sendResponse(response, DiscussionPage.createPage(page, office, servletContext.contextPath))
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
    }
}

