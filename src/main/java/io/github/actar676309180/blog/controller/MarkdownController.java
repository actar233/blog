package io.github.actar676309180.blog.controller;

import io.github.actar676309180.blog.MarkdownService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MarkdownController {

    @Autowired
    private MarkdownService markdownService;

    @GetMapping("/reload")
    public String reload(){
        try{
            markdownService.reload();
        }catch (Exception e){
            return "文档中包含错误";
        }
        return "success";
    }
}
