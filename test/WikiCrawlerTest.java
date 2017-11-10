import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.Assert.assertTrue;

/**
 * Created by Luke Sternhagen on 11/7/2017.
 */
public class WikiCrawlerTest {

    @Test
    public void constructorTest() throws FileNotFoundException {
        ArrayList<String> list = new ArrayList<>();
        list.add("Cyclones");
        list.add("Iowa");

        WikiCrawler wiki = new WikiCrawler("/wiki/Complexity_theory",20, list, "File.txt");
        String content = new Scanner(new File("File.txt")).useDelimiter("\\Z").next();
        ArrayList<String> list2 = wiki.extractLinks(content);
        printArrayList(list2);
    }

    public void printArrayList(ArrayList<String> lists){
        for(String list:lists){
            System.out.println(list);
        }
    }

    @Test
    public void graphConstructorTest(){
        GraphProcessor graph = new GraphProcessor("GraphData.txt");
    }

    @Test
    public void outDegreeTest(){
        GraphProcessor graph = new GraphProcessor("GraphData.txt");
        assertTrue(graph.outDegree("Ames")==2);
        assertTrue(graph.outDegree("Minneapolis")==1);
        assertTrue(graph.outDegree("Chicago")==1);
        assertTrue(graph.outDegree("Omaha")==1);
    }

    @Test
    public void BFSTest(){
        GraphProcessor graph = new GraphProcessor("GraphData.txt");
        ArrayList<String> bfs = graph.bfsPath("Ames","Minneapolis");
        printArrayList(bfs);
    }
}
