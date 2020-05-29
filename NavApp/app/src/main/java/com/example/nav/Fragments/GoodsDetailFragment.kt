package com.example.nav.Fragments


import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nav.Adapters.TempImageRecyclerAdapter
import com.example.nav.Entities.Goods
import com.example.nav.Entities.Image
import com.example.nav.Entities.TempImg
import com.example.nav.Entities.Types
import com.example.nav.Fragments_ViewModels.GoodsViewModel
import com.example.nav.Fragments_ViewModels.TypesViewModel
import com.example.nav.Interfaces.IApi
import com.example.nav.Interfaces.IonRecyclerClickListener
import com.example.nav.MainActivity.Companion.BaseUrl

import com.example.nav.R
import kotlinx.android.synthetic.main.fragment_goods_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * A simple [Fragment] subclass.
 */
class GoodsDetailFragment : Fragment(), IonRecyclerClickListener {

    private var imgArr: ArrayList<TempImg> = ArrayList()
    private val modelT: TypesViewModel by activityViewModels()
    private val modelG: GoodsViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val inf = inflater.inflate(R.layout.fragment_goods_detail, container, false)
        //------------------------------------------------------------------------------------------
        //Getting goods id from previous fragment
        val args: Bundle = arguments!!
        val infoId: Int = args.getInt("goodsId")

        //Getting info from API
        getGoodsInfo(infoId, inf)

        return inf
    }

    private fun getGoodsInfo(infoId: Int, inf: View){
        val progressBar: ProgressBar = inf.findViewById(R.id.loadingGoodsDetails)
        //------------------------------------------------------------------------------------------
        val retro = Retrofit.Builder().baseUrl(BaseUrl).addConverterFactory(GsonConverterFactory.create()).build()
        val service = retro.create(IApi::class.java)
        val call =  service.getGoodsById(infoId)
        call.enqueue(object : Callback<Goods> {
            override fun onResponse(call: Call<Goods>, response: Response<Goods>) {
                if (response.code() == 200) {
                    val resp = response.body()!!
                    setDisplayInfo(resp)
                    displayIfLoggedIn(resp, inf)
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

    //Show delete and edit button if logged and if the item is uploaded by viewing user
    private fun displayIfLoggedIn(item: Goods, inf: View){
        val logged = activity!!.intent.getBooleanExtra("loggedIn", false)
        if (logged){
            val uid = activity!!.intent.getIntExtra("userId", -1)
            if (uid == item.userId) {
                val deleteButton: Button = inf.findViewById(R.id.detailsDeleteBtn)
                val editButton: Button = inf.findViewById(R.id.detailsEditBtn)

                deleteButton.visibility = View.VISIBLE
                editButton.visibility = View.VISIBLE

                deleteButton.setOnClickListener { deleteItem(item) }
                editButton.setOnClickListener { editItem(item.id) }
            }
        }
    }

    //Display all information about the requested goods
    private fun setDisplayInfo(item: Goods){
        detailsTitle.text = item.title
        detailsDescription.text = "Description: " + item.description
        detailsSeller.text = "Seller: " + item.seller
        detailsPhone.text = "Phone: " + item.phone
        detailsAddress.text = "Address: " + item.address

        detailsTypes.text = "Types:\n"
        for (t in item.type) {
            for (tt in modelT.typesList.value!!) {
                if (tt.id == t.typesId)
                    detailsTypes.text = detailsTypes.text.toString() + tt.name + "\n"
            }
        }
        //-----------------convert images and display them on screen--------------------
        imgArr = convertImageArray(item.images)

        if (imgArr.size >= 1) {
            detailsMainImage.setImageBitmap(imgArr[0].image)
        }else {
            detailsMainImage.setImageResource(R.drawable.ic_noimgicon2)
        }

        val viewManager: RecyclerView.LayoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        val viewAdapter: RecyclerView.Adapter<*> = TempImageRecyclerAdapter(imgArr, this@GoodsDetailFragment)
        activity!!.findViewById<RecyclerView>(R.id.detailsImageList).apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
        //-----------------------end of conversion and display--------------------------
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

    private fun editItem(infoId: Int){
        val fragment = GoodsEditFragment()
        val args = Bundle()
        args.putInt("goodsId", infoId)
        fragment.arguments = args
        activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragmentFrame, fragment).addToBackStack(null).commit()
    }

    private fun deleteItem(item: Goods){
        //-------------------------------Delete item on holding-------------------------------------
        val token = "Bearer " + activity!!.intent.getStringExtra("Token")
        modelG.removeUserGoods(item, token, activity!!)
        //-------------------------------End of item deletion---------------------------------------
        activity!!.supportFragmentManager.popBackStackImmediate()
    }

    override fun onRecyclerItemClick(position: Int) {
        val dialog = Dialog(activity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.popup_image_dialog)
        val img: ImageView = dialog.findViewById(R.id.imagePopupImageView)
        img.setImageBitmap(imgArr[position].image)
        dialog.findViewById<View>(R.id.imagePopupView).setOnClickListener{
            dialog.cancel()
        }
        dialog.show()
    }

}
