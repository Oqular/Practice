package com.example.nav.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.nav.Entities.Types
import com.example.nav.Fragments_ViewModels.TypesViewModel

import com.example.nav.R
import kotlinx.android.synthetic.main.fragment_types_edit.*

/**
 * A simple [Fragment] subclass.
 */
class TypesEditFragment : Fragment() {

    private val model: TypesViewModel by activityViewModels()
    private lateinit var item: Types

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val inf = inflater.inflate(R.layout.fragment_types_edit, container, false)

        val editTxt: EditText = inf.findViewById(R.id.typesEditTxtEdit)
        val editBtn: Button = inf.findViewById(R.id.typesEditBtn)

        val args: Bundle = arguments!!
        val adapterPosition: Int = args.getInt("position")
        item = model.typesList.value!![adapterPosition]
        editTxt.hint = item.name

        editBtn.setOnClickListener { changeTypeName(editTxt) }

        return inf
    }

    private fun changeTypeName(editTxt: EditText){
        val name: String = editTxt.text.toString()
        val typeExist = model.typeExist(name)
        if (typeExist) {
            Toast.makeText(activity!!, "Type already exist", Toast.LENGTH_SHORT).show()
        }else if (!typeExist){
            item.name = name
            val token = "Bearer " + activity!!.intent.getStringExtra("Token")
            model.editType(item, token, activity!!)
            editTxt.onEditorAction(EditorInfo.IME_ACTION_DONE)
            val fragment = AdminTypesFragment()
            activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragmentFrame, fragment).commit()//cant go back after editing
        }

    }


}
