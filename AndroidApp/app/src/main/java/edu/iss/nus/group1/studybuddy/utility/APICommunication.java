package edu.iss.nus.group1.studybuddy.utility;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.iss.nus.group1.studybuddy.dto.TokenDTO;


@SuppressWarnings("ThrowablePrintedToSystemOut")
public class APICommunication {

    // public static final String api_base_url = "https://java.hooks.cc";
    public static final String api_base_url = "https://java.team1ad.site";
    // public static final String api_base_url = "http://10.0.2.2:8080";

    // Max time in seconds before API will time-out
    protected int maxSleep = 60;

    protected String result = "";
    protected String token;

    protected Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .create();

    protected Thread bkThread;

    public APICommunication(String token) {
        super();
        this.token = token;
    }


    public void login(String username, String password) {
        Map<String, Object> postDataMap = new HashMap<>();
        postDataMap.put("username", username);
        postDataMap.put("password", password);
        String url = api_base_url + "/token/authenticate";

        bkThread = new Thread(() -> {
            try {
                String postData = gson.toJson(postDataMap);
                System.out.println("\n" + postData + "\n");

                APIRequestHandler requestHandler = new APIRequestHandler(url, null);
                result = requestHandler.postRequest(postData);
                System.out.println("\n" + result + "\n");

                // Convert JSON to User and set api_key here.
                TokenDTO tokenObj = jsonToObj(TokenDTO.class);
                if (tokenObj != null) {
                    this.token = tokenObj.getToken();
                    System.out.println("\n" + this.token + "\n");
                } else {
                    this.token = "NOTFOUND";
                }

            } catch (Exception e) {
                System.out.println(e);
                this.token = "NOTFOUND";
            }
        });
        bkThread.start();
        waitForResult();
    }


    public void getGroups(int userId) {
        String url = api_base_url + "/group/user/" + userId;

        bkThread = new Thread(() -> {
            try {
                APIRequestHandler requestHandler = new APIRequestHandler(url, token);
                result = requestHandler.getRequest();
            } catch (Exception e) {
                //System.out.println(e.toString());
                result = "";
            }
        });
        bkThread.start();
        waitForResult();
    }


    public void createPrivateGroups(int userId, int participantId) {
        String url = api_base_url + "/group/individual/" + userId + "/" + participantId;
        System.out.println(url);
        bkThread = new Thread(() -> {
            try {
                APIRequestHandler requestHandler = new APIRequestHandler(url, token);
                result = requestHandler.postRequest("");
            } catch (Exception e) {
                //System.out.println(e.toString());
                result = "";
            }
        });
        bkThread.start();
        waitForResult();
    }


    public void verifyToken() {
        String url = api_base_url + "/token/verify/";

        bkThread = new Thread(() -> {
            try {
                APIRequestHandler requestHandler = new APIRequestHandler(url, token);
                result = requestHandler.getRequest();
            } catch (Exception e) {
                //System.out.println(e.toString());
                result = "";
            }
        });
        bkThread.start();
        waitForResult();
    }

    public void getMessages(int groupId) {
        String url = api_base_url + "/message/group/" + groupId;

        bkThread = new Thread(() -> {
            try {
                APIRequestHandler requestHandler = new APIRequestHandler(url, token);
                result = requestHandler.getRequest();
            } catch (Exception e) {
                //System.out.println(e.toString());
                result = "";
            }
        });
        bkThread.start();
        waitForResult();
    }

    public void getAllEvents() {
        String url = api_base_url + "/event/all";

        bkThread = new Thread(() -> {
            try {
                APIRequestHandler requestHandler = new APIRequestHandler(url, token);
                result = requestHandler.getRequest();
            } catch (Exception e) {
                //System.out.println(e.toString());
                result = "";
            }
        });
        bkThread.start();
        waitForResult();
    }

