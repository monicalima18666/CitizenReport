package ipvc.estg.citizenreport

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import ipvc.estg.citizenreport.api.EndPoints
import ipvc.estg.citizenreport.api.OutputPost
import ipvc.estg.citizenreport.api.Reports
import ipvc.estg.citizenreport.api.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var reports: List<Reports>
    private lateinit var lastLocation: Location
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var minhaLocalizacao:LatLng

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // initialize fusedLocationClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        //added to implement location periodic updates
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                lastLocation = p0.lastLocation
                minhaLocalizacao = LatLng(lastLocation.latitude, lastLocation.longitude)
            }
        }
        createLocationRequest()
    }

  fun butaoCriarReport(view: View) {
        //Log.d("***", "locccc: ${minhaLocalizacao.longitude}")
        val intent = Intent(this, NewReportActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest()
        // interval specifies the rate at which your app will like to receive updates.
        locationRequest.interval = 10000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null /* Looper */)
    }


    fun calculateDistance(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Float {
        val results = FloatArray(1)
        Location.distanceBetween(lat1, lng1, lat2, lng2, results)
        // distance in meter
        return results[0]
    }

    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
        Log.d("***", "onPause - removeLocationUpdates")
    }

    public override fun onResume() {
        super.onResume()
        startLocationUpdates()
        Log.d("***", "onResume - startLocationUpdates")
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val area = LatLng(41.6946, -8.83016)
        val zoomLevel = 15f

        /* mMap.moveCamera(CameraUpdateFactory.newLatLng(zone))*/
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(area, zoomLevel))


        val sharedPref: SharedPreferences = getSharedPreferences(
            getString(R.string.preference_login), Context.MODE_PRIVATE
        )
        val users_id = sharedPref.getInt("id_login", 0)


        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getReports()
        var position: LatLng

        setUpMapa()
        // checkPermissions()


        //ALTERAÇÃO DA COR DO MARKER DO USER QUE ESTA LOGADO


        call.enqueue(object : Callback<List<Reports>>{
            override fun onResponse(call: Call<List<Reports>>, response: Response<List<Reports>>) {
                if (response.isSuccessful){
                    reports = response.body()!!
                    for(report in reports){
                        position = LatLng(report.latitude,report.longitude)
                        if (report.users_id == users_id){

                            mMap.addMarker(MarkerOptions()
                                .position(position)
                                .title(report.titulo)
                                .snippet(report.descricao)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))

                            )
                        }else {
                            mMap.addMarker(
                                MarkerOptions()
                                    .position(position)
                                    .title(report.titulo)
                                    .snippet(report.descricao)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                            )
                        }
                    }
                }
            }
            override fun onFailure(call: Call<List<Reports>>, t: Throwable) {
                Toast.makeText(this@MapsActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })

        val tipo = findViewById<RadioGroup>(R.id.radioGroup_filter)
        tipo.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { group, checkedId ->
                val radio: RadioButton = findViewById(checkedId)
                Log.d("****", "$radio")

                if(radio.text.equals(getString(R.string.acidente))){
                    mMap.clear()
                    val callTipo = request.getReportByTipo(1)

                    callTipo.enqueue(object : Callback<List<Reports>> {
                        override fun onResponse(call: Call<List<Reports>>, response: Response<List<Reports>>) {
                            if (response.isSuccessful) {
                               reports = response.body()!!

                                for(report in reports){
                                    if(report.users_id == users_id) {
                                        position = LatLng(report.latitude,report.longitude)
                                        val reports = mMap.addMarker(MarkerOptions()
                                            .position(position)
                                            .title(report.id.toString())
                                            .snippet(report.users_id.toString())
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                                    }else{
                                        position = LatLng(report.latitude,report.longitude)
                                        val reports =   mMap.addMarker(MarkerOptions()
                                            .position(position)
                                            .title(report.id.toString())
                                            .snippet(report.users_id.toString()));
                                    }
                                }
                            }
                        }

                        override fun onFailure(call: Call<List<Reports>>, t: Throwable) {
                            Log.d("***", "hhh")
                        }
                    })

                }else if(radio.text.equals(getString(R.string.problema))){
                    mMap.clear()
                    val callTipo = request.getReportByTipo(2)

                    callTipo.enqueue(object : Callback<List<Reports>> {
                        override fun onResponse(call: Call<List<Reports>>, response: Response<List<Reports>>) {
                            if (response.isSuccessful) {

                                reports = response.body()!!

                                Log.d("***", "pontos Acidente: $reports")

                                for(report in reports){
                                    if(report.users_id == users_id) {
                                        position = LatLng(report.latitude,report.longitude)
                                        val reports = mMap.addMarker(MarkerOptions()
                                            .position(position)
                                            .title(report.id.toString())
                                            .snippet(report.users_id.toString())
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                                    }else{
                                        position = LatLng(report.latitude,report.longitude)
                                        val reports =   mMap.addMarker(MarkerOptions()
                                            .position(position)
                                            .title(report.id.toString())
                                            .snippet(report.users_id.toString()));
                                    }
                                }
                            }
                        }

                        override fun onFailure(call: Call<List<Reports>>, t: Throwable) {
                            Log.d("***", "hhh")
                        }
                    })

                }else if(radio.text.equals(getString(R.string.obras))){
                    mMap.clear()
                    val callTipo = request.getReportByTipo(3)

                    callTipo.enqueue(object : Callback<List<Reports>> {
                        override fun onResponse(call: Call<List<Reports>>, response: Response<List<Reports>>) {
                            if (response.isSuccessful) {

                                reports = response.body()!!

                                Log.d("***", "pontos Acidente: $reports")

                                for(report in reports){
                                    if(report.users_id == users_id) {
                                        position = LatLng(report.latitude,report.longitude)
                                        val reports = mMap.addMarker(MarkerOptions()
                                            .position(position)
                                            .title(report.id.toString())
                                            .snippet(report.users_id.toString())
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                                    }else{
                                        position = LatLng(report.latitude,report.longitude)
                                        val reports =   mMap.addMarker(MarkerOptions()
                                            .position(position)
                                            .title(report.id.toString())
                                            .snippet(report.users_id.toString()));
                                    }
                                }
                            }
                        }

                        override fun onFailure(call: Call<List<Reports>>, t: Throwable) {
                            Log.d("***", "hhh")
                        }
                    })

                }else{
                    val callTudo = request.getReports()
                    mMap.clear()
                    callTudo.enqueue(object : Callback<List<Reports>> {
                        override fun onResponse(call: Call<List<Reports>>, response: Response<List<Reports>>) {
                            if (response.isSuccessful) {

                                reports = response.body()!!

                                Log.d("***", "pontos Acidente: $reports")

                                for(report in reports){
                                    if(report.users_id == users_id) {
                                        position = LatLng(report.latitude,report.longitude)
                                        val reports = mMap.addMarker(MarkerOptions()
                                            .position(position)
                                            .title(report.id.toString())
                                            .snippet(report.users_id.toString())
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
                                    }else{
                                        position = LatLng(report.latitude,report.longitude)
                                        val reports =   mMap.addMarker(MarkerOptions()
                                            .position(position)
                                            .title(report.id.toString())
                                            .snippet(report.users_id.toString()));
                                    }
                                }
                            }
                        }

                        override fun onFailure(call: Call<List<Reports>>, t: Throwable) {
                            Log.d("***", "hhh")
                        }
                    })

                }
            })
    }


    fun logout(view: View) {

        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.Logout)
       // builder.setMessage(R.string.LogoutMessage)

        //builder.setIcon(R.drawable.ic_exit_to_app_black_24dp)
       // builder.setPositiveButton(R.string.Yes) { dialog: DialogInterface?, which: Int ->
            //Fab
            val sharedPref: SharedPreferences = getSharedPreferences(
                getString(R.string.preference_login), Context.MODE_PRIVATE
            )
            with(sharedPref.edit()){
                putBoolean(getString(R.string.LoginShared), false)
                putString(getString(R.string.UsernameShared), "")
                commit()
            }
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()

        }

       // builder.setNegativeButton(R.string.No) { dialog: DialogInterface?, which: Int ->}
       // builder.show()


    //}
/*
    override fun onMarkerClick(p0: Marker?): Boolean {
        TODO("Not yet implemented")
    }*/


    companion object {
        // add to implement last known location
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        //added to implement location periodic updates
        private const val REQUEST_CHECK_SETTINGS = 2
    }


    private fun setUpMapa() {
        //   val confirmar = false
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE)
        }else{
            mMap.isMyLocationEnabled=true
        }
    }

    override fun onBackPressed() {
        //nothing
        Toast.makeText(this@MapsActivity, R.string.Back, Toast.LENGTH_SHORT).show()
    }



    // teste git
}