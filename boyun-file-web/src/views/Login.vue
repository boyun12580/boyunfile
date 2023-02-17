<template>
  <div class="loginWrapper" id="loginBackground">

    <div class="formWrapper">
      <h1 class="loginTitle">登录</h1>
      <el-form
        :model="ruleForm"
        :rules="rules"
        ref="ruleForm"
        label-width="90px"
        class="demo-ruleForm"
        hide-required-asterisk
      >
        <el-form-item prop="telephone">
          <el-input
            prefix-icon="el-icon-mobile-phone"
            v-model="ruleForm.telephone"
            placeholder="手机号"
          ></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            prefix-icon="el-icon-lock"
            v-model="ruleForm.password"
            placeholder="密码"
            show-password
          ></el-input>
        </el-form-item>


        <el-form-item prop="code">
          <el-col :span="10">
            <el-input
              placeholder="请输入验证码"
              v-model="ruleForm.code"
            ></el-input
          ></el-col>
          <el-col :span="4">
            <div class="login-code" width="100%" @click="refreshCode">
              <!--验证码组件-->
              <dentify :identifyCode="identifyCode"></dentify>
            </div>
          </el-col>
        </el-form-item>

        <el-form-item class="loginButtonWrapper">
          <el-button
            class="loginButton"
            type="primary"
            @click="submitForm('ruleForm')"
          >
            登录
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import { login } from "@/request/user.js"; //    引入登录接口
import Cookies from "js-cookie";
import dentify from "@/components/Dentify.vue";

export default {
  name: "Login",
  components:{
    dentify
  },
  data() {
    return {
      ruleForm: {
        telephone: "",
        password: "",
        code:"",
      },
      identifyCodes: "1234567890abcdefjhijklinopqrsduvwxyz", //随机串内容,从这里随机抽几个显示验证码
      identifyCode: "", //验证码图片内容
      rules: {
        telephone: [
          { required: true, message: "请输入手机号", trigger: "blur" },
        ],
        password: [
          { required: true, message: "请输入密码", trigger: "blur" },
          {
            min: 5,
            max: 20,
            message: "长度在 5 到 20 个字符",
            trigger: "blur",
          },
        ],
        code: [
          { required: true, message: "请输入验证码", trigger: "blur" },
        ],
      },
    };
  },
  mounted(){
    // 初始化验证码
    this.identifyCode = "";
    //参数：（1）随机串内容。（2）验证码显示位数
    this.makeCode(this.identifyCodes, 4);
  },
  created() {
    if (this.$store.getters.isLogin) {
      // 用户若已登录，自动跳转到首页
      this.$notify({
          title: '成功',
          message: '您已登录！已跳转到首页',
          type: 'success'
      })
      this.$router.replace({
        name: "Home",
        query: { fileType: 0, filePath: "/" },
      });
    }
  },
  methods: {

    //  登录按钮-点击事件
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if(this.ruleForm.code !== "" && this.ruleForm.code === this.identifyCode){
            // 各项校验通过-调用登录接口
            login(this.ruleForm).then((res) => {
              if (res.success) {
                Cookies.set("token", res.data.token); //    在cookies中添加token
                this.$message.success("登录成功！");
                this.$refs[formName].resetFields(); //    清空表单项
                this.$router.replace({
                  name: "Home",
                  query: { fileType: 0, filePath: "/" },
                });
              } else {
                this.$message.error("手机号或密码错误！");
              }
            });
          }else{
            this.$message.error("验证码错误！");
            return false;
          }
        } else {
          this.$message.error("请完善信息！");
          return false;
        }
      });
    },
    // 重置验证码
    refreshCode() {
      this.identifyCode = "";
      this.makeCode(this.identifyCodes, 4);
    },
    //获取验证码的值
    makeCode(o, l) {
      for (let i = 0; i < l; i++) {
        //通过循环获取字符串内随机几位
        this.identifyCode +=
          this.identifyCodes[this.randomNum(0, this.identifyCodes.length)];
      }
    },
    //随机数字：用于当角标拿字符串的值
    randomNum(min, max) {
      return Math.floor(Math.random() * (max - min) + min);
    },
  },
};

</script>

<style lang="stylus" scoped>

.loginWrapper {
  // height: 550px !important;
  min-height: 550px !important;
  padding-top: 50px;

  .formWrapper {
    width: 375px;
    margin: 0 auto;
    text-align: center;

    .loginTitle {
      margin-bottom: 10px;
      font-weight: 300;
      font-size: 30px;
      color: #000;
    }

    .demo-ruleForm {
      width: 100%;
      margin-top: 20px;

      >>> .el-form-item__content {
        margin-left: 0 !important;
      }

      &>>> .el-input__inner {
        font-size: 16px;
      }

      .forgetPassword {
        text-align: right;
        margin: -22px 0 0 0;
      }

      .loginButtonWrapper {
        .loginButton {
          width: 100%;
        }

        &>>> .el-button {
          // padding: 10px 90px;
          font-size: 16px;
        }
      }
    }

    .tip {
      width: 70%;
      margin-left: 86px;
    }
  }
}



</style>