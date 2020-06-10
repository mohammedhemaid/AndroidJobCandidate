package app.storytel.candidate.com.postList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.storytel.candidate.com.databinding.PostItemBinding
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class PostsAdapter(
        private val listener: Listener
) : RecyclerView.Adapter<PostsAdapter.PostViewHolder>() {

    interface Listener {
        fun onBodyClick(post: Post, imageUrl: String)
    }

    var data: PostAndImages = PostAndImages(ArrayList(), ArrayList())
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(
                PostItemBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.loadPosts()
    }

    override fun getItemCount(): Int {
        return data.mPosts.size
    }

    inner class PostViewHolder(itemBinding: PostItemBinding) : RecyclerView.ViewHolder(itemBinding.root),
            View.OnClickListener {

        private val title = itemBinding.title
        private val body = itemBinding.body
        private val image = itemBinding.image

        private lateinit var post: Post
        private lateinit var imageUrl: String

        init {
            body.setOnClickListener(this)
        }

        fun loadPosts() {
            post = data.mPosts[adapterPosition]
            title.text = post.title
            body.text = post.body
            imageUrl = data.mPhotos[adapterPosition].thumbnailUrl
            Picasso.get()
                    .load(imageUrl)
                    .into(image)
        }

        override fun onClick(view: View?) {
            listener.onBodyClick(post, imageUrl)
        }
    }
}