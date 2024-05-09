package com.oguzdogdu.walliescompose.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.navigation
import com.oguzdogdu.walliescompose.features.appstate.MainAppState
import com.oguzdogdu.walliescompose.features.authenticateduser.authenticatedUserScreen
import com.oguzdogdu.walliescompose.features.authenticateduser.changeemail.changeEmailScreen
import com.oguzdogdu.walliescompose.features.authenticateduser.changeemail.navigateToChangeEmailScreen
import com.oguzdogdu.walliescompose.features.authenticateduser.changenameandsurname.changeNameAndSurnameScreen
import com.oguzdogdu.walliescompose.features.authenticateduser.changenameandsurname.navigateToChangeNameAndASurnameScreen
import com.oguzdogdu.walliescompose.features.authenticateduser.changepassword.changePasswordScreen
import com.oguzdogdu.walliescompose.features.authenticateduser.changepassword.navigateToChangePasswordScreen
import com.oguzdogdu.walliescompose.features.authenticateduser.navigateToAuthenticatedUserScreen
import com.oguzdogdu.walliescompose.features.collections.collectionScreen
import com.oguzdogdu.walliescompose.features.collections.detaillist.collectionDetailListScreen
import com.oguzdogdu.walliescompose.features.collections.detaillist.navigateToCollectionDetailListScreen
import com.oguzdogdu.walliescompose.features.detail.detailScreen
import com.oguzdogdu.walliescompose.features.detail.navigateToDetailScreen
import com.oguzdogdu.walliescompose.features.favorites.favoritesScreen
import com.oguzdogdu.walliescompose.features.home.HomeScreenNavigationRoute
import com.oguzdogdu.walliescompose.features.home.homeScreen
import com.oguzdogdu.walliescompose.features.latest.latestScreen
import com.oguzdogdu.walliescompose.features.latest.navigateToLatestScreen
import com.oguzdogdu.walliescompose.features.login.LoginScreenNavigationRoute
import com.oguzdogdu.walliescompose.features.login.forgotpassword.forgotPasswordScreen
import com.oguzdogdu.walliescompose.features.login.forgotpassword.navigateToForgotPasswordScreen
import com.oguzdogdu.walliescompose.features.login.googlesignin.GoogleAuthUiClient
import com.oguzdogdu.walliescompose.features.login.loginScreen
import com.oguzdogdu.walliescompose.features.login.signinwithemail.SignInWithEmailScreenNavigationRoute
import com.oguzdogdu.walliescompose.features.login.signinwithemail.signInWithEmailScreen
import com.oguzdogdu.walliescompose.features.popular.navigateToPopularScreen
import com.oguzdogdu.walliescompose.features.popular.popularScreen
import com.oguzdogdu.walliescompose.features.profiledetail.navigation.navigateToProfileDetailScreen
import com.oguzdogdu.walliescompose.features.profiledetail.navigation.profileDetailScreen
import com.oguzdogdu.walliescompose.features.search.navigateToSearchScreen
import com.oguzdogdu.walliescompose.features.search.searchScreen
import com.oguzdogdu.walliescompose.features.settings.settingsScreen
import com.oguzdogdu.walliescompose.features.signup.navigateToSignUpScreen
import com.oguzdogdu.walliescompose.features.signup.signUpScreen
import com.oguzdogdu.walliescompose.features.splash.SplashScreenNavigationRoute
import com.oguzdogdu.walliescompose.features.splash.splashScreen
import com.oguzdogdu.walliescompose.features.topics.navigateToTopicsScreen
import com.oguzdogdu.walliescompose.features.topics.topicdetaillist.navigateToTopicDetailListScreen
import com.oguzdogdu.walliescompose.features.topics.topicdetaillist.topicDetailListScreen
import com.oguzdogdu.walliescompose.features.topics.topicsScreen
import com.oguzdogdu.walliescompose.navigation.utils.NavigationConstants.AUTH
import com.oguzdogdu.walliescompose.navigation.utils.NavigationConstants.CONTENT

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun WalliesNavHost(
    appState: MainAppState,
    modifier: Modifier = Modifier,
    startDestination: String = SplashScreenNavigationRoute,
    isAuthenticated:Boolean,
    googleAuthUiClient: GoogleAuthUiClient
) {

    val navController = appState.navController
    var authState by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = isAuthenticated) {
        authState = isAuthenticated
    }
    SharedTransitionLayout {
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = modifier
        ) {
            splashScreen(goToLoginFlow = {
                navController.navigate(LoginScreenNavigationRoute) {
                    popUpTo(SplashScreenNavigationRoute) {
                        inclusive = true
                    }
                }
            }, goToContentScreen = {
                navController.navigate(HomeScreenNavigationRoute) {
                    popUpTo(SplashScreenNavigationRoute) {
                        inclusive = true
                    }
                }
            })

            navigation(startDestination = LoginScreenNavigationRoute, route = AUTH) {
                loginScreen(googleAuthUiClient = googleAuthUiClient,navigateToHome = {
                    navController.navigate(HomeScreenNavigationRoute) {
                        popUpTo(LoginScreenNavigationRoute){
                            inclusive = true
                        }
                    }
                },
                    onContinueWithoutLoginClick = {
                        navController.navigate(HomeScreenNavigationRoute) {
                            popUpTo(LoginScreenNavigationRoute){
                                inclusive = true
                            }
                        }
                    }, navigateBack = {
                        navController.popBackStack()
                    }, navigateToSignInEmail = {
                        navController.navigate(SignInWithEmailScreenNavigationRoute)
                    })

                signInWithEmailScreen(navigateToHome = {
                    navController.navigate(HomeScreenNavigationRoute) {
                        popUpTo(LoginScreenNavigationRoute){
                            inclusive = true
                        }
                    }
                }, navigateBack = {
                    navController.popBackStack()
                }, navigateToForgotPassword = {
                    navController.navigateToForgotPasswordScreen()
                },
                    navigateToSignUpScreen = {
                        navController.navigateToSignUpScreen()
                    }
                )
                forgotPasswordScreen(navigateToHome = {}, navigateBack = {
                    navController.popBackStack()
                })
                signUpScreen(navigateBack = { navController.popBackStack() }, goToLoginScreen = {
                    navController.navigate(LoginScreenNavigationRoute) {
                        popUpTo(LoginScreenNavigationRoute){
                            inclusive = true
                        }
                    }
                })
            }

            navigation(startDestination = HomeScreenNavigationRoute, route = CONTENT) {
                homeScreen(
                    transitionScope = this@SharedTransitionLayout,
                    onTopicDetailListClick = {
                        navController.navigateToTopicDetailListScreen(topicId = it)
                    },
                    onLatestClick = {
                        navController.navigateToDetailScreen(photoId = it)
                    },
                    onPopularClick = {
                        navController.navigateToDetailScreen(photoId = it)
                    },
                    onTopicSeeAllClick = {
                        navController.navigateToTopicsScreen()
                    },
                    onSearchClick = {
                        navController.navigateToSearchScreen()
                    },
                    onPopularSeeAllClick = {
                        navController.navigateToPopularScreen()
                    },
                    onLatestSeeAllClick = {
                        navController.navigateToLatestScreen()
                    },
                    navigateBack = {
                        appState.onBackPress()
                    },
                    onUserPhotoClick = {
                        navController.navigateToAuthenticatedUserScreen()
                    }
                )
                collectionScreen(
                    transitionScope = this@SharedTransitionLayout,
                    onCollectionClick = {id,title ->
                    navController.navigateToCollectionDetailListScreen(
                        collectionDetailListId = id,
                        collectionDetailListTitle = title
                    )
                })
                favoritesScreen(onFavoriteClick = {
                    navController.navigateToDetailScreen(photoId = it)
                })
                settingsScreen()
                searchScreen(onBackClick = {
                    navController.popBackStack()
                }, searchPhotoClick = {
                    navController.navigateToDetailScreen(photoId = it)
                })
                detailScreen(
                    transitionScope = this@SharedTransitionLayout,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onTagClick = {
                        navController.navigateToSearchScreen(queryFromDetail = it)
                    },
                    onProfileDetailClick = {
                        navController.navigateToProfileDetailScreen(username = it)
                    }
                )
                topicsScreen(onBackClick = {
                    navController.popBackStack()
                }, onTopicClick = {
                    navController.navigateToTopicDetailListScreen(topicId = it)
                })
                topicDetailListScreen(onTopicClick = {
                    navController.navigateToDetailScreen(photoId = it)
                }, onBackClick = {
                    navController.popBackStack()
                })
                collectionDetailListScreen(
                    transitionScope = this@SharedTransitionLayout,
                    onCollectionClick = {
                    navController.navigateToDetailScreen(photoId = it)
                }, onBackClick = {
                    navController.popBackStack()
                })
                popularScreen(
                    transitionScope = this@SharedTransitionLayout, onPopularClick = {
                        navController.navigateToDetailScreen(photoId = it)
                    },
                    onBackClick = {
                        navController.popBackStack()
                    })
                latestScreen(
                    transitionScope = this@SharedTransitionLayout,
                    onLatestClick = {
                        navController.navigateToDetailScreen(photoId = it)
                    },
                    onBackClick = {
                        navController.popBackStack()
                    })
                profileDetailScreen(onUserPhotoListClick = { id ->
                    navController.navigateToDetailScreen(photoId = id)

                }, onCollectionItemClick = { id, title ->
                    navController.navigateToCollectionDetailListScreen(
                        collectionDetailListId = id,
                        collectionDetailListTitle = title
                    )
                }, onBackClick = {
                    navController.popBackStack()
                })
                authenticatedUserScreen(
                    transitionScope = this@SharedTransitionLayout,
                    navigateBack = {
                        navController.popBackStack()
                }, navigateToLogin = {
                    navController.navigate(AUTH) {
                        popUpTo(CONTENT){
                            inclusive = true
                        }
                    }
                }, navigateToChangeNameAndSurname = {
                    navController.navigateToChangeNameAndASurnameScreen()
                }, navigateToChangePassword = {
                    navController.navigateToChangePasswordScreen()
                }, navigateToChangeEmail = {
                    navController.navigateToChangeEmailScreen()
                })
                changeNameAndSurnameScreen(navigateBack = {
                    navController.popBackStack()
                })
                changePasswordScreen(navigateBack = {
                    navController.popBackStack()
                })
                changeEmailScreen(navigateBack = {
                    navController.popBackStack()
                })
            }
        }
    }
}