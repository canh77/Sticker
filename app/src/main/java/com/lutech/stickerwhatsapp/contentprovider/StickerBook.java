package com.lutech.stickerwhatsapp.contentprovider;

import android.content.Context;
import android.util.Log;

import com.lutech.stickerwhatsapp.model.Sticker1;

import java.util.ArrayList;

public class StickerBook {

    static Context myContext;
    public static ArrayList<Sticker1> allStickerPacks = checkIfPacksAreNull();

    public static void init(Context context){
        myContext = context;
//        ArrayList<Sticker1> lsp = DataArchiver.readStickerPackJSON(context);
//        if(lsp!=null && lsp.size()!=0){
//            allStickerPacks = lsp;
//        }
    }



    private static ArrayList<Sticker1> checkIfPacksAreNull(){
        if(allStickerPacks==null){
            Log.w("IS PACKS NULL?", "YES");
            return new ArrayList<>();
        }
        Log.w("IS PACKS NULL?", "NO");
        return allStickerPacks;
    }


    public static void addStickerPackExisting(Sticker1 sp){
        allStickerPacks.add(sp);
    }

    public static ArrayList<Sticker1> getAllStickerPacks(){
        return allStickerPacks;
    }

    public static Sticker1 getStickerPackByName(String stickerPackName){
        for (Sticker1 sp : allStickerPacks){
            if(sp.getTitle().equals(stickerPackName)){
                return sp;
            }
        }
        return null;
    }

    public static Sticker1 getStickerPackById(String stickerPackId){
        if(allStickerPacks.isEmpty()){
            init(myContext);
        }
        Log.w("THIS IS THE ALL STICKER", allStickerPacks.toString());
        for (Sticker1 sp : allStickerPacks){
            if(sp.getTitle().equals(stickerPackId)){
                return sp;
            }
        }
        return null;
    }

    public static Sticker1 getStickerPackByIdWithContext(String stickerPackId, Context context){
        if(allStickerPacks.isEmpty()){
            init(context);
        }
        Log.w("THIS IS THE ALL STICKER", allStickerPacks.toString());
        for (Sticker1 sp : allStickerPacks){
            if(sp.getTitle().equals(stickerPackId)){
                return sp;
            }
        }
        return null;
    }

    public static void deleteStickerPackById(String stickerPackId){
        Sticker1 myStickerPack = getStickerPackById(stickerPackId);
//        new File(myStickerPack.tray_icon.getPath()).getParentFile().delete();
        allStickerPacks.remove(myStickerPack);
    }

    public static Sticker1 getStickerPackByIndex(int index){
        return allStickerPacks.get(index);
    }
}
