package ipvc.estg.citizenreport

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun textView_05(view: View) {
        val intent = Intent(this, Registo::class.java)
        startActivity(intent)
    }
}