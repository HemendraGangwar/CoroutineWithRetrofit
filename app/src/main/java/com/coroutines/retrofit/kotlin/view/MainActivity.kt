package com.coroutines.retrofit.kotlin.view

import android.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.coroutines.retrofit.kotlin.R
import com.secure.omega.app.view.customViews.customProgress.CustomProgressDialog
import coml.go.tawseel.webApi.ApiClient
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import android.support.v7.widget.LinearLayoutManager
import com.coroutines.retrofit.kotlin.model.Posts
import com.coroutines.retrofit.kotlin.view.adapter.PostsAdapter


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /**
         * tap on button and call api to get response
         */
        setOnClickEvents()

        /**
         * write here the code in this method to call api
         */
        callApiWithCoroutines()

    }

    /**
     * tap on button and call api to get response
     */
    private fun setOnClickEvents() {


    }

    /**
     * write here the code in this method to call api
     */
    private fun callApiWithCoroutines() {

        GlobalScope.launch(Dispatchers.Main) {

            // show progress
            var customProgressDialog: CustomProgressDialog = CustomProgressDialog.show(this@MainActivity, true)

            try {
                val request = ApiClient.client
                    .getRetrofitService()
                    .getPosts()
                val response = request.await()

                // hide progress
                customProgressDialog.dismiss()

                // update ui according to response
                if (response.isSuccessful) {
                    /**
                     * set response data into list
                     */
                    updateRV(response.body()!!)
                } else {
                    showPopUp(response.errorBody().toString())
                }
            } catch (exception: Exception) {
                showPopUp(exception.toString())
            }
        }

    }

    /**
     * set response data into list
     */
    private fun updateRV(postList: List<Posts>?) {

        // set a LinearLayoutManager with default horizontal orientation and false value for reverseLayout to show the items from start to end
        val linearLayoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager // set LayoutManager to RecyclerView
        var postsAdapter  = PostsAdapter(postList!!)
        recyclerView.adapter = postsAdapter

    }

    /**
     * show response pop up
     */
    private fun showPopUp(responseStr: String) {

        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setMessage(responseStr)
        builder.setNeutralButton("Ok") { dialog, which ->
            dialog.dismiss()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()

    }
}
