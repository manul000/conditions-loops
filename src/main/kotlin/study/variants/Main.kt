package study.variants

import java.math.BigDecimal
import java.math.RoundingMode

private fun readInput(): String? = System.console()?.takeIf { it.isTerminal }?.readLine() ?: readlnOrNull()

fun analyze(text: String): String {
    var digits = 0
    var spaces = 0
    val unique = mutableSetOf<Char>()
    val letters = linkedMapOf<Char, Int>()
    for (c in 'a'..'z') letters[c] = 0
    for (c in 'A'..'Z') letters[c] = 0
    for (c in text) {
        unique.add(c)
        if (c.isDigit()) digits++
        if (c == ' ') spaces++
        if (c in letters) letters[c] = letters.getValue(c) + 1
    }
    val histogram = StringBuilder()
    for ((letter, count) in letters) {
        histogram.append("$letter: ")
        for (i in 0 until count) histogram.append('*')
        histogram.appendLine()
    }
    var containsKot = false
    val word = "kot"
    for (start in 0..text.length - word.length) {
        var matches = true
        for (offset in word.indices) {
            if (text[start + offset].lowercaseChar() != word[offset]) matches = false
        }
        if (matches) containsKot = true
    }
    return """
        Символов: ${text.length}
        Цифр: $digits
        Пробелов: $spaces
        Уникальных символов: ${unique.size}
        Есть kot: $containsKot
    """.trimIndent() + "\nКоличество букв: " + letters.entries.joinToString { "${it.key}=${it.value}" } + "\nГистограмма букв (регистр учитывается):\n$histogram"
}

fun shop() {
    print("Количество товаров: ")
    val n = readInput()?.trim()?.toIntOrNull()
    if (n == null || n <= 0) {
        println("Ошибка: количество должно быть положительным")
        return
    }
    val prices = mutableListOf<BigDecimal>()
    for (i in 1..n) {
        while (true) {
            print("Цена товара $i: ")
            val input = readInput() ?: return
            val price = input.trim().replace(',', '.').toBigDecimalOrNull()
            if (price == null || price < BigDecimal.ZERO || price.scale() > 2) {
                println("Ошибка: введите неотрицательную цену с точностью до копеек")
            } else {
                prices.add(price)
                break
            }
        }
    }
    var sum = BigDecimal.ZERO
    var min = prices.first()
    var max = prices.first()
    for (price in prices) {
        sum += price
        if (price < min) min = price
        if (price > max) max = price
    }
    var above = 0
    for (price in prices) if (price.multiply(n.toBigDecimal()) > sum) above++
    val discount = when {
        sum > BigDecimal("10000") -> BigDecimal("0.10")
        sum >= BigDecimal("5000") -> BigDecimal("0.05")
        else -> BigDecimal.ZERO
    }
    println("Товар\tЦена")
    for (i in prices.indices) println("${i + 1}\t${prices[i].setScale(2)}")
    println(
        """
        Общая сумма: ${sum.setScale(2)}
        Средняя цена: ${sum.divide(n.toBigDecimal(), 2, RoundingMode.HALF_UP)}
        Дороже среднего: $above
        Минимум: ${min.setScale(2)}; максимум: ${max.setScale(2)}
        Скидка: ${discount * BigDecimal(100)}%
        К оплате: ${(sum * (BigDecimal.ONE - discount)).setScale(2, RoundingMode.HALF_UP)}
    """.trimIndent()
    )
}

fun inspect(login: String, password: String): String {
    val checks = BooleanArray(6)
    checks[0] = password.length >= 8
    checks[4] = true
    for (criterion in 1..4) {
        for (c in password) {
            when (criterion) {
                1 -> if (c.isDigit()) checks[1] = true
                2 -> if (c.isUpperCase()) checks[2] = true
                3 -> if (c.isLowerCase()) checks[3] = true
                4 -> if (c.isWhitespace()) checks[4] = false
            }
        }
    }
    checks[5] = password != login
    var count = 0
    for (ok in checks) if (ok) count++
    val level = when (count) {
        in 0..2 -> "слабый"; in 3..4 -> "средний"; else -> "сильный"
    }
    return """
        Логин: $login
        Пароль: *** скрыт ***
        Проверено критериев: 6
        Выполнено критериев: $count
        Уровень надёжности: $level
    """.trimIndent()
}

fun main(args: Array<String>) {
    val option = args.firstOrNull() ?: run {
        println("1 — анализ строки; 2 — калькулятор товаров; 3 — проверка пароля")
        readInput()
    }
    when (option) {
        "1" -> {
            print("Строка: "); println(analyze(readInput() ?: ""))
        }

        "2" -> shop()
        "3" -> {
            val console = System.console()?.takeIf { it.isTerminal }
            val login = if (console != null) console.readLine("Логин: ") ?: return else {
                print("Логин: ")
                readInput() ?: return
            }
            val password = if (console != null) console.readPassword("Пароль: ")?.let(::String) ?: return else {
                print("Пароль (без терминала ввод может быть виден): ")
                readInput() ?: return
            }
            println(inspect(login, password))
        }

        else -> println("Неизвестный вариант")
    }
}
