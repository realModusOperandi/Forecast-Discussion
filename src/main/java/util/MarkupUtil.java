package util;

import javax.servlet.http.HttpServletRequest;

public class MarkupUtil {
	public static String endPage(String page) {
        page = addEndBody(page);
        page = addEndHTML(page);
        return page;
	}

	public static String startHTML(String input) {
		return input + "<html>\n";
	}
	
	public static String addEndHTML(String page) {
		return page + "</html>\n";
	}

	public static String addStartHead(String input) {
		return input + "<head>\n";
	}
	
	public static String addEndHead(String input) {
		return input + "</head>\n";
	}

	public static String addHTMLTitle(String input, String title) {
		return input + "<title>" + title + "</title>\n";
	}
	
	public static String addMobileViewport(String input, int width) {
		return input + "<meta name=\"viewport\" content=\"width=" + width + ", initial-scale=1.0\">\n";
		//return input + "<meta name=\"viewport\" content=\"width=" + width + "\">\n";
	}
	
	public static String addMobileViewport(String input, String widthValue) {
		return input + "<meta name=\"viewport\" content=\"width=" + widthValue + ", initial-scale=1.0\">\n";
	}
	
	public static String addStylesheetLink(String input, String styleURL) {
		return input + "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + styleURL + "\"/>\n";
	}
	
	public static String addWebFont(String input, String fontName) {
		return input + "<link rel=\"stylesheet\" href=\"https://fonts.googleapis.com/css?family=" + fontName + "\"/>\n";
	}
	
	public static String addStartBody(String input) {
		return input + "<body>\n";
	}
	
	public static String addEndBody(String page) {
		return page + "</body>\n";
	}
	
	public static String addStartDiv(String input) {
		return addStartDiv(input, null, null);
	}

	public static String addStartDivWithId(String input, String id) {
		return addStartDiv(input, id, null);
	}
	
	public static String addStartDivWithClass(String input, String className) {
		return addStartDiv(input, null, className);
	}
	
	public static String addStartDiv(String input, String id, String className) {
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
	
	public static String addEndDiv(String input) {
		return input + "</div>\n";
	}
	
	public static String addStartNav(String input) {
		return addStartNav(input, null, null);
	}

	public static String addStartNavWithId(String input, String id) {
		return addStartNav(input, id, null);
	}
	
	public static String addStartNavWithClass(String input, String className) {
		return addStartNav(input, null, className);
	}
	
	public static String addStartNav(String input, String id, String className) {
		String nav = "<nav";
		if (id != null) {
			nav = nav + " id=\"" + id +"\"";
		}
		
		if (className != null) {
			nav = nav + " class=\"" + className + "\"";
		}
		
		nav = nav + ">\n";
		return input + nav;
	}
	
	public static String addEndNav(String input) {
		return input + "</nav>\n";
	}
	
	public static String addURL(String input, String url, String text) {
		return input + "<a href=\"" + url + "\">" + text + "</a>";
	}



	
}
