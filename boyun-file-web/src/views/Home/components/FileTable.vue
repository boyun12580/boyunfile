<template>
  <div class="file-table-wrapper">

    <el-table
      class="file-table"
      :data="tableData"
      fit
      v-loading="loading"
      @sort-change="handleSortChange"
      @selection-change="handleSelectRow"
    >
      <!-- 勾选框 -->
      <el-table-column
        type="selection"
        width="56"
        align="center"
        :selectable="selectEnable"
      ></el-table-column>
      <el-table-column label prop="isDir" :width="screenWidth <= 768 ? 40 : 56" align="center">
        <template slot-scope="scope">
          <img :src="setFileImg(scope.row)" style="width: 32px;height: 32px;" />
        </template>
      </el-table-column>
      <el-table-column prop="fileName" label="文件名">
        <template slot-scope="scope">
          <div style="cursor: pointer" @click="handleFileNameClick(scope.row)" v-if="fileType != 6">
            {{
              scope.row.extendName
                ? `${scope.row.fileName}.${scope.row.extendName}`
                : `${scope.row.fileName}`
            }}
          </div>
          <div v-if="fileType == 6">
            {{
              scope.row.extendName
                ? `${scope.row.fileName}.${scope.row.extendName}`
                : `${scope.row.fileName}`
            }}
          </div>
          
          <div class="file-info" v-if="screenWidth <= 768">
            {{ scope.row.uploadTime }}
            <span class="file-size">
              {{
                scope.row.isDir === 0
                  ? calculateFileSize(scope.row.fileSize)
                  : ''
              }}
            </span>
          </div>
        </template>
      </el-table-column>
      
      <el-table-column
        :label="fileType === 6 ? '原路径' : '所在路径'"
        prop="filePath"
        show-overflow-tooltip
        v-if="fileType !== 0 && screenWidth > 768"
      >
        <template slot-scope="scope">
          <div
            style="cursor: pointer"
            title="点击跳转"
            @click="
              $router.push({
                query: { fileType: 0, filePath: scope.row.filePath }
              })
            "
          >
            {{ scope.row.filePath }}
          </div>
        </template>
      </el-table-column>
      
      <el-table-column
        prop="extendName"
        label="类型"
        width="100"
        key="extendName"
        v-if="selectedColumnList.includes('extendName') && screenWidth > 768"
      >
          <template slot-scope="scope">
              <span>{{ scope.row.extendName ? scope.row.extendName : '文件夹' }}</span>
          </template>
      </el-table-column>
      <el-table-column
        prop="fileSize"
        label="大小"
        width="100"
        key="fileSize"
        sortable
        :sort-by="['fileSize']"
        v-if="selectedColumnList.includes('fileSize') && screenWidth > 768"
      >
        <template slot-scope="scope">
              <span>{{ calculateFileSize(scope.row.fileSize) }}</span>
        </template>
      </el-table-column>
      <!-- 通过 v-if 来控制 修改日期 列是否显示 -->
      <el-table-column
        prop="uploadTime"
        label="修改日期"
        width="180"
        key="uploadTime"
        :sort-by="['uploadTime']"
        sortable
        v-if="selectedColumnList.includes('uploadTime') && screenWidth > 768"
      >
      </el-table-column>

      <el-table-column
        prop="deleteTime"
        label="删除日期"
        width="180"
        key="deleteTime"
        :sort-by="['deleteTime']"
        sortable
        v-if="selectedColumnList.includes('deleteTime') && fileType == 6 && screenWidth > 768"
      >
      </el-table-column>
  
      <!-- 表格操作列 自定义表格头，原有的 label="操作" 需要删除，宽度动态变化 -->
      <el-table-column :width="operaColumnIsFold ? 100 : 100" v-if="screenWidth > 768">
        <!-- 自定义表格头 -->
        <template slot="header">
          <span>操作</span>
          <i
            class="el-icon-circle-plus"
            title="展开"
            @click="operaColumnIsFold = true"
          ></i>
          <i
            class="el-icon-remove"
            title="折叠"
            @click="operaColumnIsFold = false"
          ></i>
        </template>
        <template slot-scope="scope">
          <!-- 操作列展开状态下的按钮 通过v-if控制 -->
          <div class="opera-unfold" v-if="operaColumnIsFold">
              <!-- 操作列展开状态下的按钮 -->
              <div class="opera-unfold">
              <el-button
                  type="text"
                  size="small"
                  @click.native="handleClickDelete(scope.row)"
                  v-if="fileType >= 0 && fileType <= 5"
                  >删除</el-button
              >
              <el-button
                  type="text"
                  size="small"
                  @click.native="handleClickMove(scope.row)"
                  v-if="fileType >= 0 && fileType <= 5"
                  >移动</el-button
              >
              <el-button
                  type="text"
                  size="small"
                  @click.native="handleClickRename(scope.row)"
                  v-if="fileType >= 0 && fileType <= 5"
                  >重命名</el-button
              >
              <el-button
                  type="text"
                  size="small"
                  @click.native="handleClickShareFile(scope.row)"
                  v-if="fileType >= 0 && fileType <= 5"
                  >分享</el-button
              >
              <el-button
                  type="text"
                  size="small"
                  @click.native="handleClickRecoveryFile(scope.row)"
                  v-if="fileType == 6"
                  >还原</el-button
              >
              <el-button
                  type="text"
                  size="small"
                  @click.native=""
                  v-if="fileType == 6"
                  >删除</el-button
              >
              <!-- 操作列展开状态下的下载按钮 -->
              <el-button type="text" size="small" v-if="scope.row.isDir === 0 && fileType >= 0 && fileType <= 5">
                  <a
                    :href="`/api/filetransfer/downloadfile?userFileId=${scope.row.userFileId}`"
                    target="_blank"
                    style="display: block; color: inherit"
                    >下载</a
                  >
              </el-button>
              
              </div>
          </div>
  
          <!-- 操作列收缩状态下的按钮 通过v-else控制 -->
          <el-dropdown trigger="click" v-else>
            <el-button size="mini">
              操作
              <i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item @click.native="handleClickDelete(scope.row)" v-if="fileType >= 0 && fileType <= 5"
                >删除</el-dropdown-item
              >
              <el-dropdown-item @click.native="handleClickMove(scope.row)" v-if="fileType >= 0 && fileType <= 5"
                >移动</el-dropdown-item
              >
              <el-dropdown-item @click.native="handleClickRename(scope.row)" v-if="fileType >= 0 && fileType <= 5"
                >重命名</el-dropdown-item
              >
              <el-dropdown-item @click.native="handleClickShareFile(scope.row)" v-if="fileType >= 0 && fileType <= 5"
                >分享</el-dropdown-item
              >
              <el-dropdown-item @click.native="handleClickRecoveryFile(scope.row)" v-if="fileType == 6"
                >还原</el-dropdown-item
              >
              <el-dropdown-item @click.native="handleClickDeleteRecoveryFile(scope.row)" v-if="fileType == 6"
                >删除</el-dropdown-item
              >
              <!-- 操作列收缩状态下的下载按钮 -->
            <el-dropdown-item v-if="scope.row.isDir === 0 && fileType >= 0 && fileType <= 5">
                <a
                  :href="`/api/filetransfer/downloadfile?userFileId=${scope.row.userFileId}`"
                  target="_blank"
                  style="display: block; color: inherit"
                  >下载</a
                >
            </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
      </el-table-column>
    </el-table>
  </div>

