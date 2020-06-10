package app.storytel.candidate.com.api.callbacks

import app.storytel.candidate.com.api.ApiConstants
import app.storytel.candidate.com.postList.Post
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetPostsCallback(private val listener: Listener) : Callback<List<Post>> {

    interface Listener {
        fun onPostsSuccess(posts: List<Post>)
        fun onPostsFailure(t: Throwable?)
    }

    override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
        if (response.code() == ApiConstants.HTTP_STATUS_OK) {
            listener.onPostsSuccess(response.body() ?: ArrayList())
        }
    }

    override fun onFailure(call: Call<List<Post>>, t: Throwable) {
        listener.onPostsFailure(t)
    }
}