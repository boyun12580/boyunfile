<template>
  <div class="operation-menu-wrapper">
    <!-- 按钮组 -->
    <el-button-group class="operate-group">
      <!-- disabled 只在全部类型页面 才可新建文件夹 -->
      <el-button
        size="mini"
        type="primary"
        icon="el-icon-plus"
        :disabled="fileType !== 0"
        @click="addFolderDialog.visible = true"
        v-if="fileType >= 0 && fileType <= 5"
        >新建</el-button
      >
       <el-button
        size="mini"
        type="primary"
        icon="el-icon-upload2"
        @click="handleUploadFileClick()"
        :disabled="fileType !== 0"
        v-if="fileType >= 0 && fileType <= 5"
        >上传</el-button
      >
      <!-- disabled 当表格勾选项为空时，禁用删除按钮 -->
      <el-button
          size="mini"
          type="primary"
          icon="el-icon-delete"
          :disabled="!operationFileList.length"
          @click="handleDeleteFileClick()"
          v-if="fileType >= 0 && fileType <= 5"
          >删除</el-button
      >
      <!-- disabled 当表格勾选项为空时，禁用移动按钮 | v-if 当左侧菜单选择全部时，才显示移动按钮 -->
      <el-button
          size="mini"
          type="primary"
          icon="el-icon-rank"
          :disabled="!operationFileList.length"
          v-if="fileType === 0"
          @click="handleMoveFileClick()"
          >移动</el-button
      >
      <!-- disabled 当表格勾选项为空时，禁用下载按钮 -->
      <el-button
        size="mini"
        type="primary"
        icon="el-icon-download"
        :disabled="!operationFileList.length"
        @click="handleDownloadFileClick()"
        v-if="fileType >= 0 && fileType <= 5"
        >下载</el-button
      >

      <el-button
        size="mini"
        type="primary"
        icon="el-icon-share"
        :disabled="!operationFileList.length"
        @click="handleShareFileClick()"
        v-if="fileType >= 0 && fileType <= 5"
        >分享</el-button
      >

      <el-button
        size="mini"
        type="primary"
        icon="el-icon-refresh-left"
        :disabled="!operationFileList.length"
        @click="handleRecoveryFileClick()"
        v-if="fileType == 6"
        >还原</el-button
      >
      <el-button
        size="mini"
        type="primary"
        icon="el-icon-delete"
        :disabled="!operationFileList.length"
        @click="handleDeleteRecoveryFileClick()"
        v-if="fileType == 6"
        >永久删除</el-button
      >
    </el-button-group>

    <!-- 对话框 - 新建文件夹 -->
    <!-- @closed 对话框关闭动画结束时 重置表单并清空所有表单校验 -->
    <el-dialog
      title="新建文件夹"
      width="600px"
      :visible.sync="addFolderDialog.visible"
      @closed="$refs.addFolderForm.resetFields()"
    >
      <el-form
        :model="addFolderForm"
        :rules="addFolderRules"
        label-position="top"
        ref="addFolderForm"
      >
        <el-form-item label="文件夹名称" prop="name">
          <el-input v-model="addFolderForm.name"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addFolderDialog.visible = false">取 消</el-button>
        <el-button
          type="primary"
          :loading="addFolderDialog.loading"
          @click="handleAddFolderSubmit('addFolderForm')"
          >提 交</el-button
        >
      </span>
    </el-dialog>
    <a
      v-for="(item, index) in operationFileList"
      :key="index"
      :href="`/api/filetransfer/downloadfile?userFileId=${item.userFileId}`"
      :download="`${item.fileName}.${item.extendName}`"
      :ref="`downloadLink${index}`"
    ></a>
  </div>
</template>

<script>
import { createFile } from "@/request/file.js";
import { batchDeleteFile } from '@/request/file.js' //  引入批量删除文件接口
import { batchDeleteRecoveryFile, batchRecoveryFile } from '@/request/recoveryFile.js'

