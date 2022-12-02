
const productConfig = require("./public/config.json"); // 引入config.json文件

module.exports = {
  // transpileDependencies: true,
  lintOnSave: false,
  publicPath: "./",
  devServer: {
    host: "0.0.0.0",
    port: 8081,
    // open: false,
    // disableHostCheck: true, 被遗弃，替换下两条
    // historyApiFallback: true,
    // allowedHosts: "all",
    proxy: {
      //配置代理，解决跨域请求后台数据的问题
      "/api": {
        target: productConfig.baseUrl, //后台接口，连接本地服务
        ws: true, //是否跨域
        changeOrigin: true,
        pathRewrite: {
          "^/api": "/",
        },
      },
    },
  },

  productionSourceMap: false,

  pluginOptions: {
    'style-resources-loader': {
      preProcessor: 'stylus',
      patterns: []
    }
  },


};