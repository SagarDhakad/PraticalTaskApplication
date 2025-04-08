package com.tata.praticaltaskapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.tata.praticaltaskapplication.ui.MainScreen
import com.tata.praticaltaskapplication.repo.MainRepository
import com.tata.praticaltaskapplication.ui.viewmodel.MainViewModel
import com.tata.praticaltaskapplication.ui.viewmodel.MainViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = MainRepository(applicationContext)
        val factory = MainViewModelFactory(repository)
        val viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]
        setContent {
            MainScreen(viewModel)
        }
    }
}

