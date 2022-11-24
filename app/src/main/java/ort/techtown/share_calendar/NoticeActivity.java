package ort.techtown.share_calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import ort.techtown.share_calendar.Adapter.PostAdapter;
import ort.techtown.share_calendar.Adapter.VoteAdapter;
import ort.techtown.share_calendar.Data.Post;
import ort.techtown.share_calendar.Data.Vote;

public class NoticeActivity extends AppCompatActivity {

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
    private DatabaseReference dbReference = database.getReference();
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private StorageReference storageReference = firebaseStorage.getReference();
    // 그룹 정보
    private String groupname, uid, name, time;
    // 게시글, 투표글 관련
    private Boolean isVote;
    private Post post;
    private Vote vote;
    private ArrayList<Vote> voteArrayList;
    private ArrayList<Boolean> voteList;
    private ImageView iv_profile, iv_image;
    private TextView tv_name, tv_time, tv_noticetitle, tv_noticesummary;
    private RecyclerView vote_recyclerview;
    private RecyclerView.LayoutManager layoutManager;
    private VoteAdapter adapter;
    private Button btn_vote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        groupname = getIntent().getStringExtra("groupname");
        uid = getIntent().getStringExtra("uid");
        name = getIntent().getStringExtra("name");
        time = getIntent().getStringExtra("time");

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
                Intent intent = new Intent(NoticeActivity.this, MainActivity.class);
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
                Intent intent = new Intent(NoticeActivity.this, MakeActivity.class);
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
                Intent intent = new Intent(NoticeActivity.this, SearchActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("uid",uid);
                startActivity(intent);
                finish();
            }
        });
        // 마이그룹 버튼
        btn_group = (Button)findViewById(R.id.btn_group);
        btn_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NoticeActivity.this, GroupActivity.class);
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

        // 게시글, 투표글 구분하고 그에 맞게 보여주기
        iv_profile = (ImageView)findViewById(R.id.iv_profile);
        iv_image = (ImageView)findViewById(R.id.iv_image);
        tv_name = (TextView)findViewById(R.id.tv_name);
        tv_time = (TextView)findViewById(R.id.tv_time);
        tv_noticetitle = (TextView)findViewById(R.id.tv_noticetitle);
        tv_noticesummary = (TextView)findViewById(R.id.tv_noticesummary);
        btn_vote = (Button) findViewById(R.id.btn_vote);
        vote_recyclerview = (RecyclerView)findViewById(R.id.vote_recyclerview);
        showNotice();

        // 투표 저장 눌렀을 때
        btn_vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voteList = adapter.getCheckList();
                databaseReference.child("Group").child(groupname).child("Post").child(time).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<Vote> tempList;
                        ArrayList<String> personList, votePersonList;
                        tempList = new ArrayList<>();
                        votePersonList = new ArrayList<>();

                        post = snapshot.getValue(Post.class);
                        tempList = post.getVoteArrayList();
                        if(post.getVotePersonList() != null) {
                            votePersonList = post.getVotePersonList();
                        }
                        votePersonList.add(uid);
                        for(int i=0; i<tempList.size(); i++) {
                            if(voteList.get(i) == true) {
                                tempList.get(i).setVoteNum(tempList.get(i).getVoteNum() + 1);
                                personList = new ArrayList<>();
                                if(tempList.get(i).getPersonList() == null) {
                                    personList.add(uid);
                                    tempList.get(i).setPersonList(personList);
                                }
                                else {
                                    personList = tempList.get(i).getPersonList();
                                    personList.add(uid);
                                    tempList.get(i).setPersonList(personList);
                                }
                            }
                        }
                        mReference.child("Group").child(groupname).child("Post").child(time).child("voteArrayList").setValue(tempList);
                        mReference.child("Group").child(groupname).child("Post").child(time).child("votePersonList").setValue(votePersonList);
                        showNotice();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });
    }

    public void showNotice() {
        vote_recyclerview.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        vote_recyclerview.setLayoutManager(layoutManager);
        voteArrayList = new ArrayList<>();
        voteList = new ArrayList<>();
        databaseReference.child("Group").child(groupname).child("Post").child(time).child("vote").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // isVote로 게시글, 투표글 구분하기
                isVote = snapshot.getValue(Boolean.class);
                // 투표글일 경우
                if(isVote == true) {
                    mReference.child("Group").child(groupname).child("Post").child(time).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                            post = datasnapshot.getValue(Post.class);
                            Boolean endVote = false;
                            if(post.getVotePersonList()!=null) {
                                for(int i=0; i<post.getVotePersonList().size(); i++) {
                                    Log.e("###",post.getVotePersonList().get(i));
                                    if(post.getVotePersonList().get(i).equals(uid)) {
                                        endVote = true;
                                        break;
                                    }
                                }
                            }
                            if(endVote == true) {
                                iv_profile.setImageResource(R.drawable.ic_baseline_person_24);
                                tv_name.setText(post.getName());
                                tv_time.setText(post.getTime());
                                tv_noticetitle.setText(post.getTitle());
                                tv_noticesummary.setText(post.getSummary());
                                vote_recyclerview.setVisibility(View.VISIBLE);
                                btn_vote.setVisibility(View.GONE);
                                adapter = new VoteAdapter(voteArrayList, voteList, true, getApplicationContext());
                                vote_recyclerview.setAdapter(adapter);
                                for(int i=0; i<post.getVoteArrayList().size(); i++) {
                                    voteArrayList.add(post.getVoteArrayList().get(i));
                                    voteList.add(false);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                            else {
                                iv_profile.setImageResource(R.drawable.ic_baseline_person_24);
                                tv_name.setText(post.getName());
                                tv_time.setText(post.getTime());
                                tv_noticetitle.setText(post.getTitle());
                                tv_noticesummary.setText(post.getSummary());
                                vote_recyclerview.setVisibility(View.VISIBLE);
                                btn_vote.setVisibility(View.VISIBLE);
                                btn_vote.setText("투표하기");
                                adapter = new VoteAdapter(voteArrayList, voteList, false, getApplicationContext());
                                vote_recyclerview.setAdapter(adapter);
                                for(int i=0; i<post.getVoteArrayList().size(); i++) {
                                    voteArrayList.add(post.getVoteArrayList().get(i));
                                    voteList.add(false);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
                // 게시글일 경우
                else {
                    mReference.child("Group").child(groupname).child("Post").child(time).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                            post = datasnapshot.getValue(Post.class);
                            iv_profile.setImageResource(R.drawable.ic_baseline_person_24);
                            tv_name.setText(post.getName());
                            tv_time.setText(post.getTime());
                            tv_noticetitle.setText(post.getTitle());
                            tv_noticesummary.setText(post.getSummary());
                            if(post.getImage_url()!=null) {
                                StorageReference pathReference = storageReference.child("post_img/"+post.getImage_url());
                                pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        iv_image.setVisibility(View.VISIBLE);
                                        Glide.with(NoticeActivity.this).load(uri).into(iv_image);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    }
                                });
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
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
}