package io.github.actar676309180.blog;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Getter
@Setter
public class Markdown implements Comparable<Markdown> {
    private static Pattern titlePattern = Pattern.compile("\\[@Title\\]:<>\\((.*)\\)");
    private static Pattern datePattern = Pattern.compile("\\[@Date\\]:<>\\((.*)\\)");
    private static Pattern tagsPattern = Pattern.compile("\\[@Tags\\]:<>\\((.*)\\)");

    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    String title;
    Date date;
    String[] tags;
    String markdown;

    String mapping;
    String time;

    @Override
    public String toString() {
        return "Markdown{" +
                "title='" + title + '\'' +
                ", date=" + date +
                ", tags=" + Arrays.toString(tags) +
                '}';
    }

    @Override
    public int compareTo(Markdown other) {
        return other.date.compareTo(this.date);
    }

    public static Markdown parse(String text) throws ParseException {
        Markdown markdown = new Markdown();
        markdown.markdown = text;
        markdown.title = matcher(text, titlePattern, "Title");
        markdown.date = format.parse(matcher(text, datePattern, "Date"));
        markdown.tags = matcher(text, tagsPattern, "Tags").split(",");
        return markdown;
    }

    public static Markdown parse(File file) {
        String text = null;
        try {
            text = new BufferedReader(new InputStreamReader(new FileInputStream(file))).lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (FileNotFoundException e) {
            System.out.println(MessageFormat.format("{0}不存在", file.getAbsolutePath()));
            System.exit(0);
        }
        try {
            return parse(text);
        } catch (ParseException e) {
            throw new RuntimeException(MessageFormat.format("在{0}中,元数据包含错误", file.getAbsolutePath()));
        }
    }

    private static String matcher(String text, Pattern pattern, String name) {
        Matcher title = pattern.matcher(text);
        if (title.find()) {
            return title.group(1);
        }
        throw new RuntimeException(MessageFormat.format("{0}不存在", name));
    }

}
