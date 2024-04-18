<template>
    <el-container>
        <!-- 头部引用 -->
        <p-header></p-header>
        <!-- 内容 -->
        <el-main>
            <div class="main-1 main-1-c" ref="targetDiv" :style="{ backgroundImage: `url(${randomBackground})` }">
                <h1 class="cateFont"
                    style="font-size: 35px; color: rgb(46 95 104); margin-top: 58px; margin-bottom: 0;">
                    <span>{{ this.categoryName }} -> {{ this.noteCounts }}篇</span>
                </h1>
            </div>
            <!-- 主内容部分 -->
            <div class="main-2" style="text-align: center;">
                <div class="page-container">
                    <i-aside v-if="showSidebar" :key="asideKey"></i-aside>
                    <!-- 文章内容部分 -->
                    <div class="recent-posts" :style="{width: jianWidth}">
                        <div class="el-card-two noteA" @click="xin()" style="padding: 22px; height: 55px; width: auto; margin-top: 40px; background: white; display: flex; margin-bottom: 18px;
                            flex-direction: column; justify-content: center; padding: 16px;">
                            <!-- class="el-icon-orange" -->
                            <div>
                                <span class="xinjian"
                                    style="display: block; margin-top: 4px; font-size: 20px; text-align: left; color: #1ee6a3; position: absolute;">
                                    新建</span>
                                <span class="el-icon-circle-plus-outline"
                                    style="display: block; color: #1ee6a3; font-size: 30px;"></span>
                            </div>
                        </div>
                        <div class="font-style">
                            <div
                                style="border-bottom: 1px dashed rgb(168, 164, 164); padding-bottom: 8px; margin-bottom: 23px;">
                            </div>
                            <!-- 笔记存放区 -->
                            <div v-for="(item, index) in notesList" :key="index">
                                <el-card class="article-cover el-card-two" body-style="padding: 0">
                                    <div class="parent-div-one" @click="toArticle(item.noteId)">
                                        <div class="parent-div-one">
                                            <div class="content-text">
                                                <div class="content-title">
                                                    <span class="cus2">{{ item.title }}</span>
                                                </div>
                                                <el-popover placement="top" trigger="hover" title="移动到->">
                                                    <div v-for="(item2, index) in category" :key="index">
                                                        <div class="hov-cate"
                                                            style="font-size: 16px; color: skyblue; cursor: pointer;"
                                                            @click="moveCate(item2.categoryName, item2.categoryId, item.noteId)">
                                                            <span>{{ item2.categoryName }}</span>
                                                        </div>
                                                    </div>
                                                    <div slot="reference" class="el-icon-s-promotion"
                                                        @click.stop="share()"
                                                        style="position: absolute; color: orange; right: 45px; top: 15px;">
                                                    </div>
                                                </el-popover>
                                                <el-popover placement="top" trigger="hover">
                                                    <el-button type="warning" size="mini" plain
                                                        style="margin-bottom: 8px;" @click="openNote(item.open, item.noteId)"
                                                        v-text="item.open? '恢复':'公开'"></el-button><br />
                                                    <el-button type="danger" size="mini" plain
                                                        @click="deleNote(item.noteId)">删除</el-button>
                                                    <div slot="reference" class="el-icon-more"
                                                        style="position: absolute; color: red; right: 15px; top: 15px;"
                                                        @click.stop="">
                                                    </div>
                                                </el-popover>
                                                <div class="content-introduce">
                                                    <span v-html="item.content"></span>
                                                </div>
                                                <div
                                                    style="position: relative; height: 20px; margin-top: 15px; margin-bottom: 5px;">
                                                    <div class="content-type">
                                                        类别：<span style="background-color: #ffd698;" class="cus3">{{ item.categoryName }}</span>
                                                    </div>
                                                    <div class="content-author-time">
                                                        <span>&emsp;更改于&nbsp;</span>
                                                        <span class="content-time">{{ item.time | dateFormat }}</span>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </el-card>
                            </div>
                        </div>
                        <!-- 分页区 -->
                        <div style="margin-top: -10px;">
                            <el-pagination class="msg-pagination-container" :background="isBackground" :pager-count=7
                                @current-change="handleCurrentChange" layout="prev, pager, next"
                                :page-size="artPageSize" :total="noteCounts">
                            </el-pagination>
                        </div>
                    </div>
                </div>
            </div>
            <!-- 底部 -->
            <el-footer>Copyright © 2023 <span>violet蔡</span> All rights reserved</el-footer>
        </el-main>
        <div style="position: fixed; bottom: 65px; right: 14px;" @click="intop" text="向上">
            <i class="el-icon-top intop" style="font-size: 40px; color: #4fc5f6; font-weight: bold;"></i>
        </div>
        <el-popover placement="left" trigger="hover">
            <el-popover placement="top" trigger="hover">
                <div>樱花漫天</div>
                <el-button slot="reference" type="primary" size="mini" plain @click="sakuraChange">
                    <div class="el-icon-magic-stick" style="font-size: 15px; color: red;"></div>
                </el-button>
            </el-popover><br />
            <el-popover placement="left" trigger="hover">
                <div>简洁模式</div>
                <el-button slot="reference" style="margin-top: 8px;" type="primary" size="mini" plain @click="changeJ">
                    <div class="el-icon-refresh" style="font-size: 15px; color: red;"></div>
                </el-button>
            </el-popover><br />
            <div slot="reference" class="el-icon-s-tools settings"
                style="position: fixed; font-size: 35px; color: rgb(83 181 230); bottom: 20px; right: 15.5px;"
                text="设置">
            </div>
        </el-popover>
    </el-container>
