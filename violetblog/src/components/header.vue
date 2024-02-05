<template>
    <el-header :class="{ 'hidden': isNavHidden }">
        <div>
            <span style="float: left; font-size: 22px; color: #fee4e0; font-weight: bold;">风之谷</span>
        </div>
        <!-- 头像 -->
        <div style="float: right;">
            <el-popover placement="top-start" width="" trigger="hover">
                <el-button class="phoneHou" style="display: none; margin-bottom: 8px;" type="success" plain @click="intoSystem()">资料</el-button>
                <el-button type="warning" plain @click="exit()" style="margin-left: 0;">退出</el-button>
                <div slot="reference" style="margin-top: 7px; margin-right: 8px; height: 45px; margin-left: 5px;">
                    <img :src="this.headImg" width="45px" height="45px" style="border-radius: 50%;" alt />
                </div>
            </el-popover>
        </div>
        <!-- 菜单 -->
        <el-menu :default-active="activeIndex" :key="activeIndex" class="el-menu-demo" mode="horizontal"
            @select="handleSelect" background-color="rgb(0, 0, 0, 0)" text-color="#fff" active-text-color="#fee4e0"
            style="margin: 0;">
            <el-menu-item prefix-icon="el-icon-user" index="1" @click="menuSearch()">
                <svg class="icon" aria-hidden="true">
                    <use xlink:href="#icon-sousuo"></use>
                </svg>
                搜索
            </el-menu-item>
            <el-menu-item class="shou" index="2" @click="shouye()">
                <svg class="icon" aria-hidden="true">
                    <use xlink:href="#icon-shouye1"></use>
                </svg>
                首页
            </el-menu-item>
            <el-menu-item class="" index="3" @click="category()">
                <svg class="icon" aria-hidden="true">
                    <use xlink:href="#icon-fenlei-"></use>
                </svg>
                类别
            </el-menu-item>
            <el-menu-item class="liu" index="4">
                <svg class="icon" aria-hidden="true">
                    <use xlink:href="#icon-liuyan"></use>
                </svg>
                留言
            </el-menu-item>
            <el-menu-item class="xiang" index="5">
                <svg class="icon" aria-hidden="true">
                    <use xlink:href="#icon-xiangce"></use>
                </svg>
                相册
            </el-menu-item>
            <el-menu-item class="guan" index="6">
                <svg class="icon" aria-hidden="true">
                    <use xlink:href="#icon-guanyuwomen"></use>
                </svg>
                关于
            </el-menu-item>
            <el-menu-item class="hou" index="7" @click="intoSystem">
                <svg class="icon" aria-hidden="true">
                    <use xlink:href="#icon-houtai"></use>
                </svg>
                后台
            </el-menu-item>
        </el-menu>
        <!-- 搜索框 -->
        <el-dialog :visible.sync="centerDialogVisible" :modal="false" width="35%" background="rgb(255,255,255,0.9)" @close="cleanS()">
            <div>
                <el-input placeholder="请输入笔记标题" v-model="artTitle" clearable @clear="cleanEnt()">
                    <el-button slot="append" icon="el-icon-search" style="color: black;" @click="searchNote()"></el-button>
                </el-input>
            </div>
            <div>
                <div v-if="noneC">
                    <span style="font-size: 17px;">抱歉！没有找到您想要的内容</span>
                </div>
                <div v-if="visC" style="text-align: left; font-size: 17px;">
                    <ul class="searchUl"
                        style="list-style-type: none; padding-left: 0; margin: 0;">
                        <li v-for="(item, index) in searchNotes" :key="index" @click="toArticle(item.noteId)">{{ item.title }}</li>
                    </ul>
                </div>
            </div>
        </el-dialog>
        <!-- 搜索框 -->
        <!-- <el-card class="el-card cc" v-if="visSearch"
            style="width: 35%; min-height: 300px; margin: 70px auto; background: rgb(2552, 255, 255, 0.9); border-radius: 8px;">
            <el-col :span="10" style="width: 100%;">
                <el-input placeholder="请输入用户名" v-model="queryInfo.query" clearable @clear="">
                    <el-button slot="append" icon="el-icon-search" style="color: black;" @click=""></el-button>
                </el-input>
            </el-col>
        </el-card> -->
    </el-header>
</template>
  
<script>
import VueScrollTo from 'vue-scrollto';
import Cookies from 'js-cookie'

