import Vue from "vue";
import Vuex from "vuex";

import user from './module/user' // 引入 user.js
import file from './module/file' // 引入 file 模块
import imgReview from './module/imgReview' // 1. 引入 imgReview 模块

Vue.use(Vuex)

export default new Vuex.Store({
  state: {

  },
  getters: {
    // 是否登录
    isLogin: (state) => state.user.isLogin,
    // 用户名
    username: (state) => state.user.username,
    // 用户id
    userId: (state) => state.user.userId,
    // 用户详细信息
    userInfoObj: (state) => state.user.userInfoObj,
    // 需要显示的表格列
    selectedColumnList: (state) => state.file.selectedColumnList === null ? state.file.allColumnList : state.file.selectedColumnList.split(","),
    // 查看模式 - 0 列表 | 1 网格 | 2 时间线
    showModel: (state) => state.file.showModel === null ? 0 : Number(state.file.showModel)
  },
  mutations: {
    //
  },
  actions: {
    //
  },
  modules: {
    user,
    file,
    imgReview // 2. 注册模块
  }
})