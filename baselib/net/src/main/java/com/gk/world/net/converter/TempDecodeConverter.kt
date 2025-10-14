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

package com.gk.world.net.converter

import kotlinx.parcelize.Parcelize
import org.json.JSONObject
import java.lang.reflect.Type

/**
 * 响应结果加密时使用
 * */
@Parcelize
class TempDecodeConverter(val key : String = "requestId" ) : MyJSONConvert() {

    override fun <R> String.parseBody(succeed: Type): R? {
        if (JSONObject(this).has(key)) {
            val data = JSONObject(this).getString(key)
            return data as R
        }
        return null
    }
}