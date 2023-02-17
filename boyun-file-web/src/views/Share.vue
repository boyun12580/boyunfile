<template>
	<div class="share-wrapper">
		<div class="share-file-wrapper" v-if="shareStep === 3">
			<div class="operation-wrapper">
				<!-- 面包屑导航栏 -->
				<el-button
					type="primary"
					size="mini"
					icon="el-icon-download"
          :disabled="!operationFileList.length"
					@click="handleDownloadFileClick()"
					>下载</el-button
				>
				<BreadCrumb class="breadcrumb" :fileType="7" :filePath="filePath"></BreadCrumb>
			</div>
			<!-- 文件列表-表格模式 -->
			<FileTable
				:fileType="7"
				:filePath="filePath"
				:tableData="tableData"
				:loading="loading"
        @handleSelectFile="setOperationFile"
        @getTableDataByType="getShareList"
			></FileTable>
		</div>
		<!-- 校验文件分享链接状态和是否需要提取码对话框 -->
		<el-dialog
			title="文件分享"
			:visible.sync="dialogShareFile.visible"
			:show-close="false"
			:close-on-click-modal="false"
			:close-on-press-escape="false"
			:width="screenWidth <= 520 ? '300px' : '500px'"
		>
			<div class="end-time" v-if="shareStep === 1">此分享链接已过期</div>
			<el-form
				class="extraction-code-form"
				v-if="shareStep === 2"
				ref="codeForm"
				:model="dialogShareFile.codeForm"
				:rules="dialogShareFile.codeFormRules"
				label-width="80px"
			>
				<el-form-item label="提取码" prop="extractionCode">
					<el-input
						v-model="dialogShareFile.codeForm.extractionCode"
					></el-input>
				</el-form-item>
			</el-form>
			<span slot="footer" class="dialog-footer">
				<el-button v-if="shareStep === 1" @click="handleCloseBtnClick()"
					>关 闭</el-button
				>
				<el-button
					v-else
					type="primary"
					@click="handleSubmitBtnClick('codeForm')"
					>提 交</el-button
				>
			</span>
		</el-dialog>
    <!-- 使用图片在线查看组件 -->
    <ImgReview></ImgReview>

    <VideoReview
      videoCover=""
      :width="(screenWidth < 786 ? screenWidth : 800)"
      :height="(screenWidth < 786 ? screenWidth*9/16 : 500)"
      :autoplay="false"
      :controls="true"
      :loop="false"
      :muted="false"
      preload="auto"
      :playWidth="96"
      zoom=""
    ></VideoReview>
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

import BreadCrumb from "./Home/components/BreadCrumb.vue";
import FileTable from "./Home/components/FileTable.vue";
import ImgReview from "@/components/ImgReview"; 
import VideoReview from "@/components/VideoReview"; //  引入视频在线查看组件
import OperationMenu from './Home/components/OperationMenu.vue'; //  引入新建文件组件
import { getShareFileList, checkEndTime, checkShareType, checkShareCode} from "../request/share.js"
import { mapGetters } from 'vuex'

