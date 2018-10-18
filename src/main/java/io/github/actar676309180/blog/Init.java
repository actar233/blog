package io.github.actar676309180.blog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Init implements CommandLineRunner {

    @Autowired
    private MarkdownService markdownService;

    @Override
    public void run(String... args) throws Exception {
        try{
            markdownService.load();
        }catch (Exception e){
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }
}
