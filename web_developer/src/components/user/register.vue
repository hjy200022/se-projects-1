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
            <router-link class="nav-link" to="/"
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
              <a class="dropdown-item" href="#">Another action</a>
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
          <input
            type="text"
            class="form-control"
            v-model="userName"
            placeholder="请输入用户名"
          />
        </div>
        <div class="form-group">
          <label for="fullName">别名</label>
          <input
            type="text"
            class="form-control"
            v-model="fullName"
            placeholder="请输入别名"
          />
        </div>
        <div class="form-group">
          <label for="password">密码</label>
          <input
            type="password"
            class="form-control"
            v-model="password"
            placeholder="请输入密码"
          />
        </div>
        <div class="form-group">
          <label for="password_confirm">密码验证</label>
          <input
            type="password"
            class="form-control"
            v-model="password_confirm"
            placeholder="请再次输入密码"
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
        this.$message.error("有未输入的栏位！");
      } else {
        if (this.password != this.password_confirm) {
          this.$message.error("两次密码输入不一致");
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