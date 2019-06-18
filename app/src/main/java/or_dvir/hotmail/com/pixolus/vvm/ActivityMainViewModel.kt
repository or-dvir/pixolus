package or_dvir.hotmail.com.pixolus.vvm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import or_dvir.hotmail.com.dxutils.RetroCallback
import or_dvir.hotmail.com.dxutils.retroRequestAsync
import or_dvir.hotmail.com.pixolus.other.BodyAuth
import or_dvir.hotmail.com.pixolus.other.MyApplication
import or_dvir.hotmail.com.pixolus.other.ResponseAuth
import or_dvir.hotmail.com.pixolus.other.SMyRetrofit

class ActivityMainViewModel(app:Application): AndroidViewModel(app)
{
    private val TAG = "ActivityMainViewModel"

    val loginSuccess = MutableLiveData<Boolean>()

    fun login(email: String, password: String)
    {
        retroRequestAsync(TAG,
             CoroutineScope(Dispatchers.Main),
             SMyRetrofit.pixometerClient.authenticate(BodyAuth(email, password)),
             RetroCallback<ResponseAuth>().apply {
                              //for simplicity of this demo app, we treat all failures the same
                              onAnyFailure = { _, _, _, _ ->
                                  loginSuccess.value = false
                              }

                              onSuccess = { _, result, _ ->
                                  MyApplication.authToken = "Token ${result.token}"
                                  loginSuccess.value = true
                              }
                          })
    }
}