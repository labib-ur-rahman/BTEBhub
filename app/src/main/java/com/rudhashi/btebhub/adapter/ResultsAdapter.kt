package com.rudhashi.btebhub.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.rudhashi.btebhub.model.ResultItem
import com.rudhashi.btebhub.R

class ResultsAdapter(
    private var resultList: List<ResultItem>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<ResultsAdapter.ResultViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_results, parent, false)
        return ResultViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        val resultItem = resultList[position]
        holder.bind(resultItem, position)

        // Set OnClickListener to pass the roll argument
        holder.itemView.setOnClickListener {
            onItemClick(resultItem.roll.toString()) // Pass the roll as a string
        }
    }

    override fun getItemCount(): Int = resultList.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateResults(newResults: List<ResultItem>) {
        resultList = newResults
        notifyDataSetChanged()
    }

    class ResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewRoll: TextView = itemView.findViewById(R.id.textViewRoll)
        private val textViewResult: TextView = itemView.findViewById(R.id.textViewResult)
        private val textViewSerial: TextView = itemView.findViewById(R.id.textViewSerial)
        private val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)

        fun bind(resultItem: ResultItem, position: Int) {
            val result = resultItem.roll.toString()
            val serialNo = position + 1
            val serial = serialNo.toString()
            textViewSerial.text = serial
            textViewRoll.text = result

            val resultText = resultItem.result.gpa?.toString() ?: "Failed"
            textViewResult.text = resultText

            val referred = resultItem.result.reffereds?.size
            if (resultText.contains("Failed")) {
                tvStatus.text = "$referred referred"
                tvStatus.setTextColor(ContextCompat.getColor(tvStatus.context, R.color.grade_red))
            } else {
                "Passed".also { tvStatus.text = it }
                tvStatus.setTextColor(ContextCompat.getColor(tvStatus.context, R.color.google__Green))
            }
        }
    }
}