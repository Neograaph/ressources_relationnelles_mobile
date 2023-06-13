package com.cesi.cube;

import androidx.annotation.Nullable;

public class Post {
    public Post(String title, String content,@Nullable String imageUrl, String documentUrl) {
        this.title = title;
        this.content = content;
        this.imageURL = imageUrl;
        this.documentUrl = documentUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String title;
    public String author;
    public String content;
    @Nullable
    public String imageURL = null;

    public String getDocumentUrl() {
        return documentUrl;
    }

    public void setDocumentUrl(String documentUrl) {
        this.documentUrl = documentUrl;
    }

    public String documentUrl = null;


}
