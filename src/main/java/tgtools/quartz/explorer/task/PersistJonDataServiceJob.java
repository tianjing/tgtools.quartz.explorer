package tgtools.quartz.explorer.task;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.PersistJobDataAfterExecution;

/**
 * @author 田径
 * @Title
 * @Description
 * @date 12:38
 */
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public class PersistJonDataServiceJob extends ServiceJob {
 }