export default {
  name: "OperationMenu",
  props: {
    // 文件类型
    fileType: {
      type: Number,
      required: true,
    },
    // 文件路径
    filePath: {
      type: String,
      required: false,
    },
    // 表格行 已选项
    operationFileList: {
        type: Array,
        required: true
    },
  },
  data() {
    return {
      // 新建文件夹对话框数据
      addFolderDialog: {
        visible: false, //  对话框显隐状态
        loading: false,
      },
      // 新建文件夹表单
      addFolderForm: {
        name: "",
      },
      // 新建文件夹表单校验规则
      addFolderRules: {
        name: [
          {
            required: true,
            message: "请输入文件夹名称",
            trigger: "blur, change",
          },
        ],
      },
    };
  },
  computed:{
    // 屏幕宽度
		screenWidth() {
			return this.$store.state.common.screenWidth
		},
  },
  methods: {
    // 上传文件按钮 - 点击事件
    handleUploadFileClick() {
      this.$emit('handleUploadFile', true)
    },
    // 新建文件夹对话框 - 提交按钮
    handleAddFolderSubmit(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.addFolderDialog.loading = true; //  对话框的确定按钮打开loading状态
          // 表单校验通过 - 调用新建文件夹接口
          createFile({
            fileName: this.addFolderForm.name,
            filePath: this.filePath, //  文件路径
            isDir: 1, //  是否为文件夹：1 表示文件夹 0 表示文件
          }).then(
            (res) => {
              this.addFolderDialog.loading = false; //  对话框的确定按钮关闭loading状态
              if (res.success) {
                this.$message.success("添加成功");
                this.addFolderDialog.visible = false; //  关闭对话框
                this.$emit("getTableData"); // 重新获取文件列表
              } else {
                this.$message.warning(res.message);
              }
            },
            (error) => {
              this.addFolderDialog.loading = false; //  对话框的确定按钮关闭loading状态
              console.log(error);
            }
          );
        } else {
          return false;
        }
      });
    },
    // 删除文件按钮 - 点击事件
    handleDeleteFileClick() {
      // 消息弹框提示用户
      this.$confirm('此操作将删除文件, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          // 确定按钮 点击事件 调用批量删除文件接口
          batchDeleteFile({
            files: JSON.stringify(this.operationFileList)
          }).then((res) => {
            if (res.success) {
              this.$message({
                message: res.message,
                type: 'success'
              })
              this.$emit('getTableData') //  刷新文件列表
            } else {
              this.$message.error('失败' + res.message)
            }
          })
        })
        .catch(() => {
          //  取消
          this.$message({
            type: 'info',
            message: '已取消删除'
          })
        })
    },
    // 移动文件按钮 - 点击事件
    handleMoveFileClick() {
        // true/false 批量移动/单文件操作 | this.operationFileList 当前行文件数据
        this.$emit('handleSelectFile', true, this.operationFileList)
        this.$emit('handleMoveFile', true) // true/false 打开/关闭移动文件对话框
    },
    // 下载文件按钮 - 点击事件
    handleDownloadFileClick() {
      for (let i = 0; i < this.operationFileList.length; i++) {
        this.$refs[`downloadLink${i}`][0].click() //  依次调用 a 标签的点击事件来下载文件
      }
    },

    // 还原回收站文件
    handleRecoveryFileClick(){
      this.$confirm('是否还原这些文件?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          batchRecoveryFile({
            files: JSON.stringify(this.operationFileList)
          }).then((res) => {
            if (res.success) {
              this.$message({
                message: res.message,
                type: 'success'
              })
              this.$emit('getTableData') //  刷新文件列表
            } else {
              this.$message.error('失败' + res.message)
            }
          })
        })
        .catch(() => {
          //  取消
          this.$message({
            type: 'info',
            message: '已取消还原'
          })
        })
    },

    //删除回收站文件
    handleDeleteRecoveryFileClick() {
      // 消息弹框提示用户
      this.$confirm('此操作将永久删除文件, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      .then(() => {
        batchDeleteRecoveryFile({
          files: JSON.stringify(this.operationFileList)
        }).then((res) => {
          if (res.success) {
            this.$message({
              message: res.message,
              type: 'success'
            })
            this.$emit('getTableData') //  刷新文件列表
          } else {
            this.$message.error('失败' + res.message)
          }
        })
      })
      .catch(() => {
        //  取消
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    handleShareFileClick(){
      this.$openDialog.shareFile({
				fileInfo: this.operationFileList
			})
    },
  },
};
</script>