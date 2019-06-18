package or_dvir.hotmail.com.pixolus.model

import androidx.recyclerview.widget.DiffUtil
import com.hotmail.or_dvir.dxadapter.interfaces.IItemBase
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Meter(val resource_id: Int): IItemBase
{
    //this is a demo app and also this is the only model we will be showing in any lists,
    //therefore any number will be good here.
    //in a real app, we'd have to make sure each model has a unique id (type)
    override fun getViewType() = 1

    //////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////

    class DiffCallback(private val oldList: MutableList<Meter>, private val newList: MutableList<Meter>)
        : DiffUtil.Callback()
    {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition].resource_id == newList[newItemPosition].resource_id

        //in this case, the contents of a meter will never change so we can simply return TRUE
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = true
        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size
    }
}
