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
        list.add("Cyclones");
        list.add("Iowa");

        WikiCrawler wiki = new WikiCrawler("/wiki/Iowa_State_University",10, list, "File.txt");
        String content = new Scanner(new File("File.txt")).useDelimiter("\\Z").next();
        ArrayList<String> list2 = wiki.extractLinks(content);
        printArrayList(list2);
    }

    public void printArrayList(ArrayList<String> lists){
        for(String list:lists){
            System.out.println(list);
        }
    }
}
