package com.example.og

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class ListAdapter(context: Context, val arrayListEmployee: List<employees>) :
    ArrayAdapter<employees>(context, R.layout.custom_list_item, arrayListEmployee) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.context)
                .inflate(R.layout.custom_list_item, parent, false)
        }

        val tvID = convertView!!.findViewById<TextView>(R.id.id)
        val tvName = convertView.findViewById<TextView>(R.id.name)
        val tvEmail = convertView.findViewById<TextView>(R.id.email)
        val tvPassword = convertView.findViewById<TextView>(R.id.password)

        tvID.text = arrayListEmployee[position].id
        tvName.text = arrayListEmployee[position].name
        tvEmail.text = arrayListEmployee[position].email
        tvPassword.text = arrayListEmployee[position].password

        return convertView
    }
}