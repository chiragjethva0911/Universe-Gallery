package chirag.jethva.gallery.Activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import chirag.jethva.gallery.Fregment.FolderFragment
import chirag.jethva.gallery.Fregment.PhotoFragment
import chirag.jethva.gallery.Fregment.VideoFragment
import chirag.jethva.gallery.R
import chirag.jethva.gallery.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBottomNavigation()

        // Set default fragment
        if (savedInstanceState == null) {
            loadFragment(PhotoFragment())
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_photos -> loadFragment(PhotoFragment())
                R.id.menu_videos -> loadFragment(VideoFragment())
                R.id.menu_folders -> loadFragment(FolderFragment())
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment): Boolean {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
        return true
    }
}