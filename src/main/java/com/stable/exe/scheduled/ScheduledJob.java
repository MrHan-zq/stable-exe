//package com.stable.exe.scheduled;
//
//import jakarta.annotation.Resource;
//import lombok.extern.slf4j.Slf4j;
//import com.stable.exe.service.ModelService;
//import com.stable.exe.utils.CommonUtil;
//import com.stable.exe.web.response.UserInfoResponse;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.text.ParsePosition;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//@Slf4j
//@Component
//public class ScheduledJob {
//
//    @Resource
//    private ModelService modelService;
//
//    @Scheduled(cron = "0 0/5 * * * ?")
//    public void cacheUserInfoJob(){
//        log.info("开始执行用户缓存清理");
//        log.info("*************************************************");
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        String currDate = sdf.format(new Date());
//        long time = sdf.parse(currDate, new ParsePosition(0)).getTime();
//        for (String key : CommonUtil.TOKEN_CAHCE.keySet()) {
//            Date activityTime = ((UserInfoResponse) CommonUtil.TOKEN_CAHCE.get(key)).getActivityTime();
//            if (time-activityTime.getTime()>30*1000*60){
//                CommonUtil.TOKEN_CAHCE.remove(key);
//            }
//        }
//        log.info("用户缓存清理执行完毕");
//    }
//
//    @Scheduled(cron = "0 0 0/1 * * ? ")
//    public void userInfoSdModelMapping(){
//        log.info("开始执行用户SD-MODEL关系映射数据清理");
//        log.info("*************************************************");
//        modelService.clearUserModelMapping();
//        log.info("用户SD-MODEL关系映射数据清理执行完毕");
//    }
//
//}
