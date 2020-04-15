/*
 * Copyright (C) 2012 www.amsoft.cn
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.steve.commonarch.aes


import android.util.Log
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import com.steve.commonarch.BuildConfig.DEBUG
import com.steve.commonarch.base.TAG


class GsonUtil {

//    internal data class User(var id : String,var name : String) {
//
//    }

    companion object {

        /**
         *
         * 描述：将对象转化为json.
         * @param
         * @return
         */
        fun toJson(src: Any): String? {
            var json: String? = null
            try {
                val gsonb = GsonBuilder()
                val gson = gsonb.create()
                json = gson.toJson(src)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return json
        }

        /**
         *
         * 描述：将对象转化为jsonObject.
         * @param
         * @return
         */
        fun toJsonObject(src: Any): JsonObject {
            var jsonObject = JsonObject()
            try {
                val gsonb = GsonBuilder()
                val gson = gsonb.create()
                jsonObject = JsonParser().parse(gson.toJson(src)).asJsonObject
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return jsonObject
        }

        /**
         *
         * 描述：将对象转化为JsonArray.
         * @param
         * @return
         */
        fun toJsonArray(src: String): JsonArray? {
            var jsonArray: JsonArray? = null
            try {
                jsonArray = JsonParser().parse(src).asJsonArray
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return jsonArray
        }

        /**
         *
         * 描述：将对象转化为jsonObject.
         * @param
         * @return
         */
        fun toJsonObject(src: String): JsonObject? {
            var jsonObject: JsonObject? = null
            try {
                jsonObject = JsonParser().parse(src).asJsonObject
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return jsonObject
        }

        /**
         *
         * 描述：将对象转化为jsonObject.去除某些字段,这些字段需要使用注解@Expose修饰
         * @param
         * @return
         */
        fun toJsonObjectByExcludeFields(src: Any): JsonObject {
            var jsonObject = JsonObject()
            try {
                val gsonb = GsonBuilder()
                val gson = gsonb.excludeFieldsWithoutExposeAnnotation().create()
                jsonObject = JsonParser().parse(gson.toJson(src)).asJsonObject
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return jsonObject
        }

        /**
         *
         * 描述：将列表转化为json.
         * @param list
         * @return
         */
        fun toJson(list: List<*>): String? {
            var json: String? = null
            try {
                val gsonb = GsonBuilder()
                val gson = gsonb.create()
                json = gson.toJson(list)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return json
        }

        /**
         *
         * 描述：将json转化为列表.
         * @param json
         * @param typeToken object : TypeToken<ArrayList<*>>() {};
         * @return
         */
        fun fromJson(json: String, typeToken: TypeToken<*>): List<*>? {
            var list: List<*>? = null
            try {
                val gsonb = GsonBuilder()
                val gson = gsonb.create()
                val type = typeToken.type
                list = gson.fromJson<List<*>>(json, type)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return list
        }

        /**
         *
         * 描述：将json转化为对象.
         * @param json
         * @param clazz
         * @return
         */
        fun fromJson(json: String, clazz: Class<*>): Any? {
            var obj: Any? = null
            try {
                val gsonb = GsonBuilder()
                val gson = gsonb.create()
                obj = gson.fromJson(json, clazz)
            } catch (e: Exception) {
                e.printStackTrace()
                if (DEBUG) {
                    Log.e(TAG, "fromJson error", e)
                }
            }

            return obj
        }

//        fun test(){
//            val list = fromJson("[{id:1,name:22},{id:2,name:33}]", object : TypeToken<ArrayList<User>>() {
//
//            }) as List<User>
//            println(list.size)
//            for (u in list) {
//                println(u)
//            }
//
//            val u = fromJson("{id:1,name:22}", User::class.java) as User
//            println(u)
//        }
//
//        /**
//         * The main method.
//         *
//         * @param args the arguments
//         */
//        @JvmStatic
//        fun main(args: Array<String>) {
//            val list = fromJson("[{id:1,name:22},{id:2,name:33}]", object : TypeToken<ArrayList<User>>() {
//
//            }) as List<User>?
//            println(list!!.size)
//            for (u in list) {
//                println(u.name)
//            }
//
//            val u = fromJson("{id:1,name:22}", User::class.java) as User?
//            println(u!!.name)
//        }

//        fun test(){
//            val list = fromJson("[{id:1,name:22},{id:2,name:33}]", object : TypeToken<ArrayList<User>>() {
//
//            }) as List<User>
//            println(list.size)
//            for (u in list) {
//                println(u)
//            }
//
//            val u = fromJson("{id:1,name:22}", User::class.java) as User
//            println(u)
//        }
//
//        /**
//         * The main method.
//         *
//         * @param args the arguments
//         */
//        @JvmStatic
//        fun main(args: Array<String>) {
//            val list = fromJson("[{id:1,name:22},{id:2,name:33}]", object : TypeToken<ArrayList<User>>() {
//
//            }) as List<User>?
//            println(list!!.size)
//            for (u in list) {
//                println(u.name)
//            }
//
//            val u = fromJson("{id:1,name:22}", User::class.java) as User?
//            println(u!!.name)
//        }
    }

}
