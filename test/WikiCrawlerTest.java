import org.junit.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

/**
 * Created by Luke Sternhagen on 11/7/2017.
 */
public class WikiCrawlerTest {

    @Test
    public void constructorTest() throws FileNotFoundException {
        ArrayList<String> list = new ArrayList<>();


        WikiCrawler wiki = new WikiCrawler("/wiki/Complexity_theory",20, list, "File.txt");

        wiki.crawl();
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
        Assert.assertTrue(graph.outDegree("Ames")==2);
        Assert.assertTrue(graph.outDegree("Minneapolis")==1);
        Assert.assertTrue(graph.outDegree("Chicago")==1);
        Assert.assertTrue(graph.outDegree("Omaha")==1);
    }

    @Test
    public void BFSTest(){
        GraphProcessor graph = new GraphProcessor("GraphData.txt");
        ArrayList<String> bfs = graph.bfsPath("Omaha","Omaha");
        printArrayList(bfs);
    }
}