package ipvc.estg.citizenreport

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

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

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var reports: List<Reports>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        val sharedPref: SharedPreferences = getSharedPreferences(
                getString(R.string.preference_login), Context.MODE_PRIVATE
        )


        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getReports()
        var position: LatLng


        //ALTERAÇÃO DA COR DO MARKER DO USER QUE ESTA LOGADO


        call.enqueue(object : Callback<List<Reports>>{
            override fun onResponse(call: Call<List<Reports>>, response: Response<List<Reports>>) {
                if (response.isSuccessful){
                    reports = response.body()!!
                    for(report in reports){
                        position = LatLng(report.latitude,report.longitude)
                        if (report.users_id.equals(sharedPref.all[getString(R.string.Id_Login)])){

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
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                            )
                        }
                    }
                }
            }
            override fun onFailure(call: Call<List<Reports>>, t: Throwable) {
                Toast.makeText(this@MapsActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })



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
        val zone = LatLng(41.6946, -8.83016)
        val zoomLevel = 15f

        /* mMap.moveCamera(CameraUpdateFactory.newLatLng(zone))*/
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(zone, zoomLevel))

    }


    fun logout(view: View) {

        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.Logout)
        builder.setMessage(R.string.LogoutMessage)
        builder.setIcon(R.drawable.ic_exit_to_app_black_24dp)
        builder.setPositiveButton(R.string.Yes) { dialog: DialogInterface?, which: Int ->
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
        }

        builder.setNegativeButton(R.string.No) { dialog: DialogInterface?, which: Int ->}
        builder.show()


    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        TODO("Not yet implemented")
    }

    override fun onBackPressed() {
        //nothing
        Toast.makeText(this@MapsActivity, R.string.Back, Toast.LENGTH_SHORT).show()
    }
}