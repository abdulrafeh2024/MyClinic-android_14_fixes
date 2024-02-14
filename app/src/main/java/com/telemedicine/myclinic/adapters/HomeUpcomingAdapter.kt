package com.telemedicine.myclinic.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.telemedicine.myclinic.models.appointments.ApptsMiniModel
import com.telemedicine.myclinic.models.homemodels.DoctorsProfile
import com.telemedicine.myclinic.myapplication.R
import de.hdodenhof.circleimageview.CircleImageView

class HomeUpcomingAdapter(
        private val context: Context
) :
        RecyclerView.Adapter<HomeUpcomingAdapter.HomeUpcomingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeUpcomingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return HomeUpcomingViewHolder(
                inflater,
                parent
        )
    }

    override fun onBindViewHolder(holder: HomeUpcomingViewHolder, position: Int) {
    }

    override fun getItemCount(): Int = 3

    class HomeUpcomingViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
            RecyclerView.ViewHolder(inflater.inflate(R.layout.item_home_upcoming_appointment_view, parent, false)) {

        private var doctorName: AppCompatTextView? = null
        private var doctorDepartment: AppCompatTextView? = null
        private var doctorimage: CircleImageView? = null
        var constraintLayout: ConstraintLayout? = null

        init { }

        fun bind(model: ApptsMiniModel, mContext: Context) {}
    }

}
