<template>
  <div>
    <router-link to="/homepage">to homepage</router-link><br />
    <el-table v-loading="loading" :data="channelList" style="width: 100%">
      <el-table-column
        prop="channelId"
        label="频道ID"
        width="300"
        align="center"
      >
      </el-table-column>
      <el-table-column
        prop="channel"
        label="频道名称"
        width="300"
        align="center"
      >
      </el-table-column>
      <el-table-column>
        <template slot-scope="scope">
          <el-button
            v-if="ifConcern"
            size="mini"
            @click="subscribe(scope.$index, scope.row)"
            >订阅</el-button
          >
          <el-button
            v-else
            size="mini"
            type="danger"
            @click="handleDelete(scope.$index, scope.row)"
            >取消订阅</el-button
          >
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import axios from "axios";
import { mapState, mapActions } from "vuex";
export default {
  inject: ["reload"],
  name: "test",
  data() {
    return {
      channelList: [],
      userChannelList: [],
      loading: false,
      ifConcern: true,
    };
  },
  computed: {
    ...mapState({
      print: (state) => state.print.all,
      userId: (state) => state.userId.all,
    }),
  },
  mounted: function () {
    this.getExam();
  },
  methods: {
    getExam: function () {
      this.loading = true;
      var that = this;

      axios({
        headers: { Authorization: this.print.Authorization },
        method: "get",
        url: "http://kana.chat:70/channel?pageNum=0&pageSize=10000",
      }).then(
        function (reponse) {
          that.channelList = reponse.data.data;
          //通过channel获得考试信息
          that.loading = false;
        },
        function (err) {
          that.$message.error("获取失败，请重新登陆");
          that.loading = false;
        }
      );

      //获取用户订阅的频道id;
      axios({
        headers: { Authorization: this.print.Authorization },
        method: "get",
        url:
          "http://kana.chat:70/userSub/user?pageNum&pageSize&userId=" +
          this.userId.userId,
      }).then(
        function (reponse) {
          that.userChannelList = reponse.data.data;
        },
        function (err) {
          that.$message.error("获取用户订阅频道列表失败");
        }
      );
    },

    subscribe(index, row) {
      console.log(index, row);
    },
    handleDelete(index, row) {
      console.log(index, row);
    },
  },
};
</script>

