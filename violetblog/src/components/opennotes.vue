<template>
    <el-container>
        <!-- 头部引用 -->
        <p-header></p-header>
        <!-- 内容 -->
        <el-main>
            <div class="main-1" ref="targetDiv" :style="{ backgroundImage: `url(${randomBackground})` }">
                <!-- 文章背景图绑定 -->
                <h1 class="noteFont"
                    style="font-size: 35px; color: rgb(46 95 104); margin-top: 65px; margin-bottom: 0;">
                    <span>{{ article.title }}</span>
                </h1>
                <h1 class="noteFont2" style="font-size: 20px; color: rgb(46 95 104); margin-top: 10px;">
                    <span>{{ this.articleMessage.author
                        }}</span>&nbsp;&nbsp;<span>{{ this.articleMessage.pubTime | dateFormat2 }}</span>
                </h1>
            </div>
            <!-- 主内容部分 -->
            <div class="main-2" style="text-align: center;">
                <div class="page-container">
                    <div class="recent-posts" style="margin-top: 5px; width: 75%; position: relative;">
                        <el-card style="margin: 0 auto; height: 100%;" class="el-card-two" v-show="contentShow">
                            <div v-html="this.article.content"
                                style="text-align: left; margin: -8px 0px 20px 10px; font-size: 18px;">
                            </div>
                        </el-card>
                    </div>
                </div>
            </div>
            <!-- 底部 -->
            <el-footer>Copyright © 2023 <span>violet蔡</span> All rights reserved</el-footer>
        </el-main>
        <div style="position: fixed; bottom: 20px; right: 14px;" @click="intop" text="向上">
            <i class="el-icon-top intop" style="font-size: 40px; color: #4fc5f6; font-weight: bold;"></i>
        </div>
    </el-container>
</template>

<script>
import { startSakura, stopp, staticx } from "@/assets/js/sakura"
import Vue from 'vue'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import Header from '../components/header.vue'
import LeftAside from '../components/left.vue'
import moment from 'moment'
import Cookies from 'js-cookie'

export default Vue.extend({
    components: {
        "p-header": Header,
        "i-aside": LeftAside,
        Editor,
        Toolbar
    },
    data() {
        return {
            artId: 0,
            articleMessage: [],
            artImage: "",
            contentShow: true,
            userId: 0,
            article: {
                noteId: 0,
                title: "",
                author: "",
                content: "",
                time: "",
                categoryId: 0,
                categoryName: "",
                userId: 0,
            },
        };
    },
    created() {
        const herf = window.location.href.split("=");
        this.getArticle(herf[herf.length - 1]);
        window.scrollTo(0, 0);
        this.article.noteId = herf[herf.length - 1];
        this.userId = JSON.parse(Cookies.get("userId"));   //获取用户id
        this.article.userId = this.userId;
        //生成随机背景
        const randomNumber = Math.floor(Math.random() * 11) + 1;
        this.randomBackground = require(`@/assets/images/randomImage/${randomNumber}.jpg`);
    },
    methods: {
        //获取当前文章信息
        async getArticle(id) {
            this.artId = id
            const { data: res } = await this.$http.get(`articleMessage?id=${this.artId}`);
            this.articleMessage = res;
            this.article.content = this.articleMessage.content;
            //console.log(res)
            //编辑区信息获取
            this.html = this.articleMessage.content;
            this.article.title = this.articleMessage.title;
            this.article.categoryName = this.articleMessage.categoryName;
        },
        //回到顶部
        intop() {
            this.$refs.targetDiv.scrollIntoView({ behavior: 'smooth' });
        },
        sakuraChange() {  //落樱效果切换
            if (staticx) {
                stopp();
            } else {
                startSakura();
            }
        },
    },
    //监听路由变化，刷新页面数据
    watch: {
        '$route'(to, from) {
            this.$refs.targetDiv.scrollIntoView({ behavior: 'auto' });
            const herf = window.location.href.split("=");
            this.getArticle(herf[herf.length - 1]);

            //生成随机背景
            const randomNumber = Math.floor(Math.random() * 11) + 1;
            this.randomBackground = require(`@/assets/images/randomImage/${randomNumber}.jpg`);
        }
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
    background: linear-gradient(rgba(231, 247, 245, 0.7), rgba(237, 255, 237, 0.7));
    width: 100%;
    z-index: -3;
}

.page-container {
    display: flex;
    justify-content: center;
    width: 90%;
    padding: 20px 20px 40px 20px;
    margin: 0 auto;
    min-height: 455px;
}

.recent-posts {
    width: 60%;
}

.aside-content {
    margin-top: 5px;
}

.el-card-two {
    width: 100%;
    margin-bottom: 40px;
    border-radius: 10px;
    box-shadow: 0 1px 15px -6px rgba(0, 0, 0, 0.5) !important;
}

.el-icon-edit:hover {
    background: orange !important;
}

/* 手机端样式 */
@media screen and (max-width: 767px) {
    .noteFont {
        font-size: 18px !important;
    }

    .noteFont2 {
        font-size: 18px !important;
    }

    .main-1 {
        height: 180px !important;
    }

    .bianji {
        position: fixed !important;
        top: 185px !important;
        right: 10px !important;
        z-index: 10 !important;
    }

    .wangEdi {
        display: none !important;
    }

    .aside-content {
        display: none !important;
    }

    .qinkong {
        display: none;
    }

    /deep/ .el-form-item__content {
        max-width: 100px;
    }

    /deep/ .el-card__body {
        padding: 10px;
    }

    .page-container {
        padding: 20px 5px 40px 5px !important;
        min-height: 455px;
    }
}
</style>