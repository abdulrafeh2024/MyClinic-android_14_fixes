package com.telemedicine.myclinic.activities.otherservices

import android.content.Context
import com.telemedicine.myclinic.activities.multiservices.ServiceItems
import com.telemedicine.myclinic.myapplication.R
import com.telemedicine.myclinic.util.ValidationHelper
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.item_services_list_layout.view.*
import org.otwebrtc.EglBase
import kotlinx.android.synthetic.main.med_list_item.view.*

class OtherServicesListAdapter(
    val serviceItems: ServiceItems? = null,
    val context: Context? = null
) : Item<ViewHolder>() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.service_tv.text = serviceItems?.name
       // viewHolder.itemView.date_tv.text = serviceItems?.serviceStatus

        when (serviceItems?.serviceStatus) {
            "Paid" -> {
                viewHolder.itemView.date_tv.text = context?.getString(R.string.paid)
            }
            "Ordered" -> {
                viewHolder.itemView.date_tv.text = context?.getString(R.string.ordered)
            }
            "Started" -> {
                viewHolder.itemView.date_tv.text =  context?.getString(R.string.started)
            }
            "Decline" -> {
                viewHolder.itemView.date_tv.text =  context?.getString(R.string.declined)
            }
            "Complete" -> {
                viewHolder.itemView.date_tv.text =  context?.getString(R.string.completed)
            }
            "cancelled" -> {
                viewHolder.itemView.date_tv.text =  context?.getString(R.string.cancelled)
            }
            "Discontinued" -> {
                viewHolder.itemView.date_tv.text =  context?.getString(R.string.discontinued)
            }
            else -> {
                viewHolder.itemView.date_tv.text =  serviceItems?.serviceStatus
            }
        }
    }

    override fun getLayout(): Int {
        return R.layout.med_list_item
    }
}