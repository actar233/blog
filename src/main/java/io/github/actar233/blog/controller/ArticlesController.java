package io.github.actar233.blog.controller;

import io.github.actar233.blog.config.BlogConfig;
import io.github.actar233.blog.utils.Markdown;
import io.github.actar233.blog.service.MarkdownService;
import io.github.actar233.blog.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ArticlesController {

    private final MarkdownService markdownService;

    private final BlogConfig config;

    @Autowired
    public ArticlesController(MarkdownService markdownService, BlogConfig config) {
        this.markdownService = markdownService;
        this.config = config;
    }

    @GetMapping("/articles")
    public String articles(Model model) {
        return articles(1, model);
    }

    @GetMapping("/articles/{index}")
    public String articles(@PathVariable("index") Integer index, Model model) {

        List<Markdown> markdowns = markdownService.markdowns;

        //noinspection Duplicates
        if (index == null) {
            setModel(Page.generatePage(0, markdowns.size()), model);
        } else {
            Page page = Page.generatePage(index - 1, markdowns.size());
            if (page.check()) {
                setModel(page, model);
            } else {
                return "/error/404.html";
            }
        }

        model.addAttribute("record", config.getRecord());

        return "articles.html";
    }

    private void setModel(Page page, Model model) {

        int lave = page.count - page.currentPage * page.step;

        int from = page.currentPage * page.step;
        int to = from + Math.min(page.step, lave);

        if (to > from) {
            List<Markdown> markdowns = markdownService.markdowns.subList(from, to);

            model.addAttribute("markdowns", markdowns);
            model.addAttribute("page", page);
        } else {
            List<Markdown> markdowns = new ArrayList<>();

            model.addAttribute("markdowns", markdowns);
            model.addAttribute("page", page);
        }
    }

    @GetMapping("/articles/**")
    public String articlesMarkdown(HttpServletRequest request, Model model) {
        String uri = request.getRequestURI();
        try {
            uri = java.net.URLDecoder.decode(uri, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return "/error/404.html";
        }
        if (!markdownService.pathMapping.containsKey(uri)) {
            return "/error/404.html";
        }
        Markdown markdown = markdownService.pathMapping.get(uri);

        model.addAttribute("keywords", markdown.getKeywords());
        model.addAttribute("description", markdown.getDescription());
        model.addAttribute("markdown", markdown.getHtml());
        model.addAttribute("record", config.getRecord());

        return "article.html";
    }

}


