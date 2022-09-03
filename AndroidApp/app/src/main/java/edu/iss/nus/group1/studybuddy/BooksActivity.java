package edu.iss.nus.group1.studybuddy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import edu.iss.nus.group1.studybuddy.adapters.BookListAdapter;
import edu.iss.nus.group1.studybuddy.dto.BookDTO;
import edu.iss.nus.group1.studybuddy.utility.APICommunication;

public class BooksActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    protected ArrayList<BookDTO> bookDTOS = new ArrayList<>();
    protected ImageView btnBookSearch;
    protected EditText txtBookSearch;
    ListView listView;

    public Integer userId;
    public String username;
    public String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);

        listView = findViewById(R.id.listViewBooks);
        btnBookSearch = findViewById(R.id.btnBookSearch);
        txtBookSearch = findViewById(R.id.txtBookSearch);

        SharedPreferences pref = getSharedPreferences("user_credentials", MODE_PRIVATE);
        userId = pref.getInt("userId", -1);
        username = pref.getString("username", null);
        token = pref.getString("token", null);

        getBooks(null);
        initBookList();

        btnBookSearch.setOnClickListener(view -> {
            // Get search string and find books with it
            String bookSearchStr = txtBookSearch.getText().toString();
            if (!TextUtils.isEmpty(bookSearchStr)) {
                getBooks(bookSearchStr);
            } else {
                getBooks(null);
            }
            initBookList();
        });

        txtBookSearch.setOnKeyListener((v, keyCode, event) -> {
            // If the event is a key-down event on the "enter" button
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                // Perform action on key press
                // Get search string and find books with it
                String bookSearchStr = txtBookSearch.getText().toString();
                if (!TextUtils.isEmpty(bookSearchStr)) {
                    getBooks(bookSearchStr);
                } else {
                    getBooks(null);
                }
                initBookList();
                return true;
            }
            return false;
        });
    }

    protected void getBooks(String bookSearchStr) {
        APICommunication apiComm = new APICommunication(token);

        if (bookSearchStr != null && !bookSearchStr.isEmpty()) {
            //search for books
            apiComm.getBookSearch(bookSearchStr);
        } else {
            apiComm.getUserBooks(username);
        }

        String reply = apiComm.getResult();
        if (reply == null) return;

        List<BookDTO> listBooks = apiComm.jsonToArrayList(BookDTO.class);

        bookDTOS = new ArrayList<>();
        if (listBooks != null) {
            for (BookDTO bookDTO : listBooks) {
                bookDTO.setPublishDate(bookDTO.getPublishDate().substring(0, 10));
            }
            bookDTOS.addAll(listBooks);
        }

        if (bookDTOS.isEmpty()) {
            bookDTOS.add(new BookDTO(1, "No books found!", "", "", "", "", ""));
        }
    }

    protected void initBookList() {
        if (listView != null) {
            listView.setAdapter(new BookListAdapter(this, bookDTOS));
            listView.setOnItemClickListener(this);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> av, View v, int pos, long id) {
        BookDTO bookDTO = bookDTOS.get(pos);

        Uri uri = Uri.parse(bookDTO.getLink());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        //if (intent.resolveActivity(getPackageManager()) != null)
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }
}