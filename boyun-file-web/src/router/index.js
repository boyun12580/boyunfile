import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home/index.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',  //  路由路径，即浏览器地址栏中显示的URL
    name: 'Home', //  路由名称
    component: Home, //  路由所使用的页面
    meta: {
      title: '我的网盘',
      requireAuth: true //选择拦截
    }

  },
  {
    path: "/login", //  登录页面
    name: "Login",
    component: () =>
      import(/* webpackChunkName: "login" */ "../views/Login.vue"),
    meta: {
      title: '登录',
      keepAlive: true
    }
  },
  {
    path: "/register", //  注册页面
    name: "Register",
    component: () =>
      import(/* webpackChunkName: "register" */ "../views/Register.vue"),
    meta: {
      title: '注册',
      keepAlive: true
    }
  },
  {
    path: '/share/:shareBatchNum',
    name: 'Share',
    component: () => import(/* webpackChunkName: "share" */ "../views/Share.vue"),
    meta: {
      title: '分享',
      // keepAlive: true
    },
    props: true
  },
  {
    path: "*", //  404页面
    name: "Error_404",
    component: () =>
      import(/* webpackChunkName: "error_404" */ "../views/Error_404.vue"),
    meta: {
      title: '404错误',
      keepAlive: true
    }
  },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
