<template>
  <div class="container">
    <form v-if="ifUpdate">
      <fieldset disabled>
        <div class="form-group">
          <label for="disabledTextInput">姓名</label>
          <input
            type="text"
            id="disabledTextInput"
            class="form-control"
            :placeholder="realName"
          />
        </div>
        <div class="form-group">
          <label for="disabledTextInput">学号</label>
          <input
            type="text"
            id="disabledTextInput"
            class="form-control"
            :placeholder="stuNo"
          />
        </div>
        <div class="form-group">
          <label for="disabledTextInput">专业</label>
          <input
            type="text"
            id="disabledTextInput"
            class="form-control"
            :placeholder="major"
          />
        </div>
        <div class="form-group">
          <label for="disabledTextInput">专业班级</label>
          <input
            type="text"
            id="disabledTextInput"
            class="form-control"
            :placeholder="className"
          />
        </div>
        <div class="form-group">
          <label for="disabledTextInput">身份证号码</label>
          <input
            type="text"
            id="disabledTextInput"
            class="form-control"
            :placeholder="identificationNumber"
          />
        </div>
      </fieldset>
      <input
        type="button"
        class="btn btn-primary"
        value="更改信息"
        @click="changeIfUpdate"
      />
    </form>

    <form v-if="!ifUpdate">
      <fieldset>
        <div class="form-group">
          <label for="disabledTextInput">姓名</label>
          <input type="text" class="form-control" v-model="u_realName" />
        </div>
        <div class="form-group">
          <label for="disabledTextInput">学号</label>
          <input type="text" class="form-control" v-model="u_stuNo" />
        </div>
        <div class="form-group">
          <label for="disabledTextInput">专业</label>
          <input type="text" class="form-control" v-model="u_major" />
        </div>
        <div class="form-group">
          <label for="disabledTextInput">专业班级</label>
          <input type="text" class="form-control" v-model="u_className" />
        </div>
        <div class="form-group">
          <label for="disabledTextInput">身份证号码</label>
          <input
            type="text"
            class="form-control"
            v-model="u_identificationNumber"
          />
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
        value="更改信息"
        data-toggle="modal"
        data-target="#confirmUpdateImformation"
      />

      <!-- Modal -->
      <div
        class="modal fade"
        id="confirmUpdateImformation"
        tabindex="-1"
        role="dialog"
        aria-labelledby="exampleModalLabel"
        aria-hidden="true"
      >
        <div class="modal-dialog">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title" id="exampleModalLabel">提示</h5>
              <button
                type="button"
                class="close"
                data-dismiss="modal"
                aria-label="Close"
              >
                <span aria-hidden="true">&times;</span>
              </button>
            </div>
            <div class="modal-body">请确认信息填写正确再保存信息</div>
            <div class="modal-footer">
              <button
                type="button"
                class="btn btn-secondary"
                data-dismiss="modal"
              >
                关闭
              </button>
              <input
                type="button"
                class="btn btn-primary"
                value="保存更新"
                @click="updateInformation"
              />
            </div>
          </div>
        </div>
      </div>
    </form>
  </div>
</template>

<script>
import { mapState, mapActions } from "vuex";
import axios from "axios";
export default {
  name: "personalImformation",
  data() {
    return {
      realName: "",
      stuNo: "",
      major: "",
      className: "",
      identificationNumber: "",
      ifUpdate: true,

      //更新专用，判断是录入还是更新
      u_realName: "",
      u_stuNo: "",
      u_major: "",
      u_className: "",
      u_identificationNumber: "",
    };
  },
  mounted: function () {
    this.getImformation();
  },
  computed: {
    ...mapState({
      print: (state) => state.print.all,
    }),
  },
  methods: {
    getImformation: function () {
      var that = this;
      axios({
        headers: {
          Authorization: this.print.Authorization,
        },
        method: "get",
        url: "http://kana.chat:70/userInfo?username=" + this.print.username,
      }).then(
        function (reponse) {
          if (reponse.data.data != null) {
            that.realName = reponse.data.data.realName;
            that.stuNo = reponse.data.data.stuNo;
            that.major = reponse.data.data.major;
            that.className = reponse.data.data.className;
            that.identificationNumber = reponse.data.data.identificationNumber;

            that.u_realName = reponse.data.data.realName;
            that.u_stuNo = reponse.data.data.stuNo;
            that.u_major = reponse.data.data.major;
            that.u_className = reponse.data.data.className;
            that.u_identificationNumber =
              reponse.data.data.identificationNumber;
          }
        },
        function (err) {
          that.$message.error("你没有信息，请尽快填写");
        }
      );
    },

    changeIfUpdate: function () {
      this.ifUpdate = !this.ifUpdate;
      this.u_realName = this.realName;
      this.u_stuNo = this.stuNo;
      this.u_major = this.major;
      this.u_className = this.className;
      this.u_identificationNumber = this.identificationNumber;
    },

    updateInformation: function () {
      var that = this;
      if (this.print.Authorization == "") {
        this.$message.error("登陆失效，请重新登陆");
        this.$router.push({ name: "HelloWorld" });
      }
      if (
        this.u_realName == this.realName &&
        this.u_stuNo == this.stuNo &&
        this.u_major == this.major &&
        this.u_className == this.className &&
        this.u_identificationNumber == this.identificationNumber
      ) {
        this.$message.error('更改失败');
      } else {
        //判断是录入还是更新
        //只要有一个空值就是录入
        let update_data = {
          username: this.print.username,
          className: this.u_className,
          identificationNumber: this.u_identificationNumber,
          major: this.u_major,
          realName: this.u_realName,
          stuNo: this.u_stuNo,
        };
        if (this.realName == "") {
          console.log("录入 post");
          //录入 post请求
          axios({
            method: "post",
            url: "http://kana.chat:70/userInfo",
            data: update_data,
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
            headers: {
              Authorization: this.print.Authorization,
            },
          }).then(
            function (reponse) {
              that.$message({
                message: "更改成功",
                type: "success",
              });
              that.$router.go(0);
            },
            function (err) {
              that.$message.error('更改失败');
            }
          );
        } else {
          console.log("更新 put");
          //更新 put请求
          axios({
            method: "put",
            url: "http://kana.chat:70/userInfo",
            data: update_data,
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
            headers: {
              Authorization: this.print.Authorization,
            },
          }).then(
            function (reponse) {
              that.$message({
                message: "更改成功",
                type: "success",
              });
              that.$router.go(0);
            },
            function (err) {
              that.$message.error('更改失败');
            }
          );
        }
      }
    },
  },
};
</script>