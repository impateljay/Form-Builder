package com.jay.purpledocsassignment.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

@Table(name = "Question")
public class Question extends Model {
    @Column(name = "question_id")
    public int questionId;
    @Column(name = "number")
    public int number;
    @Column(name = "text")
    public String text;
    @Column(name = "type")
    public String type;
    @Column(name = "options")
    public String options;
    @Column(name = "is_mandatory")
    public boolean isMandatory;
    public boolean showError;

    public static List<Question> getAllQuestions() {
        return new Select().from(Question.class).execute();
    }
}