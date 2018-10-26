package io.github.actar676309180.blog.controller;

import io.github.actar676309180.blog.utils.Markdown;
import io.github.actar676309180.blog.service.MarkdownService;
import io.github.actar676309180.blog.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Set;

@Controller
public class TagsController {

    private final MarkdownService markdownService;

    @Autowired
    public TagsController(MarkdownService markdownService) {
        this.markdownService = markdownService;
    }

    @GetMapping("/tags")
    public String tags(Model model){

        Set<String> tags = markdownService.tags.keySet();

        model.addAttribute("tags",tags);

        return "tags.html";
    }

    @GetMapping("/tags/{tag}")
    public String articles(@PathVariable("tag") String tag, Model model) {
        return articles(tag,1, model);
    }

    @GetMapping("/tags/{tag}/{index}")
    public String articles(@PathVariable("tag") String tag,@PathVariable("index") Integer index, Model model) {

        if (!markdownService.tags.containsKey(tag)){
            return "/error/404.html";
        }

        List<Markdown> markdowns = markdownService.tags.get(tag);

        //noinspection Duplicates
        if (index == null) {
            setModel(Page.generatePage(0,markdowns.size()), model);
        } else {
            Page page = Page.generatePage(index - 1,markdowns.size());
            if (page.check()) {
                setModel(page, model);
            } else {
                return "/error/404.html";
            }
        }

        return "tag.html";
    }

    private void setModel(Page page, Model model) {

        int lave = page.count - page.currentPage * page.step;

        int from = page.currentPage * page.step;
        int to = from + (lave > page.step ? page.step : lave);

        if (to > from) {
            List<Markdown> markdowns = markdownService.markdowns.subList(from, to);

            model.addAttribute("markdowns", markdowns);
            model.addAttribute("page", page);
        }
    }

}
