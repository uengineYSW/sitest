
import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router);


import ConnectLogServiceConnectLogManager from "./components/listers/ConnectLogServiceConnectLogCards"
import ConnectLogServiceConnectLogDetail from "./components/listers/ConnectLogServiceConnectLogDetail"

import CodeServiceCodeManager from "./components/listers/CodeServiceCodeCards"
import CodeServiceCodeDetail from "./components/listers/CodeServiceCodeDetail"


export default new Router({
    // mode: 'history',
    base: process.env.BASE_URL,
    routes: [
            {
                path: '/connectLogServices/connectLogs',
                name: 'ConnectLogServiceConnectLogManager',
                component: ConnectLogServiceConnectLogManager
            },
            {
                path: '/connectLogServices/connectLogs/:id',
                name: 'ConnectLogServiceConnectLogDetail',
                component: ConnectLogServiceConnectLogDetail
            },

            {
                path: '/codeServices/codes',
                name: 'CodeServiceCodeManager',
                component: CodeServiceCodeManager
            },
            {
                path: '/codeServices/codes/:id',
                name: 'CodeServiceCodeDetail',
                component: CodeServiceCodeDetail
            },



    ]
})
