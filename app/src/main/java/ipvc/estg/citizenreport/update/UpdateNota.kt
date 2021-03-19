package ipvc.estg.citizenreport.update

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import ipvc.estg.citizenreport.R
import ipvc.estg.citizenreport.adapters.DESCRICAO
import ipvc.estg.citizenreport.adapters.TITULO
import ipvc.estg.citizenreport.entities.Nota
import ipvc.estg.citizenreport.viewModel.NotaViewModel
import kotlinx.android.synthetic.main.recyclerline.*
import java.text.DateFormat
import java.util.*

class UpdateNota : AppCompatActivity() {

    private lateinit var update_titulo: EditText
    private lateinit var update_descricao: EditText
    private lateinit var notaViewModel: NotaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)


        val titulo = intent.getStringExtra(TITULO)
        val descricao = intent.getStringExtra(DESCRICAO)


        findViewById<EditText>(R.id.update_titulo).setText(titulo)
        findViewById<EditText>(R.id.update_descricao).setText(descricao)

        notaViewModel = ViewModelProvider(this).get(NotaViewModel::class.java)

    }


    fun editarNota(view: View) {
        update_titulo = findViewById(R.id.update_titulo)
        update_descricao = findViewById(R.id.update_descricao)
        var item = intent.getIntExtra(Build.ID, 0)
        val replyIntent = Intent()
        if (TextUtils.isEmpty(update_titulo.text) || TextUtils.isEmpty(update_descricao.text))  {
            setResult(Activity.RESULT_CANCELED, replyIntent)
            Toast.makeText(applicationContext,"Não atualizada , campos vazios", Toast.LENGTH_LONG).show()
        } else {
            val nota = Nota(id = item, titulo = update_titulo.text.toString(), descricao = update_descricao.text.toString())
            notaViewModel.editNota(nota)

            Toast.makeText(applicationContext,"Atualizada com sucesso",Toast.LENGTH_LONG).show()

        }
        finish()

    }

}