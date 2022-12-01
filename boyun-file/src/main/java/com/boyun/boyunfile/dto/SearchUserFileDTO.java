package com.boyun.boyunfile.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "搜索文件DTO",required = true)
public class SearchUserFileDTO {
    @Schema(description = "搜索文件名")
    private String fileName;
    @Schema(description = "当前页码")
    private Long currentPage;
    @Schema(description = "一页显示数量")
    private Long pageCount;
}