package com.api.face.microsoft.microsoftfaceapi.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.api.face.microsoft.microsoftfaceapi.R;
import com.api.face.microsoft.microsoftfaceapi.adapters.GroupListAdapter;
import com.api.face.microsoft.microsoftfaceapi.tasks.ImageHelper;
import com.microsoft.projectoxford.face.contract.PersonGroup;

import java.util.List;

public class GroupListActivity extends AppCompatActivity implements View.OnClickListener {

    private Button createGroupButton;
    private TextView groupName;
    private TextView groupId;
    private RecyclerView groupRecyclerView;
    private GroupListAdapter groupRecyclerAdapter;
    private RecyclerView.LayoutManager groupLayoutManager;

    public GroupListActivity() {
    }

    public static GroupListActivity newInstance() {
        GroupListActivity fragment = new GroupListActivity();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        createGroupButton = (Button) findViewById(R.id.create_group_button);
        createGroupButton.setOnClickListener(this);

        groupName = (TextView) findViewById(R.id.group_name);
        groupId = (TextView) findViewById(R.id.group_id);

        groupRecyclerView = (RecyclerView) findViewById(R.id.group_recycler_view);
        groupRecyclerView.setHasFixedSize(true);
        groupLayoutManager = new LinearLayoutManager(this);
        groupRecyclerView.setLayoutManager(groupLayoutManager);
        groupRecyclerAdapter = new GroupListAdapter(null, this);
        groupRecyclerView.setAdapter(groupRecyclerAdapter);
    }

    @Override
    public void onClick(View v) {
        new CreateGroupTask().execute(groupId.getText().toString(), groupName.getText().toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
            new GetAllGroupsTask().execute();
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

    private class GetAllGroupsTask extends AsyncTask<Void, Void, List<PersonGroup>> {
        // params[0] - String personGroupId
// params[1] - String name
        @Override
        protected List<PersonGroup> doInBackground(Void... params) {
            return ImageHelper.getAllGroups();
        }

        @Override
        protected void onPostExecute(List<PersonGroup> personGroups) {
            super.onPostExecute(personGroups);
            groupRecyclerAdapter.setmGroupDataset(personGroups);
            groupRecyclerAdapter.notifyDataSetChanged();
        }
    }

    private class CreateGroupTask extends AsyncTask<String, Void, Void> {
        // params[0] - String personGroupId
// params[1] - String group name
        @Override
        protected Void doInBackground(String... params) {
            ImageHelper.createGroup(params[0], params[1]);
            return null;
        }
    }
}
