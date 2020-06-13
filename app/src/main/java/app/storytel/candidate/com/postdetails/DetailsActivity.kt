package app.storytel.candidate.com.postdetails

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import app.storytel.candidate.com.R
import app.storytel.candidate.com.api.RestRepository
import app.storytel.candidate.com.api.callbacks.GetPostCommentsCallback
import app.storytel.candidate.com.api.servicegenerator.RetrofitService.getPostsService
import app.storytel.candidate.com.commondialogs.TimeOutDialog
import app.storytel.candidate.com.databinding.ActivityDetailsBinding
import app.storytel.candidate.com.postList.Post
import com.squareup.picasso.Picasso

const val EXTRA_POST = "post"
const val EXTRA_POST_IMAGE = "postImage"

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
        onAddCommentClick()

        val postId = post.id
        val restRepository = RestRepository(getPostsService())
        restRepository.getPostsComments(postId, this)
        timeOutDialog = TimeOutDialog(this) {
            progressBar.visibility = View.VISIBLE
            restRepository.getPostsComments(postId, this)
        }
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

    private fun onAddCommentClick() {
        binding.addComment.setOnClickListener {
            Toast.makeText(this, R.string.comment_feature_message, Toast.LENGTH_SHORT).show()
        }
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
            }
        }
        progressBar.visibility = View.GONE
    }

    override fun onCommentFailure(t: Throwable?) {
        timeOutDialog?.show()
        progressBar.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        timeOutDialog = null
    }
}