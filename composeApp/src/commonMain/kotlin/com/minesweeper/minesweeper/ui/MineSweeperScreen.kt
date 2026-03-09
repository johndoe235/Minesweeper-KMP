package com.minesweeper.minesweeper.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.minesweeper.minesweeper.MineSweeperField
import com.minesweeper.utils.KMath
import minesweeper.GameState
import ui.MineSweeperUiState
import com.minesweeper.minesweeper.ui.minesweeper.SmileyFace
import ui.minesweeper.SquareStruct


val gray = 0xff4c545c
val lightCorner = 0xff707880
val darkCorner = 0xff222a32
val emptySquareColor = 0xff384048
val emptySquareBorderColor = 0xff2b333b


@Composable
fun MineSweeperGame(
    uiState: MineSweeperUiState,
    onClick: (Int, Int) -> Unit,
    onSmileyClick: () -> Unit,
    onFlagClick: () -> Unit
) {
    Column(
        Modifier.fillMaxSize().background(Color(0xFF181a1b)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        GameTopBar(uiState.gameState, uiState.scale, onSmileyClick, onFlagClick)
        MineField(uiState.board, uiState.scale, onClick)
    }
}


@Composable
fun GameTopBar(
    gameState: GameState, scale: Float, onSmileyClick: () -> Unit,
    onFlagClick: () -> Unit
) {
    var toggleFlag by remember { mutableStateOf(false) }
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SmileyFace(
            gameState,
            scale,
            onSmileyClick,
            Modifier.align(Alignment.CenterVertically)
        )

        TextButton(
            onClick = { onFlagClick.invoke(); toggleFlag = !toggleFlag },
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {

            Canvas(modifier = Modifier.size(60.dp)) {
                val canvasWidth = size.width // Square size (width = height)
                val squareStruct = SquareStruct(canvasWidth)
                if (!toggleFlag) {
                    hiddenSquare(squareStruct)
                }
                flagSquare()
            }
        }
    }
}

@Composable
fun Counter(
    textToDisplay: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(Color.Black)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = textToDisplay.toString().padStart(3, '0'), // Formats like "005"
            color = Color.Red,
            fontSize = 24.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
        )
    }
}


@Composable
fun MineField(uiBoard: UiBoard, scale: Float, onClick: (Int, Int) -> Unit) {
    Column {
        for (i in 0 until uiBoard.rows) {
            Row {
                for (j in 0 until uiBoard.cols) {
                    Square(
                        scale = scale,
                        state = uiBoard.board[(i * uiBoard.cols) + j],
                        onClick = { onClick(i, j) })
                }
            }
        }
    }
}


@Composable
fun Square(scale: Float, state: Char, onClick: () -> Unit) {
    val textMeasurer = rememberTextMeasurer()
    Canvas(modifier = Modifier.size((25 * scale).dp).clickable(onClick = onClick)) {
        val canvasWidth = size.width // Square size (width = height)
        val squareStruct = SquareStruct(canvasWidth)
        //Rectangle used for the button
        when (state) {
            MineSweeperField.HIDDEN -> hiddenSquare(squareStruct)
            MineSweeperField.FLAG -> {
                hiddenSquare(squareStruct);flagSquare(modifier = Modifier)
            }

            MineSweeperField.MINE -> {
                emptySquare(squareStruct)
                mine()
            }

            else -> {
                emptySquare(squareStruct)
                if (state in "12345678") {
                    val style = androidx.compose.ui.text.TextStyle(
                        fontSize = (25 * scale).sp,
                        color = getNumberColor(state),
                    )
                    val textLayoutResult = textMeasurer.measure("$state", style)

                    drawText(
                        textMeasurer = textMeasurer,
                        text = "$state",
                        style = style,
                        topLeft = Offset(
                            x = center.x - textLayoutResult.size.width / 2,
                            y = center.y - textLayoutResult.size.height / 2,
                        )
                    )
                }
            }

        }
    }
}

private fun DrawScope.mine() {
    val radius = size.minDimension / 4
    val center = Offset(size.width / 2, size.height / 2)

    val gradient = Brush.radialGradient(
        colors = listOf(
            Color(0xFF4A4A4A), // Light gray for highlight
            Color.Black // Dark black for base
        ),
        center = Offset(center.x - radius * 0.3f, center.y - radius * 0.3f), // Offset light source
        radius = radius * 1.2f
    )
    // Draw the central circle of the mine
    drawCircle(
        brush = gradient,//Color.Black,
        radius = radius,
        center = center
    )

    // Draw 8 spikes around the circle
    val spikeLength = radius * 1.5f
    for (angle in 0 until 360 step 45) {

        val rad = KMath.toRadians(angle.toDouble()).toFloat()
        val start = center
        val end = Offset(
            x = center.x + spikeLength * kotlin.math.cos(rad).toFloat(),
            y = center.y + spikeLength * kotlin.math.sin(rad).toFloat()
        )
        drawLine(
            color = Color.Black,
            start = start,
            end = end,
            strokeWidth = 2f
        )
    }

    val squareSize = radius / 2
    drawRect(
        color = Color.White,
        topLeft = Offset(
            x = center.x - 3f,
            y = center.y - 3f
        ),
        size = Size(squareSize, squareSize)
    )
}

