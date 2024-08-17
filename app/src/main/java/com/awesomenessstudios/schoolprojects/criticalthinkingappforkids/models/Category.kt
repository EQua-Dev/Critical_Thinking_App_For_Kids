package com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.models

data class Category(
    val title: String,
    val description: String,
    val categoryKey: String
)

val categories = listOf(
    Category("Problem Solving", "Tackle challenges with creativity and persistence.", "problem_solving"),
    Category("Decision Making", "Sharpen your ability to choose wisely and decisively.", "decision_making"),
    Category("Logical Reasoning", "Think critically and analyze situations logically.", "logical_reasoning"),
    Category("Creativity", "Unlock your imagination and bring new ideas to life.", "creativity"),
    Category("Memory", "Enhance your recall and retention skills.", "memory")
)

fun getCategoryByKey(categoryKey: String): Category? {
    return categories.find { it.categoryKey == categoryKey }
}