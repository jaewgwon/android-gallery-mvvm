package com.unifae.gallery.ui

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.unifae.gallery.data.api.PexelsClient
import com.unifae.gallery.data.entity.Photo
import com.unifae.gallery.data.repository.NetworkStatus
import com.unifae.gallery.databinding.ActivityGalleryBinding

class GalleryActivity : AppCompatActivity() {
    private lateinit var layout: ActivityGalleryBinding
    private var currentStatus: NetworkStatus = NetworkStatus.LOADING_COMPLETE
    private var lastViewableItemPosition: Int = 0
    private val cachedPhotos: MutableList<Photo> = ArrayList()

    private val viewModel: GalleryViewModel by viewModels {
        GalleryViewModelFactory(PexelsClient.getClient())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layout = ActivityGalleryBinding.inflate(layoutInflater)
        layout.galleryRvList.layoutManager = LinearLayoutManager(this)
        setContentView(layout.root)

        // network status observer
        viewModel.getDataSourceNetworkStatus().observe(
            this,
            { onNetworkStatusChanged(it) }
        )

        //photo list observer
        viewModel.getDataSourcePhotos().observe(
            this,
            {
                cachedPhotos.addAll(it.photos)
                viewModel.setNextPage(it.nextPage)
                layout.galleryRvList.adapter = GalleryRecyclerViewAdapter(
                    this, cachedPhotos)
            }
        )

        // button event handler
        layout.galleryBtnSearch.setOnClickListener {
            if(layout.galleryEtSearch.text.isNotEmpty()) onSearchButtonClicked(it)
        }

        // scroll event handler
        layout.galleryRvList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            var previousScrollState = 0
            var scrollCnt = 0
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if ((previousScrollState == 1 || previousScrollState == 2) && newState == 0) {
                    lastViewableItemPosition = (recyclerView.layoutManager as LinearLayoutManager?)!!
                        .findLastCompletelyVisibleItemPosition() // position of last viewable item
                    val sizeOfItems = recyclerView.adapter!!.itemCount - 1
                    if (lastViewableItemPosition == sizeOfItems && viewModel.isNextPage()) { // is the scroll at the end?
                        ++ scrollCnt
                        if (scrollCnt >= 2) {
                            viewModel.fetchNextPage()
                            scrollCnt = 0
                        }
                    }
                }
                previousScrollState = newState
            }
        })
    }

    private fun onSearchButtonClicked(view: View) {
        hideKeyboard(view)
        if (currentStatus != NetworkStatus.LOADING) {
            cachedPhotos.clear()
            lastViewableItemPosition = 0
            viewModel.searchPhotos(layout.galleryEtSearch.text.toString())
        }
    }

    private fun onNetworkStatusChanged(networkStatus: NetworkStatus) {
        currentStatus = networkStatus
        if (networkStatus == NetworkStatus.LOADING) layout.galleryWrapperLoading.visibility = View.VISIBLE
        else if (networkStatus == NetworkStatus.LOADING_COMPLETE) {
            layout.galleryWrapperLoading.visibility = View.GONE
            layout.galleryRvList.scrollToPosition(lastViewableItemPosition)
        }
    }

    fun hideKeyboard(view: View) {
        val imm: InputMethodManager? = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }
}