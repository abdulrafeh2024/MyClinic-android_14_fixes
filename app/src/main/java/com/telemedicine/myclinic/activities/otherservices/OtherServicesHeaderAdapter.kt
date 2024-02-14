package com.telemedicine.myclinic.activities.otherservices

import com.telemedicine.myclinic.myapplication.R
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.item_other_services_header_layout.view.*

class OtherServicesHeaderAdapter(val header: String) : Item<ViewHolder>() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.other_services_title.text = header
    }

    override fun getLayout(): Int {
        return R.layout.item_other_services_header_layout
    }
}