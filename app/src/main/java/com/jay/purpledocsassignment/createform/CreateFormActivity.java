package com.jay.purpledocsassignment.createform;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jay.purpledocsassignment.R;
import com.jay.purpledocsassignment.editquestion.EditQuestionActivity;
import com.jay.purpledocsassignment.model.Forms;
import com.jay.purpledocsassignment.model.Question;
import com.jay.purpledocsassignment.utility.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

public class CreateFormActivity extends AppCompatActivity {

    private List<Question> questionList = new ArrayList<>();
    private CreateFormAdapter adapter;
    private RecyclerView recyclerView;
    private TextView textViewNoRecords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_form);

        setTitle("Create Form");

        Button buttonSaveForm = findViewById(R.id.buttonSaveForm);
        recyclerView = findViewById(R.id.recyclerView);
        textViewNoRecords = findViewById(R.id.textViewNoRecords);

        adapter = new CreateFormAdapter(questionList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(CreateFormActivity.this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);

        changeVisibility();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Question question = questionList.get(position);
                Intent intent = new Intent(CreateFormActivity.this, EditQuestionActivity.class);
                intent.putExtra("position", question.questionId);
                intent.putExtra("questionId", question.questionId);
                intent.putExtra("number", question.number);
                intent.putExtra("text", question.text);
                intent.putExtra("type", question.type);
                intent.putExtra("options", question.options);
                intent.putExtra("isMandatory", question.isMandatory);
                startActivityForResult(intent, 1);
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));

        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = adapter.getItemCount();
                Question question = new Question();
                question.questionId = count + 1;
                question.number = count + 1;
                question.type = "text";
                question.text = "";
                question.options = "";
                adapter.addQuestion(question);
                Intent intent = new Intent(CreateFormActivity.this, EditQuestionActivity.class);
                intent.putExtra("position", question.questionId);
                intent.putExtra("questionId", question.questionId);
                intent.putExtra("number", question.number);
                intent.putExtra("text", question.text);
                intent.putExtra("type", question.type);
                intent.putExtra("options", question.options);
                intent.putExtra("isMandatory", question.isMandatory);
                startActivityForResult(intent, 1);
            }
        });

        buttonSaveForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(CreateFormActivity.this);
                final EditText edittext = new EditText(CreateFormActivity.this);
                alert.setMessage("Enter Form Name");
                alert.setTitle("Submit Form");
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(16, 16, 16, 16);
                edittext.setLayoutParams(lp);
                alert.setView(edittext);
                alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Forms forms = new Forms();
                        forms.name = edittext.getText().toString();
                        List<Question> questions = adapter.getAllQuestions();
                        forms.numberOfQuestions = questions.size();
                        forms.questionIds = "";
                        for (Question question : questions) {
                            long questionId = question.save();
                            forms.questionIds += questionId + ",";
                        }
                        forms.save();
                        finish();
                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
                alert.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    int position = data.getIntExtra("position", 0);
                    int questionId = data.getIntExtra("questionId", 0);
                    int number = data.getIntExtra("number", 0);
                    String text = data.getStringExtra("text");
                    String type = data.getStringExtra("type");
                    String option = data.getStringExtra("options");
                    String[] options = option.split("\\|\\|\\|");
                    Boolean mandatoryCheckBox = data.getBooleanExtra("isMandatory", false);
                    Question question = adapter.getAllQuestions().get(position - 1);
                    question.type = type;
                    question.number = number;
                    question.text = text;
                    question.questionId = questionId;
                    question.options = null;
                    question.isMandatory = mandatoryCheckBox;
                    for (String op : options) {
                        if (question.options == null) {
                            question.options = op + "\n";
                        } else if (op.length() > 0) {
                            question.options += op + "\n";
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
                changeVisibility();
            }
        }
    }

    private void changeVisibility() {
        if (questionList.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            textViewNoRecords.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            textViewNoRecords.setVisibility(View.VISIBLE);
        }
    }
}