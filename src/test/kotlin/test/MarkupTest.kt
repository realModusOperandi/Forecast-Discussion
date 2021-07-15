package test

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import util.*

class MarkupTest {
    @Test
    fun testStartHTML() {
        val valid = "$SOME_TEST_STRING<html lang=\"en-US\">\n"
        val result = startHTML(SOME_TEST_STRING)
        assertEquals(valid, result, getErrorMessage(valid, result))
    }

    @Test
    fun testEndHTML() {
        val valid = "$SOME_TEST_STRING</html>\n"
        val result = addEndHTML(SOME_TEST_STRING)
        assertEquals(valid, result, getErrorMessage(valid, result))
    }

    @Test
    fun testStartHead() {
        val valid = "hi<head>\n"
        val result = addStartHead(SOME_TEST_STRING)
        assertEquals(valid, result, getErrorMessage(valid, result))
    }

    @Test
    fun testEndHead() {
        val valid = "$SOME_TEST_STRING</head>\n"
        val result = addEndHead(SOME_TEST_STRING)
        assertEquals(valid, result, getErrorMessage(valid, result))
    }

    @Test
    fun testHTMLTitle() {
        val valid = "$TEST_STARTED_HEAD<title>hi</title>\n"
        val result = addHTMLTitle(TEST_STARTED_HEAD, SOME_TEST_STRING)
        assertEquals(valid, result, getErrorMessage(valid, result))
    }

    @Test
    fun testMobileViewportIntWidth() {
        val valid = "$TEST_STARTED_HEAD<meta name=\"viewport\" content=\"width=1024, initial-scale=1.0 viewport-fit=cover\">\n"
        val result = addMobileViewport(TEST_STARTED_HEAD, 1024)
        assertEquals(valid, result, getErrorMessage(valid, result))
    }

    @Test
    fun testMobileViewportStringWidth() {
        val valid = "$TEST_STARTED_HEAD<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0 viewport-fit=cover\">\n"
        val result = addMobileViewport(TEST_STARTED_HEAD, "device-width")
        assertEquals(valid, result, getErrorMessage(valid, result))
    }

    @Test
    fun testStylesheetLink() {
        val valid = "$TEST_STARTED_HEAD<link rel=\"stylesheet\" type=\"text/css\" href=\"/style.css\"/>\n"
        val result = addStylesheetLink(TEST_STARTED_HEAD, "/style.css")
        assertEquals(valid, result, getErrorMessage(valid, result))
    }

    @Test
    fun testWebFont() {
        val valid = "$TEST_STARTED_HEAD<link rel=\"stylesheet\" href=\"https://fonts.googleapis.com/css2?family=Lora\"/>\n"
        val result = addWebFont(TEST_STARTED_HEAD, "Lora")
        assertEquals(valid, result, getErrorMessage(valid, result))
    }

    @Test
    fun testStartBody() {
        val valid = "$TEST_COMPLETE_HEAD<body>\n"
        val result = addStartBody(TEST_COMPLETE_HEAD)
        assertEquals(valid, result, getErrorMessage(valid, result))
    }

    @Test
    fun testEndBody() {
        val valid = "$TEST_STARTED_BODY</body>\n"
        val result = addEndBody(TEST_STARTED_BODY)
        assertEquals(valid, result, getErrorMessage(valid, result))
    }

    @Test
    fun testStartDivWithId() {
        val valid = "$TEST_STARTED_BODY<div id=\"content\">\n"
        val result = addStartDiv(TEST_STARTED_BODY, "content", null)
        assertEquals(valid, result, getErrorMessage(valid, result))
    }

    @Test
    fun testStartDivWithClass() {
        val valid = "$TEST_STARTED_BODY<div class=\"content\">\n"
        val result = addStartDiv(TEST_STARTED_BODY, null, "content")
        assertEquals(valid, result, getErrorMessage(valid, result))
    }

    @Test
    fun testStartDiv() {
        val valid = "$TEST_STARTED_BODY<div id=\"tableOfContents\" class=\"largeList\">\n"
        val result = addStartDiv(TEST_STARTED_BODY, "tableOfContents", "largeList")
        assertEquals(valid, result, getErrorMessage(valid, result))
    }

    @Test
    fun testStartDivNoParams() {
        val valid = "$TEST_STARTED_BODY<div>\n"
        val result = addStartDiv(TEST_STARTED_BODY, null, null)
        assertEquals(valid, result, getErrorMessage(valid, result))
    }

    @Test
    fun testEndDiv() {
        val valid = "$TEST_STARTED_DIV</div>\n"
        val result = addEndDiv(TEST_STARTED_DIV)
        assertEquals(valid, result, getErrorMessage(valid, result))
    }

    @Test
    fun testAddURL() {
        val valid = "$TEST_STARTED_BODY<a href=\"https://forecastdiscussions.mybluemix.net\">Forecast Discussions</a>"
        val result = addURL(TEST_STARTED_BODY, "https://forecastdiscussions.mybluemix.net", "Forecast Discussions")
        assertEquals(valid, result, getErrorMessage(valid, result))
    }

    @Test
    fun testStartNavWithId() {
        val valid = "$TEST_STARTED_BODY<nav id=\"content\">\n"
        val result = addStartNav(TEST_STARTED_BODY, "content", null)
        assertEquals(valid, result, getErrorMessage(valid, result))
    }

    @Test
    fun testStartNavWithClass() {
        val valid = "$TEST_STARTED_BODY<nav class=\"content\">\n"
        val result = addStartNav(TEST_STARTED_BODY, null, "content")
        assertEquals(valid, result, getErrorMessage(valid, result))
    }

    @Test
    fun testStartNav() {
        val valid = "$TEST_STARTED_BODY<nav id=\"tableOfContents\" class=\"largeList\">\n"
        val result = addStartNav(TEST_STARTED_BODY, "tableOfContents", "largeList")
        assertEquals(valid, result, getErrorMessage(valid, result))
    }

    @Test
    fun testStartNavNoParams() {
        val valid = "$TEST_STARTED_BODY<nav>\n"
        val result = addStartNav(TEST_STARTED_BODY, null, null)
        assertEquals(valid, result, getErrorMessage(valid, result))
    }

    @Test
    fun testEndNav() {
        val valid = "$TEST_STARTED_NAV</nav>\n"
        val result = addEndNav(TEST_STARTED_NAV)
        assertEquals(valid, result, getErrorMessage(valid, result))
    }

    private fun getErrorMessage(expected: String, result: String): String {
        return "Expected \"$expected\" but got \"$result\"."
    }

    companion object {
        private const val SOME_TEST_STRING = "hi"
        private const val TEST_STARTED_HEAD = "<html><head>"
        private const val TEST_COMPLETE_HEAD = "<html><head><title>fart</title></head>"
        private const val TEST_STARTED_BODY = "<html><head><title>fart</title></head><body>"
        private const val TEST_STARTED_DIV = "<html><head><title>fart</title></head><body><div>"
        private const val TEST_STARTED_NAV = "<html><head><title>fart</title></head><body><nav>"
    }
}