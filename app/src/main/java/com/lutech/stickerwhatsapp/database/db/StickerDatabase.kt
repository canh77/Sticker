package com.lutech.stickerwhatsapp.database.db
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lutech.stickerwhatsapp.database.dao.StickerDao
import com.lutech.stickerwhatsapp.model.Sticker

//@Database(entities = [Sticker::class] , version = 1)
abstract class StickerDatabase :RoomDatabase() {
//    companion object {
//
//        @Volatile
//        private var INSTANCE: StickerDatabase? = null
//
//        fun getInstance(context: Context): StickerDatabase {
//
//            if (INSTANCE != null) return INSTANCE!!
//
//            synchronized(this) {
//
//                INSTANCE = Room
//                    .databaseBuilder(context, StickerDatabase::class.java, "STICKER_DATABASE")
//                    .allowMainThreadQueries()
//                    .fallbackToDestructiveMigration()
//                    .build()
//
//                return INSTANCE!!
//
//            }
//        }
    }

//    abstract fun stickerDao(): StickerDao
//}