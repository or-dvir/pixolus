package or_dvir.hotmail.com.pixolus.vvm

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Patterns
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_main.*
import or_dvir.hotmail.com.dxutils.createProgressDialog
import or_dvir.hotmail.com.dxutils.showKeyBoard
import or_dvir.hotmail.com.pixolus.R
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class ActivityMain : AppCompatActivity()
{
    //NOTES:
    //1) i assume the device has an internet connection.
    //2) i am aware that ProgressDialog has been deprecated by google,
    // however for the simplicity of this demo app i will use it.
    //3) i am aware that "some error occurred" is very vague and doesn't tell anything to the user
    // and is bad UX, however for the simplicity of this demo app this is the message shown for
    // all errors.

    //QUESTION: How would you store the meters persistently in the app?
    // Please give reasons for your decision(s). (No implementation needed. Pseudo-code is okay,
    // where it helps.)
    //ANSWER: given the large of data and it's complexity (from what i could see in the
    // documentation), i would choose to use a local database (more specifically i would use ROOM)

    //QUESTION: What (regarding data storage) would/could you test using automated tests?
    //ANSWER: at the moment i don't have any experience writing automated tests so unfortunately
    // i cannot answer this question. however i'll be happy to learn :)

    private lateinit var mViewModel: ActivityMainViewModel
    private lateinit var mProgDiag: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mProgDiag = createProgressDialog(R.string.loggingIn)

        activityMain_btn_login.setOnClickListener {
            if(verifyInput())
            {
                mProgDiag.show()
                mViewModel.login(activityMain_et_email.text.toString().trim(),
                                 activityMain_et_password.text.toString().trim())
            }
        }

        //for some reason the keyboard will not show unless triggered after a small delay
        activityMain_et_email.apply {
            postDelayed( { showKeyBoard(this, InputMethodManager.SHOW_FORCED) }, 50)
        }

        mViewModel = ViewModelProviders.of(this).get(ActivityMainViewModel::class.java)

        mViewModel.loginSuccess.observe(this, Observer {
            mProgDiag.dismiss()

            if (it == false)
                toast(R.string.someErrorOccurred)
            else
                startActivity<ActivityMeters>()
        })
    }

    private fun verifyInput(): Boolean
    {
        activityMain_et_email.apply {
            if(text.isBlank())
            {
                error = getString(R.string.mandatoryField)
                requestFocus()
                return false
            }

            if(!Patterns.EMAIL_ADDRESS.matcher(text.trim()).matches())
            {
                error = getString(R.string.invalidEmail)
                requestFocus()
                return false
            }
        }

        activityMain_et_password.apply {
            if(text.isEmpty())
            {
                error = getString(R.string.mandatoryField)
                requestFocus()
                return false
            }
        }

        return true
    }
}
