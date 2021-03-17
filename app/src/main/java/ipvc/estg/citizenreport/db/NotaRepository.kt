package ipvc.estg.citizenreport.db

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import ipvc.estg.citizenreport.dao.NotaDao
import ipvc.estg.citizenreport.entities.Nota

class NotaRepository(private val notaDao: NotaDao) {

    val allNotas: LiveData<List<Nota>> = notaDao.getAllNotas()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread


    suspend fun insert(nota: Nota) {
        notaDao.insert(nota)
    }

    suspend fun deleteAll(){
        notaDao.deleteAll()
    }

}