package dev.alvr.katana.macrobenchmark

import androidx.benchmark.macro.BaselineProfileMode
import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class KatanaBenchmarks {

    @get:Rule
    val macroRule = MacrobenchmarkRule()

    @Test
    fun coldStartupTiming() {
        macroRule.measureRepeated(
            packageName = AppPackage,
            metrics = listOf(StartupTimingMetric()),
            startupMode = StartupMode.COLD,
            compilationMode = releaseCompilation(),
            iterations = 10,
        ) {
            pressHome()
            startActivityAndWait()
        }
    }

    @Test
    fun homeScrollFrameTiming() {
        macroRule.measureRepeated(
            packageName = AppPackage,
            metrics = listOf(FrameTimingMetric()),
            startupMode = StartupMode.WARM,
            compilationMode = releaseCompilation(),
            iterations = 10,
            setupBlock = {
                pressHome()
                startActivityAndWait()
                device.waitForIdle()
            },
        ) {
            doVerticalSwipe(topToBottom = false)
            doVerticalSwipe(topToBottom = true)
        }
    }

    private fun releaseCompilation() = CompilationMode.Partial(BaselineProfileMode.Require)

    private fun MacrobenchmarkScope.doVerticalSwipe(topToBottom: Boolean) {
        val centerX = device.displayWidth / 2
        val topY = (device.displayHeight * 0.2f).toInt()
        val bottomY = (device.displayHeight * 0.8f).toInt()

        val startY = if (topToBottom) topY else bottomY
        val endY = if (topToBottom) bottomY else topY

        device.swipe(centerX, startY, centerX, endY, 12)
        device.waitForIdle()
    }

    private companion object {
        const val AppPackage = "dev.alvr.katana"
    }
}
