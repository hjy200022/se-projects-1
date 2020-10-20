<template>
  <div class="container">
    <form v-if="ifUpdate">
      <fieldset disabled>
        <div class="form-group">
          <label for="disabledTextInput">用户名</label>
          <input type="text" class="form-control" :placeholder="userName" />
        </div>
        <div class="form-group">
          <label for="disabledTextInput">别名</label>
          <input type="text" class="form-control" :placeholder="fullName" />
        </div>
        <div class="form-group">
          <label for="disabledTextInput">用户ID</label>
          <input type="text" class="form-control" :placeholder="userId" />
        </div>
      </fieldset>
      <input
        type="button"
        class="btn btn-primary"
        value="更改信息"
        @click="changeIfUpdate"
      />
    </form>

    <form v-else>
      <fieldset>
        <div class="form-group">
          <label for="disabledTextInput">用户名</label>
          <input
            type="text"
            class="form-control"
            :placeholder="userName"
            readonly
          />
        </div>
        <div class="form-group">
          <label for="disabledTextInput">别名</label>
          <input type="text" class="form-control" v-model="u_fullName" />
        </div>
        <div class="form-group">
          <label for="disabledTextInput">密码</label>
          <input type="text" class="form-control" v-model="password" />
        </div>
        <div class="form-group">
          <label for="disabledTextInput">再次输入密码</label>
          <input type="text" class="form-control" v-model="passwordConfirm" />
        </div>
      </fieldset>
      <input
        type="button"
        class="btn btn-primary"
        value="取消更改"
        @click="changeIfUpdate"
      />
      <input
        type="button"
        class="btn btn-primary"
        value="更改"
        @click="changeFullName"
      />
    </form>
  </div>
</template>

<script>
import axios from "axios";
import { mapState, mapActions } from "vuex";
export default {
  inject: ["reload"],
  name: "personalAccount",
  data() {
    return {
      ifUpdate: true,
      userName: "",
      fullName: "",
      userId: "",

      u_fullName: "",
      password: "",
      passwordConfirm: "",
    };
  },
  computed: {
    ...mapState({
      print: (state) => state.print.all,
    }),
  },
  mounted: function () {
    this.getUserAccount();
  },
  methods: {
    getUserAccount: function () {
      var that = this;
      axios({
        headers: {
          Authorization: this.print.Authorization,
        },
        method: "get",
        url: "http://kana.chat:70/users/single?username=" + this.print.username,
      }).then(
        function (reponse) {
          //console.log(reponse.data.data);
          that.userName = reponse.data.data.userName;
          that.fullName = reponse.data.data.fullName;
          that.userId = reponse.data.data.userId;

          that.u_fullName = reponse.data.data.fullName;
        },
        function (err) {
          that.$message.error("获取失败");
        }
      );
    },

    changeIfUpdate: function () {
      this.ifUpdate = !this.ifUpdate;
      this.u_fullName = this.fullName;
    },

    changeFullName: function () {
      var that = this;
      let account = {
        userName: this.userName,
        fullName: this.u_fullName,
        password: this.password,
      };
      axios({
        headers: {
          Authorization: this.print.Authorization,
        },
        method: "put",
        data: account,
        url: "http://kana.chat:70/users",
      }).then(
        function (reponse) {
          that.reload();
          console.log("reload");
        },
        function (err) {
          that.$message.error("更改失败，请重新尝试");
        }
      );
    },
  },
};
</script>