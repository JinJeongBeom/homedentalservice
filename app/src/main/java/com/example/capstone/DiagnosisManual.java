package com.example.capstone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class DiagnosisManual extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnosis_manual);

        getSupportActionBar().hide(); //앱바 숨기기

        mFirebaseAuth = FirebaseAuth.getInstance(); //초기화

        Button btn_logout = findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //로그 아웃 하기
                mFirebaseAuth.signOut();
                Intent intent = new Intent(DiagnosisManual.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button btn_secession = findViewById(R.id.btn_secession);
        btn_secession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //탈퇴
                mFirebaseAuth.getCurrentUser().delete();
                Intent intent = new Intent(DiagnosisManual.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        TextView text = (TextView) findViewById(R.id.diagnosis_text);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DiagnosisManual.this, ResultDiagnosis.class);
                startActivity(intent);
                //finish();
            }
        });
    }
}