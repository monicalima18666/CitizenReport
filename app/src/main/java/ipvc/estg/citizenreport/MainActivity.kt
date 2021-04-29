package ipvc.estg.citizenreport

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val sharedPref: SharedPreferences = getSharedPreferences(
                getString(R.string.preference_login), Context.MODE_PRIVATE
        )
        if (sharedPref != null){
            if(sharedPref.all[getString(R.string.LoginShared)]==true){
                var intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)

            }
        }
    }

    fun button2(view: View) {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
    }
    fun buttonnotas(view: View) {
        val intent = Intent(this, Notas::class.java)
        startActivity(intent)
    }


    //ola monica
}