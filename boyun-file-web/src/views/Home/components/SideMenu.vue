<template>
  <div class="side-menu-wrapper">
    <!-- 768px 下，以抽屉形式展示 -->
		<el-drawer
			:visible.sync="isDrawer"
			:with-header="false"
			:size="180"
			direction="ltr"
			v-if="screenWidth <= 768"
		>

      <el-menu
        class="side-menu"
        :default-active="activeIndex"
        :router="true"
        :collapse="isCollapse"
      >

        <el-submenu index="myFile" class="my-file">
          <template slot="title">
            <!-- 图标均来自 Element UI 官方图标库 https://element.eleme.cn/#/zh-CN/component/icon -->
            <i class="el-icon-files"></i>
            <span slot="title">我的文件</span>
          </template>
          <el-menu-item
            index="0"
            :route="{ name: 'Home', query: { fileType: 0, filePath: '/' } }"
          >
              <i class="el-icon-menu"></i>
              <span slot="title">全部</span>
          </el-menu-item>
          <el-menu-item index="1" :route="{ name: 'Home', query: { fileType: 1 } }">
            <i class="el-icon-picture-outline"></i>
            <span slot="title">图片</span>
          </el-menu-item>
          <el-menu-item index="2" :route="{ name: 'Home', query: { fileType: 2 } }">
            <i class="el-icon-document"></i>
            <span slot="title">文档</span>
          </el-menu-item>
          <el-menu-item index="3" :route="{ name: 'Home', query: { fileType: 3 } }">
            <i class="el-icon-video-camera"></i>
            <span slot="title">视频</span>
          </el-menu-item>
          <el-menu-item index="4" :route="{ name: 'Home', query: { fileType: 4 } }">
            <i class="el-icon-headset"></i>
            <span slot="title">音乐</span>
          </el-menu-item>
          <el-menu-item index="5" :route="{ name: 'Home', query: { fileType: 5 } }">
            <i class="el-icon-takeaway-box"></i>
            <span slot="title">其他</span>
          </el-menu-item>
        </el-submenu>

        <el-menu-item
          index="6"
          class="recovery"
          :route="{ name: 'Home', query: { fileType: 6 } }"
        >
          <i class="el-icon-delete"></i>
          <span slot="title">回收站</span>
        </el-menu-item>
        <el-menu-item
          index="8"
          class="my-share"
          :route="{ name: 'Home', query: { fileType: 8, filePath: '/' } }"
        >
          <i class="el-icon-share"></i>
          <span slot="title">我的分享</span>
        </el-menu-item>  
      </el-menu>

      <!-- 存储信息显示 -->
      <div class="storage-wrapper-min" :class="{ fold: isCollapse }">
        <el-progress
					:percentage="storagePercentage"
					:color="storageColor"
					:show-text="false"
					:type="isCollapse ? 'circle' : 'line'"
					:width="32"
					:stroke-width="isCollapse ? 4 : 6"
					stroke-linecap="square"
				></el-progress>
        <div class="text" v-show="!isCollapse">
          <span>存储</span>
          <span
            >{{ storageValue | storageTrans }} /
            {{ storageMaxValue | storageTrans(true) }}
          </span>
          <div class="text" v-show="isCollapse">
					<span>{{ storageValue | storageTrans }}</span>
				</div>
        </div>
      </div>
    </el-drawer>

    <!-- 768px 以上，平铺展示 -->
		<template v-else>
      <el-menu
        class="side-menu"
        :default-active="activeIndex"
        :router="true"
        :collapse="isCollapse"
      >

        <el-submenu index="myFile" class="my-file">
          <template slot="title">
            <!-- 图标均来自 Element UI 官方图标库 https://element.eleme.cn/#/zh-CN/component/icon -->
            <i class="el-icon-files"></i>
            <span slot="title">我的文件</span>
          </template>
          <el-menu-item
            index="0"
            :route="{ name: 'Home', query: { fileType: 0, filePath: '/' } }"
          >
              <i class="el-icon-menu"></i>
              <span slot="title">全部</span>
          </el-menu-item>
          <el-menu-item index="1" :route="{ name: 'Home', query: { fileType: 1 } }">
            <i class="el-icon-picture-outline"></i>
            <span slot="title">图片</span>
          </el-menu-item>
          <el-menu-item index="2" :route="{ name: 'Home', query: { fileType: 2 } }">
            <i class="el-icon-document"></i>
            <span slot="title">文档</span>
          </el-menu-item>
          <el-menu-item index="3" :route="{ name: 'Home', query: { fileType: 3 } }">
            <i class="el-icon-video-camera"></i>
            <span slot="title">视频</span>
          </el-menu-item>
          <el-menu-item index="4" :route="{ name: 'Home', query: { fileType: 4 } }">
            <i class="el-icon-headset"></i>
            <span slot="title">音乐</span>
          </el-menu-item>
          <el-menu-item index="5" :route="{ name: 'Home', query: { fileType: 5 } }">
            <i class="el-icon-takeaway-box"></i>
            <span slot="title">其他</span>
          </el-menu-item>
        </el-submenu>

        <el-menu-item
          index="6"
          class="recovery"
          :route="{ name: 'Home', query: { fileType: 6 } }"
        >
          <i class="el-icon-delete"></i>
          <span slot="title">回收站</span>
        </el-menu-item>
        <el-menu-item
          index="8"
          class="my-share"
          :route="{ name: 'Home', query: { fileType: 8, filePath: '/' } }"
        >
          <i class="el-icon-share"></i>
          <span slot="title">我的分享</span>
        </el-menu-item>  
      </el-menu>
      
      <!-- 存储信息显示 -->
      <div class="storage-wrapper" :class="{ fold: isCollapse }">
        <el-progress
					:percentage="storagePercentage"
					:color="storageColor"
					:show-text="false"
					:type="isCollapse ? 'circle' : 'line'"
					:width="32"
					:stroke-width="isCollapse ? 4 : 6"
					stroke-linecap="square"
				></el-progress>
        <div class="text" v-show="!isCollapse">
          <span>存储</span>
          <span
            >{{ storageValue | storageTrans }} /
            {{ storageMaxValue | storageTrans(true) }}
          </span>
        </div>
        <div class="text" v-show="isCollapse">
					<span>{{ storageValue | storageTrans }}</span>
				</div>
      </div>
    </template>
  
    <!-- 展开 & 收缩分类栏 -->
    <el-tooltip
      effect="dark"
      :content="isCollapse ? '展开' : '收起'"
      placement="right"
    >
      <div
        class="aside-title"
        @click="isCollapse ? (isCollapse = false) : (isCollapse = true)"
      >
        <i
          class="icon"
          :class="isCollapse ? 'el-icon-d-arrow-right' : 'el-icon-d-arrow-left'"
          :title="isCollapse ? '展开' : '收起'"
        ></i>
      </div>
    </el-tooltip>
  </div>
