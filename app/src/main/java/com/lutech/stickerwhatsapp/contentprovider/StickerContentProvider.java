/*
 * Copyright (c) WhatsApp Inc. and its affiliates.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.lutech.stickerwhatsapp.contentprovider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lutech.stickerwhatsapp.BuildConfig;
import com.lutech.stickerwhatsapp.model.Sticker;
import com.lutech.stickerwhatsapp.model.StickerImage;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class StickerContentProvider extends ContentProvider {

    /**
     * Do not change the strings listed below, as these are used by WhatsApp. And changing these will break the interface between sticker app and WhatsApp.
     */
    public static final String STICKER_TITLE = "sticker_title";
    public static final String STICKER_DESCRIPTIONS = "sticker_description";
    public static final String STICKER_TRAY_ICON = "sticker_tray_icon";
    public static final Integer STICKER_DOWNLOAD_COUNTER = 1;
    public static final Integer STICKER_STAFF_PICK = 1;
    public static final String STICKER_FACEBOOK_URL = "sticker_facebook_url";
    public static final String STICKER_INSTAGRAM_URL = "sticker_instagram_url";
    public static final String STICKER_TWITTER_URL = "sticker_twitter_url";
    public static final String STICKER_TIKTOK_URL = "sticker_tiktok_url";

    public static final String STICKER_FILE_EMOJI_IN_QUERY = "sticker_file_emoji_in_query";
    public static  final  String STICKER_FILE_NAME_IN_QUERY = "sticker_file_name_in_query";

    public static final Uri AUTHORITY_URI = new Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT)
            .authority(BuildConfig.CONTENT_PROVIDER_AUTHORITY).appendPath(StickerContentProvider.METADATA).build();

    /**
     * Do not change the values in the UriMatcher because otherwise, WhatsApp will not be able to fetch the stickers from the ContentProvider.
     */
    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    private static final String METADATA = "metadata";
    private static final int METADATA_CODE = 1;

    private static final int METADATA_CODE_FOR_SINGLE_PACK = 2;

    static final String STICKERS = "stickers";
    private static final int STICKERS_CODE = 3;

    static final String STICKERS_ASSET = "stickers_asset";
    private static final int STICKERS_ASSET_CODE = 4;

    private static final int STICKER_PACK_TRAY_ICON_CODE = 5;

    private List<Sticker> stickerPackList;

    @Override
    public boolean onCreate() {
        final String authority = BuildConfig.CONTENT_PROVIDER_AUTHORITY;
        if (!authority.startsWith(Objects.requireNonNull(getContext()).getPackageName())) {
            throw new IllegalStateException("your authority (" + authority + ") for the content provider should start with your package name: " + getContext().getPackageName());
        }
        stickerPackList = new ArrayList<>();
        MATCHER.addURI(authority, METADATA, METADATA_CODE);
        MATCHER.addURI(authority, METADATA + "/*", METADATA_CODE_FOR_SINGLE_PACK);
        MATCHER.addURI(authority, STICKERS + "/*", STICKERS_CODE);
            for (Sticker sticker : getStickerPackList()){
                MATCHER.addURI(authority,STICKERS_ASSET+"/"+sticker.getTitle() + "/" + sticker.getTray_icon(),STICKER_PACK_TRAY_ICON_CODE);
                for (StickerImage stickerImage : sticker.getSticker()){
                    MATCHER.addURI(authority,STICKERS_ASSET + "/" + sticker.getTitle() + "/" + stickerImage.getUrl(), STICKERS_ASSET_CODE);
                }
        }
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        final int code = MATCHER.match(uri);
        if (code == METADATA_CODE) {
            return getPackForAllStickerPacks(uri);
        } else if (code == METADATA_CODE_FOR_SINGLE_PACK) {
            return getCursorForSingleStickerPack(uri);
        } else if (code == STICKERS_CODE) {
            return getStickersForAStickerPack(uri);
        } else {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public AssetFileDescriptor openAssetFile(@NonNull Uri uri, @NonNull String mode) {
        final int matchCode = MATCHER.match(uri);
        if (matchCode == STICKERS_ASSET_CODE || matchCode == STICKER_PACK_TRAY_ICON_CODE) {
            return getImageAsset(uri);
        }
        return null;
    }


    @Override
    public String getType(@NonNull Uri uri) {
        final int matchCode = MATCHER.match(uri);
        switch (matchCode) {
            case METADATA_CODE:
                return "vnd.android.cursor.dir/vnd." + BuildConfig.CONTENT_PROVIDER_AUTHORITY + "." + METADATA;
            case METADATA_CODE_FOR_SINGLE_PACK:
                return "vnd.android.cursor.item/vnd." + BuildConfig.CONTENT_PROVIDER_AUTHORITY + "." + METADATA;
            case STICKERS_CODE:
                return "vnd.android.cursor.dir/vnd." + BuildConfig.CONTENT_PROVIDER_AUTHORITY + "." + STICKERS;
            case STICKERS_ASSET_CODE:
                return "image/webp";
            case STICKER_PACK_TRAY_ICON_CODE:
                return "image/png";
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    private synchronized void readContentFile(@NonNull Context context) {
        try (InputStream contentsInputStream = context.getAssets().open("")) {
            stickerPackList = ContentFileParser.parseStickerPacks(contentsInputStream);
        } catch (IOException | IllegalStateException e) {
//            throw new RuntimeException(CONTENT_FILE_NAME + " file has some issues: " + e.getMessage(), e);
        }
    }

    private List<Sticker> getStickerPackList() {
        if (stickerPackList == null) {
            readContentFile(Objects.requireNonNull(getContext()));
        }
        return stickerPackList;
    }

    private Cursor getPackForAllStickerPacks(@NonNull Uri uri) {
        return getStickerPackInfo(uri, getStickerPackList());
    }

    private Cursor getCursorForSingleStickerPack(@NonNull Uri uri) {
        final String identifier = uri.getLastPathSegment();
        for (Sticker stickerPack : getStickerPackList()) {
            if (identifier.equals(stickerPack.getTitle())) {
                return getStickerPackInfo(uri, Collections.singletonList(stickerPack));
            }
        }
        return getStickerPackInfo(uri, new ArrayList<>());
    }

    @NonNull
    private Cursor getStickerPackInfo(@NonNull Uri uri, @NonNull List<Sticker> stickerPackList) {
        MatrixCursor cursor = new MatrixCursor(
                new String[]{
                        STICKER_TITLE,
                        STICKER_DESCRIPTIONS,
                        STICKER_TRAY_ICON,
                        String.valueOf(STICKER_DOWNLOAD_COUNTER),
                        String.valueOf(STICKER_STAFF_PICK),
                        STICKER_INSTAGRAM_URL,
                        STICKER_TIKTOK_URL,
                        STICKER_TWITTER_URL,
                        STICKER_FACEBOOK_URL
                });
        for (Sticker stickerPack : stickerPackList) {
            MatrixCursor.RowBuilder builder = cursor.newRow();
            builder.add(stickerPack.getTitle());
            builder.add(stickerPack.getDescription());
            builder.add(stickerPack.getTray_icon());
            builder.add(stickerPack.getInstagram_url());
            builder.add(stickerPack.getTwitter_url());
            builder.add(stickerPack.getTiktok_url());
            builder.add(stickerPack.getFacebook_url());
            builder.add(stickerPack.getStaff_pick());
            builder.add(stickerPack.getTray_icon());
            builder.add(stickerPack.getImages());
        }
        cursor.setNotificationUri(Objects.requireNonNull(getContext()).getContentResolver(), uri);
        return cursor;
    }

    @NonNull
    private Cursor getStickersForAStickerPack(@NonNull Uri uri) {
        final String identifier = uri.getLastPathSegment();
        MatrixCursor cursor = new MatrixCursor(new String[]{STICKER_FILE_NAME_IN_QUERY, STICKER_FILE_EMOJI_IN_QUERY});
        for (Sticker stickerPack : getStickerPackList()) {
            if (identifier.equals(stickerPack.getTitle())) {
                for (StickerImage sticker : stickerPack.getSticker()) {
                    cursor.addRow(new Object[]{sticker.getSticker_index(), sticker.getUrl(),sticker.getIs_animated()});
                }
            }
        }
        cursor.setNotificationUri(Objects.requireNonNull(getContext()).getContentResolver(), uri);
        return cursor;
    }

    private AssetFileDescriptor getImageAsset(Uri uri) throws IllegalArgumentException {
        AssetManager am = Objects.requireNonNull(getContext()).getAssets();
        final List<String> pathSegments = uri.getPathSegments();
        if (pathSegments.size() != 3) {
            throw new IllegalArgumentException("path segments should be 3, uri is: " + uri);
        }
        String fileName = pathSegments.get(pathSegments.size() - 1);
        final String identifier = pathSegments.get(pathSegments.size() - 2);
        if (TextUtils.isEmpty(identifier)) {
            throw new IllegalArgumentException("identifier is empty, uri: " + uri);
        }
        if (TextUtils.isEmpty(fileName)) {
            throw new IllegalArgumentException("file name is empty, uri: " + uri);
        }
        //making sure the file that is trying to be fetched is in the list of stickers.
        for (Sticker stickerPack : getStickerPackList()) {
            if (identifier.equals(stickerPack.getSticker())) {
                if (fileName.equals(stickerPack.getTray_icon())) {
                    return fetchFile(uri, am, fileName, identifier);
                } else {
                    for (StickerImage sticker : stickerPack.getSticker()) {
                        if (fileName.equals(sticker.getUrl())) {
                            return fetchFile(uri, am, fileName, identifier);
                        }
                    }
                }
            }
        }
        return null;
    }

    private AssetFileDescriptor fetchFile(@NonNull Uri uri, @NonNull AssetManager am, @NonNull String fileName, @NonNull String identifier) {
        try {
            return am.openFd(identifier + "/" + fileName);
        } catch (IOException e) {
            Log.e(Objects.requireNonNull(getContext()).getPackageName(), "IOException when getting asset file, uri:" + uri, e);
            return null;
        }
    }


    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        throw new UnsupportedOperationException("Not supported");
    }
}
