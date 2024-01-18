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
                    style="display: block; margin-top: 4px; font-size: 20px; text-align: left; color: #1ee6a3; position: absolute;">
                    类别</span>
                <span class="el-icon-circle-plus-outline" style="display: block; color: #1ee6a3; font-size: 30px;"></span>
            </div>
        </div>
        <div v-for="(item, index) in category" :key="index">
            <el-card class="preson-message el-card-three"
                :style="{ 'background-color': getRandomColor(), height: '160px', cursor: 'pointer' }">
                <div class="aside-type">
                    <span class="category-name">{{ item.categoryName }}</span>
                </div>
            </el-card>
        </div>
        <!-- 新增类别区域 -->
        <div>
            <el-dialog title="添加用户" :visible.sync="addDialogVisible" @close="addDialogClosed()">
                <el-form :model="cate" label-width="65px" :rules="addFormRules" ref="cate">
                    <el-form-item label="名称" prop="categoryName">
                        <el-input placeholder="请输入分类名" v-model="cate.categoryName"></el-input>
                    </el-form-item>
                </el-form>
                <span slot="footer" class="dialog-footer">
                    <el-button @click="addDialogVisible = false">取消</el-button>
                    <el-button type="primary" @click="addCate()">确定</el-button>
                </span>
            </el-dialog>
        </div>
        <!-- 相册 -->
        <el-card class="preson-message el-card-four" style="background-color: #ffefcd;">
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
            }
        };
    },
    created() {
        this.getArticleList();
        this.getArticleByTime();
        if (JSON.parse(sessionStorage.getItem("user")) != null && sessionStorage.getItem("headimage") != null) {
            this.userName = JSON.parse(sessionStorage.getItem("user")).username;
            this.headImg = sessionStorage.getItem("headimage");
        } else {
            return;
        }
        this.userId = JSON.parse(sessionStorage.getItem("userId"));
        this.cate.userId = this.userId;
        //加载类别
        this.loadCategory();
    },
    methods: {
        async getArticleList() {
            this.author = JSON.parse(sessionStorage.getItem("user")).username;
            const { data: res } = await this.$http.get(`getArticle?author=${this.author}&title=''&pageStart=1&pageSize=7`);
            this.articleList = res.data;
            //console.log(JSON.stringify(res.data))
            this.articleCounts = res.number;  //文章总数
        },
        //获取最近更改的笔记
        async getArticleByTime() {
            this.author = JSON.parse(sessionStorage.getItem("user")).username;
            const { data: res } = await this.$http.get(`getArticleByTime?author=${this.author}`);
            this.articleListByTime = res.data;
            //console.log(this.articleListByTime)
        },
        //发布
        xinjian() {
            this.$router.push({ path: '/publish' })
        },
        //加载分类
        async loadCategory() {
            const { data: res } = await this.$http.get(`getCategory?userId=${this.userId}`);
            this.category = res.data;
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
</style>