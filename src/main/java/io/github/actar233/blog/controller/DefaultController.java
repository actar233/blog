package io.github.actar233.blog.controller;

import io.github.actar233.blog.config.BlogConfig;
import io.github.actar233.blog.utils.Markdown;
import io.github.actar233.blog.service.MarkdownService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class DefaultController {

    private static final int pageMaxMarkdownCount = 12;
    private static final int pageMaxTagCount = 8;

    private final MarkdownService markdownService;

    private final BlogConfig config;

    @Autowired
    public DefaultController(MarkdownService markdownService, BlogConfig config) {
        this.markdownService = markdownService;
        this.config = config;
    }

    @GetMapping({"/", "/index.html"})
    public String index(Model model) {

        List<Markdown> markdowns = markdownService.markdowns;

        markdowns = markdowns.subList(0, markdowns.size() > pageMaxMarkdownCount ? pageMaxMarkdownCount : markdowns.size());

        model.addAttribute("markdowns", markdowns);

        List<String> tags = new ArrayList<>(markdownService.tags.keySet());

        tags = tags.subList(0, tags.size() > pageMaxTagCount ? pageMaxTagCount : tags.size());

        model.addAttribute("tags", tags);

        model.addAttribute("record", config.getRecord());

        return "index.html";
    }

    @GetMapping("/links.html")
    public String links(Model model) {
        model.addAttribute("record", config.getRecord());
        return "links.html";
    }

}
