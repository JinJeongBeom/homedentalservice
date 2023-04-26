package com.example.capstone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class ResultDiagnosis extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_diagnosis);

        getSupportActionBar().hide(); //앱바 숨기기

        mFirebaseAuth = FirebaseAuth.getInstance(); //초기화
    }
}