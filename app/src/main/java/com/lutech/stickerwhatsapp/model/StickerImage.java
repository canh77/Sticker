package com.lutech.stickerwhatsapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class StickerImage implements Parcelable {
    private Integer sticker_index;
    private String url;
    private Integer is_animated;

    public StickerImage(Integer sticker_index, String url, Integer is_animated) {
        this.sticker_index = sticker_index;
        this.url = url;
        this.is_animated = is_animated;
    }

    public Integer getSticker_index() {
        return sticker_index;
    }

    public void setSticker_index(Integer sticker_index) {
        this.sticker_index = sticker_index;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getIs_animated() {
        return is_animated;
    }

    public void setIs_animated(Integer is_animated) {
        this.is_animated = is_animated;
    }

    public StickerImage(Parcel in){
        sticker_index = in.readInt();
        url = in.readString();
        is_animated = in.readInt();
    }

    public static final Creator<StickerImage> CREATOR = new Creator<StickerImage>() {
        @Override
        public StickerImage createFromParcel(Parcel in) {
            return new StickerImage(in);
        }

        @Override
        public StickerImage[] newArray(int size) {
            return new StickerImage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(sticker_index);
        dest.writeString(url);
        dest.writeInt(is_animated);
    }


}