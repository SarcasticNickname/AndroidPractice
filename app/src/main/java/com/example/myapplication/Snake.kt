package com.example.myapplication

class Snake {

    private val body: MutableList<Pair<Int, Int>> = mutableListOf()

    init {
        reset() // Инициализация змейки
    }

    fun move(direction: Direction) {
        val newHead = when (direction) {
            Direction.UP -> Pair(body[0].first, body[0].second - 1)
            Direction.DOWN -> Pair(body[0].first, body[0].second + 1)
            Direction.LEFT -> Pair(body[0].first - 1, body[0].second)
            Direction.RIGHT -> Pair(body[0].first + 1, body[0].second)
            Direction.STOP -> TODO()
        }

        for (i in body.size - 1 downTo 1) {
            body[i] = body[i - 1]
        }

        body[0] = newHead
    }

    fun grow() {
        body.add(body.last())
    }

    fun getBody(): List<Pair<Int, Int>> {
        return body
    }

    fun checkCollision(): Boolean {
        val head = body[0]
        for (i in 1 until body.size) {
            if (body[i] == head) {
                return true
            }
        }
        return false
    }

    fun isOutOfBounds(width: Int, height: Int): Boolean {
        val head = body[0]
        return head.first < 0 || head.second < 0 || head.first >= width || head.second >= height
    }

    fun reset() {
        body.clear()
        body.add(Pair(5, 5)) // Начальная позиция головы змейки
    }
}
