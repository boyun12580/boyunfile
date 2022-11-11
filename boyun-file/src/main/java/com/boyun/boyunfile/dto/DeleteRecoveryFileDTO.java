package com.boyun.boyunfile.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.Column;

@Data
@Schema(name = "删除回收站文件DTO",required = true)
public class DeleteRecoveryFileDTO {

    @Schema(description = "回收站文件id")
    private Long recoveryFileId;

}
