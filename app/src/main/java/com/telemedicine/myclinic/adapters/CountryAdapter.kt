package com.telemedicine.myclinic.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.telemedicine.myclinic.base.BaseRecyclerAdapter
import com.telemedicine.myclinic.models.profilecreatoinmodels.Nationalities
import com.telemedicine.myclinic.myapplication.R
import com.telemedicine.myclinic.util.TextUtil
import kotlinx.android.synthetic.main.item_country.view.*


class CountryAdapter(val context: Context) : BaseRecyclerAdapter<Nationalities, CountryAdapter.CountryViewHolder>(context){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        return CountryViewHolder(parent.inflate( R.layout.item_country))
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {

        if(TextUtil.getArabic(context)){
            holder.itemView.nameTextView.text = items[position].nameAr
        }else{
            holder.itemView.nameTextView.text = items[position].nameEn
        }

        holder.itemView.setOnClickListener {
            listener?.onItemSelected(item = items[position],position = position,view = it)
        }

    }

    fun search(searchText:String){

        if(searchText.isEmpty() or searchText.isBlank()){

            items.addAll(tempList)

        }else{

            items.clear()
            tempList.forEach {
                if(TextUtil.getArabic(context)){
                    if(  it.nameAr.toLowerCase().contains(searchText)){
                        items.add(it)
                    }
                }else{
                    if(  it.nameEn.toLowerCase().contains(searchText)){
                        items.add(it)
                    }
                }
            }
            notifyDataSetChanged()
        }


    }

    class CountryViewHolder (view: View):RecyclerView.ViewHolder(view)
}