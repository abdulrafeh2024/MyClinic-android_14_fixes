package com.telemedicine.myclinic.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.telemedicine.myclinic.models.CardModel
import com.telemedicine.myclinic.models.FuzzySearchMinModel
import com.telemedicine.myclinic.models.FuzzySearchResponseModel
import com.telemedicine.myclinic.myapplication.R
import com.telemedicine.myclinic.util.TextUtil
import com.telemedicine.myclinic.views.BoldTextView
import com.telemedicine.myclinic.views.RegularTextView
import kotlinx.android.synthetic.main.activity_search_appointment_filter.*


class FuzzySearchRvAdapter(private val context: Context, private val list:  List<FuzzySearchMinModel?>?,
                           private val listener: ClickItemListener
) :
        RecyclerView.Adapter<FuzzySearchRvAdapter.FuzzySearchViewHolder>() {

    interface ClickItemListener {
        fun onClicked(position: Int, model: FuzzySearchMinModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FuzzySearchViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return FuzzySearchViewHolder(
                inflater,
                parent
        )
    }

    override fun onBindViewHolder(holder: FuzzySearchViewHolder, position: Int) {
        val model: FuzzySearchMinModel? = list!![position]

        holder.bind(model!!, context)

        holder.fuzzySearchLayout!!.setOnClickListener {
            listener.onClicked(position, model)
        }

    }

    override fun getItemCount(): Int = list!!.size

    class FuzzySearchViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
            RecyclerView.ViewHolder(inflater.inflate(R.layout.item_fuzzy_search_layout, parent, false)) {

        var fuzzySearchLayout: ConstraintLayout? = null
        private var description: RegularTextView? = null
        private var image: AppCompatImageView? = null
        private var arrowImage: AppCompatImageView? = null

        init {
            fuzzySearchLayout = itemView.findViewById(R.id.fuzzy_search_layout)
            description = itemView.findViewById(R.id.fuzzy_search_description)
            image = itemView.findViewById(R.id.fuzzy_search_img)
            arrowImage = itemView.findViewById(R.id.nextBtn)
        }

        fun bind(model: FuzzySearchMinModel?, mContext: Context) {

            if (TextUtil.getEnglish(mContext)) {
                description!!.text = model?.name ?: ""
            } else if (TextUtil.getArabic(mContext)) {
                description!!.text = model?.arabicName ?: ""
                fuzzySearchLayout?.layoutDirection = View.LAYOUT_DIRECTION_RTL
                arrowImage?.setImageResource(R.drawable.arrow_left);
            }

            when (model?.description) {
                "Location" -> {
                    image?.setImageResource(R.drawable.ic_single_location)
                }
                "Doctor" -> {
                    image?.setImageResource(R.drawable.doctr)
                }
                else -> {
                    image?.setImageResource(R.drawable.medical_folder)
                }
            }
        }
    }
}