package com.api.face.microsoft.microsoftfaceapi.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.api.face.microsoft.microsoftfaceapi.ImageLoadService;
import com.api.face.microsoft.microsoftfaceapi.ImageResultReceiver;
import com.api.face.microsoft.microsoftfaceapi.R;
import com.api.face.microsoft.microsoftfaceapi.helper.ImageHelper;
import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.FaceServiceRestClient;
import com.microsoft.projectoxford.face.contract.Face;
import com.microsoft.projectoxford.face.contract.FaceRectangle;
import com.microsoft.projectoxford.face.rest.ClientException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;

public class DetectionFragment extends Fragment implements View.OnClickListener {

    private ImageView imageView;
    private TextView resultText;
    private Button browseButton;
    private final int UPLOAD_IMAGE = 1;
    private ImageResultReceiver imageReceiver;
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
        // imageReceiver = new ImageResultReceiver(new Handler());
        // imageReceiver.setReceiver(this);

        // Intent intent = new Intent(getActivity(), ImageLoadService.class);
        // intent.putExtra(URL, "http://b2blogger.com/pressroom/upload_images/gps-tracker_1.JPG");

        // intent.putExtra(CALLBACK, imageReceiver);
        //getActivity().startService(intent);

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

            Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
            imageView.setImageBitmap(bitmap);
            new DetectionTask().execute(bitmap);
        }
    }


//    @Override
//    public void onReceiveResult(int resultCode, Bundle resultData) {
//        Bitmap bitmap = resultData.getParcelable(RESULT_IMAGE);
//        imageView.setImageBitmap(bitmap);
//        String value = resultData.getString(RESULT_FACES);
//
//        Toast.makeText(getActivity(), value, Toast.LENGTH_LONG);
//        new DetectionTask().execute(bitmap);
//    }

    private class DetectionTask extends AsyncTask<Bitmap, Void, Face[]> {

        Bitmap bitmap;

        @Override
        protected Face[] doInBackground(Bitmap... params) {
            // Get an instance of face service client to detect faces in image.
            FaceServiceClient faceServiceClient = new FaceServiceRestClient(getString(R.string.subscription_key));
            bitmap = params[0];
            return ImageHelper.detect(params[0]);
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... progress) {
        }

        @Override
        protected void onPostExecute(Face[] result) {

            imageView.setImageBitmap(ImageHelper.drawFaceRectanglesOnBitmap(bitmap, result));

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
