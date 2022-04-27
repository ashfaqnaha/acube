package com.example.moviesinfo.data;

public class Video {
    private String video_key;
    private String video_name;

    public Video(String video_key, String video_name) {
        this.video_key = video_key;
        this.video_name = video_name;
    }

    public String getVideo_key() {
        return video_key;
    }

    public void setVideo_key(String video_key) {
        this.video_key = video_key;
    }

    public String getVideo_name() {
        return video_name;
    }

    public void setVideo_name(String video_name) {
        this.video_name = video_name;
    }
}
