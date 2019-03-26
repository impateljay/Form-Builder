package com.jay.purpledocsassignment.main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jay.purpledocsassignment.R;
import com.jay.purpledocsassignment.model.Forms;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MyViewHolder> {

    private List<Forms> formsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewName, textViewNumberOfQuestions;

        MyViewHolder(View view) {
            super(view);
            textViewName = view.findViewById(R.id.textViewName);
            textViewNumberOfQuestions = view.findViewById(R.id.textViewNumberOfQuestions);
        }
    }


    MainAdapter(List<Forms> formsList) {
        this.formsList = formsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.forms_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Forms form = formsList.get(position);
        holder.textViewName.setText("Form Name : " + form.name);
        holder.textViewNumberOfQuestions.setText("Number Of Questions : " + form.numberOfQuestions);
    }

    @Override
    public int getItemCount() {
        return formsList.size();
    }

    void setFormsList(List<Forms> forms) {
        this.formsList = forms;
    }
}
