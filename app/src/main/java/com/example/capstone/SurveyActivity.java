package com.example.capstone;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class SurveyActivity extends AppCompatActivity {

    private TextView questionTextView;
    private RadioGroup answerRadioGroup;
    private Button nextButton;

    private String[] questions = {
            "1. 치아를 닦을 때 치실 혹은 치간솔을 이용하였습니까?",
            "2. 최근 3개월 동안, 치아가 쑤시거나 욱신거리거나 아픈 적이 있습니까?",
            "3. 최근 3개월 동안, 잇몸이 아프거나 피가 난 적이 있습니까?",
            "4. 최근 3개월 동안, 혀 또는 입 안쪽 뺨이 욱신거리며 아픈 적이 있습니까?",
            "5. 최근 3개월 동안, 찬음식을 먹거나 치아를 닦을 때에 치아가 시린 적이 있습니까?"
    };

    private String[] answers = new String[questions.length];

    private int currentQuestionIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        getSupportActionBar().hide(); //앱바 숨기기


        questionTextView = findViewById(R.id.questionTextView);
        answerRadioGroup = findViewById(R.id.answerRadioGroup);
        nextButton =findViewById(R.id.nextButton);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAnswer();
                showNextQuestion();
            }
        });

        showQuestion();
    }

    private void showQuestion() {
        questionTextView.setText(questions[currentQuestionIndex]);
        answerRadioGroup.clearCheck();
    }

    private void saveAnswer() {
        int selectedRadioButtonId = answerRadioGroup.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);

        if (selectedRadioButton != null) {
            String answer = selectedRadioButton.getText().toString();
            answers[currentQuestionIndex] = answer;
        }
    }


    private void showNextQuestion() {
        if (currentQuestionIndex < questions.length - 1) {
            currentQuestionIndex++;
            showQuestion();
        } else {
            // Survey completed
            showSurveyResults();
        }
    }

    private void showSurveyResults() {
        StringBuilder resultBuilder = new StringBuilder();

        for (int i = 0; i < questions.length; i++) {
            String question = questions[i];
            String answer = answers[i];

            resultBuilder.append("질문: ").append(question).append("\n");
            resultBuilder.append("선택 항목: ").append(answer).append("\n");
            resultBuilder.append("진단 결과: ").append(getTextResult(i)).append("\n\n");
        }

        String surveyResults = resultBuilder.toString();

        // 진단 결과 표시
        AlertDialog.Builder builder = new AlertDialog.Builder(SurveyActivity.this);
        builder.setTitle("설문 결과")
                .setMessage(surveyResults)
                .setPositiveButton("완료", null)
                .show();

    }

    private String getTextResult(int questionIndex) {
        String answer = answers[questionIndex];

        if (questionIndex == 0) {
            if (answer.equals("네")) {
                return "";
            } else if (answer.equals("아니요")) {
                return "칫솔모가 잘 닿지 않는 부분은 충치나 치주병이 생기기 쉽습니다. 따라서 치실과 치간솔을 이용해 치석을 제거해주어야합니다.";
            }
        } else if (questionIndex == 1) {
            if (answer.equals("네")) {
                return "치과를 방문을 통한 검진이 필요합니다.";
            } else if (answer.equals("아니요")) {
                return "이상 없음";
            }
        } else if (questionIndex == 2) {
            if (answer.equals("네")) {
                return "잇몸이 아픈 경우 치은염이나 치주염일 가능성이 있습니다. 정확한 검진이 필요합니다.";
            } else if (answer.equals("아니요")) {
                return "이상 없음";
            }
        } else if (questionIndex == 3) {
            if (answer.equals("네")) {
                return "구강염일 가능성이 있습니다. 구강염은 스트레스, 구강 위생을 관리하고 연고제를 사용한다면 증상 완화를 도울 수 있습니다.";
            } else if (answer.equals("아니요")) {
                return "이상 없음";
            }
        } else if (questionIndex == 4) {
            if (answer.equals("네")) {
                return "치아 시림의 원인은 치아 우식증(충치)과 치아의 마모로 인해 발생합니다.\n" +
                        "치아의 마모의 경우 좌우로 칫솔질을 하거나 산과 자주 접촉하는 경우 그리고 치아에 과도한 힘을 과하는 원인이 있습니다.";
            } else if (answer.equals("아니요")) {
                return "이상 없음";
            }
        }

        // 해당 결과값이 없는 경우
        return "error";
    }
}