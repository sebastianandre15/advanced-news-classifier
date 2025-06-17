package uob.oop;

import org.apache.commons.lang3.time.StopWatch;
import org.deeplearning4j.datasets.iterator.utilty.ListDataSetIterator;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.WorkspaceMode;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdvancedNewsClassifier {
    public Toolkit myTK = null;
    public static List<NewsArticles> listNews = null;
    public static List<Glove> listGlove = null;
    public List<ArticlesEmbedding> listEmbedding = null;
    public MultiLayerNetwork myNeuralNetwork = null;

    public final int BATCHSIZE = 10;

    public int embeddingSize = 0;
    private static StopWatch mySW = new StopWatch();

    public AdvancedNewsClassifier() throws IOException {
        myTK = new Toolkit();
        myTK.loadGlove();
        listNews = myTK.loadNews();
        listGlove = createGloveList();
        listEmbedding = loadData();
    }

    public static void main(String[] args) throws Exception {
        mySW.start();
        AdvancedNewsClassifier myANC = new AdvancedNewsClassifier();

        myANC.embeddingSize = myANC.calculateEmbeddingSize(myANC.listEmbedding);
        myANC.populateEmbedding();
        myANC.myNeuralNetwork = myANC.buildNeuralNetwork(2);
        myANC.predictResult(myANC.listEmbedding);
        myANC.printResults();
        mySW.stop();
        System.out.println("Total elapsed time: " + mySW.getTime());
    }

    public List<Glove> createGloveList() {
        List<Glove> listResult = new ArrayList<>();
        //TODO Task 6.1 - 5 Marks
        Glove glove;
        Vector vector;
        boolean stopWord;
        for (int i=0; i<Toolkit.listVocabulary.size(); i++) {
            stopWord = false;
            for (int j=0; j<Toolkit.STOPWORDS.length; j++) {
                if (Toolkit.getListVocabulary().get(i).equals(Toolkit.STOPWORDS[j])) {
                    stopWord = true;
                    break;
                }
            }
            if (!stopWord) {
                vector = new Vector(Toolkit.getlistVectors().get(i));
                glove = new Glove(Toolkit.getListVocabulary().get(i), vector);
                listResult.add(glove);
            }
        }
        return listResult;
    }


    public static List<ArticlesEmbedding> loadData() {
        List<ArticlesEmbedding> listEmbedding = new ArrayList<>();
        for (NewsArticles news : listNews) {
            ArticlesEmbedding myAE = new ArticlesEmbedding(news.getNewsTitle(), news.getNewsContent(), news.getNewsType(), news.getNewsLabel());
            listEmbedding.add(myAE);
        }
        return listEmbedding;
    }

    public int calculateEmbeddingSize(List<ArticlesEmbedding> _listEmbedding) {
        int intMedian = -1;
        //TODO Task 6.2 - 5 Marks

        List<Integer> docLengths = new ArrayList<>();
        int docLength;

        for (int i=0; i< _listEmbedding.size(); i++) {
            docLength = lengthOfDoc(_listEmbedding.get(i).getNewsContent());
            docLengths.add(docLength);
        }

        int size = docLengths.size();
        docLengths.sort(null);

        if (size%2 == 0) {
            intMedian = (docLengths.get(size/2) + docLengths.get((size/2)+1))/2;
        } else {
            intMedian = docLengths.get((size+1)/2);
        }
        return intMedian;
    }

    public static int lengthOfDoc(String _content) {
        int docLength;
        boolean isGlove;
        String[] contentArray;
        contentArray = _content.split(" ");
        docLength = 0;
        for (int k=0; k<contentArray.length; k++) {
            isGlove = false;
            for (int j = 0; j < Toolkit.getListVocabulary().size(); j++) {
                if (Toolkit.getListVocabulary().get(j).equals(contentArray[k])) {
                    isGlove = true;
                    break;
                }
            }
            if (isGlove) {
                docLength += 1;
            }
        }
        return docLength;
    }

    public void populateEmbedding() {
        //TODO Task 6.3 - 10 Marks
        for (int i=0; i< listEmbedding.size(); i++) {
            try {
                listEmbedding.get(i).getEmbedding();
            } catch (Exception e) {
                if (e.getMessage().equals("Invalid size")) {
                    listEmbedding.get(i).setEmbeddingSize(embeddingSize);
                } else if (e.getMessage().equals("Invalid text")) {
                    listEmbedding.get(i).getNewsContent();
                }
            }
        }
    }

    public DataSetIterator populateRecordReaders(int _numberOfClasses) throws Exception {
        ListDataSetIterator myDataIterator = null;
        List<DataSet> listDS = new ArrayList<>();
        INDArray inputNDArray = null;
        INDArray outputNDArray = null;
        //TODO Task 6.4 - 8 Marks

        for (int i=0; i< listEmbedding.size(); i++)  {
            if (listEmbedding.get(i).getNewsType() == NewsArticles.DataType.Training) {
                inputNDArray = listEmbedding.get(i).getEmbedding();
                outputNDArray = Nd4j.zeros(1, _numberOfClasses);
                String newsGroups = listEmbedding.get(i).getNewsLabel();
                outputNDArray.putScalar(0, Integer.parseInt(newsGroups)-1, 1);
                DataSet myDataSet = new DataSet(inputNDArray, outputNDArray);
                listDS.add(myDataSet);
            }
        }
        return new ListDataSetIterator(listDS, BATCHSIZE);
    }

    public MultiLayerNetwork buildNeuralNetwork(int _numOfClasses) throws Exception {
        DataSetIterator trainIter = populateRecordReaders(_numOfClasses);
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .seed(42)
                .trainingWorkspaceMode(WorkspaceMode.ENABLED)
                .activation(Activation.RELU)
                .weightInit(WeightInit.XAVIER)
                .updater(Adam.builder().learningRate(0.02).beta1(0.9).beta2(0.999).build())
                .l2(1e-4)
                .list()
                .layer(new DenseLayer.Builder().nIn(embeddingSize).nOut(15)
                        .build())
                .layer(new OutputLayer.Builder(LossFunctions.LossFunction.HINGE)
                        .activation(Activation.SOFTMAX)
                        .nIn(15).nOut(_numOfClasses).build())
                .build();

        MultiLayerNetwork model = new MultiLayerNetwork(conf);
        model.init();

        for (int n = 0; n < 100; n++) {
            model.fit(trainIter);
            trainIter.reset();
        }
        return model;
    }

    public List<Integer> predictResult(List<ArticlesEmbedding> _listEmbedding) throws Exception {
        List<Integer> listResult = new ArrayList<>();
        //TODO Task 6.5 - 8 Marks

        for (int i=0; i< _listEmbedding.size(); i++) {
            if (_listEmbedding.get(i).getNewsType() == NewsArticles.DataType.Testing) {
                INDArray INDEmbedding = _listEmbedding.get(i).getEmbedding();
                _listEmbedding.get(i).setNewsLabel(String.valueOf(myNeuralNetwork.predict(INDEmbedding)[0]));
                listResult.add(Integer.parseInt(_listEmbedding.get(i).getNewsLabel()));
            }
        }

        return listResult;
    }

    public void printResults() {
        //TODO Task 6.6 - 6.5 Marks
        List<ArticlesEmbedding> sortedArticles = listEmbedding;

        for (int i = 1; i < sortedArticles.size(); ++i) {
            ArticlesEmbedding currentArticle = sortedArticles.get(i);
            int j = i - 1;
            while (j >= 0 && Integer.parseInt(sortedArticles.get(j).getNewsLabel()) > Integer.parseInt(currentArticle.getNewsLabel())) {
                sortedArticles.set(j + 1, sortedArticles.get(j));
                j = j - 1;
            }
            sortedArticles.set(j + 1, currentArticle);
        }

        int numberOfGroups = Integer.parseInt(sortedArticles.get(sortedArticles.size()-1).getNewsLabel());
        int counter = 0;
        for (int k=0; k< numberOfGroups; k++) {
            System.out.print("Group " + (k+1) + "\r\n");
            while (Integer.parseInt(sortedArticles.get(counter).getNewsLabel()) == k) {
                if (sortedArticles.get(counter).getNewsType() == NewsArticles.DataType.Testing) {
                    System.out.print(sortedArticles.get(counter).getNewsTitle() + "\r\n");
                }
                counter += 1;
            }
        }
    }
}
