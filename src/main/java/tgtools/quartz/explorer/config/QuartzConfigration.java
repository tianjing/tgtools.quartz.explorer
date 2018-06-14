package tgtools.quartz.explorer.config;


import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import tgtools.quartz.explorer.factory.JobFactory;
import tgtools.quartz.explorer.gateway.QuartzController;
import tgtools.quartz.explorer.gateway.ResourceController;
import tgtools.quartz.explorer.service.JobService;
import tgtools.quartz.explorer.utils.QuartzManager;
import tgtools.util.ReflectionUtil;
import tgtools.util.StringUtil;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Configuration
@MapperScan(basePackages = {"tgtools.quartz.explorer.dao"}, sqlSessionFactoryRef = "quartzSqlSessionFactory")
public class QuartzConfigration {


    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(@Qualifier("dataSource") DataSource dataSource) {

        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        try {
            schedulerFactoryBean.setOverwriteExistingJobs(true);
            schedulerFactoryBean.setQuartzProperties(quartzProperties());
            schedulerFactoryBean.setJobFactory(jobFactory());
            //schedulerFactoryBean.setDataSource(dataSource);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return schedulerFactoryBean;
    }

    @Bean
    public SqlSessionFactory quartzSqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {

        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        org.apache.ibatis.session.Configuration config = new org.apache.ibatis.session.Configuration();
        config.setMapUnderscoreToCamelCase(true);
        factoryBean.setConfiguration(config);
        factoryBean.setMapperLocations(new Resource[]{new UrlResource(ReflectionUtil.getResource("tgtools/quartz/explorer/mybatis/" + getMapperName()))});
        return factoryBean.getObject();

    }

    protected String getDataBaseType() {
        String dbtype = "";
        if (StringUtil.isNullOrEmpty(dbtype)) {
            if (null != tgtools.db.DataBaseFactory.getDefault()) {
                dbtype = tgtools.db.DataBaseFactory.getDefault().getDataBaseType();
            }
        }
        if(!"dm6".equals(dbtype)) {
            return StringUtil.EMPTY_STRING;
        }
        return dbtype;
    }

    protected String getMapperName() {
        String dbtype = getDataBaseType();
        if (StringUtil.isNullOrEmpty(dbtype)) {
            return "TaskMapper.xml";
        }
        return "TaskMapper_" + dbtype + ".xml";
    }

    @Bean
    public SqlSessionTemplate quartzSqlSessionTemplate(@Qualifier("quartzSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        SqlSessionTemplate template = new SqlSessionTemplate(sqlSessionFactory);
        return template;
    }

    // 指定quartz.properties
    @Bean
    public Properties quartzProperties() throws IOException {
        InputStream inputStream = null;
        try {
            PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
            Properties prop = new Properties();

            prop.load(ReflectionUtil.getResourceAsStream("tgtools/quartz/explorer/config/quartz.properties"));
            propertiesFactoryBean.setProperties(prop);
            propertiesFactoryBean.afterPropertiesSet();
            return propertiesFactoryBean.getObject();
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                }
            }
        }
    }


    @Bean
    public QuartzManager quartzManager() {
        return new QuartzManager();
    }

    // 创建schedule
    @Bean(name = "scheduler")
    public Scheduler scheduler(@Qualifier("dataSource") DataSource dataSource) {
        return schedulerFactoryBean(dataSource).getScheduler();
    }

    @Bean
    public JobService jobService() {
        return new JobService();
    }

    @Bean
    public JobFactory jobFactory() {
        return new JobFactory();
    }

    @Bean
    public QuartzController quartzController() {
        return new QuartzController();
    }

    @Bean
    public ResourceController quartzResourceController() {
        return new ResourceController();
    }


}
