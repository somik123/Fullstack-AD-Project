package edu.iss.nus.group1.studybuddy.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

import androidx.core.os.BuildCompat;
import androidx.core.view.inputmethod.EditorInfoCompat;
import androidx.core.view.inputmethod.InputConnectionCompat;

public class GifEditText extends androidx.appcompat.widget.AppCompatEditText {

    public interface ImageSelectedCallback {
        void onImageSelected(String message, String mimeType);
    }

    private ImageSelectedCallback imgSelectCallback;

    public void setImageSelectedCallback(ImageSelectedCallback callback) {
        this.imgSelectCallback = callback;
    }

    public GifEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        imgSelectCallback = null;
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo editorInfo) {

        final InputConnection ic = super.onCreateInputConnection(editorInfo);
        EditorInfoCompat.setContentMimeTypes(editorInfo, new String[]{"image/gif", "image/jpg", "image/jpeg", "image/png"});

        final InputConnectionCompat.OnCommitContentListener callback =
                (inputContentInfo, flags, opts) -> {
                    // read and display inputContentInfo asynchronously
                    if (BuildCompat.isAtLeastNMR1() && (flags &
                            InputConnectionCompat.INPUT_CONTENT_GRANT_READ_URI_PERMISSION) != 0) {
                        try {
                            inputContentInfo.requestPermission();
                        } catch (Exception e) {
                            return false; // return false if failed
                        }
                    }

                    // read and display inputContentInfo asynchronously.
                    // call inputContentInfo.releasePermission() as needed.
                    System.out.print("LINK URI: ");
                    System.out.println(inputContentInfo.getLinkUri());
                    //linkUri = inputContentInfo.getLinkUri().toString();
                    String message = "[img=" + inputContentInfo.getLinkUri().toString() + "]";
                    String mime = inputContentInfo.getDescription().getMimeType(0);

                    this.setText(message);
                    imgSelectCallback.onImageSelected(message, mime);

                    return true;  // return true if succeeded
                };
        return InputConnectionCompat.createWrapper(ic, editorInfo, callback);
    }

}
