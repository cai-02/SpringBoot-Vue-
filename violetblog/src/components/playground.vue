<template>
    <el-container>
        <!-- 头部引用 -->
        <p-header></p-header>
        <!-- 内容 -->
        <el-main>
            <div class="main-1 main-1-c" ref="targetDiv" :style="{ backgroundImage: `url(${randomBackground})` }">
                <h1 class="cateFont"
                    style="font-size: 35px; color: rgb(46 95 104); margin-top: 58px; margin-bottom: 0;">
                    <span>广场</span>
                </h1>
            </div>
            <!-- 主内容部分 -->
            <div class="main-2" style="text-align: center;">
                <div class="page-container">
                    <!-- 文章内容部分 -->
                    <div class="recent-posts">
                        <div class="font-style">
                            <!-- 笔记存放区 -->
                            <div v-for="(item, index) in notesList" :key="index">
                                <el-card class="article-cover el-card-two" body-style="padding: 0">
                                    <div class="parent-div-one" @click="toArticle(item.noteId)">
                                        <div class="content-title" style="text-align: left; margin-left: 15px; margin-top: 10px;">
                                            <span class="cus2">{{ item.title }}</span>
                                        </div>
                                        <div
                                            style="position: relative; height: 20px; margin-top: 10px; margin-bottom: 8px;">
                                            <div class="content-type" style="position: absolute; left: 13px; font-size: 17px;">
                                                来源：<span
                                                    style="background-color: rgb(255 233 200);" class="">{{ item.author }}</span>
                                            </div>
                                            <div class="content-author-time">
                                                <span>&emsp;更改于&nbsp;</span>
                                                <span class="content-time">{{ item.time | dateFormat }}</span>
                                            </div>
                                        </div>
                                    </div>
                                </el-card>
                            </div>
                        </div>
                        <!-- 分页区 -->
                        <div style="width: 100%; margin: 20px auto 0 auto; float: left;">
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
import Cookies from 'js-cookie'

export default Vue.extend({
    components: {
        "p-header": Header,
    },
    data() {
        return {
            artId: 0,
            artTitle: "",       //查询信息
            artPageStart: 1,     //起始页
            artPageSize: 20,    //每页笔记数
            notesList: [],   //笔记列表
            noteCounts: 0,     //笔记数量
            isBackground: true,
            randomBackground: '',   //随机背景
        };
    },
    created() {
        window.scrollTo(0, 0);
        this.getNotes();
        //生成随机背景
        const randomNumber = Math.floor(Math.random() * 11) + 1;
        this.randomBackground = require(`@/assets/images/randomImage/${randomNumber}.jpg`);
    },
    methods: {
        //分页触发动作
        handleCurrentChange(newSPage) {
            this.artPageStart = newSPage;
            this.getNotes();
            this.$refs.targetDiv.scrollIntoView({ behavior: 'smooth' });
        },
        //获取笔记
        async getNotes() {
            const { data: res } = await this.$http.get(`getArticleOpen?title=${this.artTitle}&pageStart=${this.artPageStart}&pageSize=${this.artPageSize}`);
            if (res.data != "") {
                this.notesList = res.data;
                this.noteCounts = res.number;
                //console.log(this.notesList)
            } else {
                this.notesList = ""
            }
        },
        //去到文章
        toArticle(id) {
            this.$router.push({ path: '/opennotes?id=' + id });
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
        sakuraChange() {  //落樱效果切换
            if (staticx) {
                stopp();
            } else {
                startSakura();
            }
        },
    },
    //监听路由参数变化，刷新页面数据
    // watch: {
    //     '$route'(to, from) {
    //         this.$refs.targetDiv.scrollIntoView({ behavior: 'auto' });
    //         //类别id
    //         const herf = window.location.href.split("/");
    //         this.categoryId = herf[herf.length - 1];
    //         //用户名
    //         this.username = JSON.parse(Cookies.get("user")).username;   //获取用户名
    //         this.getNotes();

    //         //生成随机背景
    //         const randomNumber = Math.floor(Math.random() * 11) + 1;
    //         this.randomBackground = require(`@/assets/images/randomImage/${randomNumber}.jpg`);
    //     }
    // },
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
.page-container {
    padding-bottom: 25px;
    min-height: 455px;
}
.recent-posts {
    width: 90%;
}
.font-style {
    width: 100%;
    float: left;
}

.el-card-two {
    cursor: pointer;
}
.article-cover {
    margin-top: 20px;
    margin-bottom: 0px;
    width: 48%;
    float: left;
    margin-left: 1%;
    margin-right: 1%;
}

.content-author-time {
    font-size: 15px !important;
    position: absolute;
    right: 15px;
}

.content-time {
    display: inline-block;
    margin-top: 5px;
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