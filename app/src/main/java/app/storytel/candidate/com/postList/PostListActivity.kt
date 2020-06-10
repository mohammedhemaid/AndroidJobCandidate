package app.storytel.candidate.com.postList

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.storytel.candidate.com.R
import app.storytel.candidate.com.postdetails.DetailsActivity
import app.storytel.candidate.com.postdetails.EXTRA_POST
import app.storytel.candidate.com.postdetails.EXTRA_POST_IMAGE
import com.google.gson.Gson
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*
import javax.net.ssl.HttpsURLConnection

class PostListActivity : AppCompatActivity(), PostsAdapter.Listener {
    lateinit var mRecyclerView: RecyclerView
    lateinit var mPostsAdapter: PostsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        mRecyclerView = findViewById(R.id.recycler_view)
        mPostsAdapter = PostsAdapter(this)
        mRecyclerView.setAdapter(mPostsAdapter)

        object : AsyncTask<Void, Void, PostAndImages>() {
            protected override fun doInBackground(vararg voids: Void): PostAndImages {
                val posts = posts
                val photos = photos
                return PostAndImages(posts!!, photos!!)
            }

            private val posts: List<Post>?
                private get() {
                    var posts: List<Post>? = null
                    var stream: InputStream? = null
                    var urlConnection: HttpURLConnection? = null
                    try {
                        var result: String? = null
                        val url = URL(POSTS_URL)
                        urlConnection = url.openConnection() as HttpURLConnection
                        urlConnection.connect()
                        val responseCode = urlConnection!!.responseCode
                        if (responseCode != HttpsURLConnection.HTTP_OK) {
                            throw IOException("HTTP error code: $responseCode")
                        }
                        stream = urlConnection.inputStream
                        if (stream != null) {
                            result = readStream(stream)
                            val array = Gson().fromJson(result, Array<Post>::class.java)
                            posts = Arrays.asList(*array)
                        }
                    } catch (e: MalformedURLException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    } finally {
                        if (stream != null) {
                            try {
                                stream.close()
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                        }
                        urlConnection?.disconnect()
                    }
                    return posts
                }

            private val photos: List<Photo>?
                private get() {
                    var photos: List<Photo>? = null
                    var stream: InputStream? = null
                    var urlConnection: HttpURLConnection? = null
                    try {
                        var result: String? = null
                        val url = URL(PHOTOS_URL)
                        urlConnection = url.openConnection() as HttpURLConnection
                        urlConnection!!.connect()
                        val responseCode = urlConnection!!.responseCode
                        if (responseCode != HttpsURLConnection.HTTP_OK) {
                            throw IOException("HTTP error code: $responseCode")
                        }
                        stream = urlConnection!!.inputStream
                        if (stream != null) {
                            result = readStream(stream)
                            val array = Gson().fromJson(result, Array<Photo>::class.java)
                            photos = Arrays.asList(*array)
                        }
                    } catch (e: MalformedURLException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    } finally {
                        if (stream != null) {
                            try {
                                stream!!.close()
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                        }
                        if (urlConnection != null) {
                            urlConnection!!.disconnect()
                        }
                    }
                    return photos
                }

            /**
             * Converts the contents of an InputStream to a String.
             */
            @Throws(IOException::class, UnsupportedEncodingException::class)
            fun readStream(stream: InputStream?): String {
                var reader: Reader? = null
                reader = InputStreamReader(stream, "UTF-8")
                val rawBuffer = CharArray(256)
                var readSize: Int
                val buffer = StringBuffer()
                while (reader.read(rawBuffer).also { readSize = it } != -1) {
                    buffer.append(rawBuffer, 0, readSize)
                }
                return buffer.toString()
            }

            override fun onPostExecute(result: PostAndImages) {
                mPostsAdapter!!.data = result
            }
        }.execute()
    }

    override fun onBodyClick(post: Post, imageUrl: String) {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra(EXTRA_POST, post)
        intent.putExtra(EXTRA_POST_IMAGE, imageUrl)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_scrolling, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)
    }

    companion object {
        private const val POSTS_URL = "https://jsonplaceholder.typicode.com/posts"
        private const val PHOTOS_URL = "https://jsonplaceholder.typicode.com/photos"
    }
}