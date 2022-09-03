package sg.edu.nus.iss.team1.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import sg.edu.nus.iss.team1.domain.Interest;

/*
// Usage:
int maxSleep = 10; // seconds

MLApi mlApi = new MLApi();
mlApi.analyzeMessage(stringMessage);
mlApi.recommendBooks(interestsCollection, lastGroupName);

// While thread is still alive
for (int i = 0; i < maxSleep*10 && mlApi.getBkThread().isAlive(); i++) {
	try {
		Thread.sleep(100); // Wait 0.1s at a time for background thread to complete
	} catch (InterruptedException e) {
		System.out.println(e);
	}
}
String reply = mlApi.GetMlReply();
*/

public class MLApi {
    protected Thread bkThread;
    protected String mlReply = "";
    protected String ml_back_url = "http://backend_ml:5000";  // Docker internal port
//	protected String ml_back_url = "http://localhost:5000";


    public void analyzeMessage(String text) {
        bkThread = new Thread(() -> {
            try {
                APIRequestHandler request = new APIRequestHandler(ml_back_url + "/analyse");

                String postData = "{ \"text\": \"" + text + "\"}";
                String reply = request.postRequest(postData);

                if (reply.length() > 0) {
                    mlReply = reply;
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        });
        bkThread.start();
    }

    public void recommendBooks(Collection<Interest> interests, String lastGroupName) {
        List<String> interestNamesList = new ArrayList<>();
        for (Interest i : interests) {
            interestNamesList.add(i.getName());
        }
        String interestNamesStr = interestNamesList.stream().collect(Collectors.joining(","));

        bkThread = new Thread(() -> {
            try {
                APIRequestHandler request = new APIRequestHandler(ml_back_url + "/recommend");

                String postData = "{ \"interest\": \"" + interestNamesStr + "\", \"_eventLatest\": \"" + lastGroupName
                        + "\" }";
                String reply = request.postRequest(postData);
//				System.out.println(postData);

                if (reply.length() > 0) {
                    mlReply = reply;
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        });
        bkThread.start();
    }

    public void searchBooks(String searchText) {
        bkThread = new Thread(() -> {
            try {
                APIRequestHandler request = new APIRequestHandler(ml_back_url + "/search");
                String postData = "{ \"searchText\": \"" + searchText + "\"}";
                String reply = request.postRequest(postData);

                if (reply.length() > 0) {
                    mlReply = reply;
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        });
        bkThread.start();
    }

    public Thread getBkThread() {
        return bkThread;
    }

    public String getMlReply() {
        return mlReply;
    }


}
