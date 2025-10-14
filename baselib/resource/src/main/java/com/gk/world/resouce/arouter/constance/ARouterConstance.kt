package com.gk.world.resouce.arouter.constance

import com.gk.world.resouce.dynamic.single.CommonListItemNextParamsInterface

/**
 * 综合治理 组件 核心业务  路由
 *
 */
class ARouterConstance {
    object RouterParam {
        /** Router 访问者 基本信息 [com.gk.world.resouce.arouter.guest.VisitorBean]*/
        const val VISITOR = "Visitor"

        /**礼品 访问者携带的东西  实现了 PARCELABLE 对象*/
        const val GIFT_PARCELABLE = "GIFT_PARCELABLE"

        /**礼品 访问者携带的东西  实现了 [CommonListItemNextParamsInterface] 的 对象 返回的map的to json*/
        const val GIFT_SERIALIZABLE = "GIFT_SERIALIZABLE"
    }

    object Main {
        private const val PRE = "/main/"

        /**
         * 主界面
         */
        const val MAIN_ACTIVITY = PRE + "MainActivity"

        const val MAIN_HOME_FRAGMENT = PRE + "MainActivity.HomeFragment"

        /**home Fragment -  待办任务*/
        const val GTASKS_FRAGMENT = PRE + "GTASKS_FRAGMENT"

        /**home Fragment -  待办工单*/
        const val REPAIR_ORDER_FRAGMENT = PRE + "REPAIR_ORDER_FRAGMENT"

        /**
         * 主界面 前的页面
         */
        const val READY_ACTIVITY = PRE + "ReadyActivity"

        /**
         * 登录
         */
        const val LOGIN_ACTIVITY = PRE + "LoginActivity"

        /**
         * 通用动态列表页面
         */
        const val COMMON_LIST_FRAGMENT = PRE + "CommonListFragment"


        /**
         * 修改密码
         */
        const val PASSWORD_MANAGE_ACTIVITY = PRE + "PasswordManageActivity"
    }

    object EnterApply {
        private const val PRE = "/EnterApply/"

        /**
         * 入园申请 待处理列表
         */
        const val UN_HANDLE_WORK_ORDER_LIST_ACTIVITY = PRE + "UnHandleWorkOrderListActivity"
    }

    object X5 {
        private const val PRE = "/X5/"

        /**
         * 入园申请 处理记录
         */
        const val X5_WEB_VIEW = PRE + "MediaPreviewActivity"
    }

    object BaiduMap {
        private const val PRE = "/Map/"

        /**
         * 入园申请 处理记录
         */
        const val LOCAL_MAP_ACTIVITY = PRE + "LocalMapActivity"
    }

    object Notice {
        private const val PRE = "/Notice/"

        /**
         * 通知公告列表
         */
        const val NOTICE_LIST_ACTIVITY = PRE + "NoticeListActivity"

        /**
         * 通知公告详情
         */
        const val NOTICE_DETAIL_ACTIVITY = PRE + "NoticeDetailActivity"
    }

    object Setting {
        private const val PRE = "/Setting/"

        /**
         * 通知公告列表
         */
        const val SETTING_ACTIVITY = PRE + "SettingActivity"
    }

    object Schedule {
        private const val PRE = "/Schedule/"

        /**
         * 所有日程
         */
        const val ALL_SCHEDULE_ACTIVITY = PRE + "AllScheduleActivity"

        /**
         * 新建日程
         */
        const val NEW_SCHEDULE_CREATE_ACTIVITY = PRE + "NewScheduleCreateActivity"
    }

    object Staff {
        private const val PRE = "/Staff/"

        /**
         * 用户信息
         */
        const val ORGANIZATION_INFO_ACTIVITY = PRE + "OrganizationInfoActivity"

        /**
         * 视频会商
         */
        const val VIDEO_X5_WEB_ACTIVITY = PRE + "VideoX5WebActivity"
    }

    object Lease {
        private const val PRE = "/lease/"

        /**
         * 租赁列表
         */
        const val LEASE_INFO_LIST_ACTIVITY = PRE + "LeaseInfoListActivity"

        /**
         * 租赁详情
         */
        const val LEASE_INFO_DETAIL_ACTIVITY = PRE + "LeaseInfoDetailActivity"

