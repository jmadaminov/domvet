package com.sablab.domvetdoctor.ui.main.finance.summary

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sablab.domvetdoctor.util.Constants
import com.sablab.domvetdoctor.util.ErrorWrapper
import com.sablab.domvetdoctor.util.ResultWrapper
import com.sablab.domvetdoctor.util.exhaustive
import com.sablab.domvetdoctor.domain.model.PassengerPost
import com.sablab.domvetdoctor.R
import com.sablab.domvetdoctor.ui.interfaces.IOnPostActionListener
import com.sablab.domvetdoctor.ui.main.MainViewModel
import com.sablab.domvetdoctor.ui.viewgroups.ItemDocCall
import com.sablab.domvetdoctor.viewobjects.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_active_calls.*
import splitties.experimental.ExperimentalSplittiesApi
import splitties.fragments.start
import javax.inject.Inject

class SummaryFragment @Inject constructor(private val viewModelFactory: ViewModelProvider.Factory) :
    Fragment(R.layout.fragment_active_calls) {


    private val adapter = GroupAdapter<GroupieViewHolder>()
    val viewmodel: SummaryViewModel by viewModels {
        viewModelFactory
    }


    private val activityViewModel: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    @ExperimentalSplittiesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        setupRecyclerView()
        setupListeners()
        subscribeObservers()
        viewmodel.getActivePosts()
    }

    override fun onStart() {
        super.onStart()
//        if (viewmodel.activePostsResponse.value != ResultWrapper.InProgress) {
//            swipeRefreshLayout.isRefreshing = true
//        } else {
//            swipeRefreshLayout.isRefreshing =
//                viewmodel.activePostsResponse.value == ResultWrapper.InProgress
//        }
    }


    override fun onResume() {
        super.onResume()


    }

    private fun setupRecyclerView() {
        activeOrdersList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        activeOrdersList.setHasFixedSize(true)
        activeOrdersList.adapter = adapter

    }

    @ExperimentalSplittiesApi
    private fun setupListeners() {
        swipeRefreshLayout.setOnRefreshListener {
            noActiveOrdersTxt.visibility = View.GONE
            viewmodel.getActivePosts()
        }
    }

    @ExperimentalSplittiesApi
    private fun subscribeObservers() {

//        activityViewModel.deletedOrderItem.observe(viewLifecycleOwner, Observer {
//            val order = it ?: return@Observer
//            adapter.remove(adapter.getItem(order.adapterPosition!!))
//            adapter.notifyItemRemoved(order.adapterPosition!!)
//            if (adapter.itemCount == 0) noActiveOrdersTxt.visibility = View.VISIBLE
//
//        })

//        activityViewModel.updatedOrderItem.observe(viewLifecycleOwner, Observer {
//            val order = it ?: return@Observer
//            val target = adapter.getItem(order.adapterPosition!!)
//            (target as ActiveOrderItem).order = order
//            adapter.notifyItemChanged(order.adapterPosition!!)
//        })

        viewmodel.activePostsResponse.observe(viewLifecycleOwner, Observer {
            val response = it ?: return@Observer
            when (response) {
                is ErrorWrapper.ResponseError -> {
                    swipeRefreshLayout.isRefreshing = false
//                    Snackbar.make(rl_parent,
//                                  response.message!!,
//                                  Snackbar.LENGTH_SHORT).show()

                    noActiveOrdersTxt.visibility = View.VISIBLE
                    noActiveOrdersTxt.text = response.message
                    activeOrdersList.visibility = View.INVISIBLE

                }
                is ErrorWrapper.SystemError -> {
//                    Snackbar.make(rl_parent,
//                                  response.err.localizedMessage.toString(),
//                                  Snackbar.LENGTH_SHORT).show()
                    noActiveOrdersTxt.visibility = View.VISIBLE
                    noActiveOrdersTxt.text = response.err.localizedMessage
                    activeOrdersList.visibility = View.INVISIBLE
                    swipeRefreshLayout.isRefreshing = false

                }
                is ResultWrapper.Success -> {
                    noActiveOrdersTxt.visibility = View.INVISIBLE
                    activeOrdersList.visibility = View.VISIBLE
                    swipeRefreshLayout.isRefreshing = false
                    loadData(response.value)
                }
                ResultWrapper.InProgress -> {
                    noActiveOrdersTxt.visibility = View.INVISIBLE
                    activeOrdersList.visibility = View.INVISIBLE
                    swipeRefreshLayout.isRefreshing = true
                }
            }.exhaustive
        })

        viewmodel.deletePostReponse.observe(viewLifecycleOwner, Observer {
            val response = it ?: return@Observer
            when (response) {
                is ErrorWrapper.ResponseError -> {
//                    Snackbar.make(swipeRefreshLayout,
//                                  response.message!!,
//                                  Snackbar.LENGTH_SHORT).show()

                }
                is ErrorWrapper.SystemError -> {
//                    Snackbar.make(swipeRefreshLayout,
//                                  response.err.localizedMessage.toString(),
//                                  Snackbar.LENGTH_SHORT).show()
                }
                is ResultWrapper.Success -> {
                    adapter.remove(adapter.getItem(response.value))
                    adapter.notifyItemRemoved(response.value)
                }
                ResultWrapper.InProgress -> {
                }
            }.exhaustive
        })

        viewmodel.finishPostResponse.observe(viewLifecycleOwner, Observer {
            val response = it ?: return@Observer
            when (response) {
                is ErrorWrapper.ResponseError -> {
//                    Snackbar.make(swipeRefreshLayout,
//                                  response.message!!,
//                                  Snackbar.LENGTH_SHORT).show()

                }
                is ErrorWrapper.SystemError -> {
//                    Snackbar.make(swipeRefreshLayout,
//                                  response.err.localizedMessage.toString(),
//                                  Snackbar.LENGTH_SHORT).show()
                }
                is ResultWrapper.Success -> {
                    adapter.remove(adapter.getItem(response.value))
                    adapter.notifyItemRemoved(response.value)
                }
                ResultWrapper.InProgress -> {
                }
            }.exhaustive
        })


    }


    var dialog: AlertDialog? = null

