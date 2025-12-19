package com.data.mobile.resouce.arouter.guest

import androidx.annotation.IntDef

/**
 * 访客行为 枚举 暂时就想到这些
 * V层表现为 1.读取（显示删除-编辑-单据操作等） 2.写入（包含提交错做）
 * R read 读取  获取信息 共用户 查看 操作
 * W write 写入  写 操作需要刷新列表
 * RG read and get 获取  一般是在列表页 选择并返回
 *
 */

@Target(
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.FIELD,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.CLASS,
    AnnotationTarget.LOCAL_VARIABLE
)
@MustBeDocumented
@IntDef(
    BehaviorEnum.R,
    BehaviorEnum.W,
    BehaviorEnum.RG,
)
@Retention(AnnotationRetention.SOURCE)
annotation class BehaviorEnum {
    companion object {
        /*读 查看 - */
        const val R = 1

        /*写 -写入 - */
        const val W = 2

        /*读并获取 - */
        const val RG = 3
    }
}