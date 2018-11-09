package io.github.actar233.blog.service;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileFilter;

@Configuration
@Component
public class ScanMarkdownService implements CommandLineRunner {

    private final MarkdownService markdownService;

    private boolean isLoading = false;

    private boolean scan = true;

    @Autowired
    public ScanMarkdownService(MarkdownService markdownService) {
        this.markdownService = markdownService;
    }

    public void listener() throws Exception {
        String filePath = "markdown/";
        FileFilter filter = FileFilterUtils.and(new IOFileFilter() {
            @Override
            public boolean accept(File file) {
                return true;
            }

            @Override
            public boolean accept(File dir, String name) {
                return true;
            }
        });
        FileAlterationObserver fileAlterationObserver = new FileAlterationObserver(filePath, filter);
        fileAlterationObserver.addListener(new FileAlterationListenerAdaptor() {

            @Override
            public void onFileChange(File file) {
                if (!scan) return;

                scan();
            }

            @Override
            public void onFileCreate(File file) {
                if (!scan) return;
                scan();
            }

            @Override
            public void onFileDelete(File file) {
                if (!scan) return;
                scan();
            }

        });
        FileAlterationMonitor filealterationMonitor = new FileAlterationMonitor(1000);
        filealterationMonitor.addObserver(fileAlterationObserver);
        filealterationMonitor.start();
    }

    public void scan() {
        synchronized (this) {
            if (isLoading) {
                return;
            }
            isLoading = true;
        }
        markdownService.reload();
        isLoading = false;
    }

    @Override
    public void run(String... args) throws Exception {
        listener();
    }


    public boolean isScan() {
        return scan;
    }

    public void setScan(boolean scan) {
        this.scan = scan;
    }
}
