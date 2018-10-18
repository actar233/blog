package io.github.actar676309180.blog;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.FileFilter;

@Configuration
public class ScanMarkdown implements CommandLineRunner {

    @Autowired
    private MarkdownService markdownService;

    private boolean isLoading = false;

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
                scan();
            }

            @Override
            public void onFileCreate(File file) {
                scan();
            }

            @Override
            public void onFileDelete(File file) {
                scan();
            }

        });
        FileAlterationMonitor filealterationMonitor = new FileAlterationMonitor(1000);
        filealterationMonitor.addObserver(fileAlterationObserver);
        filealterationMonitor.start();
    }

    private void scan() {
        if (isLoading) {
            return;
        }
        isLoading = true;
        markdownService.reload();
        isLoading = false;
    }

    @Override
    public void run(String... args) throws Exception {
        listener();
    }
}
