package com.boyun.boyunfile.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import javax.persistence.*;

@Data
@TableName("share")
@Table(name = "share")
@Entity
public class Share  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(type = IdType.AUTO)
    @Column(columnDefinition="bigint(20)")
    private Long shareid;

    //失效时间
    @Column(columnDefinition="varchar(30)")
    private String endtime;
    //提取码
    @Column(columnDefinition="varchar(10)")
    private String extractioncode;
    //分享批次号
    @Column(columnDefinition="varchar(40)")
    private String sharebatchnum;
    //分享状态(0正常,1已失效,2已撤销)
    @Column(columnDefinition="int")
    private Integer sharestatus;
    //分享时间
    @Column(columnDefinition="varchar(30)")
    private String sharetime;
    //分享类型(0公共,1私密,2好友)
    @Column(columnDefinition="int")
    private Integer sharetype;
    //用户id
    @Column(columnDefinition="bigint(20)")
    private Long userid;



}

