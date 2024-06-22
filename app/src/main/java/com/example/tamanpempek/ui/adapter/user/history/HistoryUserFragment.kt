package com.example.tamanpempek.ui.adapter.user.history

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tamanpempek.R
import com.example.tamanpempek.databinding.FragmentHistoryUserBinding
import com.example.tamanpempek.model.PaymentModel

class HistoryUserFragment : Fragment() {
    private lateinit var adapter: HistoryAdapterUser
    private lateinit var recyclerView: RecyclerView
    private var _binding: FragmentHistoryUserBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        historyReviewed = arguments?.getSerializable(ARG_CATEGORY1) as List<PaymentModel>
        historyWaiting = arguments?.getSerializable(ARG_CATEGORY2) as List<PaymentModel>
        historySent = arguments?.getSerializable(ARG_CATEGORY3) as List<PaymentModel>
        historyFinished = arguments?.getSerializable(ARG_CATEGORY4) as List<PaymentModel>
        historyCanceled = arguments?.getSerializable(ARG_CATEGORY5) as List<PaymentModel>
        historyRejected = arguments?.getSerializable(ARG_CATEGORY6) as List<PaymentModel>

        val historyReviewedList: List<PaymentModel> = historyReviewed.toList()
        val historyWaitingList: List<PaymentModel> = historyWaiting.toList()
        val historySentList: List<PaymentModel> = historySent.toList()
        val historyFinishedList: List<PaymentModel> = historyFinished.toList()
        val historyCanceledList: List<PaymentModel> = historyCanceled.toList()
        val historyRejectedList: List<PaymentModel> = historyRejected.toList()

        position = arguments!!.getInt(ARG_POSITION)

        when (position) {
            0 -> setHistory(historyReviewedList)
            1 -> setHistory(historyWaitingList)
            2 -> setHistory(historySentList)
            3 -> setHistory(historyFinishedList)
            4 -> setHistory(historyCanceledList)
            5 -> setHistory(historyRejectedList)
        }
    }

    private fun setHistory(datas: List<PaymentModel>) {
        val layoutManager = GridLayoutManager(context, 1)
        recyclerView = view!!.findViewById(R.id.rv_history)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        adapter = HistoryAdapterUser(datas)
        recyclerView.adapter = adapter
    }

    companion object {
        const val ARG_POSITION = "arg_position"
        const val ARG_CATEGORY1 = "arg_historyReviewed"
        const val ARG_CATEGORY2 = "arg_historyWaiting"
        const val ARG_CATEGORY3 = "arg_historySent"
        const val ARG_CATEGORY4 = "arg_historyFinished"
        const val ARG_CATEGORY5 = "arg_historyCanceled"
        const val ARG_CATEGORY6 = "arg_historyRejected"

        var position: Int = 0

        var historyReviewed: List<PaymentModel> = emptyList()
        var historyWaiting: List<PaymentModel> = emptyList()
        var historySent: List<PaymentModel> = emptyList()
        var historyFinished: List<PaymentModel> = emptyList()
        var historyCanceled: List<PaymentModel> = emptyList()
        var historyRejected: List<PaymentModel> = emptyList()
    }
}