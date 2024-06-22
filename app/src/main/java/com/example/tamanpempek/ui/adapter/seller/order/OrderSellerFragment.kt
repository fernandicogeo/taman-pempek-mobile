package com.example.tamanpempek.ui.adapter.seller.order

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tamanpempek.R
import com.example.tamanpempek.databinding.FragmentOrderSellerBinding
import com.example.tamanpempek.model.CartModel
import com.example.tamanpempek.viewmodel.UserViewModel
import com.example.tamanpempek.viewmodel.factory.UserViewModelFactory

class OrderSellerFragment : Fragment() {
    private lateinit var adapter: OrderAdapterSeller
    private lateinit var recyclerView: RecyclerView
    private var _binding: FragmentOrderSellerBinding? = null
    private lateinit var userViewModel: UserViewModel
    private lateinit var userFactory: UserViewModelFactory
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderSellerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userFactory = UserViewModelFactory.getInstanceAuth(requireContext())
        userViewModel = ViewModelProvider(this, userFactory)[UserViewModel::class.java]

        orderWaiting = arguments?.getSerializable(ARG_CATEGORY2) as List<CartModel>
        orderSent = arguments?.getSerializable(ARG_CATEGORY3) as List<CartModel>
        orderFinished = arguments?.getSerializable(ARG_CATEGORY4) as List<CartModel>

        val orderWaitingList: List<CartModel> = orderWaiting.toList()
        val orderSentList: List<CartModel> = orderSent.toList()
        val orderFinishedList: List<CartModel> = orderFinished.toList()

        position = arguments!!.getInt(ARG_POSITION)

        when (position) {
            0 -> setOrder(orderWaitingList)
            1 -> setOrder(orderSentList)
            2 -> setOrder(orderFinishedList)
        }
    }

    private fun setOrder(datas: List<CartModel>) {
        val layoutManager = GridLayoutManager(context, 1)
        recyclerView = view!!.findViewById(R.id.rv_order)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        adapter = OrderAdapterSeller(datas, userViewModel, this)
        recyclerView.adapter = adapter
    }

    companion object {
        const val ARG_POSITION = "arg_position"
        const val ARG_CATEGORY2 = "arg_orderWaiting"
        const val ARG_CATEGORY3 = "arg_orderSent"
        const val ARG_CATEGORY4 = "arg_orderFinished"

        var position: Int = 0

        var orderWaiting: List<CartModel> = emptyList()
        var orderSent: List<CartModel> = emptyList()
        var orderFinished: List<CartModel> = emptyList()
    }
}