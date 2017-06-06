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
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util.MarkupUtil;
import util.ProductUtil;

/**
 * Servlet implementation class CurrentForecastServlet
 */
@WebServlet("")
public class CurrentForecastServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public static final String[] footers = {
			"oh hey",
			"hey",
			"what's up",
			"hope the weather's real nice bb",
			"don't get rained on",
			"nice",
			"storm's comin",
			"it'll blow over eventually",
			"better lay low",
	};

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
		Map <String, String[]> parameters = request.getParameterMap();
		String office = "ARX";
		if (parameters.containsKey("office")) {
			office = parameters.get("office")[0];
		}
		
		boolean debug = false;
		if (parameters.containsKey("debug")) {
			debug = Boolean.valueOf(parameters.get("debug")[0]);
		}
		String productURL = "http://forecast.weather.gov/product.php?site=" + office + "&issuedby=" + office + "&product=AFD&format=txt&version=1&glossary=0";
		String[] contents = getProductText(productURL);
        String[] body = trimContents(contents);
        if (debug) {
        	sendResponse(response, Arrays.stream(body).reduce("", (a, b) -> a + b + "\n"));
        }
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
	        if (contents[i].startsWith("Area Forecast Discussion")) {
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
	    
	    page = createNav(page);
	
	    // "Area Forecast Discussion" and following lines of header
		int firstLine = ProductUtil.findEndOfTitle(body, 0);
		page = createTitle(page, body, 0, firstLine);
		
	    page = createContent(page, body, firstLine);
	    
	    page = MarkupUtil.endPage(page);
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
		String page = MarkupUtil.startHTML("");
		
		page = MarkupUtil.addStartHead(page);
		page = MarkupUtil.addHTMLTitle(page, "Area Forecast Discussion");
		page = MarkupUtil.addStylesheetLink(page, request.getContextPath() + "/style.css");
		page = MarkupUtil.addWebFont(page, "Lora");
		page = MarkupUtil.addMobileViewport(page, "device-width");
		page = MarkupUtil.addEndHead(page);
		
		page = MarkupUtil.addStartBody(page);
		return page;
	}

	private String createTitle(String page, String[] body, int i, int nextLine) {
		String title = MarkupUtil.addStartDivWithId("", "titlecontainer");
		title = MarkupUtil.addStartDivWithId(title, "title");
		title = title + "<h2>" + "Area Forecast Discussion" + "</h2>" + "\n";
		for (int j = i + 1; j <= nextLine; j++) {
			title = title + "<em>" + body[j] + "</em>" + "<br/>" + "\n";
		}
		title = MarkupUtil.addEndDiv(title);
		title = MarkupUtil.addEndDiv(title);
		return page + title;
	}
	
	private String createNav(String page) {
		page = MarkupUtil.addStartDivWithId(page, "navcontainer");
		page = MarkupUtil.addStartDivWithId(page, "nav");
		
		page = MarkupUtil.addStartDiv(page, "navtitle", "navlarge");
		page = page + "Forecast Discussions";
		page = MarkupUtil.addEndDiv(page);
		
		page = MarkupUtil.addStartDivWithClass(page, "navsmall");
		page = MarkupUtil.addURL(page, getServletContext().getContextPath() + "?office=ARX", MarkupUtil.addStartDiv("") + "la crosse wi" + MarkupUtil.addEndDiv(""));
		page = MarkupUtil.addURL(page, getServletContext().getContextPath() + "?office=DLH", MarkupUtil.addStartDiv("") + "duluth mn" + MarkupUtil.addEndDiv(""));
		page = MarkupUtil.addEndDiv(page);
		
		page = MarkupUtil.addEndDiv(MarkupUtil.addEndDiv(page));
		
		return page;
	}

	private String createContent(String page, String[] body, int firstLine) {
		page = MarkupUtil.addStartDivWithId(page, "container");
		page = MarkupUtil.addStartDivWithId(page, "content");
		
		for (int i = firstLine; i < body.length; i++) {
	        if (body[i].contains("WATCHES/WARNINGS/ADVISORIES")) {
	        	// The watches/warnings/advisories section has different formatting
	            int sectionEnd = ProductUtil.findEndOfWarnings(body, i);
	            page = createWarnings(page, body, i, sectionEnd);
	            i = sectionEnd;
	        } else if (body[i].startsWith(".")) {
	            // ".SHORT TERM", ".LONG TERM", etc and following lines
	        	int sectionEnd = ProductUtil.findEndOfHeading(body, i);
	            page = createHeading(page, body, i, sectionEnd);
	            i = sectionEnd;
	        } else if (body[i].equals("&&") || body[i].equals("$$") || body[i].equals("")) {
	            // Remove these marks
	            continue;
	        } else if (body[i-1].equals("")) {
	        	int paragraphEnd = ProductUtil.findEndOfParagraph(body, i);
	        	
	        	page = createParagraph(page, body, i, paragraphEnd);
	        	i = paragraphEnd;
	        }
	    }
		page = createFooter(MarkupUtil.addEndDiv(MarkupUtil.addEndDiv(page)));
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
		page += "<h3>" + body[sectionStart].substring(1, body[sectionStart].length() - 3).replace("/",  ", ") + "</h3>" + "\n";
		page += "<ul>" + "\n";
		for (int k = sectionStart; k < sectionEnd; k++ ) {
			if (body[k].toLowerCase().startsWith("none")) {
				page += "<li>" + "None." + "</li>" + "\n";
				break;
			}
		    String[] parts;
		    if ((parts = body[k].split("\\.\\.\\.")).length == 2) {
		        page += "<li>" + (states.containsKey(parts[0]) ? states.get(parts[0]) : parts[0]);
		        if (parts[1].toLowerCase().startsWith("none")) {
		        	page += ": " + "None." + "\n";
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
	
	private String createFooter(String page) {
		Random r = new Random();
		String footer = "<div id=\"footercontainer\">\n"
				+ "<div id=\"footer\">\n"
				+ "<p>" + footers[r.nextInt(footers.length)] + "</p>\n"
				+ "</div>\n"
				+ "</div>\n";
		
		return page + footer;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}
