package app.storytel.candidate.com.api

import app.storytel.candidate.com.api.servicegenerator.PostsService
import app.storytel.candidate.com.postList.model.Photo
import app.storytel.candidate.com.postList.model.Post
import app.storytel.candidate.com.postdetails.Comment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException

class RestRepository(private val postsService: PostsService) {

    suspend fun getPosts(): Resource<List<Post>> {
        return when (val response = processCall(postsService.getPosts())) {
            is List<*> -> {
                Resource.Success(data = response) as Resource<List<Post>>
            }
            else -> {
                Resource.DataError(errorCode = response as Int)
            }
        }
    }

    suspend fun getPhotos(): Resource<List<Photo>> {
        return when (val response = processCall(postsService.getPhotos())) {
            is List<*> -> {
                Resource.Success(data = response) as Resource<List<Photo>>
            }
            else -> {
                Resource.DataError(errorCode = response as Int)
            }
        }
    }

    suspend fun getPostsComments(postId: Int): Resource<List<Comment>> {
        return when (val response = processCall(postsService.getPostComments(postId))) {
            is List<*> -> {
                Resource.Success(data = response) as Resource<List<Comment>>
            }
            else -> {
                Resource.DataError(errorCode = response as Int)
            }
        }
    }

    private suspend fun processCall(response: Response<*>): Any? {
        return withContext(Dispatchers.IO) {
            try {
                val responseCode = response.code()
                if (response.isSuccessful) {
                    response.body()
                } else {
                    responseCode
                }
            } catch (e: IOException) {
                ApiConstants.NETWORK_ERROR
            }
        }
    }
}