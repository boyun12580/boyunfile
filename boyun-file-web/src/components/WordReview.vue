<template>
  <div
    class="img-review-wrapper"
    v-show="wordReviewVisible"
    @click.self="closeWordReview"
  >
    <!-- 顶部信息栏 & 工具栏 -->
    <div class="tip-wrapper" v-if="wordReviewVisible">
      <!-- <div class="name" :title="activeFileName + activeExtendName">
        {{ activeFileName }}.{{ activeExtendName }}
      </div> -->

      <div class="tool-wrapper">
        <el-tooltip effect="dark" placement="bottom">
          <div slot="content">
            操作提示：<br />
            点击文档以外的区域可退出查看；<br />
          </div>
          <div class="item text-wrapper">
            <!-- <span class="text">操作提示</span> -->
            <i class="el-icon-question"></i>
          </div>
        </el-tooltip>
      </div>
    </div>

    <iframe :src="xsrc" frameborder="0" scrolling="auto" style="width:100%;height:100%;"></iframe>

  </div>
</template>

<script>
export default {

  name: 'Wrod',
  props: {
    

  },

  data(){
    return{
      xsrc:"",
    }
  },
  mounted () {
  },

  computed: {
    
    wordReviewVisible() {
      return this.$store.state.wordReview.wordReviewVisible;
    },
    
    wordUrl() {
      return this.$store.state.wordReview.wordUrl;
    },
  },
  watch: {
    wordUrl(val){
      this.genXsrc(val)
    }
  },
  methods: {
    //  关闭图片预览
    closeWordReview() {
      this.$store.commit('setWordReviewData', false)
    },
    
    genXsrc(wordUrl) {
      if (wordUrl) {
        //你的文档地址
        var file = wordUrl;
        //XDOC文档预览服务地址
        var xurl = "https://view.xdocin.com/view?src=";
        //传入文档地址
        xurl += encodeURIComponent(file);
        //预览参数
        var ops = {
          "watermark": this.watermark,
          // "pdf": true, //word文档尝试以pdf方式显示，默认false
          // "saveable": true, //是否允许保存源文件，默认false
          // "printable": false, //是否允许打印，默认true
          // "copyable": false, //是否允许选择复制内容，默认true
          // "toolbar": false, //是否显示底部工具条，默认true
          // "expire": 30, //预览链接有效期，单位分钟，默认永久有效
          // "limit": "1,3", //限制页数，如：“5”表示只显示前5页，“2,5”表示从第2页开始的5页，对pdf/doc/docx/ppt/pptx有效
          // "mtime": 1633093801, //文件修改时间戳（精确到秒）或修改时间（如：2021-10-01 21:10:01），值改变刷新缓存，实时预览
        };
        //传入预览参数
        for (var a in ops) {
          if (ops[a] != undefined) {
            xurl += "&" + a + "=" + encodeURIComponent(ops[a]);
          }
        }
        //开始预览
        this.xsrc = xurl;
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