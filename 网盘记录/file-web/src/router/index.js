import Vue from 'vue'
import VueRouter from 'vue-router'  //  引入vue-router模块
import Home from '../views/Home/index.vue'  //  引入Home页面对应的文件

Vue.use(VueRouter)  //  将VueRouter挂载在Vue上

// 创建路由列表
/**
 * 1. route level code-splitting
 * 2. this generates a separate chunk (about.[hash].js) for this route :
 *    这会为该路由生成一个单独的块(about.[hash].js)，
 *    打包时对应的css文件、js文件将会以 webpackChunkName 的值拼接hash值命名。
 * 3. which is lazy-loaded when the route is visited.
 *    当此路由被加载时，才加载对应的页面文件。这样做可以加快页面访问速度。
 */
const routes = [
  {
    path: '/',  //  路由路径，即浏览器地址栏中显示的URL
    name: 'Home', //  路由名称
    component: Home, //  路由所使用的页面
    meta: {
      requireAuth: true
    }
  },
  {
    path: '/login', //  登录页面
    name: 'Login',
    component: () => import(/* webpackChunkName: "login" */ '../views/Login.vue')
  },
  {
    path: '/register', //  注册页面
    name: 'Register',
    component: () => import(/* webpackChunkName: "register" */ '../views/Register.vue')
  },
  {
    path: '*',  //  404页面
    name: 'Error_404',
    component: () => import(/* webpackChunkName: "error_404" */ '../views/Error_404.vue')
  }
]

// 创建路由实例
const router = new VueRouter({
  mode: 'history',  //  HTML5 History 模式
  base: process.env.BASE_URL,
  routes
})

export default router

const originalPush = VueRouter.prototype.push;
VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
};