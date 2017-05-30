package util;

public class ProductUtil {
	public static int findEndOfTitle(String[] body, int startOfTitle) {
		int nextLine = findEndOfHeading(body, startOfTitle);
		return nextLine;
	}

	public static int findEndOfHeading(String[] body, int i) {
		int sectionEnd = findEndOfParagraph(body, i);
		return sectionEnd;
	}

	public static int findEndOfParagraph(String[] body, int i) {
		int paragraphEnd = i;
		while (paragraphEnd < body.length && !body[paragraphEnd].equals("")) {
			paragraphEnd++;
		}
		return paragraphEnd;
	}

	public static int findEndOfWarnings(String[] body, int start) {
		int sectionEnd = start;
		while (sectionEnd < body.length && !body[sectionEnd].equals("&&")) {
			sectionEnd++;
		}
		return sectionEnd;
	}
}
