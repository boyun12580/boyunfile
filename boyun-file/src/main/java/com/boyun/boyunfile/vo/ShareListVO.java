package com.boyun.boyunfile.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.Column;

@Data
@Schema(description="获取我的分享文件列表VO")
public class ShareListVO {

    @Schema(description="分享文件id")
    private Long shareFileId;
    @Schema(description="用户文件id")
    private Long userFileId;
    @Schema(description="文件url")
    private String fileUrl;
    @Schema(description="文件大小")
    private Long fileSize;

    //分享类型(0公共,1私密,2好友)
    @Schema(description="分享类型")
    private Integer shareType;

    @Schema(description="文件名")
    private String fileName;
    @Schema(description="文件路径")
    private String filePath;
    @Schema(description="扩展名")
    private String extendName;
    @Schema(description="是否是目录")
    private Integer isDir;
    @Schema(description="过期时间")
    private String endTime;
    @Schema(description="分享时间")
    private String shareTime;
    @Schema(description="分享状态")
    private String shareStatus;
    @Schema(description="分享批次号")
    private String shareBatchNum;
    @Schema(description="提取码")
    private String extractionCode;

}
