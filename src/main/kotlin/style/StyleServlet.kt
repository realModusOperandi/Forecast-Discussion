package style

import java.io.BufferedReader
import java.io.InputStreamReader
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet("/style.css")
class StyleServlet : HttpServlet() {
    companion object {
        const val SerialVersionUID = 1L
        const val BACKGROUND_IMAGE = "%%BACKGROUND_IMAGE%%"
    }

    override fun doGet(request: HttpServletRequest, response: HttpServletResponse): Unit {
        val style = StringBuilder()
        val br = BufferedReader(InputStreamReader(servletContext.getResourceAsStream("/styletemplate.css")))
        br.lines().forEachOrdered { style.append(it.replace(BACKGROUND_IMAGE, "${request.contextPath}/images/background.jpg")).append("\n") }
        response.writer.append(style.toString())
    }

    override fun doPost(request: HttpServletRequest, response: HttpServletResponse): Unit {
        doGet(request, response)
    }
}