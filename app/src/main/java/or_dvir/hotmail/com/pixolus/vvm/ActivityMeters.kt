package or_dvir.hotmail.com.pixolus.vvm

import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_meters.*
import or_dvir.hotmail.com.dxutils.createProgressDialog
import or_dvir.hotmail.com.dxutils.makeGone
import or_dvir.hotmail.com.dxutils.makeVisible
import or_dvir.hotmail.com.pixolus.R
import or_dvir.hotmail.com.pixolus.model.Meter
import or_dvir.hotmail.com.pixolus.other.AdapterMeters
import org.jetbrains.anko.toast

class ActivityMeters : AppCompatActivity()
{
    private lateinit var mAdapter: AdapterMeters
    private lateinit var mViewModel: ActivityMetersViewModel
    private lateinit var mProgDiag: ProgressDialog

    //NOTE:
    //we can show all kinds of details about each meter... but for the purposes of this demo app
    //the resource_id will be enough (as i understood it, resource_id is the unique id of the meter).

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meters)

        //initialize with empty list until we get result from server
        mAdapter = AdapterMeters(mutableListOf())

        //viewModel immediately sends request to get meters so show the dialog
        mProgDiag = createProgressDialog(R.string.loading, shouldShow = true)
        mViewModel = ViewModelProviders.of(this).get(ActivityMetersViewModel::class.java)

        activityMeters_btn_nextPage.apply {
            setOnClickListener {
                makeGone()
                mProgDiag.show()
                mViewModel.nextPage()
            }
        }

        mViewModel.mMeters.observe(this, Observer {
            mProgDiag.dismiss()

            when
            {
                it == null   -> AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setMessage(R.string.someErrorOccurred)
                    .setPositiveButton(R.string.tryAgain) { dialog, _ ->
                        dialog.dismiss()
                        mProgDiag.show()
                        mViewModel.retryPage()
                    }
                    .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.dismiss() }
                    .show()

                it.isEmpty() -> toast(R.string.noMetersFound)

                else         ->
                {
                    mAdapter.apply {
                        //need to make a SEPARATE list containing all previous items
                        //plus the new items
                        val newList = mItems.toMutableList()
                            newList.addAll(it)

                        val result =
                            DiffUtil.calculateDiff(Meter.DiffCallback(mItems, newList), false)

                        mItems.apply {
                            clear()
                            addAll(newList)
                        }

                        result.dispatchUpdatesTo(this)
                    }
                }
            }
        })

        activityMeters_rv.apply {
            addItemDecoration(DividerItemDecoration(this@ActivityMeters, DividerItemDecoration.VERTICAL))
            layoutManager = LinearLayoutManager(this@ActivityMeters, RecyclerView.VERTICAL, false)
            adapter = mAdapter

            onLastItemVisible = {
                if(mViewModel.mHasNextPage)
                    //for some reason this will only work inside "post"
                    activityMeters_btn_nextPage.apply { post { makeVisible() } }
                else
                    toast(R.string.noMoreMeters)
            }

            onLastItemInvisible = {
                activityMeters_btn_nextPage.makeGone()
            }
        }
    }
}
