package app.storytel.candidate.com.api

import app.storytel.candidate.com.api.callbacks.GetPhotosCallback
import app.storytel.candidate.com.api.callbacks.GetPostCommentsCallback
import app.storytel.candidate.com.api.callbacks.GetPostsCallback
import app.storytel.candidate.com.api.servicegenerator.PostsService
import app.storytel.candidate.com.postList.Photo
import app.storytel.candidate.com.postList.Post
import app.storytel.candidate.com.postdetails.Comment
import retrofit2.Call

class RestRepository(private val postsService: PostsService) {

    fun getPosts(listener: GetPostsCallback.Listener) {
        val getPostsCall: Call<List<Post>> = postsService.getPosts()
        getPostsCall.enqueue(GetPostsCallback(listener))
    }

    fun getPhotos(listener: GetPhotosCallback.Listener) {
        val getPhotosCall: Call<List<Photo>> = postsService.getPhotos()
        getPhotosCall.enqueue(GetPhotosCallback(listener))
    }

    fun getPostsComments(postId: Int, listener: GetPostCommentsCallback.Listener) {
        val getPostCommentsCall: Call<List<Comment>> = postsService.getPostComments(postId)
        getPostCommentsCall.enqueue(GetPostCommentsCallback(listener))
    }
}