package com.efremov.santa.ext

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.efremov.santa.R
import java.io.Serializable

fun View.visible(visible: Boolean) {
    this.visibility = if (visible) View.VISIBLE else View.GONE
}

fun View.visibleSoft(visible: Boolean) {
    this.visibility = if (visible) View.VISIBLE else View.INVISIBLE
}


fun Fragment.setExtArguments(data: Serializable, keyName: String) {
    val args = Bundle()
    args.putSerializable(keyName, data)
    this.arguments = args
}

fun Fragment.shareContent(content: String) {
    val sharingIntent = Intent(Intent.ACTION_SEND)
    sharingIntent.type = "text/plain"
    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, content)
    startActivity(Intent.createChooser(sharingIntent, "Поделиться"))
}

fun Context.tryOpenLink(link: String,
                         basePath: String? = "https://google.com/search?q=") {
    startActivity(Intent(
        Intent.ACTION_VIEW,
        when {
            URLUtil.isValidUrl(link) -> Uri.parse(link)
            else -> Uri.parse(basePath + link)
        }
    ))
}

fun Fragment.phoneIntent(number: String) {
    startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number")))
}

fun Context?.getIcon(@DrawableRes iconRes: Int?) : Drawable? {
    return if (iconRes != null && this != null)
        ContextCompat.getDrawable(this, iconRes)
    else
        null
}

fun Context?.alert(
    title: String,
    message: String,
    positive: (() -> Unit)? = null,
    negative: (() -> Unit)? = null,
    cancel: (() -> Unit)? = null
) {
    this?.let { context ->
        val builder = AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(R.string.action_ok) { _, _ -> positive?.invoke() }

        if (negative != null)
            builder.setNegativeButton(R.string.action_cancel) { _, _ -> negative.invoke() }

        builder.setOnCancelListener { cancel?.invoke() }

        builder.show()
    }
}

fun Context?.onError(error: String) {
    this.alert("", error)
}

fun Context?.onError(errorTitle: String, errorBody: String) {
    this.alert(errorTitle, errorBody)
}

fun Context.color(colorRes: Int) = ContextCompat.getColor(this, colorRes)

fun Context.convertDpToPx(dp: Float): Int {
    val resources = resources
    val metrics = resources.displayMetrics
    return (dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()
}

fun isValidEmail(target: String) : Boolean =
    if (TextUtils.isEmpty(target)) {
        false
    } else {
        android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

fun fromHtml(target: String) : String =
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
        Html.fromHtml(target, Html.FROM_HTML_MODE_LEGACY).toString().trim()
    } else {
        Html.fromHtml(target).toString().trim()
    }

// RecyclerView extensions
fun <VH: RecyclerView.ViewHolder> RecyclerView.Adapter<VH>.inflateView(@LayoutRes layoutRes: Int, parent: ViewGroup): View {
    val inflater = android.view.LayoutInflater.from(parent.context)
    return inflater.inflate(layoutRes, parent, false)
}

fun RecyclerView.ViewHolder.getContext(): Context {
    return itemView.context
}
//

fun formatDuration(pTime: Int): String {
    return String.format("%02d:%02d", pTime / 60, pTime % 60)
}

fun audioDurationStringToNumber(target: String) : Long {
    val durationParts = target.split(":")
    return durationParts[0].toInt() * 60 + durationParts[1].toLong()
}

fun Context?.sendSupportEmail() {
    this?.let {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.type = "text/html"
        intent.data = Uri.parse("mailto:" + "")
//        intent.putExtra(Intent.EXTRA_EMAIL, Constants.SUPPORT_MAIL)
        intent.putExtra(Intent.EXTRA_SUBJECT, "")
        startActivity(Intent.createChooser(intent, ""))
    }
}