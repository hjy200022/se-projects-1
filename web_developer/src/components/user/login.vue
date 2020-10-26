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
            <router-link class="nav-link" to="/homepage"
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
              <router-link class="dropdown-item" to="/publicGetTest">考试频道</router-link>
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
          <label for="username">用户名：</label>
          <el-input type="text" v-model="username" placeholder="请输入用户名" />
        </div>
        <div class="form-group">
          <label for="password">密码：</label>
          <el-input
            type="password"
            v-model="password"
            placeholder="请输入密码"
            @keyup.enter="visible = !visible"
            show-password
          />
        </div>
        <div class="form-group form-check">
          <input type="checkbox" class="form-check-input" v-model="remember" />
          <label class="form-check-label">记住我</label>
        </div>
        <br />
        <input
          type="button"
          class="btn btn-primary"
          value="登陆"
          v-popover:popover
          @click="visible = !visible"
        />
        <input
          type="button"
          class="btn btn-primary"
          value="返回"
          @click="returnHistory"
        />
      </form>
    </div>

    <!--验证码弹窗-->
    <el-popover
      popper-class="slidingPictures"
      placement="bottom"
      ref="popover"
      trigger="manual"
      v-model="visible"
    >
      <div class="sliding-pictures">
        <div class="vimg">
          <canvas id="sliderBlock"></canvas>
          <canvas id="codeImg"></canvas>
        </div>
        <div class="slider">
          <div class="track" :class="{ pintuTrue: puzzle }">
            {{ tips }}
          </div>
          <div class="button el-icon-menu" @mousedown.prevent="drag"></div>
        </div>
        <div class="operation">
          <span
            title="关闭验证码"
            @click="visible = false"
            class="el-icon-circle-close"
          >
          </span>
          <span title="刷新验证码" @click="canvasInit" class="el-icon-refresh">
          </span>
        </div>
      </div>
    </el-popover>
  </div>
</template>

