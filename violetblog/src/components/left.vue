<template>
    <div class="aside-content">
        <!-- 头像昵称 -->
        <el-card class="preson-message el-card-two">
            <span>
                <img @click="toManage" class="headIm cus" :src="this.headImg" width="100px" height="100px"
                    style="border-radius: 50%;" />
            </span>
            <div style="margin-top: 5px;white-space: nowrap; text-overflow: ellipsis; overflow: hidden;">
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
            <div class="aside-items-sty" style="white-space: normal; line-height: 25px;">
                <span>{{ announcement }}</span>
            </div>
        </el-card>
        <!-- 最近更改 -->
        <el-card class="preson-message el-card-two">
            <div style="text-align: left;">
                <svg class="icon icon-st" aria-hidden="true" style="margin-left: -2px;">
                    <use xlink:href="#icon-wenzhang"></use>
                </svg>
                <span style="font-size: 20px;">最近更改</span>
            </div>
            <div v-for="(item, index) in articleListByTime" :key="index" class="aside-items-sty">
                <!-- 列表 -->
                <div class="notes-recent">
                    <div class="notes-tit" style="height: 30px; padding: 6px 0 6px 0;">
                        <div class="notes-tit-two">
                            <span class="notes-one-title cus" @click="toArticle(item.noteId)">{{ item.title }}</span>
                        </div>
                        <div class="notes-tit-one" style="float: right; font-size: 15px; color: gray;">
                            <span>{{ item.time | dateFormat3 }}</span>
                        </div>
                    </div>
                </div>
            </div>
        </el-card>
        <!-- 分类 -->
        <div class="el-card-two noteA" @click="addDialogVisible = true" style="padding: 22px; height: 55px; width: 100; margin-top: 40px; background: white; display: flex; margin-bottom: 18px;
                            flex-direction: column; justify-content: center; padding: 16px;">
            <!-- class="el-icon-orange" -->
            <div>
                <span class="xinjian"
                    style="display: block; margin-top: 3px; font-size: 20px; text-align: left; color: #1ee6a3; position: absolute;">
                    类别</span>
                <span class="el-icon-circle-plus-outline" style="display: block; color: #1ee6a3; font-size: 30px;"></span>
            </div>
        </div>
        <!-- 类别显示区 -->
        <div v-for="(item, index) in category.slice(0, visibleCardCount)" :key="index" @click="toCategory(item.categoryId)">
            <el-card class="preson-message el-card-three"
                :style="{ 'background-color': getRandomColor(), height: '160px', cursor: 'pointer' }">
                <div class="aside-type">
                    <span class="category-name">{{ item.categoryName }}</span>
                </div>
            </el-card>
        </div>
        <!-- 新增类别区域 -->
        <div>
            <el-dialog title="添加分类" :visible.sync="addDialogVisible" @close="addDialogClosed()">
                <el-form :model="cate" label-width="65px" :rules="addFormRules" ref="cate">
                    <el-form-item label="类名" prop="categoryName">
                        <el-input placeholder="请输入分类名" v-model="cate.categoryName"></el-input>
                    </el-form-item>
                </el-form>
                <span slot="footer" class="dialog-footer">
                    <el-button @click="addDialogVisible = false">取消</el-button>
                    <el-button type="primary" @click="addCate()">确定</el-button>
                </span>
            </el-dialog>
        </div>
        <!-- 更多分类 -->
        <div v-if="showCateBut" class="el-card-two noteA" @click="showAllCate()" style="padding: 22px; height: 55px; width: 100; margin-top: 20px; background: white; display: flex; margin-bottom: 18px;
                            flex-direction: column; justify-content: center; padding: 16px;">
            <div>
                <span v-text="moreText" class="xinjian" ref="moreCate"
                    style="display: block; margin-top: 3px; font-size: 20px; text-align: center; color: #1ee6a3;">
                </span>
            </div>
        </div>
        <!-- 相册 -->
        <el-card class="preson-message el-card-four" style="background-color: #ffefcd; cursor: pointer;">
            <div @click="toXiangce()">
                <div style="text-align: left;">
                    <svg class="icon icon-st" aria-hidden="true">
                        <use xlink:href="#icon-xiangce"></use>
                    </svg>
                    <span style="font-size: 20px;">图集</span>
                </div>
                <div class="aside-items-sty" style="white-space: normal;">
                    <span>记住每一个瞬间，珍藏每一份记忆</span>
                </div>
            </div>
        </el-card>
        <!-- 生涯统计 -->
        <el-card class="preson-message el-card-two" style="margin-bottom: 0;">
            <div style="text-align: left;">
                <svg class="icon icon-st" aria-hidden="true">
                    <use xlink:href="#icon-tongji"></use>
                </svg>
                <span style="font-size: 20px;">生涯统计<small style="color: gray;">（待更新）</small></span>
            </div>
            <div class="aside-items-sty">
                <div class="web-count">
                    <span>使用天数</span>
                    <span class="count-num">1 天</span>
                </div>
                <div class="web-count">
                    <span>总笔记数</span>
                    <span class="count-num">1 篇</span>
                </div>
                <div class="web-count">
                    <span>总类别数</span>
                    <span class="count-num">1 类</span>
                </div>
                <div class="web-count">
                    <span>好友</span>
                    <span class="count-num">0 人</span>
                </div>
            </div>
        </el-card>
    </div>
</template>
  
<script>
import Cookies from 'js-cookie'

