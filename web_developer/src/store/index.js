import Vue from 'vue'
import Vuex from 'vuex'
import print from './print'

const debug = process.env.NODE_ENV !== 'production';

Vue.use(Vuex);
export default new Vuex.Store({
    modules: {
        print,
    },
})