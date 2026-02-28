package com.calculator

// ======= IMPORTS =======

// Android Framework
import android.content.Intent  // For launching activities
import android.os.Bundle  // For storing activity state

// Jetpack Compose - Activity Integration
import androidx.activity.ComponentActivity  // Base class for activities using Compose
import androidx.activity.compose.setContent  // Sets Compose content for the activity
import androidx.activity.enableEdgeToEdge  // Enables edge-to-edge display mode

// Jetpack Compose - Core UI
import androidx.compose.foundation.layout.*  // Layout composables like Column, Row, Box
import androidx.compose.foundation.shape.RoundedCornerShape  // For creating rounded corners
import androidx.compose.material3.*  // Material Design 3 components
import androidx.compose.material3.ButtonDefaults  // Customizing button appearance
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*  // Compose runtime state management
import androidx.compose.ui.Modifier  // Used to modify composables
import androidx.compose.ui.Alignment  // Aligning elements in layouts
import androidx.compose.ui.graphics.Color  // Color definitions
import androidx.compose.ui.unit.dp  // Density-independent pixel units
import androidx.compose.ui.unit.Dp  // Type-safe dimension values
import androidx.compose.ui.unit.TextUnit  // For text size

// Project-specific
import com.calculator.ui.theme.CalculatorTheme  // Custom theme for this application

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign  // Import for text alignment
import androidx.compose.ui.unit.sp
import kotlin.math.sqrt
import kotlin.math.pow

/**
 * Data class representing a calculator button.
 *
 * @param text The text displayed on the button
 * @param onClick Lambda function executed when button is clicked
 * @param color The background color of the button (default is light blue)
 * @param textSize The size of the text on the button (default is 24sp)
 */
data class CalcButton(
    val text: String,
    val onClick: () -> Unit,
    val color: Color = Color(0xFF039BE5),  // Default color - light blue
    val textSize: TextUnit = 24.sp  // Default text size
)

/**
 * Main activity for the Calculator application.
 * Implements the UI and calculator logic using Jetpack Compose.
 */
