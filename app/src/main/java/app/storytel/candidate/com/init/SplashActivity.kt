package app.storytel.candidate.com.init

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import app.storytel.candidate.com.postList.PostListActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!isTaskRoot
                && intent.hasCategory(Intent.CATEGORY_LAUNCHER)
                && intent.action != null && intent.action == Intent.ACTION_MAIN) {
            finish()
            return
        }

        startActivity(Intent(this, PostListActivity::class.java))
        finish()
    }
}