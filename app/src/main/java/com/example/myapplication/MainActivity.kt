package com.example.myapplication

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var gameView: GameView
    private val handler = Handler(Looper.getMainLooper())
    private var updateInterval = 500L // Изначальный интервал обновления
    private var currentDirection = Direction.RIGHT
    private var isPaused = false // Флаг, указывающий на паузу в игре

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gameView = findViewById(R.id.game_view)
    }

    private fun startGameLoop() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (!isPaused) { // Проверяем, не на паузе ли игра
                    gameView.updateSnake(currentDirection)
                }
                if (!gameView.isGameOver() && !isPaused) {
                    handler.postDelayed(this, updateInterval)
                }
            }
        }, updateInterval)
    }

    fun onDirectionButtonClick(view: View) {
        // Запускаем змейку при первом нажатии на кнопку управления
        if (!gameView.isRunning()) {
            gameView.startSnake()
            startGameLoop()
        }

        // Обновляем направление движения
        when (view.id) {
            R.id.button_up -> if (currentDirection != Direction.DOWN) currentDirection = Direction.UP
            R.id.button_down -> if (currentDirection != Direction.UP) currentDirection = Direction.DOWN
            R.id.button_left -> if (currentDirection != Direction.RIGHT) currentDirection = Direction.LEFT
            R.id.button_right -> if (currentDirection != Direction.LEFT) currentDirection = Direction.RIGHT
        }
    }

    fun onSpeedButtonClick(view: View) {
        when (view.id) {
            R.id.button_increase_speed -> {
                if (updateInterval > 100L) { // Ограничиваем минимальный интервал (максимальная скорость)
                    updateInterval -= 100L
                }
            }
            R.id.button_decrease_speed -> {
                if (updateInterval < 1000L) { // Ограничиваем максимальный интервал (минимальная скорость)
                    updateInterval += 100L
                }
            }
        }
    }

    fun onStopButtonClick(view: View) {
        isPaused = !isPaused // Переключаем флаг паузы

        if (isPaused) {
            // Изменяем текст кнопки на "Resume", когда игра на паузе
            (view as Button).text = "Resume"
        } else {
            // Возвращаем текст кнопки на "Stop" и продолжаем игру
            (view as Button).text = "Stop"
            startGameLoop() // Возобновляем игровой цикл
        }
    }
}


