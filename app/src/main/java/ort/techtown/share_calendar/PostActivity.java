package ort.techtown.share_calendar;

import static java.lang.Boolean.getBoolean;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import ort.techtown.share_calendar.Adapter.CalendarAdapter;
import ort.techtown.share_calendar.Adapter.TodoListAdapter;
import ort.techtown.share_calendar.Data.CalendarUtil;
import ort.techtown.share_calendar.Data.Info;

public class PostActivity extends AppCompatActivity {

    // drawerLayout
    private DrawerLayout drawerLayout;
    private Button btn_logout, btn_calendar, btn_search, btn_make, btn_group, btn_close;
    private View drawerView;
    private ImageView menu_open;
    private TextView tv_title;
    // 파이어베이스
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();
    private DatabaseReference mReference = database.getReference();
    private DatabaseReference reference = database.getReference();
    // 달력
    TextView tv_monthyear;
    RecyclerView recyclerView, todoListRecyclerView;
    // 그룹 정보
    private String groupname, uid, tmp_name, tmp_uid;
    private TextView group_name, group_introduce;
    private ImageView group_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        groupname = getIntent().getStringExtra("groupname");
        uid = getIntent().getStringExtra("uid");

        // 달력
        tv_monthyear = findViewById(R.id.tv_month_year);
        ImageButton btn_pre = findViewById(R.id.btn_frontmonth);
        ImageButton btn_next = findViewById(R.id.btn_nextmonth);
        recyclerView = findViewById(R.id.recyclerview);
        todoListRecyclerView = findViewById(R.id.todoListRecyclerView);
        CalendarUtil.today = LocalDate.now();
        CalendarUtil.selectedDate = LocalDate.now();
        setTodoList(uid);
        // 화면 설정
        setMonthview();
        btn_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 한달 전전
                CalendarUtil.selectedDate = CalendarUtil.selectedDate.minusMonths(1);
                setMonthview();
                setTodoList(uid);
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 한달 뒤
                CalendarUtil.selectedDate = CalendarUtil.selectedDate.plusMonths(1);
                setMonthview();
                setTodoList(uid);
            }
        });

        // 툴바
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_title.setText(groupname);

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
                Intent intent = new Intent(PostActivity.this, MainActivity.class);
                intent.putExtra("uid",uid);
                startActivity(intent);
            }
        });
        // 그룹 생성 버튼
        btn_make = (Button)findViewById(R.id.btn_make);
        btn_make.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostActivity.this, MakeActivity.class);
                intent.putExtra("uid",uid);
                startActivity(intent);
            }
        });
        // 그룹 검색 버튼
        btn_search = (Button)findViewById(R.id.btn_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostActivity.this, SearchActivity.class);
                intent.putExtra("uid",uid);
                startActivity(intent);
            }
        });
        // 마이그룹 버튼
        btn_group = (Button)findViewById(R.id.btn_group);
        btn_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostActivity.this, GroupActivity.class);
                intent.putExtra("uid",uid);
                startActivity(intent);
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

        // 그룹 정보 받아오기
        group_name = (TextView)findViewById(R.id.group_name);
        group_introduce = (TextView)findViewById(R.id.group_introduce);
        group_image = (ImageView)findViewById(R.id.group_image);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("Group").child(groupname).child("groupname").getValue().toString();
                String introduce = snapshot.child("Group").child(groupname).child("introduce").getValue().toString();
                group_name.setText(name);
                group_introduce.setText(introduce);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
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
        adapter.setOnItemClickListener(new CalendarAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                setTodoList(uid);
            }
        });
    }

    private void setTodoList(String uid) {
        // 해당 일정 가져오기
        ArrayList<Info> infolist = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        TodoListAdapter adapter = new TodoListAdapter(infolist);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext());
        // 어뎁터 적용
        todoListRecyclerView.setLayoutManager(manager);
        todoListRecyclerView.setAdapter(adapter);
        // UidList 정보 가져오기
        mReference = database.getReference("/Group/"+groupname+"/Uid");
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot uid_snapshot) {
                for(DataSnapshot Uid_Snapshot : uid_snapshot.getChildren()) {
                    tmp_uid = Uid_Snapshot.getValue().toString();
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot name_snapshot) {
                            tmp_name = name_snapshot.child("User").child(tmp_uid).child("Name").getValue().toString();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    try{
                        reference = database.getReference("User").child(tmp_uid).child("Calender").child(CalendarUtil.selectedDate.toString());
                        Query myTopPostsQuery = reference.orderByChild("start");
                        myTopPostsQuery.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    infolist.add(snapshot.getValue(Info.class));
                                    TodoListAdapter adapter = new TodoListAdapter(infolist);
                                    RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext());
                                    // 어뎁터 적용
                                    todoListRecyclerView.setLayoutManager(manager);
                                    todoListRecyclerView.setAdapter(adapter);
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    // 날짜 타입 설정
    private String monthYearFromDate(LocalDate localDate){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM월 yyyy");
        return localDate.format(formatter);
    }

    private ArrayList<LocalDate> daysInMonthArray(LocalDate localDate){
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
}