<template>
  <div>
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
                <span slot="title">获取用户信息</span>
              </el-menu-item>
              <el-menu-item index="/usercenter/managerTestType">
                <i class="el-icon-edit-outline"></i>
                <span slot="title">管理考试</span>
              </el-menu-item>
              <el-menu-item index="/usercenter/managerChannel">
                <i class="el-icon-monitor"></i>
                <span slot="title">管理频道</span>
              </el-menu-item>
              <el-menu-item index="/usercenter/managerGetLog">
                <i class="el-icon-printer"></i>
                <span slot="title">获取日志</span>
              </el-menu-item>
            </div>

            <div class="dropdown-divider"></div>
            <el-menu-item index="/homepage">
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
      role: false,
      //ei-menu 是否显示
      isCollapse: true,
    };
  },
  mounted: function () {
    this.getUser();
  },
  computed: {
    ...mapState({
      print: (state) => state.print.all,
    }),
  },
  methods: {
    getUser: function () {
      var that = this;
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