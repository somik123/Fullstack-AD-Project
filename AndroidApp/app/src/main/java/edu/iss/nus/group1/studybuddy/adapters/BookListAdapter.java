package edu.iss.nus.group1.studybuddy.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import edu.iss.nus.group1.studybuddy.R;
import edu.iss.nus.group1.studybuddy.dto.BookDTO;
import edu.iss.nus.group1.studybuddy.utility.APICommunication;

public class BookListAdapter extends ArrayAdapter<Object> {
    protected final Context context;
    protected final ArrayList<BookDTO> bookDTOS;
    protected String image_base_url = APICommunication.api_base_url + "/images/textbooks/";

    public BookListAdapter(Context context, ArrayList<BookDTO> bookDTOS) {
        super(context, R.layout.chat_group_row);
        this.context = context;
        this.bookDTOS = bookDTOS;

        addAll(new Object[bookDTOS.size()]);
    }

    @NonNull
    public View getView(int pos, View view, @NonNull ViewGroup parent) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.book_row, parent, false);
        }
        BookDTO bookDTO = bookDTOS.get(pos);

        ImageView txtBookImage = view.findViewById(R.id.imgBookIcon);
        if (!bookDTOS.get(pos).getPhoto().isEmpty()) {
            Glide
                    .with(context)
                    .clear(view);
            Glide
                    .with(context)
                    .load(image_base_url + bookDTOS.get(pos).getPhoto())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(txtBookImage);
        }

        TextView txtBookTitle = view.findViewById(R.id.txtBookTitle);
        txtBookTitle.setText(bookDTO.getTitle());

        TextView txtBookPublish = view.findViewById(R.id.txtBookPublish);
        //hide Published text for 0 search results
        if (!bookDTO.getPublishDate().isEmpty()) {
            String publishDate = "Published: " + bookDTO.getPublishDate();
            txtBookPublish.setText(publishDate);
        }
        TextView txtBookDesc = view.findViewById(R.id.txtBookDesc);
        txtBookDesc.setText(bookDTO.getDescription());

        return view;
    }
}
