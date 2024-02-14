package com.telemedicine.myclinic.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.telemedicine.myclinic.models.FuzzySearchMinModel
import com.telemedicine.myclinic.models.homevisit.OrdersListResponse
import com.telemedicine.myclinic.models.homevisit.PatientAddressBookList
import com.telemedicine.myclinic.myapplication.R
import com.telemedicine.myclinic.views.RegularTextView


class MyAddressAdapter(private val context: Context, private val list:  List<PatientAddressBookList>,
                       private val listener: ClickItemListener
) :
    RecyclerView.Adapter<MyAddressAdapter.MyAddressViewHolder>() {

    interface ClickItemListener {
        fun onClicked(position: Int, model: PatientAddressBookList, isDelete: Boolean)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAddressViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyAddressViewHolder(
            inflater,
            parent
        )
    }

    override fun onBindViewHolder(holder: MyAddressViewHolder, position: Int) {
        val model: PatientAddressBookList? = list!![position]
        holder.bind(model!!, position, context)
        holder.mainContainerView!!.setOnClickListener {
            listener.onClicked(
                position,
                model!!,
                false
            )
        }

        holder.deleteImg!!.setOnClickListener {
            listener.onClicked(
                position,
                model!!,
                true
            )
        }
    }

    override fun getItemCount(): Int = list.size!!

    class MyAddressViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.item_my_address_book_layout, parent, false)) {

        var mainContainerView: LinearLayoutCompat? = null
        var deleteImg: AppCompatImageView? = null
        var nameTv: RegularTextView? = null
        var districtTv: RegularTextView? = null
        var streetTv: RegularTextView? = null

        init {
            mainContainerView = itemView.findViewById(R.id.mainContainerView)
            nameTv = itemView.findViewById(R.id.nameTv)
            deleteImg = itemView.findViewById(R.id.deleteImg)
            districtTv = itemView.findViewById(R.id.districtTv)
            streetTv = itemView.findViewById(R.id.streetTv)
        }

        fun bind(patientAddressBookList: PatientAddressBookList, position: Int, context: Context) {
            if(position == 0 || position % 2 == 0){
                mainContainerView!!.background = ContextCompat.getDrawable(context, R.color.colorGrey)
            }else{
                mainContainerView!!.background = ContextCompat.getDrawable(context, R.color.colorWhite)
            }

            districtTv!!.text = patientAddressBookList.district?:"null"
            streetTv!!.text = patientAddressBookList.street?:"null"
            nameTv!!.text = patientAddressBookList.lable?:"null"

        }


    }
}