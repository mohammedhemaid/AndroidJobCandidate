package app.storytel.candidate.com.postList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.storytel.candidate.com.api.Resource
import app.storytel.candidate.com.api.RestRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class PostListViewModel(
        private val restRepository: RestRepository
) : ViewModel() {

    private val _postAndImages = MutableLiveData<PostAndImages>()
    val postAndImages: LiveData<PostAndImages> = _postAndImages

    private val _progressBar = MutableLiveData<Boolean>()
    val progressBar: LiveData<Boolean> = _progressBar

    private val _timeOutDialog = MutableLiveData<Boolean>()
    val timeOutDialog: LiveData<Boolean> = _timeOutDialog

    fun getPostsAndImages() {
        viewModelScope.launch {
            val posts = async { restRepository.getPosts() }
            val photos = async { restRepository.getPhotos() }
            handlePostList(posts.await(), photos.await())
        }
    }

    private fun handlePostList(posts: Resource<List<Post>>, photos: Resource<List<Photo>>) {
        when (posts) {
            is Resource.Loading -> _progressBar.value = true
            is Resource.Success -> photos.data?.let {
                _postAndImages.postValue(PostAndImages(posts.data!!, it))
                _progressBar.value = false
            }
            is Resource.DataError -> {
                _timeOutDialog.value = true
                _progressBar.value = false
            }
        }
    }
}