package generate

import model.*

class PageReader {
    companion object {
        fun read(body: List<String>, office: String, contextPath: String): Page {
            val sectionsText = body.subList(util.findEndOfTitle(body, 0) + 1, body.size)

            return Page(getTitle(body), getSections(sectionsText), getListSections(sectionsText))
        }

        fun getTitle(body: List<String>): Title {
            val start = 0
            val title = body[start]
            var subtitles = body.subList(start + 1, util.findEndOfTitle(body, start))
            return Title(title, subtitles)
        }

        fun getSections(body: List<String>): List<Section> {
            val sectionTexts = splitSections(body)
            var sections = mutableListOf<Section>()

            for (st in sectionTexts) {
                if (st[0].toLowerCase().contains("watches")) {
                    // Watches section goes in list sections
                    continue;
                }
                var bodyStart = 1
                var title = Title(util.formatHeading(st[0]), listOf())
                if (st[2].isBlank()) {
                    title = Title(util.formatHeading(st[0]), listOf(st[1]))
                    bodyStart = 3
                }


                val paras = mutableListOf<String>()
                var string = ""
                for (p in st.subList(bodyStart, st.size)) {
                    if (util.formatBody(p).isBlank()) {
                        paras.add(string)
                        string = ""
                    } else {
                        string += "$p "
                    }
                }
                val paragraphs = paras.filter { !it.trim().equals("&&")}.map { Paragraph(it) }
                sections.add(Section(title, paragraphs))
            }
            return sections.toList()
        }

        fun getListSections(body: List<String>): List<ListSection> {
            val sectionTexts = splitSections(body)
            var sections = mutableListOf<ListSection>()

            for (st in sectionTexts) {
                if (!st[0].toLowerCase().contains("watches")) {
                    // Ignore non watches section
                    continue;
                }

                // Watches section has no subtitle
                val title = Title(util.formatHeading(st[0]), listOf())
                val items = st.subList(1, st.size).filter { !it.contains("&&") }

                sections.add(ListSection(title, items))
            }

            return sections.toList()
        }

        fun splitSections(body: List<String>): List<List<String>> {
            var start = 0
            while (!body[start].startsWith(".")) {
                start++
            }

            // `start` is now the first line of the first section
            var sections: MutableList<List<String>> = mutableListOf<List<String>>()

            var i = start
            while (i < body.size) {
                if (body[i].startsWith("$$")) {
                    break;
                }

                if (body[i].trim() == "" || body[i].startsWith("&&")) {
                    i++
                    continue
                }

                if (i < body.size && body[i].startsWith(".")) {
                    var section: MutableList<String> = mutableListOf<String>()
                    section.add(util.formatBody(body[i]))
                    i++
                    while (i < body.size && (!body[i].startsWith(".") || body[i].startsWith("&&"))) {
                        section.add(util.formatBody(body[i]))
                        i++
                    }
                    sections.add(section.toList())
                } else {
                    i++
                }
            }
            return sections.toList()
        }
    }
}