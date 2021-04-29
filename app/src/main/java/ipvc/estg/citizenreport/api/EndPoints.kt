package ipvc.estg.citizenreport.api

import retrofit2.Call
import retrofit2.http.*

interface EndPoints {

    @GET("/myslim/api/users")
    fun getUsers(): Call<List<User>>

    @GET("/users/{id}")
    fun getUserById(@Path("id") id: Int): Call<User>

    @FormUrlEncoded
    @POST("/myslim/api/user")
    fun login(@Field("username") first: String?,@Field("password") second: String?): Call<OutputPost>

}