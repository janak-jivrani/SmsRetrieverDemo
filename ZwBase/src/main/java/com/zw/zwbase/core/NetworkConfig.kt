/*
 * Copyright © 2023 Zetrixweb. All rights reserved.
 * Modify this class as per your requirement
 */
package com.zw.zwbase.core

import android.os.Build
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.O)
object NetworkConfig {
    val connectTimeoutSeconds: Long = 60
    val readTimeoutSeconds: Long = 60
    val writeTimeoutSeconds: Long = 60
}
