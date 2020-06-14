package app.storytel.candidate.com.api.servicegenerator

import app.storytel.candidate.com.postList.model.Photo
import app.storytel.candidate.com.postList.model.Post
import app.storytel.candidate.com.postdetails.Comment
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface PostsService {

    @GET("posts")
    suspend fun getPosts(): Response<List<Post>>

    @GET("photos")
    suspend fun getPhotos(): Response<List<Photo>>

    @GET("posts/{id}/comments")
    suspend fun getPostComments(@Path("id") postId: Int): Response<List<Comment>>
}