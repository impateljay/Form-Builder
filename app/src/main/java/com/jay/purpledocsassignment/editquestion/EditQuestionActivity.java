package com.jay.purpledocsassignment.editquestion;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jay.purpledocsassignment.R;

import java.util.LinkedHashMap;
import java.util.Map;

public class EditQuestionActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private String[] types = {"text", "text_num", "radio", "checkbox"};
    private String selectedType;
    private LinearLayout linearLayoutMainOptions;
    private LinkedHashMap<Integer, String> optionsValues = new LinkedHashMap<>();
    private int optionsCount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_question);

        setTitle("Edit Question");

        final int position = getIntent().getExtras().getInt("position");
        final int id = getIntent().getExtras().getInt("questionId");
        final int number = getIntent().getExtras().getInt("number");
        String text = getIntent().getExtras().getString("text");
        selectedType = getIntent().getExtras().getString("type");
        final String options = getIntent().getExtras().getString("options");
        final boolean mandatory = getIntent().getExtras().getBoolean("isMandatory");

        linearLayoutMainOptions = findViewById(R.id.linearLayoutMainOptions);
        final LinearLayout linearLayoutOptions = findViewById(R.id.linearLayoutOptions);
        Button buttonAddNewOption = findViewById(R.id.buttonAddNewOption);
        final CheckBox mandatoryCheckBox = findViewById(R.id.mandatoryCheckBox);
        mandatoryCheckBox.setChecked(mandatory);
        TextView buttonSave = findViewById(R.id.buttonSave);
        final EditText editTextQuestionNumber = findViewById(R.id.editTextQuestionNumber);
        editTextQuestionNumber.setText(String.valueOf(number));
        final TextInputLayout inputLayoutQuestionText = findViewById(R.id.inputLayoutQuestionText);
        final EditText editTextQuestionText = findViewById(R.id.editTextQuestionText);
        editTextQuestionText.setText(text);

        Spinner spinner = findViewById(R.id.spinnerQuestionType);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter arrayAdapter = new ArrayAdapter(EditQuestionActivity.this, android.R.layout.simple_spinner_item, types);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        switch (selectedType) {
            case "radio":
                spinner.setSelection(2);
                linearLayoutMainOptions.setVisibility(View.VISIBLE);
                if (options != null && options.length() > 0) {
                    String[] opsRadio = options.split("\n");
                    for (int i = 0; i < opsRadio.length; i++) {
                        final EditText editText = new EditText(EditQuestionActivity.this);
                        editText.setHint("Option Text");
                        editText.setId(i);
                        editText.setText(opsRadio[i]);
                        editText.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        editText.setPadding(20, 20, 20, 20);
                        if (linearLayoutOptions != null) {
                            linearLayoutOptions.addView(editText);
                        }
                        optionsValues.put(editText.getId(), editText.getText().toString());
                        editText.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                optionsValues.put(editText.getId(), s.toString());
                            }
                        });
                        optionsCount++;
                    }
                }
                break;
            case "checkbox":
                spinner.setSelection(3);
                linearLayoutMainOptions.setVisibility(View.VISIBLE);
                if (options != null && options.length() > 0) {
                    String[] opsCheckbox = options.split("\n");
                    for (int i = 0; i < opsCheckbox.length; i++) {
                        final EditText editText = new EditText(EditQuestionActivity.this);
                        editText.setHint("Option Text");
                        editText.setId(i);
                        editText.setText(opsCheckbox[i]);
                        editText.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        editText.setPadding(20, 20, 20, 20);
                        if (linearLayoutOptions != null) {
                            linearLayoutOptions.addView(editText);
                        }
                        optionsValues.put(editText.getId(), editText.getText().toString());
                        editText.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                optionsValues.put(editText.getId(), s.toString());
                            }
                        });
                        optionsCount++;
                    }
                }
                break;
            case "text_num":
                spinner.setSelection(1);
                linearLayoutMainOptions.setVisibility(View.GONE);
                break;
            default:
                spinner.setSelection(0);
                linearLayoutMainOptions.setVisibility(View.GONE);
                break;
        }

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(editTextQuestionText.getText())) {
                    inputLayoutQuestionText.setError("Please enter question text!");
                    inputLayoutQuestionText.setErrorEnabled(true);
                } else {
                    inputLayoutQuestionText.setError(null);
                    inputLayoutQuestionText.setErrorEnabled(false);
                }
                if ((selectedType.equals("radio") || selectedType.equals("checkbox")) && !hasOptions()) {
                    Toast.makeText(EditQuestionActivity.this, "Please add atleast 1 option to proceed", Toast.LENGTH_LONG).show();
                }
                if (!TextUtils.isEmpty(editTextQuestionText.getText()) && !((selectedType.equals("radio") || selectedType.equals("checkbox")) && !hasOptions())) {
                    String option = "";
                    for (Map.Entry<Integer, String> entry : optionsValues.entrySet()) {
                        option += entry.getValue() + "|||";
                    }
                    Intent intent = new Intent();
                    intent.putExtra("position", position);
                    intent.putExtra("questionId", id);
                    try {
                        intent.putExtra("number", Integer.parseInt(editTextQuestionNumber.getText().toString()));
                    } catch (Exception e) {
                        intent.putExtra("number", 0);
                    }
                    intent.putExtra("text", editTextQuestionText.getText().toString());
                    intent.putExtra("type", selectedType);
                    intent.putExtra("options", option);
                    intent.putExtra("isMandatory", mandatoryCheckBox.isChecked());
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });

        buttonAddNewOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(EditQuestionActivity.this);
                editText.setHint("Option Text");
                editText.setId(optionsCount);
                editText.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                editText.setPadding(20, 20, 20, 20);
                if (linearLayoutOptions != null) {
                    linearLayoutOptions.addView(editText);
                }
                optionsValues.put(editText.getId(), editText.getText().toString());
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        optionsValues.put(editText.getId(), s.toString());
                    }
                });
                optionsCount++;
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedType = types[position];
        if (position == 2 || position == 3) {
            linearLayoutMainOptions.setVisibility(View.VISIBLE);
        } else {
            linearLayoutMainOptions.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private boolean hasOptions() {
        for (Map.Entry<Integer, String> entry : optionsValues.entrySet()) {
            if (!TextUtils.isEmpty(entry.getValue())) {
                return true;
            }
        }
        return false;
    }
}
