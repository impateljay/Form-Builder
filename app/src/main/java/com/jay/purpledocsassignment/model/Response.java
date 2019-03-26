package com.jay.purpledocsassignment.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

@Table(name = "Response")
public class Response extends Model {
    @Column(name = "form_id")
    public long formId;
    @Column(name = "response")
    public String response;

    public static List<Response> getAllResponses() {
        return new Select().from(Response.class).execute();
    }
}