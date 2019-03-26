package com.jay.purpledocsassignment.createform;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jay.purpledocsassignment.R;
import com.jay.purpledocsassignment.model.Question;

import java.util.List;

public class CreateFormAdapter extends RecyclerView.Adapter<CreateFormAdapter.MyViewHolder> {

    private List<Question> questionList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewQuestionText, textViewQuestionNumber, textViewQuestionType, textViewOptionsTitle, textViewOptions;

        MyViewHolder(View view) {
            super(view);
            textViewQuestionText = view.findViewById(R.id.textViewQuestionText);
            textViewQuestionType = view.findViewById(R.id.textViewQuestionType);
            textViewQuestionNumber = view.findViewById(R.id.textViewQuestionNumber);
            textViewOptionsTitle = view.findViewById(R.id.textViewOptionsTitle);
            textViewOptions = view.findViewById(R.id.textViewOptions);
        }
    }

    CreateFormAdapter(List<Question> questionList) {
        this.questionList = questionList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Question question = questionList.get(position);
        holder.textViewQuestionNumber.setText("Question Number : " + String.valueOf(question.number));
        if (question.isMandatory) {
            holder.textViewQuestionText.setText("Question : " + question.text + "*");
        } else {
            holder.textViewQuestionText.setText("Question : " + question.text);
        }
        holder.textViewQuestionType.setText("Question Type : " + question.type);
        if (question.type.equals("radio") || question.type.equals("checkbox")) {
            holder.textViewOptionsTitle.setVisibility(View.VISIBLE);
            holder.textViewOptions.setVisibility(View.VISIBLE);
            holder.textViewOptions.setText(question.options);
        } else {
            holder.textViewOptionsTitle.setVisibility(View.GONE);
            holder.textViewOptions.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    List<Question> getAllQuestions() {
        return questionList;
    }

    void addQuestion(Question question) {
        questionList.add(question);
        notifyDataSetChanged();
    }
}
