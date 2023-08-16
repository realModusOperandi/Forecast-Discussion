package style

import util.getStylesheet
import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

@WebServlet("/style.css")
class StyleServlet : HttpServlet() {
    companion object {
        const val BACKGROUND_IMAGE = "%%BACKGROUND_IMAGE%%"
    }

    override fun doGet(request: HttpServletRequest, response: HttpServletResponse) {
        response.contentType = "text/css"

        val style = getStylesheet(this.javaClass.classLoader.getResourceAsStream("style/styletemplate.css")!!)
        response.writer.append(style)
    }

    override fun doPost(request: HttpServletRequest, response: HttpServletResponse) {
        doGet(request, response)
    }
}