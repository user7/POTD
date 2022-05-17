package com.geekbrains.potd.repository

import android.content.Context
import android.util.Log
import com.geekbrains.potd.App
import com.geekbrains.potd.MainViewModel
import com.geekbrains.potd.R
import com.geekbrains.potd.fragments.bookmarks.Bookmark
import com.geekbrains.potd.fragments.bookmarks.Bookmarks
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import com.google.gson.internal.bind.JsonTreeReader
import java.io.*

class BookmarksRepository(private val mainViewModel: MainViewModel) {
    fun save() {
        val bookmarks = mainViewModel.bookmarks.value?.let { Bookmarks(it) } ?: return
        Thread {
            val json = bookmarksToJson(bookmarks)
            val context = App.instance.applicationContext
            val writer =
                OutputStreamWriter(context.openFileOutput(BOOKMARKS_TEMP, Context.MODE_PRIVATE))
            writer.write(json)
            writer.close()
            context.getFileStreamPath(BOOKMARKS_TEMP)
                .renameTo(context.getFileStreamPath(BOOKMARKS_FILE))
        }.start()
    }

    fun load() {
        Thread {
            val context = App.instance.applicationContext
            try {
                var json =
                    try {
                        val reader = InputStreamReader(context.openFileInput(BOOKMARKS_FILE))
                        reader.readLines().joinToString(separator = "", transform = null)
                    } catch (e: FileNotFoundException) {
                        App.instance.applicationContext.getString(R.string.default_bookmarks)
                    }
                json?.let { mainViewModel.setBookmarks(jsonToBookmarks(json)) }
            } catch (e: Exception) {
                Log.d("==", "loading bookmarks failed: $e")
            }
        }.start()
    }

    private fun bookmarksToJson(bookmarks: List<Bookmark>): String {
        val sb = StringBuilder("[")
        for (b in bookmarks) {
            sb.append("{\"%s\":%s},".format(b::class.simpleName, Gson().toJson(b, b::class.java)))
        }
        sb.trimToSize()
        if (bookmarks.isNotEmpty())
            sb.deleteCharAt(sb.length - 1)
        sb.append("]")
        return sb.toString()
    }

    private fun jsonToBookmarks(json: String): ArrayList<Bookmark> {
        val array: JsonArray = JsonParser().parse(json).asJsonArray
        val outputArray = ArrayList<Bookmark>()
        for (i in 0 until array.size()) {
            val obj = array.get(i).asJsonObject
            for (subClass in Bookmark::class.sealedSubclasses) {
                val name = subClass.simpleName
                if (!obj.has(name))
                    continue
                val result: Bookmark =
                    Gson().fromJson(JsonTreeReader(obj.getAsJsonObject(name)), subClass.java)
                outputArray.add(result)
                break
            }
        }
        return outputArray
    }

    companion object {
        const val BOOKMARKS_TEMP = "bookmarks.json.tmp"
        const val BOOKMARKS_FILE = "bookmarks.json"
    }
}