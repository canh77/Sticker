package com.lutech.stickerwhatsapp.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lutech.stickerwhatsapp.BuildConfig;
import com.lutech.stickerwhatsapp.R;
import com.lutech.stickerwhatsapp.fragment.CommunityFragment;
import com.lutech.stickerwhatsapp.adapter.StickerDetailsAdapter;
import com.lutech.stickerwhatsapp.model.Sticker;
import com.lutech.stickerwhatsapp.model.StickerPack;
import com.lutech.stickerwhatsapp.utils.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class StickerDetailsActivity extends AppCompatActivity {
    private static final int ADD_PACK = 200;
    private StickerPack stickerPack;
    private StickerDetailsAdapter adapter;
    private RecyclerView recyclerView;
    private List<Sticker> stickers;
    private ArrayList<String> strings;
    public static String path;
    private ImageView btnBack,iv_sticker_trays;
    private LinearLayout btnAddToWhatsapp,lnShareSticker;
    private TextView tvTitleSticker,tvTitleDetailSticker,tvNumberDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker_details);
        initView();
        initData();
        handlerEvents();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    private void initView() {
        btnBack = findViewById(R.id.ivBack);
        btnAddToWhatsapp = findViewById(R.id.btnAddToWhatsapp);
        recyclerView = findViewById(R.id.recyclerView);
        iv_sticker_trays = findViewById(R.id.iv_sticker_trays);
        tvTitleSticker = findViewById(R.id.tvTitleStickers);
        tvTitleDetailSticker = findViewById(R.id.tvStickerDescriptions);
        tvNumberDownload = findViewById(R.id.tvNumberDownloads);
        lnShareSticker = findViewById(R.id.lnShareSticker);
    }

    private void initData() {
        stickerPack = getIntent().getParcelableExtra(CommunityFragment.EXTRA_STICKERPACK);
        tvTitleSticker.setText(stickerPack.name);
        tvTitleDetailSticker.setText(stickerPack.publisher);
        tvNumberDownload.setText(stickerPack.publisherEmail);
        Glide.with(this).load(Constants.Url +stickerPack.name +"/"+stickerPack.trayImageFile.replace("_","_")).into(iv_sticker_trays);
        stickers = stickerPack.getStickers();
        strings = new ArrayList<>();
        path = getFilesDir() + "/" + "stickers_asset" + "/" + stickerPack.identifier + "/";
        Log.d("0000000000000000", "path: " + path);
        File file = new File(path + stickers.get(0).imageFileName);
        Log.d("0000000000000000", "onCreate: " + path + stickers.get(0).imageFileName);
        for (Sticker s : stickers) {
            if (!file.exists()) {
                strings.add(s.imageFileName);
            } else {
                strings.add(path + s.imageFileName);
            }
        }
        adapter = new StickerDetailsAdapter(strings, this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(adapter);
    }


    private void handlerEvents() {
        btnBack.setOnClickListener(view -> {
            finish();
        });

        lnShareSticker.setOnClickListener(view -> {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT,Constants.Url+stickerPack.name+"/"+stickerPack.trayImageFile);
            startActivity(Intent.createChooser(shareIntent, "Share intent.."));
        });

        btnAddToWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction("com.whatsapp.intent.action.ENABLE_STICKER_PACK");
                intent.putExtra(CommunityFragment.EXTRA_STICKER_PACK_ID, stickerPack.identifier);
                intent.putExtra(CommunityFragment.EXTRA_STICKER_PACK_AUTHORITY, BuildConfig.CONTENT_PROVIDER_AUTHORITY);
                intent.putExtra(CommunityFragment.EXTRA_STICKER_PACK_NAME, stickerPack.name);
                try {
                    startActivityForResult(intent, ADD_PACK);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(StickerDetailsActivity.this, "error", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
