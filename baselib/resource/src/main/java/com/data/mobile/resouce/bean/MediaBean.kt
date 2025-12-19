package com.data.mobile.resouce.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MediaBean(
        var id: String? = null,
        var url: String? = null,
        var content: String? = null,
): Parcelable