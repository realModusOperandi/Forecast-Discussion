package style;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class StyleServlet
 */
@WebServlet("/style.css")
public class StyleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String BACKGROUND_IMAGE = "%%BACKGROUND_IMAGE%%";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StyleServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StringBuilder style = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(getServletContext().getResourceAsStream("/styletemplate.css")))) {
			br.lines().forEachOrdered(s -> style.append(s.replace(BACKGROUND_IMAGE, request.getContextPath() + "/images/background.jpg")).append("\n"));
		}
		response.getWriter().append(style.toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
