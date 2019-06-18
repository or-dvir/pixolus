package or_dvir.hotmail.com.pixolus.other

import retrofit2.Call
import retrofit2.http.*

interface IPixometerClient
{
    @POST("api-token-auth/")
    fun authenticate(@Body body: BodyAuth): Call<ResponseAuth>

    //change page size to a number larger than fits in your screen to see the "next page"
    //button showing when scrolling to bottom of recycler view
//    @GET("meters/?page_size=13")
    @GET("meters/")
    fun getMeters(@Header("Authorization") authToken: String,
                  @Query("page") page: Int): Call<ResponseMeters>
}
