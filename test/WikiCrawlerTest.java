import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Luke Sternhagen on 11/7/2017.
 */
public class WikiCrawlerTest {

    @Test
    public void constructorTest() throws FileNotFoundException {
        ArrayList<String> list = new ArrayList<>();
        list.add("physics");
        list.add("gravity");

        WikiCrawler wiki = new WikiCrawler("/wiki/Physics",10, list, "File.txt");
        String content = new Scanner(new File("File.txt")).useDelimiter("\\Z").next();
        wiki.extractLinks(content);
    }
}
