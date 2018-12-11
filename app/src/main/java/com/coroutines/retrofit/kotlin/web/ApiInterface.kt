package coml.go.tawseel.webApi


import com.coroutines.retrofit.kotlin.model.Posts
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by hemendrag on 21/11/2018.
 * this class contains the fields and data of api request/response
 */

interface ApiInterface {

    @Headers("Content-Type: application/json")
    @GET(ApiClient.POSTS_URL)
    fun getPosts(): Deferred<Response<List<Posts>>>

}
