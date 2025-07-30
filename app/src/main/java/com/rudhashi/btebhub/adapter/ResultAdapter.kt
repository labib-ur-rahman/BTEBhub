package com.rudhashi.btebhub.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.rudhashi.btebhub.model.Result
import com.rudhashi.btebhub.utils.SmartData.getDividedSemNo
import com.rudhashi.btebhub.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ResultAdapter : RecyclerView.Adapter<ResultAdapter.ResultViewHolder>() {

    private val results = mutableListOf<Result>()

    fun submitList(newResults: List<Result>) {
        results.clear()
        results.addAll(newResults)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_result, parent, false)
        return ResultViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        holder.bind(results[position])
    }

    override fun getItemCount() = results.size

    class ResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val semNo: TextView = itemView.findViewById(R.id.semNo)
        private val th: TextView = itemView.findViewById(R.id.th)
        private val tvLaterGrade: TextView = itemView.findViewById(R.id.tvLaterGrade)
        private val infoView: LinearLayout = itemView.findViewById(R.id.infoView)
        private val gpaOrFailedText: TextView = itemView.findViewById(R.id.gpaOrFailedText)
        private val failedSubjects: TextView = itemView.findViewById(R.id.failedSubjects)
        private val statusText: TextView = itemView.findViewById(R.id.statusText)
        private val relativeDateText: TextView = itemView.findViewById(R.id.relativeDateText)
        private val medal: ImageView = itemView.findViewById(R.id.medal)

        fun bind(result: Result) {

            // Display Semester and Published Date --------------------------------------------------------
            val semester = result.semester
            semNo.text = getDividedSemNo(semester, true)
            th.text = getDividedSemNo(semester, false)
            relativeDateText.text = formatDate(result.Date)

            // Handle GPA or Failed Subjects --------------------------------------------------------
            if (!result.results.rollP.isNullOrEmpty()) {  // Passed
                result.results.gpa?.let { resultCalculation(it.toDouble()) }
                statusText.text = "Status: Passed"
                infoView.visibility = View.GONE
                statusText.setTextColor(ContextCompat.getColor(gpaOrFailedText.context, R.color.google__Green))
            } else if (!result.results.rollF.isNullOrEmpty()) {  // Failed
                tvLaterGrade.text = "Later Grade : F"
                medal.setImageResource(R.drawable.rank_red_medal)

                val failedSubjectsList = formatSubjects(result.results.subjects)
                infoView.visibility = View.VISIBLE
                gpaOrFailedText.setTextColor(ContextCompat.getColor(gpaOrFailedText.context, R.color.grade_red))
                gpaOrFailedText.text = "Failed"
                failedSubjects.text = "Failed Subjects: $failedSubjectsList"
                val subjectsCount = failedSubjectsList.split(",").size
                statusText.text = "$subjectsCount subjects yet to pass"
                statusText.setTextColor(ContextCompat.getColor(gpaOrFailedText.context, R.color.google__Red))
            } else {
                // Unknown

                tvLaterGrade.text = "Later Grade : -"
                infoView.visibility = View.GONE
                gpaOrFailedText.setTextColor(ContextCompat.getColor(gpaOrFailedText.context, R.color.grade_red))
                medal.setImageResource(R.drawable.rank_red_medal)
                statusText.text = "Status: Unknown"
                statusText.setTextColor(ContextCompat.getColor(gpaOrFailedText.context, R.color.google__Red))
            }
        }

        private fun resultCalculation(gpa: Double) {
            if (gpa >= 3.75){ // A    A+
                tvLaterGrade.text = if (gpa == 4.00) "Later Grade : A+" else "Later Grade : A"
                gpaOrFailedText.setTextColor(ContextCompat.getColor(gpaOrFailedText.context, R.color.grade_diamond))
                medal.setImageResource(R.drawable.rank_diamond_medal)
            } else if (gpa in 3.25..3.74){  // B+  A-
                tvLaterGrade.text = if (gpa in 3.50..3.74) "Later Grade : A-" else "Later Grade : B+"
                gpaOrFailedText.setTextColor(ContextCompat.getColor(gpaOrFailedText.context, R.color.grade_gold))
                medal.setImageResource(R.drawable.rank_gold_medal)
            } else if (gpa in 2.75..3.24) { // B-   B
                tvLaterGrade.text = if (gpa in 3.00..3.24) "Later Grade : B" else "Later Grade : B-"
                gpaOrFailedText.setTextColor(ContextCompat.getColor(gpaOrFailedText.context, R.color.grade_silver))
                medal.setImageResource(R.drawable.rank_silver_medal)
            } else if (gpa in 2.00..2.74) {  //  D   C   C+
                when (gpa) {
                    in 2.50..2.74 -> tvLaterGrade.text = "Later Grade : C+"
                    in 2.25 .. 2.49 -> tvLaterGrade.text = "Later Grade : C"
                    else -> tvLaterGrade.text = "Later Grade : D"
                }
                gpaOrFailedText.setTextColor(ContextCompat.getColor(gpaOrFailedText.context, R.color.grade_bronze))
                medal.setImageResource(R.drawable.rank_bronze_medal)
            } else { //
                tvLaterGrade.text = "Later Grade : -"
                gpaOrFailedText.setTextColor(ContextCompat.getColor(gpaOrFailedText.context, R.color.grade_red))
                medal.setImageResource(R.drawable.rank_red_medal)
            }

            //gpaOrFailedText.setTextColor(ContextCompat.getColor(gpaOrFailedText.context, R.color.google__Blue))
            gpaOrFailedText.text = gpa.toString()
            statusText.text = "Status: Passed"
        }

        private fun formatSubjects(subjects: String?): String {
            if (subjects.isNullOrEmpty()) return "N/A"

            // Remove curly braces and trim the string
            val cleanedSubjects = subjects.replace("{", "").replace("}", "").trim()

            return cleanedSubjects.split(",").joinToString(", ") { subject ->
                val trimmed = subject.trim()
                val code = trimmed.substringBefore("(").trim()
                val type = when (trimmed.substringAfter("(").substringBefore(")").trim()) {
                    "T" -> "Theory"
                    "P" -> "Practical"
                    else -> "Unknown"
                }
                "$code ($type)"
            }
        }

        private fun formatDate(dateString: String): String {
            return try {
                val inputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                val outputFormat = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
                val date = inputFormat.parse(dateString)
                outputFormat.format(date ?: Date())
            } catch (e: Exception) {
                "Invalid Date"
            }
        }
    }
}