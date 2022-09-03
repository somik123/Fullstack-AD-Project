package edu.iss.nus.group1.studybuddy.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.apache.commons.text.WordUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import edu.iss.nus.group1.studybuddy.R;
import edu.iss.nus.group1.studybuddy.ViewActivity;
import edu.iss.nus.group1.studybuddy.dto.MessageDTO;

public class MessageListAdapterNew extends RecyclerView.Adapter {
    protected static final int VIEW_TYPE_MESSAGE_SENT = 1;
    protected static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    protected static final int VIEW_TYPE_IMAGE_SENT = 3;
    protected static final int VIEW_TYPE_IMAGE_RECEIVED = 4;
    protected static final int VIEW_TYPE_URL_SENT = 5;
    protected static final int VIEW_TYPE_URL_RECEIVED = 6;

    protected static ClickListener clickListener;

    protected Context mContext;
    protected ArrayList<MessageDTO> mMessageList;
    protected Integer userId;

    protected static final String htmlImgStart = "<html><style>img{margin: auto auto; height: 100%; width: 100%; object-fit: contain;}</style><body><img src=\"";
    protected static final String htmlImgEnd = "\" \"\"/></body></html>";
    protected static final String htmlVidStart = "<html><style>video{margin: auto auto; height: 100%; width: 100%;}</style><body><video controls=\"\" autoplay=\"autoplay\" name=\"media\"><source src=\"";
    protected static final String htmlVidEnd = "\" type=\"video/mp4\"></video></body></html>";
    protected static final String videoIconUrl = "https://java.team1ad.site/images/video_placeholder.gif";

    public MessageListAdapterNew(Context context, ArrayList<MessageDTO> messageList, Integer userId) {
        mContext = context;
        mMessageList = messageList;
        this.userId = userId;
    }

    public void setMessageList(ArrayList<MessageDTO> mMessageList) {
        this.mMessageList = mMessageList;
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    // Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {
        MessageDTO message = (MessageDTO) mMessageList.get(position);

        String msgType = "";
        //prevents out of bounds error for 2nd if condition
        if (message.getMessage().length() > 5) {
            msgType = message.getMessage().substring(0, 5);
        }
        if (message.getUserId().equals(userId)) {
            // If the current user is the sender of the message
            if (msgType.equalsIgnoreCase("[vid=") || msgType.equalsIgnoreCase("[img=")) {
                return VIEW_TYPE_IMAGE_SENT;
            } else if (msgType.equalsIgnoreCase("[url=")) {
                return VIEW_TYPE_URL_SENT;

            } else {
                return VIEW_TYPE_MESSAGE_SENT;
            }
        } else {
            // If some other user sent the message
            if (msgType.equalsIgnoreCase("[vid=") || msgType.equalsIgnoreCase("[img=")) {
                return VIEW_TYPE_IMAGE_RECEIVED;
            } else if (msgType.equalsIgnoreCase("[url=")) {
                return VIEW_TYPE_URL_RECEIVED;

            } else {
                return VIEW_TYPE_MESSAGE_RECEIVED;
            }
        }
    }

