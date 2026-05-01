package com.vt.atp.api;


import com.google.api.client.http.EmptyContent;
import com.google.api.client.http.MultipartContent;
import com.google.gson.reflect.TypeToken;
import com.vt.atp.api.constant.ApiConstant;
import com.vt.atp.api.constant.SizeConstant;
import com.vt.atp.component.VtRemoter;
import com.vt.atp.dto.FileUploadParse;
import com.vt.atp.dto.Result;
import com.vt.atp.dto.vt.file.FileBehaviourReportResp;
import com.vt.atp.dto.vt.file.FileReportResp;
import com.vt.atp.dto.vt.file.FileScanResp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
public class FileApi {

    private final VtRemoter vtRemoter;

    /**
     * 上传文件进行分析
     * @param uploadParse 上传文件信息
     * @return 文件id及分析结果链接
     */
    public Result<FileScanResp> uploadFile(FileUploadParse uploadParse) {
        if (SizeConstant.SIZE_LIMIT < uploadParse.size()) {
            throw new IllegalArgumentException(String.format("file size large than %sM", SizeConstant.SIZE_LIMIT));
        }

        String url = chooseUrl(uploadParse.size());

        return upload(uploadParse, url);
    }

    /**
     * 重新分析文件
     * @param id 文件id （SHA-256, SHA-1 or MD5 identifying the file）
     * @return 文件id及分析结果链接
     */
    public Result<FileScanResp> reAnalyze(String id) {
        String url = String.format(ApiConstant.PREFIX + ApiConstant.RE_ANALYSE_FILE, id);
        return vtRemoter.post(url, new EmptyContent(), new TypeToken<Result<FileScanResp>>() {});
    }

    /**
     * 获取报告
     * @param id SHA-256, SHA-1 or MD5 identifying the file
     * @return 报告
     */
    public Result<FileReportResp> getFileReport(String id) {
        String url = String.format(ApiConstant.PREFIX + ApiConstant.GET_FILE_REPORT, id);
        return vtRemoter.get(url, new TypeToken<Result<FileReportResp>>() {});
    }

    /**
     * 该端点从每个沙箱返回关于该文件的行为信息。
     * 该 API 调用返回文件行为对象中包含的所有字段。
     * 注意部分条目有
     *  has_html_report如果属实，你可能会发现HTML文件的行为。https://docs.virustotal.com/reference/get-file-behaviours-html
     *  has_pcap如果属实，你可能会了解PCAP文件的行为。https://docs.virustotal.com/reference/get-file-behaviours-html
     * @param id <sha256>
     * @return 行为报告
     */
    public Result<List<FileBehaviourReportResp>> getBehaviourReport(String id) {
        String url = String.format(ApiConstant.PREFIX + ApiConstant.GET_BEHAVIOUR_REPORT, id);
        return vtRemoter.get(url, new TypeToken<Result<List<FileBehaviourReportResp>>>() {});
    }

    /**
     * 获取url
     * @param size 文件大小
     * @return vt上传接口
     */
    private String chooseUrl(int size) {
        // 小于32M
        if (SizeConstant.SIZE_BOUNDARIES > size) {
            return ApiConstant.PREFIX + ApiConstant.UPLOAD_LT_32;
        }

        //大于32M.获取特殊上传url
        return getLargeThan32Url();
    }

    /**
     * 上传
     * @param uploadParse 上传文件内容
     * @param url vt上传接口
     * @return 文件id
     */
    private Result<FileScanResp> upload(FileUploadParse uploadParse, String url) {
        MultipartContent content = uploadParse.uploadForm();
        return vtRemoter.post(url, content, new TypeToken<Result<FileScanResp>>() {});
    }

    /**
     * 获取文件大于32M时的上传接口
     * @return 文件大于32M时的上传接口
     */
    private String getLargeThan32Url() {
        String url = ApiConstant.PREFIX + ApiConstant.GET_UPLOAD_GT_32_URL;
        return vtRemoter.get(url, new TypeToken<String>() {});
    }

}
