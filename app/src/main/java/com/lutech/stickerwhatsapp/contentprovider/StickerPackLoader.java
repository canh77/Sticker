package com.lutech.stickerwhatsapp.contentprovider;
import static com.lutech.stickerwhatsapp.contentprovider.StickerContentProvider2.STICKER_FILE_EMOJI_IN_QUERY;
import static com.lutech.stickerwhatsapp.contentprovider.StickerContentProvider2.STICKER_FILE_NAME_IN_QUERY;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import com.lutech.stickerwhatsapp.BuildConfig;
import com.lutech.stickerwhatsapp.model.Sticker1;
import com.lutech.stickerwhatsapp.model.StickerImage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

class StickerPackLoader {
    @NonNull
    static List<Sticker1> fetchStickerPacks(Context context) throws IllegalStateException {
        final Cursor cursor = context.getContentResolver().query(StickerContentProvider2.AUTHORITY_URI, null, null, null, null);
        if (cursor == null) {
            throw new IllegalStateException("could not fetch from content provider, " + BuildConfig.CONTENT_PROVIDER_AUTHORITY);
        }
        HashSet<String> identifierSet = new HashSet<>();
        final List<Sticker1> stickerPackList = fetchFromContentProvider(cursor);
        for (Sticker1 stickerPack : stickerPackList) {
            if (identifierSet.contains(stickerPack.getTitle())) {
                throw new IllegalStateException("sticker pack identifiers should be unique, there are more than one pack with identifier:" + stickerPack.getTitle());
            } else {
                identifierSet.add(stickerPack.getTitle());
            }
        }
        if (stickerPackList.isEmpty()) {
            throw new IllegalStateException("There should be at least one sticker pack in the app");
        }

        for (Sticker1 stickerPack : stickerPackList) {
            final List<StickerImage> stickers = getStickersForPack(context, stickerPack);
            stickerPack.setSticker(stickers);
//            StickerPackValidator.verifyStickerPackValidity(context, stickerPack);
        }
        return stickerPackList;
    }

    @NonNull
    private static List<StickerImage> getStickersForPack(Context context, Sticker1 stickerPack) {
        final List<StickerImage> stickers = fetchFromContentProviderForStickers(stickerPack.getTitle(), context.getContentResolver());
        for (StickerImage sticker : stickers) {
            final byte[] bytes;
            try {
                bytes = fetchStickerAsset(stickerPack.getTitle(), sticker.getUrl(), context.getContentResolver());
                if (bytes.length <= 0) {
                    throw new IllegalStateException("Asset file is empty, pack: " + stickerPack.description + ", sticker: " + sticker.getUrl());
                }
            } catch (IOException | IllegalArgumentException e) {
                throw new IllegalStateException("Asset file doesn't exist. pack: " + stickerPack.description + ", sticker: " + sticker.getUrl(), e);
            }
        }
        return stickers;
    }


    @NonNull
    private static List<Sticker1> fetchFromContentProvider(Cursor cursor) {
       List<Sticker1> stickerPackList = new ArrayList<>();
        cursor.moveToFirst();
        do {
            final String title = cursor.getString(cursor.getColumnIndexOrThrow(StickerContentProvider2.STICKER_PACK_IDENTIFIER_IN_QUERY));
            final String description = cursor.getString(cursor.getColumnIndexOrThrow(StickerContentProvider2.STICKER_PACK_NAME_IN_QUERY));
            final String tray_icon = cursor.getString(cursor.getColumnIndexOrThrow(StickerContentProvider2.STICKER_PACK_PUBLISHER_IN_QUERY));
            final String download_counter = cursor.getString(cursor.getColumnIndexOrThrow(String.valueOf(StickerContentProvider2.STICKER_PACK_ICON_IN_QUERY)));
            final String staff_pick = cursor.getString(cursor.getColumnIndexOrThrow(String.valueOf(StickerContentProvider2.ANDROID_APP_DOWNLOAD_LINK_IN_QUERY)));
            final String facebook_url = cursor.getString(cursor.getColumnIndexOrThrow(StickerContentProvider2.IOS_APP_DOWNLOAD_LINK_IN_QUERY));
            final String instagram_url = cursor.getString(cursor.getColumnIndexOrThrow(StickerContentProvider2.PUBLISHER_EMAIL));
            final String twitter_url = cursor.getString(cursor.getColumnIndexOrThrow(StickerContentProvider2.PUBLISHER_WEBSITE));
            final String tiktok_url = cursor.getString(cursor.getColumnIndexOrThrow(StickerContentProvider2.PRIVACY_POLICY_WEBSITE));
            final Sticker1 stickerPack = new Sticker1(title, description, tray_icon,
                    1, 1, facebook_url, instagram_url, twitter_url, tiktok_url,new ArrayList<>());
            stickerPackList.add(stickerPack);
        } while (cursor.moveToNext());
        return stickerPackList;
    }

    @NonNull
    private static List<StickerImage> fetchFromContentProviderForStickers(String identifier, ContentResolver contentResolver) {
        Uri uri = getStickerListUri(identifier);

        final String[] projection = {STICKER_FILE_NAME_IN_QUERY, STICKER_FILE_EMOJI_IN_QUERY};
        final Cursor cursor = contentResolver.query(uri, projection, null, null, null);
        List<StickerImage> stickers = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                final String name = cursor.getString(cursor.getColumnIndexOrThrow(STICKER_FILE_NAME_IN_QUERY));
                final String emojisConcatenated = cursor.getString(cursor.getColumnIndexOrThrow(STICKER_FILE_EMOJI_IN_QUERY));
                List<String> emojis = new ArrayList<>(StickerPackValidator.EMOJI_MAX_LIMIT);
                if (!TextUtils.isEmpty(emojisConcatenated)) {
                    emojis = Arrays.asList(emojisConcatenated.split(","));
                }
                stickers.add(new StickerImage(1,"",0));
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return stickers;
    }

    static byte[] fetchStickerAsset(@NonNull final String identifier, @NonNull final String name, ContentResolver contentResolver) throws IOException {
        try (final InputStream inputStream = contentResolver.openInputStream(getStickerAssetUri(identifier, name));
             final ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
            if (inputStream == null) {
                throw new IOException("cannot read sticker asset:" + identifier + "/" + name);
            }
            int read;
            byte[] data = new byte[16384];

            while ((read = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, read);
            }
            return buffer.toByteArray();
        }
    }

    private static Uri getStickerListUri(String identifier) {
        return new Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT).authority(BuildConfig.CONTENT_PROVIDER_AUTHORITY).appendPath(StickerContentProvider2.STICKERS).appendPath(identifier).build();
    }

    static Uri getStickerAssetUri(String identifier, String stickerName) {
        return new Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT).authority(BuildConfig.CONTENT_PROVIDER_AUTHORITY).appendPath(StickerContentProvider2.STICKERS_ASSET).appendPath(identifier).appendPath(stickerName).build();
    }
}
