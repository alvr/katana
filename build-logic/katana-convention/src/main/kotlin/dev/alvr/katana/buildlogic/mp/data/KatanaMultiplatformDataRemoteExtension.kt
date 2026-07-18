package dev.alvr.katana.buildlogic.mp.data

import com.apollographql.apollo.gradle.api.Service

abstract class KatanaMultiplatformDataRemoteExtension internal constructor(private val service: Service) {
    fun configure(action: Service.() -> Unit) {
        service.action()
    }
}
