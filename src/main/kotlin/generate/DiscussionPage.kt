package generate

import forecast.CurrentForecastServlet
import model.Page
import model.Paragraph
import model.Title
import java.util.*

class DiscussionPage {
    companion object {
        fun createPage(page: Page, office: String, contextPath: String): String {
            var result = beginPage(contextPath)
            result = createNav(result, office, contextPath)
            result = createTitle(result, page)
            result = createContent(result, page)
            result = util.endPage(result)
            return result
        }

        fun createTitle(result: String, page: Page): String {
            var title = util.addStartDiv("", "titlecontainer")
            title = util.addStartDiv(title, "title")
            title = "$title<h2>${page.title.title}</h2>\n"
            for (subtitle in page.title.subtitles) {
                title = "$title<em>$subtitle</em><br/>\n"
            }
            title = util.addEndDiv(title)
            title = util.addEndDiv(title)
            return result + title
        }

        fun createContent(result: String, page: Page): String {
            var content = result
            content = util.addStartDiv(content, "container")
            content = util.addStartDiv(content, "content")

            for (section in page.sections) {
                content = createHeading(content, section.header)
                content = createBody(content, section.body)
            }

            for (section in page.listSections) {
                content = createHeading(content, section.header)
                content = createWarnings(content, section.items)
            }

            content = createFooter(util.addEndDiv(util.addEndDiv(content)))
            return content
        }

        fun createHeading(result: String, heading: Title): String {
            val mainHeading: String = heading.title
            var head = "$result<h3>$mainHeading</h3>\n"

            for (subheading in heading.subtitles) {
                head += "<em>$subheading</em>\n"
            }
            return head
        }

        fun createBody(result: String, body: List<Paragraph>): String {
            var text = result
            for (p in body) {
                text = "$text<p>${p.text}</p>\n"
            }
            return text
        }

        fun createWarnings(result: String, items: List<String>): String {
            var list = "$result<ul>\n"

            for ((i, item) in items.withIndex()) {
                if (item.toLowerCase().startsWith("none")) {
                    list = "$list<li>None.</li>\n"
                    break
                }
                val parts: Array<String> = item.split(("\\.\\.\\.").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                if (parts.size == 2) {
                    list += "<li>" + (if (CurrentForecastServlet.states.containsKey(parts[0])) CurrentForecastServlet.states[parts[0]] else parts[0])
                    if (parts[1].toLowerCase().startsWith("none")) {
                        list = "$list: None.\n"
                    } else {
                        list = "$list\n<ul>\n"
                        list = list + "<li>" + parts[1]
                    }
                } else if (items.size > i+ 1 && items[i + 1] == "") {
                    list = "$list$item</li>\n"
                } else if (items.size > i+ 1 && items[i + 1].contains("...")) {
                    list = "$list</ul>\n"
                } else {
                    list += item
                }
            }

            list = "$list\n</ul>\n"
            return list
        }

        fun beginPage(contextPath: String): String {
            var page = util.startHTML("")
            page = util.addStartHead(page)
            page = util.addHTMLTitle(page, "Area Forecast Discussion")
            page = util.addStylesheetLink(page, "${contextPath}/style.css")
            page = util.addWebFont(page, "Sorts+Mill+Goudy")
            page = util.addWebFont(page, "Crimson+Text")
            page = util.addWebFont(page, "EB+Garamond")
            page = util.addWebFont(page, "PT+Serif")
            page = util.addMobileViewport(page, "device-width")
            page = util.addEndHead(page)
            page = util.addStartBody(page)
            return page
        }

        fun createNav(page: String, office: String, contextPath: String): String {
            var nav = util.addStartDiv(page, "navcontainer")
            nav = util.addStartDiv(nav, "nav")
            nav = util.addStartDiv(nav, "navtitle", "navlarge")
            nav = "$nav<span>Forecast Discussions</span>"
            nav = util.addEndDiv(nav)
            nav = util.addStartDiv(nav, "navtitle-abbrev", "navlarge")
            nav += "FD"
            nav = util.addEndDiv(nav)
            nav = util.addStartDiv(nav, className = "navsmall")
            nav = util.addURL(nav, "$contextPath?office=ARX", util.addStartDiv("", null, (if (office == "ARX") "current-page" else null)) + "la crosse" + util.addEndDiv(""))
            nav = util.addURL(nav, "$contextPath?office=DLH", util.addStartDiv("", null, (if (office == "DLH") "current-page" else null)) + "duluth" + util.addEndDiv(""))
            nav = util.addURL(nav, "$contextPath?office=PSR", util.addStartDiv("", null, (if (office == "PSR") "current-page" else null)) + "phoenix" + util.addEndDiv(""))
            nav = util.addEndDiv(nav)
            nav = util.addEndDiv(util.addEndDiv(nav))
            return nav
        }

        fun createFooter(page: String): String {
            val r = Random()
            val footer = ("<div id=\"footercontainer\">\n"
                    + "<div id=\"footer\">\n"
                    + "<p>" + CurrentForecastServlet.footers[r.nextInt(CurrentForecastServlet.footers.size)] + "</p>\n"
                    + "</div>\n"
                    + "</div>\n")
            return page + footer
        }
    }
}