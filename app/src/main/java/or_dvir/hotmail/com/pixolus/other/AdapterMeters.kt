package or_dvir.hotmail.com.pixolus.other

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hotmail.or_dvir.dxadapter.DxAdapter
import com.hotmail.or_dvir.dxadapter.DxHolder
import com.hotmail.or_dvir.dxadapter.onItemClickListener
import com.hotmail.or_dvir.dxadapter.onItemLongClickListener
import kotlinx.android.synthetic.main.list_item_meter.view.*
import or_dvir.hotmail.com.pixolus.R
import or_dvir.hotmail.com.pixolus.model.Meter

class AdapterMeters(var mItems: MutableList<Meter>)
    : DxAdapter<Meter, AdapterMeters.ViewHolder>()
{
    override val onItemClick: onItemClickListener<Meter>? = null
    override val onItemLongClick: onItemLongClickListener<Meter>? = null

    override fun createAdapterViewHolder(itemView: View, parent: ViewGroup, viewType: Int)
            = ViewHolder(itemView)
    override fun getOriginalAdapterItems() = mItems
    override fun getItemLayoutRes(parent: ViewGroup, viewType: Int) = R.layout.list_item_meter

    override fun bindViewHolder(holder: ViewHolder, position: Int, item: Meter)
    {
        holder.tvMeterId.text = item.resource_id.toString()
    }

    override fun unbindViewHolder(holder: ViewHolder, position: Int, item: Meter)
    {
        holder.tvMeterId.text = ""
    }
    ////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////

    class ViewHolder(itemView: View): DxHolder(itemView)
    {
        val tvMeterId: TextView = itemView.listItem_meter_tv_id
    }
}