</template>

<script>
import { startSakura, stopp, staticx } from "@/assets/js/sakura"
import Vue from 'vue'
import Header from '../components/header.vue'
import LeftAside from '../components/left.vue'
import Cookies from 'js-cookie'

export default Vue.extend({
    components: {
        "p-header": Header,
        "i-aside": LeftAside,
    },
    data() {
        return {
            artId: 0,
            username: "",
            userId: 0,
            categoryId: 0,
            artTitle: "",       //查询信息
            artPageStart: 1,     //起始页
            artPageSize: 10,    //每页笔记数
            notesList: [],   //笔记列表
            categoryName: "",
            noteCounts: 0,     //笔记数量
            isBackground: true,
            randomBackground: '',   //随机背景
            asideKey: 1,   //侧边栏key初始值，用于重新加载
            category: [],
            showSidebar: JSON.parse(sessionStorage.getItem('leftVisi')) !== false, // 从 sessionStorage 中获取状态，默认为 true
            jianWidth: sessionStorage.getItem("jianWidth"),
        };
    },
    created() {
        window.scrollTo(0, 0);
        //类别id
        const herf = window.location.href.split("/");
        this.categoryId = herf[herf.length - 1];
        //用户名
        this.username = JSON.parse(Cookies.get("user")).username;   //获取用户名
        this.userId = Cookies.get('userId');

        this.getNotes();
        //生成随机背景
        const randomNumber = Math.floor(Math.random() * 11) + 1;
        this.randomBackground = require(`@/assets/images/randomImage/${randomNumber}.jpg`);

        this.loadCategory();
    },
    methods: {
        //分页触发动作
        handleCurrentChange(newSPage) {
            this.artPageStart = newSPage;
            this.getArticleList();
            this.$refs.targetDiv.scrollIntoView({ behavior: 'smooth' });
        },
        //获取笔记
        async getNotes() {
            const { data: res } = await this.$http.get(`getArticleByCate?userId=${this.userId}&categoryId=${this.categoryId}&title=${this.artTitle}&pageStart=${this.artPageStart}&pageSize=${this.artPageSize}`);
            if (res.data != "") {
                this.notesList = res.data;
            } else {
                this.notesList = ""
            }
            this.categoryName = res.categoryName;
            this.noteCounts = res.number;
        },
        //新建
        xin() {
            this.$router.push({ path: '/publish' })
        },
        //去到文章
        toArticle(id) {
            this.$router.push({ path: '/notes?id=' + id });
        },
        //加载分类
        async loadCategory() {
            const { data: res } = await this.$http.get(`getCategory2?userId=${this.userId}`);
            this.category = res.data;
        },
        //移动分类
        async moveCate(cateName, categoryId, noteId) {
            const { data: res } = await this.$http.delete(`updateArticleCate?cateName=${cateName}&categoryId=${categoryId}&noteId=${noteId}`);
            if (res == "success") {
                this.$message.success("移动成功！")
                this.getNotes();
            } else {
                this.$message.error("移动失败！")
            }
        },
        //公开笔记
        async openNote(opentext, id) {
            if (opentext === false) {
                this.$confirm('确定要将该笔记公开到广场吗?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(async () => {
                    const { data: res } = await this.$http.put(`updateArticleOpen?open=${"true"}&noteId=${id}`);
                    if (res == "success") {
                        this.$message.success("笔记已公开！")
                        this.getNotes();
                    } else {
                        this.$message.error("失败！")
                    }
                }).catch(() => {
                    // 用户点击了取消按钮，取消删除操作
                });
            } else {
                const { data: res } = await this.$http.put(`updateArticleOpen?open=${"false"}&noteId=${id}`);
                if (res == "success") {
                    this.$message.success("笔记已恢复！")
                    this.getNotes();
                } else {
                    this.$message.error("失败！")
                }
            }
            // const { data: res } = await this.$http.delete(`deleteArticle?noteId=${id}`)    //访问后台
            // if (res == "success") {

            //     this.getArticleList();
            //     // 修改 key 值，触发组件重新加载
            //     this.asideKey += 1;
            // } else {
            //     this.$message.error("删除失败！")
            // }
        },
        //删除一篇笔记
        deleNote(id) {
            this.$confirm('确定要删除该笔记吗?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(async () => {
                const { data: res } = await this.$http.delete(`deleteArticle?noteId=${id}`)    //访问后台
                if (res == "success") {
                    this.$message.success("删除成功！")
                    this.getNotes();
                    // 修改 key 值，触发组件重新加载
                    this.asideKey += 1;
                } else {
                    this.$message.error("删除失败！")
                }
            }).catch(() => {
                // 用户点击了取消按钮，取消删除操作
            });
        },
        //滚动条滚动
        scrollOneScreen() {
            //const screenHeight = window.innerHeight; // 获取屏幕高度
            this.$refs.targetDiv.scrollIntoView({ behavior: 'smooth' });
        },
        //回到顶部
        intop() {
            window.scrollTo({
                top: 0,
                behavior: 'smooth' // 使用平滑滚动效果
            });
        },
        //简洁模式切换
        changeJ() {
            this.showSidebar = !this.showSidebar; // 切换状态
            sessionStorage.setItem('leftVisi', this.showSidebar); // 存储状态到 sessionStorage
            if(this.showSidebar !== true) {
                this.jianWidth = "75%";
                sessionStorage.setItem('jianWidth', "75%");
            }else{
                this.jianWidth = "60%";
                sessionStorage.setItem('jianWidth', "60%");
            }
        },
        sakuraChange() {  //落樱效果切换
            if (staticx) {
                stopp();
            } else {
                startSakura();
            }
        },
    },
    //监听路由参数变化，刷新页面数据
    watch: {
        '$route'(to, from) {
            this.$refs.targetDiv.scrollIntoView({ behavior: 'auto' });
            //类别id
            const herf = window.location.href.split("/");
            this.categoryId = herf[herf.length - 1];
            //用户名
            this.username = JSON.parse(Cookies.get("user")).username;   //获取用户名
            this.getNotes();

            //生成随机背景
            const randomNumber = Math.floor(Math.random() * 11) + 1;
            this.randomBackground = require(`@/assets/images/randomImage/${randomNumber}.jpg`);
        }
    },
    mounted() {

    },

})
</script>

