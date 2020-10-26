import Vue from 'vue'
import Router from 'vue-router'
import HelloWorld from '@/components/HelloWorld'
import login from '../components/user/login'
import register from '../components/user/register'
import usercenter from '../components/user/usercenter'
/* usercenter子路由 */
/* 普通用户 */
import personalAccount from '../components/user/personalPages/personalAccount'
import personalImformation from '../components/user/personalPages/personalImformation'
import personalProgram from '../components/user/personalPages/personalProgram'
import personalNotice from '../components/user/personalPages/personalNotice'
/* 管理员 */
import managerChangeRole from '../components/user/managerPages/managerChangeRole'
import managerGetUserInfo from '../components/user/managerPages/managerGetUserInfo'
import managerTestType from '../components/user/managerPages/managerTestType'
import managerChannel from '../components/user/managerPages/managerChannel'
import managerGetLog from '../components/user/managerPages/managerGetLog'

import homepage from '../components/public/homepage'
import publicGetTest from '../components/public/publicGetTest'
/* 测试页面 */
import test from '../components/test'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'HelloWorld',
      component: HelloWorld,
      children: [
        {
          path: '/usercenter',
          name: 'usercenter',
          component: usercenter,
          children: [
            {
              path: 'personalAccount',
              name: 'personalAccount',
              component: personalAccount
            },
            {
              path: 'personalImformation',
              name: 'personalImformation',
              component: personalImformation
            },
            {
              path: 'personalProgram',
              name: 'personalProgram',
              component: personalProgram
            },
            {
              path: 'personalNotice',
              name: 'personalNotice',
              component: personalNotice
            },
            {
              path: 'managerChangeRole',
              name: 'managerChangeRole',
              component: managerChangeRole
            },
            {
              path: 'managerGetUserInfo',
              name: 'managerGetUserInfo',
              component: managerGetUserInfo
            },
            {
              path: 'managerTestType',
              name: 'managerTestType',
              component: managerTestType
            },
            {
              path: 'managerChannel',
              name: 'managerChannel',
              component: managerChannel
            },
            {
              path: 'managerGetLog',
              name: 'managerGetLog',
              component: managerGetLog
            },
          ]
        },
        {
          path: '/homepage',
          name: 'homepage',
          component: homepage
        },
        {
          path: '/publicGetTest',
          name: 'publicGetTest',
          component: publicGetTest
        },
      ]
    },
    {
      path: '/login',
      name: 'login',
      component: login
    },
    {
      path: '/register',
      name: 'register',
      component: register
    },
    {
      path: '/test',
      name: 'test',
      component: test,
    },
  ]
})
