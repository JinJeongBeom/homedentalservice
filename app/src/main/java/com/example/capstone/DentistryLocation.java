package com.example.capstone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class DentistryLocation extends AppCompatActivity implements OnMapReadyCallback {
    //지도
    private double lat1, lng1;
    GoogleMap mGoogleMap = null;
    private FusedLocationProviderClient mFusedLocationClient;

    final private int REQUEST_PERMISSIONS_FOR_LAST_KNOWN_LOCATION = 100;
    private Location mLastLocation;
    private LocationCallback mLocationCallback;
    double latitude = 0.0;
    double longitude = 0.0;


    //final private int REQUEST_PERMISSIONS_FOR_LOCATION_UPDATES = 101;

    //병원-------------------------------------------------------------

    TextView text;
    XmlPullParser xpp;

    String key = "19a4579809e9441bb89ae31dab3ee32a";
    String data;


    //지도에 병원 위치 표시------------------------------------------------
    /*
    ArrayList<String> dentName = new ArrayList<String>();
    ArrayList<String> latMap = new ArrayList<String>();
    ArrayList<String> longMap = new ArrayList<String>();

     */
    String[] NameLongLatArray;


    //double DentLongitude = 0;
    //double DentLatitude = 0;
    /*
    String DentName;
    String DentLatitude;
    String DentLongitude;

     */
    String NameLongLat;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dentistry_location);

        getSupportActionBar().hide(); //앱바 숨기기

        //----------------------------------------------

        text = (TextView) findViewById(R.id.result);

        Thread api;

        api =new Thread(new Runnable() {
            @Override
            public void run() {
                data = getXmlData();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        text.setText(data);
                    }
                });

            }
        });
        api.start();
        /*
        try {
            api.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        //-------------------------------------------------------

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(DentistryLocation.this);
        //getLastLocation();

        new Thread(new Runnable() {
            @Override
            public void run() {
                //data = getXmlData();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //text.setText(data);
                        getLastLocation();
                        //onMapReady(mGoogleMap);
                    }
                });
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                //data = getXmlData();
                NameLongLat = getNameLongLat();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //text.setText(data);
                        locationDent();
                        //onMapReady(mGoogleMap);
                    }
                });
            }
        }).start();






        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(DentistryLocation.this);
        /*
        new Thread(new Runnable() {
            @Override
            public void run() {
                //data = getXmlData();

                dentName.add(getDentName());
                longMap.add(getLongitude());
                latMap.add(getLatitude());

                NameLongLet.add(getNameLongLat());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //text.setText(data);
                        locationDent();
                        //onMapReady(mGoogleMap);
                    }
                });
            }
        }).start();*/
        /*
        new Thread(new Runnable() {
            @Override
            public void run() {
                //data = getXmlData();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //text.setText(data);
                        DentLatitude = getLatitude();
                        //onMapReady(mGoogleMap);
                    }
                });
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                //data = getXmlData();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //text.setText(data);
                        DentLongitude = getLongitude();
                        //onMapReady(mGoogleMap);
                    }
                });
            }
        }).start();


         */


    }

    /*
        private void getLastLocation() {
            // 1. 위치 접근에 필요한 권한 검사 및 요청
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        MainActivity.this,            // MainActivity 액티비티의 객체 인스턴스를 나타냄
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},        // 요청할 권한 목록을 설정한 String 배열
                        REQUEST_PERMISSIONS_FOR_LAST_KNOWN_LOCATION    // 사용자 정의 int 상수. 권한 요청 결과를 받을 때
                );
                return;
            }

            // 2. Task<Location> 객체 반환
            Task task = mFusedLocationClient.getLastLocation();

            //startLocationUpdates();

            // 3. Task가 성공적으로 완료 후 호출되는 OnSuccessListener 등록
            task.addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    // 4. 마지막으로 알려진 위치(location 객체)를 얻음.
                    if (location != null) {
                        mLastLocation = location;
                        latitude = mLastLocation.getLatitude();
                        longitude = mLastLocation.getLongitude();
                    } else
                        Toast.makeText(getApplicationContext(),
                                "No location detected",
                                Toast.LENGTH_SHORT)
                                .show();
                }
            });
        }
    */

    /*
    private void startLocationUpdates() {
        // 1. 위치 요청 (Location Request) 설정
        LocationRequest locRequest = LocationRequest.create();
        locRequest.setInterval(10000);
        locRequest.setFastestInterval(5000);
        locRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // 2. 위치 업데이트 콜백 정의
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                mLastLocation = locationResult.getLastLocation();
                latitude = mLastLocation.getLatitude();
                longitude = mLastLocation.getLongitude();
            }
        };

        // 3. 위치 접근에 필요한 권한 검사
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    MainActivity.this,            // MainActivity 액티비티의 객체 인스턴스를 나타냄
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},        // 요청할 권한 목록을 설정한 String 배열
                    REQUEST_PERMISSIONS_FOR_LOCATION_UPDATES    // 사용자 정의 int 상수. 권한 요청 결과를 받을 때
            );
            return;
        }

        // 4. 위치 업데이트 요청
        mFusedLocationClient.requestLocationUpdates(locRequest,
                mLocationCallback,
                null );

    }*/

    String getXmlData(){
        StringBuffer buffer=new StringBuffer();
        //double DentLongitude = 0;
        //double DentLatitude = 0;
        //String str= edit.getText().toString();//EditText에 작성된 Text얻어오기
        //String location = URLEncoder.encode(str);
        String query="%EC%A0%84%EB%A0%A5%EB%A1%9C";


        String queryUrlSynthesize="https://openapi.gg.go.kr/SynthesizeHospital?"+"&pageNo=1&numOfRows=100&ServiceKey="+key;
        String queryUrlSeongnam="https://openapi.gg.go.kr/DentistryPrivateHospital?"+"&pageNo=1&numOfRows=100&ServiceKey="+key;
        String queryUrlDenHos="https://openapi.gg.go.kr/DentistryHospital?addr="+"&pageNo=1&numOfRows=100&ServiceKey="+key;


        String queryUrlNormal="https://openapi.gg.go.kr/DentistryHospital?KEY=19a4579809e9441bb89ae31dab3ee32a&pIndex=1&pSize=100";
        int i = 1;

        try{
            //성남
            URL url= new URL(queryUrlNormal);//문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is= url.openStream(); //url위치로 입력스트림 연결

            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();//xml파싱을 위한
            XmlPullParser xpp= factory.newPullParser();
            xpp.setInput( new InputStreamReader(is, "UTF-8") ); //inputstream 으로부터 xml 입력받기

            String tag;

            xpp.next();
            int eventType= xpp.getEventType();


            //buffer.append("성남\n\n");

            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        buffer.append("파싱 시작...\n\n");
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName();//테그 이름 얻어오기

                        if(tag.equals("item")) ;// 첫번째 검색결과
                        else if(tag.equals("SIGUN_NM")){ //시군 명
                            buffer.append(i+". 병원명 : ");
                            i++;
                            xpp.next();
                            buffer.append(xpp.getText());//title 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append(" "); //띄어쓰기 추가
                            //buffer.append("\n"); //줄바꿈 문자 추가
                        }
                        else if(tag.equals("BIZPLC_NM")){ //병원 이름
                            //buffer.append("충전소타입 : ");
                            //dentName.add(xpp.getText());//병원 이름 ArrayList 추가
                            xpp.next();
                            buffer.append(xpp.getText());//category 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n");//줄바꿈 문자 추가
                        }
                        else if(tag.equals("BSN_STATE_NM")){
                            buffer.append("영업 상태 :");
                            xpp.next();
                            buffer.append(xpp.getText());//description 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n");//줄바꿈 문자 추가
                        }
                        else if(tag.equals("MEDSTAF_CNT")){
                            buffer.append("의료인수(명) :");
                            xpp.next();
                            buffer.append(xpp.getText());//telephone 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n");//줄바꿈 문자 추가
                        }
                        else if(tag.equals("TREAT_SBJECT_CONT")){
                            buffer.append("진료과목내용 :");
                            xpp.next();
                            buffer.append(xpp.getText());//address 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n");//줄바꿈 문자 추가
                        }
                        else if(tag.equals("REFINE_LOTNO_ADDR")){
                            buffer.append("지번 주소 :");
                            xpp.next();
                            buffer.append(xpp.getText());//mapy 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n"); //줄바꿈 문자 추가
                        }
                        else if(tag.equals("REFINE_ROADNM_ADDR")){
                            buffer.append("도로명 주소 :");
                            xpp.next();
                            buffer.append(xpp.getText());//mapx 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n");
                        }
                        else if(tag.equals("REFINE_WGS84_LOGT")){
                            //longMap.add(xpp.getText());//경도값 ArrayList에 추가
                            //DentLongitude = Double.parseDouble(xpp.getText());
                            buffer.append("경도 :");
                            xpp.next();
                            buffer.append(xpp.getText());//mapy 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append(" / "); //줄바꿈 문자 추가
                        }
                        else if(tag.equals("REFINE_WGS84_LAT")){
                            //latMap.add(xpp.getText());//위도값 ArrayList에 추가
                            //DentLatitude = Double.parseDouble(xpp.getText());
                            buffer.append("위도 :");
                            xpp.next();
                            buffer.append(xpp.getText());//mapy 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n\n"); //줄바꿈 문자 추가
                        }


                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName(); //테그 이름 얻어오기

                        if(tag.equals("item")) buffer.append("\n");// 첫번째 검색결과종료..줄바꿈
                        break;
                }

                eventType= xpp.next();
            }
            /*
            //종합

            url= new URL(queryUrlSynthesize);//문자열로 된 요청 url을 URL 객체로 생성.
            is= url.openStream(); //url위치로 입력스트림 연결

            //XmlPullParserFactory factoryYongin= XmlPullParserFactory.newInstance();//xml파싱을 위한
            //XmlPullParser xppYongin= factory.newPullParser();
            xpp.setInput( new InputStreamReader(is, "UTF-8") ); //inputstream 으로부터 xml 입력받기

            //String tag;

            xpp.next();
            eventType= xpp.getEventType();

            buffer.append("종합병원\n\n");

            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        buffer.append("파싱 시작...\n\n");
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName();//테그 이름 얻어오기

                        if(tag.equals("item")) ;// 첫번째 검색결과
                        else if(tag.equals("SIGUN_NM")){ //시군 명
                            buffer.append("주소 : ");
                            xpp.next();
                            buffer.append(xpp.getText());//title 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append(" "); //띄어쓰기 추가
                            //buffer.append("\n"); //줄바꿈 문자 추가
                        }
                        else if(tag.equals("BIZPLC_NM")){ //병원 이름
                            //buffer.append("충전소타입 : ");
                            //dentName.add(xpp.getText());//병원 이름 ArrayList 추가
                            xpp.next();
                            buffer.append(xpp.getText());//category 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n");//줄바꿈 문자 추가
                        }
                        else if(tag.equals("BSN_STATE_NM")){
                            buffer.append("영업 상태 :");
                            xpp.next();
                            buffer.append(xpp.getText());//description 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n");//줄바꿈 문자 추가
                        }

                        else if(tag.equals("MEDSTAF_CNT")){
                            buffer.append("의료인수(명) :");
                            xpp.next();
                            buffer.append(xpp.getText());//telephone 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n");//줄바꿈 문자 추가
                        }

                        else if(tag.equals("TREAT_SBJECT_CONT")){
                            buffer.append("진료과목내용 :");
                            xpp.next();
                            buffer.append(xpp.getText());//address 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n");//줄바꿈 문자 추가
                        }
                        else if(tag.equals("REFINE_LOTNO_ADDR")){
                            buffer.append("지번 주소 :");
                            xpp.next();
                            buffer.append(xpp.getText());//mapy 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n"); //줄바꿈 문자 추가
                        }
                        else if(tag.equals("REFINE_ROADNM_ADDR")){
                            buffer.append("도로명 주소 :");
                            xpp.next();
                            buffer.append(xpp.getText());//mapx 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n");
                        }
                        else if(tag.equals("REFINE_WGS84_LOGT")){
                            //longMap.add(xpp.getText());//경도값 ArrayList에 추가
                            buffer.append("경도 :");
                            xpp.next();
                            buffer.append(xpp.getText());//mapy 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append(" / "); //줄바꿈 문자 추가
                        }
                        else if(tag.equals("REFINE_WGS84_LAT")){
                            //latMap.add(xpp.getText());//위도값 ArrayList에 추가
                            buffer.append("위도 :");
                            xpp.next();
                            buffer.append(xpp.getText());//mapy 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n\n"); //줄바꿈 문자 추가
                        }


                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName(); //테그 이름 얻어오기

                        if(tag.equals("item")) buffer.append("\n");// 첫번째 검색결과종료..줄바꿈
                        break;
                }

                eventType= xpp.next();
            }

            //병원

            url= new URL(queryUrlDenHos);//문자열로 된 요청 url을 URL 객체로 생성.
            is= url.openStream(); //url위치로 입력스트림 연결

            //XmlPullParserFactory factoryYongin= XmlPullParserFactory.newInstance();//xml파싱을 위한
            //XmlPullParser xppYongin= factory.newPullParser();
            xpp.setInput( new InputStreamReader(is, "UTF-8") ); //inputstream 으로부터 xml 입력받기

            //String tag;

            xpp.next();
            eventType= xpp.getEventType();

            buffer.append("병원\n\n");

            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        buffer.append("파싱 시작...\n\n");
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName();//테그 이름 얻어오기

                        if(tag.equals("item")) ;// 첫번째 검색결과
                        else if(tag.equals("SIGUN_NM")){ //시군 명
                            buffer.append("주소 : ");
                            xpp.next();
                            buffer.append(xpp.getText());//title 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append(" "); //띄어쓰기 추가
                            //buffer.append("\n"); //줄바꿈 문자 추가
                        }
                        else if(tag.equals("BIZPLC_NM")){ //병원 이름
                            //buffer.append("충전소타입 : ");
                            //dentName.add(xpp.getText());//병원 이름 ArrayList 추가
                            xpp.next();
                            buffer.append(xpp.getText());//category 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n");//줄바꿈 문자 추가
                        }
                        else if(tag.equals("BSN_STATE_NM")){
                            buffer.append("영업 상태 :");
                            xpp.next();
                            buffer.append(xpp.getText());//description 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n");//줄바꿈 문자 추가
                        }

                        else if(tag.equals("MEDSTAF_CNT")){
                            buffer.append("의료인수(명) :");
                            xpp.next();
                            buffer.append(xpp.getText());//telephone 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n");//줄바꿈 문자 추가
                        }

                        else if(tag.equals("TREAT_SBJECT_CONT")){
                            buffer.append("진료과목내용 :");
                            xpp.next();
                            buffer.append(xpp.getText());//address 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n");//줄바꿈 문자 추가
                        }
                        else if(tag.equals("REFINE_LOTNO_ADDR")){
                            buffer.append("지번 주소 :");
                            xpp.next();
                            buffer.append(xpp.getText());//mapy 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n"); //줄바꿈 문자 추가
                        }
                        else if(tag.equals("REFINE_ROADNM_ADDR")){
                            buffer.append("도로명 주소 :");
                            xpp.next();
                            buffer.append(xpp.getText());//mapx 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n");
                        }
                        else if(tag.equals("REFINE_WGS84_LOGT")){
                            //longMap.add(xpp.getText());//경도값 ArrayList에 추가
                            buffer.append("경도 :");
                            xpp.next();
                            buffer.append(xpp.getText());//mapy 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append(" / "); //줄바꿈 문자 추가
                        }
                        else if(tag.equals("REFINE_WGS84_LAT")){
                            //latMap.add(xpp.getText());//위도값 ArrayList에 추가
                            buffer.append("위도 :");
                            xpp.next();
                            buffer.append(xpp.getText());//mapy 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n\n"); //줄바꿈 문자 추가
                        }


                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName(); //테그 이름 얻어오기

                        if(tag.equals("item")) buffer.append("\n");// 첫번째 검색결과종료..줄바꿈
                        break;
                }

                eventType= xpp.next();
            }

             */



        }
        catch (Exception e){
            e.printStackTrace();
        }

        buffer.append("파싱 끝\n");

        //locationDent();

        return buffer.toString();//StringBuffer 문자열 객체 반환

    }//getXmlData method....
    String getNameLongLat(){
        StringBuffer buffer=new StringBuffer();
        //double DentLongitude = 0;
        //double DentLatitude = 0;
        //String str= edit.getText().toString();//EditText에 작성된 Text얻어오기
        //String location = URLEncoder.encode(str);
        String query="%EC%A0%84%EB%A0%A5%EB%A1%9C";

        String queryUrlSynthesize="https://openapi.gg.go.kr/SynthesizeHospital?"+"&pageNo=1&numOfRows=100&ServiceKey="+key;
        String queryUrlSeongnam="https://openapi.gg.go.kr/DentistryPrivateHospital?"+"&pageNo=1&numOfRows=100&ServiceKey="+key;
        String queryUrlDenHos="https://openapi.gg.go.kr/DentistryHospital?addr="+"&pageNo=1&numOfRows=100&ServiceKey="+key;


        String queryUrlNormal="https://openapi.gg.go.kr/DentistryHospital?KEY=19a4579809e9441bb89ae31dab3ee32a&pIndex=1&pSize=100";

        try{
            //종합
            URL url= new URL(queryUrlNormal);//문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is= url.openStream(); //url위치로 입력스트림 연결

            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();//xml파싱을 위한
            XmlPullParser xpp= factory.newPullParser();
            xpp.setInput( new InputStreamReader(is, "UTF-8") ); //inputstream 으로부터 xml 입력받기

            String tag;

            xpp.next();
            int eventType= xpp.getEventType();


            //buffer.append("용인\n\n");

            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        buffer.append("파싱 시작...\n\n");
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName();//테그 이름 얻어오기

                        if(tag.equals("item")) ;// 첫번째 검색결과

                        else if(tag.equals("BIZPLC_NM")){ //병원 이름
                            //buffer.append("충전소타입 : ");
                            //dentName.add(xpp.getText());//병원 이름 ArrayList 추가
                            xpp.next();
                            buffer.append(xpp.getText());//category 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("%");//줄바꿈 문자 추가
                        }
                        else if(tag.equals("REFINE_WGS84_LOGT")){
                            //longMap.add(xpp.getText());//경도값 ArrayList에 추가
                            //DentLongitude = Double.parseDouble(xpp.getText());
                            //buffer.append("경도 :");
                            xpp.next();
                            buffer.append(xpp.getText());//mapy 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("%"); //줄바꿈 문자 추가
                        }
                        else if(tag.equals("REFINE_WGS84_LAT")){
                            //latMap.add(xpp.getText());//위도값 ArrayList에 추가
                            //DentLatitude = Double.parseDouble(xpp.getText());
                            //buffer.append("위도 :");
                            xpp.next();
                            buffer.append(xpp.getText());//mapy 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("%"); //줄바꿈 문자 추가
                        }

                        else if(tag.equals("BSN_STATE_NM")){
                            buffer.append("영업 상태 :");
                            xpp.next();
                            buffer.append(xpp.getText());//description 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n");//줄바꿈 문자 추가
                        }
                        else if(tag.equals("MEDSTAF_CNT")){
                            buffer.append("의료인수(명) :");
                            xpp.next();
                            buffer.append(xpp.getText());//telephone 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n");//줄바꿈 문자 추가
                        }
                        else if(tag.equals("TREAT_SBJECT_CONT")){
                            buffer.append("진료과목내용 :");
                            xpp.next();
                            buffer.append(xpp.getText());//address 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n");//줄바꿈 문자 추가
                        }
                        else if(tag.equals("REFINE_LOTNO_ADDR")){
                            buffer.append("지번 주소 :");
                            xpp.next();
                            buffer.append(xpp.getText());//mapy 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n"); //줄바꿈 문자 추가
                        }


                        else if(tag.equals("REFINE_ROADNM_ADDR")){
                            buffer.append("도로명 주소 :");
                            xpp.next();
                            buffer.append(xpp.getText());//mapx 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n");
                            buffer.append("%"); //줄바꿈 문자 추가
                        }



                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName(); //테그 이름 얻어오기

                        if(tag.equals("item")) buffer.append("\n");// 첫번째 검색결과종료..줄바꿈
                        break;
                }

                eventType= xpp.next();
            }
            /*
            //성남

            url= new URL(queryUrlSynthesize);//문자열로 된 요청 url을 URL 객체로 생성.
            is= url.openStream(); //url위치로 입력스트림 연결

            //XmlPullParserFactory factoryYongin= XmlPullParserFactory.newInstance();//xml파싱을 위한
            //XmlPullParser xppYongin= factory.newPullParser();
            xpp.setInput( new InputStreamReader(is, "UTF-8") ); //inputstream 으로부터 xml 입력받기

            //String tag;

            xpp.next();
            eventType= xpp.getEventType();

            //buffer.append("성남\n\n");

            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        buffer.append("파싱 시작...\n\n");
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName();//테그 이름 얻어오기

                        if(tag.equals("item")) ;// 첫번째 검색결과
                        else if(tag.equals("BIZPLC_NM")){ //병원 이름
                            //buffer.append("충전소타입 : ");
                            //dentName.add(xpp.getText());//병원 이름 ArrayList 추가
                            xpp.next();
                            buffer.append(xpp.getText());//category 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("/");//줄바꿈 문자 추가
                        }
                        else if(tag.equals("REFINE_WGS84_LOGT")){
                            //longMap.add(xpp.getText());//경도값 ArrayList에 추가
                            //buffer.append("경도 :");
                            xpp.next();
                            buffer.append(xpp.getText());//mapy 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("/"); //줄바꿈 문자 추가
                        }
                        else if(tag.equals("REFINE_WGS84_LAT")){
                            //latMap.add(xpp.getText());//위도값 ArrayList에 추가
                            //buffer.append("위도 :");
                            xpp.next();
                            buffer.append(xpp.getText());//mapy 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("/"); //줄바꿈 문자 추가
                        }


                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName(); //테그 이름 얻어오기

                        if(tag.equals("item")) buffer.append("\n");// 첫번째 검색결과종료..줄바꿈
                        break;
                }

                eventType= xpp.next();
            }

            //병원

            url= new URL(queryUrlDenHos);//문자열로 된 요청 url을 URL 객체로 생성.
            is= url.openStream(); //url위치로 입력스트림 연결

            //XmlPullParserFactory factoryYongin= XmlPullParserFactory.newInstance();//xml파싱을 위한
            //XmlPullParser xppYongin= factory.newPullParser();
            xpp.setInput( new InputStreamReader(is, "UTF-8") ); //inputstream 으로부터 xml 입력받기

            //String tag;

            xpp.next();
            eventType= xpp.getEventType();

            //buffer.append("성남\n\n");

            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        buffer.append("파싱 시작...\n\n");
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName();//테그 이름 얻어오기

                        if(tag.equals("item")) ;// 첫번째 검색결과
                        else if(tag.equals("BIZPLC_NM")){ //병원 이름
                            //buffer.append("충전소타입 : ");
                            //dentName.add(xpp.getText());//병원 이름 ArrayList 추가
                            xpp.next();
                            buffer.append(xpp.getText());//category 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("/");//줄바꿈 문자 추가
                        }
                        else if(tag.equals("REFINE_WGS84_LOGT")){
                            //longMap.add(xpp.getText());//경도값 ArrayList에 추가
                            //buffer.append("경도 :");
                            xpp.next();
                            buffer.append(xpp.getText());//mapy 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("/"); //줄바꿈 문자 추가
                        }
                        else if(tag.equals("REFINE_WGS84_LAT")){
                            //latMap.add(xpp.getText());//위도값 ArrayList에 추가
                            //buffer.append("위도 :");
                            xpp.next();
                            buffer.append(xpp.getText());//mapy 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("/"); //줄바꿈 문자 추가
                        }


                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName(); //테그 이름 얻어오기

                        if(tag.equals("item")) buffer.append("\n");// 첫번째 검색결과종료..줄바꿈
                        break;
                }

                eventType= xpp.next();
            }


             */
        }
        catch (Exception e){
            e.printStackTrace();
        }

        //buffer.append("파싱 끝\n");

        //locationDent();

        return buffer.toString();//StringBuffer 문자열 객체 반환

    }//getXmlData method....
    /*
    String getDentName(){
        StringBuffer buffer=new StringBuffer();
        //double DentLongitude = 0;
        //double DentLatitude = 0;
        //String str= edit.getText().toString();//EditText에 작성된 Text얻어오기
        //String location = URLEncoder.encode(str);
        String query="%EC%A0%84%EB%A0%A5%EB%A1%9C";

        //String queryUrlYongin="https://openapi.gg.go.kr/DentistryHospital?addr="+str+"&pageNo=1&numOfRows=100&ServiceKey="+key;
        //String queryUrlSeongnam="https://openapi.gg.go.kr/DentistryPrivateHospital?addr="+str+"&pageNo=1&numOfRows=100&ServiceKey="+key;
        String queryUrlYongin="https://openapi.gg.go.kr/DentistryHospital?"+"&pageNo=1&numOfRows=100&ServiceKey="+key;
        String queryUrlSeongnam="https://openapi.gg.go.kr/DentistryPrivateHospital?"+"&pageNo=1&numOfRows=100&ServiceKey="+key;
        try{
            //용인
            URL url= new URL(queryUrlYongin);//문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is= url.openStream(); //url위치로 입력스트림 연결

            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();//xml파싱을 위한
            XmlPullParser xpp= factory.newPullParser();
            xpp.setInput( new InputStreamReader(is, "UTF-8") ); //inputstream 으로부터 xml 입력받기

            String tag;

            xpp.next();
            int eventType= xpp.getEventType();


            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        buffer.append("파싱 시작...\n\n");
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName();//테그 이름 얻어오기

                        if(tag.equals("item")) ;// 첫번째 검색결과
                        else if(tag.equals("BIZPLC_NM")){ //병원 이름
                            //buffer.append("충전소타입 : ");
                            //dentName.add(xpp.getText());//병원 이름 ArrayList 추가
                            xpp.next();
                            buffer.append(xpp.getText());//category 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append(" ");//줄바꿈 문자 추가
                        }

                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName(); //테그 이름 얻어오기

                        if(tag.equals("item")) buffer.append(" ");// 첫번째 검색결과종료..줄바꿈
                        break;
                }

                eventType= xpp.next();
            }

            //성남

            url= new URL(queryUrlSeongnam);//문자열로 된 요청 url을 URL 객체로 생성.
            is= url.openStream(); //url위치로 입력스트림 연결

            //XmlPullParserFactory factoryYongin= XmlPullParserFactory.newInstance();//xml파싱을 위한
            //XmlPullParser xppYongin= factory.newPullParser();
            xpp.setInput( new InputStreamReader(is, "UTF-8") ); //inputstream 으로부터 xml 입력받기

            //String tag;

            xpp.next();
            eventType= xpp.getEventType();

            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        buffer.append("파싱 시작...\n\n");
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName();//테그 이름 얻어오기

                        if(tag.equals("item")) ;// 첫번째 검색결과
                        else if(tag.equals("BIZPLC_NM")){ //병원 이름
                            //buffer.append("충전소타입 : ");
                            //dentName.add(xpp.getText());//병원 이름 ArrayList 추가
                            xpp.next();
                            buffer.append(xpp.getText());//category 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append(" ");//줄바꿈 문자 추가
                        }

                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName(); //테그 이름 얻어오기

                        if(tag.equals("item")) buffer.append("\n");// 첫번째 검색결과종료..줄바꿈
                        break;
                }

                eventType= xpp.next();
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }

        //buffer.append("파싱 끝\n");

        //locationDent();

        return buffer.toString();//StringBuffer 문자열 객체 반환

    }//getXmlData method....
    String getLatitude(){
        StringBuffer buffer=new StringBuffer();
        //double DentLongitude = 0;
        //double DentLatitude = 0;
        //String str= edit.getText().toString();//EditText에 작성된 Text얻어오기
        //String location = URLEncoder.encode(str);
        String query="%EC%A0%84%EB%A0%A5%EB%A1%9C";

        //String queryUrlYongin="https://openapi.gg.go.kr/DentistryHospital?addr="+str+"&pageNo=1&numOfRows=100&ServiceKey="+key;
        //String queryUrlSeongnam="https://openapi.gg.go.kr/DentistryPrivateHospital?addr="+str+"&pageNo=1&numOfRows=100&ServiceKey="+key;
        String queryUrlYongin="https://openapi.gg.go.kr/DentistryHospital?"+"&pageNo=1&numOfRows=100&ServiceKey="+key;
        String queryUrlSeongnam="https://openapi.gg.go.kr/DentistryPrivateHospital?"+"&pageNo=1&numOfRows=100&ServiceKey="+key;
        try{
            //용인
            URL url= new URL(queryUrlYongin);//문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is= url.openStream(); //url위치로 입력스트림 연결

            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();//xml파싱을 위한
            XmlPullParser xpp= factory.newPullParser();
            xpp.setInput( new InputStreamReader(is, "UTF-8") ); //inputstream 으로부터 xml 입력받기

            String tag;

            xpp.next();
            int eventType= xpp.getEventType();


            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        buffer.append("파싱 시작...\n\n");
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName();//테그 이름 얻어오기

                        if(tag.equals("item")) ;// 첫번째 검색결과

                        else if(tag.equals("REFINE_WGS84_LAT")){
                            //latMap.add(xpp.getText());//위도값 ArrayList에 추가
                            //DentLatitude = Double.parseDouble(xpp.getText());
                            //buffer.append("위도 :");
                            xpp.next();
                            buffer.append(xpp.getText());//mapy 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append(" "); //줄바꿈 문자 추가
                        }

                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName(); //테그 이름 얻어오기

                        if(tag.equals("item")) buffer.append("\n");// 첫번째 검색결과종료..줄바꿈
                        break;
                }

                eventType= xpp.next();
            }

            //성남

            url= new URL(queryUrlSeongnam);//문자열로 된 요청 url을 URL 객체로 생성.
            is= url.openStream(); //url위치로 입력스트림 연결

            //XmlPullParserFactory factoryYongin= XmlPullParserFactory.newInstance();//xml파싱을 위한
            //XmlPullParser xppYongin= factory.newPullParser();
            xpp.setInput( new InputStreamReader(is, "UTF-8") ); //inputstream 으로부터 xml 입력받기

            //String tag;

            xpp.next();
            eventType= xpp.getEventType();


            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        buffer.append("파싱 시작...\n\n");
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName();//테그 이름 얻어오기

                        if(tag.equals("item")) ;// 첫번째 검색결과

                        else if(tag.equals("REFINE_WGS84_LAT")){
                            //latMap.add(xpp.getText());//위도값 ArrayList에 추가
                            //buffer.append("위도 :");
                            xpp.next();
                            buffer.append(xpp.getText());//mapy 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append(" "); //줄바꿈 문자 추가
                        }


                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName(); //테그 이름 얻어오기

                        if(tag.equals("item")) buffer.append("\n");// 첫번째 검색결과종료..줄바꿈
                        break;
                }

                eventType= xpp.next();
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }


        //locationDent();

        return buffer.toString();//StringBuffer 문자열 객체 반환

    }//getXmlData method....
    String getLongitude(){
        StringBuffer buffer=new StringBuffer();
        //double DentLongitude = 0;
        //double DentLatitude = 0;
        //String str= edit.getText().toString();//EditText에 작성된 Text얻어오기
        //String location = URLEncoder.encode(str);
        String query="%EC%A0%84%EB%A0%A5%EB%A1%9C";

        //String queryUrlYongin="https://openapi.gg.go.kr/DentistryHospital?addr="+str+"&pageNo=1&numOfRows=100&ServiceKey="+key;
        //String queryUrlSeongnam="https://openapi.gg.go.kr/DentistryPrivateHospital?addr="+str+"&pageNo=1&numOfRows=100&ServiceKey="+key;
        String queryUrlYongin="https://openapi.gg.go.kr/DentistryHospital?"+"&pageNo=1&numOfRows=100&ServiceKey="+key;
        String queryUrlSeongnam="https://openapi.gg.go.kr/DentistryPrivateHospital?"+"&pageNo=1&numOfRows=100&ServiceKey="+key;
        try{
            //용인
            URL url= new URL(queryUrlYongin);//문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is= url.openStream(); //url위치로 입력스트림 연결

            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();//xml파싱을 위한
            XmlPullParser xpp= factory.newPullParser();
            xpp.setInput( new InputStreamReader(is, "UTF-8") ); //inputstream 으로부터 xml 입력받기

            String tag;

            xpp.next();
            int eventType= xpp.getEventType();


            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        buffer.append("파싱 시작...\n\n");
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName();//테그 이름 얻어오기

                        if(tag.equals("item")) ;// 첫번째 검색결과

                        else if(tag.equals("REFINE_WGS84_LOGT")){
                            //longMap.add(xpp.getText());//경도값 ArrayList에 추가
                            //DentLongitude = Double.parseDouble(xpp.getText());
                            //buffer.append("경도 :");
                            xpp.next();
                            buffer.append(xpp.getText());//mapy 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append(" "); //줄바꿈 문자 추가
                        }

                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName(); //테그 이름 얻어오기

                        if(tag.equals("item")) buffer.append("\n");// 첫번째 검색결과종료..줄바꿈
                        break;
                }

                eventType= xpp.next();
            }

            //성남

            url= new URL(queryUrlSeongnam);//문자열로 된 요청 url을 URL 객체로 생성.
            is= url.openStream(); //url위치로 입력스트림 연결

            //XmlPullParserFactory factoryYongin= XmlPullParserFactory.newInstance();//xml파싱을 위한
            //XmlPullParser xppYongin= factory.newPullParser();
            xpp.setInput( new InputStreamReader(is, "UTF-8") ); //inputstream 으로부터 xml 입력받기

            //String tag;

            xpp.next();
            eventType= xpp.getEventType();


            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        buffer.append("파싱 시작...\n\n");
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName();//테그 이름 얻어오기

                        if(tag.equals("item")) ;// 첫번째 검색결과

                        else if(tag.equals("REFINE_WGS84_LOGT")){
                            //longMap.add(xpp.getText());//경도값 ArrayList에 추가
                            //buffer.append("경도 :");
                            xpp.next();
                            buffer.append(xpp.getText());//mapy 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append(" "); //줄바꿈 문자 추가
                        }



                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName(); //테그 이름 얻어오기

                        if(tag.equals("item")) buffer.append("\n");// 첫번째 검색결과종료..줄바꿈
                        break;
                }

                eventType= xpp.next();
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }

        //buffer.append("파싱 끝\n");

        //locationDent();

        return buffer.toString();//StringBuffer 문자열 객체 반환

    }//getXmlData method....
     */

    private void getLastLocation() {
        // 1. 위치 접근에 필요한 권한 검사 및 요청
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    DentistryLocation.this,            // MainActivity 액티비티의 객체 인스턴스를 나타냄
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},        // 요청할 권한 목록을 설정한 String 배열
                    REQUEST_PERMISSIONS_FOR_LAST_KNOWN_LOCATION    // 사용자 정의 int 상수. 권한 요청 결과를 받을 때
            );
            return;
        }

        // 2. Task<Location> 객체 반환
        Task task = mFusedLocationClient.getLastLocation();

        // 3. Task가 성공적으로 완료 후 호출되는 OnSuccessListener 등록
        task.addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // 4. 마지막으로 알려진 위치(location 객체)를 얻음.
                if (location != null) {
                    mLastLocation = location;
                    updateUI();
                } else
                    Toast.makeText(getApplicationContext(),
                            "No location detected",
                            Toast.LENGTH_SHORT)
                            .show();
            }
        });
    }

    private void updateUI() {
        //double latitude = 0.0;
        //double longitude = 0.0;
        //float precision = 0.0f;

        /*
        TextView latitudeTextView = (TextView) findViewById(R.id.lat);
        TextView longitudeTextView = (TextView) findViewById(R.id.longi);
        latitudeTextView.setText("Latitude: " + latitude);
        longitudeTextView.setText("Longitude: " + longitude);
         */

        //TextView precisionTextView = (TextView) findViewById(R.id.precision_text);

        if (mLastLocation != null) {
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
            //precision = mLastLocation.getAccuracy();
        }
        /*
        latitudeTextView.setText("Latitude: " + latitude);
        longitudeTextView.setText("Longitude: " + longitude);
         */
        //precisionTextView.setText("Precision: " + precision);
        locationNow();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;




        /*
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location loc_Current = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        latitude = loc_Current.getLatitude(); //경도
        longitude = loc_Current.getLongitude(); //위도;
        */


        /*
        double latHansung = 37.5882827;
        double longHansung = 127.006390;
        LatLng now = new LatLng(latHansung, longHansung);
         */

        /*
        double latHome = 37.6653751;
        double longHome = 127.1310008;
        LatLng now = new LatLng(latHome, longHome);
        */
        /*
        LatLng now = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions().position(now).title("현재위치").snippet(latitude+" / "+longitude));
         */
        //병원 위치

        //double latDent = Double.parseDouble(latMap.get(1));
        //double longDent = Double.parseDouble(longMap.get(1));
        //LatLng dent = new LatLng(latDent, longDent);
        //googleMap.addMarker(new MarkerOptions().position(dent).title("현재위치"));

        //ArrayList<Double> longDent = new ArrayList<Double>();
        //ArrayList<Double> latDent = new ArrayList<Double>();
        /*
        for(int i=0; i<dentName.size(); i++)
        {
            //longDent.add(Double.parseDouble(longMap.get(i)));
            //latDent.add(Double.parseDouble(latMap.get(i)));
            double longDent = Double.parseDouble(longMap.get(i));
            double latDent = Double.parseDouble(latMap.get(i));
            LatLng dentistry = new LatLng(latDent,longDent);
            googleMap.addMarker(new MarkerOptions().position(dentistry).title(dentName.get(i)));
        }
         */

        //move the camera
        /*
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(13));
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(now));
         */
    }

    void locationNow(){
        LatLng now = new LatLng(latitude, longitude);
        mGoogleMap.addMarker(new MarkerOptions().position(now).title("현재위치").snippet(latitude+" / "+longitude).icon(BitmapDescriptorFactory.fromResource(R.drawable.nowlocation)));
        mGoogleMap.moveCamera(CameraUpdateFactory.zoomTo(13));
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(now));


    }

    void locationDent(){



        //ArrayList<Double> longDent = new ArrayList<Double>();
        //ArrayList<Double> latDent = new ArrayList<Double>();

            //longDent.add(Double.parseDouble(longMap.get(i)));
            //latDent.add(Double.parseDouble(latMap.get(i)));
            /*
            dentName.add(DentName);
            latMap.add(DentLatitude);
            longMap.add(DentLongitude);

             */


            //longDent = Double.parseDouble(longMap.get(i));
            //latDent = Double.parseDouble(latMap.get(i));


        double latDent;
        double longDent;
        //int i = 0;
        NameLongLatArray = NameLongLat.split("%");


        for(int i=0; i<400; i+=4)
        {
            //longDent.add(Double.parseDouble(longMap.get(i)));
            //latDent.add(Double.parseDouble(latMap.get(i)));

            longDent = Double.parseDouble(NameLongLatArray[i+2]);
            latDent = Double.parseDouble(NameLongLatArray[i+3]);

            //LatLng dentistry = new LatLng(latDent,longDent);
            mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(latDent,longDent)).title(NameLongLatArray[i]).snippet(NameLongLatArray[i+1]));
        }

        //Context mContext;

        mGoogleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                LinearLayout info = new LinearLayout(DentistryLocation.this);
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(DentistryLocation.this);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(DentistryLocation.this);
                snippet.setTextColor(Color.GRAY);
                snippet.setGravity(Gravity.LEFT);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });

        //LatLng dentistry = new LatLng(latDent,longDent);

        /*
        while(NameLongLatArray[i] != null)
        {
            longDent = Double.parseDouble(NameLongLatArray[i+1]);
            latDent = Double.parseDouble(NameLongLatArray[i+2]);

            //LatLng dentistry = new LatLng(latDent,longDent);
            mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(latDent,longDent)).title(NameLongLatArray[i]));

            i += 3;
        }

         */


        /*
        longDent = Double.parseDouble(NameLongLatArray[i+1]);
        latDent = Double.parseDouble(NameLongLatArray[i+2]);

        LatLng dentistry = new LatLng(latDent,longDent);
        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(latDent,longDent)).title(NameLongLatArray[i]));

         */


        /*
        TextView longi = (TextView) findViewById(R.id.longi);
        TextView lat = (TextView) findViewById(R.id.lat);

        longi.setText(NameLongLatArray[1]);
        lat.setText(NameLongLatArray[2]);

         */
    }

}