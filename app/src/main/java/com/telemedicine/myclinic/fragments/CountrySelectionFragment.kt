package com.telemedicine.myclinic.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.telemedicine.myclinic.adapters.CountryAdapter
import com.telemedicine.myclinic.base.OnItemSelectListener
import com.telemedicine.myclinic.models.profilecreatoinmodels.Nationalities
import com.telemedicine.myclinic.myapplication.R
import kotlinx.android.synthetic.main.fragment_country_selection.*
import java.util.*


class CountrySelectionFragment : DialogFragment() {

    private var listener:OnCountrySelectListener?=null
    private var adapter: CountryAdapter? = null
    private var list:ArrayList<Nationalities>?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            list = it.getParcelableArrayList<Nationalities>(ARG_NATIONALITIES)
        }
        setStyle(STYLE_NORMAL, R.style.appDialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return  inflater.inflate(R.layout.fragment_country_selection, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnCountrySelectListener){
            listener = context
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let {
            adapter = CountryAdapter(it)
            countryRecyclerView.adapter = adapter
            countryRecyclerView.layoutManager = LinearLayoutManager(it)

            adapter?.setItems(list)

            adapter?.addListener(object : OnItemSelectListener<Nationalities> {
                override fun onItemSelected(item: Nationalities, position: Int, view: View) {
                    listener?.onCountrySelected(item)
                    dismiss()
                }
            })
        }
        imageClose.setOnClickListener {
            dismiss()
        }
        editSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                adapter?.search(p0.toString())
            }

        })
    }
    companion object {

        const val ARG_NATIONALITIES = "nationalities"
        @JvmStatic
        fun newInstance(nationalities: ArrayList<Nationalities>) =
                CountrySelectionFragment().apply {
                    arguments = Bundle().apply {
                        putParcelableArrayList(ARG_NATIONALITIES, nationalities)
                    }
                }
    }

    interface OnCountrySelectListener{
        fun onCountrySelected(nationalities: Nationalities)
    }
}