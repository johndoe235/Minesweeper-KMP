package ui.minesweeper

import androidx.compose.ui.geometry.Offset

data class SquareStruct(
    val canvasWidth: Float,
) {
    val onePixel = canvasWidth / 25
    val strokeWidth = onePixel
    val halfPixel = onePixel / 2

    val topFirstLineStart = Offset(x = 0f, y = halfPixel)
    val topFirstLineEnd = Offset(x = canvasWidth - onePixel, y = halfPixel)
    val lightSecondLineStart: Offset = Offset(x = topFirstLineStart.x, y = topFirstLineStart.y + onePixel)
    val lightSecondLineEnd: Offset = Offset(x = topFirstLineEnd.x - onePixel, y = topFirstLineEnd.y + onePixel)
    val lightThirdLineStart: Offset = Offset(x = lightSecondLineStart.x, y = lightSecondLineStart.y + onePixel)
    val lightThirdLineEnd: Offset = Offset(x = lightSecondLineEnd.x - onePixel, y = lightSecondLineEnd.y + onePixel)

    val leftFirstLineStart: Offset = Offset(x = halfPixel, y = halfPixel)
    val leftFirstLineEnd: Offset = Offset(x = halfPixel, y = canvasWidth - onePixel)

    val leftSecondLineStart: Offset = Offset(x = leftFirstLineStart.x + onePixel, y = leftFirstLineStart.y)
    val leftSecondLineEnd: Offset = Offset(x = leftFirstLineEnd.x + onePixel, y = leftFirstLineEnd.y - onePixel)

    val leftThirdLineStart: Offset = Offset(x = leftSecondLineStart.x + onePixel, y = leftSecondLineStart.y)
    val leftThirdLineEnd: Offset = Offset(x = leftSecondLineEnd.x + onePixel, y = leftSecondLineEnd.y - onePixel)

    val rightFirstLineStart: Offset = Offset(x = canvasWidth - halfPixel, y = onePixel)
    val rightFirstLineEnd: Offset = Offset(x = canvasWidth - halfPixel, y = canvasWidth)

    val rightSecondLineStart: Offset =
        Offset(x = rightFirstLineStart.x - onePixel, y = rightFirstLineStart.y + onePixel)
    val rightSecondLineEnd: Offset = Offset(x = rightFirstLineEnd.x - onePixel, y = rightFirstLineEnd.y)

    val rightThirdLineStart: Offset =
        Offset(x = rightSecondLineStart.x - onePixel, y = rightSecondLineStart.y + onePixel)
    val rightThirdLineEnd: Offset = Offset(x = rightSecondLineEnd.x - onePixel, y = rightSecondLineEnd.y)


    val rightBottomFirstLineStart: Offset = Offset(x = onePixel, y = canvasWidth - halfPixel)
    val rightBottomFirstLineEnd: Offset = Offset(x = canvasWidth, y = canvasWidth - halfPixel)

    val rightBottomSecondLineStart: Offset =
        Offset(x = rightBottomFirstLineStart.x + onePixel, y = rightBottomFirstLineStart.y - onePixel)
    val rightBottomSecondLineEnd: Offset =
        Offset(x = rightBottomFirstLineEnd.x - onePixel, y = rightBottomFirstLineEnd.y - onePixel)

    val rightBottomThirdLineStart: Offset =
        Offset(x = rightBottomSecondLineStart.x + onePixel, y = rightBottomSecondLineStart.y - onePixel)
    val rightBottomThirdLineEnd: Offset =
        Offset(x = rightBottomSecondLineEnd.x - onePixel, y = rightBottomSecondLineEnd.y - onePixel)
}