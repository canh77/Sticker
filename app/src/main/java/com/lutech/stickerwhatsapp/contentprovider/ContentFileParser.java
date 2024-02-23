/*
 * Copyright (c) WhatsApp Inc. and its affiliates.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.lutech.stickerwhatsapp.contentprovider;

import android.text.TextUtils;
import android.util.JsonReader;
import android.util.Log;

import androidx.annotation.NonNull;


import com.lutech.stickerwhatsapp.model.Sticker1;
import com.lutech.stickerwhatsapp.model.StickerImage;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

class ContentFileParser {

    @NonNull
    static List<Sticker1> parseStickerPacks(@NonNull InputStream contentsInputStream) throws IOException, IllegalStateException {
        try (JsonReader reader = new JsonReader(new InputStreamReader(contentsInputStream))) {
            return readStickerPacks(reader);
        }
    }

    @NonNull
    private static List<Sticker1> readStickerPacks(@NonNull JsonReader reader) throws IOException, IllegalStateException {
        List<Sticker1> stickerPackList = new ArrayList<>();
        reader.beginObject();
        while (reader.hasNext()) {
            String key = reader.nextName();
            if ("android_play_store_link".equals(key)) {
//                androidPlayStoreLink = reader.nextString();
            } else if ("ios_app_store_link".equals(key)) {
//                iosAppStoreLink = reader.nextString();
            } else if ("sticker_packs".equals(key)) {
                reader.beginArray();
                while (reader.hasNext()) {
                    Sticker1 stickerPack = readStickerPack(reader);
                    stickerPackList.add(stickerPack);
                }
                reader.endArray();
            } else {
                throw new IllegalStateException("unknown field in json: " + key);
            }
        }
        reader.endObject();
        if (stickerPackList.size() == 0) {
            throw new IllegalStateException("sticker pack list cannot be empty");
        }
        return stickerPackList;
    }

    @NonNull
    private static Sticker1 readStickerPack(@NonNull JsonReader reader) throws IOException, IllegalStateException {
        reader.beginObject();
        String title = null;
        String description = null;
        String tray_icon = null;
        Integer download_counter;
        Integer staff_pick;
        String facebook_url = null;
        String instagram_url = null;
        String twitter_url = null;
        String tiktok_url = "";
        List<StickerImage> stickerList = null;
        while (reader.hasNext()) {
            String key = reader.nextName();
            switch (key) {
                case "title":
                    title = reader.nextString();
                    break;
                case "description":
                    description = reader.nextString();
                    break;
                case "tray_icon":
                    tray_icon = reader.nextString();
                    break;
                case "download_counter":
                    download_counter = reader.nextInt();
                    break;
                case "staff_pick":
                    staff_pick = reader.nextInt();
                    break;
                case "facebook_url":
                    facebook_url = reader.nextString();
                    break;
                case "instagram_url":
                    instagram_url = reader.nextString();
                    break;
                case "twitter_url":
                    twitter_url = reader.nextString();
                    break;
                case "tiktok_url":
                    tiktok_url = reader.nextString();
                    break;
                case "stickerList":
                    stickerList = readStickers(reader);
                    break;

                default:
                    reader.skipValue();
            }
        }
        if (TextUtils.isEmpty(title)) {
            Log.d("0001101100", "TextUtils.isEmpty(title): ");
            throw new IllegalStateException("title cannot be empty");
        }
        if (TextUtils.isEmpty(description)) {
            throw new IllegalStateException("description cannot be empty");
        }
        if (TextUtils.isEmpty(tray_icon)) {
            throw new IllegalStateException("tray_icon cannot be empty");
        }
        if (TextUtils.isEmpty(facebook_url)) {
            throw new IllegalStateException("facebook_url cannot be empty");
        }

        if (TextUtils.isEmpty(instagram_url)){
            throw  new IllegalStateException("instagram_url cannot be empty");
        }

        if (TextUtils.isEmpty(twitter_url)){
            throw  new IllegalStateException("twitter_url cannot be empty");
        }

        if (TextUtils.isEmpty(tiktok_url)){
            throw  new IllegalStateException("tiktok_url cannot be empty");
        }
        if (stickerList == null || stickerList.size() == 0) {
            throw new IllegalStateException("sticker list is empty");
        }
        if (title.contains("..") || title.contains("/")) {
            throw new IllegalStateException("identifier should not contain .. or / to prevent directory traversal");
        }

        reader.endObject();
        final Sticker1 stickerPack = new Sticker1(title,description,tray_icon,
                1,1,"","",
                "","","",stickerList);
        stickerPack.setSticker(stickerList);
        return stickerPack;
    }

    @NonNull
    private static List<StickerImage> readStickers(@NonNull JsonReader reader) throws IOException, IllegalStateException {
        reader.beginArray();
        List<StickerImage> stickerList = new ArrayList<>();

        while (reader.hasNext()) {
            reader.beginObject();
            String imageFile = null;
            List<String> emojis = new ArrayList<>(StickerPackValidator.EMOJI_MAX_LIMIT);
            while (reader.hasNext()) {
                final String key = reader.nextName();
                if ("image_file".equals(key)) {
                    imageFile = reader.nextString();
                } else if ("emojis".equals(key)) {
                    reader.beginArray();
                    while (reader.hasNext()) {
                        String emoji = reader.nextString();
                        if (!TextUtils.isEmpty(emoji)) {
                            emojis.add(emoji);
                        }
                    }
                    reader.endArray();

                } else {
                    throw new IllegalStateException("unknown field in json: " + key);
                }
            }
            reader.endObject();
            if (TextUtils.isEmpty(imageFile)) {
                throw new IllegalStateException("sticker image_file cannot be empty");
            }
            //có ảnh webp
            if (!imageFile.endsWith(".webp")) {
                throw new IllegalStateException("image file for stickers should be webp files, image file is: " + imageFile);
            }
            if (imageFile.contains("..") || imageFile.contains("/")) {
                throw new IllegalStateException("the file name should not contain .. or / to prevent directory traversal, image file is:" + imageFile);
            }
            stickerList.add(new StickerImage(1,"",0));
        }
        reader.endArray();
        return stickerList;
    }
}
