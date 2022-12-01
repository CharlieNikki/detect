package com.example.detect.controller;

import com.alibaba.fastjson.JSON;
import com.example.detect.entity.DetectRecord;
import com.example.detect.entity.DetectRequest;
import com.example.detect.service.DetectRecordService;
import com.example.detect.service.DetectRequestService;
import com.example.detect.utils.*;
import io.swagger.annotations.*;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

import static com.example.detect.constant.Sign.*;
import static com.example.detect.constant.Status.*;
import static com.example.detect.utils.ImageUtil.SAVE_IMAGE_PATH;
import static com.example.detect.utils.ImageUtil.getNewImagePath;

@RestController
@Api(tags = "检测相关接口")
public class DetectController {

    @Resource
    private DetectRecordService service;

    @Resource
    private DetectRequestService requestService;
    @SneakyThrows
    @Transactional
    @ApiOperation("增加检测记录接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "detectPersonId", value = "检测人员id"),
            @ApiImplicitParam(name = "file", value = "图片附件"),
            @ApiImplicitParam(name = "description", value = "检测描述"),
            @ApiImplicitParam(name = "projectId", value = "委托单号")
    })
    @PostMapping(value = "/addRecord")
    @ResponseBody
    public Result addRecord(@Param("detectPersonId") Integer detectPersonId,
                                        @RequestPart("file") MultipartFile[] files,
                                        @Param("description") String description,
                                        @Param("projectId") Integer projectId) {

        Result result = new Result();
        DetectRecord record = new DetectRecord();
        String newFileName;
        String date = DateUtil.dateFormat();

        if (files.length == 0) {
            result.setCode(RETURN_CODE_FAIL);
            result.setMsg(RETURN_MESSAGE_FAIL);
        } else {
            try {
                newFileName = FileUtil.fileDownload(files);
                if (!newFileName.equals(RETURN_MESSAGE_FAIL)) {
                    // 将数据存入数据库
                    record.setProjectId(projectId);
                    record.setDetectPersonId(detectPersonId);
                    record.setDescription(description);
                    record.setDate(date);
                    record.setImage(newFileName);
                    // 是否存入成功
                    int addResult = service.addDetectRecord(record);
                    // 存入成功
                    if (addResult == 1) {
                        // 更新request状态
                        int updateResult = requestService.updateDetectStatusAndDateByProjectId(projectId, date);
                        if (updateResult == 1) {
                            result.setCode(RETURN_CODE_SUCCESS);
                            result.setMsg(RETURN_MESSAGE_SUCCESS);
                        } else {
                            result.setCode(RETURN_CODE_FAIL);
                            result.setMsg(RETURN_MESSAGE_FAIL);
                        }
                    } else {
                        result.setCode(RETURN_CODE_FAIL);
                        result.setMsg("数据新增失败");
                    }
                } else {
                    result.setCode(RETURN_CODE_FAIL);
                    result.setMsg("图片上传失败");
                }
            } catch (Exception e) {
                result.setCode(SYSTEM_CODE_ERROR);
                result.setMsg(e.getMessage());
            }
        }
        return result;
    }

    /**
     * 更改检测记录
     */
    @PostMapping("/updateRecord")
    public Result updateRecord(@Param("projectId") Integer projectId,
                               @RequestPart("file") MultipartFile[] files,
                               @Param("description") String description) {

        DetectRecord record = new DetectRecord();
        Result result = new Result();

        if (files.length == 0) {
            result.setCode(RETURN_CODE_FAIL);
            result.setMsg(RETURN_MESSAGE_FAIL);
        } else {
            try {
                String imagesPAth = FileUtil.fileDownload(files);
                if (!imagesPAth.equals(RETURN_MESSAGE_FAIL)) {
                    record.setDate(DateUtil.dateFormat());
                    record.setProjectId(projectId);
                    record.setDescription(description);
                    record.setImage(imagesPAth);
                    int i = service.updateDetectRecord(record);
                    if (i == 1) {
                        result.setCode(RETURN_CODE_SUCCESS);
                        result.setMsg(RETURN_MESSAGE_SUCCESS);
                    } else {
                        result.setCode(RETURN_CODE_FAIL);
                        result.setMsg("数据更新失败");
                    }
                } else {
                    result.setCode(RETURN_CODE_FAIL);
                    result.setMsg("数据上传失败");
                }
            } catch (Exception e) {
                result.setCode(SYSTEM_CODE_ERROR);
                result.setMsg(e.getMessage());
            }
        }
        return result;
    }

    /**
     * 完成检测：
     *      若检测完成，可点击完成检测
     *          检测状态更改为检测完毕
     *          检测日期更改为完成时间
     * @return
     */
    @ApiOperation("完成检测接口")
    @ApiImplicitParam(name = "projectId", value = "委托单号")
    @PostMapping("/complete")
    @ResponseBody
    public Result completeDetect(@Param("projectId") Integer projectId) {

        Result result = new Result();
        try {
            // 获取该工程的检测状态
            int selectResult = requestService.selectDataStatusById(projectId);
            // 若检测状态不为：检测完毕，则更改检测状态
            if (selectResult != DETECT_STATUS_CODE_DETECTED) {
                int completeResult = requestService.updateStatusToComplete(projectId);
                if (completeResult == 1) {
                    result.setCode(RETURN_CODE_SUCCESS);
                    result.setMsg(RETURN_MESSAGE_SUCCESS);
                } else {
                    result.setCode(RETURN_CODE_FAIL);
                    result.setMsg(RETURN_MESSAGE_FAIL);
                }
            } else {
                // 检测状态已为：检测完毕，无需更改检测状态
                result.setCode(RETURN_CODE_INVALID_REQUEST);
                result.setMsg(RETURN_MESSAGE_INVALID_REQUEST);
            }

        } catch (Exception e) {
            result.setCode(SYSTEM_CODE_ERROR);
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
                result.setMsg(DETECT_STATUS_MESSAGE_NOT_DETECT);
            } else if (i == 2) {
                result.setMsg(DETECT_STATUS_MESSAGE_DETECTING);
            } else if (i == 3){
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
     * 获取信息：
     *      该委托单号projectId的检测记录record
     */
    @GetMapping("/getRecordsByProjectId")
    @ApiOperation("获取检测记录接口")
    @ApiModelProperty(name = "projectId", value = "委托单号")
    @ResponseBody
    public Result getRecordsByProjectId(@Param("projectId") Integer projectId) {

        Map<String,Object> returnMap = new HashMap<>();
        List<String> imagesList;
        Result result = new Result();

        try {
            DetectRecord detectRecords = service.selectRecordByProjectId(projectId);
            if (detectRecords != null) {

                imagesList = SplitByComma.splitStringByComma(detectRecords.getImage());
                result.setCode(RETURN_CODE_SUCCESS);
                result.setMsg(RETURN_MESSAGE_SUCCESS);
                returnMap.put("recordInfo", detectRecords);
                returnMap.put("imagesPath", imagesList);
                result.setData(returnMap);
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
     * 根据条件 ：
     *      1：待检测
     *      2：检测中
     *      3：检测完成
     *      返回工程信息
     */
    @GetMapping("/getRequestInfoByStatus")
    @ApiOperation("根据条件获取工程信息接口")
    @ResponseBody
    @ApiImplicitParam(name = "dataStatus", value = "检测状态")
    public Result getRequestInfoByStatus(@Param("dataStatus") Integer dataStatus) {

        Result result = new Result();
        try {
            List<DetectRequest> detectRequests = requestService.selectDetectRequestByStatus(dataStatus);
            if (detectRequests.size() != 0) {
                result.setCode(RETURN_CODE_SUCCESS);
                result.setMsg(RETURN_MESSAGE_SUCCESS);
                result.setData(detectRequests);
            } else {
                result.setCode(RETURN_CODE_FAIL);
                result.setMsg("没有对应的信息");
            }
        } catch (Exception e) {
            result.setCode(SYSTEM_CODE_ERROR);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 获取图片接口
     */
    @SneakyThrows
    @GetMapping(value = "/getImageUrlByProjectId", produces = "image/jpeg")
    @ResponseBody
    public byte[] getImageUrlByProjectId(String imagesPath) {

        String imageUrl = SAVE_IMAGE_PATH + imagesPath;
        File file = new File(imageUrl);

        @Cleanup FileInputStream fileInputStream = new FileInputStream(file);

        byte[] bytes = new byte[fileInputStream.available()];
        fileInputStream.read(bytes, 0, fileInputStream.available());
        return bytes;
    }

    /**
     * 根据检测记录id，获取上传的图片附件
     */
    @GetMapping(value = "/getImgById")
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
                result.setCode(RETURN_CODE_SUCCESS);
                result.setMsg(RETURN_MESSAGE_SUCCESS);
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
}
