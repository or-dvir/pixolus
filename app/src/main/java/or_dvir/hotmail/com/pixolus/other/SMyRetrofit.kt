package or_dvir.hotmail.com.pixolus.other

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object SMyRetrofit
{
    val pixometerClient: IPixometerClient

    init
    {
        val retrofit = Retrofit.Builder()
                .baseUrl("https://pixometer.io/api/v1/")
                .addConverterFactory(MoshiConverterFactory.create())
                .build()

        pixometerClient = retrofit.create<IPixometerClient>(IPixometerClient::class.java)
    }
}