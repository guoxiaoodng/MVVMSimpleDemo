package com.gk.world.net.constance.http

/**
 * 记录接口地址
 */
class HttpPortConst {
    /**
     * .net接口
     */
    object DaoNet {

    }

    /**
     * JAVA
     */
    object Java {

        /**获取验证码*/
        const val GET_AUTH_CODE = "/sys/getCode"

        const val TEM_URL = "http://222.187.46.88:9003"

        //图片上传
        const val SMART_PARK_UPLOAD_URL = "http://58.218.244.159:9000/api"

        /**
         * JAVA - 用户中心接口
         */
        object UserCenter {
            //===================== login and  main interface ===================//
            /**登录*/
            const val LOGIN = "/union/login"

            const val SECOND_LOGIN = "/cloud/auth/usercenter/login"

            /**
             * 模拟登录 视频会商
             */
            const val TMP_LOGIN = "$TEM_URL/video/system/user/mockLogin"

            /**
             * 获取用户信息
             */
            const val GET_USER_INFO = "/cloud/auth/sys/resourceUserApp/detail"

            /**
             * 系统通讯录
             */
            const val GET_SYS_ADDRESS_BOOK = "/appDepartmentAddressBook/getSystemAddressBook"

            /**
             * 通讯录详情
             */
            const val GET_ADDRESS_BOOK_DETAIL = "/appDepartmentAddressBook/detailByUserId"

            /**
             * 获取验证码
             */
            const val GET_AUTH_CODE = "/sys/getCode"

            /**我全部的菜单*/
            const val ALL_MENU = "/appSysMenu/getAppMenuTree"

            //===================== 通用 ===================//
            /**上传文件*/
            const val COMMON_UPLOAD_FILE = "/resourceAttachment/model/uploads"

            const val SMART_PARK_UPLOAD_FILE = "/cloud/auth/resource/uploadBatch"

            //===================== 版本管理 ===============//
            /**
             * 获取最新app版本
             */
            const val GET_APP_VERSION = "/appVersionInfo/latestAppVersion"

            /**
             * 楼栋列表
             */
            const val POST_GET_BUILD_LIST = "/cloud/yqfw/appParkBuilding/queryList"

            /**
             * 房源信息分页查询
             */
            const val POST_HOUSE_INFO_PAGE_LIST = "/cloud/wyfw/wyHouseInfoApp/queryPageList"

            /**
             * 房源信息详情
             */
            const val GET_HOUSE_INFO_DETAIL = "/cloud/wyfw/wyHouseInfoApp/detail"

            /**
             * 房源需求数据统计
             */
            const val GET_HOUSE_NEED_COUNT = "/cloud/wyfw/wyHouseNeedApp/count"

            /**
             * 房源需求管理详情
             */
            const val GET_HOUSE_NEED_DETAIL = "/cloud/wyfw/wyHouseNeedApp/detail"

            /**
             * 房源需求管理分页列表
             */
            const val POST_HOUSE_NEED_LIST = "/cloud/wyfw/wyHouseNeedApp/queryPageList"

            /**
             * 考勤打卡 详情
             */
            const val GET_CHECK_DETAIL = "/cloud/xtoa/oaAttendanceClockInApp/detail"

            /**
             * 考勤打卡 打卡
             */
            const val POST_CHECK_CLOCK_IN = "/cloud/xtoa/oaAttendanceClockInApp/clockIn"

            /**
             * 考勤 日统计
             */
            const val GET_DAILY_CLOCK_COUNT = "/cloud/xtoa/oaAttendanceClockInApp/dailyStatistics"

            /**
             * 考勤 周统计
             */
            const val GET_WEEKLY_CLOCK_COUNT = "/cloud/xtoa/oaAttendanceClockInApp/weeklyStatistics"

            /**
             * 考勤 月统计
             */
            const val GET_MONTH_CLOCK_COUNT = "/cloud/xtoa/oaAttendanceClockInApp/monthlyStatistics"

            /**
             * 规章制度 分页列表
             */
            const val POST_REGULATION_LIST = "/cloud/xtoa/oaRegulationApp/queryPageList"

            /**
             * 规章制度下载
             */
            const val GET_REGULATION_DETAIL = "/cloud/xtoa/oaRegulationApp/detail"

            /**
             * 规章制度分类树结构
             */
            const val GET_REGULATION_TREE_LIST = "/cloud/xtoa/oaRegulationApp/queryRegulationTypeTree"

            /**
             * 公告全部
             */
            const val POST_NOTICE_LIST = "/cloud/wyfw/wyMessageNotification/queryPageList"

            /**
             * 公告未读
             */
            const val POST_NOTICE_UNREAD_LIST = "/cloud/wyfw/wyNoticeApp/queryUnreadPageList"

            /**
             * 通知公告详情
             */
            const val GET_NOTICE_DETAIL = "/cloud/wyfw/wyNoticeApp/detail"

            /**
             * 通知公告任务详情
             */
            const val GET_NOTICE_TASK_DETAIL = "/cloud/wyfw/wyNoticeApp/detail"

