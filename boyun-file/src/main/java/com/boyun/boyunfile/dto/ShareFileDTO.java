package com.boyun.boyunfile.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "分享文件DTO",required = true)
public class ShareFileDTO {

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "用户文件id集合")
    private String files;

    @Schema(description = "过期时间")
    private String endTime;

    @Schema(description = "是否需要提取码")
    private Integer shareType;

}
