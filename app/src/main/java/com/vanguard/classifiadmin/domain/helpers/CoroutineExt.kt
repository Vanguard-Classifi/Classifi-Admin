package com.vanguard.classifiadmin.domain.helpers

import android.os.Handler
import android.os.Looper

fun runnableBlock(handler: () -> Unit) = Handler(Looper.getMainLooper())
    .post { handler() }