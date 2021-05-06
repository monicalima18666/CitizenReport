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


    @GET("/myslim/api/report")
    fun getReports(): Call<List<Reports>>


    @GET("/myslim/api/report/{id}")
    fun getReportById(@Path("id") id: Int): Call<Reports>

    @GET("/myslim/api/reportbytipo/{id}")
    fun getReportByTipo(@Path("id") id: Int): Call<List<Reports>>


    @FormUrlEncoded
    @POST("/myslim//api/inserir_report")
    fun adicionarReport(@Field("latitude") latitude: Double,
                       @Field("longitude") longitude: Double,
                        @Field("descricao") descricao: String,
                        @Field("imagem") imagem: String,
                        @Field("users_id") users_id: Int,
                        @Field("titulo") titulo: String,
                        @Field("id_tipo") id_tipo: Int
                       ): Call<OutputReports>

    @POST("/myslim/api/deletereport/{id}")
    fun eliminarReport(@Path("id") id:Int): Call<OutputEliminar>

    @FormUrlEncoded
    @POST("/myslim/api/updatereport/{id}")
    fun updateReport(@Path("id") id: Int?,
                    @Field("id_tipo") id_tipo: Int?,
                    @Field("descricao") descricao: String?,
                    @Field("imagem") imagem: String?
    ): Call<OutputEliminar>

}