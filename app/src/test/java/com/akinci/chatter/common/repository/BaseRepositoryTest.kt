package com.akinci.chatter.common.repository

import com.akinci.chatter.common.helper.Resource
import com.akinci.chatter.common.network.NetworkChecker
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class BaseRepositoryTest {

    @MockK
    lateinit var networkChecker: NetworkChecker

    private lateinit var repository : BaseRepositoryImpl

    @MockK
    lateinit var callBackObj : SuspendingCallBack

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        repository = BaseRepositoryImpl(networkChecker)
    }

    @AfterEach
    fun tearDown() { unmockkAll() }

    class SuspendingCallBack {
        fun serviceActionCallBack() : Response<String> { return Response.success("Rest Call Succeeded") }
    }

    /**
     *  BaseRepositoryImpl class holds base logic of repository layer.
     *  in this class there is callService function which handles network request responses.
     *
     *  callService with only retrofitServiceAction directly sends retrofit service response object
     *
     *  Contents of callService functions is the same apart from success case.
     * **/

    @Test
    fun `Network is ok, callService function is called, returns Resource-Success for success`() = runBlockingTest {
        every { networkChecker.isNetworkConnected() } returns true
        coEvery { callBackObj.serviceActionCallBack() } returns Response.success("Rest Call Succeeded")

        val callServiceResponseObjStr = repository.callService { callBackObj.serviceActionCallBack() }

        /** repository function response type should be Resource.Success **/
        assertThat(callServiceResponseObjStr).isInstanceOf(Resource.Success::class.java)
        /** returned error should be network error message **/
        assertThat((callServiceResponseObjStr as Resource.Success).data).isEqualTo("Rest Call Succeeded")

        /** call back should be fired. **/
        coVerify (exactly = 1) { callBackObj.serviceActionCallBack() }
        confirmVerified(callBackObj)
    }

    @Test
    fun `Network is ok, callService function is called, returns Resource-Error on null response`() = runBlockingTest {
        every { networkChecker.isNetworkConnected() } returns true
        coEvery { callBackObj.serviceActionCallBack() } returns Response.success(null)

        val callServiceResponseObj = repository.callService { callBackObj.serviceActionCallBack() }

        /** repository function response type should be Resource.Error **/
        assertThat(callServiceResponseObj).isInstanceOf(Resource.Error::class.java)
        /** returned error should be response body is null **/
        assertThat((callServiceResponseObj as Resource.Error).message).isEqualTo("Service response body is null")

        /** call back should be fired. **/
        coVerify (exactly = 1) { callBackObj.serviceActionCallBack() }
        confirmVerified(callBackObj)
    }

    @Test
    fun `Network is ok, callService function is called, returns Resource-Error on unsuccessful response code`() = runBlockingTest {
        every { networkChecker.isNetworkConnected() } returns true
        coEvery { callBackObj.serviceActionCallBack() } returns Response.error(404, "{\"error\":[\"404 Not Found\"]}"
            .toResponseBody("application/json".toMediaTypeOrNull()))

        val callServiceResponseObj = repository.callService { callBackObj.serviceActionCallBack() }

        /** repository function response type should be Resource.Error **/
        assertThat(callServiceResponseObj).isInstanceOf(Resource.Error::class.java)
        /** returned error should be contains response code of error **/
        assertThat((callServiceResponseObj as Resource.Error).message).contains(": 404")

        /** call back should be fired. **/
        coVerify (exactly = 1) { callBackObj.serviceActionCallBack() }
        confirmVerified(callBackObj)
    }

    @Test
    fun `Network is ok, callService function is called, returns Resource-Error for network error`() = runBlockingTest {
        every { networkChecker.isNetworkConnected() } returns false
        coEvery { callBackObj.serviceActionCallBack() } returns Response.success("Rest Call Succeeded")

        val callServiceResponseObj = repository.callService { callBackObj.serviceActionCallBack() }

        /** Network is not connected. **/

        /** repository function response type should be Resource.Error **/
        assertThat(callServiceResponseObj).isInstanceOf(Resource.Error::class.java)
        /** returned error should be network error message **/
        assertThat((callServiceResponseObj as Resource.Error).message).isEqualTo("Couldn't reached to server. Please check your internet connection")

        /** call back shouldn't be fired. **/
        coVerify (exactly = 0) { callBackObj.serviceActionCallBack() }
        confirmVerified(callBackObj)
    }

    @Test
    fun `An exception is occurred during call Service`() = runBlockingTest {
        every { networkChecker.isNetworkConnected() } returns true
        coEvery { callBackObj.serviceActionCallBack() } throws Exception()

        val callServiceResponseObj = repository.callService { callBackObj.serviceActionCallBack() }

        /** repository function response type should be Resource.Error **/
        assertThat(callServiceResponseObj).isInstanceOf(Resource.Error::class.java)
        /** returned error message should be unexpected error **/
        assertThat((callServiceResponseObj as Resource.Error).message).isEqualTo("UnExpected Service Exception.")

        /** call back should be fired. **/
        coVerify (exactly = 1) { callBackObj.serviceActionCallBack() }
        confirmVerified(callBackObj)
    }

}
