<template>
  <div
    class="img-review-wrapper"
    v-show="videoReviewVisible"
    @click.self="closeVideoReview"
  >
    <!-- 顶部信息栏 & 工具栏 -->
    <div class="tip-wrapper" v-if="videoReviewVisible">
      <!-- <div class="name" :title="activeFileName + activeExtendName">
        {{ activeFileName }}.{{ activeExtendName }}
      </div> -->

      <div class="tool-wrapper">
        <el-tooltip effect="dark" placement="bottom">
          <div slot="content">
            操作提示：<br />
            点击视频以外的区域可退出查看；<br />
          </div>
          <div class="item text-wrapper">
            <!-- <span class="text">操作提示</span> -->
            <i class="el-icon-question"></i>
          </div>
        </el-tooltip>
      </div>
    </div>

    <div :class="['m-video', { hover: !originPlay }]" :style="`width: ${width}px; height: ${height}px;`">
      <video
        ref="veo"
        :class="{ cursor: !originPlay }"
        :style="`object-fit: ${zoom};`"
        :src="videoUrl"
        :poster="videoCover"
        :width="width"
        :height="height"
        :autoplay="autoplay"
        :controls="!originPlay&&controls"
        :loop="loop"
        :muted="autoplay || muted"
        :preload="preload">
        您的浏览器不支持video标签。
      </video>
      <svg :class="[vplay ? 'play cursor':'hidden']" :style="`width: ${playWidth}px; height: ${playWidth}px;`" @click="clickPlay" viewBox="0 0 24 24">
        <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M4.75 6.75C4.75 5.64543 5.64543 4.75 6.75 4.75H17.25C18.3546 4.75 19.25 5.64543 19.25 6.75V17.25C19.25 18.3546 18.3546 19.25 17.25 19.25H6.75C5.64543 19.25 4.75 18.3546 4.75 17.25V6.75Z"></path>
        <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M15.25 12L9.75 8.75V15.25L15.25 12Z"></path>
      </svg>
    </div>

  </div>
</template>

<script>
export default {

  name: 'Video',
  props: {
    videoCover: { // 视频封面url
      type: String,
      default: null
    },
    width: { // 视频播放器宽度
      type: Number,
      default: 800
    },
    height: { // 视频播放器高度
      type: Number,
      default: 450
    },
    /*
      由于目前在最新版的Chrome浏览器（以及所有以Chromium为内核的浏览器）中，
      已不再允许自动播放音频和视频。就算你为video或audio标签设置了autoplay
      属性也一样不能自动播放！
      本组件解决方法：设置视频 autoplay 时，自动将视频设置为静音 muted: true 即可实现自
      动播放，然后用户可以使用控制栏开启声音，类似某宝商品自动播放的宣传视频逻辑
    */
    autoplay: { // 视频就绪后是否马上播放
      type: Boolean,
      default: false
    },
    controls: { // 是否向用户显示控件，比如进度条，全屏
      type: Boolean,
      default: true
    },
    loop: { // 视频播放完成后，是否循环播放
      type: Boolean,
      default: false
    },
    muted: { // 是否静音
      type: Boolean,
      default: false
    },
    preload: { // 是否在页面加载后载入视频，如果设置了autoplay属性，则preload将被忽略；
      type: String,
      default: 'auto' // auto:一旦页面加载，则开始加载视频; metadata:当页面加载后仅加载视频的元数据 none:页面加载后不应加载视频
    },
    playWidth: { // 中间播放暂停按钮的边长
      type: Number,
      default: 96
    },
    zoom: { // video的poster默认图片和视频内容缩放规则
      type: String,
      default: 'none' // none:(默认)保存原有内容，不进行缩放; fill:不保持原有比例，内容拉伸填充整个内容容器; contain:保存原有比例，内容以包含方式缩放; cover:保存原有比例，内容以覆盖方式缩放
    }
  },

  data(){
    return{
      originPlay: true,
      vplay: true
    }
  },
  mounted () {
    this.$refs.veo.addEventListener('pause', this.onPause)
    this.$refs.veo.addEventListener('playing', this.onPlaying)
    if (this.autoplay) {
      this.vplay = false
      this.originPlay = false
    }
  },

  computed: {
    // 视频查看组件是否显示
    videoReviewVisible() {
      return this.$store.state.videoReview.videoReviewVisible;
    },
    // 视频列表
    videoUrl() {
      return this.$store.state.videoReview.videoUrl;
    },
  },
  watch: {

  },
  methods: {
    //  关闭图片预览
    closeVideoReview() {
      this.$store.commit('setVideoReviewData', false)
    },
    onPause () {
      this.vplay = true
      this.$once('hook:beforeDestroy', function () {
        removeEventListener('pause', this.onPause)
      })
    },
    onPlaying () {
      this.vplay = false
      // 自动清理自己，避免内存泄漏
      this.$once('hook:beforeDestroy', function () {
        removeEventListener('playing', this.onPlaying)
      })
    },
    clickPlay () {
      this.$refs.veo.play()
      this.vplay = false
      if (this.originPlay) {
        this.originPlay = false
      }
    }

  },
};
</script>

<style lang="stylus" scoped>
@import '~@/assets/style/mixins.styl';

.img-review-wrapper {
  position: fixed;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  overflow: auto;
  width: 100%;
  height: 100%;
  z-index: 2010;
  text-align: center;
  display: flex;
  align-items: center;
  animation: imgReviewAnimation 0.3s;
  -webkit-animation: imgReviewAnimation 0.3s; /* Safari and Chrome */
  animation-iteration-count: 0.3;
  -webkit-animation-iteration-count: 0.3;
  animation-fill-mode: forwards;
  -webkit-animation-fill-mode: forwards; /* Safari 和 Chrome */

  @keyframes imgReviewAnimation {
    0% {
      background: transparent;
    }

    100% {
      background: rgba(0, 0, 0, 0.8);
    }
  }

  @keyframes imgReviewAnimation {
    0% {
      background: transparent;
    }

    100% {
      background: rgba(0, 0, 0, 0.8);
    }
  }

  .m-video {
    display: inline-block;
    position: relative;
    // background: #000;
    margin: 0 auto;
    transition: transform 0.5s;
    -webkit-transition: transform 0.5s; /* Safari */
    .cursor {
      cursor: pointer;
    }
    .play {
      fill: none;
      color: #FFF;
      opacity: 0.7;
      position: absolute;
      top: 0;
      right: 0;
      bottom: 0;
      left: 0;
      margin: auto;
      transition: opacity .3s;
      &:hover {
        opacity: 0.9;
      }
    }
    .hidden {
      display: none;
    }
  }
  .hover {
    &:hover {
      .play {
        opacity: 0.9;
      }
    }
  }

  .tip-wrapper {
    box-sizing: border-box;
    position: fixed;
    top: 0;
    left: 0;
    z-index: 2011;
    background: rgba(0, 0, 0, 0.5);
    padding: 0 48px;
    width: 100%;
    height: 48px;
    line-height: 48px;
    color: #fff;
    font-size: 16px;
    display: flex;
    justify-content: space-between;

    .name {
      flex: 1;
      padding-right: 16px;
      text-align: left;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }


    .tool-wrapper {
      flex: 1;
      display: flex;
      justify-content: flex-end;

      .item {
        margin-left: 16px;
        cursor: pointer;

        &:hover {
          opacity: 0.7;
        }
      }
    }
  }

  
}
</style>