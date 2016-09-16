package com.directdev.portal.adapter

import android.app.DownloadManager
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.crashlytics.android.Crashlytics
import com.directdev.portal.R
import com.directdev.portal.model.ResModel
import com.directdev.portal.model.ResResourcesModel
import kotlinx.android.synthetic.main.item_resources.view.*
import org.jetbrains.anko.downloadManager
import org.jetbrains.anko.layoutInflater
import org.jetbrains.anko.onClick

class ResourcesRecyclerAdapter(val context: Context, val data: List<String>, val resources: ResModel) : RecyclerView.Adapter<ResourcesRecyclerAdapter.ViewHolder>() {
    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(context.layoutInflater.inflate(R.layout.item_resources, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val outlines = resources.resources.filter { it.courseOutlineTopicID == data[position] }
        holder?.bindData(context, outlines, resources)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindData(ctx: Context, item: List<ResResourcesModel>, resources: ResModel) {
            if (item.size == 0) return
            itemView.resSession.text = "Meeting " + item[0].sessionIDNUM
            itemView.resTopic.text = item[0].courseOutlineTopic
            try {
                itemView.presentationDownload.backgroundTintList = ColorStateList.valueOf(Color.parseColor(ctx.getString(R.color.colorAccent)))
            } catch (e: NoSuchMethodError) {
                Crashlytics.logException(e)
            }
            itemView.presentationDownload.onClick {
                val selectedDownload = resources.path.filter {
                    it.mediaTypeId == "01" && it.courseOutlineTopicID == item[0].courseOutlineTopicID
                }
                if (selectedDownload.size == 0) return@onClick
                val path = (selectedDownload[0].path + selectedDownload[0].location + "/" + selectedDownload[0].filename).replace("\\", "/").replace(" ", "%20")
                Log.d("Path", path)
                ctx.downloadManager.enqueue(DownloadManager.Request(Uri.parse(path)))
            }
        }
    }
}