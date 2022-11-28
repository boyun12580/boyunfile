package com.boyun.boyunfile.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import javax.persistence.*;

@Data
@TableName("sharefile")
@Table(name = "sharefile")
@Entity
public class ShareFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(type = IdType.AUTO)
    @Column(columnDefinition="bigint(20)")
    private Long sharefileid;

    //分享批次号
    @Column(columnDefinition="varchar(50)")
    private String sharebatchnum;
    //分享文件路径
    @Column(columnDefinition="varchar(100)")
    private String sharefilepath;
    //用户文件id
    @Column(columnDefinition="bigint(20)")
    private Long userfileid;

    @Column(columnDefinition="int(11) comment '删除标志 0-未删除 1-已删除'")
    private Integer deleteFlag;


}

