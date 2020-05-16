package test

import generate.DiscussionPage
import generate.PageReader
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import util.trimContents
import java.io.File

class ForecastTest {
    @Test
    fun verifyCorrectHTML() {
        println(this.javaClass.classLoader.getResource("test/discussionInput.html"))
        val inputHTML = File(this.javaClass.classLoader.getResource("test/discussionInput.html").file).readLines()
        val expectedHTML = File(this.javaClass.classLoader.getResource("test/expectedDiscussionOutput.html").file).readText()
        val lines = trimContents(inputHTML)
        val page = PageReader.read(lines)
        val actualHTML = DiscussionPage.createPage(page, "ARX", "", false)
        Assertions.assertEquals(expectedHTML, actualHTML)
    }
}