export default ({
    data() {
        return {
            articleCounts: 0,    //文章总数
            articleList: [],   //文章列表
            articleListByTime: [],   //根据时间获取
            artTitle: "",       //文章标题
            userName: "",    //将用户信息字符串对象解析为json对象并获取用户名
            headImg: "",
            author: "",
            userId: 0,
            category: [],
            addDialogVisible: false,     //对话框状态
            cate: {
                categoryName: "",
                userId: 0,
            },
            addFormRules: {
                categoryName: [
                    { required: true, message: '请输入分类名', trigger: 'blur' },
                    { min: 1, max: 12, message: '长度在 1 到 12 个字符', trigger: 'blur' }
                ],
            },
            visibleCardCount: 5,    //默认显示五条类别
            moreText: "更多",
            showCateBut: false,
            announcement: "",
        };
    },
    async created() {
        this.userId = JSON.parse(Cookies.get("userId"));
        this.getArticleList();
        this.getArticleByTime();
        if (JSON.parse(Cookies.get("user")) != null && Cookies.get("headimage") != null) {
            this.userName = JSON.parse(Cookies.get("user")).username;
            this.headImg = Cookies.get("headimage");
        } else {
            return;
        }
        this.cate.userId = this.userId;
        //加载类别
        this.loadCategory();
        //加载公告
        const { data: res } = await this.$http.get("getAnn");
        this.announcement = res.data.content;
    },
    methods: {
        async getArticleList() {
            const { data: res } = await this.$http.get(`getArticle?userId=${this.userId}&title=''&pageStart=1&pageSize=7`);
            this.articleList = res.data;
            //console.log(JSON.stringify(res.data))
            this.articleCounts = res.number;  //文章总数
        },
        //点击头像进入后台
        toManage() {
            this.activePath = Cookies.get('activePath');  //取出cookie里的path，动态修改path
            if (this.activePath != null) {
                this.$router.push({ path: this.activePath });
            } else {
                this.$router.push({ path: "/admin" });
            }
        },
        //获取最近更改的笔记
        async getArticleByTime() {
            const { data: res } = await this.$http.get(`getArticleByTime?userId=${this.userId}`);
            this.articleListByTime = res.data;
            //console.log(this.articleListByTime)
        },
        //发布
        xinjian() {
            this.$router.push({ path: '/publish' })
        },
        //加载分类
        async loadCategory() {
            const { data: res } = await this.$http.get(`getCategory2?userId=${this.userId}`);
            this.category = res.data;
            //console.log(this.category)
            if (this.category.length > 5) {
                this.showCateBut = true;
            } else {
                this.showCateBut = false;
            }
        },
        //显示全部分类
        showAllCate() {
            this.visibleCardCount = (this.visibleCardCount === 5) ? this.category.length : 5;
            this.moreText = (this.moreText === "更多") ? "收起" : "更多";
        },
        //随机色
        getRandomColor() {
            // 生成稍微深一点的随机RGB颜色
            const randomColorInRange = () => Math.floor(Math.random() * 100) + 130; // 100-200范围内的稍深颜色
            const r = randomColorInRange();
            const g = randomColorInRange();
            const b = randomColorInRange();
            return `rgb(${r}, ${g}, ${b})`;
        },
        //监听添加用户操作，看是否关闭
        addDialogClosed() {
            this.$refs.cate.resetFields();
        },
        //添加类别
        async addCate() {
            this.$refs.cate.validate(async valid => {
                if (!valid) return;
                const { data: res } = await this.$http.post("/addCategory", this.cate)
                if (res == "success") {
                    this.$message.success("添加成功！");
                    this.loadCategory();
                    this.addDialogVisible = false;
                } else {
                    this.$message.error("添加失败！")
                }
            });
        },
        //去到文章
        toArticle(id) {
            const herf = window.location.href.split("=");
            if (herf[herf.length - 1] == id) {
                location.reload();   //路由重复，刷新当前页
            } else {
                this.$router.push({ path: '/notes?id=' + id });
            }
        },
        //去到分类
        toCategory(id) {
            const herf = window.location.href.split("/");
            if (herf[herf.length - 1] == id) {
                location.reload();   //路由重复，刷新当前页
            } else {
                this.$router.push({ path: '/category/' + id });
            }
        },
        //去到相册
        toXiangce() {
            if (this.$route.path == "/pictur") {
                location.reload();
            } else {
                this.$router.push({ path: '/pictur' })
            }
        },
    },
})
</script>
<style lang="less" scoped>
.noteA:hover {
    background: #fdd985 !important;
    cursor: pointer;
}

.preson-message:hover .category-name {
    color: pink;
}

.noteA:hover .xinjian,
.noteA:hover .el-icon-circle-plus-outline {
    color: rgb(255, 255, 255) !important;
}

/deep/ .el-dialog {
    width: 30%;
}

/deep/ .el-dialog__body {
    padding: 20px 20px 0 20px;
}

/deep/ .el-dialog__header {
    text-align: left;
}

.headIm {
    transition: transform 0.3s ease;
    /* 添加过渡效果 */
    /* 设置旋转中心为头像的中心 */
    transform-origin: center center;
}

.headIm:hover {
    transform: rotate(360deg);
    /* 鼠标悬停时顺时针旋转360度 */
}

.headIm:active {
    transform: rotate(-360deg);
    /* 在鼠标点击时逆时针旋转360度 */
}
</style>