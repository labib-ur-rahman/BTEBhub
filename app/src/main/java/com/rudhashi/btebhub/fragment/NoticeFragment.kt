package com.rudhashi.btebhub.fragment

import android.annotation.SuppressLint
import com.rudhashi.btebhub.R
import com.rudhashi.btebhub.adapter.NoticeAdapter
import com.rudhashi.btebhub.databinding.FragmentNoticeBinding
import com.rudhashi.btebhub.model.Notice
import android.app.AlertDialog
import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.rudhashi.btebhub.utils.SmartData.fixDownloadLink
import org.json.JSONObject
import java.net.URL
import kotlin.concurrent.thread


class NoticeFragment : Fragment() {

    private var _binding: FragmentNoticeBinding? = null
    private val binding get() = _binding!!

    // Permission callback
    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(requireContext(), "Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoticeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())


        binding.apply {
            image.visibility = View.VISIBLE
        }

        fetchNotices()
    }

    private fun fetchNotices() {
        thread {
            try {
                val response = URL("https://btebresultsbd.com/api/notice?type=all").readText()
                val jsonObject = JSONObject(response)
                val notices = jsonObject.getJSONArray("notices")
                val noticeList = mutableListOf<Notice>()

                for (i in 0 until notices.length()) {
                    val item = notices.getJSONObject(i)
                    val date = item.getString("date")
                    val mainTitle = item.getString("mainTitle")
                    val link = item.getString("link")
                    noticeList.add(Notice(date, mainTitle, link))
                }

                activity?.runOnUiThread {
                    binding.apply {
                        image.visibility = View.INVISIBLE
                        recyclerView.visibility = View.VISIBLE
                    }
                    binding.recyclerView.adapter = NoticeAdapter(noticeList) { link ->
                        checkPermissionAndDownload(fixDownloadLink(link))
                    }

                }
            } catch (e: Exception) {
                activity?.runOnUiThread {
                    Toast.makeText(requireContext(), "Failed to fetch notices", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun checkPermissionAndDownload(url: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                showConfirmDialog(url)
            } else {
                requestPermissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        } else {
            showConfirmDialog(url)
        }
    }

    private fun showConfirmDialog(url: String) {
        // Inflate the custom confirmation dialog layout
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_confirm_download, null)
        val alertDialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(true)
            .create()

        // Button listeners
        dialogView.findViewById<Button>(R.id.btnDownload).setOnClickListener {
            alertDialog.dismiss() // Close confirmation dialog
            showProgressDialog(url) // Start download with progress dialog
        }

        dialogView.findViewById<Button>(R.id.btnCancel).setOnClickListener {
            alertDialog.dismiss() // Close confirmation dialog
        }

        alertDialog.show()
    }

    private fun showProgressDialog(url: String) {
        // Inflate the custom progress dialog layout
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_progress_download, null)
        val progressBar = dialogView.findViewById<ProgressBar>(R.id.progressBar)
        val tvPercentage = dialogView.findViewById<TextView>(R.id.tvPercentage)

        val progressDialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(false) // Prevent dismissing the dialog while downloading
            .create()

        progressDialog.show()

        // Start downloading using DownloadManager
        val request = DownloadManager.Request(Uri.parse(fixDownloadLink(url)))
        request.setTitle("Downloading File")
        request.setDescription("Please wait...")
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "file_download.pdf")

        val downloadManager = requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val downloadId = downloadManager.enqueue(request)

        // Monitor download progress
        thread {
            var downloading = true
            while (downloading) {
                val query = DownloadManager.Query()
                query.setFilterById(downloadId)
                val cursor = downloadManager.query(query)
                if (cursor.moveToFirst()) {
                    val bytesDownloaded = cursor.getLong(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                    val bytesTotal = cursor.getLong(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))

                    if (bytesTotal > 0) {
                        val progress = ((bytesDownloaded * 100) / bytesTotal).toInt()
                        activity?.runOnUiThread {
                            progressBar.progress = progress
                            tvPercentage.text = "$progress%"
                        }
                    }

                    val status = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS))

                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        downloading = false
                        activity?.runOnUiThread {
                            progressDialog.dismiss() // Close progress dialog
                            Toast.makeText(requireContext(), "Download completed!", Toast.LENGTH_SHORT).show()
                        }
                    } else if (status == DownloadManager.STATUS_FAILED) {
                        downloading = false
                        activity?.runOnUiThread {
                            progressDialog.dismiss() // Close progress dialog
                            Toast.makeText(requireContext(), "Download failed!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                cursor.close()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}