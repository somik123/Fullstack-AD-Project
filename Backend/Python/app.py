# https://flask.palletsprojects.com/en/2.1.x/quickstart/#a-minimal-application
# https://huggingface.co/nlptown/bert-base-multilingual-uncased-sentiment?text=I+like+you.+I+love+you
# start flask web server with commandline: flask run
from contextlib import nullcontext
from flask import Flask
from flask import request
from transformers import AutoTokenizer, AutoModelForSequenceClassification
import string
import torch
import pandas as pd
from nltk.corpus import stopwords
import nltk
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity
import sys
import math
import re

app = Flask(__name__)
nltk.download('stopwords')

# For sentiment analysis


def initialise_sentiment_analysis_model():
    tokenizer = AutoTokenizer.from_pretrained(
        'nlptown/bert-base-multilingual-uncased-sentiment')
    model = AutoModelForSequenceClassification.from_pretrained(
        'nlptown/bert-base-multilingual-uncased-sentiment')
    return tokenizer, model


def sentiment_score(message):
    tokens = tokenizer.encode(message, return_tensors='pt')
    result = model(tokens)
    return int(torch.argmax(result.logits))+1


# For textbook recommendations
def initialise_textbook_recommendation_Model():
    tb = pd.read_excel('textBooksDataPreprocessed.xlsx')
    docs_arr = tb['preprocessed'].tolist()
    tfidf = TfidfVectorizer()
    tfidf = tfidf.fit(docs_arr)
    docs_vecs = tfidf.transform(docs_arr).toarray()

    return docs_vecs, tfidf, tb


def preprocessing(docs):
    stop_words = stopwords.words('english')
    stemmer = nltk.stem.PorterStemmer()
    punc = str.maketrans('', '', string.punctuation)
    doc_no_punc = docs.translate(punc).lower()

    return doc_no_punc


def make_similarity_dataframe(similarity_matrix):
	column_labels = [str(i) for i in range(len(similarity_matrix[0]))]
	row_labels = ["query" + str(i) for i in range(len(similarity_matrix))]

	return pd.DataFrame(similarity_matrix, index=row_labels, columns=column_labels)


def get_cosine_similarity(tbs, order, inputString):
    inputString = preprocessing(inputString)
    query_vec = tfidf.transform([inputString]).toarray()
    # gives us a N_QUERY_DOCS X N_DOCS matrix
    docs_similarity = cosine_similarity(query_vec, docs_vecs)

    # docs_similarity
    df = make_similarity_dataframe(docs_similarity)
    (order, tbs) = get_top_5_recommendations_from_consine_similarity(
        tbs, df.iloc[0], order)
    # print(order, file=sys.stdout)
    # print(tbs, file=sys.stdout)

    return(order, tbs)

def get_top_5_recommendations_from_consine_similarity(textbooks, series, order):
    sorted_series = series.sort_values(ascending=False)
    docsID = textbooks['book-id']
    # print('value is ' + str(sorted_series.iloc[0])) 

    if not math.isclose(sorted_series.iloc[0], 0.0): #should not give any results for nonsense search text 
        for i in sorted_series[:5].index: 
            # print(i , file=sys.stdout)
            pos = int(i)
            order += str(docsID.iloc[pos]) + ","
            textbooks.drop(textbooks.index[[pos]], inplace = True)

    return(order, textbooks)

# adds to order based on genre given. drops those books from remaining_textbooks
def get_books_in_genre(remaining_textbooks, genre, order): 
    books_in_genre = remaining_textbooks[remaining_textbooks['genre']==genre]
    for i in list(books_in_genre['book-id']): 
        order += str(i) + ","
    remaining_textbooks.drop(books_in_genre.index, inplace = True)
    return remaining_textbooks, order 

def get_all_remaining_books(remaining_textbooks, order): 
    for i in list(remaining_textbooks['book-id']): 
        order += str(i) + ","
    return order

