package com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.utils

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection


object TriangleShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            moveTo(size.width / 2f, 0f)
            lineTo(size.width, size.height)
            lineTo(0f, size.height)
            close()
        }
        return Outline.Generic(path)
    }
}
object PentagonShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            moveTo(size.width / 2f, 0f)
            lineTo(size.width, size.height * 0.38f)
            lineTo(size.width * 0.81f, size.height)
            lineTo(size.width * 0.19f, size.height)
            lineTo(0f, size.height * 0.38f)
            close()
        }
        return Outline.Generic(path)
    }
}
object RhombusShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            moveTo(size.width / 2f, 0f)
            lineTo(size.width, size.height / 2f)
            lineTo(size.width / 2f, size.height)
            lineTo(0f, size.height / 2f)
            close()
        }
        return Outline.Generic(path)
    }
}
object HexagonShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            val radius = size.minDimension / 2f
            val centerX = size.width / 2f
            val centerY = size.height / 2f

            val angle = 2 * Math.PI / 6
            repeat(6) { i ->
                val x = (centerX + radius * Math.cos(angle * i)).toFloat()
                val y = (centerY + radius * Math.sin(angle * i)).toFloat()
                if (i == 0) {
                    moveTo(x, y)
                } else {
                    lineTo(x, y)
                }
            }
            close()
        }
        return Outline.Generic(path)
    }
}
object TrapeziumShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            val topWidth = size.width * 0.6f
            val bottomWidth = size.width
            val height = size.height

            moveTo((size.width - topWidth) / 2f, 0f)
            lineTo((size.width + topWidth) / 2f, 0f)
            lineTo(size.width, height)
            lineTo(0f, height)
            close()
        }
        return Outline.Generic(path)
    }
}
object OctagonShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            val radius = size.minDimension / 2f
            val centerX = size.width / 2f
            val centerY = size.height / 2f
            val angle = 2 * Math.PI / 8

            repeat(8) { i ->
                val x = (centerX + radius * Math.cos(angle * i - Math.PI / 8)).toFloat()
                val y = (centerY + radius * Math.sin(angle * i - Math.PI / 8)).toFloat()
                if (i == 0) {
                    moveTo(x, y)
                } else {
                    lineTo(x, y)
                }
            }
            close()
        }
        return Outline.Generic(path)
    }
}
object StarShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            val radius = size.minDimension / 2f
            val centerX = size.width / 2f
            val centerY = size.height / 2f
            val angle = 2 * Math.PI / 5

            repeat(5) { i ->
                val xOuter = (centerX + radius * Math.cos(angle * i - Math.PI / 2)).toFloat()
                val yOuter = (centerY + radius * Math.sin(angle * i - Math.PI / 2)).toFloat()
                if (i == 0) {
                    moveTo(xOuter, yOuter)
                } else {
                    lineTo(xOuter, yOuter)
                }

                val xInner = (centerX + (radius / 2) * Math.cos(angle * (i + 0.5) - Math.PI / 2)).toFloat()
                val yInner = (centerY + (radius / 2) * Math.sin(angle * (i + 0.5) - Math.PI / 2)).toFloat()
                lineTo(xInner, yInner)
            }
            close()
        }
        return Outline.Generic(path)
    }
}
