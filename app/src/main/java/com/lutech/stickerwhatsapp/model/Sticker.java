package com.lutech.stickerwhatsapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;

public class Sticker implements Parcelable {
    private String title;
    private String description;
    private String tray_icon;
    private Integer download_counter;
    private Integer staff_pick;
    private String facebook_url;
    private String instagram_url;
    private String twitter_url;
    private String tiktok_url;
    private List<StickerImage> images;

     public Sticker(String title, String description, String tray_icon, Integer download_counter, Integer staff_pick, String facebook_url, String instagram_url, String twitter_url, String tiktok_url, String s, List<StickerImage> images) {
        this.title = title;
        this.description = description;
        this.tray_icon = tray_icon;
        this.download_counter = download_counter;
        this.staff_pick = staff_pick;
        this.facebook_url = facebook_url;
        this.instagram_url = instagram_url;
        this.twitter_url = twitter_url;
        this.tiktok_url = tiktok_url;
        this.images = images;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTray_icon() {
        return tray_icon;
    }

    public void setTray_icon(String tray_icon) {
        this.tray_icon = tray_icon;
    }

    public Integer getDownload_counter() {
        return download_counter;
    }

    public void setDownload_counter(Integer download_counter) {
        this.download_counter = download_counter;
    }

    public Integer getStaff_pick() {
        return staff_pick;
    }

    public void setStaff_pick(Integer staff_pick) {
        this.staff_pick = staff_pick;
    }

    public String getFacebook_url() {
        return facebook_url;
    }

    public void setFacebook_url(String facebook_url) {
        this.facebook_url = facebook_url;
    }

    public String getInstagram_url() {
        return instagram_url;
    }

    public void setInstagram_url(String instagram_url) {
        this.instagram_url = instagram_url;
    }

    public String getTwitter_url() {
        return twitter_url;
    }

    public void setTwitter_url(String twitter_url) {
        this.twitter_url = twitter_url;
    }

    public String getTiktok_url() {
        return tiktok_url;
    }

    public void setTiktok_url(String tiktok_url) {
        this.tiktok_url = tiktok_url;
    }

    public List<StickerImage> getImages() {
        return images;
    }

    public void setImages(List<StickerImage> images) {
        this.images = images;
    }

    public Sticker(String title, String description, String tray_icon, Integer download_counter, Integer staff_pick, String facebook_url,
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
        this.images = images;
    }


    protected Sticker(Parcel in) {
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

    public static final Parcelable.Creator<Sticker> CREATOR = new Parcelable.Creator<Sticker>() {
        @Override
        public Sticker createFromParcel(Parcel in) {
            return new Sticker(in);
        }

        @Override
        public Sticker[] newArray(int size) {
            return new Sticker[size];
        }
    };


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
