package com.telemedicine.myclinic.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.telemedicine.myclinic.models.favouritedoctors.FavouriteDoctorsMiniModel
import com.telemedicine.myclinic.myapplication.R
import com.telemedicine.myclinic.util.TextUtil
import com.telemedicine.myclinic.views.BoldTextView
import com.telemedicine.myclinic.views.LightTextView
import de.hdodenhof.circleimageview.CircleImageView

class FavouriteDoctorsRvAdapter(private val context: Context, private val list: List<FavouriteDoctorsMiniModel?>?,
                                private val listener: ClickItemListener
) :
        RecyclerView.Adapter<FavouriteDoctorsRvAdapter.FavouriteDoctorViewHolder>() {

    interface ClickItemListener {
        fun onClicked(position: Int, model: FavouriteDoctorsMiniModel, isFav: Boolean)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteDoctorViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return FavouriteDoctorViewHolder(
                inflater,
                parent
        )
    }

    override fun onBindViewHolder(holder: FavouriteDoctorViewHolder, position: Int) {
        val model: FavouriteDoctorsMiniModel? = list!![position]
        holder.bind(model!!, context)

        holder.doctorLayout!!.setOnClickListener {
            listener.onClicked(position, model, false)
        }
        holder.favImg!!.setOnClickListener{
            listener.onClicked(position, model, true)
        }

    }

    override fun getItemCount(): Int = list!!.size

    class FavouriteDoctorViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
            RecyclerView.ViewHolder(inflater.inflate(R.layout.item_doctor_card_layout, parent, false)) {

        var doctorLayout: CardView? = null
         var favImg: AppCompatImageView? = null
        private var doctorImage: CircleImageView? = null
        private var doctorName: BoldTextView? = null
        private var doctorProfession: LightTextView? = null

        init {
            doctorLayout = itemView.findViewById(R.id.doctor_layout)
            favImg = itemView.findViewById(R.id.favImg)
            doctorImage = itemView.findViewById(R.id.doctor_image)
            doctorName = itemView.findViewById(R.id.doctor_name)
            doctorProfession = itemView.findViewById(R.id.doctor_profession)
        }

        fun bind(model: FavouriteDoctorsMiniModel?, mContext: Context) {

            if(TextUtil.getArabic(mContext)){
                doctorName!!.text = model?.doctorNameAr ?: ""
                doctorProfession!!.text = model?.specialtyAr?:""
            }else{
                doctorName!!.text = model?.doctorNameEn ?: ""
                doctorProfession!!.text = model?.specialtyEn?:""
            }


            Glide.with(mContext).load(model?.doctorProfilePic).placeholder(R.drawable.doctr).into(doctorImage as ImageView)

            if(model?.isFavorite!!){
                favImg!!.setImageResource(R.drawable.ic_star_selected)
            }else{
                favImg!!.setImageResource(R.drawable.ic_un_selected_star)
            }
        }
    }
}