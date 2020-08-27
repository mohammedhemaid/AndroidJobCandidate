package app.storytel.candidate.com.postdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.storytel.candidate.com.api.Resource
import app.storytel.candidate.com.api.RestRepository
import kotlinx.coroutines.launch

class DetailsViewModel(
        postId: Int,
        private val restRepository: RestRepository
) : ViewModel() {

    private val _comments = MutableLiveData<List<Comment>>()
    val comments: LiveData<List<Comment>> = _comments

    private val _progressBar = MutableLiveData<Boolean>()
    val progressBar: LiveData<Boolean> = _progressBar

    private val _timeOutDialog = MutableLiveData<Boolean>()
    val timeOutDialog: LiveData<Boolean> = _timeOutDialog

    init {
        getComments(postId)
    }

    fun getComments(postId: Int) {
        viewModelScope.launch {
            try {
                _progressBar.value = true
                handleComments(restRepository.getPostsComments(postId))
            } catch (e: Exception) {
                _timeOutDialog.value = true
            }
        }
    }

    private fun handleComments(comments: Resource<List<Comment>>) {
        when (comments) {
            is Resource.Success -> comments.data?.let {
                _comments.postValue(it)
                _progressBar.value = false
            }
            is Resource.DataError -> {
                _timeOutDialog.value = true
                _progressBar.value = false
            }
        }
    }
}