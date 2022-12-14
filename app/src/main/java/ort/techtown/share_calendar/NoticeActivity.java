package ort.techtown.share_calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.storage.UploadTask;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ort.techtown.share_calendar.Adapter.CommentAdapter;
import ort.techtown.share_calendar.Adapter.PostAdapter;
import ort.techtown.share_calendar.Adapter.VoteAdapter;
import ort.techtown.share_calendar.Data.Comment;
import ort.techtown.share_calendar.Data.Post;
import ort.techtown.share_calendar.Data.Vote;

public class NoticeActivity extends AppCompatActivity {

    // drawerLayout
    private DrawerLayout drawerLayout;
    private Button btn_logout, btn_calendar, btn_search, btn_make, btn_group, btn_close, btn_profile;
    private View drawerView;
    private ImageView menu_open;
    private TextView tv_title;
    // ??????????????????
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();
    private DatabaseReference mReference = database.getReference();
    private DatabaseReference tmpReference = database.getReference();
    private DatabaseReference commentReference = database.getReference();
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private StorageReference storageReference = firebaseStorage.getReference();
    // ????????? ?????? ??????
    private static final int REQUEST_CODE = 0;
    private Uri uri;
    // ?????? ??????
    private String groupname, uid, host_uid, name, time;
    // ?????????, ????????? ??????
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
    // ?????? ??????
    private Button btn_comment_save;
    private EditText edt_comment;
    private CommentAdapter commentadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        groupname = getIntent().getStringExtra("groupname");
        uid = getIntent().getStringExtra("uid");
        host_uid = getIntent().getStringExtra("host_uid");
        name = getIntent().getStringExtra("name");
        time = getIntent().getStringExtra("time");

        // ??????
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_title.setText(groupname);

        // drawerLayout
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawerView = (View)findViewById(R.id.drawer);
        // drawerLayout ?????? ??????
        menu_open = (ImageView)findViewById(R.id.menu_open);
        menu_open.setVisibility(View.GONE);
//        menu_open.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                drawerLayout.openDrawer(drawerView);
//            }
//        });
        // drawLayout ?????? ??????
        btn_close = (Button)findViewById(R.id.btn_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawers();
            }
        });
        // ??????????????? ??????
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
        // ?????? ?????? ??????
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
        // ?????? ?????? ??????
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
        // ???????????? ??????
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
        // ??????????????? ??????
        btn_profile = (Button)findViewById(R.id.btn_profile);
        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        // ???????????? ??????
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

        // ?????????, ????????? ???????????? ?????? ?????? ????????????
        iv_profile = (ImageView)findViewById(R.id.iv_profile);
        iv_image = (ImageView)findViewById(R.id.iv_image);
        tv_name = (TextView)findViewById(R.id.tv_name);
        tv_time = (TextView)findViewById(R.id.tv_time);
        tv_noticetitle = (TextView)findViewById(R.id.tv_noticetitle);
        tv_noticesummary = (TextView)findViewById(R.id.tv_noticesummary);
        btn_vote = (Button) findViewById(R.id.btn_vote);
        vote_recyclerview = (RecyclerView)findViewById(R.id.vote_recyclerview);
        showNotice();
        showComment();

        // ?????? ?????? ????????? ???
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

        // ?????? ??????
        edt_comment = (EditText)findViewById(R.id.edt_comment);
        btn_comment_save = (Button)findViewById(R.id.btn_comment_save);
        btn_comment_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = edt_comment.getText().toString();
                Long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String new_time = sdf.format(date);
                Comment cmt = new Comment(name, uid, new_time, comment);
                ArrayList<Comment> commentList = new ArrayList<>();
                edt_comment.setText(null);
                databaseReference.child("Group").child(groupname).child("Post").child(time).child("commentList").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot:snapshot.getChildren()) {
                            Comment tmp_comment = dataSnapshot.getValue(Comment.class);
                            commentList.add(tmp_comment);
                        }
                        commentList.add(cmt);
                        commentReference.child("Group").child(groupname).child("Post").child(time).child("commentList").setValue(commentList);
                        Toast.makeText(getApplicationContext(),"????????? ?????????????????????.",Toast.LENGTH_SHORT).show();
                        showComment();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: //toolbar??? back??? ????????? ??? ??????
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                try{
                    uri = data.getData();
                    String filename;
                    if(uri!=null) {
                        filename = uri.toString() + ".jpg";
                        StorageReference riverRef = storageReference.child("post_img/"+filename);
                        UploadTask uploadTask = riverRef.putFile(uri);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            }
                        });
                        tmpReference.child("User").child(uid).child("Image_url").setValue(filename);
                        Toast.makeText(getApplicationContext(),"????????? ????????? ?????????????????????.",Toast.LENGTH_SHORT).show();
                        showNotice();
                    }
                } catch(Exception e){
                }
            }
            else if(resultCode == RESULT_CANCELED){
            }
        }
    }

    public void showComment() {
        RecyclerView comment_recyclerview = findViewById(R.id.comment_recyclerview);
        comment_recyclerview.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        comment_recyclerview.setLayoutManager(layoutManager);
        ArrayList<Comment> commentList = new ArrayList<>();
        commentReference.child("Group").child(groupname).child("Post").child(time).child("commentList").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commentList.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()) {
                    Comment tmp_comment = dataSnapshot.getValue(Comment.class);
                    commentList.add(tmp_comment);
                }
                commentadapter.notifyDataSetChanged();
                comment_recyclerview.setAdapter(commentadapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        commentadapter = new CommentAdapter(commentList, this);
    }

    public void showNotice() {
        vote_recyclerview.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        vote_recyclerview.setLayoutManager(layoutManager);
        voteArrayList = new ArrayList<>();
        voteList = new ArrayList<>();
        databaseReference.child("User").child(host_uid).child("Image_url").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue() == null) {
                    iv_profile.setImageResource(R.drawable.ic_baseline_person_24);
                }
                else {
                    StorageReference pathReference = storageReference.child("post_img/"+snapshot.getValue());
                    pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(iv_profile.getContext()).load(uri).centerCrop().into(iv_profile);
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
        databaseReference.child("Group").child(groupname).child("Post").child(time).child("vote").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // isVote??? ?????????, ????????? ????????????
                isVote = snapshot.getValue(Boolean.class);
                // ???????????? ??????
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
                                tv_name.setText(post.getName());
                                String curtime = post.getTime().substring(5,7) +"/"+
                                        post.getTime().substring(8,16);
                                tv_time.setText(curtime);
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
                                tv_name.setText(post.getName());
                                tv_time.setText(post.getTime());
                                tv_noticetitle.setText(post.getTitle());
                                tv_noticesummary.setText(post.getSummary());
                                vote_recyclerview.setVisibility(View.VISIBLE);
                                btn_vote.setVisibility(View.VISIBLE);
                                btn_vote.setText("????????????");
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
                // ???????????? ??????
                else {
                    mReference.child("Group").child(groupname).child("Post").child(time).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                            post = datasnapshot.getValue(Post.class);
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

    // drawerLayout ?????????
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