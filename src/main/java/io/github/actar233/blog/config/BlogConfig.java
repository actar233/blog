package io.github.actar233.blog.config;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Component
public class BlogConfig {

    private Properties properties;

    @Getter
    private String git;

    @Getter
    private String token;

    @Getter
    private String record;

    BlogConfig() {
        properties = new Properties();
        File blog = new File("blog.properties");
        if (blog.isFile()) {
            try {
                properties.load(new FileInputStream(blog));
                load();
            } catch (IOException ignored) {
            }
        }
    }

    private void load() {
        git = properties.getProperty("git");
        token = properties.getProperty("token");
        record = properties.getProperty("record");
    }
}
