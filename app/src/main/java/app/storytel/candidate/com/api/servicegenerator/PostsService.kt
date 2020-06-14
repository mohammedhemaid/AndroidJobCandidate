package app.storytel.candidate.com.api.servicegenerator

import app.storytel.candidate.com.postList.Photo
import app.storytel.candidate.com.postList.Post
import app.storytel.candidate.com.postdetails.Comment
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface PostsService {

    @GET("posts")
    fun getPosts(): Call<List<Post>>

    @GET("photos")
    fun getPhotos(): Call<List<Photo>>

    @GET("posts/{id}/comments")
    fun getPostComments(@Path("id") postId: Int): Call<List<Comment>>
}