package com.example.capstone;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.annotation.NonNullApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ResultDiagnosis extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    //ImageView load;
    TextView Day;
    ImageView image1;
    ImageView image2;
    ImageView image3;
    ImageView image4;
    static ResultAdapter adapter;

    Button findDayBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_diagnosis);

        getSupportActionBar().hide(); //앱바 숨기기

        mFirebaseAuth = FirebaseAuth.getInstance(); //초기화

        /*
        load=(ImageView)findViewById(R.id.loadimg);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        StorageReference pathReference = storageReference.child("photo");

        if (pathReference == null) {
            Toast.makeText(ResultDiagnosis.this, "저장소에 사진이 없습니다." ,Toast.LENGTH_SHORT).show();
        } else {
            StorageReference submitProfile = storageReference.child("image_store/asd12.jpeg");
            submitProfile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(ResultDiagnosis.this).load(uri).into(load);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {

                }
            });
        }
        */

        // 데이터 원본 준비
        ArrayList<ResultItem> data = new ArrayList<ResultItem>();
        /*
        data.add(new MyItem(R.drawable.sample_0, "Bella", "1"));
        data.add(new MyItem(R.drawable.sample_1, "Charlie", "2"));
        data.add(new MyItem(R.drawable.sample_2, "Daisy", "1.5"));
        data.add(new MyItem(R.drawable.sample_3, "Duke", "1"));
        data.add(new MyItem(R.drawable.sample_4, "Max", "2"));
        data.add(new MyItem(R.drawable.sample_5, "Happy", "4"));
        data.add(new MyItem(R.drawable.sample_6, "Luna", "3"));
        data.add(new MyItem(R.drawable.sample_7, "Bob", "2"));
         */

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        StorageReference pathReference = storageReference.child("photo");

        /*
        //2023년 5월 1일
        for(int m=5; m<=12; m++) {
            for (int d = 1; d <= 31; d++) {

         */
        String day = getTime();

        Day=(TextView)findViewById(R.id.day);
        Day.setText(day);

        image1=(ImageView)findViewById(R.id.image1);
        image2=(ImageView)findViewById(R.id.image2);
        image3=(ImageView)findViewById(R.id.image3);
        image4=(ImageView)findViewById(R.id.image4);

        if (pathReference == null) {
            Toast.makeText(ResultDiagnosis.this, "저장소에 사진이 없습니다." ,Toast.LENGTH_SHORT).show();
        } else {
            //StorageReference submitProfile;


            StorageReference submitProfile1 = storageReference.child("image_store" + day + "/photo_1.jpg");
            StorageReference submitProfile2 = storageReference.child("image_store" + day + "/photo_2.jpg");
            StorageReference submitProfile3 = storageReference.child("image_store" + day + "/photo_3.jpg");
            StorageReference submitProfile4 = storageReference.child("image_store" + day + "/photo_4.jpg");

            submitProfile1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(ResultDiagnosis.this).load(uri).into(image1);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(ResultDiagnosis.this, "해당 날짜 사진 없음",
                            Toast.LENGTH_SHORT).show();
                }
            });
            submitProfile2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(ResultDiagnosis.this).load(uri).into(image2);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(ResultDiagnosis.this, "해당 날짜 사진 없음",
                            Toast.LENGTH_SHORT).show();
                }
            });
            submitProfile3.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(ResultDiagnosis.this).load(uri).into(image3);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(ResultDiagnosis.this, "해당 날짜 사진 없음",
                            Toast.LENGTH_SHORT).show();
                }
            });
            submitProfile4.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(ResultDiagnosis.this).load(uri).into(image4);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(ResultDiagnosis.this, "해당 날짜 사진 없음",
                            Toast.LENGTH_SHORT).show();
                }
            });

        }

        EditText whenDiagnosis =  findViewById(R.id.findDay);

        findDayBtn = findViewById(R.id.btn_findDay);

        findDayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //진단 날짜 받아오기
                String diagnosisDay = whenDiagnosis.getText().toString();

                if(isDateFormat(diagnosisDay)) {
                    TextView Day = findViewById(R.id.day);
                    Day.setText(diagnosisDay);
                }
                else
                    Toast.makeText(ResultDiagnosis.this, "올바른 날짜 형식이 아닙니다",
                           Toast.LENGTH_SHORT).show();

                if (pathReference == null) {
                    Toast.makeText(ResultDiagnosis.this, "저장소에 사진이 없습니다." ,Toast.LENGTH_SHORT).show();
                }
                else {
                    //StorageReference submitProfile;


                    StorageReference submitProfile1 = storageReference.child("image_store" + diagnosisDay + "/photo_1.jpg");
                    StorageReference submitProfile2 = storageReference.child("image_store" + diagnosisDay + "/photo_2.jpg");
                    StorageReference submitProfile3 = storageReference.child("image_store" + diagnosisDay + "/photo_3.jpg");
                    StorageReference submitProfile4 = storageReference.child("image_store" + diagnosisDay + "/photo_4.jpg");

                    submitProfile1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(ResultDiagnosis.this).load(uri).into(image1);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(ResultDiagnosis.this, "해당 날짜 사진 없음",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                    submitProfile2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(ResultDiagnosis.this).load(uri).into(image2);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(ResultDiagnosis.this, "해당 날짜 사진 없음",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                    submitProfile3.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(ResultDiagnosis.this).load(uri).into(image3);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(ResultDiagnosis.this, "해당 날짜 사진 없음",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                    submitProfile4.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(ResultDiagnosis.this).load(uri).into(image4);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(ResultDiagnosis.this, "해당 날짜 사진 없음",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });





        /*
        //어댑터 생성
        adapter = new ResultAdapter(this, R.layout.result, data);

        //어댑터 연결
        ListView listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View vClicked,
                                    int position, long id) {
                //   String name = (String) ((TextView)vClicked.findViewById(R.id.textItem1)).getText();
                String name = ((ResultItem)adapter.getItem(position)).nDay;
                Toast.makeText(ResultDiagnosis.this, name + " selected",
                        Toast.LENGTH_SHORT).show();
            }
        });
         */

    }
    private String getTime() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String getTime = dateFormat.format(date);

        return getTime;
    }
    public static boolean isDateFormat(String str2cmp, String dateFormat) {
        try {
            //  검증할 날짜 포맷 설정
            SimpleDateFormat dateFormatParser = new SimpleDateFormat(dateFormat);
            //  parse()에 잘못된 값이 들어오면 Exception을 리턴하도록 setLenient(false) 설정
            dateFormatParser.setLenient(false);
            // 대상 인자 검증
            dateFormatParser.parse(str2cmp);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isDateFormat(String str2cmp) {
        return isDateFormat(str2cmp, "yyyy-MM-dd");
    }
}
