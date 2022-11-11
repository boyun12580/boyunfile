package com.boyun.boyunfile.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description="回收站文件列表DTO")
@Data
public class RecoveryFileListDTO {

    @Schema(description = "用户id")
    private Long userId;

}
