### Required python packages for HTTP REST API with ML
```pip install torch transformers flask pandas nltk sklearn```

-Check out testMLAPI.postman_collection.json for examples of post request

i) Sentiment Analysis: 
Send a POST requests to the following link: http://localhost:5000/analyse

ii) Textbook recommendations: 
Send a POST requests to the following link: http://localhost:5000/recommend

Run the script by running: 
`flask run`


~~### Required python packages for ML (OLD)~~
~~pip install flask torch transformers requests beautifulsoup4~~

~~Run the script by running: `python3 server.py`~~
~~(Does not require app.py file to be present. Code intregrated)~~
