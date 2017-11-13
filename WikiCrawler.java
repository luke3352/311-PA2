// LEAVE THIS FILE IN THE DEFAULT PACKAGE
//  (i.e., DO NOT add 'package cs311.pa1;' or similar)

// DO NOT MODIFY THE EXISTING METHOD SIGNATURES
//  (you may, however, add additional methods and fields)

// DO NOT INCLUDE LIBRARIES OUTSIDE OF THE JAVA STANDARD LIBRARY
//  (i.e., you may include java.util.ArrayList etc. here, but not junit, apache commons, google guava, etc.)

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.*;

public class WikiCrawler
{
    static final String BASE_URL = "https://en.wikipedia.org";
    ArrayList<String> topics;
    int max;
    String fileName;
    String seedUrl;
    // other member fields and methods

    public WikiCrawler(String seedUrl, int max, ArrayList<String> topics, String fileName)
    {
        this.topics = topics;
        this.max = max;
        this.fileName = fileName;
        this.seedUrl = seedUrl;
    }

    // NOTE: extractLinks takes the source HTML code, NOT a URL
    /*
    extractLinks(String doc). This method gets a string (that represents contents of a .html
    file) as parameter. This method should return an array list (of Strings) consisting of links from doc.
    Type of this method is ArrayList<String>. You may assume that the html page is the source
    (html) code of a wiki page. This method must
• Extract only wiki links. I.e. only links that are of form /wiki/XXXXX.
• Only extract links that appear after the first occurrence of the html tag <p> (or <P>).
• Should not extract any wiki link that contain the characters “#” or “:”.
• The order in which the links in the returned array list must be exactly the same order in
which they appear in doc.
For example, if doc looks like the attached file (sample.txt), then
Then the returned list must be
/wiki/Science, /wiki/Computation, /wiki/Procedure_(computer_science),
/wiki/Algorithm, /wiki/Information, /wiki/CiteSeerX, /wiki/Charles_Babbage

     */
    public ArrayList<String> extractLinks(String doc)
    {
        String[] strings = doc.split("<p>");
        ArrayList<String> matches = new ArrayList<>();
        int numFound =0;
        for(int i =1; i<strings.length; i++) {
            Matcher m = Pattern.compile("([\\/](wiki)+\\/)(([^:#]*?)(\"))")
                    .matcher(strings[i]);
            while (m.find()) {
                matches.add(seedUrl + " " +m.group().substring(0, m.group().length() - 1));
                numFound++;
                if(max == numFound+1){break;}
            }
            if(max == numFound+1){break;}
        }
        return matches;
    }
    /*

    crawl() This method should construct the web graph over following pages: If seedUrl does
not contain all words from topics, then the graph constructed is empty graph(0 vertices and 0
edges). Suppose that seedUrl contains all words from topics. Consider the first max many pages
(including seedUrl page), that contain every word from the list topics, that are visited when you
do a BFS with seedUrl as root. Your program should construct the web graph only over those
pages. and writes the graph to the file fileName.

For example, WikiCrawler can be used in a program as follows. Say topics has strings Iowa
State, Cyclones.
WikiCrawler w = new WikiCrawler("/wiki/Iowa_State_University", 100, topics, "WikiISU.txt");
w.crawl();

This program will start crawling with /wiki/Iowa State University as the root page. It will
collect the first 100 pages that contain both the words “Iowa State” and ”Cyclones”
that are visited by a BFS algorithm. Determines the graph (links among the those 100 pages)
and writes the graph to a (text) file named WikiISU.txt. This file will contain all edges of the
graph. Each line of this file should have one directed edge, except the first line. The first line of
the graph should indicate number of vertices which will be 100. Below is sample contents of the
file

     */

    public void crawl()
    {
        ArrayList<String> links = new ArrayList<>();
        ArrayList<String> links2 = new ArrayList<>();
        File file = new File(fileName);
        try {

            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            String html = gethtml();
            links = extractLinks(html);
            for(String link:links) {
                bw.write(link+" ");
            }

            for(String link: links){
                seedUrl = link.split(" ")[1];
                html = gethtml();
                links2 = extractLinks(html);
                for(String link2:links2) {
                    bw.write(link2+" ");
                }
            }

        }
        catch(Exception e){
            System.out.println("Error "+e.toString());
        }

    }

    private String gethtml(){
        String html = null;
        URL url = null;
        try {
            url = new URL(BASE_URL + seedUrl);
            InputStream is = url.openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String inputLine =null;
            while ((inputLine = br.readLine()) != null){
                html += inputLine;
            }
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return html;
    }
}



