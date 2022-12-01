package com.boyun.boyunfile.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "校验分享文件提取码DTO",required = true)
public class CheckShareCodeDTO {

    @Schema(description = "分享批次号")
    private String shareBatchNum;

    @Schema(description = "提取码")
    private String extractionCode;

}
