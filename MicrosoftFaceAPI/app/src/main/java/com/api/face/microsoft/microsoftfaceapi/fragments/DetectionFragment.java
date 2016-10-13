package com.api.face.microsoft.microsoftfaceapi.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.api.face.microsoft.microsoftfaceapi.R;
import com.api.face.microsoft.microsoftfaceapi.tasks.ImageHelper;
import com.bumptech.glide.Glide;
import com.microsoft.projectoxford.face.contract.Face;

import java.util.concurrent.ExecutionException;

import static android.app.Activity.RESULT_OK;

public class DetectionFragment extends Fragment implements View.OnClickListener {

    private ImageView imageView;
    private TextView resultText;
    private Button browseButton;
    private final int UPLOAD_IMAGE = 1;
    public static final String URL = "url";
    public static final String CALLBACK = "receiver";
    public static final String RESULT_IMAGE = "image";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detection, container, false);

        imageView = (ImageView) view.findViewById(R.id.view_image);
        browseButton = (Button) view.findViewById(R.id.browse_button);
        resultText = (TextView) view.findViewById(R.id.result_text);

        browseButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        Bitmap bitmap = null;

        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, UPLOAD_IMAGE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UPLOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            Glide.with(getActivity()).load(picturePath).override(500, 500).into(imageView);
            Face[] result = ImageHelper.detectIImagePath(picturePath);
            imageView.setImageBitmap(ImageHelper.drawFaceRectanglesOnBitmap(((BitmapDrawable)imageView.getDrawable()).getBitmap(), result));

            String face_description = "Age: " + (result[0].faceAttributes.age) + "\n"
                    + "Gender: " + result[0].faceAttributes.gender + "\n"
                    + "Head pose(in degree): roll(" + result[0].faceAttributes.headPose.roll + "), "
                    + "yaw(" + result[0].faceAttributes.headPose.yaw + ")\n"
                    + "Glasses: " + result[0].faceAttributes.glasses + "\n"
                    + "Smile: " + result[0].faceAttributes.smile;

            resultText.setText(face_description);
        }
    }
}
