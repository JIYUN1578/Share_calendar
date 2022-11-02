package ort.techtown.share_calendar.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import ort.techtown.share_calendar.Data.Post;
import ort.techtown.share_calendar.R;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private ArrayList<Post> arrayList;
    private Context context;

    public PostAdapter(ArrayList<Post> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_recycler_item, parent, false);
        PostViewHolder holder = new PostViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        holder.iv_image.setImageResource(R.drawable.ic_baseline_person_24);
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
        }
    }
}
