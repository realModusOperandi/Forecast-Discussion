package test

import generate.DiscussionPage
import generate.PageReader
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import util.trimContents
import java.io.File

class ForecastTest {
    @Test
    fun verifyCorrectHTML() {
        val inputHTML = File(this.javaClass.classLoader.getResource("test/discussionInput.html")!!.file).readLines()
        val expectedHTML = File(this.javaClass.classLoader.getResource("test/expectedDiscussionOutput.html")!!.file).readText()
        val lines = trimContents(inputHTML)
        val page = PageReader.read(lines)
        val actualHTML = DiscussionPage.createPage(page, "ARX", false)
        assertEquals(expectedHTML, actualHTML)
    }

    @Test
    fun testGetProduct() {
        val office = "ARX"
        val productURL = "https://forecast.weather.gov/product.php?site=$office&issuedby=$office&product=AFD&format=txt&version=1&glossary=0"
        val result = assertDoesNotThrow { util.getProductText(productURL) }
        assert(result.size > 10) { "Did not receive long enough product text\n$result" }
    }

    @Test
    fun testGetStylesheet() {
        val expectedCSS = File(this.javaClass.classLoader.getResource("test/expectedStylesheet.css")!!.file).readText()
        val actualCSS = util.getStylesheet(this.javaClass.classLoader.getResourceAsStream("style/styletemplate.css")!!)
        assertEquals(expectedCSS, actualCSS)
    }
}