package ipvc.estg.citizenreport

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import ipvc.estg.citizenreport.api.EndPoints
import ipvc.estg.citizenreport.api.OutputPost
import ipvc.estg.citizenreport.api.ServiceBuilder
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity(), SensorEventListener {

    private lateinit var txtAcelerometro: TextView
    private lateinit var sensorManager: SensorManager

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Sensor Acelerómetro
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        AppCompatDelegate.setDefaultNightMode((AppCompatDelegate.MODE_NIGHT_NO))
        txtAcelerometro = findViewById(R.id.txtAcelerometro)
        setUpSensorStuff()
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun setUpSensorStuff() {
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also {

            sensorManager.registerListener(this,
                    it,
                    SensorManager.SENSOR_DELAY_FASTEST,
                    SensorManager.SENSOR_DELAY_FASTEST)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        //Sensor de Acelerómetro
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val sides = event.values[0]
            val upDown = event.values[1]

            txtAcelerometro.apply {
                rotationX = upDown * 3f
                rotationY = sides * 3f
                rotation = -sides
                translationX = sides * -10
                translationY = upDown * -10
            }

            val color = if (upDown.toInt() == 0 && sides.toInt() == 0) Color.TRANSPARENT else Color.CYAN
            txtAcelerometro.setBackgroundColor(color)

            txtAcelerometro.text = getString(R.string.caima_baixo) + " " + upDown.toInt() + "\n" + getString(R.string.esquerda_direita) + " " + sides.toInt()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onResume() {
        super.onResume()
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

    }


    override fun onPause() {
        super.onPause()
        sensorManager!!.unregisterListener(this)
    }


    fun login(view: View) {
        val intent = Intent(this, MapsActivity::class.java)
        val user = findViewById<EditText>(R.id.insertUser)
        val pass = findViewById<EditText>(R.id.insertPassword)

        Log.d("USERNAME", user.text.toString())
        Log.d("Password", pass.text.toString())

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.login(user.text.toString(), pass.text.toString())

        val erro = getString(R.string.Error)

        // Validações

        if (user.text.isNullOrEmpty() || pass.text.isNullOrEmpty()) {

            if (user.text.isNullOrEmpty() && !pass.text.isNullOrEmpty()) {
                user.error = erro
            }
            if (!user.text.isNullOrEmpty() && pass.text.isNullOrEmpty()) {
                pass.error = erro
            }
            if (user.text.isNullOrEmpty() && pass.text.isNullOrEmpty()) {
                user.error = erro
                pass.error = erro
            }
        }


        call.enqueue(object : Callback<OutputPost> {
            override fun onResponse(call: Call<OutputPost>, response: Response<OutputPost>) {
                if (response.isSuccessful) {
                    val c: OutputPost = response.body()!!
                    Log.d("STATUS", c.status.toString())


                    //Confirmação login (atraves de status = true)
                    if (c.status) {
                        startActivity(intent)
                        finish()

                        //Shared Preferences Login
                        val sharedPref: SharedPreferences = getSharedPreferences(
                                getString(R.string.preference_login), Context.MODE_PRIVATE
                        )
                        with(sharedPref.edit()) {
                            putBoolean(getString(R.string.LoginShared), true)
                            putString(getString(R.string.UsernameShared), "${c.username}")
                            putInt("id_login", c.id)

                            commit()

                        }
                        //status = false
                    } else {

                        Toast.makeText(this@Login, R.string.Error_login, Toast.LENGTH_SHORT).show()
                    }

                }
            }

            override fun onFailure(call: Call<OutputPost>, t: Throwable) {
                /* Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()*/
            }
        })


        //teste

    }
}





