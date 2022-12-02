<template>
  <div class="home">
    <!-- 使用组件 -->
    <!-- 左侧菜单 - 区分文件类型 -->
    <el-aside width="auto">
      <SideMenu class="home-left" :storageValue="storageValue"></SideMenu>
    </el-aside>
    <!-- 右侧内容区 -->
    <div :class="{'home-right-isMin': screenWidth <= 768, 'home-right-isMax': screenWidth > 768}">
      <!-- 面包屑导航栏 - 显示文件路径 -->
      <div :class="{'operation-wrapper-isMin': screenWidth <= 768, 'operation-wrapper-isMax': screenWidth > 768}">
        <OperationMenu
          class="operation-menu"
          :fileType="fileType"
          :filePath="filePath"
          :operationFileList="operationFileList"
          @getTableData="getFileData"
          @handleUploadFile="handleUploadFile"
          @handleSelectFile="setOperationFile"
          @handleMoveFile="setMoveFileDialog"
        ></OperationMenu>
        <SelectColumn 
          :fileType="fileType"
          :filePath="filePath"
          @getSearchFileList="getSearchFileList"
          @getTableDataByType="getFileData"
          v-if="screenWidth > 768 || (screenWidth <= 768 && fileType === 0)"
        ></SelectColumn>
            <!-- 查看模式切换组件 将 fileType 传递给子组件 -->
      </div>
      <BreadCrumb :fileType="fileType" :filePath="filePath"></BreadCrumb>
      <!-- 表格组件 v-if 当左侧菜单选择图片且查看模式为"列表"时显示 或 左侧菜单选择的非图片 -->
      <FileTable
        v-if="(fileType === 1 && showModel === 0) || fileType !== 1"
        :tableData="tableData"
        :loading="loading"
        :fileType="fileType"
        @getTableData="getFileData"
        @handleSelectFile="setOperationFile"
        @handleMoveFile="setMoveFileDialog"
      ></FileTable>
      <!-- 网格模式 v-if 当左侧菜单选择图片且查看模式为"网格"时显示 -->
      <FileGrid
        v-if="fileType === 1 && showModel === 1"
        :tableData="tableData"
        :loading="loading"
      ></FileGrid>
      <!-- 时间线模式 v-if 当左侧菜单选择图片且查看模式为"时间线"时显示 -->
      <FileTimeLine
        v-if="fileType === 1 && showModel === 2"
        :tableData="tableData"
        :loading="loading"
      ></FileTimeLine>
      <FilePagination
        :style="screenWidth <= 768 && fileType !== 0 ? 'padding-top: 18px;': ''"
        :pageData="pageData"
        @changePageData="changePageData"
        v-if="fileType != 6"
      ></FilePagination>
    </div>
    <!-- 文件上传组件 -->
    <FileUploader
      ref="globalUploader"
      @getTableData="getFileData"
    ></FileUploader>

    <!-- 使用移动文件模态框 -->
    <MoveFileDialog
      :dialogMoveFile="dialogMoveFile"
      @setSelectFilePath="setSelectFilePath"
      @confirmMoveFile="confirmMoveFile"
      @handleMoveFile="setMoveFileDialog"
    ></MoveFileDialog>
    <!-- 使用图片在线查看组件 -->
    <ImgReview></ImgReview>
  </div>
</template>

<script>
import SideMenu from "./components/SideMenu.vue"; //  引入左侧菜单组件
import BreadCrumb from "./components/BreadCrumb.vue"; //  引入面包屑导航栏
import FileTable from "./components/FileTable.vue"; //  引入文件表格展示区
import FilePagination from './components/FilePagination.vue' //  引入分页组件
import SelectColumn from './components/SelectColumn.vue' //  引入控制列显隐组件
import { getFileListByPath, getFileListByType, searchUserFileList } from '@/request/file.js' //  引入获取文件列表接口
import OperationMenu from './components/OperationMenu.vue' //  引入新建文件组件
import FileUploader from './components/FileUploader.vue' //  引入文件上传组件
import { getFileStorage } from '@/request/file.js' //  引入接口
import { getFileTree } from '@/request/file.js' //  引入获取文件夹列表 树状结构 接口
import MoveFileDialog from './components/MoveFileDialog.vue' //  引入移动文件组件
import { moveFile } from '@/request/file.js' //  引入获取文件列表接口
import { batchMoveFile } from '@/request/file.js'
import ShowModel from './components/ShowModel.vue' //  引入查看模式切换组件
import FileGrid from './components/FileGrid.vue' //  引入网格组件
import FileTimeLine from "./components/FileTimeLine.vue"; //  引入时间线模式组件
import ImgReview from "@/components/ImgReview"; //  引入图片在线查看组件
import { getRecoveryFileList } from "@/request/recoveryFile.js" // 回收站
import { getShareList } from "@/request/share.js" // 获取我的分享文件列表

