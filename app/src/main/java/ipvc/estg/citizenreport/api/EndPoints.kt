package ipvc.estg.citizenreport.api

import retrofit2.Call
import retrofit2.http.*

interface EndPoints {

    @GET("/myslim/api/utilizador")
    fun getUsers(): Call<List<User>>

    @GET("/utilizador/{id}")
    fun getUserById(@Path("id") id: Int): Call<User>
}