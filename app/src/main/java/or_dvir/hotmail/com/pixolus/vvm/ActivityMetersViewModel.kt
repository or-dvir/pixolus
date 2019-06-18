package or_dvir.hotmail.com.pixolus.vvm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import or_dvir.hotmail.com.dxutils.RetroCallback
import or_dvir.hotmail.com.dxutils.retroRequestAsync
import or_dvir.hotmail.com.pixolus.model.Meter
import or_dvir.hotmail.com.pixolus.other.MyApplication
import or_dvir.hotmail.com.pixolus.other.ResponseMeters
import or_dvir.hotmail.com.pixolus.other.SMyRetrofit

class ActivityMetersViewModel(app: Application): AndroidViewModel(app)
{
    private val TAG = "ActivityMetersViewModel"
    private val FIRST_PAGE = 1
    private val CODE_NEXT_PAGE = 1000
    private val CODE_RETRY_PAGE = 1001

    val mMeters = MutableLiveData<List<Meter>?>()
    private var mCurrentPage: Int = FIRST_PAGE

    var mHasNextPage = false

    init
    {
        //NOTE:
        //"requestCode" is intended to revert mCurrentPage to its previous value in case of an error.
        //CODE_RETRY_PAGE is meant to keep mCurrentPage the same in case of failure,
        //and is what we should use for the first time the request is made, and when retrying
        //the request
        getMeters(CODE_RETRY_PAGE)
    }

    private fun getMeters(requestCode: Int)
    {
        retroRequestAsync(TAG,
             CoroutineScope(Dispatchers.Main),
             SMyRetrofit.pixometerClient.getMeters(MyApplication.authToken, mCurrentPage),
             RetroCallback<ResponseMeters>().apply {
                              //for simplicity of this demo app, we treat all failures the same
                              onAnyFailure = { _, _, _, _ ->

                                  //revert mCurrentPage to it's previous state.
                                  //checking mCurrentPage > FIRST_PAGE just to be safe so we don't
                                  //get illegal page numbers
                                  if(requestCode == CODE_NEXT_PAGE && mCurrentPage > FIRST_PAGE)
                                      mCurrentPage--

                                  mMeters.value = null
                              }

                              onSuccess = { _, result, _ ->
                                  mHasNextPage = (result.next != null)
                                  mMeters.value = result.results
                              }
                          })
    }

    fun retryPage() = getMeters(CODE_RETRY_PAGE)

    fun nextPage()
    {
        mCurrentPage++
        getMeters(CODE_NEXT_PAGE)
    }
}