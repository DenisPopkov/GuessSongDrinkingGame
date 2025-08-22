package com.guesssong.game

import androidx.compose.ui.window.ComposeUIViewController
import com.guesssong.game.di.initKoin
import com.guesssong.game.screens.App
import platform.UIKit.UIStatusBarStyleLightContent

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    App()
}