    public void sendMessage(String message, int userId, int groupId, int replyTo) {
        Map<String, Object> postDataMap = new HashMap<>();
        postDataMap.put("message", message);
        postDataMap.put("userId", userId);
        postDataMap.put("groupId", groupId);
        if (replyTo != -1)
            postDataMap.put("replyTold", replyTo);
        String url = api_base_url + "/message/create";

        bkThread = new Thread(() -> {
            try {
                String postData = gson.toJson(postDataMap);
                System.out.println("\n" + postData + "\n");

                APIRequestHandler requestHandler = new APIRequestHandler(url, token);
                result = requestHandler.postRequest(postData);
                //System.out.println("\n" + result + "\n");
            } catch (Exception e) {
                System.out.println(e);
                result = "";
            }
        });
        bkThread.start();
        waitForResult();

    }

    public void getUserBooks(String username) {
        String url = api_base_url + "/book/user/" + username;
        System.out.println(url);
        bkThread = new Thread(() -> {
            try {
                APIRequestHandler requestHandler = new APIRequestHandler(url, token);
                result = requestHandler.getRequest();
                System.out.println("\n" + result + "\n");
            } catch (Exception e) {
                System.out.println(e);
                result = "";
            }
        });

        bkThread.start();
        waitForResult();
    }

    public void getBookSearch(String searchTxt) {
        Map<String, Object> postDataMap = new HashMap<>();
        postDataMap.put("searchText", searchTxt);

        String url = api_base_url + "/book/search/";
        System.out.println(url);

        bkThread = new Thread(() -> {
            try {
                String postData = gson.toJson(postDataMap);
                System.out.println("\nPostData:" + postData + "\n");

                APIRequestHandler requestHandler = new APIRequestHandler(url, token);
                result = requestHandler.postRequest(postData);
                System.out.println("\n" + result + "\n");
            } catch (Exception e) {
                System.out.println(e);
                result = "";
            }
        });

        bkThread.start();
        waitForResult();
    }

    public void getUserDetails(Integer userId) {
        String url = api_base_url + "/user/" + userId;
        bkThread = new Thread(() -> {
            try {
                APIRequestHandler requestHandler = new APIRequestHandler(url, token);
                result = requestHandler.getRequest();
            } catch (Exception e) {
                System.out.println(e);
            }
        });

        bkThread.start();
        waitForResult();
    }

    public void searchUserByName(String name) {
        String url = api_base_url + "/user/search/" + name;
        bkThread = new Thread(() -> {
            try {
                APIRequestHandler requestHandler = new APIRequestHandler(url, token);
                result = requestHandler.getRequest();
            } catch (Exception e) {
                System.out.println(e);
            }
        });

        bkThread.start();
        waitForResult();
    }


    public void registerUser(Map<String, Object> postDataMap) {
        String url = api_base_url + "/user/register";

        bkThread = new Thread(() -> {
            try {
                String postData = gson.toJson(postDataMap);
                System.out.println("\n" + postData + "\n");

                APIRequestHandler requestHandler = new APIRequestHandler(url, token);
                result = requestHandler.postRequest(postData);
                System.out.println("\n" + result + "\n");
            } catch (Exception e) {
                System.out.println(e);
                result = "";
            }
        });
        bkThread.start();
        waitForResult();
    }

    public void userAddEvent(int userId, int eventId) {
        String url = api_base_url + "/event/" + userId + "/" + eventId;
        bkThread = new Thread(() -> {
            try {
                APIRequestHandler requestHandler = new APIRequestHandler(url, token);
                result = requestHandler.postRequest(null);
                System.out.println("\n" + result + "\n");
            } catch (Exception e) {
                System.out.println(e);
                result = "";
            }
        });
        bkThread.start();
        waitForResult();
    }

    public void getAllInterest() {
        String url = api_base_url + "/interest/all";

        bkThread = new Thread(() -> {
            try {
                APIRequestHandler requestHandler = new APIRequestHandler(url, token);
                result = requestHandler.getRequest();
            } catch (Exception e) {
                System.out.println(e);
                result = "";
            }
        });
        bkThread.start();
        waitForResult();
    }

