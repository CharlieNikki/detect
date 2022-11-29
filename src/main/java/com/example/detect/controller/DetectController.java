package com.example.detect.controller;

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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

import static com.example.detect.constant.Sign.*;
import static com.example.detect.constant.Status.*;
import static com.example.detect.utils.ImageUtil.SAVE_IMAGE_PATH;

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
        StringBuilder imagesPath = new StringBuilder();
        String imgPath = "";
        String newFileName = "";

        if (files.length == 0) {
            result.setCode(RETURN_CODE_FAIL);
            result.setMsg(RETURN_MESSAGE_FAIL);
        } else {
            try {
                for (MultipartFile multipartFile : files) {
                    // 获取文件后缀
                    String suffixName = ImageUtil.getImagePath(multipartFile);
                    System.out.println("suffixName:" + suffixName);
                    // 生成新文件的名称
                    newFileName = ImageUtil.getNewFileName(suffixName);
                    System.out.println("newFileName:" + newFileName);
                    System.out.println("SAVE_IMAGE_PATH:" + SAVE_IMAGE_PATH);
                    // 保存文件
                    File file = new File(ImageUtil.getNewImagePath(newFileName));

                    boolean state = ImageUtil.saveImage(multipartFile, file);
                    if (state) {
                        imgPath = String.valueOf(imagesPath.append(file.getAbsolutePath()).append(","));
                        System.out.println("==========================================");
                        System.out.println("imagesPath:" + imagesPath);
                        System.out.println("SAVE_IMAGE_PATH:" + SAVE_IMAGE_PATH);
                        System.out.println("newFileName:" + newFileName);
                    }
                }
            } catch (Exception e) {
                result.setCode(SYSTEM_CODE_ERROR);
                result.setMsg(e.getMessage());
            }
            record.setProjectId(projectId);
            record.setDetectPersonId(detectPersonId);
            record.setDescription(description);
            record.setDate(DateUtil.dateFormat());
            record.setImage(newFileName);
            int i = service.addDetectRecord(record);
            if (i == 1) {
                result.setCode(RETURN_CODE_SUCCESS);
                result.setMsg(RETURN_MESSAGE_SUCCESS);
            } else {
                result.setCode(RETURN_CODE_FAIL);
                result.setMsg(RETURN_MESSAGE_FAIL);
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
    @ApiModelProperty(name = "id", value = "委托单号")
    @ResponseBody
    public Result getRecordsByProjectId(@Param("projectId") Integer projectId) {

        Map<String,Object> returnMap = new HashMap<>();
        List<String> imagesList = new ArrayList<>();
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
                result.setMsg(RETURN_MESSAGE_FAIL);
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
    @GetMapping("/downloadImage")
    public void downloadImage(HttpServletResponse response, String imagePath) {
        FileUtil.downloadFile(response, imagePath);
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
