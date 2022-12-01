package com.boyun.boyunfile.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "其他用户获取分享文件列表DTO",required = true)
public class ShareFileListDTO {

    @Schema(description = "文件路径")
    private String filePath;
//    @Schema(description = "用户id")
//    private Long userId;
    @Schema(description = "分享批次号")
    private String shareBatchNum;

}
