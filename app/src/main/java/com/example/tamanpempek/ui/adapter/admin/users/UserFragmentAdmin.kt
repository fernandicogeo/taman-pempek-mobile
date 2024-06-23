package com.example.tamanpempek.ui.adapter.admin.users

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tamanpempek.R
import com.example.tamanpempek.databinding.FragmentUserAdminBinding
import com.example.tamanpempek.model.UserModel

class UserFragmentAdmin : Fragment() {
    private lateinit var adapter: UserAdapterAdmin
    private lateinit var recyclerView: RecyclerView
    private var _binding: FragmentUserAdminBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserAdminBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        category1 = arguments?.getSerializable(ARG_CATEGORY1) as List<UserModel>
        category2 = arguments?.getSerializable(ARG_CATEGORY2) as List<UserModel>

        val category1List: List<UserModel> = category1.toList()
        val category2List: List<UserModel> = category2.toList()

        position = arguments!!.getInt(ARG_POSITION)

        when (position) {
            0 -> setCategory(category1List)
            1 -> setCategory(category2List)
        }
    }

    private fun setCategory(datas: List<UserModel>) {
        val layoutManager = GridLayoutManager(context, 1)
        recyclerView = view!!.findViewById(R.id.rv_user)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        adapter = UserAdapterAdmin(datas)
        recyclerView.adapter = adapter
    }

    companion object {
        const val ARG_POSITION = "arg_position"
        const val ARG_CATEGORY1 = "arg_category1"
        const val ARG_CATEGORY2 = "arg_category2"
        var position: Int = 0
        var category1: List<UserModel> = emptyList()
        var category2: List<UserModel> = emptyList()
    }
}