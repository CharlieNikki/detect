package com.example.detect.controller;

import com.example.detect.entity.DetectRecord;
import com.example.detect.entity.DetectRequest;
import com.example.detect.service.DetectRecordService;
import com.example.detect.service.DetectRequestService;
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
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
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

    @SneakyThrows
    @Transactional
    @ApiOperation("增加检测记录接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "detectPersonId", value = "检测人员id"),
            @ApiImplicitParam(name = "description", value = "检测描述"),
            @ApiImplicitParam(name = "projectId", value = "委托单号")
    })
    @PostMapping(value = "/addRecord")
    @ResponseBody
    public Result addRecord(@RequestParam("detectPersonId") Integer detectPersonId,
                            @RequestParam("description") String description,
                            @RequestParam("projectId") Integer projectId) {

        Result result = new Result();
        DetectRecord record = new DetectRecord();
        String date = DateUtil.dateFormat();
        try {
            record.setProjectId(projectId);
            record.setDetectPersonId(detectPersonId);
            record.setDescription(description);
            record.setDate(date);
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

        } catch (Exception e) {
            result.setCode(SYSTEM_CODE_ERROR);
            result.setMsg(e.getMessage());
        }

        return result;
    }

    /**
     * 更改检测记录
     */
    @PostMapping("/updateRecord")
    public Result updateRecord(@RequestParam("projectId") Integer projectId,
                               @RequestParam("description") String description) {

        DetectRecord record = new DetectRecord();
        Result result = new Result();

        try {
            record.setDate(DateUtil.dateFormat());
            record.setProjectId(projectId);
            record.setDescription(description);

            int i = service.updateDetectRecord(record);
            if (i == 1) {
                result.setCode(RETURN_CODE_SUCCESS);
                result.setMsg(RETURN_MESSAGE_SUCCESS);
            } else {
                result.setCode(RETURN_CODE_FAIL);
                result.setMsg("数据更新失败");
            }

        } catch (Exception e) {
            result.setCode(SYSTEM_CODE_ERROR);
            result.setMsg(e.getMessage());
        }

        return result;
    }

    /**
     * 根据projectId新增图片信息
     */
    @PostMapping("/addImageByProjectID")
    public Result addImageByProjectId(@RequestParam("projectId") Integer projectId,
                                      @RequestParam("file") MultipartFile file) {

        // 根据projectId获取检测记录信息
        DetectRecord record = service.selectRecordByProjectId(projectId);
        String newFileName;
        StringBuilder imagePath = new StringBuilder();
        String imageFinalName;
        Result result = new Result();

        if (record != null) {
            if (file != null) {// 将图片下载至本地
                // 获取文件后缀
                String suffixName = ImageUtil.getImagePath(file);
                // 生成新的文件名称
                newFileName = ImageUtil.getNewFileName(suffixName);
                // 保存文件
                File f = new File(ImageUtil.getNewImagePath(newFileName));
                // 将图片存入本地磁盘
                boolean state = ImageUtil.saveImage(file, f);
                if (state) {
                    String imgPath = String.valueOf(imagePath.append(newFileName));
                    String image = record.getImage();
                    if (image != null) {
                        imageFinalName = image + "," + imgPath;
                    } else {
                        imageFinalName = imgPath;
                    }
                    // 将图片名称存入数据库
                    int i = service.addDetectImage(imageFinalName, projectId);
                    if (i == 1) {
                        result.setCode(RETURN_CODE_SUCCESS);
                        result.setMsg(RETURN_MESSAGE_SUCCESS);
                    } else {
                        result.setCode(RETURN_CODE_FAIL);
                        result.setMsg("图片存储失败");
                    }
                } else {
                    result.setCode(RETURN_CODE_FAIL);
                    result.setMsg("图片上传失败");
                }
            } else {
                result.setCode(RETURN_CODE_FAIL);
                result.setMsg("上传的图片不能为空");
            }
        } else {
            result.setCode(RETURN_CODE_FAIL);
            result.setMsg("检测记录为空");
        }
        return result;
    }


    /**
     * 完成检测：
     * 若检测完成，可点击完成检测
     * 检测状态更改为检测完毕
     * 检测日期更改为完成时间
     *
     * @return
     */
    @ApiOperation("完成检测接口")
    @ApiImplicitParam(name = "projectId", value = "委托单号")
    @PostMapping("/complete")
    @ResponseBody
    public Result completeDetect(@RequestParam("projectId") Integer projectId) {

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
     * 1： 待检测
     * 2： 检测中
     * 3： 检测完成
     *
     * @param id
     * @return
     */
    @ApiOperation("获取检测状态接口")
    @ApiImplicitParam(name = "id", value = "委托单号")
    @GetMapping("/getDataStatus")
    @ResponseBody
    public Result getDataStatus(@RequestParam("id") Integer id) {

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
     * 获取信息：
     * 该委托单号projectId的检测记录record
     */
    @GetMapping("/getRecordsByProjectId")
    @ApiOperation("获取检测记录接口")
    @ApiModelProperty(name = "projectId", value = "委托单号")
    @ResponseBody
    public Result getRecordsByProjectId(@RequestParam("projectId") Integer projectId) {

        Map<String, Object> returnMap = new HashMap<>();
        List<String> imagesList;
        Result result = new Result();

        try {
            DetectRecord detectRecords = service.selectRecordByProjectId(projectId);
            if (detectRecords != null) {

                // 获取到的图片为：String字符串，且中间会有逗号
                String image = detectRecords.getImage();
                // 图片不为空
                if (image != null) {
                    // 将url中间的逗号除去,并加上端口号等信息
                    imagesList = SplitByComma.splitStringByComma(detectRecords.getImage());
                    returnMap.put("imagesPath", imagesList);
                }
                returnMap.put("recordInfo", detectRecords);
                result.setData(returnMap);
                result.setCode(RETURN_CODE_SUCCESS);
                result.setMsg(RETURN_MESSAGE_SUCCESS);
            } else {
                result.setCode(RETURN_CODE_FAIL);
                result.setMsg(RETURN_MESSAGE_FAIL);
            }
        } catch (Exception e) {
            result.setCode(SYSTEM_CODE_ERROR);
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 根据条件 ：
     * 1：待检测
     * 2：检测中
     * 3：检测完成
     * 返回工程信息
     */
    @GetMapping("/getRequestInfoByStatus")
    @ApiOperation("根据条件获取工程信息接口")
    @ResponseBody
    @ApiImplicitParam(name = "dataStatus", value = "检测状态")
    public Result getRequestInfoByStatus(@RequestParam("dataStatus") Integer dataStatus) {

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
    public Result getImgById(HttpServletResponse response, @RequestParam("id") Integer id) {

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
