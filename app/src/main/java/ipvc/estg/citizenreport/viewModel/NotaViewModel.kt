package ipvc.estg.citizenreport.viewModel

import android.app.Application
import androidx.lifecycle.*
//import ipvc.estg.citizenreport.db.NotaDB
import ipvc.estg.citizenreport.db.NotaRepository
import ipvc.estg.citizenreport.entities.Nota
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/*
class NotaViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NotaRepository

    val allNotas: LiveData<List<Nota>>

    init {
        val notasDao = NotaDB.getDatabase(application, viewModelScope).notaDao()
        repository = NotaRepository(notasDao)
        allNotas = repository.allNotas
    }


    fun insert(nota: Nota) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(nota)
    }

    // delete all
    fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAll()
    }

}*/
