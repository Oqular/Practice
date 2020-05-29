package com.example.nav.Fragments


import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nav.Adapters.TempImageRecyclerAdapter
import com.example.nav.Adapters.TypesListAdapter
import com.example.nav.Entities.Goods
import com.example.nav.Entities.TempImg
import com.example.nav.Entities.Types
import com.example.nav.Entities.Types_Goods
import com.example.nav.Fragments_ViewModels.GoodsViewModel
import com.example.nav.Fragments_ViewModels.TypesViewModel
import com.example.nav.Interfaces.IonRecyclerClickListener

import com.example.nav.R
import java.io.IOException

/**
 * A simple [Fragment] subclass.
 */
class GoodsAddFragment : Fragment(), IonRecyclerClickListener {

    private val modelT: TypesViewModel by activityViewModels()
    private val modelG: GoodsViewModel by activityViewModels()
    private val selectedTypesArr: ArrayList<Types> = ArrayList()
    private lateinit var inf: View
    private val imgArray: ArrayList<TempImg> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        inf = inflater.inflate(R.layout.fragment_goods_add, container, false)
        //--------------------------------ui initialize---------------------------------------------
        val signAddImage: Button = inf.findViewById(R.id.signAddImage)
        val signGoods: Button = inf.findViewById(R.id.signGoods)
        //------------------------------------------------------------------------------------------
        typesSelection()

        signAddImage.setOnClickListener { chooseImageToUpload() }

        signGoods.setOnClickListener { uploadInfo() }

        return inf
    }

    private fun uploadInfo(){
        //--------------------------------initialize ui elements------------------------------------
        val signTitle: EditText = inf.findViewById(R.id.signTitle)
        val signDescription: EditText = inf.findViewById(R.id.signDescription)
        val signAddress: EditText = inf.findViewById(R.id.signAddress)
        val signPhone: EditText = inf.findViewById(R.id.signPhone)
        val addProgressBar: ProgressBar = inf.findViewById(R.id.addProgressBar)
        //------------------------------------------------------------------------------------------
        addProgressBar.visibility = View.VISIBLE
        val token = "Bearer " + activity!!.intent.getStringExtra("Token")
        val uId: Int = activity!!.intent.getIntExtra("userId", -1)
        //------------------------------------------------------------------------------------------
        val types: ArrayList<Types_Goods> = ArrayList()
        for (t in selectedTypesArr) {
            val type = Types_Goods()
            type.typesId = t.id
            types.add(type)
        }

        val goods = Goods()
        goods.title = signTitle.text.toString()
        goods.description   = signDescription.text.toString()
        goods.address = signAddress.text.toString()
        goods.phone = signPhone.text.toString()
        goods.seller = activity!!.intent.getStringExtra("userName")
        goods.type = types
        if (uId == -1){
            goods.userId = null
        }
        else{
            goods.userId = uId
        }

        modelG.postGoodsData(goods, imgArray, token, activity!!, addProgressBar)

    }

    private fun chooseImageToUpload(){
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, 1)
    }

    private fun getItemPath(contentUri: Uri): String{
        //-----------------------------------
        val cursor: Cursor = activity!!.contentResolver.query( contentUri,
            null,
            null,       // WHERE clause; which rows to return (all rows)
            null,       // WHERE clause selection arguments (none)
            null)!! // Order-by clause (ascending by name)
        val columnIndex: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(columnIndex)
        //-----------------------------------
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1){
            if (data != null){
                val contentUri = data.data
//                con = contentUri!!.path!!
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(activity!!.contentResolver, contentUri)
                    val imgStuff = TempImg()
                    imgStuff.image = bitmap
                    imgStuff.path = getItemPath(contentUri!!)
                    imgArray.add(imgStuff)
                    recyclerDisplay()
                }
                catch (e: IOException){
                    Toast.makeText(activity!!, "Failed", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun recyclerDisplay(){
        //---------------------------Recycler display-----------------------------------
        val viewManager: RecyclerView.LayoutManager = LinearLayoutManager(activity!!, LinearLayoutManager.HORIZONTAL, false)
        val viewAdapter: RecyclerView.Adapter<*> = TempImageRecyclerAdapter(imgArray, this)
        inf.findViewById<RecyclerView>(R.id.signImageList).apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
        //-------------------------end of display stuff---------------------------------
    }

    private fun typesSelection(){
        //-----------------------------------initialize lists---------------------------------------
        val signTypesListSelector: ListView = inf.findViewById(R.id.signTypesListSelector)
        val signTypesListChosen: ListView = inf.findViewById(R.id.signTypesListChosen)
        //------------------------------------------------------------------------------------------
        val typesArr: ArrayList<Types> = modelT.typesList.value!!
        val arrayAdapter = TypesListAdapter(activity!!, typesArr)
        signTypesListSelector.adapter = arrayAdapter

        //select type
        signTypesListSelector.setOnItemClickListener { parent, view, position, id ->
            val item: Types = signTypesListSelector.getItemAtPosition(position) as Types
            if (!selectedTypesArr.contains(item)) {
                if (selectedTypesArr.size < 3) {
                    selectedTypesArr.add(item)
                    val arrayAdapter = TypesListAdapter(activity!!, selectedTypesArr)
                    signTypesListChosen.adapter = arrayAdapter
                }
                else
                    Toast.makeText(activity!!, "Can choose only 3 types", Toast.LENGTH_SHORT).show()
            }
            else
                Toast.makeText(activity!!, "Type already selected", Toast.LENGTH_SHORT).show()
        }

        //delete item from selected list(unselect)
        signTypesListChosen.setOnItemClickListener { parent, view, position, id ->
            val item: Types = signTypesListChosen.getItemAtPosition(position) as Types
            selectedTypesArr.remove(item)
            val arrayAdapter = TypesListAdapter(activity!!, selectedTypesArr)
            signTypesListChosen.adapter = arrayAdapter
            Toast.makeText(activity!!, id.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRecyclerItemClick(position: Int) {
        //Delete items from the list
        imgArray.removeAt(position)
        recyclerDisplay()
    }
}
