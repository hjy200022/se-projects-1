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
              {{ student_Name }}
            </a>
            <div class="dropdown-menu" aria-labelledby="navbarDropdown">
              <router-link class="dropdown-item" to="usercenter"
                >个人中心</router-link
              >
              <div class="dropdown-divider"></div>
              <router-link class="dropdown-item" @click.native="logout" to=""
                >退出</router-link
              >
            </div>
          </li>
        </ul>
        <form class="form-inline my-2 my-lg-0">
          <input
            class="form-control mr-sm-2"
            type="search"
            placeholder="Search"
            aria-label="Search"
          />
          <button class="btn btn-outline-success my-2 my-sm-0" type="submit">
            Search
          </button>
        </form>
      </div>
    </nav>

    <!-- 导航栏下布局 -->
    <div class="container-fluid">
      <div class="row">
        <div class="col-3" @mouseenter="showMenu" @mouseleave="hideMenu">
          <el-menu
            :default-active="this.$route.path"
            class="el-menu-vertical-demo"
            :collapse="isCollapse"
            router
          >
            <el-menu-item index="/usercenter/personalAccount">
              <i class="el-icon-user"></i>
              <span slot="title">账户信息</span>
            </el-menu-item>
            <el-menu-item index="/usercenter/personalImformation">
              <i class="el-icon-user-solid"></i>
              <span slot="title">个人信息</span>
            </el-menu-item>
            <el-menu-item index="/usercenter/personalProgram">
              <i class="el-icon-s-order"></i>
              <span slot="title">查询报考项目</span>
            </el-menu-item>
            <el-menu-item index="/usercenter/personalNotice">
              <i class="el-icon-s-comment"></i>
              <span slot="title">个人通知</span>
            </el-menu-item>
            <div v-if="role">
              <div class="dropdown-divider"></div>
              <el-menu-item index="/usercenter/managerChangeRole">
                <i class="el-icon-help"></i>
                <span slot="title">用户角色</span>
              </el-menu-item>
              <el-menu-item index="/usercenter/managerGetUserInfo">
                <i class="el-icon-document-copy"></i>
                <span slot="title">获取普通用户信息</span>
              </el-menu-item>
              <el-menu-item index="/usercenter/managerGetLog">
                <i class="el-icon-printer"></i>
                <span slot="title">获取日志</span>
              </el-menu-item>
            </div>

            <div class="dropdown-divider"></div>
            <el-menu-item index="/">
              <i class="el-icon-back"></i>
              <span slot="title">返回主页</span>
            </el-menu-item>
          </el-menu>
        </div>
        <div class="col-9">
          <router-view></router-view>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState, mapActions } from "vuex";
import axios from "axios";
export default {
  name: "usercenter",
  data() {
    return {
      student_Name: "",
      username: "",
      authorization: "",
      role: false,
      //ei-menu 是否显示
      isCollapse: true,
    };
  },
  mounted: function () {
    this.getUser();
    this.getManagerPageMaxNum();
  },
  computed: {
    ...mapState({
      print: (state) => state.print.all,
    }),
  },
  methods: {
    getUser: function () {
      var that = this;
      if (this.print.username == null) {
        this.$router.push({ name: "login" });
        this.$message.error("登陆已失效，请重新登陆");
      } else {
        axios({
          headers: {
            Authorization: this.print.Authorization,
          },
          method: "get",
          params: {
            username: this.print.username,
          },
          url: "http://kana.chat:70/users/check",
        }).then(
          function (reponse) {
            that.role = reponse.data.data;
          },
          function (err) {
            console.log(err);
          }
        );
        axios({
          headers: {
            Authorization: this.print.Authorization,
          },
          method: "get",
          url: "http://kana.chat:70/userInfo?username=" + this.print.username,
        }).then(
          function (reponse) {
            that.student_Name = reponse.data.data.realName;
          },
          function (err) {
            that.student_Name = "你不配有名字";
          }
        );
      }
    },

    logout: function () {
      this.$store.commit("print/setPrint", {
        Authorization: "",
        username: "",
      });
      this.$router.push({
        name: "HelloWorld",
      });
    },

    showMenu: function () {
      this.isCollapse = false;
    },
    hideMenu: function () {
      this.isCollapse = true;
    },
  },
};
</script>