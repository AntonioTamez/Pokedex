package com.example.pokedex.data

import com.google.gson.annotations.SerializedName

data class PokemonResponse(
    val results: List<PokemonEntry>
)

data class PokemonEntry(
    val name: String,
    val url: String
) {
    val id: Int
        get() = url.split("/").filter { it.isNotEmpty() }.last().toInt()
    
    val imageUrl: String
        get() = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"
}

data class PokemonDetail(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val types: List<PokemonTypeEntry>,
    val sprites: PokemonSprites
)

data class PokemonTypeEntry(
    val type: PokemonType
)

data class PokemonType(
    val name: String
)

data class PokemonSprites(
    @SerializedName("front_default") val frontDefault: String,
    val other: OtherSprites
)

data class OtherSprites(
    @SerializedName("official-artwork") val officialArtwork: OfficialArtwork
)

data class OfficialArtwork(
    @SerializedName("front_default") val frontDefault: String
)
