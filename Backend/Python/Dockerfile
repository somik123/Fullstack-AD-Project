FROM python:3

WORKDIR /app

RUN pip3 install --no-cache --upgrade pip setuptools
RUN pip3 install flask torch transformers requests pandas numpy nltk sklearn openpyxl

ADD app.py ./
ADD textBooksDataPreprocessed.xlsx ./

EXPOSE 5000

CMD [ "python3", "-m" , "flask", "run", "--host=0.0.0.0"]
