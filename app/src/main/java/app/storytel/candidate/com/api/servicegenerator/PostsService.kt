package app.storytel.candidate.com.api.servicegenerator

import app.storytel.candidate.com.postdetails.Comment
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface PostsService {
    @GET("posts/{id}/comments")
    fun getPostComments(@Path("id") postId: Int): Call<List<Comment>>
}