    public void updateInterest(Map<String, Object> postDataMap) {
        String url = api_base_url + "/interest/edit";

        bkThread = new Thread(() -> {
            try {
                String postData = gson.toJson(postDataMap);
                System.out.println("\n" + postData + "\n");

                APIRequestHandler requestHandler = new APIRequestHandler(url, token);
                result = requestHandler.postRequest(postData);
                System.out.println("\n" + result + "\n");
            } catch (Exception e) {
                System.out.println(e);
                result = "";
            }
        });
        bkThread.start();
        waitForResult();
    }


    public void changePassword(Map<String, Object> postDataMap) {
        String url = api_base_url + "/user/updatePassword";

        bkThread = new Thread(() -> {
            try {
                String postData = gson.toJson(postDataMap);
                System.out.println("\n" + postData + "\n");

                APIRequestHandler requestHandler = new APIRequestHandler(url, token);
                result = requestHandler.putRequest(postData);
                System.out.println("\n" + result + "\n");
            } catch (Exception e) {
                System.out.println(e);
                result = "";
            }
        });
        bkThread.start();
        waitForResult();
    }

    public void createEvent(Map<String, Object> postDataMap) {
        String url = api_base_url + "/event/";

        bkThread = new Thread(() -> {
            try {
                String postData = gson.toJson(postDataMap);
                System.out.println("\n" + postData + "\n");

                APIRequestHandler requestHandler = new APIRequestHandler(url, token);
                result = requestHandler.postRequest(postData);
                System.out.println("\n" + result + "\n");
            } catch (Exception e) {
                System.out.println(e);
                result = "";
            }
        });
        bkThread.start();
        waitForResult();
    }

    public void updateUserPhoto(Map<String, Object> postDataMap) {
        String url = api_base_url + "/user/updatePhoto";

        bkThread = new Thread(() -> {
            try {
                String postData = gson.toJson(postDataMap);
                System.out.println("\n" + postData + "\n");

                APIRequestHandler requestHandler = new APIRequestHandler(url, token);
                result = requestHandler.putRequest(postData);
                System.out.println("\n" + result + "\n");
            } catch (Exception e) {
                System.out.println(e);
                result = "";
            }
        });
        bkThread.start();
        waitForResult();
    }


    // Post file by InputStream and filenames by File
    public void uploadFile(InputStream fileStream, String fileName) {
        String url = api_base_url + "/file";

        bkThread = new Thread(() -> {
            try {
                APIRequestHandler requestHandler = new APIRequestHandler(url, token);
                result = requestHandler.postFile(fileStream, fileName);
                System.out.println("\n" + result + "\n");
            } catch (Exception e) {
                System.out.println(e);
                result = "";
            }
        });
        bkThread.start();
        waitForResult();
    }


    // Convert JSON to Map
    public <T> Map<String, T> jsonToMap() {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, T>>() {
        }.getType();
        return gson.fromJson(result, type);
    }

    // Convert JSON to Object

    public <T> T jsonToObj(Class<T> objClass) {
        return new Gson().fromJson(result, objClass);
    }
    // Convert JSON to ArrayMap

    public <T> ArrayList<T> jsonToArrayList(Class<T> clazz) {
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        Type type = new TypeToken<ArrayList<JsonObject>>() {
        }.getType();
        ArrayList<JsonObject> jsonObjects = new Gson().fromJson(result, type);

        ArrayList<T> arrayList = new ArrayList<>();

        for (JsonObject jsonObject : jsonObjects) {
            arrayList.add(new Gson().fromJson(jsonObject, clazz));
        }
        return arrayList;
    }

    protected void waitForResult() {
        // Wait for API to confirm login
        // While thread is still alive
        for (int i = 0; i < maxSleep * 10 && bkThread.isAlive(); i++) {
            try {
                Thread.sleep(100); // Wait 0.1s at a time for background thread to complete
                //System.out.print(" .");
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
        System.out.println();
    }


    public void setToken(String token) {
        this.token = token;
    }

    public String getResult() {
        return result;
    }

    public Thread getBkThread() {
        return bkThread;
    }

    public void setBkThread(Thread bkThread) {
        this.bkThread = bkThread;
    }
}