<style lang="less" scoped>
.el-container {
    padding: 0;
    margin: 0;
}

.el-main {
    padding: 0;
    overflow: hidden;
}

.main-1 {
    background-size: cover;
    display: flex;
    justify-content: center;
    height: 230px;
    width: 100%;
    z-index: -4;
    flex-direction: column;
    align-items: center;
    margin: 0;
}

.main-2 {
    background: linear-gradient(rgba(255, 255, 255, 0.93), rgba(237, 255, 237, 0.93));
    text-align: center;
    width: 100%;
    z-index: -3;
}

.recent-posts {
    width: 60%;
}

.noteA {
    cursor: pointer;
}

.noteA:hover {
    background: #a4ffcc !important;
}

.noteA:hover .xinjian,
.noteA:hover .el-icon-circle-plus-outline {
    color: rgb(255, 255, 255) !important;
}

.el-card-two {
    cursor: pointer;
}

.hov-cate:hover {
    color: orange !important;
}

// 内容卡片样式一
.parent-div-one {
    width: 100%;
    position: relative;

    .triangle-div {
        width: 100%;
        height: 100%;
        position: absolute;
        top: 0;
    }

    .left {
        clip-path: polygon(0% 0%, 60% 0%, 40% 100%, 0% 100%);
    }

    .right {
        clip-path: polygon(60% 0%, 100% 0%, 100% 100%, 40% 100%);
    }

    .content-text {
        height: 100%;
        display: flex;
        flex-direction: column;
        padding: 15px 15px 15px 15px;
        white-space: normal;
        text-align: left;
    }
}

.content-title {
    overflow: hidden;
    font-weight: bold;
    height: 25px;
    white-space: nowrap;
    text-overflow: ellipsis;
}

.el-icon-s-promotion:hover {
    color: skyblue !important;
}

.el-icon-more:hover {
    color: skyblue !important;
}

.content-introduce {
    overflow: hidden;
    word-break: break-all;
    font-size: 17px;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-line-clamp: 3;
    -webkit-box-orient: vertical;
}

.content-type {
    position: absolute;
    font-size: 17px;
}

.content-author-time {
    font-size: 14px !important;
    position: absolute;
    right: 0px;
}

.content-time {
    display: inline-block;
    margin-top: 5px;
}

.intop:hover {
    cursor: pointer;
    color: orange !important;
}
</style>

<style>
.el-popover {
    min-width: 50px !important;
}

.msg-pagination-container.is-background .el-pager li {
    /*对页数的样式进行修改*/
    background-color: #ffffff !important;
    color: #000000 !important;
}

.msg-pagination-container.is-background .el-pager li:not(.disabled).active {
    /*当前选中页数的样式进行修改*/
    background-color: #446cff !important;
    color: #ffffff !important;
}

.msg-pagination-container.is-background .btn-next {
    /*对下一页的按钮样式进行修改*/
    background-color: #ffffff !important;
    color: #000000 !important;
}

.msg-pagination-container.is-background .btn-prev {
    /*对上一页的按钮样式进行修改*/
    background-color: #ffffff !important;
    color: #000000 !important;
}

/* 手机端样式 */
@media screen and (max-width: 767px) {
    .main-1-c {
        height: 180px !important;
    }

    .cateFont {
        font-size: 20px !important;
    }
}
</style>