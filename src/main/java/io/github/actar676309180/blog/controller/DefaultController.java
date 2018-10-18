package io.github.actar676309180.blog.controller;

import io.github.actar676309180.blog.Markdown;
import io.github.actar676309180.blog.MarkdownService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class DefaultController {

    @Autowired
    private MarkdownService markdownService;

    @GetMapping({"/","/index.html"})
    public String index(Model model){

        List<Markdown> markdowns = markdownService.markdowns;

        markdowns = markdowns.subList(0, markdowns.size() > 12 ? 12 : markdowns.size());

        model.addAttribute("markdowns",markdowns);

        List<String> tags = new ArrayList<>(markdownService.tags.keySet());

        tags = tags.subList(0, tags.size() > 8 ? 8 : tags.size());

        model.addAttribute("tags",tags);

        return "index.html";
    }

}
