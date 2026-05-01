package dev.alvr.katana.baselineprofile

import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class BaselineProfileGenerator {

    @get:Rule val baselineProfileRule = BaselineProfileRule()

    @Test
    fun startup() =
        baselineProfileRule.collect(packageName = "dev.alvr.katana", includeInStartupProfile = true) {
            startActivityAndWait()
            device.waitForIdle()
        }
}
