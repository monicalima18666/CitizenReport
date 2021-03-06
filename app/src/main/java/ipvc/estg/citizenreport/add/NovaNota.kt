package ipvc.estg.citizenreport.add

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import ipvc.estg.citizenreport.R

class NovaNota : AppCompatActivity() {
    private lateinit var tituloText: EditText
    private lateinit var descricaoText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nova_nota)

        tituloText = findViewById(R.id.edit_titulo)
        descricaoText = findViewById(R.id.edit_descricao)


        if (intent.getStringExtra(EXTRA_REPLY_TITULO).isNullOrEmpty() && intent.getStringExtra(EXTRA_REPLY_DESCRICAO).isNullOrEmpty()) {

            val button = findViewById<Button>(R.id.button_save)
            button.setOnClickListener {

                if (TextUtils.isEmpty((tituloText.text)) || TextUtils.isEmpty((descricaoText.text))) {

                    if (TextUtils.isEmpty(tituloText.text) && !TextUtils.isEmpty(descricaoText.text)) {
                        tituloText.error = getString(R.string.tituloMsg)
                    }

                    if (!TextUtils.isEmpty(descricaoText.text) && TextUtils.isEmpty(descricaoText.text)) {
                        descricaoText.error = getString(R.string.DescMsg)
                    }

                    if (TextUtils.isEmpty(tituloText.text) && TextUtils.isEmpty(descricaoText.text)) {
                        tituloText.error = getString(R.string.tituloMsg)
                        descricaoText.error = getString(R.string.DescMsg)
                    }
                } else {
                    val replyIntent = Intent()

                    replyIntent.putExtra(EXTRA_REPLY_TITULO, tituloText.text.toString())
                    replyIntent.putExtra(EXTRA_REPLY_DESCRICAO, descricaoText.text.toString())
                    setResult(Activity.RESULT_OK, replyIntent)

                    finish()
                }
            }
        }

    }

    companion object {
        const val EXTRA_REPLY_TITULO = "ipvc.estg.citizenreport.titulo"
        const val EXTRA_REPLY_DESCRICAO = "ipvc.estg.citizenreport.descricao"
    }
}