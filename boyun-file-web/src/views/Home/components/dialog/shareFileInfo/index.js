import Vue from 'vue'
// 导入组件
import ShareFileInfoDialog from './Dialog.vue'
// 使用基础 Vue 构造器，创建一个“子类”
const ShareFileInfoConstructor = Vue.extend(ShareFileInfoDialog)

let shareFileInfoInstance = null
/**
 * 初始化分享文件实例
 * @param {array} shareData 要分享的文件信息
 */
const initInstanceShareFile = (shareData) => {
	shareFileInfoInstance = new ShareFileInfoConstructor({
		el: document.createElement('div'),
		data() {
			return {
				shareData
			}
		}
	})
}
/**
 * 分享文件 Promise 函数
 * @returns {Promise} 抛出确认和取消回调函数
 */
const showShareFileInfoDialog = (obj) => {
	// 非首次调用服务时，在 DOM 中移除上个实例
	if (shareFileInfoInstance !== null) {
		document.body.removeChild(shareFileInfoInstance.$el)
	}
	let { shareData } = obj
	return new Promise((reslove) => {
		initInstanceShareFile(shareData)
		shareFileInfoInstance.callback = (res) => {
			reslove(res)
			// 服务取消时卸载 DOM
			if (res === 'cancel' && shareFileInfoInstance !== null) {
				document.body.removeChild(shareFileInfoInstance.$el)
				shareFileInfoInstance = null
			}
		}
		document.body.appendChild(shareFileInfoInstance.$el) //  挂载 DOM
		Vue.nextTick(() => {
			shareFileInfoInstance.visible = true //  打开对话框
		})
	})
}

export default showShareFileInfoDialog
