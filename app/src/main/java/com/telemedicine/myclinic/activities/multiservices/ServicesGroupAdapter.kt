package com.telemedicine.myclinic.activities.multiservices

import android.content.Context
import android.view.Gravity
import android.view.View
import com.telemedicine.myclinic.myapplication.R
import com.telemedicine.myclinic.util.TextUtil
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.item_services_list_layout.view.*

class ServicesGroupAdapter(
    val serviceItems: ServiceItems? = null,
    val context: Context? = null,
    val onClickListiner: OnCardClickListner
): Item<ViewHolder>() {

    override fun bind(viewHolder: ViewHolder, position: Int) {

        if (TextUtil.getArabic(context)) {
            viewHolder.itemView.serviceName.gravity = Gravity.RIGHT
        }
        
        if (!serviceItems?.isPayAllowed!!) {
            //invisible hai ye
            viewHolder.itemView.checkbox.visibility = View.INVISIBLE
        } else {
            if(serviceItems.amount == 0.0 && serviceItems.responsibility != "insurance"){
                viewHolder.itemView.checkbox.visibility = View.INVISIBLE
            }else{
                viewHolder.itemView.checkbox.visibility = View.VISIBLE
            }
        }
        viewHolder.itemView.serviceName.text = serviceItems.name

        if(serviceItems.amount != 0.0){
            viewHolder.itemView.amountTv.text = "Amount: " +serviceItems.amount
        }else{
            viewHolder.itemView.amountTv.text = ""
        }

        if(TextUtil.getEnglish(context)){
            viewHolder.itemView.paymentStatus.text = serviceItems.responsibility
        }else{
            when (serviceItems.responsibility) {
                "self" -> {
                    viewHolder.itemView.paymentStatus.text = context?.getString(R.string.Self)
                }
                "insurance" -> {
                    viewHolder.itemView.paymentStatus.text = context?.getString(R.string.Insurance)
                }
                else -> {
                    viewHolder.itemView.paymentStatus.text = serviceItems.responsibility
                }
            }
        }

       //viewHolder.itemView.serviceStatus.text = serviceItems.serviceStatus

        when (serviceItems.serviceStatus) {
            "Paid" -> {
                viewHolder.itemView.serviceStatus.text = context?.getString(R.string.paid)
            }
            "Ordered" -> {
                viewHolder.itemView.serviceStatus.text = context?.getString(R.string.ordered)
            }
            "Started" -> {
                viewHolder.itemView.serviceStatus.text =  context?.getString(R.string.started)
            }
            "Decline" -> {
                viewHolder.itemView.serviceStatus.text =  context?.getString(R.string.declined)
            }
            "Complete" -> {
                viewHolder.itemView.serviceStatus.text =  context?.getString(R.string.completed)
            }
            "cancelled" -> {
                viewHolder.itemView.serviceStatus.text =  context?.getString(R.string.cancelled)
            }
            "Discontinued" -> {
                viewHolder.itemView.serviceStatus.text =  context?.getString(R.string.discontinued)
            }
            else -> {
                viewHolder.itemView.serviceStatus.text =  serviceItems.serviceStatus
            }
        }

        if(serviceItems.IsPaid){
            viewHolder.itemView.disableView.visibility = View.VISIBLE
        }else{
            viewHolder.itemView.disableView.visibility = View.INVISIBLE
        }

        if(serviceItems.isSelected){
            viewHolder.itemView.checkbox.isChecked = true
        }else{
            viewHolder.itemView.checkbox.isChecked = false
        }


        viewHolder.itemView.checkbox.setOnClickListener {
            onClickListiner.OnCardClicked(serviceItems, position,viewHolder.itemView.checkbox.isChecked);
            if(viewHolder.itemView.checkbox.isChecked){
                serviceItems.setSelected(true)
            }else{
                serviceItems.setSelected(false)
            }
        }
    }

    override fun getLayout(): Int {
        return  R.layout.item_services_list_layout
    }

    interface OnCardClickListner {
        fun OnCardClicked(model: ServiceItems?, pos: Int, isChecked: Boolean)
    }


}

