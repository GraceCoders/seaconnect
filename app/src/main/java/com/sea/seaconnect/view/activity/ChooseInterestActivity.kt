package com.sea.seaconnect.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.sea.seaconnect.R
import com.sea.seaconnect.controller.Utills.AppPreferences
import com.sea.seaconnect.controller.utills.Constants
import com.sea.seaconnect.model.ResponseModel.Intereaction_list.Data
import com.sea.seaconnect.model.ResponseModel.Intereaction_list.Intereaction_list_response
import com.sea.seaconnect.view.adapter.Interest_Adapter
import com.sea.seaconnect.viewModel.InterestViewModel
import kotlinx.android.synthetic.main.activity_choose_interest.*


class ChooseInterestActivity : AppCompatActivity(), View.OnClickListener {
    var auth_token = ""
    private var viewModel: InterestViewModel? = null
    private var int_list: ArrayList<Data> = ArrayList()
    val currentSelectedItemsid: ArrayList<String> = ArrayList()
    val currentSelectedItemsName: ArrayList<String> = ArrayList()
    var getName: ArrayList<String> = ArrayList()
    var statusCode: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_interest)

        initView()
    }

    private fun initView() {
        auth_token = AppPreferences.init(this).getString(Constants.TOKEN)
        tvWelcomeText.text= getString(R.string.welcome)+AppPreferences.init(this).getString(Constants.F_NAME)
        viewModel = ViewModelProviders.of(this).get(InterestViewModel::class.java)
        ll_choose_intereset.setOnClickListener(this)
        Interest_list_api()

    }


    //..........................................interest list api...........................................

    private fun Interest_list_api() {
        clLoader_interest.visibility = View.VISIBLE
        viewModel?.getInterest(auth_token)?.observe(
            this,
            object : Observer<Intereaction_list_response?> {
                override fun onChanged(@Nullable interestlist: Intereaction_list_response?) {
                    clLoader_interest.visibility = View.GONE
                    int_list = interestlist?.data as ArrayList<Data>
                    if (int_list.size > 0) {


                        rv_interest?.layoutManager = LinearLayoutManager(
                            this@ChooseInterestActivity,
                            LinearLayoutManager.VERTICAL,
                            false
                        )
                        rv_interest?.adapter =
                            Interest_Adapter(this@ChooseInterestActivity, int_list)
                        (rv_interest?.adapter as Interest_Adapter).onItemClick = { pos, view ->

                            var id = int_list.get(pos).id
                            if (currentSelectedItemsid.contains(id)) {
                                currentSelectedItemsid.remove(int_list.get(pos).id)
                                currentSelectedItemsName.remove(int_list.get(pos).name)

                            } else {
                                currentSelectedItemsid.add(int_list.get(pos).id)
                                currentSelectedItemsName.add(int_list.get(pos).name)
                            }

                        }
                        rv_interest?.isNestedScrollingEnabled = false

                    } else {

                    }


                }
            })


    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ll_choose_intereset -> {

                saveDataId()
                saveName()
                val i = Intent(this, MainActivity::class.java)
                startActivity(i)
                finishAffinity()
                getdata()
            }
        }
    }

    private fun getdata() {

    }

    //.............................interest name in array list.............................
    private fun saveName() {
        val sharedPreferences =
            getSharedPreferences("shared preferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(currentSelectedItemsName)
        editor.putString("nameList", json)
        editor.apply()
    }

    //..............................interest id in array list...........................
    private fun saveDataId() {
        val sharedPreferences =
            getSharedPreferences("shared preferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(currentSelectedItemsid)
        editor.putString("idList", json)
        editor.apply()

    }
}
