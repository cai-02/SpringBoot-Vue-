<template>
    <el-container>
        <!-- 头部引用 -->
        <i-header></i-header>
        <!-- 主体 -->
        <el-main>
            <!-- 背景 -->
            <div class="main-1">
                <!-- 通知 -->
                <div class="scrolling-container"
                    style="display: none; position: absolute; top: 100px; color: #f891a3; font-size: 15px;">
                    {{ notification }}
                </div>
                <h1 class="logh1" style="font-size: 40px; color: #fee4e0; margin-top: -300px; margin-bottom: 20px;">
                    <span>风会代替你去往任何地方</span>
                </h1>
                <div class="logot">
                    <span>{{ te }}|</span>
                </div>
            </div>
            <!-- 向下箭头 -->
            <div class="arrow-container"
                style="top: 44vh; position: absolute; cursor: pointer; margin: auto; left: 0; right: 0; width: 45px;"
                @click="scrollOneScreen">
                <i class="el-icon-arrow-down" style="color: white; font-size: 45px; font-weight: 900 !important;"></i>
            </div>
            <!-- 波浪效果 -->
            <div class="bannerWave1">
                <div class="wave-body">
                    <div class="waves"></div>
                    <div class="waves2"></div>
                    <div class="waves"></div>
                    <div class="waves2"></div>
                    <div class="waves"></div>
                    <div class="waves2"></div>
                    <div class="waves"></div>
                    <div class="waves2"></div>
                    <div class="waves"></div>
                    <div class="waves2"></div>
                    <div class="waves"></div>
                    <div class="waves2"></div>
                    <div class="waves"></div>
                    <div class="waves2"></div>
                    <div class="waves"></div>
                    <div class="waves2"></div>
                    <div class="waves"></div>
                    <div class="waves2"></div>
                    <div class="waves"></div>
                    <div class="waves2"></div>
                </div>
                <div class="wave-body2">
                    <div class="waves3"></div>
                    <div class="waves4"></div>
                    <div class="waves3"></div>
                    <div class="waves4"></div>
                    <div class="waves3"></div>
                    <div class="waves4"></div>
                    <div class="waves3"></div>
                    <div class="waves4"></div>
                    <div class="waves3"></div>
                    <div class="waves4"></div>
                    <div class="waves3"></div>
                    <div class="waves4"></div>
                    <div class="waves3"></div>
                    <div class="waves4"></div>
                    <div class="waves3"></div>
                    <div class="waves4"></div>
                </div>
            </div>
            <!-- 主内容部分 -->
            <div class="main-2" ref="targetDiv">
                <div class="page-container">
                    <!-- 左侧侧边栏引用 -->
                    <i-aside v-if="showSidebar" :key="asideKey"></i-aside>
                    <!-- 文章内容部分 -->
                    <div class="recent-posts" :style="{ width: jianWidth }">
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
                            <div v-if="noteVisi" style="margin-bottom: 30px; font-size: 25px; color: #ff7e94;">
                                暂无笔记，快新建一篇吧！
                            </div>
                            <div v-for="(item, index) in articleList" :key="index">
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
                                                    <div slot="reference" class="el-icon-s-promotion" @click.stop=""
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
                                                        类别：<span @click.stop="toCategory(item.categoryId)"
                                                            style="background-color: #ffd698;" class="cus3">{{ item.categoryName }}</span>
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
                                :page-size="artPageSize" :total="articleCounts">
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
                <el-button slot="reference" style="margin-top: 8px;" type="primary" size="mini" plain
                    @click="changeJ()">
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
import Header from '../components/header.vue'
import LeftAside from '../components/left.vue'
import Cookies from 'js-cookie'

