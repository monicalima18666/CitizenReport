package ipvc.estg.citizenreport

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import ipvc.estg.citizenreport.api.*
import kotlinx.android.synthetic.main.activity_new_report.*
import kotlinx.android.synthetic.main.recyclerline.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.Base64;
import java.util.Base64.getEncoder


private const val REQUEST_CODE = 42
private val IMAGE_PICK_CODE=1000;
private val PERMISSION_CODE=1001;

// add to implement last known location
private lateinit var lastLocation: Location

//added to implement location periodic updates
private lateinit var locationCallback: LocationCallback
private lateinit var locationRequest: LocationRequest

private const val LOCATION_PERMISSION_REQUEST_CODE = 1


class NewReportActivity : AppCompatActivity() {
    private var latitude:Double = 0.0
    private var longitude:Double = 0.0

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_report)


        val sharedPref: SharedPreferences = getSharedPreferences(
                getString(R.string.preference_login), Context.MODE_PRIVATE
                            )
        val users_id = sharedPref.getInt("id_login", 0)


        // initialize fusedLocationClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        checkPermissions()

        createLocationRequest()

        photo_btn.setOnClickListener{
            val tirarPhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            if(tirarPhotoIntent.resolveActivity(this.packageManager) != null){
                startActivityForResult(tirarPhotoIntent, REQUEST_CODE)
            }else{
                Toast.makeText(this@NewReportActivity, getString(R.string.erro), Toast.LENGTH_SHORT).show()
            }
        }

        galeria_btn.setOnClickListener{
            //permissoes
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                    //permissao negada
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                    requestPermissions(permissions, PERMISSION_CODE)
                }else{
                    //ja tem permissoes
                    pickImageGallery()
                }
            }else{
                //SO baixo
                pickImageGallery()
            }
        }

        add_marker.setOnClickListener{

            val tipoProblema = findViewById<RadioGroup>(R.id.radioGroup_insert).checkedRadioButtonId

            val descricao = findViewById<EditText>(R.id.descricao_insertMarker)
            val titulo = findViewById<EditText>(R.id.insertTitulo)

            if(tipoProblema == -1 ||TextUtils.isEmpty(descricao.text)){
                Toast.makeText(this, getString(R.string.preenchacampos) , Toast.LENGTH_LONG).show()

            }else{
                var tipo_id:Int =0
                val request = ServiceBuilder.buildService(EndPoints::class.java)

                //texto do radiobutton

                val tipo = findViewById<RadioGroup>(R.id.radioGroup_insert)
                var tt =  findViewById<RadioButton>(tipoProblema)
                var radioButtonTexto =  findViewById<RadioButton>(tipoProblema).getText().toString();

                if(findViewById<RadioButton>(R.id.radioButton).isChecked){
                    tipo_id=1
                }else if(findViewById<RadioButton>(R.id.radioButton2).isChecked){
                    tipo_id=2
                }else{
                    tipo_id=3
                }


                Log.d("***", "tipoproblema: $tipo_id")

//
                val image = findViewById<ImageView>(R.id.imagem)
                val bitmap = image.drawable.toBitmap()
                val byteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream)
                val encodedImage = getEncoder().encodeToString(byteArrayOutputStream.toByteArray())


                val call = request.adicionarReport( latitude , longitude ,  descricao.text.toString()  ,encodedImage, users_id , titulo.text.toString(),  tipo_id)

                Log.d("***", "lat: $latitude")
                Log.d("***", "log: $longitude")
                Log.d("***", "imagem: $encodedImage")
                Log.d("***", "idtipo: $tipo_id")
                Log.d("***", "descricao: ${descricao.text.toString()}")
                Log.d("***", "id: $users_id")
                Log.d("***", "titulo: ${titulo.text.toString()}")

                call.enqueue(object : Callback<OutputReports> {
                    override fun onResponse(call: Call<OutputReports>, response: Response<OutputReports>) {
                        if (response.isSuccessful) {
                            Log.d("***", "funcionou insert")
                            val intent = Intent(applicationContext, MapsActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }

                    override fun onFailure(call: Call<OutputReports>, t: Throwable) {
                        //Toast.makeText(applicationContext, "${t.message}", Toast.LENGTH_SHORT).show()
                        Log.d("***", "ErrorOccur:  ${t.message}, ${call}"  )


                    }
                })
            }
        }
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest()
        // interval specifies the rate at which your app will like to receive updates.
        locationRequest.interval = 10000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }


    private fun checkPermissions(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION), 1)
            return
        }else{
            getLocations()
        }
    }

    private fun getLocations() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        fusedLocationClient.lastLocation?.addOnSuccessListener {

            location : Location? ->
            if (location != null) {
                latitude = location.latitude
                longitude = location.longitude
            }
        }
    }


    private fun pickImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            PERMISSION_CODE -> {
                if(grantResults.size >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    pickImageGallery()
                } else{
                    Toast.makeText(this, getString(R.string.naopermitiu) , Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
            val takenImage = data?.extras?.get("data") as Bitmap
            imagem.setImageBitmap(takenImage)
        }
        else if( requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK ){
            imagem.setImageURI(data?.data)
        }
        else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onBackPressed() {
        //nothing
        Toast.makeText(this@NewReportActivity, R.string.Insert, Toast.LENGTH_SHORT).show()
    }


}