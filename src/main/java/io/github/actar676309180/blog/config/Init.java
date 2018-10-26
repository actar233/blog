package io.github.actar676309180.blog.config;

import io.github.actar676309180.blog.service.MarkdownService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Init implements CommandLineRunner {

    private final MarkdownService markdownService;

    @Autowired
    public Init(MarkdownService markdownService) {
        this.markdownService = markdownService;
    }

    @Override
    public void run(String... args) {
        try{
            markdownService.load();
        }catch (Exception e){
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }
}