//    @ExperimentalSplittiesApi
//    val onOrderActionListener = object : IOnOrderActionListener {
//        override fun onModifyClick(order: OrderData) {
//            val dialog = EditOrderBottomSheetDialog(viewModelFactory)
//            val args = Bundle()
//            args.putParcelable(Constants.ARG_ORDER, order)
//            dialog.arguments = args
//            dialog.showsDialog
//            dialog.show(childFragmentManager, "Edit_Order_BSD")
//        }
//
//        override fun onEditClick(order: OrderData) {
//        }
//
//    }

    @ExperimentalSplittiesApi
    private fun loadData(orders: List<PassengerPost>) {
        adapter.clear()
        if (orders.isEmpty()) {
            noActiveOrdersTxt.visibility = View.VISIBLE
            noActiveOrdersTxt.text = getString(R.string.no_active_orders)
        } else noActiveOrdersTxt.visibility = View.GONE

        orders.forEach {
            adapter.add(ItemDocCall(it, onOrderActionListener))
        }
        adapter.notifyDataSetChanged()

    }


    @ExperimentalSplittiesApi
    private val onOrderActionListener = object : IOnPostActionListener {
        override fun onEditClick(post: PassengerPost) {

            val from = PlaceViewObj(post.from.districtId,
                                    post.from.regionId,
                                    post.from.lat,
                                    post.from.lon,
                                    post.from.regionName,
                                    post.from.name)

            val to = PlaceViewObj(post.to.districtId,
                                  post.to.regionId,
                                  post.to.lat,
                                  post.to.lon,
                                  post.from.regionName,
                                  post.from.name)



        }

        override fun onCancelClick(position: Int, post: PassengerPost, parentView: View) {
            viewmodel.deletePost(post.id.toString(), position)

        }

        override fun onDoneClick(position: Int, post: PassengerPost, parentView: View) {
            viewmodel.finishPost(post.id.toString(), position)
        }

    }


}