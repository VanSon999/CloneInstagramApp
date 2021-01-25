package vanson.dev.instagramclone.controllers

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_search.*
import vanson.dev.instagramclone.R
import vanson.dev.instagramclone.adapters.ImagesAdapter
import vanson.dev.instagramclone.controllers.common.BaseActivity
import vanson.dev.instagramclone.controllers.common.setupAuthGuard
import vanson.dev.instagramclone.views.setupBottomNavigation

class SearchActivity : BaseActivity(), TextWatcher {
    private lateinit var mViewModel: SearchViewModel
    private lateinit var mAdapter: ImagesAdapter
    private var isSearchEntered = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        setupBottomNavigation(1)
        setupAuthGuard {
            mAdapter = ImagesAdapter()
            search_result_recycler.layoutManager = GridLayoutManager(this, 3)
            search_result_recycler.adapter = mAdapter

            mViewModel = initViewModel()

            mViewModel.posts.observe(this, Observer { list ->
                list?.let{ posts ->
                    mAdapter.updateImages(posts.map{it.image})
                }
            })
            search_input.addTextChangedListener(this)
            mViewModel.setSearchText("")
        }
    }

    companion object {
        const val tag = "SearchActivity"
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable?) {
        if(!isSearchEntered){
            isSearchEntered = true
            @Suppress("DEPRECATION")
            Handler().postDelayed({
                isSearchEntered = false
                mViewModel.setSearchText(search_input.text.toString())
            }, 500)
        }
    }
}
