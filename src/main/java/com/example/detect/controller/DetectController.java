package com.example.detect.controller;

import com.alibaba.fastjson.JSON;
import com.example.detect.entity.DetectRecord;
import com.example.detect.entity.DetectRequest;
import com.example.detect.service.DetectRecordService;
import com.example.detect.service.DetectRequestService;
import com.example.detect.utils.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@RestController
@Api(tags = "检测相关接口")
public class DetectController {

    @Resource
    private DetectRecordService service;

    @Resource
    private DetectRequestService requestService;

    /**
     * 现场检测：
     *      点击现场检测，弹出检测记录填写，填写后，点击确认提交
     *          点击后检测状态由待检测变为检测中
     *      需要的参数：
     *          1. Integer projectId   --->   委托单号
     *          2. String description --->   检测描述
     *          3. Integer detectPersonId  --->   检测人员Id
     *          4. Object image  --->    图片附件
     * 继续检测：
     *      点击继续检测，弹出检测记录填写，填写后，点击确认提交
     *         点击后检测状态为检测中
     * @return
     */
    @Transactional
    @ApiOperation("增加检测记录接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "detectPersonName", value = "检测人员姓名"),
            @ApiImplicitParam(name = "file", value = "图片附件"),
            @ApiImplicitParam(name = "description", value = "检测描述"),
            @ApiImplicitParam(name = "projectId", value = "委托单号")
    })
    @PostMapping("/addRecord")
    public String addRecord(@Param("detectPersonName") String detectPersonName,
                            @Param("file")MultipartFile file,
                            @Param("description") String description,
                            @Param("projectId")Integer projectId) throws IOException {

        String date = DateUtil.dateFormat();
        BASE64Encoder encoder = new BASE64Encoder();
        String image = encoder.encode(file.getBytes());

        DetectRecord record = new DetectRecord();
        record.setDate(date);
        record.setDescription(description);
        record.setProjectId(projectId);
        record.setImage(image);
        record.setDetectPersonName(detectPersonName);
        // 添加检测记录
        int addResult = service.addDetectRecord(record);
        // 更新检测状态
        int updateResult = requestService.updateStatusToDetecting(projectId);
        // 更新最后检测日期
        int updateDate = requestService.updateDetectDateByProjectId(projectId, date);

        if (addResult == 1 && updateResult == 1 && updateDate == 1) {
            return "添加成功！状态更改成功！最近检测时间更改成功！";
        } else {
            return "添加失败！";
        }
    }

    /*
    @PostMapping("/continueDetect")
    @ApiOperation("继续检测")
    public String continueDetect(@Param("detectPersonName") String detectPersonName,
                                 @Param("file")MultipartFile file,
                                 @Param("description") String description,
                                 @Param("projectId")Integer projectId) throws IOException {
        BASE64Encoder encoder = new BASE64Encoder();
        String image = encoder.encode(file.getBytes());

        DetectRecord record = new DetectRecord();
        record.setDate(DateUtil.dateFormat());
        record.setDetectPersonName(detectPersonName);
        record.setDescription(description);
        record.setProjectId(projectId);
        record.setImage(image);
        int i = service.addDetectRecord(record);
        if (i == 1) {
            return "添加成功！";
        } else {
            return "添加失败";
        }
    }*/

    /**
     * 完成检测：
     *      若检测完成，可点击完成检测
     *          检测状态更改为检测完毕(按钮变成灰色)
     *          检测日期更改为完成时间
     * @param id
     * @return
     */
    @ApiOperation("完成检测接口")
    @ApiImplicitParam(name = "id", value = "委托单号")
    @PostMapping("/completeDetect")
    public String completeDetect(@Param("id") Integer id) {
        String date = DateUtil.dateFormat();
        int completeResult = requestService.updateStatusToComplete(id);
        int updateResult = requestService.updateDetectDateByProjectId(id, date);
        if (completeResult == 1 && updateResult == 1) {
            return "检测已完成！检测日期已更改为最新日期";
        } else {
            return "请求失败";
        }
    }

    /**
     * 获取检测状态
     *      1： 待检测
     *      2： 检测中
     *      3： 检测完成
     * @param id
     * @return
     */
    @ApiOperation("获取检测状态接口")
    @ApiImplicitParam(name = "id", value = "委托单号")
    @PostMapping("/getDataStatus")
    public String getDataStatus(@Param("id") Integer id) {
        int i = requestService.selectDataStatusById(id);
        if (i == 1) {
            return "检测状态为：待检测";
        } else if (i == 2) {
            return "检测状态为：已检测";
        } else {
            return "检测状态为：检测完毕";
        }
    }

    /**
     * 获取信息：
     *      检测人员id  ---->  detectPersonId
     *      委托单号 ---> projectId
     *      该委托单号projectId的所有检测记录records
     */
    @PostMapping("/getRecordByProjectId")
    @ApiOperation("获取检测记录接口")
    @ResponseBody
    public String getRecordByProjectId(Integer id) {
        List<DetectRecord> detectRecords = service.selectRecordByProjectId(id);
        if (detectRecords != null) {
            return JSON.toJSONString(detectRecords);
        } else {
            return "没有数据返回！";
        }
    }

    /**
     * 根据条件 ：
     *      1：待检测
     *      2：检测中
     *      3：检测完成
     *      返回工程信息
     */
    @PostMapping("/getRequestInfoByStatus")
    @ApiOperation("获取工程信息接口")
    @ApiImplicitParam(name = "dataStatus", value = "检测状态")
    public String getRequestInfoByStatus(@Param("dataStatus") Integer dataStatus) {
        List<DetectRequest> detectRequests = requestService.selectDetectRequestByStatus(dataStatus);
        if (detectRequests.size() != 0) {
            return JSON.toJSONString(detectRequests);
        } else {
            return "没有返回数据！";
        }
    }
}
