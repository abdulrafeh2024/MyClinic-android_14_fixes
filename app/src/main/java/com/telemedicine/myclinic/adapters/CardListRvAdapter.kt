package com.telemedicine.myclinic.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.telemedicine.myclinic.models.CardModel
import com.telemedicine.myclinic.myapplication.R
import com.telemedicine.myclinic.views.BoldTextView
import com.telemedicine.myclinic.views.RegularTextView


class CardListRvAdapter(private val context: Context, private val list: List<CardModel?>?,
                        private val listener: ClickItemListener
) :
        RecyclerView.Adapter<CardListRvAdapter.CardListViewHolder>() {

    interface ClickItemListener {
        fun onClicked(position: Int, id: Int, model: CardModel)
        fun onDeleteCardClicked(id: Int, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CardListViewHolder(
                inflater,
                parent
        )
    }

    override fun onBindViewHolder(holder: CardListViewHolder, position: Int) {
        val model: CardModel? = list!![position]
        holder.bind(model!!, context)

        holder.cardLayout!!.setOnClickListener { listener.onClicked(position, model.paymentCardId, model) }

        holder.deleteLayout?.setOnClickListener {
            holder.swipeLayout!!.close(true)
            listener.onDeleteCardClicked(model.paymentCardId, position)
        }
    }

    override fun getItemCount(): Int = list!!.size

    class CardListViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
            RecyclerView.ViewHolder(inflater.inflate(R.layout.item_payment_card_layout, parent, false)) {

        var cardLayout: ConstraintLayout? = null
        private var cardnumber: RegularTextView? = null
        private var expiryDateTv: BoldTextView? = null
        private var image: AppCompatImageView? = null
        var swipeLayout: SwipeRevealLayout? = null
        var deleteLayout: LinearLayoutCompat? = null

        init {
            swipeLayout = itemView.findViewById(R.id.swipeLayout) as SwipeRevealLayout
            deleteLayout = itemView.findViewById(R.id.deleteLayout)
            cardLayout = itemView.findViewById(R.id.cardLayout)
            cardnumber = itemView.findViewById(R.id.cardNumberTv)
            expiryDateTv = itemView.findViewById(R.id.expiryDateTv)
            image = itemView.findViewById(R.id.appCompatImageView)
        }

        fun bind(model: CardModel?, mContext: Context) {

            cardnumber!!.text = "**** **** **** ${model?.last4Digit}"
            expiryDateTv!!.text = "${model?.expiryMonth}/${model?.expiryYear?.substring(2, 4)}"

            if (model?.cardBrand == "VISA") {
                image?.setImageResource(R.drawable.visa_card)
                cardLayout?.background =  ContextCompat.getDrawable(mContext, R.drawable.blue_card_bg)
            }else{
                image?.setImageResource(R.drawable.master_card)
                cardLayout?.background =  ContextCompat.getDrawable(mContext, R.drawable.orange_card_bg)
            }
        }
    }
}
