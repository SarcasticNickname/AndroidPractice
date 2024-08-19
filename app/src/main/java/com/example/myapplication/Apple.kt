package com.example.myapplication

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

class Apple(private val size: Float, var x: Int, var y: Int) {

    private val paint: Paint = Paint().apply {
        color = Color.YELLOW // Золотой цвет для яблока
        style = Paint.Style.FILL
    }

    // Метод для отрисовки яблока на игровом поле
    fun draw(canvas: Canvas) {
        val left = x * size
        val top = y * size
        val right = left + size
        val bottom = top + size
        canvas.drawRect(left, top, right, bottom, paint)
    }

    // Метод для проверки, находится ли яблоко на координатах змейки (съедено ли яблоко)
    fun isEaten(snakeX: Int, snakeY: Int): Boolean {
        return snakeX == x && snakeY == y
    }
}
