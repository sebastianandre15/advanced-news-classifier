package uob.oop;

import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.pipeline.*;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import java.util.Properties;


public class ArticlesEmbedding extends NewsArticles {
    private int intSize = -1;
    private String processedText = "";

    private INDArray newsEmbedding = Nd4j.create(0);

    public ArticlesEmbedding(String _title, String _content, NewsArticles.DataType _type, String _label) {
        //TODO Task 5.1 - 1 Mark
        super(_title,_content,_type,_label);
    }

    public void setEmbeddingSize(int _size) {
        //TODO Task 5.2 - 0.5 Marks
        this.intSize = _size;
    }

    public int getEmbeddingSize(){
        return intSize;
    }

    @Override
    public String getNewsContent() {
        //TODO Task 5.3 - 10 Marks
        if (processedText.isEmpty()) {
            String cleanedContent = textCleaning(super.getNewsContent());
            StringBuilder lemmatizedContent = new StringBuilder();
            boolean stopWord;
            String[] stopWords = Toolkit.STOPWORDS;

            Properties props = new Properties();
            props.setProperty("annotators", "tokenize,pos,lemma");
            StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
            CoreDocument document = pipeline.processToCoreDocument(cleanedContent);

            for (CoreLabel tok : document.tokens()) {
                stopWord = false;
                for (int j = stopWords.length - 1; j >= 0; j--) {
                    if (tok.lemma().equals(stopWords[j])) {
                        stopWord = true;
                        break;
                    }
                }
                if (!stopWord) {
                    lemmatizedContent.append(tok.lemma()).append(" ");
                }
            }
            processedText = lemmatizedContent.toString().toLowerCase();
            return processedText.trim();
        } else {
            return processedText;
        }
    }

    public INDArray getEmbedding() throws Exception {
        //TODO Task 5.4 - 20 Marks
        if (newsEmbedding.isEmpty()) {
            if (intSize != -1 && !processedText.isEmpty()) {
                newsEmbedding = Nd4j.create(intSize, Toolkit.getlistVectors().get(0).length);
                String[] contentArray;
                contentArray = processedText.split(" ");
                int matchCount = 0;
                double[][] embeddingArray = new double[intSize][Toolkit.getlistVectors().get(0).length];
                int eachWord = 0;
                INDArray empty = Nd4j.zeros(Toolkit.getlistVectors().get(0).length);

                while (eachWord < contentArray.length && matchCount < intSize) {
                    for (int j = 0; j < Toolkit.getListVocabulary().size(); j++) {
                        if (Toolkit.getListVocabulary().get(j).equals(contentArray[eachWord])) {
                            embeddingArray[matchCount] = Toolkit.getlistVectors().get(j);
                            INDArray tempEmbedding = Nd4j.create(embeddingArray[matchCount]);
                            newsEmbedding.putRow(matchCount, tempEmbedding);
                            matchCount += 1;
                            break;
                        }
                    }
                    eachWord += 1;
                }
                if (intSize > matchCount + 1) {
                    for (int i = matchCount; i < intSize; i++) {
                        newsEmbedding.putRow(i, empty);
                    }
                }
            } else if (intSize == -1) {
                throw new InvalidSizeException("Invalid size");
            } else {
                throw new InvalidTextException("Invalid text");
            }
        }
        return Nd4j.vstack(newsEmbedding.mean(1));
    }

    /***
     * Clean the given (_content) text by removing all the characters that are not 'a'-'z', '0'-'9' and white space.
     * @param _content Text that need to be cleaned.
     * @return The cleaned text.
     */
    private static String textCleaning(String _content) {
        StringBuilder sbContent = new StringBuilder();

        for (char c : _content.toLowerCase().toCharArray()) {
            if ((c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || Character.isWhitespace(c)) {
                sbContent.append(c);
            }
        }

        return sbContent.toString().trim();
    }
}
