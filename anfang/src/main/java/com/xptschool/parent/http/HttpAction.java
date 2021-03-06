package com.xptschool.parent.http;

import com.xptschool.parent.BuildConfig;

/**
 * Created by Administrator on 2016/11/2.
 */
public class HttpAction {

    public static final int SUCCESS = 1;
    public static final int FAILED = 0;
    public static final String ACTION_LOGIN = "用户未登录请先登录";
    public static final String TOKEN_LOSE = "接口访问不合法";

    public static String Index = BuildConfig.SERVICE_URL + "/index.php";
    public static String HEAD = BuildConfig.SERVICE_URL + "/index.php/Api/";
    /*登录*/
    public static String LOGIN = HEAD + "Login/login2_1_3";

    /*修改用户信息*/
    public static String UserInfo = HEAD + "UserInfo/updateUserInfo";

    /*更改密码*/
    public static String UPDATE_PASSWORD = HEAD + "Login/edit_password";

    public static String GetClass = HEAD + "Comment/get_class";
    public static String GetCourse = HEAD + "Comment/get_course";
    public static String GETExams = HEAD + "Comment/get_exam_name";

    //广告位
    public static String HOME_Banner = HEAD + "Banner/query";
    public static String SHOW_Banner = BuildConfig.SERVICE_URL + "/getadstatics.php";
    //获取分组数据
    public static String Home_GroupCfg = HEAD + "AppHomeset/titleQuery";
    /*获取推荐商品*/
    public static String GETHotGoods = HEAD + "HotSale/query";

    //家庭作业
    public static String HOMEWORK_QUERY = HEAD + "Homework/query";
    public static String HOMEWORK_DEL = HEAD + "Homework/delete";
    public static String HOMEWORK_ADD = HEAD + "Homework/add";
    public static String HOMEWORK_DETAIL = HEAD + "Homework/getDetail";

    //班级公告
    public static String NOTICE_QUERY = HEAD + "Notice/query";
    public static String NOTICE_DEL = HEAD + "Notice/delete";
    public static String NOTICE_ADD = HEAD + "Notice/add";
    public static String NOTICE_DETAIL = HEAD + "Notice/getDetail";

    public static String GET_TEACHER_BYCID = HEAD + "Comment/get_teacher_list";

    //考试名称查询
    public static String EXAM_QUERY_FOR_PARENT = HEAD + "Exam/getScoresForParent";
    public static String EXAM_QUERY = HEAD + "Exam/query";

    //考勤管理
    public static String Attendance_QUERY = HEAD + "Attendance/query_v1_0_1";
    public static String Attendance_Detail = HEAD + "Attendance/getDetail";

    //请假管理
    public static String Leave_QUERY = HEAD + "Leave/query";
    public static String Leave_Edit = HEAD + "Leave/edit";
    public static String Leave_Add = HEAD + "Leave/add_leave";
    public static String Leave_Del = HEAD + "Leave/del";
    public static String Leave_Detail = HEAD + "Leave/getDetail";

    //课程表
    public static String Timetable_QUERY = HEAD + "Timetable/query";
    //荣誉墙
    //rw_type(荣誉类型),stu_id(家长查看孩子荣誉使用),c_id(教室查看班级学生荣誉使用)
    public static String Honor_query = HEAD + "Honor/query";
    public static String Honor_edit = HEAD + "Honor/edit";
    public static String Honor_detail = HEAD + "Honor/getDetail";

    //评语查询
    public static String Remark_query = HEAD + "Remark/query";
    public static String Remark_edit = HEAD + "Remark/edit";
    public static String Remark_detail = HEAD + "Remark/getDetail";

    //报警记录
    public static String Track_alarm = HEAD + "Track/alarmRecord_v1_0_1";
    //我的班级
    public static String MyClass_QUERY = HEAD + "MyClass/query";
    //我的学生
    public static String MyStudent_QUERY = HEAD + "MyStudent/query";
    //学生详情
    public static String MyStudent_Detail = HEAD + "MyStudent/view";
    //通讯录
    public static String MyContacts_ForParent = HEAD + "Contacts/getContactsForParent";
    public static String MyContacts_ForTeacher = HEAD + "Contacts/getContactsForTeacher";

    //位置
    public static String Track_RealTime = HEAD + "Track/realtimeLocation";
    public static String Track_HistoryTrack = HEAD + "Track/historyTrack";
    //围栏
    public static String Track_StudentFence = HEAD + "Track/studentFence";
    public static String Track_addStudentFence = HEAD + "Track/addStudentFence";
    public static String Track_deleteStudentFence = HEAD + "Track/deleteStudentFence";

