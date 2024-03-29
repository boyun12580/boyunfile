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
			v-show="!shareIsSuccess"
			class="share-file-form"
			:model="form"
			ref="shareFileForm"
			label-suffix="："
			label-width="130px"
			:label-position="screenWidth <= 520 ? 'top' : 'right'"
			:rules="rules"
		>
			<el-form-item label="链接有效期至" prop="endTime">
				<el-date-picker
					v-model="form.endTime"
					type="date"
					placeholder="选择日期时间"
					align="right"
					value-format="yyyy-MM-dd"
					:editable="false"
					:clearable="false"
					:picker-options="pickerOptions"
				>
				</el-date-picker>
			</el-form-item>
			<el-form-item label="是否需要提取码" prop="shareType">
				<el-radio-group v-model="form.shareType">
					<el-radio :label="1">是</el-radio>
					<el-radio :label="0">否</el-radio>
				</el-radio-group>
			</el-form-item>
		</el-form>
		<el-form
			v-if="shareIsSuccess"
			class="share-success-form"
			:model="shareData"
			ref="shareSuccessForm"
			label-suffix="："
			label-width="90px"
			:label-position="screenWidth <= 520 ? 'top' : 'right'"
		>
			<div class="success-tip">
				<i class="el-icon-success"></i>
				<span class="text">成功创建分享链接</span>
			</div>
			<el-form-item label="分享链接" prop="shareBatchNum">
				<el-input
					:value="getShareLink(shareData.shareBatchNum)"
					:readonly="true"
					type="textarea"
					autosize
				></el-input>
			</el-form-item>
			<el-form-item label="提取码" prop="extractionCode" v-if="shareData.extractionCode">
				<el-input
					v-model="shareData.extractionCode"
					:readonly="true"
				></el-input>
			</el-form-item>

      <el-form-item label="二维码" prop="shareBatchNum" style="text-align: center;">
        <vue-qr :text="getShareLink(shareData.shareBatchNum)" :size="250" :margin="20" style="margin-right: 50px;"></vue-qr>
      </el-form-item>

		</el-form>
		<div slot="footer" class="dialog-footer">
			<el-button
				v-if="shareIsSuccess"
				type="primary"
				@click="copyShareLink(shareData.shareBatchNum, shareData.extractionCode)"
				>复制链接及提取码</el-button
			>
			<template v-else>
				<el-button @click="handleDialogCancel">取 消</el-button>
				<el-button
					type="primary"
					:loading="sureBtnLoading"
					@click="handleDialogSure('shareFileForm')"
					>确 定</el-button
				>
			</template>
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
			shareIsSuccess: false, //  分享是否成功
			// 分享成功的数据
			shareData: {
				shareBatchNum: '',
				extractionCode: ''
			}
		}
	},
  
	methods: {
		/**
		 * 分享文件对话框 | 取消按钮点击事件
		 * @description 关闭对话框，重置表单
		 */
		handleDialogCancel() {
			this.$refs['shareFileForm'].resetFields()
			this.visible = false
			this.callback('cancel')
		},
		/**
		 * 分享文件对话框 | 确定按钮点击事件
		 * @description 校验表单，校验通过后调用分享文件接口
		 * @param {string} formName 表单ref值
		 */
		handleDialogSure(formName) {
      // this.sureBtnLoading = true
      this.$refs[formName].validate((valid) => {
				if (valid) {
          shareFile({
						...this.form,
            userId: store.getters.userId,
						files: JSON.stringify(this.fileInfo)
					})
					.then((res) => {
						this.sureBtnLoading = false
						if (res.success) {
							this.shareData = res.data
							this.shareIsSuccess = true
              this.$message.success(res.message)
							this.callback('confirm')
						} else {
							this.$message.error(res.message)
						}
					})
					.catch(() => {
						this.sureBtnLoading = false
					})
				} else {
					this.sureBtnLoading = false
					return false
				}
			})
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
