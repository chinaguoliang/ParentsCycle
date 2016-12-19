package com.jgkj.parentscycle.global;

/**
 * Created by chen on 16/7/5.
 */
public class BgGlobal {
    public static final String IMG_SERVER_PRE_URL = "http://obb849fep.bkt.clouddn.com/";  //七牛服务器地址
    public static final String BASE_URL = "http://123.206.43.102:8080/support/";  //  自己的服务器地址
    public static final String CHEN_BASE_URL = "http://123.206.43.102:9999/";  //远端服务器地址
//    public static final String CHEN_BASE_URL = "http://192.168.1.102:8080/";  //本地服务器地址











    public static final String DEVICE_PARAMS = "";
    public static final String REGISTER_URL = "jgkj/regtuser";
    public static final String VERIFY_PHONE_NUMBER_URL = "jgkj/getCode";
    public static final String RESET_PASSWORD = "jgkj/jgeditPass"; //找回密码
    public static final String CHECK_RESET_PASSWORD = "jgkj/jgkjGetPassCheck";  //找回密码校验
    public static final String LOGIN_URL = "jgkj/loginUser";
    public static final String TEACHER_INFO_SAVE = "jgkj/teacherinfoSave"; //教师完善个人信息
    public static final String TEACHER_INFO_LIST = "teacherinfo/getTeacherinfo";  //教师个人中心

    public static final String CREATE_CLASS = "classinfo/save";  //建立班级

    public static final String MODIFY_TEACHER_PERMISSION = "schoolTeacher/schoolTeacherUpdate";  //修改教师权限
    public static final String CLASS_TEACHER_MANGEMENT = "classTeacher/updateClassinfoList"; //班级教师管理
    public static final String ANNOUNCEMENT_PUBLISH = "announcement/save"; //公告发布
    public static final String SEARCH_CLASS_LIST_BY_SCHOOL_ID = "classinfo/getClassList";
    public static final String ANNOUNCEMENT_LIST = "announcement/announcementList"; //公告列表
    public static final String ANNOUNCEMENT_COMMENT_SAVE = "announcementComment/save"; //公告评论保存
    public static final String MODIFY_PASS_BY_OLD_PASS = "jgkj/jgeditOldPass";  //根据旧密码修改新密码
    public static final String MODIFY_PHONE_BY_OLD_PHONE = "jgkj/jgeditOldPhone";//根据旧手机号修改手机号
    public static final String GET_SEVEN_COW_TOKEN = "jgkj/getQiniuToken"; //获得七牛上传token
    public static final String PUBLIC_ARTICLE_DISTRIBUCTION = "commonZfLog/save";  //所有文章公共转发
    public static final String SET_GOOD = "commonDzLog/save";//所有点赞公共接口
    public static final String PUBLISH_COURSE = "coursetime/save";//coursetime/save
    public static final String PUBLISH_FOOD_LIST = "food/save"; // 食谱发布
    public static final String PARENTS_CYCLE_SEND_ARTICLE = "parentTopic/save"; //父母圈发帖
    public static final String PERFECT_PARENTS_INFO = "jgkj/ParentinfoSave";//家长完善个人信息
    public static final String PERFECT_BYBY_INFO = "babyinfo/save";//宝宝信息完善
    public static final String PUBLISH_BABY_GROW_RECORD = "babyGrowthRecord/save"; //宝宝成长记录发布
    public static final String BABY_LIST = "babyinfo/babyinfoList"; //宝宝列表
    public static final String BABY_ASK_LEAVE_ADD = "babyAskforLeave/save"; //家长版-宝宝请假添加     (此接口教师也可以使用，看具体需求，如果是签到直接改变类型，内容可不传)
    public static final String ASK_FOR_LEAVE_MODIFY_FOR_TEACHER_VERSION = "babyAskforLeave/update"; // 教师版请假修改
    public static final String CHECK_ATTENDANCE = "babyAskforLeave/babyAskforLeaveList"; //考勤日历 颜色列表  根据类型显示 各种颜色
    public static final String TEACHERS_LIST = "teacherinfo/getTeacherinfoList";  //教师列表

    public static final String QUERY_FOOD_LIST = "food/foodList";  // 查询食谱列表
    public static final String QUERY_COURSE_LIST = "coursetime/coursetimeList"; // 查询课程列表

    public static final String ANNOUNCEMENT_DETAIL = "announcementComment/announcementCommentList"; //公告评论列表

    public static final String GET_VIDEO_CONTROL_DATA = "videoTime/getVideoControlTime";
    public static final String SAVE_VIDEO_CONTROL_DATA = "videoTime/saveVideoControlTime"; //保存视频控制时间
    public static final String GET_VIDEO_CONTROL_DATA_BY_SCHOOL_ID = "videoTime/getVideoControlTimeBySchoolId";//根据SchoolId获取视频控制时间

    public static final String ANNOUNCEMENT_COMMENT_LIST = "announcementComment/announcementCommentList"; //公告评论列表
    public static final String PARENTS_CYCLE_POSTS_LIST = "parentTopic/parentTopicList"; //父母圈帖子列表查询
    public static final String COMMENTS_LIST = "tocipComment/tocipCommentList";//帖子評論列表
    public static final String COMMENTS_SAVE = "tocipComment/save";//帖子評論列表


    public static final String QUERY_SCHOOLS_LIST = "schoolMGController/querySchoolList";//获取学校列表
    public static final String GET_BABY_GROWTH_RECORD_LIST = "babyGrowthRecord/babyGrowthRecordList";//宝宝成长记录查询列表
    public static final String QUERY_SCHOOL_LIST_BY_SCHOOL_NAME = "schoolMGController/querySchoolSerchList"; //学校查询列表
    public static final String PARENTS_LIST_INFO = "parentinfo/parentinfoList"; //家长信息列表
    public static final String PARENTS_INFO_AUDIT = "parentinfo/updateParentinfo"; //家长信息审核验证

    public static final String TEACHER_INFO_MODIFY_OR_EXAMINE = "teacherinfo/updateTeacherinfo";//教师信息修改或者审核
    public static final String BABY_JOIN_CLASS = "classBaby/save"; //宝宝加入班级
    public static final String SCHOOL_LEADER_EXAMINE_TEACHER_JOIN_SHCOOL = "schoolTeacher/schoolTeacherUpdate";//园长审核老师加入学校
    public static final String TEACHER_EXAMINE_STUDENT_JOIN_CLASS = "classBaby/update";//老师审核学生加入班级
    public static final String SCHOOL_LEADER_EXAMINE_TEACHER_JOIN_CLASS = "classTeacher/verifyClassinfo";//园长审核老师加入班级
}
