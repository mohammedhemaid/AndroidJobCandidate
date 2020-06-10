package app.storytel.candidate.com.api

import app.storytel.candidate.com.Comment
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface PostsService {
    @GET("posts/{id}/comments")
    fun getPostComments(@Path("id") postId: Int): Call<List<Comment>>
}