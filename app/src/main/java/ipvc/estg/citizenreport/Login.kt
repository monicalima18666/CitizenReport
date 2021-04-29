package ipvc.estg.citizenreport

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import ipvc.estg.citizenreport.api.EndPoints
import ipvc.estg.citizenreport.api.OutputPost
import ipvc.estg.citizenreport.api.ServiceBuilder
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)





    }

    fun login(view: View) {
        val intent = Intent(this, MapsActivity::class.java)
        val user = findViewById<EditText>(R.id.insertUser)
        val pass = findViewById<EditText>(R.id.insertPassword)

        Log.d("USERNAME", user.text.toString())
        Log.d("Password", pass.text.toString())
        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.login(user.text.toString(),pass.text.toString())

        val erro = getString(R.string.Error)

        // validações

        if(user.text.isNullOrEmpty() || pass.text.isNullOrEmpty()){

            if(user.text.isNullOrEmpty() && !pass.text.isNullOrEmpty()){
                user.error = erro
            }
            if(!user.text.isNullOrEmpty() && pass.text.isNullOrEmpty()){
                pass.error = erro
            }
            if(user.text.isNullOrEmpty() && pass.text.isNullOrEmpty()){
                user.error = erro
                pass.error = erro
            }
        }


        call.enqueue(object : Callback<OutputPost> {
                override fun onResponse(call: Call<OutputPost>, response: Response<OutputPost>) {
                    if (response.isSuccessful){
                        val c: OutputPost = response.body()!!
                        Log.d("STATUS", c.status.toString())
                        //Confirmação login
                        if(c.status){
                            startActivity(intent)

                            //Shared Preferences Login
                            val sharedPref: SharedPreferences = getSharedPreferences(
                                    getString(R.string.preference_login), Context.MODE_PRIVATE
                            )
                            with(sharedPref.edit()){
                                putBoolean(getString(R.string.LoginShared), true)
                                putString(getString(R.string.UsernameShared), "${c.username}")

                                commit()
                                //Log.d("****SHARED","${c.id}" )
                            }

                        }else{

                            Toast.makeText(this@Login, R.string.Error_login, Toast.LENGTH_SHORT).show()
                        }

                    }
                }

            override fun onFailure(call: Call<OutputPost>, t: Throwable) {
                /* Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()*/
            }
        })

    }

    fun map(view: View) {
        val intent = Intent(this, map::class.java)
        startActivity(intent)
    }
}





