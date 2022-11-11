import { get, post } from "./http";

// 获取回收站文件列表
export const getRecoveryFileList = (p) => get("/recoveryfile/getrecoveryfilelist", p);

// 恢复文件夹 或 文件
export const recoveryFile = (p) => post("/recoveryfile/recoveryfile", p);

// 删除回收站文件
export const deleteRecoveryFile = (p) => post("/recoveryfile/deleterecoveryfile", p);

// 批量删除回收站文件
export const batchDeleteRecoveryFile = (p) => post("/recoveryfile/batchdeleterecoveryfile", p);

// 批量恢复文件夹 或 文件
export const batchRecoveryFile = (p) => post("/recoveryfile/batchrecoveryfile", p);