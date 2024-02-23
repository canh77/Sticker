package com.lutech.stickerwhatsapp.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;


import java.util.ArrayList;
import java.util.List;

public class Sticker1 implements Parcelable {
    public String title;
    public String description;
    public String tray_icon;
    public Integer download_counter;
    public Integer staff_pick;
    public String facebook_url;
    public String instagram_url;
    public String twitter_url;
    public String tiktok_url;
    public List<StickerImage> images;
    private int stickersAddedIndex = 0;


     public Sticker1(String title, String description, String tray_icon, Integer download_counter, Integer staff_pick, String facebook_url, String instagram_url, String twitter_url, String tiktok_url, String s, List<StickerImage> images) {
        this.title = title;
        this.description = description;
        this.tray_icon = tray_icon;
        this.download_counter = download_counter;
        this.staff_pick = staff_pick;
        this.facebook_url = facebook_url;
        this.instagram_url = instagram_url;
        this.twitter_url = twitter_url;
        this.tiktok_url = tiktok_url;
        this.images = new ArrayList<>();
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public Sticker1(String title, String description, String tray_icon, Integer download_counter, Integer staff_pick, String facebook_url,
                    String instagram_url, String twitter_url, String tiktok_url, List<StickerImage> images) {
        this.title = title;
        this.description = description;
        this.tray_icon = tray_icon;
        this.download_counter = download_counter;
        this.staff_pick = staff_pick;
        this.facebook_url = facebook_url;
        this.instagram_url = instagram_url;
        this.twitter_url = twitter_url;
        this.tiktok_url = tiktok_url;
        this.images = new ArrayList<>();
    }


    protected Sticker1(Parcel in) {
        title = in.readString();
        description = in.readString();
        tray_icon = in.readString();
        download_counter = in.readInt();
        staff_pick = in.readInt();
        facebook_url = in.readString();
        instagram_url = in.readString();
        twitter_url = in.readString();
        tiktok_url = in.readString();
        images = in.createTypedArrayList(StickerImage.CREATOR);
    }

    public static final Parcelable.Creator<Sticker1> CREATOR = new Parcelable.Creator<Sticker1>() {
        @Override
        public Sticker1 createFromParcel(Parcel in) {
            return new Sticker1(in);
        }

        @Override
        public Sticker1[] newArray(int size) {
            return new Sticker1[size];
        }
    };

    public void addSticker( Context context) {
        String index = String.valueOf(stickersAddedIndex);
        this.images.add(new StickerImage(0, index, 0));
        stickersAddedIndex++;
    }


    public List<StickerImage> getSticker() {
        return images;
    }

   public void setSticker(List<StickerImage> stickerImages){
        this.images = stickerImages;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(tray_icon);
        parcel.writeInt(download_counter);
        parcel.writeInt(staff_pick);
        parcel.writeString(facebook_url);
        parcel.writeString(instagram_url);
        parcel.writeString(twitter_url);
        parcel.writeString(tiktok_url);
        parcel.writeTypedList(images);
    }
}
