package ort.techtown.share_calendar.Adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import ort.techtown.share_calendar.Data.GroupRecyclerView;
import ort.techtown.share_calendar.NoticeActivity;
import ort.techtown.share_calendar.R;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {

    private ArrayList<GroupRecyclerView> arrayList;
    private Context context;
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private StorageReference storageReference = firebaseStorage.getReference();

    public GroupAdapter(ArrayList<GroupRecyclerView> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    // 아이템 클릭 리스너
    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    private OnItemClickListener mListener = null;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_recycler_item,parent,false);
        GroupViewHolder holder = new GroupViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        holder.group_name.setText(arrayList.get(position).getGroup_name());
        holder.group_introduce.setText(arrayList.get(position).getGroup_introduce());

        holder.group_image.setClipToOutline(true);
        if(!arrayList.get(position).getImage_url().equals(" ")) {
            StorageReference pathReference = storageReference.child("post_img/"+arrayList.get(position).getImage_url());
            pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(holder.group_image.getContext()).load(uri).centerCrop().into(holder.group_image);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
        }
        else {
            holder.group_image.setImageBitmap(null);
        }
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder {
        TextView group_name, group_introduce;
        ImageView group_image;

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);

            this.group_name = itemView.findViewById(R.id.group_name);
            this.group_introduce = itemView.findViewById(R.id.group_introduce);
            this.group_image = itemView.findViewById(R.id.group_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION) {
                        if(mListener!=null) {
                            mListener.onItemClick(v ,position);
                        }
                    }
                }
            });
        }
    }
}
