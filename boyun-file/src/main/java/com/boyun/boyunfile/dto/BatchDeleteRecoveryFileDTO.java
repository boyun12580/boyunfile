package com.boyun.boyunfile.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "批量删除回收站文件DTO",required = true)
public class BatchDeleteRecoveryFileDTO {
    @Schema(description="文件集合")
    private String files;
}
