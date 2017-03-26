package forecast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class CurrentForecastServlet
 */
@WebServlet("/current")
public class CurrentForecastServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String PAGE_BEGIN = "<html>\n" +
            "<head>\n" +
			"<link rel=\"stylesheet\" type=\"text/css\" href=\"%%STYLESHEET%%\" />\n" +
            "<title>Area Forecast Discussion</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "<div id=\"content\">\n";

    public static final String PAGE_END = "</div>\n" +
    		"</body>\n" +
            "</html>";

    public static Map<String, String> states = new HashMap<>();

    static {
        states.put("MN", "Minnesota");
        states.put("WI", "Wisconsin");
        states.put("IA", "Iowa");
    }
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CurrentForecastServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		
		URL url = null;
        try {
            url = new URL("http://forecast.weather.gov/product.php?site=ARX&issuedby=ARX&product=AFD&format=txt&version=1&glossary=0");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        String[] contents = new String[0];
        try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()))) {
            contents = in.lines().toArray(String[]::new);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        int firstLine = 0, lastLine = 0;

        for (int i = 0; i < contents.length; i++) {
            if (contents[i].equals("Area Forecast Discussion")) {
                firstLine = i;
            } else if (contents[i].equals("$$")) {
                lastLine = i - 2;
            }
        }

        StringBuilder output = new StringBuilder();
        output.append(PAGE_BEGIN
        		.replace("%%STYLESHEET%%", request.getContextPath() + "/style.css")
        		.replace("%%BACKGROUND_IMAGE%%", request.getContextPath() + "/images/background.jpg"));

        for (int i = firstLine; i <= lastLine; i++) {
            if (i == firstLine) {
                // "Area Forecast Discussion" and following lines of header
                output.append("<h2>").append(contents[i]).append("</h2>").append("\n");
                output.append("<em>").append(contents[i+1]).append("</em>").append("<br/>").append("\n");
                output.append("<em>").append(contents[i+2]).append("</em>").append("\n");
                i += 2;
            } else if (contents[i].contains("ARX WATCHES/WARNINGS/ADVISORIES")) {
                int j = i+1;
                output.append("<h3>").append(contents[i].substring(1, contents[i].length() - 3)).append("</h3>").append("\n");
                output.append("<ul>").append("\n");
                while (!contents[j].equals("&&")) {
                    String[] parts;
                    if ((parts = contents[j].split("\\.\\.\\.")).length == 2) {
                        output.append("<li>").append(states.get(parts[0]));
                        if (parts[1].equals("None.")) {
                        	output.append(": ").append(parts[1]).append("\n");
                        } else {
                        	output.append("\n").append("<ul>").append("\n");
                            output.append("<li>").append(parts[1]);
                        }
                        
                    } else if (contents[j-1].equals("")){
                        output.append("<li>").append(contents[j]);
                    } else if (contents[j+1].equals("")) {
                        output.append(contents[j]).append("</li>").append("\n");
                    } else if (contents[j+1].contains("...")) {
                        output.append("</ul>").append("\n");
                    }

                    else {
                        output.append(contents[j]);
                    }

                    j++;
                }
                output.append("</ul>").append("\n");
                i += j;
            } else if (contents[i].startsWith(".")) {
                // ".SHORT TERM", ".LONG TERM", etc and following lines
                output.append("<h3>").append(contents[i].substring(1).replace("...", ": ").replace("(", "").replace(")", "")).append("</h3>").append("\n");
                output.append("<em>").append(contents[i+1]).append("</em>").append("\n");
                i += 1;
            } else if (contents[i].equals("&&") || contents[i].equals("$$")) {
                // Remove these marks
                continue;
            } else if (contents[i-1].equals("")) {
                // First line of a paragraph so start the <p>
                output.append("<p>").append(contents[i]).append(" ");
            } else if (contents[i+1].equals("")) {
                // Last line of a paragraph, so end the <p>
                output.append(contents[i]).append("</p>").append("\n");
            } else if (contents[i].equals("")) {
                continue;
            } else {
                output.append(contents[i]).append(" ");
            }
        }

        output.append(PAGE_END);

        try (BufferedWriter out = new BufferedWriter(response.getWriter())) {
            out.write(output.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
