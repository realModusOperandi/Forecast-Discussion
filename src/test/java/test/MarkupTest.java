package test;

import static org.junit.Assert.*;

import org.junit.Test;
import util.MarkupUtil;

public class MarkupTest {

	private static final String SOME_TEST_STRING = "hi";
	private static final String TEST_STARTED_HEAD = "<html><head>";
	private static final String TEST_COMPLETE_HEAD = "<html><head><title>fart</title></head>";
	private static final String TEST_STARTED_BODY = "<html><head><title>fart</title></head><body>";
	private static final String TEST_STARTED_DIV = "<html><head><title>fart</title></head><body><div>";
	private static final String TEST_STARTED_NAV = "<html><head><title>fart</title></head><body><nav>";

	@Test
	public void testStartHTML() {
		String valid = SOME_TEST_STRING + "<html>\n";
		String result = MarkupUtil.startHTML(SOME_TEST_STRING);
		assertEquals(getErrorMessage(valid, result), valid, result);
	}
	
	@Test
	public void testEndHTML() {
		String valid = SOME_TEST_STRING + "</html>\n";
		String result = MarkupUtil.addEndHTML(SOME_TEST_STRING);
		assertEquals(getErrorMessage(valid, result), valid, result);
	}
	
	@Test
	public void testStartHead() {
		String valid = "hi<head>\n";
		String result = MarkupUtil.addStartHead(SOME_TEST_STRING);
		assertEquals(getErrorMessage(valid, result), valid, result);
	}
	
	@Test
	public void testEndHead() {
		String valid = SOME_TEST_STRING + "</head>\n";
		String result = MarkupUtil.addEndHead(SOME_TEST_STRING);
		assertEquals(getErrorMessage(valid, result), valid, result);
	}
	
	@Test
	public void testHTMLTitle() {
		String valid = TEST_STARTED_HEAD + "<title>hi</title>\n";
		String result = MarkupUtil.addHTMLTitle(TEST_STARTED_HEAD, SOME_TEST_STRING);
		assertEquals(getErrorMessage(valid, result), valid, result);
	}
	
	@Test
	public void testMobileViewportIntWidth() {
		String valid = TEST_STARTED_HEAD + "<meta name=\"viewport\" content=\"width=1024, initial-scale=1.0\">\n";
		String result = MarkupUtil.addMobileViewport(TEST_STARTED_HEAD, 1024);
		assertEquals(getErrorMessage(valid, result), valid, result);
	}
	
	@Test
	public void testMobileViewportStringWidth() {
		String valid = TEST_STARTED_HEAD + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n";
		String result = MarkupUtil.addMobileViewport(TEST_STARTED_HEAD, "device-width");
		assertEquals(getErrorMessage(valid, result), valid, result);
	}
	
	@Test
	public void testStylesheetLink() {
		String valid = TEST_STARTED_HEAD + "<link rel=\"stylesheet\" type=\"text/css\" href=\"/style.css\"/>\n";
		String result = MarkupUtil.addStylesheetLink(TEST_STARTED_HEAD, "/style.css");
		assertEquals(getErrorMessage(valid, result), valid, result);
	}
	
	@Test
	public void testWebFont() {
		String valid = TEST_STARTED_HEAD + "<link rel=\"stylesheet\" href=\"https://fonts.googleapis.com/css?family=Lora\"/>\n";
		String result = MarkupUtil.addWebFont(TEST_STARTED_HEAD, "Lora");
		assertEquals(getErrorMessage(valid, result), valid, result);
	}
	
	@Test
	public void testStartBody() {
		String valid = TEST_COMPLETE_HEAD + "<body>\n";
		String result = MarkupUtil.addStartBody(TEST_COMPLETE_HEAD);
		assertEquals(getErrorMessage(valid, result), valid, result);
	}
	
	@Test
	public void testEndBody() {
		String valid = TEST_STARTED_BODY + "</body>\n";
		String result = MarkupUtil.addEndBody(TEST_STARTED_BODY);
		assertEquals(getErrorMessage(valid, result), valid, result);
	}
	
	@Test
	public void testStartDivWithId() {
		String valid = TEST_STARTED_BODY + "<div id=\"content\">\n";
		String result = MarkupUtil.addStartDivWithId(TEST_STARTED_BODY, "content");
		assertEquals(getErrorMessage(valid, result), valid, result);
	}
	
	@Test
	public void testStartDivWithClass() {
		String valid = TEST_STARTED_BODY + "<div class=\"content\">\n";
		String result = MarkupUtil.addStartDivWithClass(TEST_STARTED_BODY, "content");
		assertEquals(getErrorMessage(valid, result), valid, result);
	}
	
	@Test
	public void testStartDiv() {
		String valid = TEST_STARTED_BODY + "<div id=\"tableOfContents\" class=\"largeList\">\n";
		String result = MarkupUtil.addStartDiv(TEST_STARTED_BODY, "tableOfContents", "largeList");
		assertEquals(getErrorMessage(valid, result), valid, result);
	}
	
	@Test
	public void testStartDivNoParams() {
		String valid = TEST_STARTED_BODY + "<div>\n";
		String result = MarkupUtil.addStartDiv(TEST_STARTED_BODY);
		assertEquals(getErrorMessage(valid, result), valid, result);
	}
	
	@Test
	public void testEndDiv() {
		String valid = TEST_STARTED_DIV + "</div>\n";
		String result = MarkupUtil.addEndDiv(TEST_STARTED_DIV);
		assertEquals(getErrorMessage(valid, result), valid, result);
	}
	
	@Test
	public void testAddURL() {
		String valid = TEST_STARTED_BODY + "<a href=\"https://forecastdiscussions.mybluemix.net\">Forecast Discussions</a>";
		String result = MarkupUtil.addURL(TEST_STARTED_BODY, "https://forecastdiscussions.mybluemix.net", "Forecast Discussions");
		assertEquals(getErrorMessage(valid, result), valid, result);
	}
	
	@Test
	public void testStartNavWithId() {
		String valid = TEST_STARTED_BODY + "<nav id=\"content\">\n";
		String result = MarkupUtil.addStartNavWithId(TEST_STARTED_BODY, "content");
		assertEquals(getErrorMessage(valid, result), valid, result);
	}
	
	@Test
	public void testStartNavWithClass() {
		String valid = TEST_STARTED_BODY + "<nav class=\"content\">\n";
		String result = MarkupUtil.addStartNavWithClass(TEST_STARTED_BODY, "content");
		assertEquals(getErrorMessage(valid, result), valid, result);
	}
	
	@Test
	public void testStartNav() {
		String valid = TEST_STARTED_BODY + "<nav id=\"tableOfContents\" class=\"largeList\">\n";
		String result = MarkupUtil.addStartNav(TEST_STARTED_BODY, "tableOfContents", "largeList");
		assertEquals(getErrorMessage(valid, result), valid, result);
	}
	
	@Test
	public void testStartNavNoParams() {
		String valid = TEST_STARTED_BODY + "<nav>\n";
		String result = MarkupUtil.addStartNav(TEST_STARTED_BODY);
		assertEquals(getErrorMessage(valid, result), valid, result);
	}
	
	@Test
	public void testEndNav() {
		String valid = TEST_STARTED_NAV + "</nav>\n";
		String result = MarkupUtil.addEndNav(TEST_STARTED_NAV);
		assertEquals(getErrorMessage(valid, result), valid, result);
	}
	
	public String getErrorMessage(String expected, String result) {
		return "Expected \"" + expected + "\" but got \"" + result + "\".";
	}

}