export default {
    components: {
        "i-header": Header,
        "i-aside": LeftAside
    },
    data() {
        return {
            activeIndex: '1',
            activeIndex2: '1',
            timer: "",
            value: 0,
            te: "",
            text: "听凭风引，静看花开花落，云卷云舒。",
            articleList: [],   //文章列表
            articleCounts: 0,    //文章总数
            artTitle: "",       //查询信息
            artPageStart: 1,     //起始页
            artPageSize: 10,    //每页文章数
            but_color: "blue",
            isBackground: true,
            author: "",
            asideKey: 1,   //侧边栏key初始值，用于重新加载
            userId: 0,
            noteVisi: false,
            notification: '前往PC（电脑）端，体验完整功能，手机百度浏览器暂时有问题，请使用其他浏览器访问',
            category: [],
            showSidebar: JSON.parse(sessionStorage.getItem('leftVisi')) !== false, // 从 sessionStorage 中获取状态，默认为 true
            jianWidth: sessionStorage.getItem("jianWidth"),
        };
    },
    created() {
        this.start();
        this.userId = Cookies.get("userId");
        this.getArticleList();
        this.loadCategory();
    },
    methods: {
        //分页触发动作
        handleCurrentChange(newSPage) {
            this.artPageStart = newSPage;
            this.getArticleList();
            this.$refs.targetDiv.scrollIntoView({ behavior: 'smooth' });
        },
        //获取所有文章
        async getArticleList() {
            this.author = JSON.parse(Cookies.get("user")).username;
            const { data: res } = await this.$http.get(`getArticle?userId=${this.userId}&title=${this.artTitle}&pageStart=${this.artPageStart}&pageSize=${this.artPageSize}`);
            this.articleList = res.data;
            //console.log(this.articleList)
            //console.log(res.number)
            this.articleCounts = res.number;
            if (this.articleCounts === 0) {
                this.noteVisi = true;
            } else {
                this.noteVisi = false;
            }
        },
        //加载分类
        async loadCategory() {
            const { data: res } = await this.$http.get(`getCategory2?userId=${this.userId}`);
            this.category = res.data;
        },
        //移动分类
        async moveCate(cateName, categoryId, noteId) {
            const { data: res } = await this.$http.put(`updateArticleCate?cateName=${cateName}&categoryId=${categoryId}&noteId=${noteId}`);
            if (res == "success") {
                this.$message.success("移动成功！")
                this.getArticleList();
            } else {
                this.$message.error("移动失败！")
            }
        },
        //简洁模式切换
        changeJ() {
            this.showSidebar = !this.showSidebar; // 切换状态
            sessionStorage.setItem('leftVisi', this.showSidebar); // 存储状态到 sessionStorage
            if (this.showSidebar !== true) {
                this.jianWidth = "75%";
                sessionStorage.setItem('jianWidth', "75%");
            } else {
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
        //打字机效果
        start() {
            this.timer = setInterval(this.valChange, 100);
        },
        valChange() {
            if (this.value <= this.text.length && this.value >= 0) {
                this.te = this.text.slice(0, this.value);
                this.value++;
            } else {
                clearInterval(this.timer);
                setTimeout(() => {
                    this.timer = setInterval(this.reverseValChange, 70);
                }, 2000);
            }
        },
        reverseValChange() {
            if (this.value > 0) {
                this.value--;
                this.te = this.text.slice(0, this.value);
            } else {
                clearInterval(this.timer);
                setTimeout(() => {
                    this.timer = setInterval(this.valChange, 100);
                }, 2000);
            }
        },
        //新建
        xin() {
            this.$router.push({ path: '/publish' })
        },
        //去到文章
        toArticle(id) {
            this.$router.push({ path: '/notes?id=' + id });
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
                        this.getArticleList();
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
                    this.getArticleList();
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
                    this.getArticleList();
                    // 修改 key 值，触发组件重新加载
                    this.asideKey += 1;
                } else {
                    this.$message.error("删除失败！")
                }
            }).catch(() => {
                // 用户点击了取消按钮，取消删除操作
            });
        },
        //去到分类
        toCategory(id) {
            const herf = window.location.href.split("/");
            if (herf[herf.length - 1] == id) {
                //location.reload();   //路由重复，刷新当前页
            } else {
                //console.log(id)
                this.$router.push({ path: '/category/' + id });
            }
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
    }

}
</script>

<style lang="less" scoped>
body {
    overflow-x: hidden !important;
}

.el-container {
    padding: 0;
}

.el-main {
    padding: 0;
    overflow: hidden;
    white-space: nowrap;
}

.main-1 {
    background-image: url(../assets/images/zhuti1.jpg);
    background-size: cover;
    color: #333;
    position: fixed;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    height: 100%;
    width: 100%;
    top: 0;
    z-index: -4;
    // .filter{
    //     width: 100%;
    //     height: 100%;
    //     backdrop-filter: blur(1px);
    // }

}

.main-2 {
    background: linear-gradient(rgba(255, 255, 255, 0.93), rgba(237, 255, 237, 0.93));
    text-align: center;
    width: 100%;
    z-index: -3;
}

