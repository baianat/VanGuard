package app.baianat.com.demoApp.fragments

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import app.baianat.com.demoApp.R

/**
 * used to testing the behaviour while moving between fragment,
 * to test it, make it a launcher activity in the manifest
 */
class FragmentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment)


        supportFragmentManager.beginTransaction().replace(R.id.holder, FirstFragment()).commit()
    }


    fun openSecond() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.holder, SecondFragment())
            .addToBackStack("")
            .commit()
    }
}
