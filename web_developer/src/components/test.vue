<template>
  <div>
    <router-link to="/">to homepage</router-link>
  </div>
</template>

<script>
import axios from "axios";
import { mapState, mapActions } from "vuex";
export default {
  inject: ["reload"],
  name: "test",
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

