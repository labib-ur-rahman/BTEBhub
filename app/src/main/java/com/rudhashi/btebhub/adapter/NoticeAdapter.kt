package com.rudhashi.btebhub.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.rudhashi.btebhub.R
import com.rudhashi.btebhub.manager.DialogManager.showConfirmDownloadDialog
import com.rudhashi.btebhub.model.Notice
import com.rudhashi.btebhub.utils.SmartData.fixDownloadLink

class NoticeAdapter(
    private val noticeList: List<Notice>,
    private val onDownloadClick: (String) -> Unit // Callback for handling download
) : RecyclerView.Adapter<NoticeAdapter.NoticeViewHolder>() {

    inner class NoticeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        val tvMainTitle: TextView = itemView.findViewById(R.id.tvMainTitle)
        val ivDownload: ConstraintLayout = itemView.findViewById(R.id.ivDownload)

        fun bind(notice: Notice) {
            tvDate.text = notice.date
            tvMainTitle.text = notice.mainTitle

            ivDownload.setOnClickListener {
                val context = itemView.context
                val activity = context as? FragmentActivity
                activity?.let { it1 -> showConfirmDownloadDialog(context, it1, fixDownloadLink(notice.link)) }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_notice, parent, false)
        return NoticeViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoticeViewHolder, position: Int) {
        holder.bind(noticeList[position])
    }

    override fun getItemCount(): Int {
        return noticeList.size
    }
}