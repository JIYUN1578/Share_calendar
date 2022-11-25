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

import ort.techtown.share_calendar.Data.Post;
import ort.techtown.share_calendar.R;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private ArrayList<Post> arrayList;
    private Context context;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private StorageReference storageReference = firebaseStorage.getReference();

    public PostAdapter(ArrayList<Post> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    // 아이템 클릭 리스너
    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    private OnItemClickListener mListener = null;

    public void setOnItemClickListener(OnItemClickListener listener) {this.mListener = listener; }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_recycler_item, parent, false);
        PostViewHolder holder = new PostViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        databaseReference = database.getReference("User/"+arrayList.get(position).getUid()+"/Image_url/");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue() == null) {
                    holder.iv_image.setImageResource(R.drawable.ic_baseline_person_24);
                }
                else {
                    StorageReference pathReference = storageReference.child("post_img/"+snapshot.getValue());
                    pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(holder.iv_image.getContext()).load(uri).centerCrop().into(holder.iv_image);
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
        if(arrayList.get(position).getVote() == true) {
            holder.iv_separate.setImageResource(R.drawable.ic_baseline_how_to_vote_24);
        } else {
            holder.iv_separate.setImageResource(R.drawable.ic_baseline_notes_24);
        }
        holder.tv_name.setText(arrayList.get(position).getName());
        holder.tv_posttitle.setText(arrayList.get(position).getTitle());
        holder.tv_postsummary.setText(arrayList.get(position).getSummary());
    }

    @Override
    public int getItemCount() {
        return (arrayList!=null?arrayList.size():0);
    }

    public class PostViewHolder extends RecyclerView.ViewHolder{
        ImageView iv_image, iv_separate;
        TextView tv_posttitle, tv_postsummary, tv_name;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            this.iv_image = itemView.findViewById(R.id.iv_image);
            this.iv_separate = itemView.findViewById(R.id.iv_separate);
            this.tv_posttitle = itemView.findViewById(R.id.tv_posttitle);
            this.tv_postsummary = itemView.findViewById(R.id.tv_postsummary);
            this.tv_name = itemView.findViewById(R.id.tv_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION) {
                        if(mListener!=null) {
                            mListener.onItemClick(v, position);
                        }
                    }
                }
            });
        }
    }
}
