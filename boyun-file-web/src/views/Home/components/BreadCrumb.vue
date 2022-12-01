<template>
  <div class="bread-crumb-wrapper">
    <div class="current-path">当前位置：</div>
    <el-input
			class="file-path-input"
			ref="filePathInputRef"
			placeholder="请输入路径"
			v-model="inputFilePath"
			size="mini"
			:autofocus="true"
			v-show="isShowInput"
			@blur="handleInputBlurEnter"
			@change="handleInputBlurEnter"
		></el-input>

    <div
			class="breadcrumb-box"
			:class="{ 'able-input': fileType === 0 }"
			v-show="!isShowInput"
			@click.self="handleClickBreadCrumbSelf"
		>
      <!-- 按类型查看文件时 -->
      <el-breadcrumb class="bread-crumb" v-if="fileType > 0 && fileType < 7" separator-class="el-icon-arrow-right">
        <el-breadcrumb-item>{{ fileTypeMap[fileType] }}</el-breadcrumb-item>
      </el-breadcrumb>
      <!-- 按路径查看文件时 -->
      <el-breadcrumb class="bread-crumb" v-else separator-class="el-icon-arrow-right">
        <!-- 当点击面包屑导航栏中的某一级时，改变路由 -->
        <el-breadcrumb-item
          v-for="(item, index) in breadCrumbList"
          :key="index"
          :to="getRouteQuery(item)"
          >{{ item.name }}</el-breadcrumb-item
        >
      </el-breadcrumb>
    </div>
  </div>
</template>

<script>
export default {
  name: "BreadCrumb",
  props: {
    fileType: {
      type: Number,
      required: true,
    },
    
		// 文件路径
		filePath: {
			required: true,
			type: String
		}
  },
  data() {
    return {
      fileTypeMap: {
        1: "全部图片",
        2: "全部文档",
        3: "全部视频",
        4: "全部音乐",
        5: "其他",
        6: "回收站",
        7: "",
        8: "我的分享"
      },
      isShowInput: false, //  是否展示路径输入框
			inputFilePath: '' //  路径输入
    };
  },
  computed: {
    // 依据路径查看时 当前路径拆分的面包屑数据，data 中原有的 breadCrumbList 需要删掉
    breadCrumbList: {
      // let path = this.$route.query.filePath //  路径示例 /目录一/
      // let pathList = path ? path.split('/').slice(1, -1) : [] // 路径列表，取第二个 ~ 倒数第二个，因为第一个和最后一个数组项都是空字符串
      // let res = [{ name: '全部文件', path: '/' }]
      // pathList.forEach((element, index) => {
      //   res.push({
      //     name: element,
      //     path: `${res[index].path}${element}/`
      //   })
      // })
      // return res
      get() {
				let filePath = this.$route.query.filePath
				let filePathList = filePath ? filePath.split('/') : []
				let res = [] //  返回结果数组
				let _path = [] //  存放祖先路径
				for (let i = 0; i < filePathList.length; i++) {
					if (filePathList[i]) {
						_path.push(filePathList[i])
						res.push({
							path: _path.join('/') + '/',
							name: filePathList[i]
						})
					} else if (i === 0) {
						//  根目录
						filePathList[i] = ''
						_path.push(filePathList[i])
						res.push({
							path: '/',
							name:
								this.fileType === 0
									? '全部文件'
									: this.fileType === 8
									? '我的分享'
									: this.$route.name === 'Share'
									? '全部分享'
									: ''
						})
					}
				}
				return res
			},
			set() {
				return []
			}
    }
  },
  methods:{
    /**
		 * 点击面包屑导航栏空白处
		 */
		handleClickBreadCrumbSelf() {
			if (this.fileType === 0) {
				this.inputFilePath = this.filePath
				this.isShowInput = true
				this.$nextTick(() => {
					this.$refs.filePathInputRef.focus()
				})
			}
		},
		/**
		 * 路径输入框失去焦点或用户按下回车时触发
		 */
		handleInputBlurEnter() {
			this.isShowInput = false
			if (this.inputFilePath !== this.filePath) {
				this.$router.push({
					query: { fileType: 0, filePath: `${this.inputFilePath}` }
				}, onComplete => { }, onAbort => { })
			}
		},

    getRouteQuery(item){
      return { query: { fileType: this.fileType, filePath: item.path }}
    }
  }
};
</script>

<style lang="stylus" scoped>
@import '~@/assets/style/varibles.styl';

.bread-crumb-wrapper {
  height: 32px;
  line-height: 32px;
  display: flex;
  align-items: center;

  >>> .el-breadcrumb {
    height: 30px;
    line-height: 30px;
  }

  .file-path-input {
    flex: 1;
    font-size: 14px;
  }

  .breadcrumb-box {
    padding: 0 8px;
    flex: 1;
    display: flex;
    &.able-input {
      cursor: pointer;
      &:hover {
        background: $tabBackColor;
      }
    }
  }

}
</style>