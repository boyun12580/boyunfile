package com.boyun.boyunfile.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "分享文件DTO",required = true)
public class DeleteShareDTO {

    @Schema(description = "用户文件id")
    private Long userFileId;
    @Schema(description = "分享文件id")
    private Long shareFileId;
    @Schema(description = "分享批次号")
    private String shareBatchNum;

}
