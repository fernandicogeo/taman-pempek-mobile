package com.example.tamanpempek.ui.adapter.admin.payment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.tamanpempek.model.PaymentModel
import java.io.Serializable

class SectionPagerAdapterAdminPayment(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    var paymentReviewed: List<PaymentModel> = emptyList()
    var paymentWaiting: List<PaymentModel> = emptyList()
    var paymentSent: List<PaymentModel> = emptyList()
    var paymentFinished: List<PaymentModel> = emptyList()
    var paymentCanceled: List<PaymentModel> = emptyList()
    var paymentRejected: List<PaymentModel> = emptyList()
    override fun createFragment(position: Int): Fragment {
        val fragment = PaymentFragmentAdmin()
        fragment.arguments = Bundle().apply {
            putInt(PaymentFragmentAdmin.ARG_POSITION, position)
            putSerializable(PaymentFragmentAdmin.ARG_CATEGORY1, paymentReviewed as Serializable)
            putSerializable(PaymentFragmentAdmin.ARG_CATEGORY2, paymentWaiting as Serializable)
            putSerializable(PaymentFragmentAdmin.ARG_CATEGORY3, paymentSent as Serializable)
            putSerializable(PaymentFragmentAdmin.ARG_CATEGORY4, paymentFinished as Serializable)
            putSerializable(PaymentFragmentAdmin.ARG_CATEGORY5, paymentCanceled as Serializable)
            putSerializable(PaymentFragmentAdmin.ARG_CATEGORY6, paymentRejected as Serializable)
        }
        return fragment
    }

    override fun getItemCount(): Int {
        return 6
    }
}