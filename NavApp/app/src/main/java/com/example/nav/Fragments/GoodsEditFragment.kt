package com.example.nav.Fragments


import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nav.Adapters.TempImageRecyclerAdapter
import com.example.nav.Adapters.TypesListAdapter
import com.example.nav.Entities.*
import com.example.nav.Fragments_ViewModels.GoodsViewModel
import com.example.nav.Fragments_ViewModels.TypesViewModel
import com.example.nav.Interfaces.IApi
import com.example.nav.Interfaces.IonRecyclerClickListener
import com.example.nav.MainActivity.Companion.BaseUrl

import com.example.nav.R
import kotlinx.android.synthetic.main.fragment_goods_edit.*
import okio.Utf8
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.charset.Charset

/**
 * A simple [Fragment] subclass.
 */
class GoodsEditFragment : Fragment(), IonRecyclerClickListener {

    private val model: GoodsViewModel by activityViewModels()
    private lateinit var item: Goods
    private var imgArr: ArrayList<TempImg> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val inf = inflater.inflate(R.layout.fragment_goods_edit, container, false)
        val token = "Bearer " + activity!!.intent.getStringExtra("Token")
        val goodsEditButton: Button = inf.findViewById(R.id.goodsEditSubmit)
        val imageAddButton: Button = inf.findViewById(R.id.goodsEditAddImage)
        val progressBar: ProgressBar = inf.findViewById(R.id.loadingGoodsEdit)
        //------------------------------------------------------------------------------------------
        val args: Bundle = arguments!!
        val infoId: Int = args.getInt("goodsId")

        getGoodsInfo(infoId, progressBar)

        imageAddButton.setOnClickListener { chooseImageToUpload() }
        goodsEditButton.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            editSubmit(token, progressBar)
        }

        return inf
    }

    private fun editSubmit(token: String, progressBar: ProgressBar){
        item.title = goodsEditTitle.text.toString()
        item.address = goodsEditAddress.text.toString()
        item.phone = goodsEditPhone.text.toString()
        item.description = goodsEditDescription.text.toString()

        model.editGoods(item, token, activity!!, progressBar)
    }

    private fun getGoodsInfo(infoId: Int, progressBar: ProgressBar){
        val retro = Retrofit.Builder().baseUrl(BaseUrl).addConverterFactory(GsonConverterFactory.create()).build()
        val service = retro.create(IApi::class.java)
        val call =  service.getGoodsById(infoId)
        call.enqueue(object : Callback<Goods> {
            override fun onResponse(call: Call<Goods>, response: Response<Goods>) {
                if (response.code() == 200) {
                    item = response.body()!!
                    setEditableInfo()
                    progressBar.visibility = View.GONE
                }else{
                    Toast.makeText(activity, "The item no longer exist", Toast.LENGTH_SHORT).show()
                    val fragment = GoodsFragment()
                    activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragmentFrame, fragment).commit()
                }
            }
            override fun onFailure(call: Call<Goods>, t: Throwable) {
                Toast.makeText(activity, "Failure", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setEditableInfo(){
        goodsEditTitle.setText(item.title)
        goodsEditAddress.setText(item.address)
        goodsEditPhone.setText(item.phone)
        goodsEditDescription.setText(item.description)

        imgArr = convertImageArray(item.images)
        recyclerDisplay(imgArr)

    }

    //Convert from Image array to tempImage array that is used in adapter to show images on screen
    private fun convertImageArray(imgArray: ArrayList<Image>): ArrayList<TempImg>{
        val result: ArrayList<TempImg> = ArrayList()
        for (image in imgArray) {
            val img: ByteArray = Base64.decode(image.imageB, 0)//--------
            val bitmap: Bitmap = BitmapFactory.decodeByteArray(img, 0, img.size, BitmapFactory.Options())//-------
            val imgStuff = TempImg()
            imgStuff.image = bitmap
            result.add(imgStuff)
        }
        return result
    }

    //converting image(bitmap) to byte array
    private fun convertToByteArray(bitmap: Bitmap){
        val byteArrOS = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrOS)
        val bytes: ByteArray = byteArrOS.toByteArray()
        val str: String = Base64.encodeToString(bytes, 0)//String(img, Charset.forName("UTF-8"))
        val image = Image()
        image.imageB = str
        item.images.add(image)
        imgArr = convertImageArray(item.images)
        recyclerDisplay(imgArr)
    }

    private fun chooseImageToUpload(){
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1){
            if (data != null){
                val contentUri = data.data
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(activity!!.contentResolver, contentUri)
                    convertToByteArray(bitmap)
                }
                catch (e: IOException){
                    Toast.makeText(activity!!, "Failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun recyclerDisplay(imgArray: ArrayList<TempImg>){
        //---------------------------Recycler display-----------------------------------
        val viewManager: RecyclerView.LayoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        val viewAdapter: RecyclerView.Adapter<*> = TempImageRecyclerAdapter(imgArray, this)
        activity!!.findViewById<RecyclerView>(R.id.goodsEditImageList).apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
        //-------------------------end of display stuff---------------------------------
    }

    override fun onRecyclerItemClick(position: Int) {
        item.images.removeAt(position)
        imgArr.removeAt(position)
        recyclerDisplay(imgArr)
    }
}
