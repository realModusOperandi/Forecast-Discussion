package style

import util.getStylesheet
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet("/style.css")
class StyleServlet : HttpServlet() {
    companion object {
        const val BACKGROUND_IMAGE = "%%BACKGROUND_IMAGE%%"
    }

    override fun doGet(request: HttpServletRequest, response: HttpServletResponse) {
        val style = getStylesheet(this.javaClass.classLoader.getResourceAsStream("style/styletemplate.css"), request.contextPath)
        response.writer.append(style)
    }

    override fun doPost(request: HttpServletRequest, response: HttpServletResponse) {
        doGet(request, response)
    }
}