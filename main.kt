package converter

import java.util.*

val measureTable: Map<String, Double> = mapOf(
        "km" to 1000.0,
        "m" to 1.0,
        "cm" to 0.01,
        "mm" to 0.001,
        "mi" to 1609.35,
        "yd" to 0.9144,
        "ft" to 0.3048,
        "in" to 0.0254
)

val weightTable: Map<String, Double> = mapOf(
        "kg" to 1000.0,
        "g" to 1.0,
        "mg" to 0.001,
        "lb" to 453.592,
        "oz" to 28.3495
)

class MeasureConverter(private val number: Double, private val measure: String, private val targetMeasure: String) {
    private fun getMeasureForOutput(measure: String, single: Boolean): String = when (measure) {
        "m" -> if (single) "meter" else "meters"
        "km" -> if (single) "kilometer" else "kilometers"
        "cm" -> if (single) "centimeter" else "centimeters"
        "mm" -> if (single) "millimeter" else "millimeters"
        "mi" -> if (single) "mile" else "miles"
        "yd" -> if (single) "yard" else "yards"
        "ft" -> if (single) "foot" else "feet"
        "in" -> if (single) "inch" else "inches"
        "g" -> if (single) "gram" else "grams"
        "kg" -> if (single) "kilogram" else "kilograms"
        "mg" -> if (single) "milligram" else "milligrams"
        "lb" -> if (single) "pound" else "pounds"
        "oz" -> if (single) "ounce" else "ounces"
        "c" -> if (single) "degree Celsius" else "degrees Celsius"
        "f" -> if (single) "degree Fahrenheit" else "degrees Fahrenheit"
        "k" -> if (single) "Kelvin" else "Kelvins"
        else -> "???"
    }

    private fun getMeasureForSearch(measure: String): String = when (measure) {
        "m", "meter", "meters" -> "m"
        "km", "kilometer", "kilometers" -> "km"
        "cm", "centimeter", "centimeters" -> "cm"
        "mm", "millimeter", "millimeters" -> "mm"
        "mi", "mile", "miles" -> "mi"
        "yd", "yard", "yards" -> "yd"
        "ft", "foot", "feet" -> "ft"
        "in", "inch", "inches" -> "in"
        "g", "gram", "grams" -> "g"
        "kg", "kilogram", "kilograms" -> "kg"
        "mg", "milligram", "milligrams" -> "mg"
        "lb", "pound", "pounds" -> "lb"
        "oz", "ounce", "ounces" -> "oz"
        "degree celsius", "degrees celsius", "celsius", "dc", "c" -> "c"
        "degree fahrenheit", "degrees fahrenheit", "fahrenheit", "df", "f" -> "f"
        "kelvin", "kelvins", "k" -> "k"
        else -> ""
    }

    private fun isTemperature (measure: String) = measure == "c" || measure == "f" || measure == "k"

    private fun convertTemperature(to: String, from: String, value: Double): Double {
        val f2c = fun (f: Double) = (f - 32) * 5 / 9
        val k2c = fun (k: Double) = k - 273.15
        val c2f = fun (c: Double) = c * 9 / 5 + 32
        val c2k = fun (c: Double) = c + 273.15
        val f2k = fun (f: Double) = (f + 459.67) * 5 / 9
        val k2f = fun (k: Double) = k * 9 / 5 - 459.67

        return when (from) {
            "c" -> when (to) {
                "f" -> c2f(value)
                "k" -> c2k(value)
                "c" -> value
                else -> throw NumberFormatException()
            }
            "k" -> when (to) {
                "c" -> k2c(value)
                "f" -> k2f(value)
                "k" -> value
                else-> throw NumberFormatException()
            }
            "f" -> when (to) {
                "c" -> f2c(value)
                "k" -> f2k(value)
                "f" -> value
                else-> throw NumberFormatException()
            }
            else-> throw NumberFormatException()
        }
    }

    fun convertMeasure() {
        val outMeasure = getMeasureForSearch(measure)
        var outNumber = 0.0
        val parsedMeasure = getMeasureForSearch(targetMeasure)
        if (outMeasure == "") {
            println("Conversion from ${getMeasureForOutput(measure, false)} to ${getMeasureForOutput(parsedMeasure, false)} is impossible")
            return
        }

        when {
            measureTable.containsKey(outMeasure) -> {
                if (!measureTable.containsKey(parsedMeasure)) {
                    println("Conversion from ${getMeasureForOutput(outMeasure, false)} to ${getMeasureForOutput(parsedMeasure, false)} is impossible")
                    return
                }
                if (number < 0) {
                    println("Length shouldn't be negative.")
                    return
                }
                outNumber = measureTable.getOrElse(outMeasure) { 1.0 } * number
                outNumber /= measureTable.getOrElse(parsedMeasure) { 1.0 }
            }
            weightTable.containsKey(outMeasure) -> {
                if (!weightTable.containsKey(parsedMeasure)) {
                    println("Conversion from ${getMeasureForOutput(outMeasure, false)} to ${getMeasureForOutput(parsedMeasure, false)} is impossible")
                    return
                }
                if (number < 0) {
                    println("Weight shouldn't be negative.")
                    return
                }
                outNumber = weightTable.getOrElse(outMeasure) { 1.0 } * number
                outNumber /= weightTable.getOrElse(parsedMeasure) { 1.0 }
            }
            isTemperature(outMeasure) -> {
                if (!isTemperature(parsedMeasure)) {
                    println("Conversion from ${getMeasureForOutput(outMeasure, false)} to ${getMeasureForOutput(parsedMeasure, false)} is impossible")
                    return
                }
                outNumber = convertTemperature(parsedMeasure, outMeasure, number)
            }
            else-> throw NumberFormatException()
        }
        val meter = getMeasureForOutput(parsedMeasure, outNumber == 1.0)

        println("$number ${getMeasureForOutput(outMeasure, number == 1.0)} is $outNumber $meter")
    }
}

val scan = Scanner(System.`in`)

fun main() {
    while(true) {
        println("Enter what you want to convert (or exit):")
        var inputString = scan.nextLine().toLowerCase()
        if (inputString == "exit") {
            return
        } else {
            try {
                if (inputString.contains(' ')) inputString = inputString.replace(' ', ' ')
                val number = inputString.substring(0, inputString.indexOf(' ')).toDouble()
                var stringWithoutNumber = inputString.substring(inputString.indexOf(' ')).trim()
                val dataArray = if (stringWithoutNumber.indexOf(" in ") != -1) stringWithoutNumber.split(" in ") else stringWithoutNumber.split("to")
                val measure = dataArray[0].trim()
                val targetMeasure = dataArray[1].trim()
                MeasureConverter(number, measure, targetMeasure).convertMeasure()
            } catch (e: Exception) {
                println("Parse error")
            }
        }
    }
}
