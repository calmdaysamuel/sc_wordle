package com.calmday.scwordle

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.children

class MainActivity : AppCompatActivity() {

    var wordToGuess = ""
    var currentGuess = 1
    var maxGuesses = 3

    var guesses: MutableList<String> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val  guessButton = findViewById<Button>(R.id.guessButton)
        wordToGuess()
        guessButton.setOnClickListener {
            processGuess()
        }

        findViewById<Button>(R.id.resetButton).setOnClickListener {
            resetGame()
        }
    }

    private fun processGuess() {
        val guess = findViewById<TextView>(R.id.wordInput).text ?: ""
        if (guess.length < 4) {
            Toast.makeText(this, "Your guess is too short guess a 4 letter word", Toast.LENGTH_SHORT).show()
            return
        } else if (guess.length > 4) {
            Toast.makeText(this, "Your guess is too long guess a 4 letter word", Toast.LENGTH_SHORT).show()
            return
        }
        Toast.makeText(this, "YOU GUESSED " + guess, Toast.LENGTH_SHORT).show()

        val result = checkGuess(guess.toString().uppercase())
        guesses.add(guess.toString().uppercase())
        showResult(currentGuess,result)
        if (currentGuess >= maxGuesses) {
            if (guess.toString().uppercase() == wordToGuess) {
                Toast.makeText(this, "You guess the word correctly. Congrats!!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "You did not get the word. Try again", Toast.LENGTH_SHORT).show()
            }
            findViewById<TextView>(R.id.answer).visibility = View.VISIBLE
            findViewById<Button>(R.id.guessButton).isEnabled = false
            findViewById<Button>(R.id.guessButton).isClickable = false
            return
        }

        currentGuess++;
    }
    private fun wordToGuess() {
        this.wordToGuess =  FourLetterWordList.getRandomFourLetterWord().uppercase()
        findViewById<TextView>(R.id.answer).text = wordToGuess;
        Log.v("cword", wordToGuess)
    }
    private fun showResult(guess: Int, result: String) {
        var row = findViewById<LinearLayout>(R.id.r1)
        if (guess == 2) {
            row = findViewById<LinearLayout>(R.id.r2)
        } else if (guess == 3) {
            row = findViewById<LinearLayout>(R.id.r3)
        }
        var i = 0
        for (text in row.children) {
           ( text as TextView).text = guesses[guess - 1][i].toString();
            Log.v("gresult", result)

            textToColor((text as TextView), result[i])

            i++

        }
    }
    private fun resetGame() {
        wordToGuess = ""
        currentGuess = 1
        maxGuesses = 3

        guesses = mutableListOf("    ", "    ", "    ")

        showResult(1, "----")
        showResult(2, "----")
        showResult(3, "----")

        guesses = mutableListOf()
        wordToGuess()
        findViewById<Button>(R.id.guessButton).isEnabled = true
        findViewById<Button>(R.id.guessButton).isClickable = true
        Toast.makeText(this, "The game has been reset. The word has changed", Toast.LENGTH_SHORT)
        findViewById<TextView>(R.id.answer).visibility = View.GONE

    }
    private fun textToColor(textView: TextView, symbol: Char) {
        if (symbol == 'X') {
            textView.setBackgroundColor(resources.getColor(R.color.red));
        } else if (symbol == '+') {
            textView.setBackgroundColor(resources.getColor(R.color.yellow));
        } else if (symbol == 'O') {
            textView.setBackgroundColor(resources.getColor(R.color.green));
        } else {
            textView.setBackgroundColor(resources.getColor(R.color.grey));

        }
    }
    /**
     * Parameters / Fields:
     *   wordToGuess : String - the target word the user is trying to guess
     *   guess : String - what the user entered as their guess
     *
     * Returns a String of 'O', '+', and 'X', where:
     *   'O' represents the right letter in the right place
     *   '+' represents the right letter in the wrong place
     *   'X' represents a letter not in the target word
     */


    private fun checkGuess(guess: String) : String {

        var result = ""
        for (i in 0..3) {
            if (guess[i] == wordToGuess[i]) {
                result += "O"
            }
            else if (guess[i] in wordToGuess) {
                result += "+"
            }
            else {
                result += "X"
            }
        }
        return result
    }
}