    //报警处理
    public static String Track_Alarm_edit = HEAD + "Track/alarm_edit";
    public static String Track_Alarm_detail = HEAD + "Track/getDetail";

    public static String GetCard_Phone = HEAD + "StudentCard/get_cardphone";
    public static String SetCard_Phone = HEAD + "StudentCard/set_cardphone";

    //上传token
    public static String HOOK_PUSH_TOKEN = HEAD + "Hook/addPushToken";

    //添加监护人
    public static String ADD_TUTELAGE = HEAD + "AddGuardian/add";

    //钱包
    public static String Learning_Server = HEAD + "Learning/query";
    //零钱
    public static String POCKET_BALANCE = HEAD + "OrderBalance/queryAccount";
    //零钱明细记录
    public static String POCKET_BILLS = HEAD + "Order/getBillList";
    public static String POCKET_BILL_DETAIL = HEAD + "Order/getBillDetail";

    //提现
    public static String REFUND_ADD = HEAD + "UserRefund/addRefund";
    public static String REFUND_Query = HEAD + "UserRefund/query";

    public static String STU_CARD_ORDERID = HEAD + "OrderBalance/getCardOrderId";
    //学生卡充值
    public static String STU_CARD_RECHARGE = HEAD + "OrderBalance/onCardRecharge";

    //学生卡冻结/解冻
    public static String STU_CARD_FREEZE = HEAD + "OrderBalance/onFreezeChange";
    //学生卡消费记录
    public static String STU_CARD_BILL = HEAD + "OrderBalance/getStuCardBill";

    //获取订单信息
    public static String GET_OrderInfo = HEAD + "Order/getOrder";

    //银行卡
    public static String GET_BankCards = HEAD + "BankCard/query";
    public static String Add_BankCard = HEAD + "BankCard/add";
    public static String Delete_BankCard = HEAD + "BankCard/del";

    //手机话费充值
    public static String GET_TEL_RECHARGE_ORDER = HEAD + "TelCharge/getTelCharge";
    public static String TEL_RECHARGE = HEAD + "TelCharge/telTopUp";

    //密码找回
    public static String FORGOT_PWD_STEP1 = HEAD + "ForgetPwd/checkUser";
    public static String FORGOT_PWD_STEP2 = HEAD + "ForgetPwd/ForgotPassword";
    public static String FORGOT_PWD_STEP3 = HEAD + "ForgetPwd/checkCode";
    public static String FORGOT_PWD_STEP4 = HEAD + "ForgetPwd/PasswordReset";

    //注册获取验证码
    public static String REGISTER_GETCODE = HEAD + "Register/getCode";
    //注册
    public static String REGISTER = HEAD + "Register/register";

    //获取学校
    public static String GET_SCHOOL = HEAD + "Register/information";

    //获取商品列表
    public static String GET_SHOPLIST = HEAD + "Goods/getGoodsList";
    public static String GET_GOODDETAIL = HEAD + "Goods/getGoodsDetail";

    public static String GET_REFER = Index + "/Wap/Register/getReferrer";

    //获取新品新闻|理财咨询等
    public static String GET_MESSAGE = HEAD + "UserMessage/getMessageList";
    public static String GET_NEWS_DETAIL = HEAD + "UserMessage/getMessageDetail";

    public static String GET_NOTIFY_LIST = HEAD + "UserMessage/getNotifyList";

    //计步
    public static String GET_WATCH_STEP = HEAD + "Watch/GetStepNum";
    //关机
    public static String GET_WATCH_SHUTDOWN = HEAD + "Watch/SetShutDown";
    //碰碰好友列表
    public static String GET_WATCH_FRIENDLIST = HEAD + "Watch/friendList";
    //获取闹钟列表
    public static String GET_WATCH_ALARM_LIST = HEAD + "Watch/UpAlarmTime";
    //修改闹钟
    public static String GET_WATCH_ALARM_EDIT = HEAD + "Watch/SetAlarmTime";
    //远程监听
    public static String SET_WATCH_Monitor = HEAD + "Watch/SetMonitor";
    //绑定
    public static String GET_WATCH_Bind = HEAD + "Watch/BindDevice";
    //解绑设备
    public static String WATCH_UnBind = HEAD + "Watch/UnBindDevice";
    //发送微聊信息
    public static String POST_WCHAT_MESSAGE = HEAD + "ChatInfo/uploadMsg";
    //获取微聊信息
    public static String GET_UNREAD_MESSAGE = HEAD + "ChatInfo/getUnReadChatMsg";


}
