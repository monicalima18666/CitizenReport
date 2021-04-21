package ipvc.estg.citizenreport

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ipvc.estg.citizenreport.adapters.UserAdapter
import ipvc.estg.citizenreport.api.EndPoints
import ipvc.estg.citizenreport.api.ServiceBuilder
import ipvc.estg.citizenreport.api.User
import kotlinx.android.synthetic.main.activity_map.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class map : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewmap)
        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getUsers()

        call.enqueue(object : Callback<List<User>>{
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful){
                    recyclerView.apply {
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(this@map)
                        adapter = UserAdapter(response.body()!!)
                    }
                }
            }
            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Toast.makeText(this@map, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
