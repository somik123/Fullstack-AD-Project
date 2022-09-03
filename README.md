# FullStack AD Project

FrontEnd(ReactJS,Android), BackEnd(Java, Python(ML)), DB MySQL, and deployment with Docker compose

## Deployment
(Requires docker and docker compose to be installed)

The current IP address or the hostname of the docker host is required for the following steps. 
As my local ip address is `10.0.0.20` I'll be using this as the example. Replace this with the IP address or hostname of the docker host.

<p>&nbsp;</p>

To point the React App to correct backend, edit the file: 
```
Frontend_React/src/utils/AppSetting.js
```

And change it to:
```
export const AppSetting = {
    BASE_URL: "http://10.0.0.20:8080",
    FILE_UPLOAD_URL: "http://10.0.0.20:8080/file"
};
```

<p>&nbsp;</p>


To point the Android App to correct backend, edit the file: 
```
AndroidApp/app/src/main/java/edu/iss/nus/group1/studybuddy/utility/APICommunication.java
```

(in android studio, it is under the package `edu.iss.nus.group1.studybuddy.utility`

And change lines `22` to `27` to read:
```
    protected String api_file_upload = "http://10.0.0.20:8080/file";
    protected String api_base_url = "http://10.0.0.20:8080";
```
(Note that if running on Android emulator on host running docker, the IP can be set to `10.0.2.2` to access the host from emulator.
.

<p>&nbsp;</p>

From the main directory (the one with docker-compose.yml file) run the command:
```docker compose build```

Once complete, run:
```docker compose up -d```

React will be running port `3000` accessed by host IP adress. In my case, it's `http://10.0.0.20:3000/`


<p>&nbsp;</p>

<p>&nbsp;</p>


## Login details

|  Username  |  Password  |
|------------|------------|
|  somik     |  pass123   |
|  liyuan    |  pass123   |
|  nyanhtet  |  pass123   |
|  aungkhant |  pass123   |
|  ziyou     |  pass123   |
|  amber     |  pass123   |
|  tingting  |  pass123   |


<p>&nbsp;</p>

<p>&nbsp;</p>


## Android App features

### General
1. Register for new account
2. Login to app

### Study Groups / Chats
1. Chat in Study groups
2. New message notifications. Tap on notification to see the new message.
3. Able to send images/videos/files.
4. If using Gboard (Google Keyboard) can send reaction gifs or stickers built into the Keyboard
5. Long pressing on any chat message will copy it's contents to the clipboard
6. Able to view images/play videos in full screen by tapping on them from the chat screen.
7. Able to download files sent
8. Search for users to chat 1 to 1

### Events
1. View all available events
2. Join events
3. Create new events

### Books
1. View books recommended based on interests & groups joined
2. Search for books
3. View books

### Profile
1. View current profile
2. Change profile photo by tapping on the profile image
3. Edit interests
4. Change password
5. Logout

<p>&nbsp;</p>

<p>&nbsp;</p>


### Technical Challenges

#### Utility Classes
1. Custom APIRequestHandler class to talk to API using raw HTTP commands. The method postFile was specially challenging as a multipart post request was needed to be created for this purpose.
2. Custom APICommunication class to wrap custom class APIRequestHandler and parse JSON replies.

#### Widget Class
1. Custom GifEditText to override android EditText to allow for GBoard (Google Keyboard) gif/sticker support.

#### Adapter Classes
1. Custom ArrayAdapter to display books in BookListAdapter
2. Custom ArrayAdapter to display groups in GroupListAdapter
3. Custom ArrayAdapter to display users in UserListAdapter
4. Custom RecyclerView Adapter to display events in EventListAdapter
5. Custom RecyclerView Adapter to display chats in MessageListAdapter

#### Services
1. Custom Foreground service to poll groups for new messages



<p>&nbsp;</p>

<p>&nbsp;</p>


## React App features


### General
1. Register for new account
2. Login to app

### Study Groups / Chats

1. Study groups with chatting.
2. Able to send images/videos/files.
3. Able to view images/play videos in full screen by tapping on them from the chat screen.
4. Able to download files sent
5. Search for users to chat 1 to 1
6. Both light mode and dark mode available
7. Mobile responsive for small device
8. Autoscroll to last message when click on each chat list.
9. Display message send time based on current timestamp.

### Events
1. View all available events with Calendar View
2. Join events or View Discussion (already member of group) by clicking each event
3. Create new events based on Date and Time
4. Have Agenda View.

### Books
1. View books recommended based on interests & groups joined
2. Search for books with autocomplete feature
3. View books

### Profile
1. View current profile
2. Change profile photo by clicking upload photo (Drag and Drop supported)
3. Edit interests, email, display name
4. Change password
5. Logout


### Technical Challenges

#### React Hook
1. React hook is a bit tricky. If you don't handle api request properly using hook effect, you will get memory heat problem.
2. React hook is newly introduced feature, it takes a bit of time to understand and use it.

#### Client Site Routing
1. react-router-dom V6 is used for client site routing. take a bit of time to know how to use the library.

#### CORS issue
1. This issue always happen when server and client site are not in the same domain. So, server reject every request from client because of cors issue.
2. Able to solve by setting up Cors compitablity setting in backend code.

#### Styling issue
1. In order to support multi mode in chat style, style was created using sass instead of css. By using sass, eliminateed lots of duplicate code.

#### HTTPS issue
1. In order to send data over the internet, browser compains unless it is being sent over a HTTPS tunnel. So react is running behind a Nginx reverse proxy providing HTTPS support.
2. If react is running with HTTPS, it cant send data to JAVA backend as it was running without HTTPS. So now all the servers are behind Nginx reverse proxies.

#### Auto Scroll  
1. Auto scroll chat history when there is new message especially image in React. When message is arrived, auto scroll will be triggered but image is still loading, and browser don't know exact height to be scrolled yet. 
2. So able to solve issue by adding auto scroll function inside onload event of image. 

#### Sate Management 
1. React UI state management. To maintain single source of truth between Parent and Child components. 

<p>&nbsp;</p>

<p>&nbsp;</p>


## Java Backend features

Before using the resource API endpoints, please make sure your account is registered in the server as one of the users. If not register yet, you can use "Register user " endpoint to register. 

Authentication (JWT Token) 

We use JWT for communications between clients and server. The client needs to have correct username and password when getting token from server. When the username and password is correct, server will give token for authentication purposes. The client needs to use the JWT token every time when communicating with resources API. The JWT token expiration time is 90 days. 

You can get the JWT token from folder name "JWT generate token" and use "generate token". 

User 

1. To register the new user, use "register user". 

2. To update the user information, use "update user". 

3. To update the password, use "update password". 

4. To edit user interest, use "edit user interest". 

Message 

1. To send the message for both public and private group, use "create message in group". 

2. To retrieve messages in both public and private group with limit and offset, use "retrieve messages by groupId with limit and offset". OR to retrieve all messages in both public and private group, use "find Messages by groupId". 

Group (PUBLIC, PRIVATE)  

1. To create PUBLIC Group, use " create new group". 

2. To create PRIVATE Group, use "create new or retrieve existing individual group with two users". (NOTE: It will give existing group when private with two participants exist) 

3. To search the group which user join as participant, use "search group by single user as participant". 

Event 

1. To create Event, use "Create Event". (NOTE: It will also create public group) 

2. To join the event as user, use "user join event". 

3. To search the specific event, use "search event by Id". 

4. To get all existing events, use "get all events". 

Book 

1. To search the book, use "find book by Id". 

2. To add new book, use "create book". 

3. To search the book by "title", use " search book with title". 

4. To search the book ordered by use, use “get user ordered book” 

### General

1. Handles authorization and authenication for users on React and Android using Json Web Tokens

2. Handles all CRUD functions for all entites by providing API calls for React and Android

3. Communicates with Python backend for its ML calculations and functions with API calls

4. Serves as a File server for users to store profile pictures, chat images, videos and file downloads.  

### Technical Challenges  

#### JWT tokens

Authentication 

1. To make sure all the server communication pass through the " JWT Authentication Entry Point”. 

2. To show authentication error message when token is expired. 

General 

1. Try not to make circular references in development. 

2. Try not to get null exception in getting Object. 

3. Try to meet business logic. 

#### File server
(Somik elaborate on this?)

<p>&nbsp;</p>

<p>&nbsp;</p>


## ML features

### General

1. Sentiment Analysis using BERT model  

2. Book recommendation order using: 
  i. Picked interests
  ii. Cosine similarity of interests and latest event joined against textbook descriptions
  iii. Supported by scraped textbook data from <https://open.bccampus.ca/browse-our-collection/find-open-textbooks/>
3. Book search top 5 results using:
  i. Cosine Similarity against textbook descriptions and title

### Technical Challenges

1. Scraping of textbooks are done programmatically but manually. Any changes to website format might break the written webscraping code.  
2. Since the code for cosine similarity is adapted from the workshop, the challenge is to modify it to work on the textbooks data and serve the function as API call.  


<p>&nbsp;</p>

<p>&nbsp;</p>

