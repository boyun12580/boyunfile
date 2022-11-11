package com.boyun.boyunfile.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "恢复文件DTO",required = true)
public class RecoveryFileDTO {

    @Schema(description = "恢复文件id")
    private Long recoveryFileId;
    @Schema(description = "删除批次")
    @Deprecated
    private String deleteBatchNum;


}
