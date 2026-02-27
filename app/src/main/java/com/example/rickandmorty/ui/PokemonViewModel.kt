package com.example.pokedex.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.data.PokeApiService
import com.example.pokedex.data.PokemonDetail
import com.example.pokedex.data.PokemonEntry
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PokemonViewModel : ViewModel() {
    private val apiService = Retrofit.Builder()
        .baseUrl("https://pokeapi.co/api/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(PokeApiService::class.java)

    var pokemonList by mutableStateOf<List<PokemonEntry>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var selectedPokemon by mutableStateOf<PokemonDetail?>(null)
        private set

    var isDetailLoading by mutableStateOf(false)
        private set

    init {
        fetchPokemon()
    }

    private fun fetchPokemon() {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = apiService.getPokemonList(limit = 151)
                pokemonList = response.results
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    fun selectPokemon(pokemon: PokemonEntry) {
        viewModelScope.launch {
            isDetailLoading = true
            try {
                selectedPokemon = apiService.getPokemonDetail(pokemon.name)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isDetailLoading = false
            }
        }
    }

    fun dismissDetail() {
        selectedPokemon = null
    }
}