</template>

<script>
export default {
  name: "SideMenu",
  props: {
    storageValue: {
      type: Number,
      required: true
    }
  },
  data() {
    return {
      isDrawer: false, //  控制移动端菜单抽屉是否显示
      isCollapse: false,
      storageMaxValue: Math.pow(1024, 3) * 1, //  最大存储容量，1GB
      //  自定义进度条颜色，不同占比，进度条颜色不同
      storageColor: [
        { color: '#67C23A', percentage: 50 },
        { color: '#E6A23C', percentage: 80 },
        { color: '#F56C6C', percentage: 100 }
      ]
    }
  },
  computed: {
    // 当前激活菜单的 index
    activeIndex() {
      return String(this.$route.query.fileType); //  获取当前路由参数中包含的文件类型
    },
    // 存储百分比
    storagePercentage() {
      return (this.storageValue / this.storageMaxValue) * 100
    },
    // 屏幕宽度
		screenWidth() {
      // console.log(this.$store.state.common.screenWidth)
			return this.$store.state.common.screenWidth
		},
  },
  filters: {
    // 计算空间占比
    storageTrans(size, status) {
      const B = 1024
      const KB = Math.pow(1024, 2)
      const MB = Math.pow(1024, 3)
      const GB = Math.pow(1024, 4)
      if (status) {
        //    截取整数部分
        if (!size) {
          return 0 + 'KB'
        } else if (size < KB) {
          return (size / B).toFixed(0) + 'KB'
        } else if (size < MB) {
          return (size / KB).toFixed(0) + 'MB'
        } else if (size < GB) {
          return (size / MB).toFixed(0) + 'GB'
        } else {
          return (size / GB).toFixed(0) + 'TB'
        }
      } else {
        if (!size) {
          return 0 + 'KB'
        } else if (size < KB) {
          return (size / B).toFixed(0) + 'KB'
        } else if (size < MB) {
          return (size / KB).toFixed(2) + 'MB'
        } else if (size < GB) {
          return (size / MB).toFixed(3) + 'GB'
        } else {
          return (size / GB).toFixed(4) + 'TB'
        }
      }
    }
  },
   watch: {
    // 监听收缩状态变化，存储在sessionStorage中，保证页面刷新时仍然保存设置的状态
    isCollapse(newValue) {
      sessionStorage.setItem("isCollapse", newValue);
      if (this.screenWidth <= 768 && newValue) {
				this.isDrawer = true
				this.isCollapse = false
			}
    },
  },
  created() {
    this.isCollapse = sessionStorage.getItem("isCollapse") === "true"; //  读取保存的状态
  },
};
</script>