export default {
  name: "Home",
  components: {
    SideMenu, 
    BreadCrumb,
    FileTable,
    FilePagination,
    SelectColumn,
    OperationMenu,
    FileUploader,
    MoveFileDialog,
    ShowModel,
    FileGrid,
    FileTimeLine,
    ImgReview,
  },
  data() {
    return {
      loading: false,
      tableData: [], //  文件列表
      pageData: {
        currentPage: 1, //   页码
        pageCount: 10, //  每页显示条目个数
        total: 0 //  总数
      },
      storageValue: 0, //  存储空间占用大小
      //  移动文件模态框数据
      dialogMoveFile: {
        visible: false, //  对话框是否显示
        fileTree: [] //  目录树
      },
      isBatch: false, //  是否批量移动
      operationFile: {}, // 单个操作的文件信息
      operationFileList: [], // 批量操作的文件信息
      selectFilePath: '', //  目标路径
    }
  },
  computed: {
    // 左侧菜单选中的文件类型
    fileType() {
      return this.$route.query.fileType ? Number(this.$route.query.fileType) : 0
    },
    // 当前所在路径
    filePath() {
        return this.$route.query.filePath ? this.$route.query.filePath : '/'
    },
    // 查看模式
    showModel() {
      return this.$store.getters.showModel
    },
    // 屏幕宽度
		screenWidth() {
			return this.$store.state.common.screenWidth
		},
  },
  watch: {
    fileType() {
      this.getFileData() //  获取文件列表
    },
    filePath() {
      // 当左侧菜单选择全部，文件路径发生变化时，再重新获取文件列表
      if (this.fileType === 0 || this.fileType == 8) {
        this.getFileData() //  获取文件列表
      }
    }
  },
  mounted() {
    this.getFileData() //  获取文件列表
    
  },
  methods: {
    // 上传文件 按钮点击事件
    handleUploadFile() {
        //  触发子组件中的打开文件上传窗口事件
        this.$refs.globalUploader.triggerSelectFileClick()
    },
    // 获取文件列表
    getFileData() {
      this.loading = true; // 打开表格loading状态
      if (this.fileType === 0) {
        // 左侧菜单选择的为 全部 时，根据路径，获取文件列表
        this.getFileDataByPath();
      } else if(this.fileType === 6){
        this.getRecoveryFileList();
      } else if(this.fileType === 8){
        this.getShareFileList();
      } else {
        // 左侧菜单选择的为 除全部以外 的类型时，根据类型，获取文件列表
        this.getFileDataByType();
      }
      this.getStorageValue(); //  2.调用获取文件占用空间函数
    },
    // 获取文件占用空间
    getStorageValue() {
      getFileStorage().then((res) => {
        //    3. 调用接口
        if (res.success) {
          this.storageValue = res.data ? res.data : 0; //    
        } else {
          this.$message.error(res.message);
        }
      })
    },
    // 根据路径获取文件列表
    getFileDataByPath() {
      getFileListByPath({
          filePath: this.filePath, // 传递当前路径
          currentPage: this.pageData.currentPage,
          pageCount: this.pageData.pageCount
      }).then(
        (res) => {
          this.loading = false //  关闭表格loading状态
          if (res.success) {
            this.tableData = res.data.list // 表格数据赋值
            this.pageData.total = res.data.total //  分页组件 - 文件总数赋值
          } else {
            this.$message.error(res.message)
          }
        },
        (error) => {
          this.loading = false
          console.log(error)
        }
      )
    },
    // 根据类型获取文件列表
    getFileDataByType() {

      getFileListByType({
        fileType: this.fileType, //  文件类型
        currentPage: this.pageData.currentPage, //  当前页码
        pageCount: this.pageData.pageCount //  每页条目数
      }).then(
        (res) => {
          this.loading = false //  关闭表格loading状态
          if (res.success) {
            this.tableData = res.data.list // 表格数据赋值
            this.pageData.total = res.data.total //  分页组件 - 文件总数赋值
          } else {
            this.$message.error(res.message)
          }
        },
        (error) => {
          this.loading = false
          console.log(error)
        }
      )
    },

    // 获取分享文件列表
    getShareFileList(){
      getShareList({
        shareBatchnum: this.$route.query.shareBatchNum,
        userId: this.$store.getters.userId,
        filePath: this.filePath, // 传递当前路径
        currentPage: this.pageData.currentPage,
        pageCount: this.pageData.pageCount
      }).then(
        (res) => {
          this.loading = false //  关闭表格loading状态
          if (res.success) {
            this.tableData = res.data.list // 表格数据赋值
            this.pageData.total = res.data.total //  分页组件 - 文件总数赋值
          } else {
            this.$message.error(res.message)
          }
        },
        (error) => {
          this.loading = false
          console.log(error)
        }
      )
    },

    // 搜索文件
    getSearchFileList(fileName){
      this.loading = true
			searchUserFileList({
				currentPage: this.pageData.currentPage,
				pageCount: this.pageData.pageCount,
				fileName: fileName
			}).then((res) => {
				this.loading = false
				if (res.success) {
					this.tableData = res.data.list // 表格数据赋值
          this.pageData.total = res.data.total //  分页组件 - 文件总数赋值
				} else {
					this.$message.error(res.message)
				}
			})
    },

    // 获取回收站文件列表
    getRecoveryFileList(){
      getRecoveryFileList({
        userId : this.$store.getters.userId
      }).then(
        (res) => {
          this.loading = false; //  关闭表格loading状态
          if (res.success) {
            this.tableData = res.data.list; // 表格数据赋值
          } else {
            this.$message.error(res.message);
          }
        },
        (error) => {
          this.loading = false;
          console.log(error);
        }
      )
    },
    // 分页组件 - 页码或当页条目数改变时
    changePageData(pageData) {
      this.pageData.currentPage = pageData.currentPage // 页码赋值
      this.pageData.pageCount = pageData.pageCount //  每页条目数赋值
      this.getFileData() // 获取文件列表
    },

    /**
     * 设置移动文件时的文件信息
     * @param {Boolean} isBatch 是否批量移动，true 是批量移动，false 是单个文件操作
     * @param {Object | Array} file 需要移动的文件信息，单个操作时为Oject，批量操作时，为Array
     */
    setOperationFile(isBatch, file) {
      this.isBatch = isBatch //  保存操作类型
      if (isBatch) {
        this.operationFileList = file //  批量操作文件
      } else {
        this.operationFile = file //  单个操作文件
      }
    },
    /**
     * 设置移动文件对话框相关数据
     * @param {Boolean} visible 打开/关闭移动文件模态框
     */
    setMoveFileDialog(visible) {
      this.dialogMoveFile.visible = visible //  打开对话框
      if (visible) {
        // 打开对话框时，获取文件夹目录树
        getFileTree().then((res) => {
          if (res.success) {
            this.dialogMoveFile.fileTree = [res.data]
          } else {
            this.$message.error(res.message)
          }
        })
      }
    },
    //  设置移动文件的目标路径
    setSelectFilePath(selectFilePath) {
      this.selectFilePath = selectFilePath
    },
    //  移动文件模态框-确定按钮事件
    confirmMoveFile() {
      if (this.isBatch) {
        //  批量移动
        let data = {
          filePath: this.selectFilePath,
          files: JSON.stringify(this.operationFileList)
        }
        batchMoveFile(data).then((res) => {
          if (res.success) {
            this.$message.success(res.data)
            this.getFileData() //  刷新文件列表
            this.dialogMoveFile.visible = false //  关闭对话框
            this.operationFileList = []
          } else {
            this.$message.error(res.message)
          }
        })
      } else {
        //  单文件移动
        let data = {
          filePath: this.selectFilePath, //  目标路径
          oldFilePath: this.operationFile.filePath, //  原路径
          fileName: this.operationFile.fileName, //  文件名称
          extendName: this.operationFile.extendName //  文件扩展名
        }
        moveFile(data).then((res) => {
          if (res.success) {
            this.$message.success('移动文件成功')
            this.getFileData() //  刷新文件列表
            this.dialogMoveFile.visible = false //  关闭对话框
          } else {
            this.$message.error(res.message)
          }
        })
      }
    },
  },
};
</script>

