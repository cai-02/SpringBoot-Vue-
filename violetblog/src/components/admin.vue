<template>
    <!-- 引入container布局 -->
    <el-container class="home-container">
        <el-header>
            <div>
                <img style="margin-left: 2px;" src="../assets/images/logo.jpg" alt />
                <span style="font-size: 22px; color: #18bf2d;">风之谷</span>
            </div>
            <el-button type="primary" @click="logout()">退出</el-button>
        </el-header>
        <!-- 主体 -->
        <el-container>
            <!-- 侧边栏 -->
            <el-aside class="asideWid" :width="isCollapse?'64px':'200px'">
                <div class="toggle-button" style="background-color: #fed7dd; color: #18bf2d;" @click="toggleCollapse()">|||</div>
                <el-menu background-color="white" text-color="black" active-text-color="#409eff" active-background-color="black" :collapse="isCollapse" 
                :collapse-transition="false" :router="true" :default-active="activePath">
                    <!-- 一级菜单 -->
                    <el-submenu :index="item.id+''" v-for="item in menuList" :key="item.id">
                        <template slot="title">
                            <i :class="iconsObject[item.id]"></i>
                            <span> {{item.title}}</span>
                        </template>
                        <!-- 二级菜单 -->
                        <el-menu-item :index="it.path" v-for="it in item.sList" :key="it.id" @click="saveNavState(it.path)">
                            <template slot="title">
                                <i :class="iconsObject[it.id]"></i>
                                <span> {{it.title}}</span>
                            </template>
                        </el-menu-item>
                    </el-submenu>
                </el-menu>
            </el-aside>
            <el-main>
                <router-view></router-view>
            </el-main>
        </el-container>
    </el-container>
</template>

<script>
import Cookies from 'js-cookie'

export default {
    data() {
        return {
            //菜单列表
            menuList:[],
            isCollapse: false,
            iconsObject: {
                '100':'el-icon-s-custom',
                '200':'el-icon-s-data',
                '201':'el-icon-user-solid',
                '202':'el-icon-message-solid',
                '101':'el-icon-s-order',
                '102':'el-icon-s-management',
            },
            activePath: '/message',   //默认路径
            userId: 0,
        }
    },
    // onload事件
    created() {
        this.userId = Cookies.get("userId");
        //查询menuList
        this.getMenuList();
        this.activePath = Cookies.get('activePath');  //取出cookie里的path，动态修改path
    },
    methods: {
        logout() {
            //window.sessionStorage.clear();   //清楚session
            this.$router.push("/index");
        },
        //获取导航菜单方法
        async getMenuList() {
            const{data:res} = await this.$http.get(`/menus?userId=${this.userId}`);
            if(res.flag != 200){
                return this.$message.error("获取列表失败！");
            }
            this.menuList = res.menus;    //访问成功数据回填
            //console.log(this.menuList)
        },
        //控制伸缩
        toggleCollapse() {
            this.isCollapse = !this.isCollapse;
        },
        //保存路径
        saveNavState(activePath) {
            Cookies.set('activePath', activePath, { expires: 7 });
            this.activePath = activePath;
        },
    },
    //监听路由，返回上一级时刷新左侧导航栏状态
    watch: {
        '$route'(){
            const herf = window.location.href.split("/");
            this.activePath = "/" + herf[herf.length - 1];
            //Cookies.set('activePath', activePath, { expires: 7 });
            this.getMenuList();
        }
    }
}
</script>

<style lang="less" scoped>
.home-container {
    height: 100%;
    overflow-y: hidden;
}

.el-header {
    background-color: #c1e6eb;
    display: flex;
    justify-content: space-between; //左右贴边
    padding-left: 0%; //左边界
    align-items: center;
    color: #fff;
    font-size: 20px;

    >div {
        display: flex;
        align-items: center;

        span {
            margin-left: 15px;
        }
    }
}
.el-menu .el-menu-item{
    font-size: 18px !important;
}
.el-submenu .el-submenu__title span{
    font-size: 18px !important;
}

.el-aside {
    background-color: #c1e6eb;
    .el-menu{
        border-right: none;
    }
}

.el-main {
    background-color: #eaedf1;
}

img {
    width: 55px;
    height: 55px;
}

//按钮样式
.toggle-button{
    background-color: #4A5064;
    font-size: 10px;
    line-height: 24px;
    color: white;
    text-align: center;
    letter-spacing: 0.2em;
    cursor: pointer;
}
/deep/.el-menu .el-menu-item:hover{
    background: #e0fff1 !important;
}
.el-submenu /deep/.el-submenu__title:hover {
  background: #e0fff1 !important;
}
/deep/.el-menu .el-menu-item.is-active {
    background: #d0fbf1 !important;
}

/* 手机端样式 */
@media screen and (max-width: 767px) {
  .asideWid{
    display: none;
  }
  
}
</style>