package com.unifae.gallery.ui

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.unifae.gallery.data.api.dto.PagedPhotos
import com.unifae.gallery.data.repository.NetworkStatus
import com.unifae.gallery.databinding.ActivityGalleryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GalleryActivity : AppCompatActivity() {
    private lateinit var layout: ActivityGalleryBinding

    private var lastViewableItemPosition: Int = 0
    private var previousQuery: String = ""
    private var nextPageUrl: String = ""

    private val viewModel: GalleryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layout = ActivityGalleryBinding.inflate(layoutInflater)
        layout.galleryRvList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = GalleryRecyclerViewAdapter(context)
        }
        setContentView(layout.root)

        // network status observer
        viewModel.networkStatus.observe(this, { onNetworkStatusChanged(it) })

        //photo list observer
        viewModel.photoList.observe(this, { onPhotoListChanged(it) })

        // button event handler
        layout.galleryBtnSearch.setOnClickListener { onSearchButtonClicked(it) }

        // scroll event handler
        layout.galleryRvList.addOnScrollListener(infiniteScrollListener)
    }

    private fun onSearchButtonClicked(view: View) {
        hideKeyboard(view)
        if (
            layout.galleryEtSearch.text.toString() != previousQuery
            && layout.galleryEtSearch.text.isNotEmpty()
        ) {
            lastViewableItemPosition = 0
            previousQuery = layout.galleryEtSearch.text.toString()
            (layout.galleryRvList.adapter as GalleryRecyclerViewAdapter).clearList()
            viewModel.getPhotosByQuery(layout.galleryEtSearch.text.toString())
        }
    }

    private fun onPhotoListChanged(pagedPhotos: PagedPhotos) {
        this.nextPageUrl = pagedPhotos.nextPage
        (layout.galleryRvList.adapter as GalleryRecyclerViewAdapter).setList(pagedPhotos.photos)
    }

    private fun onNetworkStatusChanged(networkStatus: NetworkStatus) {
        when (networkStatus) {
            NetworkStatus.LOADING -> {
                layout.galleryWrapperLoading.visibility = View.VISIBLE
                layout.galleryBtnSearch.isClickable = false
            }
            NetworkStatus.LOADING_COMPLETE -> {
                layout.galleryWrapperLoading.visibility = View.GONE
                layout.galleryRvList.scrollToPosition(lastViewableItemPosition)
                layout.galleryBtnSearch.isClickable = true
            }
            NetworkStatus.ERROR -> {
                Toast.makeText(this, "Network error; please retry.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private val infiniteScrollListener = object: RecyclerView.OnScrollListener() {
        var previousScrollState = 0
        var scrollCnt = 0
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if ((previousScrollState == 1 || previousScrollState == 2) && newState == 0) {
                lastViewableItemPosition = (recyclerView.layoutManager as LinearLayoutManager?)!!
                    .findLastCompletelyVisibleItemPosition() // position of last viewable item
                val sizeOfItems = recyclerView.adapter!!.itemCount - 1
                if (lastViewableItemPosition == sizeOfItems && nextPageUrl != "") { // is the scroll at the end?
                    ++ scrollCnt
                    if (scrollCnt >= 2) {
                        viewModel.getNextPage(nextPageUrl)
                        scrollCnt = 0
                    }
                }
            }
            previousScrollState = newState
        }
    }

    fun hideKeyboard(view: View) {
        val imm: InputMethodManager? = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }
}