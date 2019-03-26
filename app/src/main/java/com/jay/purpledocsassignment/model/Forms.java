package com.jay.purpledocsassignment.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

@Table(name = "Forms")
public class Forms extends Model {
    @Column(name = "name")
    public String name;
    @Column(name = "number_of_questions")
    public int numberOfQuestions;
    @Column(name = "question_ids")
    public String questionIds;

    public static List<Forms> getAllForms() {
        return new Select().from(Forms.class).execute();
    }
}