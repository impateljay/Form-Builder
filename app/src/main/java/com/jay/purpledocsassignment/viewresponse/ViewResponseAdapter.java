package com.jay.purpledocsassignment.viewresponse;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.jay.purpledocsassignment.R;
import com.jay.purpledocsassignment.model.Forms;
import com.jay.purpledocsassignment.model.Question;
import com.jay.purpledocsassignment.model.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ViewResponseAdapter extends RecyclerView.Adapter<ViewResponseAdapter.MyViewHolder> {

    private List<Response> responses;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewFormName, textViewResponse;

        MyViewHolder(View view) {
            super(view);
            textViewFormName = view.findViewById(R.id.textViewFormName);
            textViewResponse = view.findViewById(R.id.textViewResponse);
        }
    }

    ViewResponseAdapter(List<Response> responses) {
        this.responses = responses;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.response_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Response response = responses.get(position);
        Forms forms = Forms.load(Forms.class, response.formId);
        holder.textViewFormName.setText("Form Name : " + forms.name);
        String answer = "";
        try {
            JSONArray jsonArray = new JSONArray(response.response);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                long questionId = Long.parseLong(json.getString("questionId"));
                Question question = Question.load(Question.class, questionId);
                String questionName = question.text;
                String answer1;
                if (question.type.equals("radio")) {
                    final String[] choices = question.options.split("\n");
                    if (json.has("answer")) {
                        answer1 = choices[Integer.parseInt(json.getString("answer"))];
                    } else {
                        answer1 = "Unanswered";
                    }
                } else if (question.type.equals("checkbox")) {
                    final String[] choices = question.options.split("\n");
                    answer1 = "";
                    if (json.has("answer") && json.getString("answer").length() > 2) {
                        String[] arr = json.getString("answer").substring(1, json.getString("answer").length() - 1).split(",");
                        for (int j = 0; j < arr.length; j++) {
                            int optionIndex = Integer.parseInt(arr[j]);
                            if (j == 0) {
                                answer1 += choices[optionIndex];
                            } else {
                                answer1 += ", " + choices[optionIndex];
                            }
                        }
                    } else {
                        answer1 = "Unanswered";
                    }
                } else {
                    if (json.has("answer") && !TextUtils.isEmpty(json.getString("answer"))) {
                        answer1 = json.getString("answer");
                    } else {
                        answer1 = "Unanswered";
                    }
                }
                if (i == jsonArray.length() - 1) {
                    answer += "Q. " + questionName + "\nA. " + answer1;
                } else {
                    answer += "Q. " + questionName + "\nA. " + answer1 + "\n\n";
                }
            }
        } catch (JSONException e) {
            Crashlytics.log(e.getMessage());
        }
        holder.textViewResponse.setText(answer);
    }

    @Override
    public int getItemCount() {
        return responses.size();
    }
}
