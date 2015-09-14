package com.uyogist.uyogist;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;

import java.io.File;

/**
 * Custom Dialog Fragment
 * Created by oyewale on 9/13/15.
 */
public class CreateGistDialogFragment extends DialogFragment implements ImageChooserListener {
    private static final String TAG = CreateGistDialogFragment.class.getSimpleName();
    private EditText mGistText;
    private Button addPhotoButton;
    private ImageView photoPreview;

    private ImageChooserManager imageChooserManager;
    private int chooserType;
    private String mediaPath;


    // Empty constructor required for DialogFragment
    public CreateGistDialogFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_gist, container);
        mGistText = (EditText) view.findViewById(R.id.gist_text);
        addPhotoButton = (Button) view.findViewById(R.id.add_photo_button);
        photoPreview = (ImageView) view.findViewById(R.id.img_preview);

        mGistText.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        getDialog().setTitle("Enter Your Gist Jor");

        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });

        return view;
    }

    private void takePicture() {
        chooserType = ChooserType.REQUEST_CAPTURE_PICTURE;
        imageChooserManager = new ImageChooserManager(this,
                ChooserType.REQUEST_CAPTURE_PICTURE, true);
        imageChooserManager.setImageChooserListener(this);
        try {
            mediaPath = imageChooserManager.choose();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("media_path")) {
                mediaPath = savedInstanceState.getString("media_path");
            }
            if (savedInstanceState.containsKey("chooser_type")) {
                chooserType = savedInstanceState.getInt("chooser_type");
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("On Activity Result", requestCode + "");
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (imageChooserManager == null) {
                imageChooserManager = new ImageChooserManager(this, requestCode, true);
                imageChooserManager.setImageChooserListener(this);
                imageChooserManager.reinitialize(mediaPath);
            }
            imageChooserManager.submit(requestCode, data);
        }
    }

    @Override
    public void onImageChosen(final ChosenImage image) {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (image != null) {
                    photoPreview.setImageURI(Uri.parse(new File(image
                            .getFileThumbnail()).toString()));

                }
            }
        });
    }

    @Override
    public void onError(final String reason) {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(CreateGistDialogFragment.this.getActivity(), reason,
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (chooserType != 0) {
            outState.putInt("chooser_type", chooserType);
        }
        if (mediaPath != null) {
            outState.putString("media_path", mediaPath);
        }
    }

}
