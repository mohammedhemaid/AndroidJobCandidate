package app.storytel.candidate.com.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.storytel.candidate.com.api.RestRepository

class ViewModelFactory(
        private val restRepository: RestRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(RestRepository::class.java).newInstance(restRepository)
    }
}