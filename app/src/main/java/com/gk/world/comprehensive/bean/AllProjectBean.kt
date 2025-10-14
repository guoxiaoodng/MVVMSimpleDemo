package com.gk.world.comprehensive.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 *版权 ： GXD
 *@author: gxd
 *@date: 2022/3/18
 *@time: 13:57
 *@description：
 *{
"optimizeCountSql": true,
"records": [
{
"projectResponsibilityDepartment": "12345热线",
"unifiedCreditCode": "4",
"landArea": "4",
"industryCategoryName": "新能源汽车整车制造",
"updateName": "系统管理员",
"attractInvestmentSpeedName": "沟通洽谈",
"attractInvestmentSpeed": "1502173617425797121",
"enterpriseType": "股份有限公司",
"projectRecordName": "4",
"id": "1504386200008527872",
"totalInvestment": "4",
"projectName": "4",
"enterpriseName": "4",
"projectResponsibilityUser": "4",
"createName": "系统管理员"
}
],
"total": 3,
"current": 1,
"size": 10,
"isSearchCount": true
}
 */

data class AllProjectBean(
    val attractInvestmentSpeed: String,
    val attractInvestmentSpeedName: String,
    val createName: String,
    val enterpriseName: String,
    val enterpriseType: String,
    val id: String,
    val industryCategoryName: String,
    val landArea: String,
    val projectName: String,
    val projectRecordName: String,
    val projectResponsibilityDepartment: String,
    val projectResponsibilityUser: String,
    val totalInvestment: String,
    val unifiedCreditCode: String,
    val updateName: String,
)

@Parcelize
data class ProjectTypeBeanItem(
    val id: String,
    val value: String
) : Parcelable