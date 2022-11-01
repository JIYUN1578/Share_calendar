package ort.techtown.share_calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ort.techtown.share_calendar.Adapter.GroupAdapter;
import ort.techtown.share_calendar.Class.BackKeyHandler;
import ort.techtown.share_calendar.Data.Group;
import ort.techtown.share_calendar.Data.GroupRecyclerView;

public class GroupActivity extends AppCompatActivity {

    private BackKeyHandler backKeyHandler = new BackKeyHandler(this);
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
    private String uid, name;
    // 그룹 정보 담는 리스트
    private ArrayList<String> arrayList;
    private ArrayList<GroupRecyclerView> groupList;
    // 리사이클러뷰
    private RecyclerView group_recyclerview;
    private RecyclerView.LayoutManager layoutManager;
    private GroupAdapter adapter;
    private GroupRecyclerView data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        name = intent.getStringExtra("name");

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_title.setText("마이그룹");

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
                Intent intent = new Intent(GroupActivity.this, MainActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("uid",uid);
                startActivity(intent);
                finish();
            }
        });
        // 그룹 생성 버튼
        btn_make = (Button)findViewById(R.id.btn_make);
        btn_make.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupActivity.this, MakeActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("uid",uid);
                startActivity(intent);
                finish();
            }
        });
        // 그룹 검색 버튼
        btn_search = (Button)findViewById(R.id.btn_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupActivity.this, SearchActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("uid",uid);
                startActivity(intent);
                finish();
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

        // 마이그룹 정보 가져오기
        group_recyclerview = (RecyclerView)findViewById(R.id.group_recyclerview);
        group_recyclerview.setHasFixedSize(true);

        layoutManager = new GridLayoutManager(getApplicationContext(),2);
        group_recyclerview.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();
        groupList = new ArrayList<>();
        adapter = new GroupAdapter(groupList, getApplicationContext());
        getGroupInfo();

        // 그룹 클릭 리스너
        adapter.setOnItemClickListener(new GroupAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                String groupname = groupList.get(position).getGroup_name();
                Intent intent = new Intent(GroupActivity.this, PostActivity.class);
                intent.putExtra("groupname",groupname);
                intent.putExtra("uid",uid);
                startActivity(intent);
                finish();
            }
        });
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

    public void getGroupInfo() {
        String path = "/User/" + uid + "/Group";
        databaseReference = database.getReference(path);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String groupname = dataSnapshot.getValue().toString();
                    arrayList.add(groupname);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                groupList.clear();
                for(int i=0; i<arrayList.size(); i++) {
                    data = new GroupRecyclerView();
                    String group_name = snapshot.child("Group").child(arrayList.get(i)).child("groupname").getValue().toString();
                    String group_introduce = snapshot.child("Group").child(arrayList.get(i)).child("introduce").getValue().toString();
                    data.setGroup_name(group_name);
                    data.setGroup_introduce(group_introduce);
                    groupList.add(data);
                }
                adapter.notifyDataSetChanged();
                group_recyclerview.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        backKeyHandler.onBackPressed();
    }
}