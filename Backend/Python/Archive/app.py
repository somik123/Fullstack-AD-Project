from http.server import BaseHTTPRequestHandler, HTTPServer
from urllib.parse import parse_qs,urlparse

from transformers import AutoTokenizer, AutoModelForSequenceClassification
import torch


hostName = "localhost"
serverPort = 8082


def parse_code_from_url(url: str) -> str:
    query = urlparse(url).query
    print(query)
    text = parse_qs(query).get('text', None)
    print(text)

    if text is not None:
        if len(text) == 1:
            return text[0]
    return ""



def initialise_libraries_and_model(): 
    tokenizer = AutoTokenizer.from_pretrained('nlptown/bert-base-multilingual-uncased-sentiment')
    model = AutoModelForSequenceClassification.from_pretrained('nlptown/bert-base-multilingual-uncased-sentiment')
    return tokenizer, model

def sentiment_score(message):
    tokens = tokenizer.encode(message, return_tensors='pt')
    result = model(tokens)
    return int(torch.argmax(result.logits))+1

(tokenizer, model) = initialise_libraries_and_model() 





class MyServer(BaseHTTPRequestHandler):
    def do_GET(self):
        if '/?text' in self.path:
            print(self.path)
            self.send_response(200)
            self.send_header('Content-Type', 'application/json')
            self.end_headers()
            print(self.path)
            text_data = parse_code_from_url(self.path)
            if len(text_data) > 5:
                print(text_data)
                score = str(sentiment_score(text_data))
                output = '{"text": "'+text_data+'", "score" : '+score+'}'
                self.wfile.write(bytes(output,"utf-8"))
    def do_POST(self): 
        print("here")
        if '/test' in self.path:
            content_len = int(self.headers.get('Content-Length'))
            post_body = self.rfile.read(content_len)
            print(post_body)


if __name__ == "__main__":        
    webServer = HTTPServer((hostName, serverPort), MyServer)
    print("Server started http://%s:%s" % (hostName, serverPort))

    try:
        webServer.serve_forever()
    except KeyboardInterrupt:
        pass

    webServer.server_close()
    print("Server stopped.")

