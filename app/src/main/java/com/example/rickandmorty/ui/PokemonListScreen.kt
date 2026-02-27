package com.example.pokedex.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.pokedex.data.PokemonDetail
import com.example.pokedex.data.PokemonEntry

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonListScreen(viewModel: PokemonViewModel) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("POKÉDEX", fontWeight = FontWeight.Bold, color = Color.White) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFFE3350D)
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.startQuiz() },
                containerColor = Color(0xFFE3350D),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Quiz")
            }
        },
        containerColor = Color(0xFFF2F2F2)
    ) { padding ->
        if (viewModel.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFFE3350D))
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                modifier = Modifier.padding(padding)
            ) {
                items(viewModel.pokemonList) { pokemon ->
                    PokemonCard(pokemon) {
                        viewModel.selectPokemon(pokemon)
                    }
                }
            }
        }

        viewModel.selectedPokemon?.let { pokemon ->
            PokemonDetailDialog(pokemon, onDismiss = { viewModel.dismissDetail() })
        }

        if (viewModel.showQuiz) {
            PokemonQuizDialog(viewModel)
        }
    }
}

@Composable
fun PokemonQuizDialog(viewModel: PokemonViewModel) {
    Dialog(
        onDismissRequest = { viewModel.closeQuiz() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "¿Quién es este Pokémon?",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(32.dp))

                Box(
                    modifier = Modifier
                        .size(250.dp)
                        .background(Color(0xFFF2F2F2), RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = viewModel.quizPokemon?.imageUrl,
                        contentDescription = "Quiz Pokemon",
                        modifier = Modifier.size(200.dp),
                        colorFilter = if (viewModel.isAnswerCorrect) null else ColorFilter.tint(Color.Black)
                    )
                }

                Spacer(modifier = Modifier.height(48.dp))

                viewModel.quizOptions.forEach { option ->
                    Button(
                        onClick = { viewModel.checkAnswer(option) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = when {
                                viewModel.isAnswerCorrect && option == viewModel.quizPokemon?.name -> Color(0xFF4CAF50)
                                else -> Color(0xFFE3350D)
                            }
                        ),
                        enabled = !viewModel.isAnswerCorrect
                    ) {
                        Text(
                            text = option.replaceFirstChar { it.uppercase() },
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                if (viewModel.isAnswerCorrect) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "¡Correcto!",
                        color = Color(0xFF4CAF50),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row {
                        Button(
                            onClick = { viewModel.nextQuiz() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
                        ) {
                            Text("Siguiente")
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(
                            onClick = { viewModel.closeQuiz() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                        ) {
                            Text("Cerrar")
                        }
                    }
                } else {
                    Spacer(modifier = Modifier.height(24.dp))
                    TextButton(onClick = { viewModel.closeQuiz() }) {
                        Text("Rendirse", color = Color.Gray)
                    }
                }
            }
        }
    }
}

@Composable
fun PokemonCard(pokemon: PokemonEntry, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .aspectRatio(0.8f)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(Color(0xFFF2F2F2), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = pokemon.imageUrl,
                    contentDescription = pokemon.name,
                    modifier = Modifier.size(120.dp),
                    contentScale = ContentScale.Fit
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "#${pokemon.id.toString().padStart(3, '0')}",
                fontSize = 12.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = pokemon.name.replaceFirstChar { it.uppercase() },
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}

@Composable
fun PokemonDetailDialog(pokemon: PokemonDetail, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .background(Color(0xFFF2F2F2), RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = pokemon.sprites.other.officialArtwork.frontDefault,
                        contentDescription = pokemon.name,
                        modifier = Modifier.size(180.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = pokemon.name.replaceFirstChar { it.uppercase() },
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
                Row(modifier = Modifier.padding(vertical = 8.dp)) {
                    pokemon.types.forEach { typeEntry ->
                        SuggestionChip(
                            onClick = { },
                            label = { Text(typeEntry.type.name.uppercase()) },
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                    }
                }
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "${pokemon.weight / 10.0} kg", fontWeight = FontWeight.Bold)
                        Text(text = "Weight", fontSize = 12.sp, color = Color.Gray)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "${pokemon.height / 10.0} m", fontWeight = FontWeight.Bold)
                        Text(text = "Height", fontSize = 12.sp, color = Color.Gray)
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE3350D)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Close")
                }
            }
        }
    }
}