            //================= 招商客户 start ===================//
            /**
             * 所属产业列表
             */
            const val GET_INDUSTRIAL = "/cloud/zsyz/attractInvestmentIntentionApp/enterprises/queryHyflList"

            /**
             * 省市级联列表
             */
            const val GET_CITY = "/cloud/zsyz/attractInvestmentIntentionApp/enterprises/queryProvinceCityCascaderList"

            /**
             * 靶向搜索列表
             */
            const val POST_GET_POINT_SEARCH_LIST = "/cloud/zsyz/attractInvestmentIntentionApp/enterprises/queryPageList"

            /**
             * 靶向搜索企业详情
             */
            const val POST_GET_POINT_SEARCH_DETAIL = "/cloud/zsyz/attractInvestmentIntentionApp/enterprises/baseInfo"

            /**
             * 靶向搜索添加意向
             */
            const val POST_ADD_INTENTION = "/cloud/zsyz/attractInvestmentIntentionApp/intention/add"

            /**
             * 意向管理分页查询
             */
            const val POST_GET_INTENT_ENTERPRISE_LIST = "/cloud/zsyz/attractInvestmentIntentionApp/intention/queryPageList"

            /**
             *意向管理删除
             */
            const val POST_DELETE_INTENT_ENTERPRISE = "/cloud/zsyz/attractInvestmentIntentionApp/intention/delete"

            /**
             * 申请项目
             */
            const val POST_ADD_INTENT_ENTERPRISE = "/cloud/zsyz/attractInvestmentIntentionApp/intention/transfer/investment"

            /**
             * 完善意向
             */
            const val POST_INTENTION_EDIT = "/cloud/zsyz/attractInvestmentIntentionApp/intention/edit"

            /**
             * 意向企业详情
             */
            const val POST_GET_INTENT_ENTERPRISE_DETAIL = "/cloud/zsyz/attractInvestmentIntentionApp/intention/detail"

            /**
             * 意向企业 我的申请列表
             */
            const val POST_MY_APPLY_QUERY_LIST = "/cloud/zsyz/attractInvestmentIntentionApp/approved/my/queryPageList"

            //================= 招商客户 end ===================//

            //================= 访客管理 start =================//
            /**
             * 访客申请记录
             */
            const val POST_QUERY_VISITOR_APPLY_RECORDS = "/cloud/yqfw/visitorsToApplyApp/queryRecordPageList"

            /**
             * 访客申请详情
             */
            const val GET_VISITOR_APPLY_DETAIL = "/cloud/yqfw/visitorsToApplyApp/detail"

            /**
             * 访客分析
             */
            const val POST_VISITOR_ANALYSIS = "/cloud/yqfw/visitorsToApplyApp/visitorAnalysis"
            //================= 访客管理 end =================//

            //================= 安保巡视 start =================//
            /**
             * 开始巡视
             */
            const val POST_START_PATROL = "/cloud/wyfw/wyPatrolRecordApp/start"

            /**
             * 结束巡视
             */
            const val POST_END_PATROL = "/cloud/wyfw/wyPatrolRecordApp/end"

            /**
             * 巡视记录
             */
            const val POST_QUERY_PATROL_LIST = "/cloud/wyfw/wyPatrolRecordApp/queryPageList"

            /**
             * 巡视详情
             */
            const val GET_PATROL_DETAIL = "/cloud/wyfw/wyPatrolRecordApp/detail"
            //================= 安保巡视 end =================//

            /**
             * app菜单
             */
            const val GET_APP_USER_MENU = "/cloud/auth/app/getAppMenu"

            /**
             * app我的菜单
             */
            const val GET_APP_COLLECTED_MENU = "/cloud/auth/app/getCollectMenu"

            /**
             * 添加我的菜单
             */
            const val POST_SAVE_MINE_MENU = "/cloud/auth/app/addCollect"

            //================= 巡检 start =================//
            /**
             * 巡检任务
             */
            const val GET_PATROL_TASK_LIST = "/cloud/wyfw/wyInspectionApp/queryTaskPageList"

            /**
             * 巡检任务详情
             */
            const val GET_PATROL_TASK_DETAIL = "/cloud/wyfw/wyInspectionApp/detailTask"

            /**
             * 巡检记录
             */
            const val GET_PATROL_RECORDS_LIST = "/cloud/wyfw/wyInspectionApp/queryRecordPageList"

            /**
             * 巡检记录详情
             */
            const val GET_PATROL_RECORD_DETAIL = "/cloud/wyfw/wyInspectionApp/detailRecord"

            /**
             * 巡检添加
             */
            const val POST_PATROL_RECORD_ADD = "/cloud/wyfw/wyInspectionApp/add"

            /**
             * 获取设备类型
             */
            const val GET_EQUIPMENT_TYPE_LIST = "/cloud/iot/device/getTypeList"

            /**
             * 获取设备
             */
            const val GET_EQUIPMENT_LIST = "/cloud/iot/device/getInfoListByTypeId"
            //================= 巡检 end =================//
        }
    }
}