<template>
  <div id="app">
    <!-- 使用组件 -->
    <Header></Header>
    <router-view />
  </div>
</template>

<script>
import Header from "@/components/Header.vue"; //  引入组件

export default {
  name: "App",
  //  注册组件
  components: {
    Header,
  },

};
</script>

<style lang="stylus" scoped>

#app {
  height: 100%;
  overflow-x: hidden;
  -webkit-text-size-adjust: none;
  overflow-y: auto;
}

</style>
