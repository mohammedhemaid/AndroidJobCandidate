package app.storytel.candidate.com.postdetails

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import app.storytel.candidate.com.Post
import app.storytel.candidate.com.api.RestRepository
import app.storytel.candidate.com.api.servicegenerator.RetrofitService.getPostsService
import app.storytel.candidate.com.api.callbacks.GetPostCommentsCallback
import app.storytel.candidate.com.commondialogs.TimeOutDialog
import app.storytel.candidate.com.databinding.ActivityDetailsBinding
import com.squareup.picasso.Picasso
import java.io.IOException
import java.net.SocketTimeoutException


const val EXTRA_POST = "post"
const val EXTRA_POST_IMAGE = "postImage"
private const val TAG = "DetailsActivity"

class DetailsActivity : AppCompatActivity(), GetPostCommentsCallback.Listener {

    private lateinit var binding: ActivityDetailsBinding
    private lateinit var progressBar: ProgressBar
    private lateinit var post: Post
    private var timeOutDialog: TimeOutDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intentExtra = intent.extras
        post = intentExtra!!.getParcelable(EXTRA_POST)!!

        bindToolbar()
        binding.details.text = post.body
        progressBar = binding.progressBar
        val restRepository = RestRepository(getPostsService())

        val postId = post.id
        timeOutDialog = TimeOutDialog(this) {
            progressBar.visibility = View.VISIBLE
            restRepository.getPostsComments(postId, this)
        }
        restRepository.getPostsComments(postId, this)
    }

    private fun bindToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val postImage = intent.extras!!.getString(EXTRA_POST_IMAGE)
        Picasso.get()
                .load(postImage)
                .into(binding.backdrop)
        binding.collapsingToolbar.title = post.title
    }

    override fun onCommentsSuccess(comments: List<Comment>) {
        for ((counter, comment) in comments.withIndex()) {
            when (counter) {
                0 -> {
                    binding.comment1.visibility = View.VISIBLE
                    binding.title1.text = comment.name
                    binding.description1.text = comment.body
                }
                1 -> {
                    binding.comment2.visibility = View.VISIBLE
                    binding.title2.text = comment.name
                    binding.description2.text = comment.body
                }
                2 -> {
                    binding.comment3.visibility = View.VISIBLE
                    binding.title3.text = comment.name
                    binding.description3.text = comment.body
                }
                else -> progressBar.visibility = View.GONE
            }
        }
    }

    override fun onCommentFailure(t: Throwable?) {
        if (t is SocketTimeoutException) {
            // "Connection Timeout";
            Log.d(TAG, "Connection Timeout")

        } else if (t is IOException) {
            // "Timeout";
            Log.d(TAG, "Timeout")

        }
        timeOutDialog?.show()
        progressBar.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        timeOutDialog = null
    }
}