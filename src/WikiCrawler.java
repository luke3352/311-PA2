// LEAVE THIS FILE IN THE DEFAULT PACKAGE
//  (i.e., DO NOT add 'package cs311.pa1;' or similar)

// DO NOT MODIFY THE EXISTING METHOD SIGNATURES
//  (you may, however, add additional methods and fields)

// DO NOT INCLUDE LIBRARIES OUTSIDE OF THE JAVA STANDARD LIBRARY
//  (i.e., you may include java.util.ArrayList etc. here, but not junit, apache commons, google guava, etc.)

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.regex.*;

public class WikiCrawler
{
    static final String BASE_URL = "https://en.wikipedia.org";
    ArrayList<String> topics;
    int max;
    HashMap<String,Integer> hashTopics = new HashMap<>();
    String fileName;
    String seedUrl;
    String OGSeedUrl;
    int requestCount =0;
    private boolean topicsGiven=false;
    // other member fields and methods

    public WikiCrawler(String seedUrl, int max, ArrayList<String> topics, String fileName)
    {
        this.topics = topics;
        this.max = max;
        this.fileName = fileName;
        this.seedUrl = seedUrl;
    }

    public ArrayList<String> extractLinks(String doc)
    {

        ArrayList<String> matches = new ArrayList<>();

            int endScan = 0;
            //we only want to extract links after <p>
            String[] strings = doc.split("<p>");
            //stop extracting links when NewPP is seen
            for (int i = 1; i < strings.length; i++) {
                if (strings[i].contains("NewPP")) {
                    strings[i] = strings[i].split("NewPP")[0];
                    endScan = i + 1;
                }
            }
            //System.out.println(strings.length);

            int numFound = 0;
            boolean firstTime = false;
            if (hashTopics.size() == 0) {
                firstTime = true;
            }
            boolean[] wasFound = new boolean[hashTopics.size()];
            for(int i =0; i<hashTopics.size(); i++){
                wasFound[i] = false;
            }

            for (int i = 1; i < endScan; i++) {
                Matcher m = Pattern.compile("([\\/](wiki)+\\/)(([^:#]*?)(\"))")
                        .matcher(strings[i]);
                while (m.find()) {
                    String found = m.group().substring(0, m.group().length() - 1);
                    //url can't equal page we are on
                    if (!seedUrl.equals(found)) {
                        if (!firstTime) {
                            if (hashTopics.containsKey(found)) {
                                //check if duplicate
                                if (wasFound[hashTopics.get(found)]==false) {
                                    matches.add(seedUrl + " " + found);
                                    numFound++;
                                    wasFound[hashTopics.get(found)]=true;
                                }
                            }
                        } else {
                            boolean isNew = true;

                                if (hashTopics.containsKey(found)) {
                                    isNew = false;
                                }

                            if (isNew) {
                                matches.add(seedUrl + " " + m.group().substring(0, m.group().length() - 1));
                                hashTopics.put(found,hashTopics.size());
                                numFound++;
                            }

                        }
                    }
                    if (max == numFound + 1) {
                        break;
                    }
                }
                if (max == numFound + 1) {
                    break;
                }
            }


        return matches;
    }

    public void crawl()
    {
        ArrayList<String> queue = new ArrayList<>();
        ArrayList<String> queue2 = new ArrayList<>();
        File file = new File(fileName);
        try {

            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            String html = gethtml();
            queue = extractLinks(html);
            if(topics.size()==0) {
                bw.write(queue.size()+1 + "");
                bw.newLine();
                for (String link : queue) {

                    bw.write(link + " ");
                    bw.newLine();
                }

                for (String link : queue) {
                    seedUrl = link.split(" ")[1];
                    html = gethtml();
                    queue2 = extractLinks(html);

                    for (String link2 : queue2) {
                        bw.write(link2 + " ");
                         bw.newLine();

                    }
                }
            }//if there are topics
            else {
                int topicsNum = 0;
                for (String topic : topics) {
                    if (html.contains(topic)) {
                        topicsNum++;
                    }
                }
                if (topics.size() == topicsNum) {
                    bw.write(queue.size()+1 + "");
                    bw.newLine();
                    for (String link : queue) {
                        bw.write(link);
                        bw.newLine();

                    }
                    for (String link : queue) {
                        seedUrl = link.split(" ")[1];
                        html = gethtml();
                        int topicsNum2 = 0;
                        for (String topic : topics) {
                            if (html.contains(topic)) {
                                topicsNum2++;
                            }
                        }
                        if (topics.size() == topicsNum2) {
                            queue2 = extractLinks(html);
                            for (String link2 : queue2) {
                                bw.write(link2 + " ");
                               bw.newLine();
                            }
                        }
                    }
                }
                else {bw.write("0"); bw.newLine();}
            }
            bw.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

    private String gethtml(){
        if(requestCount%50==0){
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        requestCount++;

        String html = null;
        URL url = null;
        try {
            url = new URL(BASE_URL + seedUrl);
            InputStream is = url.openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String inputLine =null;
            while ((inputLine = br.readLine()) != null){
                html += inputLine+"\n";
            }
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return html;
    }
}