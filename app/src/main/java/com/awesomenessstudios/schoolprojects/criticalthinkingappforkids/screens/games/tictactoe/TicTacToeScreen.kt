package com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.screens.games.tictactoe

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.viewmodels.TicTacToeViewModel

@Composable
fun TicTacToeScreen(viewModel: TicTacToeViewModel = hiltViewModel(), difficulty: String) {
    val boardState by viewModel.boardState.observeAsState()
    val currentPlayer by viewModel.currentPlayer.observeAsState()
    val gameStatus by viewModel.gameStatus.observeAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Game status
        Text(
            text = gameStatus!!,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Board
        BoardGrid(modifier = Modifier.fillMaxWidth(), boardState!!, onCellClick = { row, col ->
            viewModel.makeMove(row, col)
        })

        Spacer(modifier = Modifier.height(16.dp))

        // Reset Button
        Button(
            onClick = { viewModel.resetGame() },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Reset Game")
        }
    }

    // Start game based on difficulty
    LaunchedEffect(Unit) {
        viewModel.startGame(difficulty)
    }
}

@Composable
fun BoardGrid(
    modifier: Modifier = Modifier,
    boardState: List<List<String>>,
    onCellClick: (Int, Int) -> Unit
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        for (row in boardState.indices) {
            Row {
                for (col in boardState[row].indices) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .border(1.dp, Color.Black)
                            .clickable { onCellClick(row, col) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = boardState[row][col],
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                }
            }
        }
    }
}
