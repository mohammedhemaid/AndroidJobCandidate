package app.storytel.candidate.com.postdetails

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.storytel.candidate.com.api.Resource
import app.storytel.candidate.com.api.RestRepository
import kotlinx.coroutines.launch
import java.lang.Exception

private const val TAG = "DetailsViewModel"

class DetailsViewModel(
        private val restRepository: RestRepository
) : ViewModel() {

    private val _postAndImages = MutableLiveData<List<Comment>>()
    val postAndImages: LiveData<List<Comment>> = _postAndImages

    private val _progressBar = MutableLiveData<Boolean>()
    val progressBar: LiveData<Boolean> = _progressBar

    private val _timeOutDialog = MutableLiveData<Boolean>()
    val timeOutDialog: LiveData<Boolean> = _timeOutDialog

    fun getComments(postId: Int) {
        Log.d(TAG, "getComments")
        viewModelScope.launch {
            try {
                handleComments(restRepository.getPostsComments(postId))
            } catch (e: Exception) {
                _progressBar.value = false
                _timeOutDialog.value = true
            }
        }
    }

    private fun handleComments(comments: Resource<List<Comment>>) {
        when (comments) {
            is Resource.Loading -> _progressBar.value = true
            is Resource.Success -> comments.data?.let {
                _postAndImages.postValue(it)
                _progressBar.value = false
            }
            is Resource.DataError -> {
                _timeOutDialog.value = true
                _progressBar.value = false
            }
        }
    }
}