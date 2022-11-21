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

//    分享时间正常
    public static final int NOMALTIME = 0;
//    分享时间已过期
    public static final int EXPIREDTIME = 1;
//    分享时间已删除
    public static final int DELETETIME = 2;
//    分享公共
    public static final int SHAREALL = 0;
//    分享私密
    public static final int SHAREPRIVATE = 1;
//    分享好友
    public static final int SHAREFRIENDS = 2;



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(type = IdType.AUTO)
    @Column(columnDefinition="bigint(20)")
    private Long shareid;

    //失效时间
    @Column(columnDefinition="varchar(30)")
    private String endTime;
    //提取码
    @Column(columnDefinition="varchar(10)")
    private String extractionCode;
    //分享批次号
    @Column(columnDefinition="varchar(40)")
    private String shareBatchnum;
    //分享状态(0正常,1已失效,2已撤销)
    @Column(columnDefinition="int")
    private Integer shareStatus;
    //分享时间
    @Column(columnDefinition="varchar(30)")
    private String shareTime;
    //分享类型(0公共,1私密,2好友)
    @Column(columnDefinition="int")
    private Integer shareType;
    //用户id
    @Column(columnDefinition="bigint(20)")
    private Long userId;



}

