package com.example.tamanpempek.ui.adapter.user.history

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.tamanpempek.model.PaymentModel
import java.io.Serializable

class SectionPagerHistoryAdapterUser(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    var historyReviewed: List<PaymentModel> = emptyList()
    var historySent: List<PaymentModel> = emptyList()
    var historyFinished: List<PaymentModel> = emptyList()
    var historyCanceled: List<PaymentModel> = emptyList()
    var historyRejected: List<PaymentModel> = emptyList()
    override fun createFragment(position: Int): Fragment {
        val fragment = HistoryUserFragment()
        fragment.arguments = Bundle().apply {
            putInt(HistoryUserFragment.ARG_POSITION, position)
            putSerializable(HistoryUserFragment.ARG_CATEGORY1, historyReviewed as Serializable)
            putSerializable(HistoryUserFragment.ARG_CATEGORY2, historySent as Serializable)
            putSerializable(HistoryUserFragment.ARG_CATEGORY3, historyFinished as Serializable)
            putSerializable(HistoryUserFragment.ARG_CATEGORY4, historyCanceled as Serializable)
            putSerializable(HistoryUserFragment.ARG_CATEGORY5, historyRejected as Serializable)
        }
        return fragment
    }

    override fun getItemCount(): Int {
        return 5
    }
}