private fun DrawScope.emptySquare(squareStruct: SquareStruct) {
    drawRect(
        color = Color(emptySquareColor),
        size = size,
    )
    drawLine(
        color = Color(emptySquareBorderColor),
        start = squareStruct.topFirstLineStart,
        end = squareStruct.topFirstLineEnd,
        strokeWidth = squareStruct.strokeWidth
    )

    drawLine(
        color = Color(emptySquareBorderColor),
        start = squareStruct.leftFirstLineStart,
        end = squareStruct.leftFirstLineEnd,
        strokeWidth = squareStruct.strokeWidth
    )
}

fun DrawScope.hiddenSquare(squareStruct: SquareStruct) {
    drawRect(
        color = Color(gray),
        size = size,
    )
    //shading lines light
    drawLine(
        color = Color(lightCorner),
        start = squareStruct.topFirstLineStart,
        end = squareStruct.topFirstLineEnd,
        strokeWidth = squareStruct.strokeWidth
    )
    drawLine(
        color = Color(lightCorner),
        start = squareStruct.lightSecondLineStart,
        end = squareStruct.lightSecondLineEnd,
        strokeWidth = squareStruct.strokeWidth
    )
    drawLine(
        color = Color(lightCorner),
        start = squareStruct.lightThirdLineStart,
        end = squareStruct.lightThirdLineEnd,
        strokeWidth = squareStruct.strokeWidth,
    )

    drawLine(
        color = Color(lightCorner),
        start = squareStruct.leftFirstLineStart,
        end = squareStruct.leftFirstLineEnd,
        strokeWidth = squareStruct.strokeWidth,
    )

    drawLine(
        color = Color(lightCorner),
        start = squareStruct.leftSecondLineStart,
        end = squareStruct.leftSecondLineEnd,
        strokeWidth = squareStruct.strokeWidth,
    )
    drawLine(
        color = Color(lightCorner),
        start = squareStruct.leftThirdLineStart,
        end = squareStruct.leftThirdLineEnd,
        strokeWidth = squareStruct.strokeWidth,
    )


    //shading lines dark right
    drawLine(
        color = Color(darkCorner),
        start = squareStruct.rightFirstLineStart,
        end = squareStruct.rightFirstLineEnd,
        strokeWidth = squareStruct.strokeWidth
    )
    drawLine(
        color = Color(darkCorner),
        start = squareStruct.rightSecondLineStart,
        end = squareStruct.rightSecondLineEnd,
        strokeWidth = squareStruct.strokeWidth
    )
    drawLine(
        color = Color(darkCorner),
        start = squareStruct.rightThirdLineStart,
        end = squareStruct.rightThirdLineEnd,
        strokeWidth = squareStruct.strokeWidth
    )

    //shading lines dark bottom
    this.drawLine(
        color = Color(darkCorner),
        start = squareStruct.rightBottomFirstLineStart,
        end = squareStruct.rightBottomFirstLineEnd,
        strokeWidth = squareStruct.strokeWidth
    )
    drawLine(
        color = Color(darkCorner),
        start = squareStruct.rightBottomSecondLineStart,
        end = squareStruct.rightBottomSecondLineEnd,
        strokeWidth = squareStruct.strokeWidth
    )
    drawLine(
        color = Color(darkCorner),
        start = squareStruct.rightBottomThirdLineStart,
        end = squareStruct.rightBottomThirdLineEnd,
        strokeWidth = squareStruct.strokeWidth
    )
}


fun DrawScope.flagSquare(modifier: Modifier = Modifier) {
    val canvasWidth = size.width * 0.9f
    val canvasHeight = size.height * 0.9f

    // Define colors for the flag components
    val flagColor = Color.Red
    val poleColor = Color.Black
    val baseColor = Color.DarkGray

    // Calculate proportions relative to the canvas size
    val poleWidth = canvasWidth * 0.05f
    val poleHeight = canvasHeight * 0.6f
    val poleX = canvasWidth * 0.5f
    val poleYStart = canvasHeight * 0.2f
    val poleYEnd = poleYStart + poleHeight

    val flagWidth = canvasWidth * 0.4f
    val flagHeight = canvasHeight * 0.25f

    val baseX = poleX - (poleWidth * 2)
    val baseY = poleYEnd
    val baseWidth = poleWidth * 4
    val baseHeight = canvasHeight * 0.1f

    // Draw the flagpole
    drawRect(
        color = poleColor,
        topLeft = Offset(poleX - poleWidth / 2, poleYStart),
        size = Size(poleWidth, poleHeight)
    )

    // Draw the flag (red triangle)
    val flagPath = Path().apply {
        moveTo(poleX, poleYStart) // Top-left corner (attached to pole)
        lineTo(poleX + flagWidth, poleYStart + flagHeight / 2) // Right point of the flag
        lineTo(poleX, poleYStart + flagHeight) // Bottom-left corner (attached to pole)
        close() // Closes the triangle path
    }
    drawPath(
        path = flagPath,
        color = flagColor
    )

    // Draw the base of the flagpole
    drawRect(
        color = baseColor,
        topLeft = Offset(baseX, baseY),
        size = Size(baseWidth, baseHeight)
    )

    // Optional: Draw a subtle border around the flag for definition
    drawPath(
        path = flagPath,
        color = Color.Black,
        style = Stroke(width = 2.dp.toPx()) // Convert DP to pixels for stroke width
    )
}

fun getNumberColor(number: Char): Color = when (number) {
    '1' -> Color(0xff0001ff)
    '2' -> Color(0xff008501)
    '3' -> Color(0xffff0100)
    '4' -> Color(0xff010083)
    '5' -> Color(0xff860000)
    '6' -> Color(0xff028485)
    '7' -> Color(0xff860187)
    '8' -> Color(0xff777777)
    else -> {
        Color.Black
    }
}
