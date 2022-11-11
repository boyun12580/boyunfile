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

import CollapseTransition from 'element-ui/lib/transitions/collapse-transition'

Vue.component(CollapseTransition.name, CollapseTransition)

Vue.use(all)

Vue.use(uploader)

Vue.use(ElementUI);

Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