class Main : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()  // Enable edge-to-edge display mode for modern Android UI
        setContent {
            // Apply custom theme to the entire application
            CalculatorTheme {
                // ======= STATE MANAGEMENT =======

                // Holds the actual calculation expression to be evaluated
                var calculate = ""

                // Holds the display-friendly version of the equation (with proper symbols)
                var showEquation by remember { mutableStateOf("") }

                // Holds the calculation result
                var answer by remember { mutableStateOf("") }

                // Special state to track square root operations
                // This helps us know when to close the parenthesis for sqrt
                var waitingForSqrtNumber by remember { mutableStateOf(false) }

                // ======= BUTTON CONFIGURATION =======
                // Define all calculator buttons with their properties and behaviors
                val buttonRows = listOf(
                    // First row: Clear, Square Root, Exponent, Division
                    listOf(
                        CalcButton("C", {
                            // Clear button resets all values and states
                            calculate = ""
                            showEquation = ""
                            answer = ""
                            waitingForSqrtNumber = false
                        }, Color.Red),  // Red color for clear button for visibility

                        CalcButton("√", {
                            // Square root button - opens a parenthesis
                            // We'll close it when the next operation or = is pressed
                            calculate += "sqrt("  // For calculation string
                            showEquation += "√"   // For display
                            waitingForSqrtNumber = true  // Flag that we need to close this parenthesis later
                        }, Color(0xFFF57C00)),  // Orange color for function buttons

                        CalcButton("^", {
                            // Exponent button
                            // Close sqrt parenthesis if one is open
                            if (waitingForSqrtNumber) {
                                calculate += ")"
                                waitingForSqrtNumber = false
                            }
                            calculate += "**"  // ** is the power operator in our evaluation function
                            showEquation += "^"  // ^ symbol for display
                        }, Color(0xFF7986CB)),  // Indigo color for operations

                        CalcButton("÷", {
                            // Division button
                            // Close sqrt parenthesis if needed
                            if (waitingForSqrtNumber) {
                                calculate += ")"
                                waitingForSqrtNumber = false
                            }
                            calculate += "/"  // / is the division operator in our evaluation function
                            showEquation += "÷"  // ÷ symbol for display
                        }, Color(0xFF7986CB))  // Same color for all basic operations
                    ),

                    // Second row: 7, 8, 9, Multiplication
                    listOf(
                        CalcButton("7", {
                            // Check if we've reached the 15-digit limit for the current number
                            if (countCurrentNumberDigits(calculate) < 15) {
                                calculate += "7"
                                showEquation += "7"
                            }
                        }),  // Default blue color for number buttons

                        CalcButton("8", {
                            // Check if we've reached the 15-digit limit for the current number
                            if (countCurrentNumberDigits(calculate) < 15) {
                                calculate += "8"
                                showEquation += "8"
                            }
                        }),

                        CalcButton("9", {
                            // Check if we've reached the 15-digit limit for the current number
                            if (countCurrentNumberDigits(calculate) < 15) {
                                calculate += "9"
                                showEquation += "9"
                            }
                        }),

                        CalcButton("×", {
                            // Multiplication button
                            // Close sqrt parenthesis if needed
                            if (waitingForSqrtNumber) {
                                calculate += ")"
                                waitingForSqrtNumber = false
                            }
                            calculate += "*"  // * is the multiplication operator in our evaluation function
                            showEquation += "×"  // × symbol for display
                        }, Color(0xFF7986CB))  // Indigo color for operations
                    ),

                    // Third row: 4, 5, 6, Subtraction
                    listOf(
                        CalcButton("4", {
                            // Check if we've reached the 15-digit limit for the current number
                            if (countCurrentNumberDigits(calculate) < 15) {
                                calculate += "4"
                                showEquation += "4"
                            }
                        }),

                        CalcButton("5", {
                            // Check if we've reached the 15-digit limit for the current number
                            if (countCurrentNumberDigits(calculate) < 15) {
                                calculate += "5"
                                showEquation += "5"
                            }
                        }),

                        CalcButton("6", {
                            // Check if we've reached the 15-digit limit for the current number
                            if (countCurrentNumberDigits(calculate) < 15) {
                                calculate += "6"
                                showEquation += "6"
                            }
                        }),

                        CalcButton("-", {
                            // Subtraction button
                            // Close sqrt parenthesis if needed
                            if (waitingForSqrtNumber) {
                                calculate += ")"
                                waitingForSqrtNumber = false
                            }
                            calculate += "-"
                            showEquation += "-"
                        }, Color(0xFF7986CB))
                    ),

                    // Fourth row: 1, 2, 3, Addition
                    listOf(
                        CalcButton("1", {
                            // Check if we've reached the 15-digit limit for the current number
                            if (countCurrentNumberDigits(calculate) < 15) {
                                calculate += "1"
                                showEquation += "1"
                            }
                        }),

                        CalcButton("2", {
                            // Check if we've reached the 15-digit limit for the current number
                            if (countCurrentNumberDigits(calculate) < 15) {
                                calculate += "2"
                                showEquation += "2"
                            }
                        }),

                        CalcButton("3", {
                            // Check if we've reached the 15-digit limit for the current number
                            if (countCurrentNumberDigits(calculate) < 15) {
                                calculate += "3"
                                showEquation += "3"
                            }
                        }),

                        CalcButton("+", {
                            // Addition button
                            // Close sqrt parenthesis if needed
                            if (waitingForSqrtNumber) {
                                calculate += ")"
                                waitingForSqrtNumber = false
                            }
                            calculate += "+"
                            showEquation += "+"
                        }, Color(0xFF7986CB))
                    ),

                    // Fifth row: 00, 0, Decimal Point, Equals
                    listOf(
                        CalcButton("00", {
                            if (calculate.isEmpty() || calculate == "0") {
                                calculate = "0"
                                showEquation = "0"
                            }
                            else {
                                if (countCurrentNumberDigits(calculate) < 15) {
                                    calculate += "00"
                                    showEquation += "00"
                                }
                            }
                        }),

                        CalcButton("0", {
                            if (calculate.isEmpty() || calculate == "0") {
                                calculate = "0"
                                showEquation = "0"
                            }
                            else {
                                if (countCurrentNumberDigits(calculate) < 15) {
                                    calculate += "0"
                                    showEquation += "0"
                                }
                            }
                        }),

                        CalcButton(".", {
                            // Decimal points don't count toward the 15-digit limit
                            calculate += "."
                            showEquation += "."
                        }, Color(0xFFE57373)),  // Light red for decimal point

                        CalcButton("=", {
                            try {
                                // Close any open sqrt parenthesis before evaluating
                                if (waitingForSqrtNumber) {
                                    calculate += ")"
                                    waitingForSqrtNumber = false
                                }

                                // For debugging - useful for tracking issues
                                println("Evaluating expression: $calculate")

                                // Use our custom evaluation function to calculate the result
                                val result = evalExpression(calculate)

                                // Format the result using our new formatting function
                                answer = "= ${formatResult(result)}"
                            } catch (e: Exception) {
                                // Handle any calculation errors gracefully
                                answer = "= Error"
                                println("Calculation error: ${e.message}")
                            }
                        }, Color(0xFF4CAF50))  // Green for the equals button
                    )
                )

                // ======= UI LAYOUT =======

                // Main scaffold provides the overall structure
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Box allows absolute positioning of elements
                    Box(Modifier.fillMaxSize()) {
                        // Main column containing all UI elements with consistent spacing
                        Column(
                            modifier = Modifier
                                .padding(innerPadding) // Respect the Scaffold's padding
                                .padding(16.dp)       // Add additional padding around content
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceBetween, // Distribute space evenly
                            horizontalAlignment = Alignment.CenterHorizontally  // Center items horizontally
                        ) {
                            // ======= TOP SECTION - DISPLAY AREA =======
                            Column(
                                modifier = Modifier.weight(1f),  // Takes 1/3 of available space
                                verticalArrangement = Arrangement.Center
                            ) {
                                // Title displayed at the top
                                Title(
                                    name = "Calculator",
                                    color = Color.Blue,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )

                                // Thick divider below the title
                                HorizontalDivider(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    thickness = 2.dp,
                                    color = Color.Black
                                )

                                // Display area for equation and answer
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(120.dp)  // Fixed height for the display area
                                        .padding(vertical = 8.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.align(Alignment.BottomEnd),  // Align at bottom-right
                                        horizontalAlignment = Alignment.End
                                    ) {
                                        // Display current equation being entered with dynamic font sizing
                                        Text(
                                            text = showEquation,
                                            fontSize = dynamicFontSize(showEquation),  // Dynamic font size based on length
                                            textAlign = TextAlign.End,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(bottom = 8.dp)
                                        )

                                        // Display calculation result with dynamic font sizing
                                        Text(
                                            text = answer,
                                            fontSize = dynamicFontSize(answer.removePrefix("= ")),  // Dynamic font size based on length
                                            fontWeight = FontWeight.Bold,
                                            textAlign = TextAlign.End,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                }

                                // Thin divider separating display from buttons
                                HorizontalDivider(
                                    modifier = Modifier.fillMaxWidth(),
                                    thickness = 1.dp,
                                    color = Color.Black
                                )
                            }

                            // ======= BOTTOM SECTION - CALCULATOR BUTTONS =======
                            Column(
                                modifier = Modifier.weight(2f),  // Takes 2/3 of available space
                                verticalArrangement = Arrangement.SpaceEvenly // Equal spacing between rows
                            ) {
                                // Display the buttons using our data structure
                                buttonRows.forEach { row ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceEvenly // Equal spacing between buttons
                                    ) {
                                        // Display each button in the row
                                        row.forEach { calcButton ->
                                            Button(
                                                text = calcButton.text,
                                                onClick = calcButton.onClick,
                                                color = calcButton.color,
                                                cornerRadius = 16.dp,  // Rounded corners for all buttons
                                                modifier = Modifier
                                                    .size(72.dp)  // Fixed size for all buttons
                                                    .aspectRatio(1f), // Keep buttons square
                                                textSize = calcButton.textSize
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Helper function to calculate appropriate font size based on text length.
     * Progressively reduces font size as text length increases to ensure it fits in the display.
     *
     * @param text The text whose length determines the font size
     * @return Appropriate text size based on length
     */
    @Composable
    private fun dynamicFontSize(text: String): TextUnit {
        return when {
            text.length > 20 -> 16.sp  // Very small font for extremely long text
            text.length > 15 -> 20.sp  // Small font for long text
            text.length > 10 -> 24.sp  // Medium font for medium-length text
            else -> 32.sp             // Large font for short text
        }
    }

    /**
     * Helper function to count the number of digits in the current number being entered.
     * Used to enforce the 15-digit limit per number.
     *
     * This function analyzes the calculation expression to find where the current number
     * begins (typically after the last mathematical operator) and counts only the digits
     * in that current number.
     *
     * @param expr The current calculation expression string
     * @return The number of digits in the current number (after the last operator)
     */
    private fun countCurrentNumberDigits(expr: String): Int {
        // If expression is empty, we have 0 digits
        if (expr.isEmpty()) return 0

        // Define which characters are considered operators that would start a new number
        // We include opening parenthesis as it also indicates start of a new number context
        val operatorChars = "+-*/^("

        // Find the index of the last operator in the expression
        // This helps us determine where the current number being entered starts
        val lastOperatorIndex = expr.indexOfLast { it in operatorChars }

        // Determine the starting index for counting digits in the current number:
        // - If no operator found (lastOperatorIndex == -1), start from the beginning (index 0)
        // - If an operator was found, start counting from the character right after it
        //   This ensures we only count digits in the current number being entered
        val startIndex = if (lastOperatorIndex == -1)
            0
        else
            lastOperatorIndex + 1

        // Extract the substring from the starting index to the end of the expression
        // This represents the current number being entered
        val currentNumber = expr.substring(startIndex)

        // Count only the digits in the current number (excluding decimal points or other characters)
        // This gives us the exact digit count to enforce the 15-digit limit
        return currentNumber.count { it.isDigit() }
    }

    /**
     * Formats the calculation result to ensure it doesn't exceed display constraints.
     * - Uses scientific notation for very large numbers
     * - Removes unnecessary decimal places for whole numbers
     * - Limits precision for decimal numbers
     *
     * @param result The raw calculation result
     * @return Formatted string representation of the result
     */
    private fun formatResult(result: Double): String {
        return when {
            // For very large or small numbers, use scientific notation with 10 significant digits
            result.toString().length > 15 -> String.format("%.10e", result)
            // For whole numbers, show as integers without decimal point
            result.isWholeNumber() -> result.toLong().toString()
            // For decimal numbers, limit to 10 decimal places maximum
            else -> {
                val formatted = String.format("%.10f", result).trimEnd('0').trimEnd('.')
                // If the formatted result is still too long, use scientific notation
                if (formatted.length > 15) String.format("%.10e", result) else formatted
            }
        }
    }

    /**
     * Extension function to check if a Double is a whole number.
     * Used to format numbers without unnecessary decimal places.
     *
     * @return true if the number has no fractional part
     */
    private fun Double.isWholeNumber(): Boolean {
        return this == this.toLong().toDouble()
    }

    /**
     * Custom expression evaluator that handles basic mathematical operations.
     * Implements operator precedence (parentheses, exponents, multiplication/division, addition/subtraction).
     *
     * @param expression The mathematical expression to evaluate as a string
     * @return The calculated result as a Double
     * @throws ArithmeticException if division by zero occurs
     * @throws IllegalArgumentException if the expression is invalid
     */
    private fun evalExpression(expression: String): Double {
        // ======= PARENTHESES HANDLING =======
        // Handle parentheses and nested expressions first with a recursive approach
        if (expression.contains("(")) {
            // Find the innermost parentheses and evaluate them first
            var openIndex = -1
            var closeIndex = -1
            var nestLevel = 0  // Track nesting level to find matching parentheses

            // Scan the expression to find matching parentheses
            for (i in expression.indices) {
                when (expression[i]) {
                    '(' -> {
                        nestLevel++
                        if (openIndex == -1) openIndex = i  // Record the first opening parenthesis
                    }
                    ')' -> {
                        nestLevel--
                        if (nestLevel == 0) {  // Found matching closing parenthesis
                            closeIndex = i
                            break
                        }
                    }
                }
            }

            // Process parenthetical expression if found
            if (openIndex != -1 && closeIndex != -1) {
                val subExpression = expression.substring(openIndex + 1, closeIndex)
                val subResult = evalExpression(subExpression)  // Recursive call

                // Handle special functions (currently only sqrt)
                if (openIndex >= 4 && expression.substring(openIndex - 4, openIndex) == "sqrt") {
                    // For square root function
                    val sqrtResult = sqrt(subResult)

                    // Construct new expression with the result of sqrt substituted
                    val newExpression = expression.substring(0, openIndex - 4) + sqrtResult +
                            (if (closeIndex + 1 < expression.length) expression.substring(closeIndex + 1) else "")

                    return evalExpression(newExpression)  // Evaluate the new expression
                } else {
                    // For regular parentheses
                    val newExpression = expression.substring(0, openIndex) + subResult +
                            (if (closeIndex + 1 < expression.length) expression.substring(closeIndex + 1) else "")

                    return evalExpression(newExpression)  // Evaluate the new expression
                }
            }
        }

        // ======= OPERATOR PRECEDENCE HANDLING =======
        // Handle operations in order of precedence: exponents, multiply/divide, add/subtract

        // ======= EXPONENTS =======
        if (expression.contains("**")) {
            val parts = expression.split("**", limit = 2)  // Split at first ** occurrence
            if (parts.size == 2) {
                val base = evalExpression(parts[0])
                val exponent = evalExpression(parts[1])
                return base.pow(exponent)  // Use Kotlin's power function
            }
        }

        // ======= MULTIPLICATION AND DIVISION =======
        if (expression.contains("*") || expression.contains("/")) {
            // Find operators while respecting numbers that might contain decimal points
            var i = 0
            while (i < expression.length) {
                // Check for multiplication (but not power operator **)
                if (expression[i] == '*' && i+1 < expression.length && expression[i+1] != '*') {
                    val leftPart = expression.substring(0, i)
                    val rightPart = expression.substring(i + 1)
                    return evalExpression(leftPart) * evalExpression(rightPart)  // Recursive call
                }
                // Check for division
                else if (expression[i] == '/') {
                    val leftPart = expression.substring(0, i)
                    val rightPart = expression.substring(i + 1)
                    val divisor = evalExpression(rightPart)

                    // Check for division by zero
                    if (divisor == 0.0) throw ArithmeticException("Division by zero")

                    return evalExpression(leftPart) / divisor  // Recursive call
                }
                i++
            }
        }

        // ======= ADDITION AND SUBTRACTION =======
        // Scan from left to right, but be careful with negative numbers
        var i = 0
        while (i < expression.length) {
            if (expression[i] == '+') {
                val leftPart = expression.substring(0, i)
                val rightPart = expression.substring(i + 1)
                return evalExpression(leftPart) + evalExpression(rightPart)  // Recursive call
            }
            // Only treat as subtraction if it's not at the beginning
            // (which would be a negative number)
            else if (expression[i] == '-' && i > 0) {
                val leftPart = expression.substring(0, i)
                val rightPart = expression.substring(i + 1)
                return evalExpression(leftPart) - evalExpression(rightPart)  // Recursive call
            }
            i++
        }

        // ======= FINAL CONVERSION =======
        // If it's just a number, convert and return it
        try {
            return expression.toDouble()
        } catch (_: NumberFormatException) {
            throw IllegalArgumentException("Invalid expression: $expression")
        }
    }
}

/**
 * Composable function for the calculator title.
 *
 * @param name The text to display
 * @param color The color of the text
 * @param modifier Optional modifiers for the text
 */
@Composable
fun Title(
    name: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Text(
        text = name,
        modifier = modifier,
        fontWeight = FontWeight.Bold,
        color = color,
        fontSize = 24.sp
    )
}

/**
 * Composable function for calculator buttons.
 *
 * @param text The text displayed on the button
 * @param onClick Lambda function executed when button is clicked
 * @param color The background color of the button
 * @param cornerRadius The radius for rounded corners of the button
 * @param modifier Optional modifiers for the button
 * @param textSize The size of the text on the button
 */
@Composable
fun Button(
    text: String,
    onClick: () -> Unit,
    color: Color,
    cornerRadius: Dp,
    modifier: Modifier = Modifier,
    textSize: TextUnit = 24.sp
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(cornerRadius),
        modifier = modifier,
        contentPadding = PaddingValues(0.dp), // Remove internal padding for consistent button size
        colors = ButtonDefaults.buttonColors(containerColor = color)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = text,
                fontSize = textSize,
                textAlign = TextAlign.Center
            )
        }
    }
}