def remove_emojis(data):
    emoj = re.compile("["
        u"\U0001F600-\U0001F64F"  # emoticons
        u"\U0001F300-\U0001F5FF"  # symbols & pictographs
        u"\U0001F680-\U0001F6FF"  # transport & map symbols
        u"\U0001F1E0-\U0001F1FF"  # flags (iOS)
        u"\U00002500-\U00002BEF"  # chinese char
        u"\U00002702-\U000027B0"
        u"\U00002702-\U000027B0"
        u"\U000024C2-\U0001F251"
        u"\U0001f926-\U0001f937"
        u"\U00010000-\U0010ffff"
        u"\u2640-\u2642" 
        u"\u2600-\u2B55"
        u"\u200d"
        u"\u23cf"
        u"\u23e9"
        u"\u231a"
        u"\ufe0f"  # dingbats
        u"\u3030"
                      "]+", re.UNICODE)
    return re.sub(emoj, '', data)

(tokenizer, model) = initialise_sentiment_analysis_model() 
(docs_vecs, tfidf, textbooks) = initialise_textbook_recommendation_Model()

# if ok: return value 1-5. 5 is most postive, 1 is most negative. 3 is neutral
# if any errors: return 0. java backend will treat it as neutral 
@app.route('/analyse', methods=['POST'])
def sentimentAnalysis():
    request_data = request.get_json()
    text = remove_emojis(request_data['text'])

    if text:
        return str(sentiment_score(text))
    else: 
        return str(0)


# if ok: return string of order "100,66,301, 4...90,318"
# if any errors: return string of order "1,2,3,4....326,327,328"
@app.route('/recommend', methods=['POST'])
def textbookRecommendation():
    request_data = request.get_json()
    order = "" 
    

    # return order if request data exists. even if both are empty string,
    # the default order 1-382 will still be returned 
    if request_data: 
        interest = request_data['interest']
        _eventLatest = request_data['_eventLatest']
        print(interest, file=sys.stdout)
        print(_eventLatest, file=sys.stdout)
        tbs = textbooks.copy() 
        print(type(_eventLatest))
        # order by event title/cosine similarity
        # if not (_eventLatest == "" or _eventLatest.isspace() or _eventLatest is None or not _eventLatest or not _eventLatest == 'null'): #if not empty
        if _eventLatest != 'null' and not _eventLatest.isspace() and _eventLatest != None and _eventLatest != '':   
            (order, tbs) = get_cosine_similarity(tbs, order, _eventLatest)
            print('order after _eventLatest:')
            print(order)
        
        # order by genre interest 
        if 'Computer Sciences' in interest: 
            (tbs, order) = get_books_in_genre(tbs, 'Computer Sciences', order)
        if 'Math/Stats' in interest: 
            (tbs, order) = get_books_in_genre(tbs, 'Math/Stats', order)
        if 'Humanities' in interest: 
            (tbs, order) = get_books_in_genre(tbs, 'Humanities', order)
        if 'Biological/Physical Sciences' in interest: 
            (tbs, order) = get_books_in_genre(tbs, 'Biological/Physical Sciences', order)
        if 'Business' in interest: 
            (tbs, order) = get_books_in_genre(tbs, 'Business', order)
        if 'Others' in interest: 
            (tbs, order) = get_books_in_genre(tbs, 'Others', order)

        # finalise order with remaining books
        order = get_all_remaining_books(tbs, order)
        order = order[:-1]
    # return default order 1-382 if request data is missing
    else: 
        for i in range(1,383): 
            order += str(i) + ","
        
        order = order[:-1]

    return order 

@app.route('/search', methods=['POST'])
def searchByCosine():
    request_data = request.get_json()
    interest = request_data['searchText']
    if interest != 'null' and not interest.isspace() and interest != None and interest != '': 
        tbs = textbooks.copy() 
        order = ""
        (order, tbs) = get_cosine_similarity(tbs, order, interest)
        if not order:
            return "" 
        else: 
            return order[:-1] 
    else: 
        return ""
        