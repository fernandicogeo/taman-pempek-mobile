package com.example.tamanpempek.ui.adapter.admin.payment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tamanpempek.R
import com.example.tamanpempek.databinding.FragmentPaymentAdminBinding
import com.example.tamanpempek.model.PaymentModel

class PaymentFragmentAdmin : Fragment() {
    private lateinit var adapter: PaymentAdapterAdmin
    private lateinit var recyclerView: RecyclerView
    private var _binding: FragmentPaymentAdminBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentAdminBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        paymentReviewed = arguments?.getSerializable(ARG_CATEGORY1) as List<PaymentModel>
        paymentWaiting = arguments?.getSerializable(ARG_CATEGORY2) as List<PaymentModel>
        paymentSent = arguments?.getSerializable(ARG_CATEGORY3) as List<PaymentModel>
        paymentFinished = arguments?.getSerializable(ARG_CATEGORY4) as List<PaymentModel>
        paymentCanceled = arguments?.getSerializable(ARG_CATEGORY5) as List<PaymentModel>
        paymentRejected = arguments?.getSerializable(ARG_CATEGORY6) as List<PaymentModel>

        val paymentReviewedList: List<PaymentModel> = paymentReviewed.toList()
        val paymentWaitingList: List<PaymentModel> = paymentWaiting.toList()
        val paymentSentList: List<PaymentModel> = paymentSent.toList()
        val paymentFinishedList: List<PaymentModel> = paymentFinished.toList()
        val paymentCanceledList: List<PaymentModel> = paymentCanceled.toList()
        val paymentRejectedList: List<PaymentModel> = paymentRejected.toList()

        position = arguments!!.getInt(ARG_POSITION)

        when (position) {
            0 -> setPayment(paymentReviewedList)
            1 -> setPayment(paymentWaitingList)
            2 -> setPayment(paymentSentList)
            3 -> setPayment(paymentFinishedList)
            4 -> setPayment(paymentCanceledList)
            5 -> setPayment(paymentRejectedList)
        }
    }

    private fun setPayment(datas: List<PaymentModel>) {
        val layoutManager = GridLayoutManager(context, 1)
        recyclerView = view!!.findViewById(R.id.rv_payment)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        adapter = PaymentAdapterAdmin(datas)
        recyclerView.adapter = adapter
    }

    companion object {
        const val ARG_POSITION = "arg_position"
        const val ARG_CATEGORY1 = "arg_paymentReviewed"
        const val ARG_CATEGORY2 = "arg_paymentWaiting"
        const val ARG_CATEGORY3 = "arg_paymentSent"
        const val ARG_CATEGORY4 = "arg_paymentFinished"
        const val ARG_CATEGORY5 = "arg_paymentCanceled"
        const val ARG_CATEGORY6 = "arg_paymentRejected"

        var position: Int = 0

        var paymentReviewed: List<PaymentModel> = emptyList()
        var paymentWaiting: List<PaymentModel> = emptyList()
        var paymentSent: List<PaymentModel> = emptyList()
        var paymentFinished: List<PaymentModel> = emptyList()
        var paymentCanceled: List<PaymentModel> = emptyList()
        var paymentRejected: List<PaymentModel> = emptyList()
    }
}