export default {
    data() {
        return {
            headImg: "",
            activeIndex: "2",
            isNavHidden: false,
            prevScrollPos: 0,
            //查询信息封装的实体
            artTitle: "",       //查询信息
            artPageStart: 1,     //起始页
            artPageSize: 100,    //每页文章数
            //搜索框隐藏显示
            centerDialogVisible: false,
            noneC: false,
            visC: false,
            searchNotes: [],
            articleCounts: 0,
        };
    },
    mounted() {
        // 监听滚动事件
        window.addEventListener('scroll', this.handleScroll);
    },
    beforeDestroy() {
        // 在组件销毁前移除滚动事件监听
        window.removeEventListener('scroll', this.handleScroll);
    },
    created() {
        if (JSON.parse(Cookies.get("user")) != null && Cookies.get("headimage") != null) {
            this.headImg = Cookies.get("headimage");
        } else {
            return;
        }
        //导航栏变化
        const herf = window.location.href.split("/");
        const herf2 = herf[herf.length - 1];
        if (herf[herf.length - 1] == 'publish' || herf2.split("?")[0] == "notes") {
            this.activeIndex = '0';
        } else if (herf[herf.length - 1] == 'catemanage') {
            this.activeIndex = '3';
        }

        this.userId = Cookies.get("userId");
    },
    methods: {
        // 进入后台
        intoSystem() {
            this.activePath = Cookies.get('activePath');  //取出cookie里的path，动态修改path
            if (this.activePath != null) {
                this.$router.push({ path: this.activePath });
            } else {
                this.$router.push({ path: "/admin" });
            }
        },
        handleSelect(key, keyPath) {
        },
        //退出登录
        exit() {
            this.$router.push("/login");
            Cookies.remove('user');
            Cookies.remove('rememberMe');
            Cookies.remove('success');
            Cookies.remove("user");
            Cookies.remove("userId");
            Cookies.remove("headimage");
            Cookies.remove("activePath");
            Cookies.remove("role");
            sessionStorage.clear("success");
        },
        //搜索笔记
        async searchNote() {
            if (this.artTitle === "") {
                this.$message.warning('搜索内容不能为空！');
            } else {
                const { data: res } = await this.$http.get(`getArticle?userId=${this.userId}&title=${this.artTitle}&pageStart=${this.artPageStart}&pageSize=${this.artPageSize}`);
                this.searchNotes = res.data;
                if (this.searchNotes.length === 0) {
                    this.noneC = true;
                    this.visC = false;
                } else {
                    this.visC = true;
                    this.noneC = false;
                }
            }
        },
        //去到笔记
        toArticle(id) {
            if(this.$route.fullPath != ('/notes?id=' + id)){  //判断导航是否重复
                this.$router.push({ path: '/notes?id=' + id });
            }else{
                location.reload();
            }
            this.cleanS();
            this.centerDialogVisible = false;
        },
        //搜索框关闭回调
        cleanS() {
            this.artTitle = "";
            this.searchNotes = [];
            this.noneC = false;
            this.visC = false;
        },
        //清除回调
        cleanEnt() {
            this.searchNotes = [];
            this.noneC = false;
            this.visC = false;
        },
        //回到首页
        shouye() {
            if (this.$route.path == "/index") {
                location.reload();
            } else {
                this.$router.push({ path: '/index' })
            }
        },
        //去到分类页
        category() {
            if (this.$route.path == "/catemanage") {
                location.reload();
            } else {
                this.$router.push({ path: '/catemanage' })
            }
        },
        //搜索
        menuSearch() {
            this.centerDialogVisible = true;
        },
        //导航栏显示隐藏
        handleScroll() {
            // 获取当前滚动的垂直位置
            const currentScrollPos = window.scrollY;

            // 判断滚动方向
            if (currentScrollPos > this.prevScrollPos) {
                // 向下滚动，隐藏导航栏
                this.isNavHidden = true;
                this.centerDialogVisible = false;
            } else {
                // 向上滚动，显示导航栏
                this.isNavHidden = false;
            }

            // 更新上一次滚动的位置
            this.prevScrollPos = currentScrollPos;
        },
    },
}
</script>

<style lang="less" scoped>
.el-menu .el-menu-item:hover {
    color: #2E95FB !important;
    background: rgb(254, 228, 224) !important;
}

.el-menu {
    float: right;
    border: none !important;
}

.el-menu .el-menu-item {
    background-color: rgba(0, 0, 0, 0) !important;
    font-size: 18px !important;
    margin-left: -3px;
}

.el-menu .el-menu-item:focus {
    color: #fee4e0 !important;
    border-bottom-color: #fee4e0 !important;
}

.el-header {
    color: #333;
    text-align: center;
    line-height: 60px;
    position: fixed;
    width: 100%;
    padding-left: 10px;
    padding-right: 10px;
    justify-content: space-between;
    background-color: rgba(0, 0, 0, 0.4);
    z-index: 1;
}

.hidden {
    transform: translateY(-100%);
    transition: transform 0.3s ease-in-out;
}

.searchUl li {
    line-height: 40px;
    margin-left: 15px;
}

.searchUl li:hover {
    color: skyblue;
    cursor: pointer;
}

/deep/ .el-dialog {
    background: rgba(255, 255, 255, 0.9);
    max-height: 400px;
    margin-top: 70px !important;
    border-radius: 8px;
    overflow: auto;
}

/deep/ .el-dialog__header {
    padding: 0;
}

/deep/ .el-dialog__body {
    margin-top: 0px;
    padding: 0px 10px 10px 10px;
}

/* 手机端样式 */
@media screen and (max-width: 767px) {
  .shou, .liu, .xiang, .guan, .hou {
    display: none !important;
  }
  .phoneHou{
    display: block !important;
  }
  /deep/ .el-dialog {
    width: 73% !important;
  }
}

</style>