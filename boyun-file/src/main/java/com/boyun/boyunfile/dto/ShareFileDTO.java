package com.boyun.boyunfile.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "分享文件DTO",required = true)
public class ShareFileDTO {

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "用户文件id")
    private Long userFileId;

    @Schema(description = "过期时间")
    private String endTime;

    @Schema(description = "提取码")
    private String extractionCode;

}
