package com.example.detect.controller;

import com.example.detect.entity.DetectRecord;
import com.example.detect.entity.DetectRequest;
import com.example.detect.service.DetectRecordService;
import com.example.detect.service.DetectRequestService;
import com.example.detect.utils.DateUtil;
import com.example.detect.utils.Result;
import io.swagger.annotations.*;
import lombok.Cleanup;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
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
    public Result addRecord(@Param("detectPersonName") String detectPersonName,
                            @Param("file") MultipartFile file,
                            @Param("description") String description,
                            @Param("projectId") Integer projectId,
                            @Param("longitude") BigDecimal longitude,
                            @Param("latitude") BigDecimal latitude) {

        String date = DateUtil.dateFormat();
        BASE64Encoder encoder = new BASE64Encoder();
        DetectRecord record = new DetectRecord();
        Result result = new Result();
        String image = "";

        try {
            if (file != null) {
                image = encoder.encode(file.getBytes());
            }
            record.setDate(date);
            record.setDescription(description);
            record.setProjectId(projectId);
            record.setImage(image);
            record.setDetectPersonName(detectPersonName);
            record.setLongitude(longitude);
            record.setLatitude(latitude);
            // 添加检测记录
            int addResult = service.addDetectRecord(record);

            if (addResult == 1) {
                result.setCode(200);
                result.setMsg("添加检测成功");
                result.setData(record);
                // 更新检测状态
                int updateResult = requestService.updateStatusToDetecting(projectId);
                if (updateResult == 1) {
                    result.setMsg("添加检测成功，更新检测状态成功");
                    // 更新最后检测日期
                    int updateDate = requestService.updateDetectDateByProjectId(projectId, date);
                    if (updateDate == 1) {
                        result.setMsg("添加检测成功，更新检测状态成功，更新最后检测日期成功");
                    }
                }
            } else {
                result.setCode(404);
                result.setMsg("添加检测失败");
            }
        } catch (Exception e) {
            result.setCode(-1);
            result.setMsg(e.getMessage());
        }
        return result;
    }

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
    @ResponseBody
    public Result completeDetect(@Param("id") Integer id) {

        Result result = new Result();
        try {
            int completeResult = requestService.updateStatusToComplete(id);
            if (completeResult == 1) {
                result.setCode(200);
                result.setMsg("检测已完成");
            } else {
                result.setCode(404);
                result.setMsg("请求失败");
            }
        } catch (Exception e) {
            result.setCode(-1);
            result.setMsg(e.getMessage());
        }
        return result;
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
    @GetMapping("/getDataStatus")
    @ResponseBody
    public Result getDataStatus(@Param("id") Integer id) {

        Result result = new Result();
        try {
            int i = requestService.selectDataStatusById(id);
            result.setCode(i);
            if (i == 1) {
                result.setMsg("待检测");
            } else if (i == 2) {
                result.setMsg("检测中");
            } else if (i == 3){
                result.setMsg("检测完毕");
            } else {
                result.setCode(404);
                result.setMsg("请求失败");
            }
        } catch (Exception e) {
            result.setCode(-1);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 获取信息：
     *      检测人员id  ---->  detectPersonId
     *      委托单号 ---> projectId
     *      该委托单号projectId的所有检测记录records
     */
    @GetMapping("/getRecordByProjectId")
    @ApiOperation("获取检测记录接口")
    @ApiModelProperty(name = "id", value = "委托单号")
    @ResponseBody
    public Result getRecordByProjectId(Integer id) {

        Result result = new Result();
        try {
            List<DetectRecord> detectRecords = service.selectRecordByProjectId(id);
            if (detectRecords.size() != 0) {
                result.setCode(200);
                result.setMsg("请求成功");
                result.setData(detectRecords);
            } else {
                result.setCode(404);
                result.setMsg("请求失败");
            }
        } catch (Exception e) {
            result.setCode(-1);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 根据条件 ：
     *      1：待检测
     *      2：检测中
     *      3：检测完成
     *      返回工程信息
     */
    @GetMapping("/getRequestInfoByStatus")
    @ApiOperation("获取工程信息接口")
    @ResponseBody
    @ApiImplicitParam(name = "dataStatus", value = "检测状态")
    public Result getRequestInfoByStatus(@Param("dataStatus") Integer dataStatus) {

        Result result = new Result();
        try {
            List<DetectRequest> detectRequests = requestService.selectDetectRequestByStatus(dataStatus);
            if (detectRequests.size() != 0) {
                result.setCode(200);
                result.setMsg("请求成功");
                result.setData(detectRequests);
            } else {
                result.setCode(404);
                result.setMsg("请求失败");
            }
        } catch (Exception e) {
            result.setCode(-1);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 根据检测记录id，获取上传的图片附件
     */
    @GetMapping(value = "/getImgById", produces = "image/jpeg")
    @ApiOperation("获取图片附件接口")
    @ApiModelProperty(name = "id", value = "检测记录id")
    @ResponseBody
    public Result getImgById(HttpServletResponse response, @Param("id") Integer id) {

        Result result = new Result();
        BASE64Decoder decoder = new BASE64Decoder();

        try {
            byte[] bytes = (byte[]) service.getImageByRecordId(id);
            if (bytes.length != 0) {
                String data = new String(bytes, StandardCharsets.UTF_8);
                byte[] b = decoder.decodeBuffer(data);
                for (int i = 0; i < b.length; ++i) {
                    if (b[i] < 0) {
                        b[i] += 256;
                    }
                }
                response.setContentType("image/jpeg");
                @Cleanup ServletOutputStream out = response.getOutputStream();
                out.write(b);
                out.flush();
                result.setCode(200);
                result.setMsg("请求成功");
            } else {
                result.setCode(404);
                result.setMsg("请求失败");
            }
        } catch (Exception e) {
            result.setCode(-1);
            result.setMsg(e.getMessage());
        }
        return result;
    }
}
