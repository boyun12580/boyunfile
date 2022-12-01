<template>
  <div class="select-column-main">

    <!-- 搜索栏 -->
    <el-input
      class="select-file-input"
      size="mini"
			maxlength="255"
      v-model="searchFile.fileName"
      placeholder="搜索您的文件"
			:clearable="true"
      @change="handleSearchInputChange(searchFile.fileName)"
			@clear="$emit('getTableDataByType')"
			@keyup.enter.native="handleSearchInputChange(searchFile.fileName)"
      v-if="fileType === 0"
    > 
      <i
				slot="prefix"
				class="el-input__icon el-icon-search"
				title="点击搜索"
				@click="handleSearchInputChange(searchFile.fileName)"
			></i>
    </el-input>

    <el-divider direction="vertical"></el-divider>
    <i
			class="refresh-icon el-icon-refresh"
			title="刷新文件列表"
			@click="$emit('getTableDataByType')"
		></i>
    <el-divider direction="vertical"></el-divider>
    <!-- 操作栏收纳 -->
		<el-popover
			placement="bottom"
			:trigger="screenWidth <= 768 ? 'manual' : 'hover'"
		>
      <i
				slot="reference"
				class="setting-icon el-icon-setting"
			></i>
      <div class="select-column">

        <div
          class="text"
          @click="handleClickSelectColumn"
          v-if="screenWidth > 768"
        >
        <i class="el-icon-s-operation"></i>
        设置显示字段</div>
  
        <!-- 对话框 当点击"设置显示列"按钮时弹出对话框 -->
        <el-dialog
          title="设置表格列显隐"
          width="700px"
          :append-to-body="true"
          :visible.sync="dialogVisible"
        >
          <!-- 多选框组件 勾选需要在表格中显示的列 -->
          <el-checkbox-group v-model="selectedColumn">
            <el-checkbox
              v-for="item in columnOptions"
              :key="item.value"
              :label="item.value"
              >{{ item.label }}</el-checkbox
            >
          </el-checkbox-group>
          <span slot="footer" class="dialog-footer">
            <el-button @click="dialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="dialogOk()">确 定</el-button>
          </span>
        </el-dialog>
      </div>
    </el-popover>

  </div>
</template>

<script>
export default {
  name: "SelectColumn",
  props: {
		// 文件类型
		fileType: {
			required: true,
			type: Number
		},
		// 文件路径
		filePath: {
			required: true,
			type: String
		}
	},
  data() {
    return {
      // 文件搜索数据
			searchFile: {
				fileName: ''
			},
      dialogVisible: false,
      selectedColumn: [], //  被选中的表格需要显示的列
      columnOptions: [
        {
          value: "extendName",
          label: "类型",
        },
        {
          value: "fileSize",
          label: "大小",
        },
        {
          value: "uploadTime",
          label: "修改日期",
        },
        {
          value: "deleteTime",
          label: "删除日期",
        },
      ],
    };
  },
  computed:{
    // 屏幕宽度
		screenWidth() {
			return this.$store.state.common.screenWidth
		},
  },
  methods: {
    /**
		 * 搜索输入框搜索事件
		 * @param {string} value 搜索内容
		 */
		handleSearchInputChange(value) {
			if (value === '') {
				this.$emit('getTableDataByType')
			} else {
				this.$emit('getSearchFileList', value)
			}
		},
    // 设置显示列按钮 - 点击事件
    handleClickSelectColumn() {
      // 1. 获取store中存储的表格显示列
      this.selectedColumn = this.$store.getters.selectedColumnList
      this.dialogVisible = true
    },
    // 对话框 确定按钮
    dialogOk() {
      // 2. 通过提交 mutation 更新表格显示列
      this.$store.commit('changeSelectedColumnList', this.selectedColumn)
      this.dialogVisible = false
      //console.log(this.selectedColumn)
    }
  },
};
</script>

<style lang="stylus" scoped>
@import '~@/assets/style/varibles.styl';

.select-column-main{
  display: flex;  
  align-items: center;
}

.select-column {
  .text {
    padding-top 8px
    cursor pointer
    &:hover {
      color $Primary
    }
  }
}

.select-file-input {
  margin-right: 8px;
  width: 250px;
  .el-icon-search {
    cursor: pointer;
    font-size: 16px;
    &:hover {
      color: $Primary;
    }
  }
}
.batch-icon,
.model-icon {
  margin-right: 8px;
  &:last-of-type {
    margin-right: 0;
  }
}
.model-icon.active {
  color: $Primary;
}
.refresh-icon, .batch-icon, .model-icon, .setting-icon {
  font-size: 20px;
  cursor: pointer;
  color: $SecondaryText;
  &:hover {
    color: $Primary;
  }
}
.batch-icon.active {
  color: $Primary;
}

.split-line {
  margin: 8px 0;
}

</style>
