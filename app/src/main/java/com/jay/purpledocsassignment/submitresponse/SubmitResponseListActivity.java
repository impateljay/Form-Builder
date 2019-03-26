package com.jay.purpledocsassignment.submitresponse;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.jay.purpledocsassignment.R;
import com.jay.purpledocsassignment.model.Answer;
import com.jay.purpledocsassignment.model.Forms;
import com.jay.purpledocsassignment.model.Question;
import com.jay.purpledocsassignment.model.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SubmitResponseListActivity extends AppCompatActivity {

    private List<Question> questionList = new ArrayList<>();
    private List<Answer> answerList = new ArrayList<>();
    private FormAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_response_list);

        final long formId = getIntent().getLongExtra("formId", -1);
        if (formId == -1) {
            Toast.makeText(SubmitResponseListActivity.this, "Failed to find form", Toast.LENGTH_SHORT).show();
            finish();
        }

        Forms forms = Forms.load(Forms.class, formId);
        final String[] questionIds = forms.questionIds.split(",");
        for (String questionId : questionIds) {
            questionList.add(Question.load(Question.class, Long.parseLong(questionId)));
            Collections.sort(questionList, new Comparator<Question>() {
                @Override
                public int compare(Question question, Question question2) {
                    if (question.number > question2.number) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
            });
        }

        Button buttonSubmitResponse = findViewById(R.id.buttonSubmitResponse);
        buttonSubmitResponse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pair<Boolean, List<String>> pair = readyToSubmit();
                if (pair.first) {
                    Response response = new Response();
                    JSONArray jsonArray = new JSONArray();
                    for (Answer answer : answerList) {
                        try {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("questionId", answer.getQuestionId());
                            jsonObject.put("answer", answer.getAnswer());
                            jsonArray.put(jsonObject);
                        } catch (JSONException e) {
                            Crashlytics.log(e.getMessage());
                        }
                    }
                    response.response = String.valueOf(jsonArray);
                    response.formId = formId;
                    response.save();
                    finish();
                } else {
                    List<String> questionIds = pair.second;
                    for (Question question : questionList) {
                        question.showError = questionIds.contains(question.getId().toString());
                    }
                    adapter.all_questions = questionList;
                    adapter.notifyDataSetChanged();
                }
            }
        });
        ListView listView = findViewById(R.id.listView);
        adapter = new FormAdapter(this, questionList);
        listView.setAdapter(adapter);
    }

    private Pair<Boolean, List<String>> readyToSubmit() {
        List<String> questionIds = new ArrayList<>();
        boolean readyToSubmit = true;
        for (Answer answer : answerList) {
            Question question = Question.load(Question.class, Long.parseLong(answer.getQuestionId()));
            if (question.isMandatory) {
                if (TextUtils.isEmpty(answer.getAnswer())) {
                    questionIds.add(answer.getQuestionId());
                    readyToSubmit = false;
                }
            }
        }
        return new Pair<>(readyToSubmit, questionIds);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    private class FormAdapter extends BaseAdapter {
        private static final String CHECK_SYMBOL = "\u2714";
        private static final String CROSS_SYMBOL = "\u2716";
        private static final String WARNING = "\u26A0";
        List<Question> all_questions;
        Context context;
        private LayoutInflater inflater;

        FormAdapter(Context context, List<Question> questions) {
            this.all_questions = questions;
            this.context = context;
            inflater = LayoutInflater.from(context);
            for (Question question : questionList) {
                Answer answer = new Answer();
                answer.setQuestionId(String.valueOf(question.getId()));
                answerList.add(answer);
            }
        }

        @Override
        public int getCount() {
            return all_questions.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        Question getQuestion(int i) {
            return all_questions.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            Question question = this.getQuestion(i);
            view = inflater.inflate(R.layout.submit_response_list_row, viewGroup, false);
            TextView textViewError = view.findViewById(R.id.textViewError);
            TextView textViewQuestionText = view.findViewById(R.id.textViewQuestionText);
            EditText editTextAnswer = view.findViewById(R.id.editTextAnswer);
            RadioGroup radioGroup = view.findViewById(R.id.radioGroup);
            LinearLayout linearLayoutCheckboxGroup = view.findViewById(R.id.linearLayoutCheckboxGroup);
            String question_content = question.text + (question.isMandatory ? "*" : "");
            textViewQuestionText.setText(question_content);

            if (question.showError){
                textViewError.setVisibility(View.VISIBLE);
            } else {
                textViewError.setVisibility(View.INVISIBLE);
            }

            switch (question.type) {
                case "text":
                    showEditText(editTextAnswer, false, i);
                    break;
                case "text_num":
                    showEditText(editTextAnswer, true, i);
                    break;
                case "radio":
                    showRadioButtons(radioGroup, question, i);
                    break;
                case "checkbox":
                    showCheckbox(linearLayoutCheckboxGroup, question, i);
                    break;
            }
            return view;
        }

        private void showEditText(final EditText editTextAnswer, boolean isNumeric, final int position) {
            final Answer answer = answerList.get(position);
            editTextAnswer.setSelection(0);
            if (isNumeric) {
                editTextAnswer.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
            }
            editTextAnswer.setVisibility(View.VISIBLE);
            editTextAnswer.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    if (!hasFocus) {
                        answer.setAnswer(editTextAnswer.getText().toString().trim());
                    }
                }
            });
            editTextAnswer.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    answer.setAnswer(editTextAnswer.getText().toString().trim());
                }
            });
            if (!TextUtils.isEmpty(answer.getAnswer())) {
                editTextAnswer.setText(answer.getAnswer());
            } else {
                editTextAnswer.setText("");
            }
        }

        private void showRadioButtons(RadioGroup radioGroup, final Question question, int position) {
            final Answer answer = answerList.get(position);
            radioGroup.setVisibility(View.VISIBLE);
            final String[] choices = question.options.split("\n");
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    int selected = radioGroup.getCheckedRadioButtonId();
                    if (selected == -1) {
                        return;
                    }
                    answer.setAnswer(String.valueOf(selected));
                }
            });
            RadioButton radioButton;
            int index = 0;
            for (String s : choices) {
                radioButton = new RadioButton(context);
                radioButton.setText(s);
                radioButton.setId(index++);
                radioGroup.addView(radioButton);
            }
            if (!TextUtils.isEmpty(answer.getAnswer())) {
                radioGroup.check(Integer.parseInt(answer.getAnswer()));
            } else {
                radioGroup.clearCheck();
            }
        }

        private void showCheckbox(final LinearLayout checkBoxGroup, Question question, int position) {
            final Answer answer = answerList.get(position);
            checkBoxGroup.setVisibility(View.VISIBLE);
            CheckBox checkBox;
            int index = 0;
            String[] choices = question.options.split("\n");
            CompoundButton.OnCheckedChangeListener checkListener = new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    List<String> checked_indexes = new ArrayList<>();
                    for (int i = 0; i < (checkBoxGroup).getChildCount(); ++i) {
                        CheckBox nextChild = (CheckBox) (checkBoxGroup).getChildAt(i);
                        if (nextChild.isChecked()) {
                            checked_indexes.add(String.valueOf(nextChild.getId()));
                        }
                    }
                    answer.setAnswer(getArray(checked_indexes));
                }
            };
            for (String s : choices) {
                checkBox = new CheckBox(context);
                checkBox.setId(index++);
                checkBox.setText(s);
                checkBox.setOnCheckedChangeListener(checkListener);
                checkBoxGroup.addView(checkBox);
            }
            if (answer.getAnswer() != null) {
                String[] selected_ids = extractArray(answer.getAnswer());
                CheckBox answerCB;
                for (String id : selected_ids) {
                    if (!TextUtils.isEmpty(id)) {
                        answerCB = checkBoxGroup.findViewById(Integer.parseInt(id.replace("\"", "")));
                        answerCB.setChecked(true);
                    }
                }
            }
        }

        private String getArray(List<String> strings) {
            String prefix = "";
            StringBuilder sb = new StringBuilder("");
            sb.append("[");
            for (String s : strings) {
                sb.append(prefix);
                prefix = ",";
                sb.append(s);
            }
            sb.append("]");
            return prefix.equals("") ? "" : sb.toString();
        }

        private String[] extractArray(String string) {
            String[] strings;
            if (!TextUtils.isEmpty(string)) {
                StringBuilder sb = new StringBuilder(string);
                sb.deleteCharAt(sb.length() - 1);
                sb.deleteCharAt(0);
                strings = sb.toString().split(",");
            } else {
                strings = new String[]{};
            }
            return strings;
        }
    }
}
