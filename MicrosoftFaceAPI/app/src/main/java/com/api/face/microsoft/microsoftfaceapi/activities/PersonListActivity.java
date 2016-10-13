package com.api.face.microsoft.microsoftfaceapi.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.api.face.microsoft.microsoftfaceapi.R;
import com.api.face.microsoft.microsoftfaceapi.adapters.PersonListAdapter;
import com.api.face.microsoft.microsoftfaceapi.tasks.ImageHelper;
import com.microsoft.projectoxford.face.contract.Person;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PersonListActivity extends AppCompatActivity implements View.OnClickListener {

    private Button addPersonButton;
    private TextView personId;
    private RecyclerView personRecyclerView;
    private PersonListAdapter personRecyclerAdapter;
    private RecyclerView.LayoutManager personLayoutManager;
    String personGroupID;

    public PersonListActivity() {
    }

    public static PersonListActivity newInstance() {
        PersonListActivity fragment = new PersonListActivity();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_list);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Bundle bundle = getIntent().getExtras();
        personGroupID = bundle.getString("personGroupID");

        addPersonButton = (Button) findViewById(R.id.add_person_button);
        addPersonButton.setOnClickListener(this);

        personId = (TextView) findViewById(R.id.person_id);

        personRecyclerView = (RecyclerView) findViewById(R.id.person_recycler_view);
        personRecyclerView.setHasFixedSize(true);
        personLayoutManager = new GridLayoutManager(this,2);
        personRecyclerView.setLayoutManager(personLayoutManager);
        personRecyclerAdapter = new PersonListAdapter(null, PersonListActivity.this);
        personRecyclerView.setAdapter(personRecyclerAdapter);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, FaceListActivity.class);
        intent.putExtra("personGroupID", personGroupID);
        intent.putExtra("personID", personId.getText());
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
           new GetAllPersonsTask().execute(personGroupID);

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

    private class GetAllPersonsTask extends AsyncTask<String, Void, List<Person>> {
        // params[0] - String personGroupId
        @Override
        protected List<Person> doInBackground(String... params) {
            return ImageHelper.getAllGroupPersons(params[0]);
        }

        @Override
        protected void onPostExecute(List<Person> persons) {
            personRecyclerAdapter.setmPersonDataset(persons);
            personRecyclerAdapter.notifyDataSetChanged();
        }
    }

    private class CreateGroupTask extends AsyncTask<String, Void, Void> {
        // params[0] - String personGroupId
// params[1] - String name
        @Override
        protected Void doInBackground(String... params) {
            ImageHelper.createGroup(params[0], params[1]);
            return null;
        }
    }

    private class CreatePersonTask extends AsyncTask<String, Void, Void> {
        //String personGroupId,
        // String personName,
        // String imagePath
        //

        Bitmap bitmap = null;

        @Override
        protected Void doInBackground(String... params) {
         //   ImageHelper.createPerson(params[0], params[1], params[2]);
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

    private List<Bitmap> getAllImagesFromDirectory(File rootDirectory) {
        File[] fileArray = rootDirectory.listFiles();
        List<Bitmap> allImages = new ArrayList<>();
        BitmapFactory.Options options = new BitmapFactory.Options();
        for (File file : fileArray) {
            if (file.getAbsolutePath().endsWith(".jpg")) {
                String filePath = file.getAbsolutePath();

                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(filePath, options);
                Log.i("TAG", options.outHeight + " /" + options.outWidth);
                options.inJustDecodeBounds = false;
                options.inSampleSize = 4;
                Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 200, 300, false);
                allImages.add(resizedBitmap);
            }
            if (file.isDirectory()) {
                allImages.addAll(getAllImagesFromDirectory(file));
            }
        }
        return allImages;
    }
}
