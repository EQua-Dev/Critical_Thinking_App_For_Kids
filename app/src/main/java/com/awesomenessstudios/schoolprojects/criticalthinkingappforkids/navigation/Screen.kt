package com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.R

sealed class Screen(
    val route: String,
    @StringRes val title: Int? = null,
    @DrawableRes val icon: Int? = null,
) {
    object Splash : Screen(
        route = "splash",
    )
   object Signup : Screen(
        route = "signup",
        title = R.string.signup,
    )
    object Login : Screen(
        route = "login",
        title = R.string.login,
    )

    object ForgotPassword : Screen(
        route = "forgotpassword",
        title = R.string.forgot_password,
    )
    object ParentHome : Screen(
        route = "parenthome",
        title = R.string.parent_home,
    )
}
