package coml.go.tawseel.webApi

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.Gson
import retrofit2.converter.scalars.ScalarsConverterFactory


/**
 * Created by hemendrag on 21/11/2018.
 * this class contains the web api
 */

class ApiClient {

    companion object {

        /**
         * create singleton for accessing variables
         */
        var mApiClient: ApiClient? = null
        var retrofit: Retrofit? = null

        val client: ApiClient
            get() {

                if (mApiClient == null) {
                    mApiClient = ApiClient()
                }
                return mApiClient as ApiClient
            }

        // urls
        const val BASE_URL = "http://jsonplaceholder.typicode.com/"
        const val POSTS_URL = "posts/"
    }


    /**
     * this method will return instance ApiInterface
     */
    fun getRetrofitService(): ApiInterface {

        var httpClient = OkHttpClient.Builder()
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        httpClient.interceptors().add(interceptor)

        val gson =
            GsonBuilder()
            .setLenient()
            .create()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build().create(ApiInterface::class.java)
    }




}
