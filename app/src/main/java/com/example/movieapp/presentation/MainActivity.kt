package com.example.movieapp.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.movieapp.R
import com.example.movieapp.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {
    private val vm by viewModel<MainViewModel>()
    private val binding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val searchFragment=SearchFragment()
        val bookmarksFragment=BookmarksFragment()

        setCurrentFragment(searchFragment)

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.search->setCurrentFragment(searchFragment)
                R.id.bookMarks->setCurrentFragment(bookmarksFragment)
            }
            true
        }

    }

    private fun setCurrentFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment, fragment)
            commit()
        }
}