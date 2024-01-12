package com.akinci.chatter.core.application

/**
 * AppConfig is a wrapper class for BuildConfig fields which is mocked on unit tests.
 * **/
class AppConfig @Inject constructor() {
    fun isDebugMode() = BuildConfig.DEBUG

    //  fun getServiceEndpointBaseUrl() = BuildConfig.SERVICE_ENDPOINT_BASE_URL
}