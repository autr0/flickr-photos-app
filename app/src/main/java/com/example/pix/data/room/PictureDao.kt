package com.example.pix.data.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pix.data.room.model.PictureDbo

@Dao
interface PictureDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<PictureDbo>)

    @Query("delete from pictures")
    suspend fun clearAll()

    @Query("select * from pictures")
    fun pagingSource(): PagingSource<Int, PictureDbo>

}