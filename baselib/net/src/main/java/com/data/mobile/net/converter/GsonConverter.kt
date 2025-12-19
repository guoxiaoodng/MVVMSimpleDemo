/*
 * Copyright (C) 2018 Drake, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.data.mobile.net.converter

import com.google.gson.GsonBuilder
import kotlinx.parcelize.Parcelize
import org.json.JSONObject
import timber.log.Timber
import java.lang.reflect.Type

/**
 *直接 获取响应结果 的 data
 * */
@Parcelize
class GsonConverter : MyJSONConvert() {
    private val gson = GsonBuilder().serializeNulls().create()

    override fun <R> String.parseBody(succeed: Type): R? {
        val data = JSONObject(this).getString("data")
        Timber.i(data)
        return gson.fromJson(data, succeed)
    }
}