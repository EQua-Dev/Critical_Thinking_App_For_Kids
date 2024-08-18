package com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.holder

import android.util.Log
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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.navigation.Screen
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.providers.LocalNavHost
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.screens.QuizScreen
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.screens.auth.ForgotPasswordScreen
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.screens.auth.LoginScreen
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.screens.auth.SignUpScreen
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.screens.child.ActivityTypeOverviewScreen
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.screens.child.ActivityTypeRuleScreen
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.screens.child.CategoryOverviewScreen
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.screens.child.ChildHomeScreen
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.screens.games.GameScreen
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.screens.parent.ParentHomeScreen
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.utils.Common.mAuth
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.utils.getDp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HolderScreen(
    onStatusBarColorChange: (color: Color) -> Unit,
    holderViewModel: HolderViewModel = hiltViewModel(),
) {

    val TAG = "HolderScreen"
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
            onCategorySelected = { childId, childStage, category ->
                controller.navigate(
                    Screen.CategoryOverview.route.replace("{childId}", childId)
                        .replace("{childStage}", childStage).replace("{category}", category)
                )
            },
            onActivityTypeSelected = { activityTypeKey, childId, category, childStage ->
                Log.d(TAG, "HolderScreen: $category")
                controller.navigate(
                    Screen.ActivityTypeOverview.route.replace("{activityTypeKey}", activityTypeKey)
                        .replace("{childId}", childId)
                        .replace("{childStage}", childStage).replace("{category}", category)
                )
            },
            onActivityRuleSelected = { childId, categoryKey, childStage, activityTypeKey, selectedDifficulty ->
                controller.navigate(
                    Screen.ActivityTypeRule.route.replace("{childId}", childId)
                        .replace("{categoryKey}", categoryKey).replace("{childStage}", childStage)
                        .replace("{activityTypeKey}", activityTypeKey)
                        .replace("{selectedDifficulty}", selectedDifficulty)
                )
            },
            onChildSelected = { childId ->
                controller.navigate(Screen.ChildHome.route.replace("{childId}", childId))

            },
            onQuizStart = { childId,
                            categoryKey,
                            childStage,
                            difficultyLevel ->
                controller.navigate(
                    Screen.Quiz.route.replace("{childId}", childId)
                        .replace("{categoryKey}", categoryKey)
                        .replace("{childStage}", childStage)
                        .replace("{difficultyLevel}", difficultyLevel)
                )
            },
            onQuizCompleted = { score ->

            },
            onGameStart = { childId,
                            category,
                            difficultyLevel,
                            childStage ->
                controller.navigate(
                    Screen.Game.route.replace("{childId}", childId)
                        .replace("{categoryKey}", category)
                        .replace("{difficultyLevel}", difficultyLevel)
                        .replace("{childStage}", childStage)
                )
            },
            onNewScreenRequest = { route, patientId ->
                controller.navigate(route.replace("{patientId}", "$patientId"))
            },
            onLogoutRequested = {
                mAuth.signOut()
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
    onCategorySelected: (childId: String, childStage: String, category: String) -> Unit,
    onActivityTypeSelected: (activityTypeKey: String, childId: String, category: String, childStage: String) -> Unit,
    onActivityRuleSelected: (childId: String, categoryKey: String, childStage: String, activityTypeKey: String, selectedDifficulty: String) -> Unit,
    onChildSelected: (childId: String) -> Unit,
    onQuizStart: (
        childId: String,
        categoryKey: String,
        childStage: String,
        difficultyLevel: String
    ) -> Unit,
    onQuizCompleted: (score: Int) -> Unit,
    onGameStart: (childId: String, category: String, difficultyLevel: String, childStage: String) -> Unit,
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
                composable(Screen.ParentHome.route) {
                    onStatusBarColorChange(MaterialTheme.colorScheme.background)
                    ParentHomeScreen(
                        onNavigationRequested = onNavigationRequested,
                        onChildSelected = onChildSelected
                    )
                }
                composable(
                    Screen.ChildHome.route,
                    arguments = listOf(
                        navArgument(name = "childId") { type = NavType.StringType }
                    ),
                ) {
                    onStatusBarColorChange(MaterialTheme.colorScheme.background)
                    val childId = it.arguments?.getString("childId")

                    ChildHomeScreen(
                        childId = childId!!,
                        navController = controller,
                        onNavigationRequested = onNavigationRequested,
                        onCategorySelected = onCategorySelected
                    )
                }
                composable(
                    Screen.CategoryOverview.route,
                    arguments = listOf(
                        navArgument(name = "childId") { type = NavType.StringType },
                        navArgument(name = "childStage") { type = NavType.StringType },
                        navArgument(name = "category") { type = NavType.StringType },
                    ),
                ) {
                    onStatusBarColorChange(MaterialTheme.colorScheme.background)
                    val childId = it.arguments?.getString("childId")
                    val category = it.arguments?.getString("category")
                    val childStage = it.arguments?.getString("childStage")

                    CategoryOverviewScreen(
                        childId = childId!!,
                        categoryKey = category!!,
                        childStage = childStage!!,
                        navController = controller,
                        onActivityTypeSelected = onActivityTypeSelected

                    )
                }
                composable(
                    Screen.ActivityTypeOverview.route,
                    arguments = listOf(
                        navArgument(name = "childId") { type = NavType.StringType },
                        navArgument(name = "childStage") { type = NavType.StringType },
                        navArgument(name = "category") { type = NavType.StringType },
                        navArgument(name = "activityTypeKey") { type = NavType.StringType },
                    ),
                ) {
                    onStatusBarColorChange(MaterialTheme.colorScheme.background)
                    val childId = it.arguments?.getString("childId")
                    val category = it.arguments?.getString("category")
                    val childStage = it.arguments?.getString("childStage")
                    val activityTypeKey = it.arguments?.getString("activityTypeKey")

                    ActivityTypeOverviewScreen(
                        activityKey = activityTypeKey!!,
                        childStage = childStage!!,
                        categoryKey = category!!,
                        childId = childId!!,
                        navController = controller,
                        onActivityRuleSelected = onActivityRuleSelected,
                    )

                }
                composable(
                    Screen.ActivityTypeRule.route,
                    arguments = listOf(
                        navArgument(name = "childId") { type = NavType.StringType },
                        navArgument(name = "categoryKey") { type = NavType.StringType },
                        navArgument(name = "childStage") { type = NavType.StringType },
                        navArgument(name = "activityTypeKey") { type = NavType.StringType },
                        navArgument(name = "selectedDifficulty") { type = NavType.StringType },
                    ),
                ) {
                    onStatusBarColorChange(MaterialTheme.colorScheme.background)
                    val childId = it.arguments?.getString("childId")
                    val categoryKey = it.arguments?.getString("categoryKey")
                    val childStage = it.arguments?.getString("childStage")
                    val activityTypeKey = it.arguments?.getString("activityTypeKey")
                    val selectedDifficulty = it.arguments?.getString("selectedDifficulty")

                    ActivityTypeRuleScreen(
                        activityTypeKey = activityTypeKey!!,
                        childStage = childStage!!,
                        categoryKey = categoryKey!!,
                        childId = childId!!,
                        selectedDifficulty = selectedDifficulty!!,
                        onQuizStart = onQuizStart,
                        onGameStart = onGameStart,
                        navController = controller,
                    )

                }
                composable(
                    Screen.Quiz.route,
                    arguments = listOf(
                        navArgument(name = "childId") { type = NavType.StringType },
                        navArgument(name = "categoryKey") { type = NavType.StringType },
                        navArgument(name = "childStage") { type = NavType.StringType },
                        navArgument(name = "difficultyLevel") { type = NavType.StringType },
                    ),
                ) {
                    onStatusBarColorChange(MaterialTheme.colorScheme.background)
                    val childId = it.arguments?.getString("childId")
                    val categoryKey = it.arguments?.getString("categoryKey")
                    val childStage = it.arguments?.getString("childStage")
                    val selectedDifficulty = it.arguments?.getString("difficultyLevel")

                    QuizScreen(
                        childStage = childStage!!,
                        categoryKey = categoryKey!!,
                        childId = childId!!,
                        difficultyLevel = selectedDifficulty!!,
                        onQuizCompleted = onQuizCompleted

                    )

                }
                composable(
                    Screen.Game.route,
                    arguments = listOf(
                        navArgument(name = "childId") { type = NavType.StringType },
                        navArgument(name = "categoryKey") { type = NavType.StringType },
                        navArgument(name = "difficultyLevel") { type = NavType.StringType },
                        navArgument(name = "childStage") { type = NavType.StringType },
                    ),
                ) {
                    onStatusBarColorChange(MaterialTheme.colorScheme.background)
                    val childId = it.arguments?.getString("childId")
                    val categoryKey = it.arguments?.getString("categoryKey")
                    val selectedDifficulty = it.arguments?.getString("difficultyLevel")
                    val childStage = it.arguments?.getString("childStage")

                    GameScreen(
                        childStage = childStage!!,
                        category = categoryKey!!,
                        childId = childId!!,
                        difficultyLevel = selectedDifficulty!!,
                        //onQuizCompleted = onQuizCompleted

                    )

                }

                //{activityTypeKey}/{childId}/{category}/{childStage}

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
