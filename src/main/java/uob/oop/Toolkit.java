package uob.oop;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class Toolkit {
    public static List<String> listVocabulary = null;
    public static List<double[]> listVectors = null;
    private static final String FILENAME_GLOVE = "glove.6B.50d_Reduced.csv";

    public static final String[] STOPWORDS = {"a", "able", "about", "across", "after", "all", "almost", "also", "am", "among", "an", "and", "any", "are", "as", "at", "be", "because", "been", "but", "by", "can", "cannot", "could", "dear", "did", "do", "does", "either", "else", "ever", "every", "for", "from", "get", "got", "had", "has", "have", "he", "her", "hers", "him", "his", "how", "however", "i", "if", "in", "into", "is", "it", "its", "just", "least", "let", "like", "likely", "may", "me", "might", "most", "must", "my", "neither", "no", "nor", "not", "of", "off", "often", "on", "only", "or", "other", "our", "own", "rather", "said", "say", "says", "she", "should", "since", "so", "some", "than", "that", "the", "their", "them", "then", "there", "these", "they", "this", "tis", "to", "too", "twas", "us", "wants", "was", "we", "were", "what", "when", "where", "which", "while", "who", "whom", "why", "will", "with", "would", "yet", "you", "your"};

    public void loadGlove() throws IOException {
        BufferedReader myReader = null;
        //TODO Task 4.1 - 5 marks

        listVocabulary = new ArrayList<>();
        listVectors = new ArrayList<>();
        String line;
        String[] wordArray;
        double[] vectorArray;

        try {
            File file = getFileFromResource(FILENAME_GLOVE);
            String path = file.getPath();
            myReader = new BufferedReader(new FileReader(path));

            while ((line = myReader.readLine()) != null) {
                wordArray = line.split(",");
                vectorArray = new double[wordArray.length-1];
                listVocabulary.add(wordArray[0]);
                for (int i=0; i< vectorArray.length; i++) {
                    vectorArray[i] = Double.parseDouble(wordArray[i+1]);
                }
                listVectors.add(vectorArray);
            }
        } catch (URISyntaxException | FileNotFoundException e) {
            throw new IOException(e.getMessage());
        } finally {
            if (myReader != null) {
                try {
                    myReader.close();
                } catch (IOException c) {
                    throw new IOException(c.getMessage());
                }
            }
        }
    }

    private static File getFileFromResource(String fileName) throws URISyntaxException {
        ClassLoader classLoader = Toolkit.class.getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException(fileName);
        } else {
            return new File(resource.toURI());
        }
    }

    public List<NewsArticles> loadNews() {
        List<NewsArticles> listNews = new ArrayList<>();
        //TODO Task 4.2 - 5 Marks
        NewsArticles news;
        BufferedReader myReader;
        String line;
        StringBuilder strFile;
        try {
            File folder = getFileFromResource("News");
            String[] files = folder.list();
            if (files.length > 1) {
                for (int j = 0; j < files.length - 1; j++) {
                    if (files[j] != null && files[j].endsWith(".htm") && files[j + 1] != null && files[j + 1].endsWith(".htm")) {
                        for (int k = 0; k < files.length - j - 1; k++) {
                            if (Integer.parseInt(files[k].substring(0, files[k].indexOf("."))) > Integer.parseInt(files[k + 1].substring(0, files[k + 1].indexOf(".")))) {
                                String temp = files[k];
                                files[k] = files[k + 1];
                                files[k + 1] = temp;
                            }
                        }
                    }
                }
            }
            if (files.length > 0) {
                for (int i = 0; i < files.length; i++) {
                    strFile = new StringBuilder();
                    if (files[i] != null && files[i].endsWith(".htm")) {
                        myReader = new BufferedReader(new FileReader(folder.getPath() + "/" + files[i]));
                        while ((line = myReader.readLine()) != null) {
                            strFile.append(line);
                        }
                        news = new NewsArticles(HtmlParser.getNewsTitle(strFile.toString()), HtmlParser.getNewsContent(strFile.toString()), HtmlParser.getDataType(strFile.toString()), HtmlParser.getLabel(strFile.toString()));
                        listNews.add(news);
                        myReader.close();
                    }
                }
            }
        } catch (URISyntaxException | IOException | NullPointerException e) {
            System.out.println(e.getMessage());
        }

        return listNews;
    }

    public static List<String> getListVocabulary() {
        return listVocabulary;
    }

    public static List<double[]> getlistVectors() {
        return listVectors;
    }
}