        /**
         * 房源需求
         */
        const val HOUSE_NEED_DETAIL_ACTIVITY = PRE + "HouseNeedDetailActivity"

        /**
         * 房源需求列表
         */
        const val HOUSE_NEED_LIST_ACTIVITY = PRE + "HouseNeedListActivity"
    }

    object CheckSign {
        private const val PRE = "/checkSign/"

        /**
         * 考勤打卡
         */
        const val CHECK_SIGN_ACTIVITY = PRE + "CheckSignActivity"
    }

    object Rules {
        private const val PRE = "/rules/"

        /**
         * 规章制度
         */
        const val RULES_LIST_ACTIVITY = PRE + "RulesListActivity"
    }

    object Merchants {
        private const val PRE = "/merchants/"

        /**
         * 靶向搜索
         */
        const val POINT_SEARCH_ACTIVITY = PRE + "PointSearchActivity"

        /**
         * 靶向搜索企业列表
         */
        const val POINT_SEARCH_LIST_ACTIVITY = PRE + "PointSearchListActivity"

        /**
         * 靶向搜索企业详情
         */
        const val POINT_SEARCH_DETAIL_ACTIVITY = PRE + "PointSearchDetailActivity"

        /**
         * 意向企业列表
         */
        const val INTENT_ENTERPRISE_LIST_ACTIVITY = PRE + "IntentEnterpriseListActivity"

        /**
         * 意向企业详情
         */
        const val INTENT_ENTERPRISE_DETAIL_ACTIVITY = PRE + "IntentEnterpriseDetailActivity"

        /**
         * 意向企业我的申请列表
         */
        const val INTENT_ENTERPRISE_MY_APPLY_LIST_ACTIVITY =
            PRE + "IntentEnterpriseMyAppListActivity"

        /**
         * 意向企业我的申请详情
         */
        const val INTENT_ENTERPRISE_MY_APPLY_DETAIL_ACTIVITY =
            PRE + "IntentEnterpriseMyAppDetailActivity"
    }

    object Visitor {
        private const val PRE = "/visitor/"

        /**
         * 访客申请记录
         */
        const val VISITOR_APPLY_RECORDS_LIST_ACTIVITY = PRE + "VisitorApplyRecordsListActivity"

        /**
         * 访客申请详情
         */
        const val VISITOR_APPLY_RECORDS_DETAIL_ACTIVITY = PRE + "VisitorApplyRecordsDetailActivity"

        /**
         * 访客分析
         */
        const val VISITOR_ANALYSIS_ACTIVITY = PRE + "VisitorAnalysisActivity"
    }

    object SecurityPatrol {
        private const val PRE = "/securityPatrol/"

        /**
         * 安保巡视记录列表
         */
        const val SECURITY_PATROL_RECORDS_LIST_ACTIVITY = PRE + "SecurityPatrolRecordsListActivity"

        /**
         * 安保巡视记录详情
         */
        const val SECURITY_PATROL_RECORDS_DETAIL_ACTIVITY = PRE + "SecurityPatrolDetailActivity"

        /**
         * 物业管理
         */
        const val PROPERTY_MANAGER_ACTIVITY = PRE + "PropertyManagerActivity"
    }

    object EquipmentPatrol {
        private const val PRE = "/equipmentPatrol/"

        /**
         * 巡检任务处理
         */
        const val PATROL_TASK_HANDLE_ACTIVITY = PRE + "PatrolTaskHandleActivity"

        /**
         * 巡检任务列表
         */
        const val PATROL_TASK_LIST_ACTIVITY = PRE + "PatrolTaskListActivity"

        /**
         * 巡检记录列表
         */
        const val PATROL_TASK_RECORDS_LIST_ACTIVITY = PRE + "PatrolTaskRecordsListActivity"

        /**
         * 点检任务处理
         */
        const val POINT_TASK_HANDLE_ACTIVITY = PRE + "PointTaskHandleActivity"

        /**
         * 巡检记录详情
         */
        const val PATROL_RECORDS_DETAIL_ACTIVITY = PRE + "PatrolRecordsDetailActivity"
    }

    object IOTEquipment {
        private const val PRE = "/iotEquipment/"