</template>

<script>
import { deleteFile } from '@/request/file.js'
import { renameFile } from '@/request/file.js' //  引入文件重命名接口
import { recoveryFile, deleteRecoveryFile } from '@/request/recoveryFile.js'
import config from '@/config/index.js'
import shareFile from '@/views/Home/components/dialog/shareFile/Dialog.vue'


export default {
  name: "FileTable",
  props: {
    // 表格数据，同时需要删除原本在 data( return { } ) 中的 tableData，否则会报错
    tableData: {
      type: Array,
      required: true
    },
    // 表格加载状态
    loading: {
      type: Boolean,
      required: true
    },
    // 文件类型
    fileType: {
        type: Number,
        required: true
    },
  },
  components:{
    shareFile
  },
  data() {
    return {
      //  文件图片Map映射
      fileImgMap: {
        apk: require('@/assets/image/apk.png'),
        csv: require('@/assets/image/csv.png'),
        doc: require('@/assets/image/doc.png'),
        excel: require('@/assets/image/excel.png'),
        exe: require('@/assets/image/exe.png'),
        fold: require('@/assets/image/fold.png'),
        gif: require('@/assets/image/gif.png'),
        html: require('@/assets/image/html.png'),
        json: require('@/assets/image/json.png'),
        mp3: require('@/assets/image/mp3.png'),
        mp4: require('@/assets/image/mp4.png'),
        other: require('@/assets/image/other.png'),
        pdf: require('@/assets/image/pdf.png'),
        ppt: require('@/assets/image/ppt.png'),
        rar: require('@/assets/image/rar.png'),
        svg: require('@/assets/image/svg.png'),
        txt: require('@/assets/image/txt.png'),
        zip: require('@/assets/image/zip.png')
      },
      operaColumnIsFold: sessionStorage.getItem('operaColumnIsFold') || false //  表格操作列-是否收缩
    };
  },
  watch: {
    // 监听收缩状态变化，存储在sessionStorage中，保证页面刷新时仍然保存设置的状态
    operaColumnIsFold(newValue) {
      sessionStorage.setItem('operaColumnIsFold', newValue)
    }
  },
  created() {
    this.operaColumnIsFold = sessionStorage.getItem('operaColumnIsFold') === 'true' //  读取保存的状态
  },
  methods: {

    /**
     * 当前行为文件夹时，多选框变为不可选
     * 在回收站时固定可用
     * @param {*} row 
     * @param {*} rowIndex 
     */
    selectEnable(row, rowIndex) {
      if(this.fileType == 6) return 1;
      else{
        return row.isDir == 1 ? false : true;
      }
    },

    handleClickShareFile(row){
      this.$openDialog.shareFile({
				fileInfo: [
					{
						userFileId: row.userFileId
					}
				]
			})
    },

    // 还原文件
    handleClickRecoveryFile(row){
      recoveryFile({
        recoveryFileId : row.recoveryFileId
      }).then(
        (res) => {
          if (res.success) {
              this.$emit('getTableData') //  刷新文件列表
              this.$message.success('还原成功')
            } else {
              this.$message.error(res.message)
            }
        }
      )
    },
    // 删除回收站文件
    handleClickDeleteRecoveryFile(row){
      this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
          deleteRecoveryFile(row).then(
            (res) => {
              if (res.success) {
                this.$emit('getTableData') //  刷新文件列表
                this.$message.success('删除成功')
              } else {
                this.$message.error(res.message)
              }
            }
          )
        }
      ).catch(() => {

          //  取消
          this.$message({
            type: 'info',
            message: '已取消删除'
          })
        })
    },
    /**
		 * 当表格的排序条件发生变化的时候会触发该事件
		 */
		handleSortChange() {
			this.sortedFileList = this.$refs.multipleTable.tableData
		},

    // 删除按钮 - 点击事件
    handleClickDelete(row) {
      // 消息弹框提示用户
      this.$confirm('此操作将删除该文件, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          // 确定按钮 点击事件 调用删除文件接口
          deleteFile(row).then((res) => {
            if (res.success) {
              this.$emit('getTableData') //  刷新文件列表
              this.$message.success('删除成功')
            } else {
              this.$message.error(res.message)
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
    //  根据文件扩展名设置文件图片
    setFileImg(row) {

      if(this.fileType == 6){
        if (!row.extendName) {
          //  文件夹
          return this.fileImgMap.fold
        }else{
          const fileTypeMap = {
            pptx: 'ppt',
            doc: 'word',
            docx: 'doc',
            xls: 'excel',
            xlsx: 'excel'
          }
          return this.fileImgMap.other;
        }
      }else{
        if (!row.extendName) {
          //  文件夹
          return this.fileImgMap.fold
        } else if (['jpg', 'png', 'jpeg', 'gif'].includes(row.extendName)) {
          // 图片类型，直接显示缩略图
          return this.downloadImgMin(row)
        } else {
          const fileTypeMap = {
            pptx: 'ppt',
            doc: 'word',
            docx: 'doc',
            xls: 'excel',
            xlsx: 'excel'
          }
          //  可以识别文件类型的文件
          return this.fileImgMap[row.extendName] || this.fileImgMap[fileTypeMap[row.extendName]] || this.fileImgMap.other
        }
      }

    },
    // 文件名点击事件
    handleFileNameClick(row) {
        //  若是目录则进入目录
        if (row.isDir) {
            this.$router.push({
                query: {
                    filePath: `${row.filePath}${row.fileName}/`,
                    fileType: 0
                }
            })
        } else {
            //  若当前点击项是图片
            const PIC = ['png', 'jpg', 'jpeg', 'gif', 'svg']
            if (PIC.includes(row.extendName)) {
                let data = {
                    imgReviewVisible: true,
                    imgReviewList: [{
                      fileUrl: `/${config.staticContext}${row.fileUrl}`,
                      // fileUrl: `${row.fileUrl}`,
                      downloadLink: `/${config.baseContext}/filetransfer/downloadfile?userFileId=${row.userFileId}`,
                        fileName: row.fileName,
                        extendName: row.extendName
                    }],
                    activeIndex: 0
                }
                this.$store.commit('setImgReviewData', data)    //    触发图片在线查看
            }
        }
    },
    //  计算文件大小
    calculateFileSize(size) {
        const B = 1024
        const KB = Math.pow(1024, 2)
        const MB = Math.pow(1024, 3)
        const GB = Math.pow(1024, 4)
        if (!size) {
            return '_'
        } else if (size < KB) {
            return (size / B).toFixed(0) + 'KB'
        } else if (size < MB) {
            return (size / KB).toFixed(1) + 'MB'
        } else if (size < GB) {
            return (size / MB).toFixed(2) + 'GB'
        } else {
            return (size / GB).toFixed(3) + 'TB'
        }
    },
    // 移动按钮 - 点击事件
    handleClickMove(row) {
        this.$emit('handleSelectFile', false, row) // true/false 操作类型：批量移动/单文件操作；row 当前行文件数据
        this.$emit('handleMoveFile', true) // true/false 打开/关闭移动文件对话框
    },

    // 表格行勾选事件
    handleSelectRow(selection) {
      this.$emit('handleSelectFile', true, selection) // true/false 批量移动/单文件操作；selection 勾选的表格行数据
    },

    // 重命名按钮 - 点击事件
    handleClickRename(row) {
      var fileName = row.fileName
      this.$prompt('请输入文件名', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputValue: fileName,
        inputPattern: /\S/, //  文件名不能为空
        inputErrorMessage: '请输入文件名',
        closeOnClickModal: false
      })
        .then(({ value }) => {
          // 确定按钮 调用重命名接口
          renameFile({
            ...row,
            fileName: value
          }).then((res) => {
            if (res.success) {
              this.$emit('getTableData') //  刷新文件列表
              this.$message.success('文件已重命名')
            } else {
              this.$message.error(res.message)
            }
          })
        })
        .catch(() => {
          this.$message({
            type: 'info',
            message: '取消'
          })
        })
    }
  },
  computed: {
    // 表格显示列
    selectedColumnList() {
      return this.$store.getters.selectedColumnList
    },
    // 屏幕宽度
		screenWidth() {
			return this.$store.state.common.screenWidth
		},
  },
};
</script>

<style lang="stylus" scoped>
@import '~@/assets/style/varibles.styl';
@import '~@/assets/style/mixins.styl';

.file-table-wrapper {
  margin-top: 2px;

  .file-type-0 {
    height: calc(100vh - 206px) !important;

    >>> .el-table__body-wrapper {
      height: calc(100vh - 262px) !important;
    }
  }

  .file-type-6 {
    height: calc(100vh - 211px) !important;

    >>> .el-table__body-wrapper {
      height: calc(100vh - 263px) !important;
    }
  }

  .file-table.share {
    height: calc(100vh - 109px) !important;

    >>> .el-table__body-wrapper {
      height: calc(100vh - 161px) !important;
    }
  }

  .file-table {
    width: 100% !important;
    height: calc(100vh - 203px);

    >>> .el-table__header-wrapper {
      th {
        // background: $tabBackColor;
        padding: 4px 0;
        color: $RegularText;
      }

      .el-icon-circle-plus, .el-icon-remove {
        margin-left: 6px;
        cursor: pointer;
        font-size: 16px;

        &:hover {
          color: $Primary;
        }
      }
    }

    >>> .el-table__body-wrapper {
      height: calc(100vh - 255px);
      // height: 90%;
      overflow-y: auto;
      setScrollbar(6px, transparent, #C0C4CC);

      td {
        padding: 8px 0;
        .file-name {
          .keyword {
            color: $Danger;
          }
        }
      }

      .el-icon-warning {
        font-size: 16px;
        color: $Warning;
      }

      .el-icon-time {
        font-size: 16px;
        color: $Success;
      }
    }
  }
}

// .right-menu-list {
//   position: fixed;
//   display: flex;
//   flex-direction: column;
//   background: #fff;
//   border: 1px solid $BorderLighter;
//   border-radius: 4px;
//   box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
//   z-index: 2;
//   padding: 4px 0;
//   color: $RegularText;

//   .right-menu-item,
//   .unzip-item {
//     padding: 0 16px;
//     height: 36px;
//     line-height: 36px;
//     cursor: pointer;
//     &:hover {
//       background: $PrimaryHover;
//       color: $Primary;
//     }
//     i {
//       margin-right: 8px;
//     }
//   }

//   .unzip-menu-item {
//     position: relative;
//     &:hover {
//       .unzip-list {
//         display: block;
//       }
//     }
//     .unzip-list {
//       position: absolute;
//       display: none;
//       .unzip-item {
//         width: 200px;
//         setEllipsis(1)
//       }
//     }
//   }
// }
// .right-menu-list,
// .unzip-list {
//   background: #fff;
//   border: 1px solid $BorderLighter;
//   border-radius: 4px;
//   box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
//   z-index: 2;
//   padding: 4px 0;
//   color: $RegularText;
// }
</style>