package com.zw.benchmark

import androidx.benchmark.macro.ExperimentalBaselineProfilesApi
import androidx.benchmark.macro.junit4.BaselineProfileRule
import org.junit.Rule
import org.junit.Test
@OptIn(ExperimentalBaselineProfilesApi::class)
class BaselineProfileGenerator {
    @get:Rule
    val baselineProfileRule = BaselineProfileRule()


    @Test
    fun startup() = baselineProfileRule.collectBaselineProfile(
        packageName = "com.zw.composetemplate",
        profileBlock = {
            startActivityAndWait()
        }
    )
}