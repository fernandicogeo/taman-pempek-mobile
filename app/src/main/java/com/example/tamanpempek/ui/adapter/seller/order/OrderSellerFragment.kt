package com.example.tamanpempek.ui.adapter.seller.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tamanpempek.databinding.FragmentOrderSellerBinding
import com.example.tamanpempek.model.CartModel
import com.example.tamanpempek.viewmodel.UserViewModel
import com.example.tamanpempek.viewmodel.factory.UserViewModelFactory

class OrderSellerFragment : Fragment() {

    private var _binding: FragmentOrderSellerBinding? = null
    private val binding get() = _binding!!
    private lateinit var userViewModel: UserViewModel
    private lateinit var userFactory: UserViewModelFactory
    private lateinit var adapter: OrderAdapterSeller
    private lateinit var recyclerView: RecyclerView

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
        userViewModel = ViewModelProvider(this, userFactory).get(UserViewModel::class.java)

        val orderWaiting = arguments?.getSerializable(ARG_CATEGORY2) as? List<CartModel> ?: emptyList()
        val orderSent = arguments?.getSerializable(ARG_CATEGORY3) as? List<CartModel> ?: emptyList()
        val orderFinished = arguments?.getSerializable(ARG_CATEGORY4) as? List<CartModel> ?: emptyList()

        val position = arguments?.getInt(ARG_POSITION) ?: 0

        setOrder(position, orderWaiting, orderSent, orderFinished)
    }

    private fun setOrder(position: Int, orderWaiting: List<CartModel>, orderSent: List<CartModel>, orderFinished: List<CartModel>) {
        val layoutManager = GridLayoutManager(requireContext(), 1)
        recyclerView = binding.rvOrder
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)

        val data = when (position) {
            0 -> orderWaiting
            1 -> orderSent
            2 -> orderFinished
            else -> emptyList()
        }

        adapter = OrderAdapterSeller(data, userViewModel, viewLifecycleOwner)
        recyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ARG_POSITION = "arg_position"
        const val ARG_CATEGORY2 = "arg_orderWaiting"
        const val ARG_CATEGORY3 = "arg_orderSent"
        const val ARG_CATEGORY4 = "arg_orderFinished"
    }
}
