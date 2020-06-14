package app.storytel.candidate.com.postList

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import app.storytel.candidate.com.R
import app.storytel.candidate.com.api.RestRepository
import app.storytel.candidate.com.api.callbacks.GetPhotosCallback
import app.storytel.candidate.com.api.callbacks.GetPostsCallback
import app.storytel.candidate.com.api.servicegenerator.RetrofitService.getPostsService
import app.storytel.candidate.com.commondialogs.TimeOutDialog
import app.storytel.candidate.com.databinding.ActivityPostListBinding
import app.storytel.candidate.com.postdetails.DetailsActivity
import app.storytel.candidate.com.postdetails.EXTRA_POST
import app.storytel.candidate.com.postdetails.EXTRA_POST_IMAGE

class PostListActivity : AppCompatActivity(), PostsAdapter.Listener,
        GetPostsCallback.Listener, GetPhotosCallback.Listener {

    private lateinit var binding: ActivityPostListBinding
    private lateinit var progressBar: ProgressBar
    private lateinit var mPostsAdapter: PostsAdapter
    private lateinit var restRepository: RestRepository
    private lateinit var postsTimeOutDialog: TimeOutDialog
    private lateinit var photosTimeOutDialog: TimeOutDialog
    private var callCounter = 0
    private var posts: List<Post> = ArrayList()
    private var photos: List<Photo> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        progressBar = binding.progressBar

        val postList = binding.postsList
        val itemDecorator = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        itemDecorator.setDrawable(ContextCompat.getDrawable(this, R.drawable.line_divider)!!)
        postList.addItemDecoration(itemDecorator)
        mPostsAdapter = PostsAdapter(this)
        postList.adapter = mPostsAdapter

        restRepository = RestRepository(getPostsService())
        restRepository.getPosts(this)
        restRepository.getPhotos(this)

        postsTimeOutDialog = TimeOutDialog(this) {
            progressBar.visibility = View.VISIBLE
            restRepository.getPosts(this)
        }
        photosTimeOutDialog = TimeOutDialog(this) {
            progressBar.visibility = View.VISIBLE
            restRepository.getPhotos(this)
        }
    }

    override fun onPostsSuccess(posts: List<Post>) {
        callCounter++
        this.posts = posts
        setAdapterPosts()
    }

    override fun onPostsFailure(t: Throwable?) {
        progressBar.visibility = View.GONE
        postsTimeOutDialog.show()
    }

    override fun onPhotosSuccess(photos: List<Photo>) {
        callCounter++
        this.photos = photos
        setAdapterPosts()
    }

    override fun onPhotosFailure(t: Throwable?) {
        progressBar.visibility = View.GONE
        photosTimeOutDialog.show()
    }


    private fun setAdapterPosts() {
        if (callCounter == 2) {
            progressBar.visibility = View.GONE
            mPostsAdapter.data = PostAndImages(posts, photos)
        }
    }

    override fun onBodyClick(post: Post, imageUrl: String) {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra(EXTRA_POST, post)
        intent.putExtra(EXTRA_POST_IMAGE, imageUrl)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_post_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)
    }
}