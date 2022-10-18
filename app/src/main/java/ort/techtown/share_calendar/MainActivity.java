package ort.techtown.share_calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.internal.Objects;
import com.google.firebase.auth.FirebaseAuth;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient googleApiClient;
    // drawerLayout
    private DrawerLayout drawerLayout;
    private Button btn_logout, btn_calendar, btn_search, btn_make, btn_group, btn_close;
    private View drawerView;
    private ImageView menu_open;
    // 달력
    TextView tv_monthyear;
    RecyclerView recyclerView;
    // 일정 추가 버튼
    ImageButton btn_goAddActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        // drawerLayout
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawerView = (View)findViewById(R.id.drawer);

        // drawerLayout 열기 버튼
        menu_open = (ImageView)findViewById(R.id.menu_open);
        menu_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(drawerView);
            }
        });

        // drawLayout 닫기 버튼
        btn_close = (Button)findViewById(R.id.btn_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawers();
            }
        });

        // 마이캘린더 버튼
        btn_calendar = (Button)findViewById(R.id.btn_calendar);
        btn_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // 그룹 생성 버튼
        btn_make = (Button)findViewById(R.id.btn_make);
        btn_make.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MakeActivity.class);
                startActivity(intent);
            }
        });

        // 그룹 검색 버튼
        btn_search = (Button)findViewById(R.id.btn_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // 마이그룹 버튼
        btn_group = (Button)findViewById(R.id.btn_group);
        btn_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // 로그아웃 버튼
        btn_logout = (Button)findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        });

        drawerLayout.setDrawerListener(listener);
        drawerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        // 달력
        tv_monthyear = findViewById(R.id.tv_month_year);
        ImageButton btn_pre = findViewById(R.id.btn_frontmonth);
        ImageButton btn_next = findViewById(R.id.btn_nextmonth);
        recyclerView = findViewById(R.id.recyclerview);

        CalendarUtil.selectedDate = LocalDate.now();
        CalendarUtil.today = LocalDate.now();

        // 화면 설정
        setMonthview();
        btn_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 한달 전전
                CalendarUtil.selectedDate = CalendarUtil.selectedDate.minusMonths(1);
                setMonthview();
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 한달 뒤
                CalendarUtil.selectedDate = CalendarUtil.selectedDate.plusMonths(1);
                setMonthview();
            }
        });

        btn_goAddActivity = (ImageButton) findViewById(R.id.btn_gotoAddActivity);
        btn_goAddActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this,AddActivity.class);
                // Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle();
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.anim_in,R.anim.anim_out);
            }
        });

    }

    // 달력
    private void setMonthview() {
        // 년월 텍스트뷰 셋팅
        tv_monthyear.setText(monthYearFromDate(CalendarUtil.selectedDate));
        // 해당 월 날짜 가져오기
        ArrayList<LocalDate> dayList = daysInMonthArray(CalendarUtil.selectedDate);
        // 어뎁터 데이터 적용
        CalendarAdapter adapter = new CalendarAdapter(dayList);
        // 레이아웃 설정 열 7개
        RecyclerView.LayoutManager manager = new GridLayoutManager(getApplicationContext(),7);
        // 레이아웃 적용
        recyclerView.setLayoutManager(manager);
        // 어뎁터 적용
        recyclerView.setAdapter(adapter);
    }

    // 날짜 타입 설정
    private String monthYearFromDate(LocalDate localDate){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM월 yyyy");
        return localDate.format(formatter);
    }

    private ArrayList<LocalDate>  daysInMonthArray(LocalDate localDate){
        ArrayList<LocalDate> daylist = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(localDate);
        // 해당 월 마지막 날짜 가져오기
        int lastday = yearMonth.lengthOfMonth();
        // 해당월의 첫번째 날짜 가져오기
        LocalDate firstday = CalendarUtil.selectedDate.withDayOfMonth(1);
        // 첫번째 날 요일 가져오기
        int dayOfweek = firstday.getDayOfWeek().getValue();
        // 날짜 생성
        for(int i = 1 ; i <= 42 ; i++){
            if(i <=dayOfweek){
                LocalDate premonthDate = CalendarUtil.selectedDate.minusMonths(1);
                int lastdayOfpremonth = premonthDate.lengthOfMonth();
                daylist.add(LocalDate.of(premonthDate.getYear(), premonthDate.getMonth(),
                       lastdayOfpremonth - dayOfweek + i ));
                Log.d("달력 남은 날짜", premonthDate.toString() );
            }
            else if(i> lastday + dayOfweek){
                LocalDate nextmonthDate = CalendarUtil.selectedDate.plusMonths(1);
                daylist.add(LocalDate.of(nextmonthDate.getYear(),
                        nextmonthDate.getMonth(),
                        i - lastday-dayOfweek));
                Log.d("달력 남은 날짜",nextmonthDate.toString() );
            }else{
                daylist.add(LocalDate.of(CalendarUtil.selectedDate.getYear(),
                        CalendarUtil.selectedDate.getMonth(),
                        i - dayOfweek));
            }
        }
        return daylist;
    }

    // drawerLayout 리스너
    DrawerLayout.DrawerListener listener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
        }
        @Override
        public void onDrawerOpened(@NonNull View drawerView) {
        }
        @Override
        public void onDrawerClosed(@NonNull View drawerView) {
        }
        @Override
        public void onDrawerStateChanged(int newState) {
        }
    };

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }
}