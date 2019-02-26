package io.github.actar233.blog.utils;

import java.util.ArrayList;
import java.util.List;

public class Page {
    public int step;
    public int count;
    public int countPage;
    public int currentPage;

    private int fromPage;
    private int toPage;

    public Page(int step, int count, int currentPageNumber) {
        this.step = step;
        this.count = count;
        this.countPage = count / step + (count % step > 0 ? 1 : 0);
        this.currentPage = currentPageNumber;

        fromPage = currentPage - 2;
        toPage = currentPage + 2;

        while (fromPage < 0) {
            fromPage++;
            toPage++;
        }
        while (toPage >= countPage) {
            toPage--;
            if (fromPage > 0) fromPage--;
        }
    }

    public boolean check() {
        return (currentPage >= 0 && currentPage < countPage) || (count == 0);
    }

    public boolean hasNextPage() {
        if (count == 0) return false;
        return currentPage < countPage - 1;
    }

    public boolean hasLastPage() {
        if (count == 0) return false;
        return currentPage > 0;
    }

    public List<Integer> fromPage() {
        List<Integer> list = new ArrayList<>();
        for (int i = fromPage; i < currentPage; i++) {
            list.add(i);
        }
        return list;
    }

    public List<Integer> toPage() {
        List<Integer> list = new ArrayList<>();
        for (int i = currentPage + 1; i <= toPage; i++) {
            list.add(i);
        }
        return list;
    }

    public static Page generatePage(int currentPage, int size) {

        int step = 12;

        return new Page(step, size, currentPage);
    }

}
