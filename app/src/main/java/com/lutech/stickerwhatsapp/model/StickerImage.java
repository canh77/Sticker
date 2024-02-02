package com.lutech.stickerwhatsapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class StickerImage implements Parcelable {
    final String sticker_index;
    final String url;
    final Integer is_animated;

    public StickerImage(String sticker_index, String url, Integer is_animated) {
        this.sticker_index = sticker_index;
        this.url = url;
        this.is_animated = is_animated;
    }


    public String getSticker_index() {
        return sticker_index;
    }

    public String getUrl() {
        return url;
    }

    public Integer getIs_animated() {
        return is_animated;
    }

    private StickerImage(Parcel in){
        sticker_index = in.readString();
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
        dest.writeString(sticker_index);
        dest.writeString(url);
        dest.writeInt(is_animated);
    }


}