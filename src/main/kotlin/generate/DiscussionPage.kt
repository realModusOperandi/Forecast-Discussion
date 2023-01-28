package generate

import model.Page
import model.Paragraph
import model.Title
import java.util.*

class DiscussionPage {
    companion object {
        fun createPage(page: Page, office: String, randomQuote: Boolean = true): String {
            var result = beginPage()
            result = createNav(result, office)
            result = createTitle(result, page)
            result = createContent(result, page, randomQuote)
            result = util.endPage(result)
            return result
        }

        private fun createTitle(result: String, page: Page): String {
            var title = util.addStartDiv("", className = "title-bg")
            title = util.addStartDiv(title, "titlecontainer")
            title = util.addStartDiv(title, "title")
            title = "$title<h2>${page.title.title.lowercase(Locale.getDefault())}</h2>\n"
            for (subtitle in page.title.subtitles) {
                title = "$title<em>$subtitle</em><br/>\n"
            }
            title = util.addEndDiv(title)
            title = util.addEndDiv(title)
            title = util.addEndDiv(title)
            return result + title
        }

        private fun createContent(result: String, page: Page, randomQuote: Boolean): String {
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

            content = createFooter(util.addEndDiv(util.addEndDiv(content)), randomQuote)
            return content
        }

        private fun createHeading(result: String, heading: Title): String {
            val mainHeading: String = heading.title
            var head = "$result<div class=\"sectionhead\"><h3>$mainHeading</h3>\n"

            for (subheading in heading.subtitles) {
                head += "<em>$subheading</em>\n"
            }
            head += "</div>\n"
            return head
        }

        private fun createBody(result: String, body: List<Paragraph>): String {
            var text = result
            for (p in body) {
                text = when {
                    p.subheader -> "$text<h4>${util.formatSubheader(p.text)}</h4>\n"
                    else -> "$text<p>${p.text}</p>\n"
                }
            }
            return text
        }

        private fun createWarnings(result: String, items: List<String>): String {
            var list = "$result<ul>\n"

            for ((i, item) in items.withIndex()) {
                if (item.lowercase(Locale.getDefault()).startsWith("none")) {
                    list = "$list<li>None.</li>\n"
                    break
                }
                val parts: Array<String> = item.split(("\\.\\.\\.").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                if (parts.size == 2) {
                    list += "<li>" + (if (util.states.containsKey(parts[0])) util.states[parts[0]] else parts[0])
                    if (parts[1].lowercase(Locale.getDefault()).startsWith("none")) {
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

        private fun beginPage(): String {
            var page = util.startHTML("")
            page = util.addStartHead(page)
            page = util.addHTMLTitle(page, "Area Forecast Discussion")
            page = util.addStylesheetLink(page, "style.css")
            page = util.addWebFont(page, "Open Sans")
            page = util.addWebFont(page, "Lato", ":ital,wght@0,100;0,300;0,400;0,700;0,900;1,100;1,300;1,400;1,700;1,900&display=swap")
            page = util.addMobileViewport(page, "device-width")
            page = util.addScriptReference(page, "scripts/navbar.js")
            page = util.addEndHead(page)
            page = util.addStartBody(page)
            return page
        }

        private fun createNav(page: String, office: String): String {
            var nav = util.addStartDiv(page, "nav-bg")
            nav = util.addEndDiv(nav)
            nav = util.addStartDiv(nav, "navcontainer")
            nav = util.addStartDiv(nav, "nav")
            nav = util.addStartDiv(nav, "navtitle", "navlarge")
            nav = "$nav<span>Forecast Discussions</span>"
            nav = util.addEndDiv(nav)
            nav = util.addStartDiv(nav, "navtitle-abbrev", "navlarge")
            nav += "FD"
            nav = util.addEndDiv(nav)
            nav = util.addStartDiv(nav, className = "navsmall")
            nav = util.addURL(nav, "?office=ARX", util.addStartDiv("", null, (if (office == "ARX") "current-page" else null)) + "la crosse" + util.addEndDiv(""))
            nav = util.addURL(nav, "?office=DLH", util.addStartDiv("", null, (if (office == "DLH") "current-page" else null)) + "duluth" + util.addEndDiv(""))
            nav = util.addURL(nav, "?office=PSR", util.addStartDiv("", null, (if (office == "PSR") "current-page" else null)) + "phoenix" + util.addEndDiv(""))
            nav = util.addEndDiv(nav)
            nav = util.addEndDiv(util.addEndDiv(nav))
            return nav
        }

        private fun createFooter(page: String, randomQuote: Boolean): String {
            val r = Random()
            val footer = ("<div id=\"footercontainer\">\n"
                    + "<div id=\"footer\">\n"
                    + "<p>" +
                    if (randomQuote) {
                        util.footers[r.nextInt(util.footers.size)]

                    } else {
                        util.footers[0]
                    }
                    + "</p>\n"
                    + "</div>\n"
                    + "</div>\n")
            return page + footer
        }
    }
}