<script>
import axios from "axios";
export default {
  name: "login",
  data() {
    return {
      username: "",
      password: "",
      remember: false,
      authorization: "",

      tips: "拖动左边滑块完成上方拼图",
      rules: {},
      visible: false,
      //滑块x轴数据
      slider: {
        mx: 0,
        bx: 0,
      },
      //拼图是否正确
      puzzle: false,
    };
  },
  watch: {
    visible(e) {
      if (e === true) {
        this.canvasInit();
        this.puzzle = false;
      }
    },
  },
  methods: {
    returnHistory: function () {
      this.$router.go(-1);
    },

    login: function () {
      var that = this;
      if (this.username == "" || this.password == "") {
        this.$message({
          message: "用户名或密码未输入",
          type: "warning",
        });
      } else {
        let login_data = {
          username: this.username,
          password: this.password,
          rememberMe: this.remember,
        };
        axios.post("http://kana.chat:70/auth/login", login_data).then(
          function (reponse) {
            that.authorization = reponse.headers.authorization;
            //存到vuex store
            that.$store.commit("print/setPrint", {
              Authorization: that.authorization,
              username: that.username,
            });
            that.$message({
              message: "登陆成功",
              type: "success",
            });
            that.$router.push({
              name: "homepage",
            });
          },
          function (err) {
            that.$message.error("登陆失败");
          }
        );
      }
    },

    //拼图验证码初始化
    canvasInit() {
      //生成指定区间的随机数
      const random = (min, max) => {
        return Math.floor(Math.random() * (max - min + 1) + min);
      };
      //x: 254, y: 109
      let mx = random(127, 244),
        bx = random(10, 128),
        y = random(10, 99);
      this.slider = { mx, bx };
      this.draw(mx, bx, y);
    },

    //鼠标按下
    drag(e) {
      console.log("鼠标按下", e);
      let dom = e.target; //dom元素
      let slider = document.querySelector("#sliderBlock"); //滑块dom
      const downCoordinate = { x: e.x, y: e.y };
      //正确的滑块数据
      let checkx = Number(this.slider.mx) - Number(this.slider.bx);
      //x轴数据
      let x = 0;
      const move = (moveEV) => {
        x = moveEV.x - downCoordinate.x;
        //y = moveEV.y - downCoordinate.y;
        if (x >= 251 || x <= 0) return false;
        dom.style.left = x + "px";
        //dom.style.top = y + "px";
        slider.style.left = x + "px";
      };
      const up = () => {
        document.removeEventListener("mousemove", move);
        document.removeEventListener("mouseup", up);
        dom.style.left = "";
        console.log(x, checkx);
        let max = checkx - 5;
        let min = checkx - 10;
        //允许正负误差1
        if ((max >= x && x >= min) || x === checkx) {
          console.log("滑动解锁成功");
          this.puzzle = true;
          this.tips = "验证成功";
          setTimeout(() => {
            this.visible = false;
          }, 500);
          this.login();
        } else {
          console.log("拼图位置不正确");
          this.tips = "验证失败，请重试";
          this.puzzle = false;
          this.canvasInit();
        }
      };
      document.addEventListener("mousemove", move);
      document.addEventListener("mouseup", up);
    },
    draw(mx = 200, bx = 20, y = 50) {
      let mainDom = document.querySelector("#codeImg");
      let bg = mainDom.getContext("2d");
      let width = mainDom.width;
      let height = mainDom.height;
      let blockDom = document.querySelector("#sliderBlock");
      let block = blockDom.getContext("2d");
      //重新赋值，让canvas进行重新绘制
      blockDom.height = height;
      mainDom.height = height;

      //  let imgesrcList = [{ imgUrl:require("../assets/1.jpg")},
      //                     {imgUrl:require("../assets/3.jpg")}  ]

      let imgsrc1 = require("../../assets/1.jpg");
      let imgsrc2 = require("../../assets/2.jpg");
      let imgsrc3 = require("../../assets/3.jpg");
      let imgsrc4 = require("../../assets/4.jpg");
      var Arr = [imgsrc4, imgsrc1, imgsrc2, imgsrc3];
      var n = Math.floor(Math.random() * Arr.length + 1) - 1;
      let img = document.createElement("img");
      img.style.objectFit = "scale-down";
      img.src = Arr[n];
      console.log(n);
      img.onload = function () {
        bg.drawImage(img, 0, 0, width, height);
        block.drawImage(img, 0, 0, width, height);
      };
      let mainxy = { x: mx, y: y, r: 9 };
      let blockxy = { x: bx, y: y, r: 9 };
      this.drawBlock(bg, mainxy, "fill");
      this.drawBlock(block, blockxy, "clip");
    },

    //绘制拼图
    drawBlock(ctx, xy = { x: 254, y: 109, r: 9 }, type) {
      let x = xy.x,
        y = xy.y,
        r = xy.r,
        w = 40;
      let PI = Math.PI;
      //绘制
      ctx.beginPath();
      //left
      ctx.moveTo(x, y);
      //top
      ctx.arc(x + (w + 5) / 2, y, r, -PI, 0, true);
      ctx.lineTo(x + w + 5, y);
      //right
      ctx.arc(x + w + 5, y + w / 2, r, 1.5 * PI, 0.5 * PI, false);
      ctx.lineTo(x + w + 5, y + w);
      //bottom
      ctx.arc(x + (w + 5) / 2, y + w, r, 0, PI, false);
      ctx.lineTo(x, y + w);
      ctx.arc(x, y + w / 2, r, 0.5 * PI, 1.5 * PI, true);
      ctx.lineTo(x, y);
      //修饰，没有会看不出效果
      ctx.lineWidth = 1;
      ctx.fillStyle = "rgba(255, 255, 255, 0.5)";
      ctx.strokeStyle = "rgba(255, 255, 255, 0.5)";
      ctx.stroke();
      ctx[type]();
      ctx.globalCompositeOperation = "xor";
    },
  },
};
</script>

<style>
.slidingPictures {
  padding: 0;
  width: 300px;
  border-radius: 5px;
}

.vimg {
  width: 300px;
  height: 170px;
}
#codeImg,
#sliderBlock {
  padding: 7px 7px 0 7px;
  width: inherit;
  height: inherit;
}
#sliderBlock {
  position: absolute;
  z-index: 4000;
}
/* dsdsd */

.slider {
  width: 100%;
  height: 65px;
  border-bottom: #c7c9d0 1px solid;
  display: flex;
  align-items: center;
  justify-content: flex-start;
}
.track {
  margin-left: 7px;
  width: 286px;
  height: 38px;
  background: rgba(28, 136, 188, 0.5);
  border-radius: 25px;
  font-size: 14px;
  line-height: 38px;
  padding-right: 15px;
  padding-left: 70px;
}
.pintuTrue {
  background: #67c23a;
  color: #ffffff;
}
.button {
  position: absolute;
  width: 50px;
  height: 50px;
  line-height: 48px;
  background: #ffffff;
  box-shadow: #b9bdc8 0 0 3px;
  border-radius: 50%;
  left: 7px;
  text-align: center;
  font-size: 28px;
  color: #3e5d8b;
}
.operation span {
  color: #9fa3ac;
  display: inline-block;
  width: 40px;
  font-size: 25px;
  line-height: 40px;
  text-align: center;
}
</style>