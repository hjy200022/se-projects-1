<template>
  <div>
    <nav
      class="navbar navbar-expand-lg navbar-light fixed-top"
      style="background-color: #e3f2fd"
    >
      <a class="navbar-brand" href="#">考试管理</a>
      <button
        class="navbar-toggler"
        type="button"
        data-toggle="collapse"
        data-target="#navbarSupportedContent"
        aria-controls="navbarSupportedContent"
        aria-expanded="false"
        aria-label="Toggle navigation"
      >
        <span class="navbar-toggler-icon"></span>
      </button>

      <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
          <li class="nav-item active">
            <router-link class="nav-link" to="/homepage"
              >主页 <span class="sr-only">(current)</span></router-link
            >
          </li>
          <li class="nav-item dropdown">
            <a
              class="nav-link dropdown-toggle"
              href="#"
              id="navbarDropdown"
              role="button"
              data-toggle="dropdown"
              aria-haspopup="true"
              aria-expanded="false"
            >
              考试报名
            </a>
            <div class="dropdown-menu" aria-labelledby="navbarDropdown">
              <a class="dropdown-item" href="#">报名中心</a>
              <router-link class="dropdown-item" to="/publicGetTest">考试频道</router-link>
              <div class="dropdown-divider"></div>
              <a class="dropdown-item" href="#">Something else here</a>
            </div>
          </li>
        </ul>
      </div>
    </nav>

    <div class="container">
      <form action="">
        <div class="form-group">
          <label for="userName">用户名</label>
          <el-input type="text" v-model="userName" placeholder="请输入用户名" />
        </div>
        <div class="form-group">
          <label for="fullName">别名</label>
          <el-input type="text" v-model="fullName" placeholder="请输入别名" />
        </div>
        <div class="form-group">
          <label for="password">密码</label>
          <el-input
            type="password"
            v-model="password"
            placeholder="请输入8-16位包含至少一个大小写英文字母的密码"
            show-password
          />
        </div>
        <div class="form-group">
          <label for="password_confirm">密码验证</label>
          <el-input
            type="password"
            v-model="password_confirm"
            placeholder="请再次输入密码"
            show-password
            @keyup.enter="register"
          />
        </div>
        <input
          type="button"
          class="btn btn-primary"
          value="注册"
          :plain="true"
          @click="register"
        />
        <input
          type="button"
          class="btn btn-primary"
          value="返回"
          @click="returnHistory"
        />
      </form>
    </div>
  </div>
</template>

<script>
import axios from "axios";
export default {
  name: "register",
  data() {
    return {
      userName: "",
      fullName: "",
      password: "",
      password_confirm: "",
    };
  },
  methods: {
    returnHistory: function () {
      this.$router.go(-1);
    },

    register: function () {
      var that = this;
      if (
        this.userName == "" ||
        this.fullName == "" ||
        this.password == "" ||
        this.password_confirm == ""
      ) {
        this.$message({
          message: "有未输入的栏位",
          type: "warning",
        });
      } else if (this.password != this.password_confirm) {
        this.$message({
          message: "两次密码输入不一致",
          type: "warning",
        });
      } else {
        // 没有检测完善，只有密码的正则表达式
        var pwdCheck = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[^]{8,16}$/;
        if (!pwdCheck.test(this.password)) {
          this.$message({
          message: "密码格式不正确",
          type: "warning",
        });
        } else {
          let register_data = {
            userName: this.userName,
            fullName: this.fullName,
            password: this.password,
          };
          axios.post("http://kana.chat:70/users/sign-up", register_data).then(
            function (reponse) {
              that.$message({
                message: "注册成功，请重新登陆",
                type: "success",
              });
              that.$router.push({
                name: "login",
                params: {
                  username: that.userName,
                  password: that.password,
                },
              });
            },
            function (err) {
              that.$message.error(err);
            }
          );
        }
      }
    },
  },
};
</script>

<style>
</style>