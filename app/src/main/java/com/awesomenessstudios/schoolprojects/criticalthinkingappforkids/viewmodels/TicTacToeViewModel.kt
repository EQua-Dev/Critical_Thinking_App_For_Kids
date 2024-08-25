package com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TicTacToeViewModel @Inject constructor() : ViewModel() {

    private val _boardState = MutableLiveData<List<List<String>>>()
    val boardState: LiveData<List<List<String>>> = _boardState

    private val _currentPlayer = MutableLiveData<String>()
    val currentPlayer: LiveData<String> = _currentPlayer

    private val _gameStatus = MutableLiveData<String>()
    val gameStatus: LiveData<String> = _gameStatus

    private var difficultyLevel: String = "Easy"

    init {
        resetGame()
    }

    fun startGame(difficulty: String) {
        difficultyLevel = difficulty
        resetGame()
    }

    fun resetGame() {
        _boardState.value = List(3) { List(3) { "" } }
        _currentPlayer.value = "You"
        _gameStatus.value = "Your turn"
    }

    fun makeMove(row: Int, col: Int) {
        val board = _boardState.value?.map { it.toMutableList() } ?: return

        if (board[row][col].isNotEmpty() || _gameStatus.value?.contains("Win") == true || _gameStatus.value?.contains("Draw") == true) return

        board[row][col] = "X"
        _boardState.value = board

        if (checkWin(board, "X")) {
            _gameStatus.value = "You Win!"
            return
        }

        if (isBoardFull(board)) {
            _gameStatus.value = "It's a Draw!"
            return
        }

        _currentPlayer.value = "Computer's Turn"
        makeComputerMove(board)
    }

    private fun makeComputerMove(board: List<MutableList<String>>) {
        when (difficultyLevel) {
            "Easy" -> makeRandomMove(board)
            "Medium" -> makeMediumMove(board)
            "Hard" -> makeOptimalMove(board)
        }

        _boardState.value = board

        if (checkWin(board, "O")) {
            _gameStatus.value = "Computer Wins!"
            return
        }

        if (isBoardFull(board)) {
            _gameStatus.value = "It's a Draw!"
            return
        }

        _currentPlayer.value = "Your Turn"
        _gameStatus.value = "Your turn"
    }

    private fun makeRandomMove(board: List<MutableList<String>>) {
        val availableMoves = board.flatMapIndexed { row, cols ->
            cols.mapIndexedNotNull { col, cell -> if (cell.isEmpty()) row to col else null }
        }
        val (row, col) = availableMoves.random()
        board[row][col] = "O"
    }

    private fun makeMediumMove(board: List<MutableList<String>>) {
        // Simple strategy: block the player’s winning move or make a winning move
        // 1. Check if computer can win
        if (tryWinningMove(board, "O")) return

        // 2. Block player's winning move
        if (tryWinningMove(board, "X")) return

        // 3. Otherwise, make a random move
        makeRandomMove(board)
    }

    private fun tryWinningMove(board: List<MutableList<String>>, symbol: String): Boolean {
        for (row in 0..2) {
            for (col in 0..2) {
                if (board[row][col].isEmpty()) {
                    board[row][col] = symbol
                    if (checkWin(board, symbol)) {
                        if (symbol == "X") board[row][col] = "O"
                        return true
                    }
                    board[row][col] = ""
                }
            }
        }
        return false
    }

    private fun makeOptimalMove(board: List<MutableList<String>>) {
        // Implement the minimax algorithm or any optimal strategy
        // For simplicity, this example makes random moves
        makeRandomMove(board)
    }

    private fun checkWin(board: List<List<String>>, symbol: String): Boolean {
        val lines = listOf(
            // Rows
            listOf(board[0][0], board[0][1], board[0][2]),
            listOf(board[1][0], board[1][1], board[1][2]),
            listOf(board[2][0], board[2][1], board[2][2]),
            // Columns
            listOf(board[0][0], board[1][0], board[2][0]),
            listOf(board[0][1], board[1][1], board[2][1]),
            listOf(board[0][2], board[1][2], board[2][2]),
            // Diagonals
            listOf(board[0][0], board[1][1], board[2][2]),
            listOf(board[0][2], board[1][1], board[2][0])
        )
        return lines.any { line -> line.all { it == symbol } }
    }

    private fun isBoardFull(board: List<List<String>>): Boolean {
        return board.flatten().none { it.isEmpty() }
    }
}
