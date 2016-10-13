package com.api.face.microsoft.microsoftfaceapi.activities;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.api.face.microsoft.microsoftfaceapi.R;
import com.api.face.microsoft.microsoftfaceapi.adapters.ImageAdapter;
import com.api.face.microsoft.microsoftfaceapi.tasks.ImageHelper;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alina_Zhdanava on 10/12/2016.
 */

public class FaceListActivity extends AppCompatActivity implements View.OnClickListener {

    private Button addFaceButton;
    private EditText personName;
    private RecyclerView personRecyclerView;
    private ImageAdapter personRecyclerAdapter;
    private RecyclerView.LayoutManager personLayoutManager;

    private List<String> personList;
    private String personId;
    private String personGroupID;

    public FaceListActivity() {
    }

    public static PersonListActivity newInstance() {
        PersonListActivity fragment = new PersonListActivity();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_list);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Bundle bundle = getIntent().getExtras();

        addFaceButton = (Button) findViewById(R.id.add_face_button);
        addFaceButton.setOnClickListener(this);

        personName = (EditText) findViewById(R.id.person_name);
        personId = getIntent().getStringExtra("personID");

        personGroupID = bundle.getString("personGroupID");

        personRecyclerView = (RecyclerView) findViewById(R.id.person_faces);
        personRecyclerView.setHasFixedSize(true);
        personLayoutManager = new LinearLayoutManager(this);
        personRecyclerView.setLayoutManager(personLayoutManager);
        File rootDirectory = new File(Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DCIM + "/Camera");
        personList = getAllImagePaths(rootDirectory);
        personRecyclerAdapter = new ImageAdapter(personList, FaceListActivity.this);
        personRecyclerView.setAdapter(personRecyclerAdapter);

    }

    @Override
    public void onClick(View v) {
        for (String path : personRecyclerAdapter.getSelectedItems()) {
            new CreatePersonTask().execute(personGroupID, personName.getText().toString(), path);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private class CreatePersonTask extends AsyncTask<String, Void, Void> {
        //String personGroupId,
        // String personName,
        // String imagePath
        Bitmap bitmap = null;

        @Override
        protected Void doInBackground(String... params) {
            //ImageHelper.createPerson(params[0], params[1], params[2]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

        }
    }

    private List<String> getAllImagePaths(File rootDirectory) {

        File[] fileArray = rootDirectory.listFiles();
        List<String> allPaths = new ArrayList<>();
        for (File file : fileArray) {
            if (file.isDirectory()) {
                allPaths.addAll(getAllImagePaths(file));
            }
            if (file.getAbsolutePath().endsWith(".jpg")) {
                String filePath = file.getAbsolutePath();
                allPaths.add(filePath);
            }
        }
        return allPaths;
    }
}