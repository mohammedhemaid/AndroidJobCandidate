package app.storytel.candidate.com.postList

import android.content.Context
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.storytel.candidate.com.api.Resource
import app.storytel.candidate.com.api.RestRepository
import app.storytel.candidate.com.postList.model.Photo
import app.storytel.candidate.com.postList.model.Post
import app.storytel.candidate.com.postList.model.PostAndImages
import app.storytel.candidate.com.utils.isInternetAvailable
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import java.net.SocketTimeoutException

class PostListViewModel(
        private val context: Context,
        private val restRepository: RestRepository
) : ViewModel() {

    private val _postAndImages = MutableLiveData<PostAndImages>()
    val postAndImages: LiveData<PostAndImages> = _postAndImages

    private val _notInternet = MutableLiveData<Boolean>()
    val notInternet: LiveData<Boolean> = _notInternet

    private val _progress = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean> = _progress

    private val _timeOutDialog = MutableLiveData<Boolean>()
    val timeOutDialog: LiveData<Boolean> = _timeOutDialog

    init {
        fetchPostsAndImages()
    }

    fun fetchPostsAndImages() {
        if (isInternetAvailable(context)) {
            getPostsAndImages()
            _notInternet.value = false
        } else {
            _notInternet.value = true
        }
    }

    private fun getPostsAndImages() {
        viewModelScope.launch {
            supervisorScope {
                try {
                    _progress.value = true
                    val posts = async { restRepository.getPosts() }
                    val photos = async { restRepository.getPhotos() }
                    handlePostList(posts.await(), photos.await())
                } catch (e: SocketTimeoutException) {
                    _timeOutDialog.value = true
                    _progress.value = false
                }
            }
        }
    }

    private fun handlePostList(posts: Resource<List<Post>>, photos: Resource<List<Photo>>) {
        when (posts) {
            is Resource.Success -> photos.data?.let {
                _postAndImages.postValue(PostAndImages(posts.data!!, it))
                _progress.value = false
            }
            is Resource.DataError -> {
                _timeOutDialog.value = true
                _progress.value = false
            }
        }
    }
}