package com.cesi.cube;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
        holder.imageUrl = post.getImageURL();
        holder.layoutUrl = post.getDocumentUrl();
        holder.titleTextView.setText(post.getTitle());
        if(holder.imageUrl == null) holder.imageView.setVisibility(View.GONE);
        if(holder.layoutUrl == null) holder.layoutDoc.setVisibility(View.GONE);
        holder.docName.setText(post.getDocumentUrl());
        Picasso.get()
                .load(holder.imageUrl)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        public ImageView profileImageView;
        public TextView usernameTextView;
        public TextView titleTextView;
        public TextView postContentTextView;
        public String imageUrl;
        public ImageView imageView;
        public LinearLayout layoutDoc;
        public String layoutUrl;
        public TextView docName;
        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImageView = itemView.findViewById(R.id.profileImage);
            usernameTextView = itemView.findViewById(R.id.profileName);
            postContentTextView = itemView.findViewById(R.id.postContent);
            titleTextView = itemView.findViewById(R.id.profileName);
            imageView = itemView.findViewById(R.id.postImage);
            layoutDoc = itemView.findViewById(R.id.layoutDoc);
            docName = itemView.findViewById(R.id.docName);

        }
    }
}
