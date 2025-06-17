# Advanced News Classifier using GloVe Embeddings and Machine Learning

> An advanced news classifier using GloVe embeddings and machine learning, expanding upon a previous project that used TF-IDF and cosine similarity, now capturing contextual meaning and deeper semantic connections between words, written for an Object-Oriented programming assignment at the University of Birmingham.

---

## Description

The project consists of the following main components:

### 1. **Glove.java**
Handles the loading and representation of GloVe word embeddings. Each word is represented by a 50-dimensional vector.

### 2. **NewsArticles.java**
Stores metadata for news articles, including title, content, type (training or testing), and label.

### 3. **HtmlParser.java**
Parses HTML files to extract the title, content, data type, and label of each article.

### 4. **Toolkit.java**
Provides utility functions for loading GloVe vectors and news articles.

### 5. **ArticlesEmbedding.java**
Generates document-level embeddings by averaging word embeddings. Includes preprocessing steps:
- Text cleaning
- Lemmatization (via Stanford CoreNLP)
- Stop-words removal

---

## Technologies used

- Language: Java 17
- Deeplearning4j (DL4J)
- GloVe (Global Vectors for Word Representation)
- Build and dependencies: Maven

---

## GloVe

The file is called "glove.6B.50d_Reduced.csv" and is located in the "resources" folder. It was trained based on Wikipedia 2014 + Gigaword 5, which contains 6 billion tokens. Originally, there were 400,000 words included in this model, it has been reduced to only include 38,534 unique words. Below is an
example of how this file is structured:

abacus,0.9102,-0.22416,0.37178,0.81798,...,0.34126
abadan,-0.33432,-0.95664,-0.23116,0.21188,...,-0.23159
abalone,0.34318,-0.8135,-0.99188,0.6452,0.0057126,...,-0.15903
.
.
.
zygote,0.78116,-0.49601,0.02579,0.69854,...,-0.40833
zymogen,-0.34302,-0.76724,0.13492,-0.0059688,...,0.37539

Each line starts with a unique word (so 38,534 lines in total), then followed by 50
floating numbers (separated by ","). These floating numbers are the vector representation of that word. In other words, each unique word is associated with a size/length 50vector. Elements in this vector must be consistent with the order of the floating numbers in the CSV file. Using the word "abacus" as an example, the first element in its vector representation should be "0.9102", then the second element is "-0.22416", and so on and so forth.

---

## Testing

A test file is included in the source code to verify the functionality of the classifier, along with the dataset consisting of 20 news articles from the Sky News website. This dataset can be replaced to perform semantic analysis on a different collection of documents.

---

## References

[1] J. Devlin, M.-W. Chang, K. Lee, and K. Toutanova, “Bert: Pre-training
of deep bidirectional transformers for language understanding,” arXiv preprint
arXiv:1810.04805, 2018.

[2] C. D. Manning, M. Surdeanu, J. Bauer, J. R. Finkel, S. Bethard, and D. McClosky, “The stanford corenlp natural language processing toolkit,” in Proceedings of 52nd annual meeting of the association for computational linguistics: system demonstrations, 2014, pp. 55–60.

[3] T. Mikolov, K. Chen, G. Corrado, and J. Dean, “Efficient estimation of word
representations in vector space,” 1st International Conference on Learning
Representations, ICLR 2013 - Workshop Track Proceedings, 1 2013. [Online].
Available: http://ronan.collobert.com/senna/

[4] T. Mikolov, Q. V. Le, and I. Sutskever, “Exploiting similarities among
languages for machine translation,” ArXiv, 9 2013. [Online]. Available:
http://arxiv.org/abs/1309.4168

[5] T. Mikolov, W. T. Yih, and G. Zweig, “Linguistic regularities in continuous space-word representations,” NAACL HLT 2013 - 2013 Conference of the North American
Chapter of the Association for Computational Linguistics: Human Language Technologies, Proceedings of the Main Conference, pp. 746–751, 2013.

[6] J. Pennington, R. Socher, and C. D. Manning, “Glove: Global vectors for word
representation,” in Empirical Methods in Natural Language Processing (EMNLP),
2014, pp. 1532–1543. [Online]. Available: http://www.aclweb.org/anthology/
D14-1162

[7] A. Radford, K. Narasimhan, T. Salimans, I. Sutskever et al., “Improving language understanding by generative pre-training,” 2018.

[8] E. D. D. Team, “DL4J: Deep Learning for Java,” 2016. [Online]. Available:
https://github.com/eclipse/deeplearning4j

[9] ——, “ND4J: Fast, Scientific and Numerical Computing for the JVM,” 2016.
[Online]. Available: https://github.com/eclipse/deeplearning4j

---

## License

This project was developed as coursework for academic purposes.
