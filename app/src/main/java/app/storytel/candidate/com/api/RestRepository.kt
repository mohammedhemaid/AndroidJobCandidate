package app.storytel.candidate.com.api

import app.storytel.candidate.com.api.callbacks.GetPostCommentsCallback
import app.storytel.candidate.com.api.servicegenerator.PostsService
import app.storytel.candidate.com.postList.Photo
import app.storytel.candidate.com.postList.Post
import app.storytel.candidate.com.postdetails.Comment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response
import java.io.IOException

class RestRepository(private val postsService: PostsService) {

    suspend fun getPosts(): Resource<List<Post>> {
        return when (val response = processCall(postsService::getPosts)) {
            is List<*> -> {
                Resource.Success(data = response) as Resource<List<Post>>
            }
            else -> {
                Resource.DataError(errorCode = response as Int)
            }
        }
    }

    suspend fun getPhotos(): Resource<List<Photo>> {
        return when (val response = processCall(postsService::getPhotos)) {
            is List<*> -> {
                Resource.Success(data = response) as Resource<List<Photo>>
            }
            else -> {
                Resource.DataError(errorCode = response as Int)
            }
        }
    }

    fun getPostsComments(postId: Int, listener: GetPostCommentsCallback.Listener) {
        val getPostCommentsCall: Call<List<Comment>> = postsService.getPostComments(postId)
        getPostCommentsCall.enqueue(GetPostCommentsCallback(listener))
    }

    private suspend fun processCall(responseCall: suspend () -> Response<*>): Any? {
        return withContext(Dispatchers.IO) {
            try {
                val response = responseCall.invoke()
                val responseCode = response.code()
                if (response.isSuccessful) {
                    response.body()
                } else {
                    responseCode
                }
            } catch (e: IOException) {
                -2
            }
        }
    }
}