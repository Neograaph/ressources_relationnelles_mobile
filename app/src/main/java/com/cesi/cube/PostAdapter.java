package com.cesi.cube;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<Post> postList;

    public PostAdapter(List<Post> postList) {
        this.postList = postList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_tp, parent, false);
        return new PostViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.usernameTextView.setText(post.getAuthor());
        holder.postContentTextView.setText(post.getContent());
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        public ImageView profileImageView;
        public TextView usernameTextView;
        public TextView postContentTextView;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImageView = itemView.findViewById(R.id.profileImage);
            usernameTextView = itemView.findViewById(R.id.profileName);
            postContentTextView = itemView.findViewById(R.id.postContent);
            ImageView imageView = itemView.findViewById(R.id.postImage);
            String imageUrl = "https://latavernedutesteur.files.wordpress.com/2017/11/testss.png";

            Picasso.get()
                    .load(imageUrl)
                    .into(imageView);
        }
    }
}
