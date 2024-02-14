package com.telemedicine.myclinic.adapters
import com.telemedicine.myclinic.myapplication.R
import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.telemedicine.myclinic.base.BaseRecyclerAdapter
import com.telemedicine.myclinic.models.company.Company
import com.telemedicine.myclinic.util.TextUtil
import kotlinx.android.synthetic.main.item_company_selection.view.*


class CompanySelectionAdapter(private val context: Context) : BaseRecyclerAdapter<Company, CompanySelectionAdapter.CompanyViewHolder>(context) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompanyViewHolder {
        return CompanyViewHolder(parent.inflate(R.layout.item_company_selection))
    }

    override fun onBindViewHolder(holder: CompanyViewHolder, position: Int) {

        val company = items[position];

        if(TextUtil.getArabic(context)){


            holder.itemView.companyArTextView.visibility = View.VISIBLE
            holder.itemView.cityArTextView.visibility = View.VISIBLE

            holder.itemView.companyEnTextView.visibility = View.GONE
            holder.itemView.cityEnTextView.visibility = View.GONE


            holder.itemView.companyArTextView.text = company.companyFullAr
            holder.itemView.cityArTextView.text = company.cityAr



        }else{

            holder.itemView.companyEnTextView.visibility = View.VISIBLE
            holder.itemView.cityEnTextView.visibility = View.VISIBLE

            holder.itemView.companyArTextView.visibility = View.GONE
            holder.itemView.cityArTextView.visibility = View.GONE


            holder.itemView.companyEnTextView.text = company.companyFullEn
            holder.itemView.cityEnTextView.text = company.cityEn
        }

        holder.itemView.setOnClickListener {
            listener?.onItemSelected(company,position,it)
        }
    }

    class CompanyViewHolder (view: View): RecyclerView.ViewHolder(view)
}