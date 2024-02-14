package com.telemedicine.myclinic.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.telemedicine.myclinic.models.homevisit.MedicineList
import com.telemedicine.myclinic.myapplication.R

class MedicineListAdapter(
    private val list: List<MedicineList?>?,
    private val mContext: Context
) :
    RecyclerView.Adapter<MedicineListAdapter.DeliveriesViewHolder>() {
//
    /* interface ClickItemListener {
         fun onClicked(position: Int, model: AssignedDeliveriesResponseModel.Orders)
         fun onActionClicked(position: Int, model: AssignedDeliveriesResponseModel.Orders)
         fun onDistrictClicked(position: Int, model: AssignedDeliveriesResponseModel.Orders)
         fun onPhoneClicked(position: Int, model: AssignedDeliveriesResponseModel.Orders)
     }*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveriesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return DeliveriesViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: DeliveriesViewHolder, position: Int) {
        val assignedDeliveries: MedicineList? = list?.get(position)
        holder.bind(assignedDeliveries, position, mContext)

    }

    override fun getItemCount(): Int = list?.size!!



    class DeliveriesViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.item_ordered_medication, parent, false)) {

        var nameMedication: AppCompatTextView? = null
        var nameDosage: AppCompatTextView? = null

        init {
            nameMedication = itemView.findViewById(R.id.name_medication)
            nameDosage = itemView.findViewById(R.id.name_dosage)
        }

        fun bind(medicine: MedicineList?, position: Int, context: Context) {

            if(medicine != null){
                if(medicine.name != null){
                    nameMedication!!.text = medicine.name
                }else{
                    nameMedication!!.text = "null"
                }

                if(medicine.dosage != null){
                    nameDosage!!.text = medicine.dosage
                }else{
                    nameDosage!!.text = "null"
                }

            }
        }
    }
}