<style lang="stylus" scoped>
.home {
  // 使用flex布局 菜单居左固定宽度 右侧内容区域自适应宽度
  display: flex;
  width: 100% !important;
  overflow: hidden;

  .home-left {
    height: calc(100vh - 61px);
    overflow: hidden;
    box-sizing: border-box;
  }

  .home-right-isMax {
    box-sizing: border-box;
    width: calc(100% - 200px);
    padding: 8px 24px;
    flex: 1;
    overflow: hidden;
    // padding: 0px 16px !important;
    // overflow: hidden;
  }

  .home-right-isMin {
    box-sizing: border-box;
    width: calc(100% - 200px);
    padding: 8px 4px;
    flex: 1;
    overflow: hidden;
    // padding: 0px 16px !important;
    // overflow: hidden;
  }

  .operation-wrapper-isMax {
    overflow: hidden;
    // margin: 8px 0;
    margin-bottom: 16px;
    display: flex;
    align-items: center;
    justify-content: space-between;

    // 左侧菜单按钮组 样式调整
      >>> .operation-menu-wrapper {
          flex: 1;
      }
  }

  .operation-wrapper-isMin {
    overflow: hidden;
    // margin: 8px 0;
    // display: flex;
    align-items: center;
    justify-content: space-between;

    // 左侧菜单按钮组 样式调整
      >>> .operation-menu-wrapper {
          flex: 1;
          margin: 6px 0 6px;
      }
  }
}
</style>