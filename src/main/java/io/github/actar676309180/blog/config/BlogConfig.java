package io.github.actar676309180.blog.config;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Component
public class BlogConfig {

    private Properties properties;

    private String git;
    private String token;

    BlogConfig(){
        properties=new Properties();
        File blog = new File("blog.properties");
        if (blog.isFile()){
            try {
                properties.load(new FileInputStream(blog));
                load();
            } catch (IOException ignored) {
            }
        }
    }

    private void load(){
        git = properties.getProperty("git");
        token = properties.getProperty("token'");
    }

    public String getGit() {
        return git;
    }

    public String getToken() {
        return token;
    }
}