.scrolling-container {
    white-space: nowrap;
    animation: scrollText 20s linear infinite;
}

@keyframes scrollText {
    0% {
        transform: translateX(100%);
    }

    100% {
        transform: translateX(-100%);
    }
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

// 水波纹实现
.bannerWave1 {
    margin-top: 48vh;
    height: 85px;
    background: rgba(255, 255, 255, 0);
    width: 100%;
    z-index: -2;
    position: relative;
}

.wave-body {
    position: absolute;
    height: 100%;
    width: 200%;
    margin-top: -45px;
    margin-left: -100%;
    animation: m 20s infinite linear;
}

.wave-body2 {
    position: absolute;
    height: 100%;
    width: 200%;
    margin-top: -45px;
    margin-left: -100%;
    animation: m 15s infinite linear;
}

.waves {
    float: left;
    --c: rgba(255, 255, 255, 0.9);
    --w1: radial-gradient(90% 59% at top, #0000 100%, var(--c) 100.5%) no-repeat;
    background: var(--w1);
    height: 130px;
    width: 5%;
    background-size: 100% 100%;
}

.recent-posts {
    width: 60%;
}

.waves2 {
    float: left;
    --c: rgba(255, 255, 255, 0.9);
    --w2: radial-gradient(90% 60.7% at bottom, var(--c) 100%, #0000 100.5%) no-repeat;
    background: var(--w2);
    height: 130px;
    width: 5%;
    background-size: 100% 100%;
}

.waves3 {
    float: left;
    --c: rgba(255, 255, 255, 0.3);
    --w1: radial-gradient(90% 49% at top, #0000 100%, var(--c) 100.5%) no-repeat;
    background: var(--w1);
    height: 130px;
    width: 6.25%;
    background-size: 100% 100%;
}

.waves4 {
    float: left;
    --c: rgba(255, 255, 255, 0.3);
    --w2: radial-gradient(90% 70.7% at bottom, var(--c) 100%, #0000 100.5%) no-repeat;
    background: var(--w2);
    height: 130px;
    width: 6.25%;
    background-size: 100% 100%;
}

@keyframes m {
    0% {
        margin-left: -100%
    }

    100% {
        margin-left: 0%
    }
}

.logot {
    padding: 12px;
    font-size: 23px;
    color: rgb(254, 228, 224);
    background-color: rgba(0, 0, 0, 0.4) !important;
    border-radius: 10px;
}

// 箭头移动
.arrow-container {
    position: relative;
    animation: floatAnimation 2s infinite ease-in-out;
}

@keyframes floatAnimation {

    0%,
    100% {
        transform: translateY(0);
    }

    50% {
        transform: translateY(35px);
    }
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

// 内容卡片样式二
.parent-div-two {
    width: 100%;
    height: 280px;
    position: relative;

    .triangle-div {
        width: 100%;
        height: 100%;
        position: absolute;
        top: 0;
    }

    .left {
        clip-path: polygon(0% 0%, 40% 0%, 60% 100%, 0% 100%);
    }

    .right {
        clip-path: polygon(40% 0%, 100% 0%, 100% 100%, 60% 100%);
    }

    .content-text {
        width: 60%;
        height: 100%;
        display: flex;
        flex-direction: column;
        padding: 15px 15px 15px 30px;
        float: right;
        align-items: end;
        white-space: normal;
        text-align: right;
    }
}

// 标题
.content-title {
    overflow: hidden;
    font-weight: bold;
    height: 25px;
    white-space: nowrap;
    text-overflow: ellipsis;
}

.hov-cate:hover {
    color: orange !important;
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
    top: 5px;
}

.content-author-time {
    font-size: 14px !important;
    position: absolute;
    right: 0px;
    top: 2px;
}

.content-time {
    display: inline-block;
    margin-top: 5px;
}

// 右下角
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
    .aside-content {
        display: none !important;
    }

    .page-container {
        width: 100% !important;
    }

    .recent-posts {
        width: 100% !important;
    }

    .wave-body,
    .wave-body2 {
        width: 400% !important;
    }

    .logh1 {
        font-size: 27px !important;
    }

    .logot {
        font-size: 14px !important;
    }

    .content-title {
        max-width: 250px !important;
    }

    .el-message-box {
        width: 320px !important;
    }

    .scrolling-container {
        display: block !important;
    }
}
</style>
