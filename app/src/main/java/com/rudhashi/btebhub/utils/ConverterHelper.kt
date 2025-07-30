package com.rudhashi.btebhub.utils

import java.text.SimpleDateFormat
import java.util.Locale

object ConverterHelper {
    fun getFormattedSemester(semester: String): String {
        return when (semester) {
            "1" -> "1st Semester"
            "2" -> "2nd Semester"
            "3" -> "3rd Semester"
            "4" -> "4th Semester"
            "5" -> "5th Semester"
            "6" -> "6th Semester"
            "7" -> "7th Semester"
            "8" -> "8th Semester"
            else -> "$semester Semester"
        }
    }

    fun getSemToNo(semester: String): String {
        return when (semester) {
            "1st Semester" -> "1"
            "2nd Semester" -> "2"
            "3rd Semester" -> "3"
            "4th Semester" -> "4"
            "5th Semester" -> "5"
            "6th Semester" -> "6"
            "7th Semester" -> "7"
            "8th Semester" -> "8"
            else -> "0"
        }
    }

    fun formatDate(dateString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        return outputFormat.format(date!!)
    }

    fun getTechToExam(semester: String): String {
        return when (semester) {
            "Diploma In Engineering" -> "DIPLOMA+IN+ENGINEERING"
            "Diploma In Engineering (Army)" -> "DIPLOMA+IN+ENGINEERING+%28ARMY%29"
            "Diploma In Engineering (Naval)" -> "DIPLOMA+IN+ENGINEERING+%28NAVAL%29"
            "Diploma In Tourism And Hospitality" -> "DIPLOMA+IN+TOURISM+AND+HOSPITALITY"
            "Diploma In Textile Engineering" -> "DIPLOMA+IN+TEXTILE+ENGINEERING"
            "Diploma In Agriculture" -> "DIPLOMA+IN+AGRICULTURE "
            "Diploma In Fisheries" -> "DIPLOMA+IN+FISHERIES"
            "Diploma In Forestry" -> "DIPLOMA+IN+FORESTRY"
            "Diploma In Livestock" -> "DIPLOMA+IN+LIVESTOCK"
            "Diploma In Medical Technology" -> "DIPLOMA+IN+MEDICAL+TECHNOLOGY"
            "Certificate In Medical Ultrasound" -> "CERTIFICATE+IN+MEDICAL+ULTRASOUND"
            "Diploma In Commerce" -> "DIPLOMA+IN+COMMERCE"
            "Certificate In Marine Trade" -> "CERTIFICATE+IN+MARINE+TRADE"
            "Advanced Certificate Course" -> "ADVANCED+CERTIFICATE+COURSE"
            "National Skill Standard Basic Certificate Course" -> "NATIONAL+SKILL+STANDARD+BASIC+CERTIFICATE+COURSE"
            "HSC (Business Management)" -> "HSC+%28BUSINESS+MANAGEMENT%29"
            "HSC (Vocational)" -> "HSC+%28VOCATIONAL%29"
            else -> "N/A"
        }
    }

    fun getExamToTech(semester: String): String {
        return when (semester) {
            "DIPLOMA+IN+ENGINEERING" -> "Diploma In Engineering"
            "DIPLOMA+IN+ENGINEERING+%28ARMY%29" -> "Diploma In Engineering (Army)"
            "DIPLOMA+IN+ENGINEERING+%28NAVAL%29" -> "Diploma In Engineering (Naval)"
            "DIPLOMA+IN+TOURISM+AND+HOSPITALITY" -> "Diploma In Tourism And Hospitality"
            "DIPLOMA+IN+TEXTILE+ENGINEERING" -> "Diploma In Textile Engineering"
            "DIPLOMA+IN+AGRICULTURE" -> "Diploma In Agriculture"
            "DIPLOMA+IN+FISHERIES" -> "Diploma In Fisheries"
            "DIPLOMA+IN+FORESTRY" -> "Diploma In Forestry"
            "DIPLOMA+IN+LIVESTOCK" -> "Diploma In Livestock"
            "DIPLOMA+IN+MEDICAL+TECHNOLOGY" -> "Diploma In Medical Technology"
            "CERTIFICATE+IN+MEDICAL+ULTRASOUND" -> "Certificate In Medical Ultrasound"
            "DIPLOMA+IN+COMMERCE" -> "Diploma In Commerce"
            "CERTIFICATE+IN+MARINE+TRADE" -> "Certificate In Marine Trade"
            "ADVANCED+CERTIFICATE+COURSE" -> "Advanced Certificate Course"
            "NATIONAL+SKILL+STANDARD+BASIC+CERTIFICATE+COURSE" -> "National Skill Standard Basic Certificate Course"
            "HSC+%28BUSINESS+MANAGEMENT%29" -> "HSC (Business Management)"
            "HSC+%28VOCATIONAL%29" -> "HSC (Vocational)"
            else -> "Unknown"
        }
    }

    //========================================
    // Other utility variables here...
    val technologyList = listOf(
        "Diploma In Engineering",
        "Diploma In Engineering (Army)",
        "Diploma In Engineering (Naval)",
        "Diploma In Tourism And Hospitality",
        "Diploma In Textile Engineering",
        "Diploma In Agriculture",
        "Diploma In Fisheries",
        "Diploma In Forestry",
        "Diploma In Livestock",
        "Diploma In Medical Technology",
        "Certificate In Medical Ultrasound",
        "Diploma In Commerce",
        "Certificate In Marine Trade",
        "Advanced Certificate Course",
        "National Skill Standard Basic Certificate Course",
        "HSC (Business Management)",
        "HSC (Vocational)"
    )
    //========================================
}