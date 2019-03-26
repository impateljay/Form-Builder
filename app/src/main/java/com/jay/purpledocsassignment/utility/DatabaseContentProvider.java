package com.jay.purpledocsassignment.utility;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.activeandroid.content.ContentProvider;
import com.jay.purpledocsassignment.model.Forms;
import com.jay.purpledocsassignment.model.Question;
import com.jay.purpledocsassignment.model.Response;

public class DatabaseContentProvider extends ContentProvider {

    @Override
    protected Configuration getConfiguration() {
        Configuration.Builder builder = new Configuration.Builder(getContext());
        builder.addModelClass(Forms.class);
        builder.addModelClass(Question.class);
        builder.addModelClass(Response.class);
        Configuration configuration = builder.create();
        ActiveAndroid.initialize(configuration);
        return configuration;
    }
}