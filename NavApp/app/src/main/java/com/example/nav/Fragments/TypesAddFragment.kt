package com.example.nav.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.nav.Entities.Types
import com.example.nav.Fragments_ViewModels.TypesViewModel

import com.example.nav.R

/**
 * A simple [Fragment] subclass.
 */
class TypesAddFragment : Fragment() {

    private val model: TypesViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val inf = inflater.inflate(R.layout.fragment_types_add, container, false)
        val addBtn: Button = inf.findViewById(R.id.addTypesBtn)
        val editTxt: EditText = inf.findViewById(R.id.typesAddTxt)

        addBtn.setOnClickListener { setType(editTxt) }

        return inf
    }

    private fun setType(editTxt: EditText){
        val tempType = Types()
        val name = editTxt.text.toString()
        val typeExist = model.typeExist(name)
        if (typeExist){
            Toast.makeText(activity!!, "Type already exist", Toast.LENGTH_SHORT).show()
        }else if (!typeExist){
            tempType.name = name
            val token = "Bearer " + activity!!.intent.getStringExtra("Token")
            model.addType(tempType, token, activity!!)
            editTxt.onEditorAction(EditorInfo.IME_ACTION_DONE)
            val fragment = AdminTypesFragment()
            activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragmentFrame, fragment).commit()
        }

    }


}
