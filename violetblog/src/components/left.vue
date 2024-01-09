<template>
    <div class="aside-content">
        <!-- 头像昵称 -->
        <el-card class="preson-message el-card-two">
            <span>
                <img class="cus" :src="this.headImg" width="100px" height="100px" style="border-radius: 50%;" />
            </span>
            <div style="margin-top: 5px;">
                <span style="font-size: 24px;">{{ userName }}</span>
            </div>
            <div style="margin-top: 15px;">
                <span class="wz-counts">笔记</span>
                <span style="font-size: 20px;">{{ articleCounts }}</span>
            </div>
            <div style="margin-top: 15px;">
                <a class="follow-but" @click="xinjian()" style="font-size: 17px;">
                    <i class="el-icon-edit-outline" style="color: white;"></i>
                    新建
                </a>
            </div>
        </el-card>
        <!-- 公告 -->
        <el-card class="preson-message el-card-two" style="background-color: #c3f9fb;">
            <div style="text-align: left;">
                <svg class="icon icon-st" aria-hidden="true">
                    <use xlink:href="#icon-gonggao"></use>
                </svg>
                <span style="font-size: 20px;">公告</span>
            </div>
            <div class="aside-items-sty">
                <span>网站维护中，暂不可用！</span>
            </div>
        </el-card>
        <!-- 最新发布 -->
        <el-card class="preson-message el-card-two">
            <div style="text-align: left;">
                <svg class="icon icon-st" aria-hidden="true">
                    <use xlink:href="#icon-wenzhang"></use>
                </svg>
                <span style="font-size: 20px;">最新发布</span>
            </div>
            <div v-for="(item, index) in articleList" :key="index">
                <div class="aside-items-sty">
                    <!-- 列表 -->
                    <div class="article-review-divsty">
                        <div class="aside-img" @click="toArticle(item.id)">
                            <img class="cus zoom-effect" :src="item.cover" width="70px" height="70px" />
                        </div>
                        <div class="article-tit">
                            <div class="article-tit-one" @click="toArticle(item.id)">
                                <span class="article-one-title cus">{{ item.title }}</span>
                            </div>
                            <div class="article-tit-two">
                                <span class="article-two-author cus">{{ item.author }}&emsp;</span>
                                <span>{{ item.time | formatDate2 }}</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </el-card>
        <!-- 分类 -->
        <el-card class="preson-message el-card-two" style="background-color: rgba(239 168 137); height: 160px;">
            <div class="aside-type">
                <span>学习人生</span>
            </div>
        </el-card>
        <el-card class="preson-message el-card-two" style="background-color: rgb(249 174 174); height: 160px;">
            <div class="aside-type">
                <span>随笔</span>
            </div>
        </el-card>
        <!-- 相册 -->
        <el-card class="preson-message el-card-two" style="background-color: #ffefcd;">
            <div style="text-align: left;">
                <svg class="icon icon-st" aria-hidden="true">
                    <use xlink:href="#icon-xiangce"></use>
                </svg>
                <span style="font-size: 20px;">图集</span>
            </div>
            <div class="aside-items-sty">
                <span>急急急！功能正在紧急更新中</span>
            </div>
        </el-card>
        <!-- 站长统计 -->
        <el-card class="preson-message el-card-two">
            <div style="text-align: left;">
                <svg class="icon icon-st" aria-hidden="true">
                    <use xlink:href="#icon-tongji"></use>
                </svg>
                <span style="font-size: 20px;">站长统计</span>
            </div>
            <div class="aside-items-sty">
                <div class="web-count">
                    <span>总访问量</span>
                    <span class="count-num">10000 次</span>
                </div>
                <div class="web-count">
                    <span>总文章数</span>
                    <span class="count-num">1000 篇</span>
                </div>
                <div class="web-count">
                    <span>总用户</span>
                    <span class="count-num">1000 人</span>
                </div>
                <div class="web-count">
                    <span>网站运行天数</span>
                    <span class="count-num">14 天</span>
                </div>
                <div class="web-count">
                    <span>最近更新</span>
                    <span class="count-num">1 天前</span>
                </div>
            </div>
        </el-card>
    </div>
</template>
  
<script>

export default ({
    data() {
        return {
            articleCounts: 0,    //文章总数
            articleList: [],   //文章列表
            artTitle: "",       //文章标题
            artPageStart: 1,     //起始页
            artPageSize: 7,    //每页文章数
            userName: "",    //将用户信息字符串对象解析为json对象并获取用户名
            headImg: "",
            author: "",
        };
    },
    created() {
        this.getUserList();
        if (JSON.parse(sessionStorage.getItem("user")) != null && sessionStorage.getItem("headimage") != null) {
            this.userName = JSON.parse(sessionStorage.getItem("user")).username;
            this.headImg = sessionStorage.getItem("headimage");
        } else {
            return;
        }
    },
    methods: {
        async getUserList() {
            this.author = JSON.parse(sessionStorage.getItem("user")).username;
            const { data: res } = await this.$http.get(`getArticle?author=${this.author}&title=''&pageStart=1&pageSize=7`);
            this.articleList = res.data;
            //console.log(this.articleList)
            this.articleCounts = res.number;
        },
        //发布
        xinjian() {
            this.$router.push({ path: '/publish' })
        },
        toArticle(id) {
            const herf = window.location.href.split("=");
            if(herf[herf.length - 1] == id){
                location.reload();   //路由重复，刷新当前页
            }else{
                this.$router.push({ path: '/articles?id=' + id });
            }
        },
    },
})
</script>

<style lang="less" scoped></style>