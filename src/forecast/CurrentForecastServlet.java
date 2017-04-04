package forecast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
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
@WebServlet("")
public class CurrentForecastServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

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
		String productURL = "http://forecast.weather.gov/product.php?site=ARX&issuedby=ARX&product=AFD&format=txt&version=1&glossary=0";
		
		String[] contents = getProductText(productURL);
        String[] body = trimContents(contents);
        String page = createPage(request, body);
        sendResponse(response, page);
	}

	private String[] getProductText(String productURL) {
		URL url = null;
	    try {
			url = new URL(productURL);
	    } catch (MalformedURLException e) {
	        e.printStackTrace();
	    }
	
	    String[] contents = new String[0];
	    try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()))) {
	        contents = in.lines().toArray(String[]::new);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
		return contents;
	}

	private String[] trimContents(String[] contents) {
		int firstLine = 0, lastLine = 0;
	
	    for (int i = 0; i < contents.length; i++) {
	        if (contents[i].equalsIgnoreCase("Area Forecast Discussion")) {
	            firstLine = i;
	        } else if (contents[i].equals("$$")) {
	            lastLine = i - 2;
	        }
	    }
	    
	    String[] body = Arrays.copyOfRange(contents, firstLine, lastLine+1);
		return body;
	}

	private String createPage(HttpServletRequest request, String[] body) {
	    String page = beginPage(request);
	
	    // "Area Forecast Discussion" and following lines of header
		int firstLine = findEndOfTitle(body, 0);
		page = createTitle(page, body, 0, firstLine);
		
	    page = createContent(page, body, firstLine);
	    
	    page = endPage(page);
		return page;
	}

	private void sendResponse(HttpServletResponse response, String page) {
		try (BufferedWriter out = new BufferedWriter(response.getWriter())) {
            out.write(page);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	private String beginPage(HttpServletRequest request) {
		String page = startHTML("");
		
		page = addStartHead(page);
		page = addHTMLTitle(page, "Area Forecast Discussion");
		page = addStylesheetLink(page, request.getContextPath() + "/style.css");
		page = addMobileViewport(page, 800);
		page = addEndHead(page);
		
		page = addStartBody(page);
		return page;
	}

	private String createTitle(String page, String[] body, int i, int nextLine) {
		String title = addStartDivWithId("", "titlecontainer");
		title = addStartDivWithId(title, "title");
		title = title + "<h2>" + body[i] + "</h2>" + "\n";
		for (int j = i + 1; j <= nextLine; j++) {
			title = title + "<em>" + body[j] + "</em>" + "<br/>" + "\n";
		}
		title = addEndDiv(title);
		title = addEndDiv(title);
		return page + title;
	}

	private String createContent(String page, String[] body, int firstLine) {
		page = addStartDivWithId(page, "container");
		page = addStartDivWithId(page, "content");
		
		for (int i = firstLine; i < body.length; i++) {
	        if (body[i].contains("ARX WATCHES/WARNINGS/ADVISORIES")) {
	        	// The watches/warnings/advisories section has different formatting
	            int sectionEnd = findEndOfWarnings(body, i);
	            page = createWarnings(page, body, i, sectionEnd);
	            i = sectionEnd;
	        } else if (body[i].startsWith(".")) {
	            // ".SHORT TERM", ".LONG TERM", etc and following lines
	        	int sectionEnd = findEndOfHeading(body, i);
	            page = createHeading(page, body, i, sectionEnd);
	            i = sectionEnd;
	        } else if (body[i].equals("&&") || body[i].equals("$$") || body[i].equals("")) {
	            // Remove these marks
	            continue;
	        } else if (body[i-1].equals("")) {
	        	int paragraphEnd = findEndOfParagraph(body, i);
	        	
	        	page = createParagraph(page, body, i, paragraphEnd);
	        	i = paragraphEnd;
	        }
	    }
	
	    page = addEndDiv(addEndDiv(page));
		return page;
	}

	private String createParagraph(String page, String[] body, int paragraphStart, int paragraphEnd) {
		String paragraph = "<p>" + body[paragraphStart];
		for (int j = paragraphStart + 1; j < paragraphEnd; j++) {
			paragraph = paragraph + "\n" + body[j];
		}
		paragraph = paragraph + "</p>";
		
		page = page + paragraph;
		return page;
	}

	private String createHeading(String page, String[] body, int sectionStart, int sectionEnd) {
		page += "<h3>" + body[sectionStart].substring(1).replace("...", ": ").replace("(", "").replace(")", "") + "</h3>" + "\n";
		
		for (int j = sectionStart + 1; j < sectionEnd; j++) {
			page += "<em>" + body[j] + "</em>" + "\n";
		}
		return page;
	}

	private String createWarnings(String page, String[] body, int sectionStart, int sectionEnd) {
		page += "<h3>" + body[sectionStart].substring(1, body[sectionStart].length() - 3) + "</h3>" + "\n";
		page += "<ul>" + "\n";
		for (int k = sectionStart; k < sectionEnd; k++ ) {
			if (body[k].equals("NONE.")) {
				page += "<li>" + "None." + "</li>" + "\n";
				break;
			}
		    String[] parts;
		    if ((parts = body[k].split("\\.\\.\\.")).length == 2) {
		        page += "<li>" + states.get(parts[0]);
		        if (parts[1].equals("None.")) {
		        	page += ": " + parts[1] + "\n";
		        } else {
		        	page += "\n" + "<ul>" + "\n";
		            page += "<li>" + parts[1];
		        }
		        
		    } else if (body[k+1].equals("")) {
		        page += body[k] + "</li>" + "\n";
		    } else if (body[k+1].contains("...")) {
		        page += "</ul>" + "\n";
		    }

		    else {
		        page += body[k];
		    }
		}
		
		page += "</ul>" + "\n";
		return page;
	}

	private String endPage(String page) {
        page = addEndBody(page);
        page = addEndHTML(page);
        return page;
	}

	private String startHTML(String input) {
		return input + "<html>\n";
	}
	
	private String addEndHTML(String page) {
		return page + "</html>\n";
	}

	private String addStartHead(String input) {
		return input + "<head>\n";
	}
	
	private String addEndHead(String input) {
		return input + "</head>\n";
	}

	private String addHTMLTitle(String input, String title) {
		return input + "<title>" + title + "</title>\n";
	}
	
	private String addMobileViewport(String input, int width) {
		return input + "<meta name=\"viewport\" content=\"width=" + width + "\">\n";
	}
	
	private String addStylesheetLink(String input, String styleURL) {
		return input + "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + styleURL + "\"/>\n";
	}
	
	private String addStartBody(String input) {
		return input + "<body>\n";
	}
	
	private String addEndBody(String page) {
		return page + "</body>\n";
	}

	private String addStartDivWithId(String input, String id) {
		return addStartDiv(input, id, null);
	}
	
	private String addStartDiv(String input, String id, String className) {
		String div = "<div";
		if (id != null) {
			div = div + " id=\"" + id +"\"";
		}
		
		if (className != null) {
			div = div + " class=\"" + className + "\"";
		}
		
		div = div + ">\n";
		return input + div;
	}
	
	private String addEndDiv(String input) {
		return input + "</div>\n";
	}
	
	protected boolean supportsFixedBackground(HttpServletRequest request) {
		String agent = request.getHeader("User-Agent");
		return !agent.contains("iPhone") || agent.contains("iPad") || agent.contains("iPod");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	protected String contextPath(HttpServletRequest request) {
		return request.getContextPath();
	}

	private int findEndOfTitle(String[] body, int startOfTitle) {
		int nextLine = findEndOfHeading(body, startOfTitle);
		return nextLine;
	}

	private int findEndOfHeading(String[] body, int i) {
		int sectionEnd = findEndOfParagraph(body, i);
		return sectionEnd;
	}

	private int findEndOfParagraph(String[] body, int i) {
		int paragraphEnd = i;
		while (!body[paragraphEnd].equals("")) {
			paragraphEnd++;
		}
		return paragraphEnd;
	}

	private int findEndOfWarnings(String[] body, int start) {
		int sectionEnd = start;
		while (!body[sectionEnd].equals("&&")) {
			sectionEnd++;
		}
		return sectionEnd;
	}

}
