package io.github.actar676309180.blog.controller;

import io.github.actar676309180.blog.BlogConfig;
import io.github.actar676309180.blog.ScanMarkdown;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;

@RestController
public class GitWebHookController {

    private final ScanMarkdown scanMarkdown;

    private final String tempDir = System.getProperty("java.io.tmpdir");

    private final BlogConfig blogConfig;

    @Autowired
    public GitWebHookController(ScanMarkdown scanMarkdown, BlogConfig blogConfig) {
        this.scanMarkdown = scanMarkdown;
        this.blogConfig = blogConfig;
    }

    @RequestMapping({"/git/target", "/github/target/{token}"})
    public void hook(@PathVariable(value = "token", required = false) String token) {
        if (blogConfig.getGit() == null) return;
        if (!(blogConfig.getToken() == null || blogConfig.getToken().equals(token))) return;
        scanMarkdown.setScan(false);

        File tempBlogDir = new File(tempDir, "blog");

        if (tempBlogDir.isDirectory()) deletes(tempBlogDir);

        boolean cloneResult = cloneGithub(blogConfig.getGit(), tempBlogDir.getAbsolutePath());

        if (cloneResult) {
            File tempMarkdown = new File(tempBlogDir, "markdown");
            if (tempMarkdown.isDirectory()) {
                File markdown = new File("markdown");
                deletes(markdown);
                //noinspection ResultOfMethodCallIgnored
                tempMarkdown.renameTo(markdown);
            }
            deletes(tempBlogDir);
            scanMarkdown.scan();
        }
        scanMarkdown.setScan(true);
    }

    private void deletes(File file) {

        if (file.isFile())
            //noinspection ResultOfMethodCallIgnored
            file.delete();

        File[] files = file.listFiles();

        assert files != null;
        for (File f : files) {
            deletes(f);
        }

        //noinspection ResultOfMethodCallIgnored
        file.delete();
    }

    private boolean cloneGithub(String git, String local) {
        Runtime runtime = Runtime.getRuntime();
        String[] args = new String[]{
                "git", "clone", git, local
        };
        try {
            Process process = runtime.exec(args);
            process.waitFor();
            return true;
        } catch (IOException e) {
            return false;
        } catch (InterruptedException e) {
            return true;
        }
    }

}
