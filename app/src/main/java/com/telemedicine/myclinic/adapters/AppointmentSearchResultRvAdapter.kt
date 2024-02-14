package com.telemedicine.myclinic.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.telemedicine.myclinic.models.appointments.ApptsMiniModel
import com.telemedicine.myclinic.myapplication.R
import com.telemedicine.myclinic.views.BoldTextView
import com.wajahatkarim3.easyflipview.EasyFlipView
import de.hdodenhof.circleimageview.CircleImageView


class AppointmentSearchResultRvAdapter(
        private val context: Context,
        private val list: List<Any>,
        val onMonthView: (() -> Unit)? = null
) :
        RecyclerView.Adapter<AppointmentSearchResultRvAdapter.AppointmentSearchResultViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentSearchResultViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return AppointmentSearchResultViewHolder(
                inflater,
                parent
        )
    }

    override fun onBindViewHolder(holder: AppointmentSearchResultViewHolder, position: Int) {
        holder.bind(context)
    }

    override fun getItemCount(): Int = 5

    class AppointmentSearchResultViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
            RecyclerView.ViewHolder(inflater.inflate(R.layout.item_search_appointment_doctor_layout, parent, false))
    , View.OnClickListener{

         var monthView: BoldTextView? = null
        private var doctorDepartment: AppCompatTextView? = null
        private var doctorimage: CircleImageView? = null
        var constraintLayout: ConstraintLayout? = null
        var myEasyFlipView: EasyFlipView? = null

        init { }

        fun bind(mContext: Context) {
           // monthView = itemView.findViewById(R.id.monthView)
            myEasyFlipView = itemView.findViewById(R.id.myEasyFlipView);
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            myEasyFlipView?.flipTheView();
        }
    }

}
