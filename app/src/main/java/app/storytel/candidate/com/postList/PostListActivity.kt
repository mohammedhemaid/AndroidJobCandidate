package app.storytel.candidate.com.postList

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import app.storytel.candidate.com.R
import app.storytel.candidate.com.api.RestRepository
import app.storytel.candidate.com.api.servicegenerator.RetrofitService.getPostsService
import app.storytel.candidate.com.commondialogs.TimeOutDialog
import app.storytel.candidate.com.databinding.ActivityPostListBinding
import app.storytel.candidate.com.postdetails.DetailsActivity
import app.storytel.candidate.com.postdetails.EXTRA_POST
import app.storytel.candidate.com.postdetails.EXTRA_POST_IMAGE
import app.storytel.candidate.com.utils.ViewModelFactory
import app.storytel.candidate.com.utils.isInternetAvailable
import app.storytel.candidate.com.utils.observe

class PostListActivity : AppCompatActivity(), PostsAdapter.Listener {

    private lateinit var binding: ActivityPostListBinding
    private lateinit var postListViewModel: PostListViewModel
    private lateinit var progressBar: ProgressBar
    private lateinit var noInternet: LinearLayout
    private lateinit var postList: RecyclerView
    private lateinit var mPostsAdapter: PostsAdapter
    private lateinit var postsTimeOutDialog: TimeOutDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        progressBar = binding.progressBar
        noInternet = binding.noInternet
        postList = binding.postsList

        val itemDecorator = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        itemDecorator.setDrawable(ContextCompat.getDrawable(this, R.drawable.line_divider)!!)
        postList.addItemDecoration(itemDecorator)
        mPostsAdapter = PostsAdapter(this)
        postList.adapter = mPostsAdapter

        postListViewModel = ViewModelProvider(
                this,
                ViewModelFactory(RestRepository(getPostsService()))
        ).get(PostListViewModel::class.java)
        observeViewModel()
        fetchPostsAndImages()

        postsTimeOutDialog = TimeOutDialog(this) {
            fetchPostsAndImages()
        }
        onNoInternetRetryClick()
    }

    private fun fetchPostsAndImages() {
        if (isInternetAvailable(this)) {
            postListViewModel.getPostsAndImages()
            progressBar.visibility = View.VISIBLE
            noInternet.visibility = View.GONE
        } else {
            progressBar.visibility = View.GONE
            noInternet.visibility = View.VISIBLE
        }
    }

    private fun observeViewModel() {
        observe(postListViewModel.postAndImages, ::handlePostList)
        observe(postListViewModel.progressBar, ::handelProgress)
        observe(postListViewModel.timeOutDialog, ::handelTimeOut)
    }

    private fun handlePostList(postsAndImages: PostAndImages) {
        mPostsAdapter.data = postsAndImages
    }

    private fun handelTimeOut(showDialog: Boolean) {
        postsTimeOutDialog.show()
    }

    private fun handelProgress(showProgress: Boolean) {
        progressBar.visibility = if (showProgress) View.VISIBLE else View.GONE
    }

    override fun onBodyClick(post: Post, imageUrl: String) {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra(EXTRA_POST, post)
        intent.putExtra(EXTRA_POST_IMAGE, imageUrl)
        startActivity(intent)
    }

    private fun onNoInternetRetryClick() {
        binding.retry.setOnClickListener {
            fetchPostsAndImages()
        }
    }
}