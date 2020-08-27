package app.storytel.candidate.com.postdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.storytel.candidate.com.api.RestRepository

class DetailsViewModelFactory(
        private val postId: Int,
        private val restRepository: RestRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(Int::class.java, RestRepository::class.java).newInstance(postId, restRepository)
    }
}