    // Inflates the appropriate layout according to the ViewType.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_message_row_me, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_message_row_other, parent, false);
            return new ReceivedMessageHolder(view);
        } else if (viewType == VIEW_TYPE_IMAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_image_row_me, parent, false);
            return new SentImageHolder(view);
        } else if (viewType == VIEW_TYPE_IMAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_image_row_other, parent, false);
            return new ReceivedImageHolder(view);
        } else if (viewType == VIEW_TYPE_URL_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_url_row_me, parent, false);
            return new SentUrlHolder(view);
        } else if (viewType == VIEW_TYPE_URL_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_url_row_other, parent, false);
            return new ReceivedUrlHolder(view);
        }


        return null;
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        MessageListAdapterNew.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);

        void onItemLongClick(int position, View v);
    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MessageDTO message = (MessageDTO) mMessageList.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(message, position);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message, position);
                break;
            case VIEW_TYPE_IMAGE_SENT:
                ((SentImageHolder) holder).bind(message, position);
                break;
            case VIEW_TYPE_IMAGE_RECEIVED:
                ((ReceivedImageHolder) holder).bind(message, position);
                break;
            case VIEW_TYPE_URL_SENT:
                ((SentUrlHolder) holder).bind(message, position);
                break;
            case VIEW_TYPE_URL_RECEIVED:
                ((ReceivedUrlHolder) holder).bind(message, position);
                break;

        }
    }

    private String setDateIfNeeded(MessageDTO message, int position) {
        LocalDate date = LocalDate.parse(message.getTime().substring(0, "YYYY-MM-DD".length()));
        String dateOfMessage = date.getDayOfMonth() + " "
                + WordUtils.capitalizeFully(date.getMonth() + " "
                + date.getYear());
        //first message will need to display date
        if (position == 0) {
            return dateOfMessage;
        } else {
            //check message above if the date is different
            LocalDate dateOfPreviousMessage = LocalDate.parse(mMessageList.get(position - 1).getTime().substring(0, "YYYY-MM-DD".length()));
            //a) if previous message is different date, date needs to be displayed
            if (date.toEpochDay() != dateOfPreviousMessage.toEpochDay()) {
                return dateOfMessage;
            } else {
                //b) if above message is same date, no need to display since message(s) above it will handle it
                return "";
            }
        }
    }

    private int getColorFromMessage(Integer color) {
        if (color == null) {
            color = 3;
        }
        switch (color) {
            case 1:
                return Color.parseColor("#FF3A3A"); //red, angry
            case 2:
                return Color.parseColor("#FFC45E"); //light orange
            case 3:
                return Color.parseColor("#5CB2FF"); //light blue, neutral
            case 4:
                return Color.parseColor("#E1FF50"); //lime
            case 5:
                return Color.parseColor("#00FF76"); //green, happy
            default: // 0 or unexpected value for no color
                // message with only emotes and no text should have no color.
                return Color.parseColor("#774df2"); //purple/background color(no emotion)
        }
    }

    public String[] getHtmlCodeForWebview(String message) {
        final String htmlData;
        final String altHtmlData;

        String mediaType = message.substring(0, 5);
        if (mediaType.equals("[img=")) {
            String imgUrl = message.substring(5, message.length() - 1);
            htmlData = htmlImgStart + imgUrl + htmlImgEnd;
            altHtmlData = htmlData;
        } else if (mediaType.equals("[vid=")) {
            String videoURL = message.substring(5, message.length() - 1);
            htmlData = htmlImgStart + videoIconUrl + htmlImgEnd;
            altHtmlData = htmlVidStart + videoURL + htmlVidEnd;
        } else if (mediaType.equals("[url=")) {
            String urlLink = message.substring(5, message.length() - 1);
            htmlData = "<font color='#0099cc'> <a href=\"" + urlLink + "\">" + urlLink + "</a></font>";
            altHtmlData = htmlData;
        } else {
            htmlData = "";
            altHtmlData = "";
        }
        return new String[]{htmlData, altHtmlData};
    }

    public String getDisplayMessageForFiles(String message) {
        String name = message.substring(message.lastIndexOf("/") + 1, message.lastIndexOf("."));
        Optional<String> ext = Optional.ofNullable(message).filter(f -> f.contains("."))
                .map(f -> f.substring(message.lastIndexOf(".") + 1, message.length() - 1));
        String url = message.substring(5, message.length() - 1);

        String displayMessage = "";
        if (name.length() > 0 && ext.isPresent()) {
            displayMessage += "⬇️  DOWNLOAD  ⬇️";
            displayMessage += "\n" + name;
            displayMessage += "\nAttachment Type: " + ext.get().toUpperCase();

        }
        return displayMessage;
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView messageText, timeText, dateText;
        CardView cardView;

        SentMessageHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            messageText = itemView.findViewById(R.id.text_gchat_message_me);
            timeText = itemView.findViewById(R.id.text_gchat_timestamp_me);
            dateText = itemView.findViewById(R.id.text_gchat_date_me);
            cardView = itemView.findViewById(R.id.card_gchat_message_me);
        }

        void bind(MessageDTO message, int position) {
            messageText.setText(message.getMessage());
            dateText.setText(setDateIfNeeded(message, position));
            // Format the stored timestamp into a readable String using method.
            timeText.setText(message.getTime().substring(11, 16));
            cardView.setCardBackgroundColor(getColorFromMessage(message.getColor()));
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }

        @Override
        public boolean onLongClick(View v) {
            clickListener.onItemLongClick(getAdapterPosition(), v);
            return false;
        }
    }


    private class ReceivedMessageHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView messageText, timeText, nameText, dateText;
        ImageView profileImage;
        CardView cardView;

        ReceivedMessageHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            messageText = itemView.findViewById(R.id.text_gchat_message_other);
            dateText = itemView.findViewById(R.id.text_gchat_date_other);
            timeText = itemView.findViewById(R.id.text_gchat_timestamp_other);
            nameText = itemView.findViewById(R.id.text_gchat_user_other);
            profileImage = itemView.findViewById(R.id.image_gchat_profile_other);
            cardView = itemView.findViewById(R.id.card_gchat_message_other);
        }

        void bind(MessageDTO message, int position) {
            messageText.setText(message.getMessage());
            dateText.setText(setDateIfNeeded(message, position));
            // Format the stored timestamp into a readable String using method.
            timeText.setText(message.getTime().substring(11, 16));
            nameText.setText(message.getUserName());
            cardView.setCardBackgroundColor(getColorFromMessage(message.getColor()));

            String userPhoto = message.getUserPhoto();
            // Insert the profile image from the URL into the ImageView.
            if (userPhoto != null && userPhoto.length() > 10) {
                Glide
                        .with(itemView)
                        .load(userPhoto)
                        .into(profileImage);
            } else {
                profileImage.setImageResource(R.drawable.no_img_avatar);
            }
            //Utils.displayRoundImageFromUrl(mContext, message.getSender().getProfileUrl(), profileImage);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }

        @Override
        public boolean onLongClick(View v) {
            clickListener.onItemLongClick(getAdapterPosition(), v);
            return false;
        }
    }

    private class SentImageHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView timeText, dateText;
        WebView webView;

        SentImageHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            webView = itemView.findViewById(R.id.webview_gchat_image_me);
            dateText = itemView.findViewById(R.id.text_gchat_date_me);
            timeText = itemView.findViewById(R.id.text_gchat_timestamp_me);
        }

        void bind(MessageDTO message, int position) {

            String[] strArr = getHtmlCodeForWebview(message.getMessage());
            final String htmlData = strArr[0];
            final String altHtmlData = strArr[1];

            webView.setVerticalScrollBarEnabled(false);
            webView.loadData(htmlData, "text/html", null);

            webView.setOnTouchListener((view, motionEvent) -> {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    Intent intent = new Intent(itemView.getContext(), ViewActivity.class);
                    intent.putExtra("htmlData", altHtmlData);
                    itemView.getContext().startActivity(intent);
                }
                return false;
            });

            dateText.setText(setDateIfNeeded(message, position));
            // Format the stored timestamp into a readable String using method.
            timeText.setText(message.getTime().substring(11, 16));
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }

        @Override
        public boolean onLongClick(View v) {
            clickListener.onItemLongClick(getAdapterPosition(), v);
            return false;
        }
    }

    private class ReceivedImageHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView timeText, nameText, dateText;
        ImageView profileImage;
        WebView webView;

        ReceivedImageHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            webView = itemView.findViewById(R.id.webview_gchat_image_other);
            dateText = itemView.findViewById(R.id.text_gchat_date_other);
            timeText = itemView.findViewById(R.id.text_gchat_timestamp_other);
            nameText = itemView.findViewById(R.id.text_gchat_user_other);
            profileImage = itemView.findViewById(R.id.image_gchat_profile_other);
        }

        void bind(MessageDTO message, int position) {

            String[] strArr = getHtmlCodeForWebview(message.getMessage());
            final String htmlData = strArr[0];
            final String altHtmlData = strArr[1];

            webView.setVerticalScrollBarEnabled(false);
            webView.loadData(htmlData, "text/html", null);

            webView.setOnTouchListener((view, motionEvent) -> {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    Intent intent = new Intent(itemView.getContext(), ViewActivity.class);
                    intent.putExtra("htmlData", altHtmlData);
                    itemView.getContext().startActivity(intent);
                }
                return false;
            });

            dateText.setText(setDateIfNeeded(message, position));
            // Format the stored timestamp into a readable String using method.
            timeText.setText(message.getTime().substring(11, 16));

            nameText.setText(message.getUserName());

            String userPhoto = message.getUserPhoto();
            // Insert the profile image from the URL into the ImageView.
            if (userPhoto != null && userPhoto.length() > 10) {
                Glide
                        .with(itemView)
                        .load(userPhoto)
                        .into(profileImage);
            } else {
                profileImage.setImageResource(R.drawable.no_img_avatar);
            }

        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }

        @Override
        public boolean onLongClick(View v) {
            clickListener.onItemLongClick(getAdapterPosition(), v);
            return false;
        }
    }

    private class SentUrlHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView messageText, timeText, dateText;
        CardView cardView;
        ImageView imageView;

        SentUrlHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            messageText = itemView.findViewById(R.id.url_gchat_message_me);
            dateText = itemView.findViewById(R.id.text_gchat_date_me);
            timeText = itemView.findViewById(R.id.text_gchat_timestamp_me);
            cardView = itemView.findViewById(R.id.card_gchat_message_me);
            imageView = itemView.findViewById(R.id.downloadIcon);
        }

        public void bind(MessageDTO message, int position) {

            String displayMessage = getDisplayMessageForFiles(message.getMessage());

            messageText.setText(displayMessage);
            dateText.setText(setDateIfNeeded(message, position));
            // Format the stored timestamp into a readable String using method.
            timeText.setText(message.getTime().substring(11, 16));
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }

        @Override
        public boolean onLongClick(View v) {
            clickListener.onItemLongClick(getAdapterPosition(), v);
            return false;
        }
    }


    private class ReceivedUrlHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView messageText, timeText, nameText, dateText;
        ImageView profileImage;

        ReceivedUrlHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            messageText = itemView.findViewById(R.id.url_gchat_message_other);
            dateText = itemView.findViewById(R.id.text_gchat_date_other);
            timeText = itemView.findViewById(R.id.text_gchat_timestamp_other);
            nameText = itemView.findViewById(R.id.text_gchat_user_other);
            profileImage = itemView.findViewById(R.id.image_gchat_profile_other);
        }

        public void bind(MessageDTO message, int position) {

            String displayMessage = getDisplayMessageForFiles(message.getMessage());

            messageText.setText(displayMessage);

            dateText.setText(setDateIfNeeded(message, position));
            // Format the stored timestamp into a readable String using method.
            timeText.setText(message.getTime().substring(11, 16));
            nameText.setText(message.getUserName());

            String userPhoto = message.getUserPhoto();
            // Insert the profile image from the URL into the ImageView.
            if (userPhoto != null && userPhoto.length() > 10) {
                Glide
                        .with(itemView)
                        .load(userPhoto)
                        .into(profileImage);
            } else {
                profileImage.setImageResource(R.drawable.no_img_avatar);
            }
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }

        @Override
        public boolean onLongClick(View v) {
            clickListener.onItemLongClick(getAdapterPosition(), v);
            return false;
        }
    }
}