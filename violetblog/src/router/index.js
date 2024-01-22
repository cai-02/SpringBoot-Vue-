import Vue from 'vue'
import VueRouter from 'vue-router'
//引入login组件
import Test from '../components/test.vue'
import Login from '../components/Login.vue'
import Admin from '../components/admin.vue'
import UserList from '../components/admin/UserList.vue'
import ArticleList from '../components/admin/articleList.vue'
import Index from '../components/index.vue'
import Message from '../components/admin/message.vue'
import Publish from '../components/publish.vue'
import Notes from '../components/notes.vue'
import Category from '../components/category.vue'
import Catemanage from '../components/catemanage.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: "/",
    redirect: "/login"
  },
  {
    path: "/login",
    component: Login
  },
  {
    path: "/test",
    component: Test
  },
  {
    path: "/admin",
    component: Admin,
    redirect: "/message",
    children: [
      { path: "/message", component: Message },
      { path: "/user", component: UserList },
      { path: "/article", component: ArticleList },
    ]
  },
  {
    path: "/index",
    component: Index
  }
  ,
  {
    path: "/publish",
    component: Publish
  }
  ,
  {
    path: "/notes",
    component: Notes
  }
  ,
  {
    path: "/category/:id",
    component: Category
  }
  ,
  {
    path: "/catemanage",
    component: Catemanage
  }
  ,
]

const router = new VueRouter({
  // scrollBehavior(to, from, savedPosition) {
  //   return {
  //     x: 0,
  //     y: 0
  //   }
  // },
  routes
})
//挂载路由导航守卫
/*router.beforeEach((to, from, next)=>{
  //to 将要访问
  //from 从哪儿访问
  //next 接着干的事
  if(to.path=='/login') return next();
  //获取user
  const userFlag = window.sessionStorage.getItem("user");  //取出当前用户
  if(!userFlag) return next("/login");  //无值返回登录页
  next();   //符合要求，放行
})*/

export default router
