package com.lutech.stickerwhatsapp.adapter;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.lutech.stickerwhatsapp.R;
import com.lutech.stickerwhatsapp.fragment.CommunityFragment;
import com.lutech.stickerwhatsapp.activity.StickerDetailsActivity;
import com.lutech.stickerwhatsapp.model.Sticker;
import com.lutech.stickerwhatsapp.model.StickerPack;
import com.lutech.stickerwhatsapp.utils.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.ViewHolder> {

    Context context;
    ArrayList<StickerPack> StickerPack;

    public StickerAdapter(Context context, ArrayList<StickerPack> StickerPack) {
        this.context = context;
        this.StickerPack = StickerPack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(com.lutech.stickerwhatsapp.R.layout.list_item_stickers, viewGroup, false);
        return new ViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        final List<Sticker> models = StickerPack.get(i).getStickers();
        viewHolder.tvTitleSticker.setText(StickerPack.get(i).name);
        viewHolder.tvTitleDetailSticker.setText(StickerPack.get(i).publisher);
        viewHolder.tvTitleDetailSticker.setText(StickerPack.get(i).publisher);
        viewHolder.tvNumberDownload.setText(StickerPack.get(i).publisherEmail);
        int totalImages = models.size() - 5;
        viewHolder.tv_total_image.setText("+" + totalImages);
        Glide.with(context).load(Constants.Url + StickerPack.get(i).name + "/" + StickerPack.get(i).trayImageFile.replace("_", "_")).into(viewHolder.iv_sticker_tray);
        Glide.with(context).load(Constants.Url + StickerPack.get(i).name + "/" + models.get(i).imageFileName.replace(".webp", ".png")).into(viewHolder.imone);
        Glide.with(context).load(Constants.Url + StickerPack.get(i).name + "/" + models.get(1).imageFileName.replace(".webp", ".png")).into(viewHolder.imtwo);
        Glide.with(context).load(Constants.Url + StickerPack.get(i).name + "/" + models.get(2).imageFileName.replace(".webp", ".png")).into(viewHolder.imthree);
        Glide.with(context).load(Constants.Url + StickerPack.get(i).name + "/" + models.get(3).imageFileName.replace(".webp", ".png")).into(viewHolder.imfour);
        Glide.with(context).load(Constants.Url + StickerPack.get(i).name + "/" + models.get(4).imageFileName.replace(".webp", ".png")).into(viewHolder.imfive);
        Glide.with(context).load(Constants.Url + StickerPack.get(i).name + "/" + models.get(5).imageFileName.replace(".webp", ".png")).into(viewHolder.imsix);
        Glide.with(context).load(Constants.Url + StickerPack.get(i).name + "/" + models.get(6).imageFileName.replace(".webp", ".png")).into(viewHolder.ivseven);
        Glide.with(context).load(Constants.Url + StickerPack.get(i).name + "/" + models.get(7).imageFileName.replace(".webp", ".png")).into(viewHolder.iveight);


        Glide.with(context)
                .asBitmap()
                .load(Constants.Url + StickerPack.get(i).name + "/" + StickerPack.get(i).trayImageFile.replace("_", "_"))
                .addListener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        Bitmap bitmap1 = Bitmap.createBitmap(96, 96, Bitmap.Config.ARGB_8888);
                        Matrix matrix = new Matrix();
                        Canvas canvas = new Canvas(bitmap1);
                        canvas.drawColor(Color.TRANSPARENT);
                        matrix.postTranslate(canvas.getWidth() / 2 - resource.getWidth() / 2, canvas.getHeight() / 2 - resource.getHeight() / 2);
                        canvas.drawBitmap(resource, matrix, null);
                        CommunityFragment.SaveTryImage(bitmap1, StickerPack.get(i).trayImageFile, StickerPack.get(i).identifier);
                        return false;
                    }
                })
                .submit();

        viewHolder.ct_item_sticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, StickerDetailsActivity.class)
                                .putExtra(CommunityFragment.EXTRA_STICKERPACK, StickerPack.get(viewHolder.getAdapterPosition())),
                        ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight()).toBundle());

                ((Activity) context).runOnUiThread(
                        new Runnable() {
                            @Override
                            public void run() {
                                for (final Sticker s : StickerPack.get(viewHolder.getAdapterPosition()).getStickers()) {
                                    Log.d("111111102215", "onClick: " + s.imageFileName);
                                    Glide.with(context)
                                            .asBitmap()
                                            .apply(new RequestOptions().override(512, 512))
                                            .load(Constants.Url + StickerPack.get(i).name + "/" + s.imageFileName.replace(".webp", ".png"))
                                            .addListener(new RequestListener<Bitmap>() {
                                                @Override
                                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                                    return false;
                                                }

                                                @Override
                                                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                                    Bitmap bitmap1 = Bitmap.createBitmap(512, 512, Bitmap.Config.ARGB_8888);
                                                    Matrix matrix = new Matrix();
                                                    Canvas canvas = new Canvas(bitmap1);
                                                    canvas.drawColor(Color.TRANSPARENT);
                                                    matrix.postTranslate(
                                                            canvas.getWidth() / 2 - resource.getWidth() / 2,
                                                            canvas.getHeight() / 2 - resource.getHeight() / 2
                                                    );
                                                    canvas.drawBitmap(resource, matrix, null);
                                                    CommunityFragment.SaveImage(bitmap1, s.imageFileName, StickerPack.get(viewHolder.getAdapterPosition()).identifier);
                                                    return true;
                                                }
                                            }).submit();
                                    Log.d("30303003030", "run: " + Constants.Url + StickerPack.get(i).name + "/" + s.imageFileName.replace(".webp", ".png"));
                                }
                            }
                        }
                );

                File file = new File(CommunityFragment.path + "/" + StickerPack.get(i).identifier + "/" + models.get(0).imageFileName);
                Log.d("1111111022152", "onClick: " + CommunityFragment.path + "/" + StickerPack.get(i).identifier + "/" + models.get(0).imageFileName);
                if (!file.exists()) {
                    ((Activity) context).runOnUiThread(
                            new Runnable() {
                                @Override
                                public void run() {
                                    for (final Sticker s : StickerPack.get(viewHolder.getAdapterPosition()).getStickers()) {
                                        Log.d("adapter", "onClick: " + s.imageFileName);
                                        Glide.with(context)
                                                .asBitmap()
                                                .apply(new RequestOptions().override(512, 512))
                                                .load(Constants.Url + s.imageFileName.replace(".webp", ".png"))
                                                .addListener(new RequestListener<Bitmap>() {
                                                    @Override
                                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                                        return false;
                                                    }

                                                    @Override
                                                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                                        Bitmap bitmap1 = Bitmap.createBitmap(512, 512, Bitmap.Config.ARGB_8888);
                                                        Matrix matrix = new Matrix();
                                                        Canvas canvas = new Canvas(bitmap1);
                                                        canvas.drawColor(Color.TRANSPARENT);
                                                        matrix.postTranslate(
                                                                canvas.getWidth() / 2 - resource.getWidth() / 2,
                                                                canvas.getHeight() / 2 - resource.getHeight() / 2
                                                        );
                                                        canvas.drawBitmap(resource, matrix, null);
                                                        CommunityFragment.SaveImage(bitmap1, s.imageFileName, StickerPack.get(viewHolder.getAdapterPosition()).identifier);
                                                        return true;
                                                    }
                                                }).submit();
                                    }
                                }
                            }
                    );
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return StickerPack.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imone, imtwo, imthree, imfour, imfive, imsix, ivseven, iveight, iv_sticker_tray;
        ConstraintLayout ct_item_sticker;
        TextView tv_total_image, tvTitleSticker, tvTitleDetailSticker, tvNumberDownload;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imone = itemView.findViewById(R.id.sticker_one);
            imtwo = itemView.findViewById(R.id.sticker_two);
            imthree = itemView.findViewById(R.id.sticker_three);
            imfour = itemView.findViewById(R.id.sticker_four);
            imfive = itemView.findViewById(R.id.sticker_five);
            imsix = itemView.findViewById(R.id.sticker_six);
            ivseven = itemView.findViewById(R.id.sticker_sevens);
            iveight = itemView.findViewById(R.id.sticker_eights);
            ct_item_sticker = itemView.findViewById(R.id.ct_item_sticker);
            tv_total_image = itemView.findViewById(R.id.tv_total_image);
            iv_sticker_tray = itemView.findViewById(R.id.iv_sticker_tray);
            tvTitleSticker = itemView.findViewById(R.id.tvTitleSticker);
            tvTitleDetailSticker = itemView.findViewById(R.id.tvTitleDetailSticker);
            tvNumberDownload = itemView.findViewById(R.id.tvNumberDownload);
        }
    }
}
