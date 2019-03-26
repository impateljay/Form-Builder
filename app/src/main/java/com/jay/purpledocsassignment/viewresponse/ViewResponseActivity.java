package com.jay.purpledocsassignment.viewresponse;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jay.purpledocsassignment.R;
import com.jay.purpledocsassignment.model.Response;

import java.util.ArrayList;
import java.util.List;

public class ViewResponseActivity extends AppCompatActivity {

    List<Response> responses = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_response);

        setTitle("Submitted Forms");

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        TextView textViewNoRecords = findViewById(R.id.textViewNoRecords);
        responses = Response.getAllResponses();
        ViewResponseAdapter adapter = new ViewResponseAdapter(responses);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);

        if (responses.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            textViewNoRecords.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            textViewNoRecords.setVisibility(View.VISIBLE);
        }
    }
}
