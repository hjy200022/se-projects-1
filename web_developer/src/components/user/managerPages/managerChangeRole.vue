<template>
  <div>这是管理员更改用户角色页面</div>
</template>

<script>
import axios from "axios";
import { mapState, mapActions } from "vuex";
export default {
  inject: ["reload"],
  name: "managerChangeRole",
  data() {
    return {};
  },
  computed: {
    ...mapState({
      print: (state) => state.print.all,
    }),
  },
  mounted: function () {
    this.getRole();
  },
  methods: {
    getRole: function () {
      var that = this;
      axios({
        headers: {
          Authorization: this.print.Authorization,
        },
        method: "get",
        url: "http://kana.chat:70/roles",
      }).then(
        function (reponse) {
          that.roleList = reponse.data.data;
        },
        function (err) {
          that.$message.error("获取失败");
        }
      );
    },

    addRole: function () {
      var that = this;
      axios({
        headers: {
          Authorization: this.print.Authorization,
        },
        method: "post",
        url:
          "http://kana.chat:70/roles?name=" +
          this.name +
          "&description=" +
          this.description,
      }).then(
        function (repponse) {
          that.$message({
            message: "添加成功",
            type: "success",
          });
          that.reload();
        },
        function (err) {
          that.$message.error("添加失败，请重新尝试");
        }
      );
    },

    deleteRole: function () {
      var that = this;
      axios({
        headers: {
          Authorization: this.print.Authorization,
          "Content-Type": "application/x-www-form-urlencoded",
        },
        method: "delete",
        url:
          "http://kana.chat:70/roles?name=" +
          this.name +
          "&description=" +
          this.description,
      }).then(
        function (repponse) {
          that.$message({
            message: "删除成功",
            type: "success",
          });
          that.reload();
        },
        function (err) {
          that.$message.error("删除失败，请重新尝试");
        }
      );
    },

    updateRole: function () {
      var that = this;
      axios({
        headers: {
          Authorization: this.print.Authorization,
          "Content-Type": "application/x-www-form-urlencoded",
        },
        method: "put",
        url:
          "http://kana.chat:70/roles?name=" +
          this.name +
          "&description=" +
          this.description,
      }).then(
        function (repponse) {
          that.$message({
            message: "更改成功",
            type: "success",
          });
          that.reload();
        },
        function (err) {
          that.$message.error("更改失败，请重新尝试");
        }
      );
    },
  },
};
</script>