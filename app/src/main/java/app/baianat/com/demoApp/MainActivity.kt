package app.baianat.com.demoApp

import android.app.PendingIntent.getActivity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.doOnPreDraw
import app.baianat.com.demoApp.activities.FancyActivity
import app.baianat.com.demoApp.activities.NativeActivity
import app.baianat.com.demoApp.activities.WidgetsActivity
import app.baianat.com.vanguard.utils.print
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.graphics.DrawFilter
import android.graphics.drawable.Drawable
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.View
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nativeBtn.setOnClickListener {
            startActivity(Intent(this, NativeActivity::class.java))
        }

        customBtn.setOnClickListener {
            startActivity(Intent(this, WidgetsActivity::class.java))
        }

        fancyBtn.setOnClickListener {
            startActivity(Intent(this, FancyActivity::class.java))
        }

//        startActivity(Intent(this, FancyActivity::class.java))

    }
}