<style lang="stylus" scoped>
@import '~@/assets/style/mixins.styl';
@import '~@/assets/style/varibles.styl';

.side-menu-wrapper {

  position: relative;
  height: calc(100vh - 61px);
  padding-right: 11px;

  .side-menu {
    // 高度设置为屏幕高度减去顶部导航栏的高度
    height: calc(100vh - 127px);
    // overflow: hidden;
    // overflow: auto;
    // 调整滚动条样式
    setScrollbar(6px, transparent, #C0C4CC);
    .el-menu-item.is-active {
      background: #ecf5ff;
    }
    .my-file, .recovery {
      box-shadow: 0 4px 12px 0 $BorderExtralight;
    }
  }
  >>> .el-menu {
    background: transparent;
    .el-submenu .el-menu-item {
      min-width: 180px;
    }
  }

  // 对展开状态下的菜单设置宽度
  .side-menu:not(.el-menu--collapse) {
    width: 180px;
  }
  // 折叠图标调整样式
  .aside-title {
    position: absolute;
    top: calc(50% - 50px);
    right: 0;
    z-index: 2;
    background: $Primary;
    color: #fff;
    width: 12px
    height: 100px;
    line-height: 95px;
    cursor: pointer;
    border-radius: 0 16px 16px 0;
    &:hover {
      opacity: 0.7;
    }
    .icon {
      font-size: 12px;
    }
  }

  // 存储空间展示区
  .storage-wrapper {
    position: absolute;
    bottom: 0;
    left: 0;
    box-shadow: 0 -2px 12px 0 $BorderExtralight;
    border-right: solid 1px #e6e6e6;
    box-sizing: border-box;
    width: calc(100% - 11px);
    // width: 180px;
    height: 66px;
    padding: 16px;
    z-index: 2;
    color: $PrimaryText;
    .text {
      margin-top: 8px;
      display: flex;
      justify-content: space-between;
      align-items: center;
      font-size: 12px;
      flex-wrap: wrap;
    }
  }
  .storage-wrapper.fold {
    padding: 0;
    >>> .el-progress--circle {
      margin: 0 auto;
      width: 32px;
      display: block;
    }
    .text {
      font-size: 12px;
      justify-content: center;
    }
  }

  .storage-wrapper-min {
    position: absolute;
    bottom: 0;
    left: 0;
    box-shadow: 0 -2px 12px 0 $BorderExtralight;
    border-right: solid 1px #e6e6e6;
    box-sizing: border-box;
    width: calc(100%);
    // width: 180px;
    height: 66px;
    padding: 16px;
    z-index: 2;
    color: $PrimaryText;
    .text {
      margin-top: 8px;
      display: flex;
      justify-content: space-between;
      align-items: center;
      font-size: 12px;
      flex-wrap: wrap;
    }
  }
}
</style>