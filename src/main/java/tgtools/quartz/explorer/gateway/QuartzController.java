package tgtools.quartz.explorer.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tgtools.quartz.explorer.entity.TaskDO;
import tgtools.quartz.explorer.service.JobService;
import tgtools.web.entity.GridData;
import tgtools.web.entity.ResposeData;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 田径
 * @Title
 * @Description
 * @date 18:39
 */
@RequestMapping("/quartz/explorer/manage/quartz")
public class QuartzController {

    @Autowired
    private JobService taskScheduleJobService;

    @RequestMapping(value = "/get", method = {RequestMethod.GET})
    @ResponseBody
    public ResposeData get(@RequestParam("id") int pIndex) throws IOException {
        ResposeData entity = new ResposeData();
        Map<String, Object> param = new HashMap<String, Object>();
        TaskDO taskScheduleJob = taskScheduleJobService.get((long) pIndex);
        entity.setSuccess(true);
        entity.setData(taskScheduleJob);
        return entity;
    }

    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    @ResponseBody
    public GridData list(@RequestParam("pageIndex") int pIndex, @RequestParam("pageSize") int pPageSize) throws IOException {
        GridData entity = new GridData();
        pIndex = 0;
        pPageSize = Integer.MAX_VALUE - 1;
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("offset", pIndex * pPageSize);
        param.put("limit", pPageSize);
        List<TaskDO> taskScheduleJobList = taskScheduleJobService.list(param);
        int total = taskScheduleJobService.count(param);
        entity.setTotalRows(total);
        entity.setData(taskScheduleJobList);
        return entity;
    }

    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    @ResponseBody
    public ResposeData save(@RequestBody TaskDO taskScheduleJob) throws IOException {
        ResposeData result = new ResposeData();
        result.setSuccess(false);
        result.setData("操作失败");
        if (taskScheduleJobService.save(taskScheduleJob) > 0) {
            result.setSuccess(true);
            result.setData("操作成功");
            return result;
        }
        return result;
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    @ResponseBody
    public ResposeData update(@RequestBody TaskDO taskScheduleJob) throws IOException {
        ResposeData result = new ResposeData();
        result.setSuccess(false);
        result.setData("操作失败");
        if (taskScheduleJobService.update(taskScheduleJob) > 0) {
            result.setSuccess(true);
            result.setData("操作成功");
            return result;
        }
        return result;
    }

    @RequestMapping(value = "/remove", method = {RequestMethod.POST})
    @ResponseBody
    public ResposeData remove(@RequestBody HashMap<String, Object> params) throws IOException {
        Integer id = (Integer) params.get("id");
        ResposeData result = new ResposeData();
        result.setSuccess(false);
        result.setData("操作失败");
        if (taskScheduleJobService.remove((long) id) > 0) {
            result.setSuccess(true);
            result.setData("操作成功");
            return result;
        }
        return result;
    }

    @RequestMapping(value = "/changeJobStatus", method = {RequestMethod.POST})
    @ResponseBody
    public ResposeData changeJobStatus(@RequestBody HashMap<String, Object> params) throws IOException {
        Integer id = (Integer) params.get("id");
        String cmd = (String) params.get("cmd");

        ResposeData result = new ResposeData();
        result.setSuccess(false);
        result.setData("操作失败");


        String label = "停止";
        if ("start".equals(cmd)) {
            label = "启动";
        } else {
            label = "停止";
        }
        try {
            taskScheduleJobService.changeStatus((long) id, cmd);
            result.setSuccess(true);
            result.setData("任务" + label + "成功");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.setData("任务" + label + "失败");
        return result;
    }

}
