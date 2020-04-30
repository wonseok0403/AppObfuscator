package com.ono.test_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    private String test = "test";

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.textView);
        textView.setText(test);
        fTest("테스트1");
    }

    void fTest(String test) {
        test = "테스트2";
        String t = "테스트3";
    }
}