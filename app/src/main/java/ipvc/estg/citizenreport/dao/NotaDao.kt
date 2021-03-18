package ipvc.estg.citizenreport.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ipvc.estg.citizenreport.entities.Nota

@Dao
interface NotaDao {

    @Query("SELECT * FROM nota_table")
    fun getAllNotas(): LiveData<List<Nota>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(nota: Nota)

    @Query("DELETE FROM nota_table")
    suspend fun deleteAll()

    @Update
    suspend fun updateNota(nota: Nota)
}