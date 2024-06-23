package com.example.tamanpempek.ui.adapter.admin.product

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tamanpempek.R
import com.example.tamanpempek.databinding.FragmentProductBinding
import com.example.tamanpempek.model.ProductModel
import com.example.tamanpempek.ui.adapter.user.product.ProductAdapterUser

class ProductFragmentAdmin : Fragment() {
    private lateinit var adapter: ProductAdapterAdmin
    private lateinit var recyclerView: RecyclerView
    private var _binding: FragmentProductBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        category1 = arguments?.getSerializable(ARG_CATEGORY1) as List<ProductModel>
        category2 = arguments?.getSerializable(ARG_CATEGORY2) as List<ProductModel>
        category3 = arguments?.getSerializable(ARG_CATEGORY3) as List<ProductModel>

        val category1List: List<ProductModel> = category1.toList()
        val category2List: List<ProductModel> = category2.toList()
        val category3List: List<ProductModel> = category3.toList()

        position = arguments!!.getInt(ARG_POSITION)

        when (position) {
            0 -> setCategory(category1List)
            1 -> setCategory(category2List)
            2 -> setCategory(category3List)
        }
    }

    private fun setCategory(datas: List<ProductModel>) {
        val layoutManager = GridLayoutManager(context, 2)
        recyclerView = view!!.findViewById(R.id.rv_product)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        adapter = ProductAdapterAdmin(datas)
        recyclerView.adapter = adapter
    }

    companion object {
        const val ARG_POSITION = "arg_position"
        const val ARG_CATEGORY1 = "arg_category1"
        const val ARG_CATEGORY2 = "arg_category2"
        const val ARG_CATEGORY3 = "arg_category3"
        var position: Int = 0
        var category1: List<ProductModel> = emptyList()
        var category2: List<ProductModel> = emptyList()
        var category3: List<ProductModel> = emptyList()
    }
}