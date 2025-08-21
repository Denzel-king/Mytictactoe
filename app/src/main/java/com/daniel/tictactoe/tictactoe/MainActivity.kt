package com.daniel.tictactoe.tictactoe

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.daniel.tictactoe.tictactoe.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val player1 = mutableListOf<Int>()
    private val player2 = mutableListOf<Int>()
    private var activePlayer = 1
    private var gameMode = 1 // 1 = PVP, 2 = PVC

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun restartGame(view: View) {
        val buttons = listOf(
            binding.button1, binding.button2, binding.button3,
            binding.button4, binding.button5, binding.button6,
            binding.button7, binding.button8, binding.button9
        )
        buttons.forEach {
            it.text = ""
            it.setBackgroundResource(android.R.drawable.btn_default)
            it.isEnabled = true
        }

        player1.clear()
        player2.clear()
        activePlayer = 1

        // Keep selected mode colors
        if (gameMode == 1) {
            binding.PVP.setBackgroundColor(Color.CYAN)
            binding.PVC.setBackgroundColor(Color.LTGRAY)
        } else {
            binding.PVC.setBackgroundColor(Color.CYAN)
            binding.PVP.setBackgroundColor(Color.LTGRAY)
        }
    }

    fun buttonClick(view: View) {
        val buSelected = view as Button
        val cellId = when (buSelected) {
            binding.button1 -> 1
            binding.button2 -> 2
            binding.button3 -> 3
            binding.button4 -> 4
            binding.button5 -> 5
            binding.button6 -> 6
            binding.button7 -> 7
            binding.button8 -> 8
            binding.button9 -> 9
            else -> 0
        }
        playGame(cellId, buSelected)
    }

    fun playerChoose(view: View) {
        val ps = view as Button
        if (ps == binding.PVP) {
            gameMode = 1
            ps.setBackgroundColor(Color.CYAN)
            binding.PVC.setBackgroundColor(Color.LTGRAY)
        } else if (ps == binding.PVC) {
            gameMode = 2
            ps.setBackgroundColor(Color.CYAN)
            binding.PVP.setBackgroundColor(Color.LTGRAY)
        }
        restartGame(binding.button1)
    }

    private fun playGame(cellId: Int, buSelected: Button) {
        if (activePlayer == 1) {
            buSelected.text = "X"
            buSelected.setBackgroundColor(Color.GREEN)
            player1.add(cellId)
            buSelected.isEnabled = false
            activePlayer = 2
            checkWinner()
            if (gameMode == 2 && !isGameOver()) autoPlay()
        } else {
            buSelected.text = "O"
            buSelected.setBackgroundColor(Color.CYAN)
            player2.add(cellId)
            buSelected.isEnabled = false
            activePlayer = 1
            checkWinner()
        }
    }

    private fun checkWinner() {
        val winPositions = listOf(
            listOf(1, 2, 3), listOf(4, 5, 6), listOf(7, 8, 9),
            listOf(1, 4, 7), listOf(2, 5, 8), listOf(3, 6, 9),
            listOf(1, 5, 9), listOf(3, 5, 7)
        )

        var winner = -1
        winPositions.forEach {
            if (player1.containsAll(it)) winner = 1
            if (player2.containsAll(it)) winner = 2
        }

        if (winner != -1) {
            val message = when (winner) {
                1 -> if (gameMode == 1) "Player 1 Wins!!" else "You Won!!"
                else -> if (gameMode == 1) "Player 2 Wins!!" else "CPU Wins!!"
            }
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            stopTouch()
        }
    }

    private fun stopTouch() {
        val buttons = listOf(
            binding.button1, binding.button2, binding.button3,
            binding.button4, binding.button5, binding.button6,
            binding.button7, binding.button8, binding.button9
        )
        buttons.forEach { it.isEnabled = false }
    }

    private fun isGameOver(): Boolean {
        return player1.size + player2.size == 9
    }

    private fun autoPlay() {
        val emptyCells = (1..9).filter { it !in player1 && it !in player2 }
        if (emptyCells.isEmpty()) return

        val cellId = emptyCells.random()
        val buSelected = when (cellId) {
            1 -> binding.button1
            2 -> binding.button2
            3 -> binding.button3
            4 -> binding.button4
            5 -> binding.button5
            6 -> binding.button6
            7 -> binding.button7
            8 -> binding.button8
            9 -> binding.button9
            else -> binding.button1
        }

        // CPU is always Player 2
        buSelected.text = "O"
        buSelected.setBackgroundColor(Color.CYAN)
        player2.add(cellId)
        buSelected.isEnabled = false
        activePlayer = 1
        checkWinner()
    }
}
