package com.example.assignment4.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;
import kotlinx.coroutines.flow.Flow;

@Dao
public interface FunFactDao {
    @Query("SELECT * FROM fun_facts WHERE text LIKE '%' || :keyword || '%' ORDER BY fetchedAt DESC")
    Flow<List<FunFact>> observeFacts(String keyword);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(FunFact fact);
}
