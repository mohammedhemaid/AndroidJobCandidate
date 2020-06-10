package app.storytel.candidate.com.api.callbacks

import app.storytel.candidate.com.api.ApiConstants
import app.storytel.candidate.com.postdetails.Comment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetPostCommentsCallback(private val listener: Listener) : Callback<List<Comment>> {

    interface Listener {
        fun onCommentsSuccess(comments: List<Comment>)
        fun onCommentFailure(t: Throwable?)
    }

    override fun onResponse(call: Call<List<Comment>>, response: Response<List<Comment>>) {
        if (response.code() == ApiConstants.HTTP_STATUS_OK) {
            listener.onCommentsSuccess(response.body() ?: ArrayList())
        }
    }

    override fun onFailure(call: Call<List<Comment>>, t: Throwable) {
        listener.onCommentFailure(t)
    }
}