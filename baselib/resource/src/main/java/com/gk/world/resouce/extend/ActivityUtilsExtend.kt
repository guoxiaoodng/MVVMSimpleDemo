package com.gk.world.resouce.extend


/**
 *版权 ： GXD
 *@author: gxd
 *@date: 2021/11/20
 *@time: 16:02
 *@description：
 */
object ActivityUtilsExtend {
    fun finishOtherWithNameActivities(clz: String) {
        /*val activities = ActivityUtils.getActivityList()
        for (act in activities) {
            if (!clz.contains(act.javaClass.simpleName)) {
                ActivityUtils.finishActivity(act, true)
            }
        }*/
    }

    fun finishOtherWithNameActivities(vararg clz: String) {
//        val activities = ActivityUtils.getActivityList()
        /* loop@for (a in activities) {
             for (s in clz) {
                 if (s.contains(a.javaClass.simpleName)) {
                     a.finish()
                     break@loop
                 }
             }
         }*/
        /*activities.forEach { a ->
            clz.forEach loop@{
                if (it.contains(a.javaClass.simpleName)) {
                    a.finish()
                    return@loop
                }
            }
        }*/
    }
}