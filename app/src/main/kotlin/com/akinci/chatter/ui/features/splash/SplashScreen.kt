package com.akinci.chatter.ui.features.splash

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.akinci.chatter.R
import com.akinci.chatter.core.compose.UIModePreviews
import com.akinci.chatter.core.mvi.CollectEffect
import com.akinci.chatter.ui.ds.components.InfiniteLottieAnimation
import com.akinci.chatter.ui.ds.theme.ChatterTheme
import com.akinci.chatter.ui.ds.theme.displayLarge_swash
import com.akinci.chatter.ui.features.splash.SplashViewContract.Effect
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@RootNavGraph(start = true)
@Destination
@Composable
fun SplashScreen(
    navigator: DestinationsNavigator,
    vm: SplashViewModel = hiltViewModel(),
) {

    // collect and process side effects
    CollectEffect(effect = vm.effect) { effect ->
        when (effect) {
            is Effect.NavigateToRoute -> navigator.navigate(effect.route.toDestination())
        }
    }

    // we need to disable back button actions for this screen, consume back action.
    BackHandler(enabled = true) {}

    SplashScreenContent()
}

@Composable
private fun SplashScreenContent() {
    Surface {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                InfiniteLottieAnimation(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .aspectRatio(1f)
                        .testTag("lottie_animation"),
                    animationId = R.raw.chatter
                )
                Spacer(modifier = Modifier.height(64.dp))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.app_name),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.displayLarge_swash,
                )
            }
        }
    }
}

@UIModePreviews
@Composable
private fun SplashScreenPreview() {
    ChatterTheme {
        SplashScreenContent()
    }
}