        /**
         * 物联设备统计
         */
        const val IOT_EQUIPMENT_ALL_ACTIVITY = PRE + "IOTEquipmentAllActivity"

        /**
         * 物联设备详情
         */
        const val IOT_EQUIPMENT_List_ACTIVITY = PRE + "IOTEquipmentDetailActivity"

        /**
         * 物联设备信息
         */
        const val IOT_EQUIPMENT_INFO_ACTIVITY = PRE + "IOTEquipmentInfoActivity"
    }

    /**
     *  物业报修
     **/
    object Maintain {
        private const val PRE = "/Maintain/"

        /**
         * 发起维修
         */
        const val LAUNCH_MAINTAIN_ACTIVITY = PRE + "LaunchMaintainActivity"
        const val ACTIVITY_PROPERTY_MAINTAIN_LIST = PRE + "LaunchVerifyListActivity"
        const val ACTIVITY_PROPERTY_MAINTAIN_DETAIL = PRE + "LaunchVerifyDetailActivity"

    }

    object ZoneState {
        private const val PRE = "/zoneState/"

        /**
         * 园区状态详情
         */
        const val ZONE_STATE_DETAIL_ACTIVITY = PRE + "ZoneStateDetailActivity"

        /**
         * 园区状态列表
         */
        const val ZONE_STATE_LIST_ACTIVITY = PRE + "ZoneStateListActivity"
    }

    object MineCommon {
        private const val PRE = "/mineCommon/"

        /**
         * 我的界面记录通用界面
         */
        const val MINE_COMMON_RECORDS_LIST_ACTIVITY = PRE + "MineCommonRecordsListActivity"
    }

    object DailyWork {
        private const val PRE = "/dailyWork/"

        /**
         * 新增绿化 保洁
         */
        const val GREEN_CLEAN_WORK_NEW_ADD_ACTIVITY = PRE + "GreenCleanWorkNewAddActivity"
        /**
         * 巡检任务新增
         */
        const val INSPECTION_NEW_ADD_ACTIVITY = PRE + "InspectionNewAddActivity"

        /**
         * 安保巡视新增
         */
        const val SECURITY_PATROL_START_ACTIVITY = PRE + "SecurityPatrolNewAddActivity"

        /**
         * 安保巡视结束
         */
        const val SECURITY_PATROL_END_ACTIVITY = PRE + "SecurityPatrolEndActivity"

        /**
         * 已完成列表
         */
        const val DAILY_WORK_FINISHED_ACTIVITY = PRE + "DailyWorkFinishedActivity"

        /**
         * 设备巡检已完成详情
         */
        const val DAILY_WORK_FINISHED_PATROL_DETAIL_ACTIVITY = PRE + "DailyWorkFinishedPatrolDetailActivity"

        /**
         * 设备点检已完成详情
         */
        const val DAILY_WORK_FINISHED_POINT_DETAIL_ACTIVITY = PRE + "DailyWorkFinishedPointDetailActivity"

        /**
         * 绿化 保洁详情
         */
        const val DAILY_WORK_FINISHED_GREEN_CLEAN_DETAIL_ACTIVITY = PRE + "DailyWorkFinishGreenCleanDetailActivity"
    }

    object PropertyManager {
        private const val PRE = "/PropertyManager/"

        /**
         * 设备详情
         */
        const val EQUIPMENT_DETAIL_ACTIVITY = PRE + "EquipmentDetailActivity"

        /**
         * 资产领用
         */
        const val PROPERTY_COLLECT_CREATE_ACTIVITY = PRE + "PropertyCollectCreateActivity"

        /**
         * 资产退库
         */
        const val PROPERTY_CANCEL_STOCK_CREATE_ACTIVITY = PRE + "PropertyCancelStockCreateActivity"

        /**
         * 资产调拨
         */
        const val PROPERTY_ALLOCATE_TRANSFER_CREATE_ACTIVITY = PRE + "PropertyAllocateTransferCreateActivity"

        /**
         * 缺陷 故障登记
         */
        const val DEFECT_HITCH_SIGN_CREATE_ACTIVITY = PRE + "DefectHitchSignCreateActivity"

        /**
         * 完成维修
         */
        const val FINISH_MAINTAIN_ACTIVITY = PRE + "FinishMaintainActivity"
    }
}