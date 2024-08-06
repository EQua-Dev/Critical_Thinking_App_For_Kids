package com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.holder

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.navigation.Screen
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.providers.LocalNavHost
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.screens.auth.ForgotPasswordScreen
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.screens.auth.LoginScreen
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.screens.auth.SignUpScreen
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.screens.parent.ParentHomeScreen
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.utils.Common.mAuth
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.utils.getDp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HolderScreen(
    onStatusBarColorChange: (color: Color) -> Unit,
    holderViewModel: HolderViewModel = hiltViewModel(),
) {
    /*  val destinations = remember {
          listOf(Screen.Home, Screen.Notifications, Screen.Bookmark, Screen.Profile)
      }*/

    /** Our navigation controller that the MainActivity provides */
    val controller = LocalNavHost.current

    /** The current active navigation route */
    val currentRouteAsState = getActiveRoute(navController = controller)

    /** The current logged user, which is null by default */

    /** The main app's scaffold state */
    val scaffoldState = rememberBottomSheetScaffoldState()

    /** The coroutine scope */
    val scope = rememberCoroutineScope()

    /** Dynamic snack bar color */
    val (snackBarColor, setSnackBarColor) = remember {
        mutableStateOf(Color.White)
    }

    /** SnackBar appear/disappear transition */
    val snackBarTransition = updateTransition(
        targetState = scaffoldState.snackbarHostState,
        label = "SnackBarTransition"
    )

    /** SnackBar animated offset */
    val snackBarOffsetAnim by snackBarTransition.animateDp(
        label = "snackBarOffsetAnim",
        transitionSpec = {
            TweenSpec(
                durationMillis = 300,
                easing = LinearEasing,
            )
        }
    ) {
        when (it.currentSnackbarData) {
            null -> {
                100.getDp()
            }

            else -> {
                0.getDp()
            }
        }
    }

    Box {
        /** Cart offset on the screen */
        val (cartOffset, setCartOffset) = remember {
            mutableStateOf(IntOffset(0, 0))
        }
        ScaffoldSection(
            controller = controller,
            scaffoldState = scaffoldState,
            onStatusBarColorChange = onStatusBarColorChange,
            onNavigationRequested = { route, removePreviousRoute ->
                if (removePreviousRoute) {
                    controller.popBackStack()
                }
                controller.navigate(route)
            },
            onBackRequested = {
                controller.popBackStack()
            },
            onAuthenticated = { userType ->
                var navRoute = ""
                when (userType) {
                    /*Common.UserTypes.STUDENT.userType -> navRoute = Screen.StudentLanding.route
                    Common.UserTypes.LECTURER.userType -> navRoute =
                        Screen.LecturerLandingScreen.route*/
                }
                controller.navigate(navRoute) {
                    /* popUpTo(Screen.Login.route) {
                         inclusive = true
                     }*/
                }
            },
            onAccountCreated = {
                //nav to register courses
                /*controller.navigate(Screen.CourseRegistration.route) {
                    popUpTo(Screen.Signup.route) {
                        inclusive = true
                    }
                }*/
            },
            onCourseRegistered = {
                //nav to set course goals
                //controller.navigate(Screen.SetCourseGoal.route)
            },
            onGoalsSet = {
                //nav to student home
                //controller.navigate(Screen.StudentLanding.route)
            },
            onSemesterSelected = { level, semester ->
                /*controller.navigate(
                    Screen.SemesterScreen.route.replace("{level}", level)
                        .replace("{semester}", semester)
                )
*/
            },
            onViewStudent = { studentId ->
                //controller.navigate(Screen.StudentDetail.route.replace("{studentId}", studentId))
            },
            onNewScreenRequest = { route, patientId ->
                controller.navigate(route.replace("{patientId}", "$patientId"))
            },
            onLogoutRequested = {
                mAuth.signOut()
                /*controller.navigate(Screen.Login.route) {
                    popUpTo(Screen.Signup.route) {
                        inclusive = true
                    }
                }*/
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldSection(
    controller: NavHostController,
    scaffoldState: BottomSheetScaffoldState,
    onStatusBarColorChange: (color: Color) -> Unit,
    onNavigationRequested: (route: String, removePreviousRoute: Boolean) -> Unit,
    onBackRequested: () -> Unit,
    onAuthenticated: (userType: String) -> Unit,
    onAccountCreated: () -> Unit,
    onCourseRegistered: () -> Unit,
    onGoalsSet: () -> Unit,
    onSemesterSelected: (level: String, semester: String) -> Unit,
    onViewStudent: (studentId: String) -> Unit,
    onNewScreenRequest: (route: String, id: String?) -> Unit,
    onLogoutRequested: () -> Unit
) {
    Scaffold(
        //scaffoldState = scaffoldState,
        snackbarHost = {
            scaffoldState.snackbarHostState
        },
    ) { paddingValues ->
        Column(
            Modifier.padding(paddingValues)
        ) {
            NavHost(
                modifier = Modifier.weight(1f),
                navController = controller,
                startDestination = Screen.Login.route
            ) {
                composable(Screen.Login.route) {
                    onStatusBarColorChange(MaterialTheme.colorScheme.background)
                    LoginScreen(
                        onNavigationRequested = onNavigationRequested,
                        onAuthenticated = onAuthenticated
                    )
                }
                composable(Screen.Signup.route) {
                    onStatusBarColorChange(MaterialTheme.colorScheme.background)
                    SignUpScreen(
                        onNavigationRequested = onNavigationRequested,
                        onAccountCreated = onAccountCreated,
                    )
                }
                    composable(Screen.Login.route) {
                        onStatusBarColorChange(MaterialTheme.colorScheme.background)
                        LoginScreen(
                            onNavigationRequested = onNavigationRequested,
                            onAuthenticated = onAuthenticated
                    )
                }
                composable(Screen.ForgotPassword.route) {
                    onStatusBarColorChange(MaterialTheme.colorScheme.background)
                    ForgotPasswordScreen(
                        onNavigationRequested = onNavigationRequested
                    )
                }
                composable(Screen.ParentHome.route){
                    onStatusBarColorChange(MaterialTheme.colorScheme.background)
                    ParentHomeScreen(                        onNavigationRequested = onNavigationRequested
                    )
                }

            }
        }
    }
}

/**
 * A function that is used to get the active route in our Navigation Graph , should return the splash route if it's null
 */
@Composable
fun getActiveRoute(navController: NavHostController): String {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route ?: "splash"
}
