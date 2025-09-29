package com.minesweeper.minesweeper.ui.minesweeper

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.minesweeper.minesweeper.ui.hiddenSquare
import minesweeper.GameState
import ui.minesweeper.SquareStruct

enum class SmileyState {
    NORMAL,    // Smiling face (default)
    PRESSED,   // Surprised face (during click)
    WIN,       // Cool face with sunglasses
    LOSE       // Sad face
}

@Composable
fun SmileyFace(
    gameState: GameState,
    scale: Float = 1f,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state: SmileyState = when (gameState) {
        GameState.WON -> SmileyState.WIN
        GameState.LOST -> SmileyState.LOSE
        else -> SmileyState.NORMAL
    }

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val currentState = if (isPressed && state == SmileyState.NORMAL) SmileyState.PRESSED else state

    Canvas(
        modifier = modifier
            .size((32 * scale).dp) // Typical Minesweeper smiley size
            .clickable(interactionSource = interactionSource, indication = null) { onClick() }
    ) {
        val radius = size.minDimension / 3
        val center = Offset(size.width / 2, size.height / 2)
        val canvasWidth = size.width // Square size (width = height)
        val squareStruct = SquareStruct(canvasWidth)
        hiddenSquare(squareStruct)
        // Draw yellow face with black border
        drawCircle(
            color = Color.Yellow,
            radius = radius,
            center = center
        )
        drawCircle(
            color = Color.Black,
            radius = radius,
            center = center,
            style = Stroke(width = 2f)
        )

        // Draw eyes
        val eyeRadius = radius / 6
        val eyeOffsetX = radius / 3
        val eyeY = center.y - radius / 3
        // Left eye
        if (currentState != SmileyState.LOSE) {
            drawCircle(
                color = Color.Black,
                radius = eyeRadius,
                center = Offset(center.x - eyeOffsetX, eyeY)
            )
            // Right eye
            drawCircle(
                color = Color.Black,
                radius = eyeRadius,
                center = Offset(center.x + eyeOffsetX, eyeY)
            )
        }

        // Draw mouth or sunglasses based on state
        when (currentState) {
            SmileyState.NORMAL -> {
                // Smiling mouth (upward arc)
                val path = Path().apply {
                    arcTo(
                        rect = Rect(
                            left = center.x - radius / 2,
                            top = center.y - 10f,
                            right = center.x + radius / 2,
                            bottom = center.y + 20f
                        ),
                        startAngleDegrees = 0f,
                        sweepAngleDegrees = 180f,
                        forceMoveTo = false
                    )
                }
                drawPath(path, Color.Black, style = Stroke(width = 2f))
            }

            SmileyState.PRESSED -> {
                // Surprised mouth (small circle)
                drawCircle(
                    color = Color.Black,
                    radius = radius / 4,
                    center = Offset(center.x, center.y + radius / 3)
                )
            }

            SmileyState.WIN -> {
                // Sunglasses (rectangles over eyes)
                val glassWidth = radius / 2
                val glassHeight = radius / 4
                // Left glass
                drawRect(
                    color = Color.Black,
                    topLeft = Offset(center.x - eyeOffsetX - glassWidth / 2, eyeY - glassHeight / 2),
                    size = Size(glassWidth, glassHeight)
                )
                // Right glass
                drawRect(
                    color = Color.Black,
                    topLeft = Offset(center.x + eyeOffsetX - glassWidth / 2, eyeY - glassHeight / 2),
                    size = Size(glassWidth, glassHeight)
                )
                // Bridge between glasses
                drawLine(
                    color = Color.Black,
                    start = Offset(center.x - eyeOffsetX + glassWidth / 2, eyeY),
                    end = Offset(center.x + eyeOffsetX - glassWidth / 2, eyeY),
                    strokeWidth = 2f
                )
                // Smiling mouth (same as NORMAL)
                val path = Path().apply {
                    arcTo(
                        rect = Rect(
                            left = center.x - radius / 2,
                            top = center.y,
                            right = center.x + radius / 2,
                            bottom = center.y + radius
                        ),
                        startAngleDegrees = 0f,
                        sweepAngleDegrees = 180f,
                        forceMoveTo = false
                    )
                }
                drawPath(path, Color.Black, style = Stroke(width = 2f))
            }

            SmileyState.LOSE -> {
                // Sad mouth (downward arc)
                val eyeSize = radius / 3 // Size of eye (diameter for circles, square for X)
                val eyeOffsetX = radius / 3
                val eyeY = center.y - radius / 3
                val leftEyeCenter = Offset(center.x - eyeOffsetX, eyeY)
                val rightEyeCenter = Offset(center.x + eyeOffsetX, eyeY)
                val path = Path().apply {

                    // Left eye: Draw X
                    drawLine(
                        color = Color.Black,
                        start = Offset(leftEyeCenter.x - eyeSize / 2, leftEyeCenter.y - eyeSize / 2),
                        end = Offset(leftEyeCenter.x + eyeSize / 2, leftEyeCenter.y + eyeSize / 2),
                        strokeWidth = 2f
                    )
                    drawLine(
                        color = Color.Black,
                        start = Offset(leftEyeCenter.x - eyeSize / 2, leftEyeCenter.y + eyeSize / 2),
                        end = Offset(leftEyeCenter.x + eyeSize / 2, leftEyeCenter.y - eyeSize / 2),
                        strokeWidth = 2f
                    )
                    // Right eye: Draw X
                    drawLine(
                        color = Color.Black,
                        start = Offset(rightEyeCenter.x - eyeSize / 2, rightEyeCenter.y - eyeSize / 2),
                        end = Offset(rightEyeCenter.x + eyeSize / 2, rightEyeCenter.y + eyeSize / 2),
                        strokeWidth = 2f
                    )
                    drawLine(
                        color = Color.Black,
                        start = Offset(rightEyeCenter.x - eyeSize / 2, rightEyeCenter.y + eyeSize / 2),
                        end = Offset(rightEyeCenter.x + eyeSize / 2, rightEyeCenter.y - eyeSize / 2),
                        strokeWidth = 2f
                    )
                    arcTo(
                        rect = Rect(
                            left = center.x - radius / 2,
                            top = center.y,
                            right = center.x + radius / 2,
                            bottom = center.y + 30f
                        ),
                        startAngleDegrees = 180f,
                        sweepAngleDegrees = 180f,
                        forceMoveTo = false
                    )
                }
                drawPath(path, Color.Black, style = Stroke(width = 2f))
            }
        }
    }
}