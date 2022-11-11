package com.boyun.boyunfile.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@Table(name = "recoveryfile")
@Entity
@TableName("recoveryfile")
public class RecoveryFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(type = IdType.AUTO)
    @Column(columnDefinition="bigint(20)")
    private Long recoveryFileId;
    @Column(columnDefinition = "bigint(20)")
    private Long userFileId;
    @Column(columnDefinition="varchar(25)")
    private String deleteTime;
    @Column(columnDefinition = "varchar(50)")
    private String deleteBatchNum;
    @Column(columnDefinition = "bigint(20)")
    private Long userId;
    @Column(columnDefinition="int(11) comment '删除标志 0-未删除 1-已删除'")
    private Integer deleteFlag;
}