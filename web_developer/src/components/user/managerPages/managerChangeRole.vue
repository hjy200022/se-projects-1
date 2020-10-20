<template>
    <div>
        这是管理员更改用户角色页面
    </div>
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
  mounted: function () {},
  methods: {},
};
</script>