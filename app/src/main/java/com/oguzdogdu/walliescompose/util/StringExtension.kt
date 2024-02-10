package com.oguzdogdu.walliescompose.util

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun String.formatDate(
    outputFormat: String = DateFormats.OUTPUT_DOTTED_LAST_PAYMENT_DATE_FORMAT.value,
    inputFormat: String = DateFormats.INPUT_DATE_FORMAT.value
): String {
    var d: Date? = null

    val inputDateFormat = SimpleDateFormat(inputFormat, Locale.ENGLISH)
    val outputDateFormat = SimpleDateFormat(outputFormat, Locale("en"))

    try {
        d = inputDateFormat.parse(this)
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return if (d == null) {
        this
    } else {
        outputDateFormat.format(d)
    }
}

fun String.colorize(color: Int): SpannableString {
    val spannable = SpannableString(this)
    spannable.setSpan(
        ForegroundColorSpan(color),
        0,
        this.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    return spannable
}

fun String.toBitmap(): Bitmap {
    val imageUrl = URL(this)
    val connection = imageUrl.openConnection() as HttpURLConnection
    connection.doInput = true
    connection.connect()
    val input = connection.inputStream
    return BitmapFactory.decodeStream(input)
}

enum class DateFormats(val value: String) {
    INPUT_DATE_FORMAT("yyyy-MM-dd'T'HH:mm:ss"),
    OUTPUT_BASIC_DATE_FORMAT("MMMM yyyy"),
    OUTPUT_LAST_PAYMENT_DATE_FORMAT("dd MMMM yyyy"),
    OUTPUT_DOTTED_LAST_PAYMENT_DATE_FORMAT("dd.MM.yyyy")
}

enum class ThemeKeys(val value: String) {
    LIGHT_THEME("1"),
    DARK_THEME("2"),
    SYSTEM_THEME("3")
}
fun String.shareExternal(): Intent {
    val dataToShare = this
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, dataToShare)
        type = "text/plain"
    }
    return Intent.createChooser(sendIntent, null)
}

 fun String.openInstagramProfile() : Intent {
    val url = "https://instagram.com/$this"
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
     return Intent.createChooser(intent, null)
}

 fun String.openTwitterProfile() :Intent {
    val url = "https://twitter.com/$this"
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
     return Intent.createChooser(intent, null)
}


 fun String.openPortfolioUrl() :Intent {
     val intent = Intent(Intent.ACTION_VIEW, Uri.parse(this))
     return Intent.createChooser(intent, null)
}
