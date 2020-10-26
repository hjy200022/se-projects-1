<template>
  <div>
    <el-button
      type="info"
      icon="el-icon-delete"
      @click="toggleSelection()"
      round
      >取消选择</el-button
    >
    <el-button
      type="primary"
      icon="el-icon-plus"
      @click="adddialogFormVisible = true"
      circle
    ></el-button>
    <el-button
      type="danger"
      icon="el-icon-delete"
      @click="deleteChannel"
      circle
    ></el-button>
    <el-button
      type="warning"
      icon="el-icon-edit"
      @click="beforeCheck"
      circle
    ></el-button>
    <el-button
      type="info"
      icon="el-icon-message"
      @click="sendChannelMessage"
      circle
    ></el-button>

    <!-- 发布频道的dialog -->
    <el-dialog title="频道发布" :visible.sync="adddialogFormVisible">
      <el-form :model="form">
        <el-form-item label="频道名称" :label-width="formLabelWidth">
          <el-input v-model="form.channel" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="考试id" :label-width="formLabelWidth">
          <el-input v-model="form.examTypeId" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="adddialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="addChannel">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 更改频道的dialog -->
    <el-dialog title="更新频道信息" :visible.sync="updatedialogFormVisible">
      <el-form :model="u_form">
        <el-form-item label="频道名称" :label-width="formLabelWidth">
          <el-input v-model="u_form.u_channel" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="考试id" :label-width="formLabelWidth">
          <el-input v-model="u_form.u_examTypeId" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="updatedialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="updateChannle">确 定</el-button>
      </div>
    </el-dialog>

    <el-table
      v-loading="loading"
      ref="multipleTable"
      :data="
        channelList.slice((currentPage - 1) * pagesize, currentPage * pagesize)
      "
      style="width: 100%"
      @selection-change="handleSelectionChange"
      tooltip-effect="dark"
    >
      <el-table-column type="selection" width="50" align="center" />
      <el-table-column
        type="index"
        label="序号"
        width="100"
        align="center"
      ></el-table-column>
      <el-table-column
        prop="channelId"
        label="频道id"
        width="300"
        align="center"
      >
      </el-table-column>
      <el-table-column prop="channel" label="频道" width="180" align="center">
      </el-table-column>
      <el-table-column
        prop="examTypeId"
        label="考试类型id（参考考试列表）"
        align="center"
      >
      </el-table-column>
    </el-table>
    <el-pagination
      align="center"
      @current-change="handleCurrentChange"
      :current-page="currentPage"
      :page-size="pagesize"
      background
      layout="total, prev, pager, next, jumper"
      :total="pageTotal"
    >
    </el-pagination>
  </div>
</template>

<script>
import axios from "axios";
import { mapState, mapActions } from "vuex";
export default {
  inject: ["reload"],
  name: "managerChannel",
  data() {
    return {
      channelList: [],
      loading: false,
      form: {
        channel: "",
        examTypeId: "",
      },
      //更新表
      u_form: {
        //需要id更新
        u_channelId: "",
        u_channel: "",
        u_examTypeId: "",
      },
      //初始页
      currentPage: 1,
      //每页的数据
      pagesize: 10,
      //数组总数
      pageTotal: 100000,
      //选中的数据
      multipleSelection: [],

      //添加 dialog
      adddialogFormVisible: false,
      //更新 dialog
      updatedialogFormVisible: false,

      formLabelWidth: "120px",
    };
  },
  computed: {
    ...mapState({
      print: (state) => state.print.all,
    }),
  },
  mounted: function () {
    this.getChannel();
  },
  methods: {
    getChannel: function () {
      this.loading = true;
      var that = this;
      axios({
        headers: {
          Authorization: this.print.Authorization,
        },
        method: "get",
        //限制页大小，待改善
        //参数 channel：测试频道 channeld：频道id examTypeId 考试类型id
        url: "http://kana.chat:70/channel?pageNum=0&pageSize=" + this.pageTotal,
      }).then(
        function (reponse) {
          that.pageTotal = reponse.data.data.length;
          that.channelList = reponse.data.data;
          that.loading = false;
        },
        function (err) {
          that.$message.error("获取失败");
          that.loading = false;
        }
      );
    },

    handleSelectionChange(val) {
      this.multipleSelection = val;
    },

    //取消选择
    toggleSelection(rows) {
      if (rows) {
        rows.forEach((row) => {
          this.$refs.multipleTable.toggleRowSelection(row);
        });
      } else {
        this.$refs.multipleTable.clearSelection();
      }
    },

    handleCurrentChange: function (currentPage) {
      this.currentPage = currentPage;
    },

    addChannel: function () {
      var that = this;
      axios({
        headers: {
          Authorization: this.print.Authorization,
          "Content-Type": "application/x-www-form-urlencoded",
        },
        method: "post",
        url: "http://kana.chat:70/channel",
        params: {
          channel: this.form.channel,
          examTypeId: this.form.examTypeId,
        },
      }).then(
        function (reponse) {
          that.$message({
            message: "发布成功",
            type: "success",
          });
          that.reload();
        },
        function (err) {
          that.$message.error("发布失败，请重新尝试");
        }
      );
    },

    deleteChannel: function () {
      var that = this;
      if (this.multipleSelection.length == 0) {
        this.$message({
          message: "未选中删除项",
          type: "warning",
        });
      } else {
        for (var i = 0; i < this.multipleSelection.length; i++) {
          console.log(this.multipleSelection[i]);
          axios({
            headers: { Authorization: this.print.Authorization },
            method: "delete",
            url: "http://kana.chat:70/channel",
            params: {
              channelId: this.multipleSelection[i].channelId,
            },
          }).then(
            function (reponse) {
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
        }
      }
    },

    beforeCheck: function () {
      if (this.multipleSelection.length > 1) {
        this.$message({
          message: "只能选中一个进行修改",
          type: "warning",
        });
      } else if (this.multipleSelection.length == 0) {
        this.$message({
          message: "请选中一个进行修改",
          type: "warning",
        });
      } else {
        this.updatedialogFormVisible = true;
        this.u_form.u_channelId = this.multipleSelection[0].channelId;
        this.u_form.u_channel = this.multipleSelection[0].channel;
        this.u_form.u_examTypeId = this.multipleSelection[0].examTypeId;
      }
    },

    updateChannle: function () {
      var that = this;
      console.log(this.u_form)
      axios({
        headers: { Authorization: this.print.Authorization },
        method: "put",
        url: "http://kana.chat:70/channel",
        data: {
          channelId: this.u_form.u_channelId,
          channel: this.u_form.u_channel,
          examTypeId: this.u_form.u_examTypeId,
        },
        transformRequest: [
          function (data) {
            let ret = "";
            for (let it in data) {
              ret +=
                encodeURIComponent(it) +
                "=" +
                encodeURIComponent(data[it]) +
                "&";
            }
            return ret;
          },
        ],
      }).then(
        function (reponse) {
          that.$message({
            message: "更改成功",
            type: "success",
          });
          that.reload();
        },
        function (err) {
          that.$message.error("更新失败，请重新尝试");
        }
      );
    },

    sendChannelMessage: function(){
      if (this.multipleSelection.length > 1) {
        this.$message({
          message: "只能选中一个频道发布消息",
          type: "warning",
        });
      } else if (this.multipleSelection.length == 0) {
        this.$message({
          message: "请选中一个进行频道发布消息",
          type: "warning",
        });
      } else {

      }
    }
  },
};
</script>

