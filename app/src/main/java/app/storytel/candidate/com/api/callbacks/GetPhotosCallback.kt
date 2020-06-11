package app.storytel.candidate.com.api.callbacks

import app.storytel.candidate.com.api.ApiConstants
import app.storytel.candidate.com.postList.Photo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetPhotosCallback(private val listener: Listener) : Callback<List<Photo>> {

    interface Listener {
        fun onPhotosSuccess(photos: List<Photo>)
        fun onPhotosFailure(t: Throwable?)
    }

    override fun onResponse(call: Call<List<Photo>>, response: Response<List<Photo>>) {
        if (response.code() == ApiConstants.HTTP_STATUS_OK) {
            listener.onPhotosSuccess(response.body() ?: ArrayList())
        }
    }

    override fun onFailure(call: Call<List<Photo>>, t: Throwable) {
        listener.onPhotosFailure(t)
    }
}