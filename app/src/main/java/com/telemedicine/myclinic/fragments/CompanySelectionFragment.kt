package com.telemedicine.myclinic.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.telemedicine.myclinic.adapters.CompanySelectionAdapter
import com.telemedicine.myclinic.base.OnItemSelectListener
import com.telemedicine.myclinic.models.company.Company
import com.telemedicine.myclinic.myapplication.R
import com.telemedicine.myclinic.util.Const
import com.telemedicine.myclinic.util.TinyDB
import kotlinx.android.synthetic.main.fragment_company_selection.*
import java.util.*

class CompanySelectionFragment : DialogFragment() {

    private var listener: CompanySelectionFragment.OnCompanySelectListener?=null
    private var companyList  : ArrayList<Company>? = null
    private var adapter: CompanySelectionAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            companyList = it.getParcelableArrayList<Company>(ARG_COMPANIES)
        }
        setStyle(DialogFragment.STYLE_NORMAL, R.style.appDialog)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is CompanySelectionFragment.OnCompanySelectListener){
            listener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_company_selection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let {

            adapter = CompanySelectionAdapter(it)
            companyRecyclerView.layoutManager = LinearLayoutManager(it)
            companyRecyclerView.adapter = adapter

            adapter?.setItems(companyList)

            adapter?.addListener(object : OnItemSelectListener<Company> {
                override fun onItemSelected(item: Company, position: Int, view: View) {
                    listener?.onCompanySelected(company = item)
                    TinyDB(it).putString(Const.COMPANY_ID,item.companyId)
                    TinyDB(it).putString(Const.COMPANY_NAME_AR, item.companyFullAr)
                    TinyDB(it).putString(Const.COMPANY_NAME_EN, item.companyFullEn)
                    dismiss()
                }
            })
        }

        imageClose.setOnClickListener {
            dismiss()
        }
    }
    companion object {

        const val ARG_COMPANIES = "arg_companies"

        @JvmStatic
        fun newInstance(companyList : ArrayList<Company>) =
                CompanySelectionFragment().apply {
                    arguments = Bundle().apply {
                        putParcelableArrayList(ARG_COMPANIES, companyList)
                    }
                }
    }

    interface OnCompanySelectListener{
        fun onCompanySelected(company: Company)
    }
}