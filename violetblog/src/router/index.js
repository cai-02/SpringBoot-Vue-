import Vue from 'vue'
import VueRouter from 'vue-router'
import Cookies from 'js-cookie'
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

// 导航守卫
router.beforeEach((to, from, next) => {
  const isAuthenticated = Cookies.get('rememberMe');
  const isAuthenticated2 = sessionStorage.getItem('success');
  if (isAuthenticated === undefined) {
    if (isAuthenticated2 !== "true") {  //无cookie未登录
      if (to.path !== '/login') {
        next('/login');  // 未登录时重定向到 '/login'
      } else {
        next();  // 已经在 /login 路由，正常访问
      }
    } else {                           //无cookie已登录
      next();
      if (to.path === '/user' && Cookies.get("role") !== "管理员") {
        alert("权限不足！");
        next('/index');
      }
    }
  } else {           //有cookie
    next();
    if (to.path === '/user' && Cookies.get("role") !== "管理员") {
      alert("权限不足！");
      next('/index');
    }
  }
});

export default router
