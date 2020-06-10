package app.storytel.candidate.com.api

import app.storytel.candidate.com.api.callbacks.GetPostCommentsCallback
import app.storytel.candidate.com.api.servicegenerator.PostsService
import app.storytel.candidate.com.postdetails.Comment
import retrofit2.Call

class RestRepository(private val postsService: PostsService) {

    fun getPostsComments(postId: Int, listener: GetPostCommentsCallback.Listener) {
        val getPostCommentsCall: Call<List<Comment>> = postsService.getPostComments(postId)
        getPostCommentsCall.enqueue(GetPostCommentsCallback(listener))
    }
}