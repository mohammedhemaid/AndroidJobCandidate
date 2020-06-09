package app.storytel.candidate.com

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.storytel.candidate.com.databinding.PostItemBinding
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList

class PostsAdapter(
        private val listener: Listener
) : RecyclerView.Adapter<PostsAdapter.PostViewHolder>() {

    interface Listener {
        fun onBodyClick()
    }

    var data: PostAndImages = PostAndImages(ArrayList<Post>(), ArrayList<Photo>())
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
        holder.loadPosts(position)
    }

    override fun getItemCount(): Int {
        return data.mPosts.size
    }

    inner class PostViewHolder(itemBinding: PostItemBinding) : RecyclerView.ViewHolder(itemBinding.root),
            View.OnClickListener {

        private val title = itemBinding.title
        private val body = itemBinding.body
        private val image = itemBinding.image

        init {
            body.setOnClickListener(this)
        }

        fun loadPosts(position: Int) {
            title.text = data.mPosts[position].title
            body.text = data.mPosts[position].body
            val index = Random().nextInt(data.mPhotos.size - 1)
            val imageUrl = data.mPhotos[index].thumbnailUrl
            Picasso.get()
                    .load(imageUrl)
                    .into(image)
        }

        override fun onClick(view: View?) {
            listener.onBodyClick()
        }
    }
}