package com.uyogist.uyogist.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
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
import com.squareup.picasso.Picasso;
import com.uyogist.uyogist.model.Gist;
import com.uyogist.uyogist.R;
import com.uyogist.uyogist.service.APIClient;
import com.uyogist.uyogist.service.UyoGistService;

import java.io.File;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

/**
 * Custom Dialog Fragment
 * Created by oyewale on 9/13/15.
 */
public class CreateGistDialogFragment extends DialogFragment implements ImageChooserListener {
    private static final String TAG = CreateGistDialogFragment.class.getSimpleName();
    private EditText mGistText;
    private Button addPhotoButton, selectPhotoButton;
    private ImageView photoPreview;
    private Button postButton;

    private ImageChooserManager imageChooserManager;
    private int chooserType;
    private String mediaPath;
    private String imagePath;
    private View postingProgressView;
    private View rootView;


    // Empty constructor required for DialogFragment
    public CreateGistDialogFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_gist, container);
        this.rootView = view;
        mGistText = (EditText) view.findViewById(R.id.gist_text);
        addPhotoButton = (Button) view.findViewById(R.id.add_photo_button);
        photoPreview = (ImageView) view.findViewById(R.id.img_preview);
        postButton = (Button) view.findViewById(R.id.post_button);
        selectPhotoButton = (Button) view.findViewById(R.id.select_photo_button);
        postingProgressView = (View) view.findViewById(R.id.posting_progress_view);

        mGistText.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        getDialog().setTitle("Enter Your Gist Jor");

        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postGist();
            }
        });
        selectPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPhoto();
            }
        });

        return view;
    }



    private void postGist() {
        UyoGistService service = APIClient.getUyoGistAPIService(getActivity());
        GistCallback callback = new GistCallback();
        postingProgressView.setVisibility(View.VISIBLE);

//        File imageFile = null;
//        if (imagePath != null){
//            imageFile = new File(imagePath);
////            TypedFile typedImage = new TypedFile("application/octet-stream", photo);
//        }
        TypedFile file = new TypedFile("image/*", new File(imagePath));
//        RequestBody file = RequestBody.create(MediaType.parse("image/*"), imagePath);
        service.postGist(file, "author", "Test Gist", callback);
    }

    private class GistCallback implements Callback<Gist> {

        @Override
        public void success(Gist gist, Response response) {
            Log.d(TAG, "success");
            postingProgressView.setVisibility(View.GONE);
            Snackbar snackbar = Snackbar.make(rootView, "Success, Gist Posted", Snackbar.LENGTH_SHORT);
            snackbar.setCallback(new Snackbar.Callback() {
                @Override
                public void onDismissed(Snackbar snackbar, int event) {
                    super.onDismissed(snackbar, event);
                    CreateGistDialogFragment.this.dismiss();
                }
            });
            snackbar.show();
        }

        @Override
        public void failure(RetrofitError error) {
            Log.e(TAG, error.getMessage());
            postingProgressView.setVisibility(View.GONE);
            Snackbar.make(rootView, "Error: " + error.getMessage(), Snackbar.LENGTH_SHORT).show();
        }
    }

    private void selectPhoto() {
        chooserType = ChooserType.REQUEST_PICK_PICTURE;
        imageChooserManager = new ImageChooserManager(this,
                ChooserType.REQUEST_PICK_PICTURE, true);
        imageChooserManager.setImageChooserListener(this);
        try {
            mediaPath = imageChooserManager.choose();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                    photoPreview.setVisibility(View.VISIBLE);
                    Picasso.with(getActivity())
                            .load(new File(image.getFileThumbnail()))
                            .fit()
                            .into(photoPreview);
                    imagePath = image.getFilePathOriginal();

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
                photoPreview.setVisibility(View.GONE);
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
