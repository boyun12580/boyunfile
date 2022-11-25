import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from "element-ui"
import "element-ui/lib/theme-chalk/index.css"
import './assets/style/iconfontCover.styl'
import './assets/style/elementCover.styl'
import "./assets/style/base.styl"

import "@/router/before.js";
import uploader from 'vue-simple-uploader'
import all from '@/libs/globalFunction.js'

// 引入文件操作相关插件
import fileOperationPlugins from '@/plugins/fileOperationPlugins.js'

// 引入 Element UI 组件
import element from '@/plugins/element.js'

import config from '@/config/index.js'

import CollapseTransition from 'element-ui/lib/transitions/collapse-transition'

Vue.component(CollapseTransition.name, CollapseTransition)

Vue.use(all)

Vue.prototype.$config = config

Vue.use(uploader)

Vue.use(ElementUI);

Vue.config.productionTip = false

for (let key in fileOperationPlugins) {
	Vue.prototype[`$${key}`] = fileOperationPlugins[key]
}

Vue.use(element)

// Vue.prototype.$config = config

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
