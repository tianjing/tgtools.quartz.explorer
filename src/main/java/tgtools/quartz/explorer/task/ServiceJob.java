package tgtools.quartz.explorer.task;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import tgtools.plugin.util.JARLoader;
import tgtools.service.BaseService;
import tgtools.tasks.Task;
import tgtools.tasks.TaskContext;
import tgtools.util.LogHelper;

/**
 * @author 田径
 * @Title
 * @Description
 * @date 12:38
 */
public class ServiceJob implements Job {
    JARLoader jar = new JARLoader(ClassLoader.getSystemClassLoader());


    public void execute(JobExecutionContext context) throws JobExecutionException {
        String jobname = context.getJobDetail().getKey().getName();
        String path = tgtools.web.platform.Platform.getServerPath() + "Services/" + jobname;
        System.out.println("ServiceJob path:" + path);
        try {
            String className = (String) context.getJobDetail().getJobDataMap().get("SpringBean");
            jar.addPath(path);
            Class clazz = jar.loadClass(className);
            Object task = clazz.newInstance();
            tgtools.web.services.ServicesEntity entity = new tgtools.web.services.ServicesEntity();
            entity.setPATH(path);
            entity.setCLASSNAME(className);
            entity.setNAME(jobname);

            if (task instanceof BaseService) {
                BaseService service = (BaseService) task;
                TaskContext cont = new TaskContext();
                cont.put("info", entity);
                if (service.canRun()) {
                    service.run(cont);
                }
            } else if (task instanceof Task) {
                TaskContext cont = new TaskContext();
                cont.put("info", entity);
                ((Task) task).run(cont);
            }
        } catch (Throwable e) {
            LogHelper.error("", "服务执行失败;服务名称：" + jobname + ";原因：" + e.getMessage(), "ServiceJob", e);
        }
    }
}
