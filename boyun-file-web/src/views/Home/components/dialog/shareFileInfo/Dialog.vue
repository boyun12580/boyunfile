<template>
	<!-- 分享对话框 -->
	<el-dialog
		title="分享文件"
		:visible.sync="visible"
		:close-on-click-modal="false"
    :width="screenWidth <= 520 ? '300px' : '500px'"
		@close="handleDialogCancel"
	>
		<el-form
			v-if="shareIsSuccess"
			class="share-success-form"
			:model="shareDatas"
			ref="shareSuccessForm"
			label-suffix="："
			label-width="90px"
			:label-position="screenWidth <= 520 ? 'top' : 'right'"
		>
			<el-form-item label="分享链接" prop="shareBatchNum">
				<el-input
					:value="getShareLink(shareDatas.shareBatchNum)"
					:readonly="true"
					type="textarea"
					autosize
				></el-input>
			</el-form-item>
			<el-form-item label="提取码" prop="extractionCode" v-if="shareDatas.extractionCode">
				<el-input
					v-model="shareDatas.extractionCode"
					:readonly="true"
				></el-input>
			</el-form-item>

      <el-form-item label="二维码" prop="shareBatchNum" style="text-align: center;">
        <vue-qr :text="getShareLink(shareDatas.shareBatchNum)" :size="250" :margin="20" style="margin-right: 50px;"></vue-qr>
      </el-form-item>

		</el-form>
		<div slot="footer" class="dialog-footer">
			<el-button
				v-if="shareIsSuccess"
				type="primary"
				@click="copyShareLink(shareDatas.shareBatchNum, shareDatas.extractionCode)"
				>复制链接及提取码</el-button
			>
		</div>
	</el-dialog>
</template>

<script>

import store from '@/store/index.js'
import { shareFile } from '@/request/share.js'
import vueQr from 'vue-qr'

export default {
	name: 'ShareFileDialog',
  components:{
    vueQr,
  },
	data() {
		return {
			visible: false, //  对话框是否可见
			// 分享文件对话框数据
			form: {
				endTime: '',
				shareType: 0
			},
			rules: {
				endTime: [
					{ required: true, message: '请选择链接有效期', trigger: 'blur' }
				]
			},
			sureBtnLoading: false,
			pickerOptions: {
				shortcuts: [
					{
						text: '今天',
						onClick(picker) {
							const nowDate = new Date()
							picker.$emit(
								'pick',
								new Date(
									nowDate.getFullYear(),
									nowDate.getMonth(),
									nowDate.getDate(),
									23,
									59,
									59
								)
							)
						}
					},
					{
						text: '1天',
						onClick(picker) {
							const date = new Date()
							date.setTime(date.getTime() + 3600 * 1000 * 24)
							picker.$emit('pick', date)
						}
					},
					{
						text: '7天',
						onClick(picker) {
							const date = new Date()
							date.setTime(date.getTime() + 3600 * 1000 * 24 * 7)
							picker.$emit('pick', date)
						}
					},
					{
						text: '30天',
						onClick(picker) {
							const date = new Date()
							date.setTime(date.getTime() + 3600 * 1000 * 24 * 30)
							picker.$emit('pick', date)
						}
					}
				]
			},
			shareIsSuccess: true, //  分享是否成功
      // 分享成功的数据
			shareDatas: {
				shareBatchNum: '',
				extractionCode: ''
			}
		}
	},
  mounted(){
    this.shareDatas.shareBatchNum = this.shareData.shareBatchNum
    this.shareDatas.extractionCode = this.shareData.extractionCode
  },
  
	methods: {
		/**
		 * 分享文件对话框 | 取消按钮点击事件
		 * @description 关闭对话框，重置表单
		 */
		handleDialogCancel() {

			this.visible = false
			this.callback('cancel')
		},
    getShareLink(shareBatchNum){
      return `${location.protocol}//${location.host}/share/${shareBatchNum}`
    },
    copyShareLink(shareBatchNum, extractionCode){
      let input = document.createElement('textarea') // 直接构建textarea以保持换行
      input.value =
        extractionCode === ""
          ? `${this.getShareLink(
              shareBatchNum
            )}\n`
          : `${this.getShareLink(
              shareBatchNum
            )}\n提取码：${extractionCode}\n复制链接到浏览器中并输入提取码即可查看文件` // 设置内容
      document.body.appendChild(input) // 添加临时实例
      input.select() // 选择实例内容
      document.execCommand('Copy') // 执行复制
      document.body.removeChild(input) // 删除临时实例
      this.$message.success('复制成功')
    }
	},
	computed: {
		// 屏幕宽度
		screenWidth() {
			return store.state.common.screenWidth
		},
	}
}
</script>

<style lang="stylus" scoped>
@import '~@/assets/style/varibles.styl';

.success-tip {
  margin-bottom: 16px;
  text-align: center;
  color: $Success;

  .el-icon-success {
    margin-right: 8px;
    height: 20px;
    line-height: 20px;
  }
}
</style>
