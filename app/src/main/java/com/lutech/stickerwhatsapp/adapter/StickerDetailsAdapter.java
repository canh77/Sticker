package com.lutech.stickerwhatsapp.adapter;

import android.content.Context;
import android.net.Uri;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.lutech.stickerwhatsapp.R;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class StickerDetailsAdapter extends RecyclerView.Adapter<StickerDetailsAdapter.ViewHolder> {

    ArrayList<String> strings;
    Context context;

    public StickerDetailsAdapter(ArrayList<String> strings, Context context) {
        this.strings = strings;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_sticker_details, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        InputStream iStream = null;
        viewHolder.tvNumberImage.setText(String.valueOf(i + 1));
        try {
            if (strings.get(i).endsWith(".jpg")) {
                Glide.with(context)
                        .asBitmap()
                        .load(Uri.parse(strings.get(i)))
                        .apply(new RequestOptions().override(512, 512))
                        .into(viewHolder.iv_Sticker_Details);
                Log.d("1111000000", "jpg: " + Uri.parse(strings.get(i)));
            } else {
                if (!strings.get(i).endsWith(".png")) {
                    iStream = new FileInputStream(strings.get(i));
                    Glide.with(context)
                            .asBitmap()
                            .load(getBytes(iStream))
                            .into(viewHolder.iv_Sticker_Details);
                    Log.d("1111000000", "png: " + getBytes(iStream));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return strings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_Sticker_Details;
        private TextView tvNumberImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_Sticker_Details = itemView.findViewById(R.id.iv_Sticker_Details);
            tvNumberImage = itemView.findViewById(R.id.tvNumberImage);
        }
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
}
