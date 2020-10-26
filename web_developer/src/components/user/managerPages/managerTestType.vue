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
      @click="deleteTestType"
      circle
    ></el-button>
    <el-button
      type="warning"
      icon="el-icon-edit"
      @click="beforeCheck"
      circle
    ></el-button>

    <!-- 添加考试的dialog -->
    <el-dialog title="添加考试" :visible.sync="adddialogFormVisible">
      <el-form :model="form">
        <el-form-item label="考试名称" :label-width="formLabelWidth">
          <el-input v-model="form.examTypeName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="考试描述" :label-width="formLabelWidth">
          <el-input
            v-model="form.examTypeDescription"
            autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-form-item label="考试限制" :label-width="formLabelWidth">
          <el-input v-model="form.examLimit" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="adddialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="addTestType">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 更改考试的dialog -->
    <el-dialog title="更新考试" :visible.sync="updatedialogFormVisible">
      <el-form :model="u_form">
        <el-form-item label="考试名称" :label-width="formLabelWidth">
          <el-input
            v-model="u_form.u_examTypeName"
            autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-form-item label="考试描述" :label-width="formLabelWidth">
          <el-input
            v-model="u_form.u_examTypeDescription"
            autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-form-item label="考试限制" :label-width="formLabelWidth">
          <el-input v-model="u_form.u_examLimit" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="updatedialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="updateTestType">确 定</el-button>
      </div>
    </el-dialog>

    <el-table
      v-loading="loading"
      ref="multipleTable"
      :data="
        testList.slice((currentPage - 1) * pagesize, currentPage * pagesize)
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
        prop="examTypeId"
        label="考试id"
        align="center"
        width="300"
      >
      </el-table-column>
      <el-table-column prop="examTypeName" label="考试名称" align="center">
      </el-table-column>
      <el-table-column
        prop="examTypeDescription"
        label="考试描述"
        align="center"
      >
      </el-table-column>
      <el-table-column prop="examLimit" label="考试限制" align="center">
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
  name: "test",
  data() {
    return {
      testList: [],
      loading: false,
      form: {
        //考试名称
        examTypeName: "",
        //考试描述
        examTypeDescription: "",
        //考试限制
        examLimit: "",
      },
      //更新表
      u_form: {
        //需要id更新
        u_examTypeId: "",
        u_examTypeName: "",
        u_examTypeDescription: "",
        u_examLimit: "",
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
    this.getTestType();
  },
  methods: {
    getTestType: function () {
      this.loading = true;
      var that = this;
      axios({
        headers: {
          Authorization: this.print.Authorization,
        },
        method: "get",
        //限制页大小，待改善
        //参数 examTypeName:考试类型 examTypeDescription:考试类型描述 examLimit:考试限制
        url: "http://kana.chat:70/exam",
        data: {
          pageNum: 0,
          pageSize: this.pageTotal,
        },
      }).then(
        function (reponse) {
          that.testList = reponse.data.data;
          that.pageTotal = reponse.data.data.length;
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

    addTestType: function () {
      var that = this;
      axios({
        headers: {
          Authorization: this.print.Authorization,
          "Content-Type": "application/x-www-form-urlencoded",
        },
        method: "post",
        url: "http://kana.chat:70/exam",
        params: {
          examTypeName: this.form.examTypeName,
          examTypeDescription: this.form.examTypeDescription,
          examLimit: this.form.examLimit,
        },
      }).then(
        function (reponse) {
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

    deleteTestType: function () {
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
            url: "http://kana.chat:70/exam",
            params: {
              examTypeId: this.multipleSelection[i].examTypeId,
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
        this.u_form.u_examTypeId = this.multipleSelection[0].examTypeId;
        this.u_form.u_examTypeName = this.multipleSelection[0].examTypeName;
        this.u_form.u_examTypeDescription = this.multipleSelection[0].examTypeDescription;
        this.u_form.u_examLimit = this.multipleSelection[0].examLimit;
      }
    },

    updateTestType: function () {
      var that = this;
      axios({
        headers: { Authorization: this.print.Authorization },
        method: "put",
        url: "http://kana.chat:70/exam",
        data: {
          examTypeName: this.u_form.u_examTypeName,
          examTypeDescription: this.u_form.u_examTypeDescription,
          examLimit: this.u_form.u_examLimit,
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
  },
};
</script>

