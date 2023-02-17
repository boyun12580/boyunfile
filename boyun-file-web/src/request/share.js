import { get, post } from "./http";

// 分享文件
export const shareFile = (p) => post("/share/sharefile", p);

// 获取我的分享文件列表
export const getShareList = (p) => get("/share/getsharelist", p);

// 获取我的分享文件列表
export const getShareFileList = (p) => get("/share/getsharefilelist", p);

// 校验分享链接过期时间
export const checkEndTime = (p) => get("/share/checkendtime", p);

// 校验分享类型
export const checkShareType = (p) => get("/share/checksharetype", p);

// 校验提取码
export const checkShareCode = (p) => get("/share/checksharecode", p);

// 取消分享
export const deleteShare = (p) => post("/share/deleteshare", p);

