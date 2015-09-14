package com.uyogist.uyogist;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Gist Model
 * Created by oyewale on 9/12/15.
 */
public class Gist {

    private String key;

    @SerializedName("timestamp")
    private long createdAt;

    private String gist;

    @SerializedName("added_by")
    private String author;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getGist() {
        return gist;
    }

    public void setGist(String gist) {
        this.gist = gist;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Get Dummy Gists
     * @param count gist count
     * @return dummy gists
     */
    public static List<Gist> getDummyGists(int count) {
        List<Gist> gists = new ArrayList<>();
        for (int i=0; i<count; i++){
            gists.add(new Gist());
        }
        return gists;
    }
}
