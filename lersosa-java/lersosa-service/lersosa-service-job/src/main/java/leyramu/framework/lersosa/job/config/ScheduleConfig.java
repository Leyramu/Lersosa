/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */
//
//package leyramu.framework.lersosa.job.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.quartz.SchedulerFactoryBean;
//
//import javax.sql.DataSource;
//import java.util.Properties;
//
/// **
// * 定时任务配置
// *
// * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
// * @version 1.0.0
// * @since 2024/10/23
// */
//@Configuration
//public class ScheduleConfig {
//
//    /**
//     * 定时任务数据源
//     */
//    @Bean
//    public SchedulerFactoryBean schedulerFactoryBean(DataSource dataSource) {
//        SchedulerFactoryBean factory = new SchedulerFactoryBean();
//        factory.setDataSource(dataSource);
//
//        Properties prop = new Properties();
//        prop.put("org.quartz.scheduler.instanceName", "LersosaScheduler");
//        prop.put("org.quartz.scheduler.instanceId", "AUTO");
//        prop.put("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
//        prop.put("org.quartz.threadPool.threadCount", "20");
//        prop.put("org.quartz.threadPool.threadPriority", "5");
//        prop.put("org.quartz.jobStore.class", "org.springframework.scheduling.quartz.LocalDataSourceJobStore");
//        prop.put("org.quartz.jobStore.isClustered", "true");
//        prop.put("org.quartz.jobStore.clusterCheckinInterval", "15000");
//        prop.put("org.quartz.jobStore.maxMisfiresToHandleAtATime", "10");
//        prop.put("org.quartz.jobStore.txIsolationLevelSerializable", "true");
//
//        prop.put("org.quartz.jobStore.misfireThreshold", "12000");
//        prop.put("org.quartz.jobStore.tablePrefix", "QRTZ_");
//        factory.setQuartzProperties(prop);
//
//        factory.setSchedulerName("LersosaScheduler");
//        factory.setStartupDelay(1);
//        factory.setApplicationContextSchedulerContextKey("applicationContextKey");
//        factory.setOverwriteExistingJobs(true);
//        factory.setAutoStartup(true);
//
//        return factory;
//    }
//}
