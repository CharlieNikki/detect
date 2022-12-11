package com.example.detect.controller;

import com.alibaba.fastjson.JSON;
import com.example.detect.entity.DetectRecord;
import com.example.detect.entity.DetectRequest;
import com.example.detect.entity.Image;
import com.example.detect.entity.ReturnImage;
import com.example.detect.service.DetectRecordService;
import com.example.detect.service.DetectRequestService;
import com.example.detect.service.ImageService;
import com.example.detect.utils.*;
import io.swagger.annotations.*;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.example.detect.constant.Sign.*;
import static com.example.detect.constant.Status.*;
import static com.example.detect.utils.ImageUtil.SAVE_IMAGE_PATH;
import static com.example.detect.utils.ImageUtil.SAVE_IMAGE_RELATIVE_PATH;

@RestController
@Api(tags = "检测相关接口")
public class DetectController {

    @Resource
    private DetectRecordService service;

    @Resource
    private DetectRequestService requestService;

    @SneakyThrows
    @ApiOperation("增加检测记录接口")
    @PostMapping(value = "/addRecord")
    @ResponseBody
    public Result addRecord(DetectRecord record) {

        Result result = new Result();
        // 注入检测创建日期
        record.setDate(DateUtil.dateFormat());
        // 使用雪花算法注入检测id
        record.setId(String.valueOf(new SnowflakeIdWorker(0,0).nextId()));
        // 标识
        boolean isInsert = false;
        try {
            // 将检测信息存入数据库，并更改最新检测时间和数据状态
            isInsert = service.addDetectRecord(record);
        } catch (Exception e) {
            result.setResult(SYSTEM_CODE_ERROR, e.getMessage(), null, 0);
            e.printStackTrace();
        }
        if (isInsert) {
            // 存入成功
            result.setResult(RETURN_CODE_SUCCESS, RETURN_MESSAGE_SUCCESS, record, 1);
        } else {
            // 存入失败
            result.setResult(RETURN_CODE_FAIL, "数据库更新失败", null, 0);
        }
        return result;
    }

    /**
     * 更改检测记录
     */
    @PostMapping("/updateRecord")
    public Result updateRecord(DetectRecord record) {

        Result result = new Result();
        record.setDate(DateUtil.dateFormat());
        // 标识
        boolean isUpdate = false;

        try {
            // 更改检测记录
            isUpdate = service.updateDetectRecord(record);
        } catch (Exception e) {
            result.setResult(SYSTEM_CODE_ERROR, e.getMessage(), null, 0);
        }
        if (isUpdate) {
            // 更改成功
            result.setResult(RETURN_CODE_SUCCESS, RETURN_MESSAGE_SUCCESS, null, 1);
        } else {
            // 更改失败
            result.setResult(RETURN_CODE_FAIL, "数据库更新失败", null, 0);
        }
        return result;
    }

    /**
     * 完成检测
     */
    @ApiOperation("完成检测接口")
    @PostMapping("/complete")
    @ResponseBody
    public Result completeDetect(@RequestParam("projectId") String projectId) {

        Result result = new Result();
        boolean isComplete = false;
        try {
            // 将project状态更改为：检测完毕
            isComplete = service.completeDetect(projectId);
        } catch (Exception e) {
            result.setResult(SYSTEM_CODE_ERROR, e.getMessage(), null, 0);
        }
        if (isComplete) {
            // 更改状态成功
            result.setResult(RETURN_CODE_SUCCESS, RETURN_MESSAGE_SUCCESS, null, 1);
        } else {
            // 更改状态失败
            result.setResult(RETURN_CODE_FAIL, "数据库更新失败", null, 0);
        }
        return result;
    }

    /**
     * 根据id，获取检测状态
     */
    @ApiOperation("获取检测状态接口")
    @GetMapping("/getDataStatus")
    @ResponseBody
    public Result getDataStatus(@RequestParam("id") String id) {

        Result result = new Result();
        try {
            int i = requestService.selectDataStatusById(id);
            result.setCode(i);
            if (i == 1) {
                result.setMsg(DETECT_STATUS_MESSAGE_NOT_DETECT);
            } else if (i == 2) {
                result.setMsg(DETECT_STATUS_MESSAGE_DETECTING);
            } else if (i == 3) {
                result.setMsg(DETECT_STATUS_MESSAGE_DETECTED);
            } else {
                result.setCode(RETURN_CODE_FAIL);
                result.setMsg(RETURN_MESSAGE_FAIL);
            }
        } catch (Exception e) {
            result.setCode(SYSTEM_CODE_ERROR);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 根据projectId获取对应的检测信息(包括图片路径)
     */
    @GetMapping("/getRecordsByProjectId")
    @ApiOperation("获取检测记录接口")
    @ResponseBody
    public Result getRecordsByProjectId(@RequestParam("projectId") String projectId) {

        Map<String, Object> returnMap = new HashMap<>();
        Result result = new Result();

        try {
            returnMap = service.getRecordByProjectId(projectId);
        } catch (Exception e) {
            result.setResult(SYSTEM_CODE_ERROR, e.getMessage(), null, 0);
            e.printStackTrace();
        }
        if (!returnMap.isEmpty()) {
            // 返回的信息不为空
            result.setResult(RETURN_CODE_SUCCESS, RETURN_MESSAGE_SUCCESS, returnMap, returnMap.size());
        } else {
            // 返回的信息为空
            result.setResult(RETURN_CODE_FAIL, "没有对应的检测记录信息", returnMap, 0);
        }
        return result;
    }

    /**
     * 根据data_status返回工程信息
     */
    @GetMapping("/getRequestInfoByStatus")
    @ApiOperation("根据条件获取工程信息接口")
    @ResponseBody
    public Result getRequestInfoByStatus(@RequestParam("dataStatus") Integer dataStatus) {

        Result result = new Result();
        List<DetectRequest> detectRequests = null;
        try {
            detectRequests = requestService.selectDetectRequestByStatus(dataStatus);
        } catch (Exception e) {
            result.setResult(SYSTEM_CODE_ERROR, e.getMessage(), null, 0);
        }
        if (detectRequests.size() != 0) {
            result.setResult(RETURN_CODE_SUCCESS, RETURN_MESSAGE_SUCCESS, detectRequests, detectRequests.size());
        } else {
            result.setResult(RETURN_CODE_FAIL, "没有对应的信息", null, 0);
        }
        return result;
    }
}
