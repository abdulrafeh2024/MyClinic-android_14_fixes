package com.telemedicine.myclinic.activities.multiservices

import com.telemedicine.myclinic.myapplication.R
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.item_service_header_layout.view.*

class ServiceHeaderAdapter(val header: String) : Item<ViewHolder>() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.headerTv.text = header
    }

    override fun getLayout(): Int {
        return R.layout.item_service_header_layout
    }
}