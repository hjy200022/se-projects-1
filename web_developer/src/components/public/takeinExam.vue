<template>
  <div class="container">
    <el-header style="text-align: left; font-size: 30px">
      <span>{{ examDescription }}报名页面</span>
    </el-header>
    <el-main>
      <el-steps :active="active" finish-status="success">
        <el-step title="步骤 1"></el-step>
        <el-step title="步骤 2"></el-step>
        <el-step title="步骤 3"></el-step>
      </el-steps>
      <template v-if="step == 0"> 请阅读考试须知。。。略 </template>
      <template v-if="step == 1"> 确认个人信息 </template>
      <template v-if="step == 2"> 选择考场 </template>
      <template v-if="step == 3"> 已完成所有步骤，请点击完成报名 </template>
    </el-main>
    <el-footer style="text-align: center">
      <template v-if="active < 3">
        <el-button style="margin-top: 12px" @click="prev">上一步</el-button>
        <el-button style="margin-top: 12px" @click="next">下一步</el-button>
      </template>
      <template v-else>
        <el-button style="margin-top: 12px" @click="takeinExam"
          >完成报名</el-button
        >
      </template>
    </el-footer>
  </div>
</template>

<script>
import axios from "axios";
import { mapState, mapActions } from "vuex";
export default {
  inject: ["reload"],
  name: "takeinExam",
  data() {
    return {
      examEntryId: this.$route.params.examEntryId,
      examDescription: this.$route.params.examDescription,
      active: 0,
      step: 0,
    };
  },
  computed: {
    ...mapState({
      print: (state) => state.print.all,
      userId: (state) => state.userId.all,
    }),
  },
  mounted: function () {
    console.log(this.examEntryId);
    console.log(this.examDescription);
    var that = this;
    setTimeout(function () {}, 300);
  },
  methods: {
    prev() {
      if (this.active == 0) {
        this.$router.go(-1);
      } else {
        this.active--;
        this.checkStep(this.active);
      }
    },
    next() {
      this.active++;
      this.checkStep(this.active);
    },

    checkStep: function (active) {
      if (active == 0) {
        this.step = 0;
      } else if (active == 1) {
        this.step = 1;
      } else if (active == 2) {
        this.step = 2;
      } else if (active == 3) {
        this.step = 3;
      }
    },

    takeinExam: function () {
        //url userExamEntry
        //userid
        //examEntryId
        this.$message({
            message:"报名成功",
            type:"success"
        })
        this.$router.push({name:"publicGetExam"})
    },
  },
};
</script>