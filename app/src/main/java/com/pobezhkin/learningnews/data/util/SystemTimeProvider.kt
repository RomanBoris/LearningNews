package com.pobezhkin.learningnews.data.util

import com.pobezhkin.learningnews.domain.util.TimeProvider
import javax.inject.Inject

class SystemTimeProvider @Inject constructor() : TimeProvider {

    override fun currentTimeMillis(): Long = System.currentTimeMillis()
}
