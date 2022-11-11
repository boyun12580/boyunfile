<template>
  <div class="header-wrapper">
    <img class="logo" :src="logoUrl" @click="$router.push({ name: 'Home', query: { fileType: 0, filePath: '/' } })" />
    <el-menu
      class="header-menu"
      :default-active="activeIndex"
      :router="true"
      mode="horizontal"
    >
      <el-menu-item
        index="Home"
        :route="{ name: 'Home', query: { fileType: 0, filePath: '/' } }"
      >首页</el-menu-item>
      <el-menu-item
        class="login"
        index="Login"
        :route="{ name: 'Login' }"
        v-show="!isLogin"
        >登录</el-menu-item
      >
      <!-- <el-menu-item
        class="register"
        index="Register"
        :route="{ name: 'Register' }"
        v-show="!isLogin"
        >注册</el-menu-item
      > -->
      <!-- 为了和其他菜单样式保持一致，请一定要添加类名 el-menu-item -->
      <div class="el-menu-item exit" @click="exitButton()" v-show="isLogin">
        退出
      </div>
      <!-- 为了和其他菜单样式保持一致，请一定要添加类名 el-menu-item -->
      <div class="el-menu-item username" v-show="isLogin">
        <!-- 图标来自于Element UI官方图标库 -->
        <i class="el-icon-user-solid"></i>{{ username }}
      </div>
    </el-menu>
  </div>
</template>

<script>
import Cookies from "js-cookie";

export default {
  name: "Header",
  data() {
    return {
      logoUrl: require('@/assets/logo2.png'),
    };
  },
  computed: {
    // 当前激活菜单的 index
    activeIndex() {
      return this.$route.name; //  获取当前路由名称
    },
    // 登录状态
    isLogin() {
      return this.$store.getters.isLogin;
    },
    // 用户名
    username() {
      return this.$store.getters.username;
    },
  },
  methods: {
    // 退出登录
    exitButton() {
      Cookies.set("token", "");
      this.$router.push({ path: "/login" }); //  退出登录后跳转到登录页面
      this.$message.success("退出登录成功！");
    },
  },
};
</script>

<style lang="stylus" scoped>

@import '~@/assets/style/varibles.styl';

.header-wrapper {
  width: 100%;
  // padding: 0 20px;
  box-shadow: $tabBoxShadow;
  display: flex;

  .logo {
    margin: 12px 24px 0 24px;
    display: inline-block;
    height: 40px;
    cursor: pointer;
  }

  >>> .el-menu--horizontal {
    .el-menu-item:not(.is-disabled):hover {
      border-bottom-color: $Primary !important;
      background: $tabBackColor;
    }

    .external-link {
      padding: 0;
      a {
        display: block;
        padding: 0 20px;
      }
    }
  }

  .header-menu {
    flex: 1;

    .login, .register, .username, .exit {
      float: right;
    }
  }
}
</style>