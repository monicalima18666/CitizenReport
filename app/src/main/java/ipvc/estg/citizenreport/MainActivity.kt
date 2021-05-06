package ipvc.estg.citizenreport

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.hardware.SensorEvent
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException

class MainActivity : AppCompatActivity(), SensorEventListener {

    var sensor: Sensor? = null
    var sensorDistancia: Sensor? = null
    var sensorManager: SensorManager? = null

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
                finish()
            }
        }

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_LIGHT)
        sensorDistancia = sensorManager!!.getDefaultSensor(Sensor.TYPE_PROXIMITY)
    }

    fun button2(view: View) {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
    }
    fun buttonnotas(view: View) {
        val intent = Intent(this, Notas::class.java)
        startActivity(intent)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        try{
            if (event != null) {
                if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
                    Log.d("***", "luz: ${event.values[0]}")
                    if (event!!.values[0] < 50) {
                        relativeLayoutMain.setBackgroundColor(getResources().getColor(R.color.blueClaro))
                    } else {
                        relativeLayoutMain.setBackgroundColor(getResources().getColor(R.color.white))
                    }
                }

                else if(event.sensor.getType() == Sensor.TYPE_PROXIMITY){
                    if (event!!.values[0] < sensorDistancia!!.maximumRange) {
                        imgMaoSensor.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_auto_awesome_24))
                    } else {
                        imgMaoSensor.setImageDrawable(null)
                        //imageView4.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_far_24))
                        //    Log.d("***", "longe: ${event.values[0]}")
                    }
                }
            }
        }catch(e : IOException){

        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onResume() {
        super.onResume()
        sensorManager!!.registerListener(this, sensor , SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager!!.registerListener(this, sensorDistancia , SensorManager.SENSOR_DELAY_NORMAL)
    }


    override fun onPause() {
        super.onPause()
        sensorManager!!.unregisterListener(this)
    }


    //ola monica
}