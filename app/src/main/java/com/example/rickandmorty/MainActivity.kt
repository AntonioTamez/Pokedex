package com.example.pokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.pokedex.ui.PokemonListScreen
import com.example.pokedex.ui.PokemonViewModel
import com.example.pokedex.ui.theme.PokedexTheme

class MainActivity : ComponentActivity() {
    private val viewModel: PokemonViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PokedexTheme {
                PokemonListScreen(viewModel)
            }
        }
    }
}
