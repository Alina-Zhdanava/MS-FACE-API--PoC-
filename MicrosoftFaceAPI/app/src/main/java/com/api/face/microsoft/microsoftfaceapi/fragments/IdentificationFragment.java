package com.api.face.microsoft.microsoftfaceapi.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.api.face.microsoft.microsoftfaceapi.activities.GroupListActivity;
import com.api.face.microsoft.microsoftfaceapi.R;
import com.api.face.microsoft.microsoftfaceapi.tasks.ImageHelper;

import static android.app.Activity.RESULT_OK;

public class IdentificationFragment extends Fragment implements View.OnClickListener {

    private ImageView imageView;
    private TextView resultText;
    private Button addImageButton;
    private Button addGroupButton;
    private Button identifyButton;
    private String picturePath;
    private final int UPLOAD_IMAGE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_identification, container, false);

        imageView = (ImageView) view.findViewById(R.id.view_image);
        addImageButton = (Button) view.findViewById(R.id.add_image_button);
        addGroupButton = (Button) view.findViewById(R.id.add_group_button);
        identifyButton = (Button) view.findViewById(R.id.identify_button);
        resultText = (TextView) view.findViewById(R.id.result_text);

        addImageButton.setOnClickListener(this);
        addGroupButton.setOnClickListener(this);
        identifyButton.setOnClickListener(this);

        //new DeleteAllGroupsTask().execute();

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.add_image_button: {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, UPLOAD_IMAGE);

                break;
            }

            case R.id.add_group_button: {
                Intent intent = new Intent(getActivity(), GroupListActivity.class);
                startActivity(intent);

                break;
            }

            case R.id.identify_button: {

                new IdentificationTask().execute(picturePath);
                break;
            }
        }
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
            picturePath = cursor.getString(columnIndex);
            cursor.close();

            Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
            imageView.setImageBitmap(bitmap);
        }
    }

    private class IdentificationTask extends AsyncTask<String, Void, String> {
        // String imagePath - params[0]
//
        protected String doInBackground(String... params) {

          //  return ImageHelper.identify(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i("TAG", "завершено");
                resultText.setText(result);
        }
    }

    private class DeleteAllGroupsTask extends AsyncTask<String, Void, Void> {
        // String imagePath - params[0]
//
        protected Void doInBackground(String... params) {
            ImageHelper.deleteAll();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.i("TAG", "завершено");
        }
    }

}


