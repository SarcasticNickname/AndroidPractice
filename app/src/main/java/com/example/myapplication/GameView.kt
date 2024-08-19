package com.example.myapplication

import android.app.AlertDialog
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class GameView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
) : View(context, attrs, defStyle) {

    private val snake: Snake = Snake()
    private val paint: Paint = Paint().apply {
        color = Color.GREEN
        style = Paint.Style.FILL
    }
    private val segmentSize = 100f
    private var isGameOver = false
    private var isRunning = false // Переменная для отслеживания движения змейки
    private var apple: Apple? = null // Яблоко инициализируется после начала игры

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (!isGameOver) {
            // Рисуем змейку
            for (segment in snake.getBody()) {
                val left = segment.first * segmentSize
                val top = segment.second * segmentSize
                val right = left + segmentSize
                val bottom = top + segmentSize
                canvas.drawRect(left, top, right, bottom, paint)
            }

            // Рисуем яблоко, если оно уже инициализировано
            apple?.draw(canvas)
        }
    }

    fun startSnake() {
        isRunning = true
        if (apple == null) {
            resetApple() // Инициализируем яблоко при первом запуске змейки
        }
    }

    fun updateSnake(direction: Direction) {
        if (isRunning && !isGameOver) {
            snake.move(direction)
            if (checkGameOver(width, height)) {
                showGameOverDialog()
            } else if (apple != null && apple!!.isEaten(snake.getBody()[0].first, snake.getBody()[0].second)) {
                snake.grow()
                resetApple() // Генерируем новое яблоко
            }
            invalidate()
        }
    }

    private fun checkGameOver(width: Int, height: Int): Boolean {
        if (snake.isOutOfBounds(width / segmentSize.toInt(), height / segmentSize.toInt()) || snake.checkCollision()) {
            isGameOver = true
            isRunning = false // Остановка змейки при окончании игры
            return true
        }
        return false
    }

    private fun showGameOverDialog() {
        // Загружаем кастомный макет диалога
        val dialogView = View.inflate(context, R.layout.custom_game_over_dialog, null)
        val builder = AlertDialog.Builder(context, R.style.CustomDialog)
        builder.setView(dialogView)

        // Получаем ссылки на элементы макета
        val scoreTextView = dialogView.findViewById<TextView>(R.id.game_over_score)
        val restartButton = dialogView.findViewById<Button>(R.id.button_restart)
        val gameOverImage = dialogView.findViewById<ImageView>(R.id.game_over_image)

        // Устанавливаем текст очков
        val score = snake.getBody().size
        scoreTextView.text = "Your Score: $score"

        // Настраиваем изображение, если нужно (например, смена картинки при разных условиях)
        gameOverImage.setImageResource(R.drawable.game_over_image)

        // Создаём диалог
        val dialog = builder.create()
        dialog.setCancelable(false)

        // Обработчик нажатия на кнопку рестарта
        restartButton.setOnClickListener {
            resetGame()
            dialog.dismiss()  // Закрываем диалог
        }

        // Показываем диалог
        dialog.show()
    }


    private fun resetGame() {
        snake.reset()
        isGameOver = false
        isRunning = false
        apple = null // Яблоко удаляется при сбросе игры
        invalidate()
    }

    private fun resetApple() {
        val maxX = (width / segmentSize).toInt()
        val maxY = (height / segmentSize).toInt()

        if (maxX > 0 && maxY > 0) {
            apple = Apple(segmentSize, (0 until maxX).random(), (0 until maxY).random())
        }
    }

    fun growSnake() {
        if (isRunning && !isGameOver) {
            snake.grow()
        }
    }

    fun isRunning(): Boolean {
        return isRunning
    }

    fun isGameOver(): Boolean {
        return isGameOver
    }
}
