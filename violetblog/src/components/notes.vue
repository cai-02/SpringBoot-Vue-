<template>
    <el-container>
        <!-- 头部引用 -->
        <p-header></p-header>
        <!-- 内容 -->
        <el-main>
            <div class="main-1" ref="targetDiv" :style="{ backgroundImage: `url(${randomBackground})` }"> <!-- 文章背景图绑定 -->
                <h1 class="noteFont" style="font-size: 35px; color: #39c5bb; margin-top: 65px; margin-bottom: 0;">
                    <span>{{ article.title }}</span>
                </h1>
                <h1 class="noteFont2" style="font-size: 20px; color: white; margin-top: 10px;">
                    <span>{{ article.categoryName
                    }}</span>&nbsp;<span>{{ this.articleMessage.pubTime | dateFormat2 }}</span>
                </h1>
            </div>
            <!-- 主内容部分 -->
            <div class="main-2" style="text-align: center;">
                <div class="page-container">
                    <i-aside></i-aside>
                    <div class="recent-posts" style="width: 60%; margin-top: 5px; position: relative;">
                        <el-popover placement="top" trigger="hover" content="编辑" v-show="contentShow">
                            <div class="el-icon-edit bianji" slot="reference" @click="edit()"
                                style="background: #39c5bb; width: 50px; color: white; padding-left: 1px;
                        padding-top: 15px; font-size: 19px; height: 50px; border-radius: 50%; right: -60px; position: absolute; cursor: pointer;"></div>
                        </el-popover>
                        <el-popover placement="top" trigger="hover" content="确认" v-show="contentShow2">
                            <div class="el-icon-check bianji" slot="reference" @click="save()"
                                style="background: #39c5bb; font-weight: bolder; width: 50px; color: white; padding-left: 1px;
                        padding-top: 12px; font-size: 25px; height: 50px; border-radius: 50%; right: -60px; position: absolute; cursor: pointer;"></div>
                        </el-popover>
                        <el-card style="margin: 0 auto; height: 100%;" class="el-card-two" v-show="contentShow">
                            <div v-html="this.article.content" style="text-align: left; margin: -8px 0px 20px 10px; font-size: 18px;">

                            </div>
                        </el-card>
                        <!-- 编辑区 -->
                        <el-card style="margin-left: 0px; border-radius: 10px; text-align: left;" v-show="contentShow2">
                            <div>
                                <el-form :inline="true" class="demo-form-inline">
                                    <el-form-item label="标题" style="margin-right: 20px;">
                                        <el-input v-model="article.title" placeholder="输入笔记标题"></el-input>
                                    </el-form-item>
                                    <el-form-item label="类型">
                                        <el-select v-model="article.categoryName" placeholder="选择类型">
                                            <el-option v-for="item in category" :value="item.categoryName" :label="item.categoryName"></el-option>
                                        </el-select>
                                    </el-form-item>
                                    <el-form-item style="margin-right: 0; float: right;">
                                        <el-popover placement="top" width="160" v-model="visible">
                                            <p>确定要清空内容吗？</p>
                                            <div style="text-align: center; margin: 0">
                                                <el-button type="primary" size="mini" @click="clean()">确定</el-button>
                                                <el-button size="mini" type="" @click="visible = false">取消</el-button>
                                            </div>
                                            <el-button class="qinkong" type="primary" slot="reference">清空</el-button>
                                        </el-popover>
                                    </el-form-item>
                                </el-form>
                            </div>
                            <div style="border: 1px solid #ccc;">
                                <Toolbar class="wangEdi" style="border-bottom: 1px solid #ccc" :editor="editor"
                                    :defaultConfig="toolbarConfig" :mode="mode" />
                                <Editor style="min-height: 500px; overflow-y: hidden; font-size: 18px;" v-model="html"
                                    :defaultConfig="editorConfig" :mode="mode" @onCreated="onCreated" />
                            </div>
                            <!-- <div style="text-align: center; padding-top: 15px;">
                            <el-button type="primary" @click="publish()">发布</el-button>
                        </div> -->
                        </el-card>

                    </div>
                </div>
            </div>
            <!-- 底部 -->
            <el-footer>Copyright © 2023 <span>violet蔡</span> All rights reserved</el-footer>
        </el-main>
        <div style="position: fixed; bottom: 70px; right: 14px;" @click="intop" text="向上">
            <i class="el-icon-top intop" style="font-size: 40px; color: #4fc5f6; font-weight: bold;"></i>
        </div>
        <div class="settings" style="position: fixed; bottom: 20px; right: 15px;" @click="sakuraChange" text="设置">
            <svg class="icon" aria-hidden="true" style="width: 2.2em; height: 2.2em;">
                <use xlink:href="#icon-shezhitianchong"></use>
            </svg>
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
            contentShow2: false,
            editor: null,
            html: '<p></p>',
            toolbarConfig: {},
            editorConfig: { 
                placeholder: '请输入内容...',
                MENU_CONF: {
                    // 配置上传图片
                    uploadImage: {
                        // 后端上传地址，必填
                        server: "http://localhost:9000/flies/noteP",
                        timeout: 5 * 1000, // 5s 超时时间 
                        fieldName: 'custom-fileName',
                        //选择文件时的类型限制，默认为['image/*'] 如不想限制，则设置为 []
                        allowedFileTypes: [],
                        metaWithUrl: true, // 参数拼接到 url 上
                        maxFileSize: 1000 * 1024 * 1024, //1g //设置大点 不然图片过大会报错
                        base64LimitSize: 1000000 * 1024, // 1g 以下插入 base64

                        // 上传前
                        onBeforeUpload(file) {
                            console.log('onBeforeUpload', file)
                            // Message({
                            //                 message: '图片正在上传中,请耐心等待',
                            //                 duration: 0,
                            //                 customClass: 'uploadTip',
                            //                 iconClass: 'el-icon-loading',
                            //                 showClose: true
                            //               });
                            return files;
                            return file.url // 返回哪些文件可以上传
                            // return false 会阻止上传
                        },
                        // 上传进度的回调函数
                        onProgress(progress) {
                            console.log('onProgress', progress)
                        },
                        // 单个文件上传成功之后
                        onSuccess(file, res) {
                            console.log('onSuccess', file.url, res)
                        },
                        // 单个文件上传失败
                        onFailed(file, res) {
                            alert(res.message)
                            console.log('onFailed', file, res)
                        },
                        // 上传错误，或者触发 timeout 超时
                        onError(file, err, res) {
                            alert(err.message)
                            console.error('onError', file, err, res)
                        },
                    }
                }
            },
            mode: 'default', // or 'simple'
            visible: false,
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
            category: [],
        };
    },
    created() {
        const herf = window.location.href.split("=");
        this.getArticle(herf[herf.length - 1]);
        window.scrollTo(0, 0);
        this.article.noteId = herf[herf.length - 1];
        this.userId = JSON.parse(Cookies.get("userId"));   //获取用户id
        this.article.userId = this.userId;
        this.loadCategory()  //加载类别

        //生成随机背景
        const randomNumber = Math.floor(Math.random() * 11) + 1;
        this.randomBackground = require(`@/assets/images/randomImage/${randomNumber}.jpg`);
    },
    methods: {
        onCreated(editor) {
            this.editor = Object.seal(editor) // 一定要用 Object.seal() ，否则会报错
        },
        //清空
        clean() {
            this.html = "<p><br></p>"
            this.visible = false
        },
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
        //获取类型
        async loadCategory() {
            const { data: res } = await this.$http.get(`getCategory?userId=${this.userId}`);
            this.category = res.data;
        },
        //回到顶部
        intop() {
            this.$refs.targetDiv.scrollIntoView({ behavior: 'smooth' });
        },
        //编辑
        edit() {
            this.contentShow = !this.contentShow;    //视图切换
            this.contentShow2 = !this.contentShow2;
        },
        //保存
        async save(){
            if (this.article.title == "") {
                this.$message.warning("标题不能为空！")
            } else {
                this.contentShow = !this.contentShow;
                this.contentShow2 = !this.contentShow2;
                //当前时间获取
                var nowTime = moment(new Date()).format("YYYY-MM-DD HH:mm:ss");
                this.article.author = JSON.parse(Cookies.get("user")).username;
                this.article.time = nowTime;
                this.article.content = this.html;

                const { data: res } = await this.$http.put("/updateArticle", this.article)    //访问后台
                if (res == "success") {
                    this.$message.success("更新成功！")
                } else {
                    this.$message.error("更新失败！")
                }
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
    mounted() {

    },
    beforeDestroy() {
        const editor = this.editor
        if (editor == null) return
        editor.destroy() // 组件销毁时，及时销毁编辑器
    }

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
  .noteFont{
    font-size: 18px !important;
  }
  .noteFont2{
    font-size: 18px !important;
  }

  .main-1 {
    height: 180px !important;
  }
  .bianji{
    position: fixed !important;
    top: 185px !important;
    right: 10px !important;
    z-index: 10 !important;
  }
  .wangEdi{
    display: none !important;
  }
  .aside-content {
    display: none !important;
  }
  .qinkong{
    display: none;
  }
  /deep/ .el-form-item__content{
    max-width: 100px;
  }
  /deep/ .el-card__body{
    padding: 10px;
  }
  .page-container{
    padding: 20px 5px 40px 5px !important;
  }
}
</style>