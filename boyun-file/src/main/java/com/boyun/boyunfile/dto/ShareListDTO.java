package com.boyun.boyunfile.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "获取我的分享文件列表DTO",required = true)
public class ShareListDTO {

    @Schema(description = "文件路径")
    private String filePath;
    @Schema(description = "用户id")
    private Long userId;
    @Schema(description = "当前页码")
    private Long currentPage;
    @Schema(description = "一页显示数量")
    private Long pageCount;

}
