package com.telemedicine.myclinic.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.telemedicine.myclinic.models.FuzzySearchMinModel
import com.telemedicine.myclinic.models.homevisit.OrdersListResponse
import com.telemedicine.myclinic.myapplication.R
import com.telemedicine.myclinic.util.LocaleDateHelper
import com.telemedicine.myclinic.views.RegularTextView


class MedicationDeliveriesAdapter(private val context: Context, private val list:  List<OrdersListResponse>,
                                  private val listener: ClickItemListener
) :
    RecyclerView.Adapter<MedicationDeliveriesAdapter.MedicationDeliveriesViewHolder>() {

    interface ClickItemListener {
        fun onClicked(position: Int, model: OrdersListResponse)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicationDeliveriesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MedicationDeliveriesViewHolder(
            inflater,
            parent
        )
    }

    override fun onBindViewHolder(holder: MedicationDeliveriesViewHolder, position: Int) {
        val model: OrdersListResponse? = list!![position]
        holder.bind(model!!, position, context)

        holder.mainContainerView!!.setOnClickListener {
            listener.onClicked(
                position,
                model!!
            )
        }
    }

    override fun getItemCount(): Int = list.size!!

    class MedicationDeliveriesViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.item_my_medication_deliveries_layout, parent, false)) {

        var mainContainerView: LinearLayoutCompat? = null
        var statusTv: RegularTextView? = null
        var districtTv: RegularTextView? = null
        var doctorNameTv: RegularTextView? = null
        var appointmentDate: RegularTextView? = null

        init {
            mainContainerView = itemView.findViewById(R.id.main_container_view)
            statusTv = itemView.findViewById(R.id.statusTv)
            districtTv = itemView.findViewById(R.id.districtTv)
            doctorNameTv = itemView.findViewById(R.id.doctorNameTv)
            appointmentDate = itemView.findViewById(R.id.appointmentDate)
        }

        fun bind(order: OrdersListResponse, position: Int, context: Context) {
            if(position == 0 || position % 2 == 0){
                mainContainerView!!.background = ContextCompat.getDrawable(context, R.color.colorGrey)
            }else{
                mainContainerView!!.background = ContextCompat.getDrawable(context, R.color.colorWhite)
            }

            if(order?.orderStatus != null){
                when(order.orderStatus){
                    2 -> {
                        statusTv!!.text = "No Delivered"
                    }
                    3 ->{
                        statusTv!!.text = "En Route"
                    }
                    4 -> {
                        statusTv!!.text = "Delivered"
                    }
                    5 -> {
                        statusTv!!.text = "Cancelled"
                    }
                    else -> {
                        statusTv!!.text = "null"
                    }
                }
            }else{
                statusTv!!.text = "null"
            }

            districtTv!!.text = order.lable?:"null"
            doctorNameTv!!.text = order.dortorName?:""

            val date = LocaleDateHelper.convertDateStringFormat(
                order.apptDate,
                "yyyy-MM-dd'T'HH:mm:ss",
                "dd-MMM-yyyy"
            )

            appointmentDate!!.text = date
        }
    }
}