package com.jay.purpledocsassignment.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.jay.purpledocsassignment.R;
import com.jay.purpledocsassignment.createform.CreateFormActivity;
import com.jay.purpledocsassignment.model.Forms;
import com.jay.purpledocsassignment.submitresponse.SubmitResponseListActivity;
import com.jay.purpledocsassignment.utility.RecyclerTouchListener;
import com.jay.purpledocsassignment.viewresponse.ViewResponseActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Forms> formsList = new ArrayList<>();
    private MainAdapter adapter;
    private RecyclerView recyclerView;
    private TextView textViewNoRecords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Forms");

        recyclerView = findViewById(R.id.recyclerView);
        textViewNoRecords = findViewById(R.id.textViewNoRecords);
        formsList = Forms.getAllForms();
        adapter = new MainAdapter(formsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Forms forms = formsList.get(position);
                Intent intent = new Intent(MainActivity.this, SubmitResponseListActivity.class);
                intent.putExtra("formId", forms.getId());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));

        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateFormActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        formsList = Forms.getAllForms();
        adapter.setFormsList(formsList);
        adapter.notifyDataSetChanged();
        if (formsList.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            textViewNoRecords.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            textViewNoRecords.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.viewAllResponse:
                startActivity(new Intent(MainActivity.this, ViewResponseActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