export default {
	name: 'Share',
	components: {
    FileTable,
    BreadCrumb,
    ImgReview,
    OperationMenu,
    VideoReview,
	},
	data() {
		return {
			// 文件分享对话框数据
			dialogShareFile: {
				visible: false,
				codeForm: {
					extractionCode: ''
				},
				codeFormRules: {
					extractionCode: [
						{
							required: true,
							message: '请输入提取码',
							trigger: 'blur'
						}
					]
				}
			},
      isBatch: false, //  是否批量移动
			shareStep: 0,
      operationFile: {}, // 单个操作的文件信息
      operationFileList: [], // 批量操作的文件信息
			tableData: [], //  文件列表
			loading: false
		}
	},
	computed: {
    ...mapGetters(['isLogin']),

		shareBatchNum() {
			return this.$route.params.shareBatchNum
		},
		filePath() {
			return this.$route.query.filePath
		},
		shareFilePath() {
			return this.$route.query.filePath
		},
		// 被选中的文件列表
		selectedFiles() {
			return this.$store.state.fileList.selectedFiles
		},
    // 屏幕宽度
		screenWidth() {
			return this.$store.state.common.screenWidth
		},
	},
	watch: {
		filePath() {
			this.getShareList()
		}
	},
	created() {
    if (!this.filePath) {
      this.$router.replace({
        query: {
          filePath: '/'
				}
			})
		}
    this.getShareList();
	},
	mounted() {
		this.checkShareEndTime()
	},
	methods: {
    // 下载文件按钮 - 点击事件
    handleDownloadFileClick() {
      for (let i = 0; i < this.operationFileList.length; i++) {
        // console.log(this.$refs[`downloadLink${i}`])
        this.$refs[`downloadLink${i}`][0].click() //  依次调用 a 标签的点击事件来下载文件
      }
    },
    /**
     * 设置移动文件时的文件信息
     * @param {Boolean} isBatch 是否批量移动，true 是批量移动，false 是单个文件操作
     * @param {Object | Array} file 需要移动的文件信息，单个操作时为Oject，批量操作时，为Array
     */
     setOperationFile(isBatch, file) {
      // console.log(file)
      this.isBatch = isBatch //  保存操作类型
      if (isBatch) {
        this.operationFileList = file //  批量操作文件
      } else {
        this.operationFile = file //  单个操作文件
      }
    },
		/**
		 * 校验分享链接过期时间
		 */
		checkShareEndTime() {
			checkEndTime({
				shareBatchNum: this.shareBatchNum
			}).then((res) => {
				if (res.success) {
          if(res.data.shareStatus == 0){
            if (
              localStorage.getItem(`boyun_${this.shareBatchNum}`) === 'true'
            ) {
              this.checkShareComplete()
            } else {
              this.checkShareLinkType()
            }
          }else{
            this.dialogShareFile.visible = true
            this.shareStep = 1 //  链接已过期
          }
				} else {
          this.$message.error(res.message)
				}
			})
		},
		/**
		 * 校验分享类型
		 * @description 校验分享链接是公共还是私密
		 */
		checkShareLinkType() {
			checkShareType({
				shareBatchNum: this.shareBatchNum
			}).then((res) => {
				if (res.success) {
					// 0 公共 1 私密
					if (res.data.shareType === 0) {
						this.shareStep = 3 //  不是私密链接，直接展示文件列表
						this.getShareList()
						// this.dialogShareFile.visible = false
					}
					if (res.data.shareType === 1) {
            this.dialogShareFile.visible = true
						this.shareStep = 2 //  是私密链接时，让用户输入提取码
					}
				} else {
					this.$message.error(res.message)
				}
			})
		},
		/**
		 * 分享文件验证对话框 取消按钮
		 */
		handleCloseBtnClick() {
			this.dialogShareFile.visible = false
			this.$router.push({ name: 'Home', query: { fileType: 0, filePath: '/' } })
		},
		/**
		 * 提交按钮点击事件
		 */
		handleSubmitBtnClick(formName) {
			this.$refs[formName].validate((valid) => {
				if (valid) {
					checkShareCode({
						extractionCode: this.dialogShareFile.codeForm.extractionCode,
						shareBatchNum: this.shareBatchNum
					}).then((res) => {
						if (res.success) {
							localStorage.setItem(`boyun_${this.shareBatchNum}`, true)
							this.$refs[formName].resetFields() //  清空表单
							this.checkShareComplete()
						} else {
							this.$message.error(res.message)
						}
					})
				} else {
					return false
				}
			})
		},
		/**
		 * 校验全部通过
		 */
		checkShareComplete() {
			this.shareStep = 3 //  展示文件列表
			this.dialogShareFile.visible = false
			this.getShareList()
		},
		/**
		 * 获取分享文件列表
		 */
		getShareList() {
			this.loading = true
			getShareFileList({
				filePath: this.shareFilePath,
				shareBatchNum: this.shareBatchNum
			}).then((res) => {
				if (res.success) {
					this.tableData = res.data.list
					this.loading = false
				} else {
					this.$message.error(res.message)
				}
			})
		},
		/**
		 * 保存到网盘按钮点击事件
		 */
		handleSaveBtnClick() {
			alert('保存')
		}
	}
}
</script>

<style lang="stylus" scoped>
.share-wrapper {
  padding-left: 10px; 
  padding-right: 10px;
  .share-file-wrapper {
    width: 100%;


    .operation-wrapper {
      display: flex;
      justify-content: space-between;
      padding: 8px 0;
    }
  }
}
</style>
