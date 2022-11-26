package ort.techtown.share_calendar.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import ort.techtown.share_calendar.Data.Comment;
import ort.techtown.share_calendar.R;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private ArrayList<Comment> arrayList;
    private Context context;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private StorageReference storageReference = firebaseStorage.getReference();

    public CommentAdapter(ArrayList<Comment> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        CommentViewHolder holder = new CommentViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        databaseReference = database.getReference("User/"+arrayList.get(position).getComment_uid()+"/Image_url/");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue() == null) {
                    holder.iv_profile.setImageResource(R.drawable.ic_baseline_person_24);
                }
                else {
                    StorageReference pathReference = storageReference.child("post_img/"+snapshot.getValue());
                    pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(holder.iv_profile.getContext()).load(uri).centerCrop().into(holder.iv_profile);
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
        holder.tv_name.setText(arrayList.get(position).getComment_name());
        holder.tv_time.setText(arrayList.get(position).getComment_time());
        holder.tv_comment.setText(arrayList.get(position).getComment_summary());
    }

    @Override
    public int getItemCount() {
        return (arrayList!=null?arrayList.size():0);
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder{

        ImageView iv_profile;
        TextView tv_name, tv_time, tv_comment;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            this.iv_profile = itemView.findViewById(R.id.iv_profile);
            this.tv_name = itemView.findViewById(R.id.tv_name);
            this.tv_time = itemView.findViewById(R.id.tv_time);
            this.tv_comment = itemView.findViewById(R.id.tv_comment);
        }
    }
}
