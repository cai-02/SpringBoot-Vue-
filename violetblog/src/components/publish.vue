<template>
    <el-container>
        <!-- 头部引用 -->
        <p-header></p-header>
        <!-- 内容 -->
        <el-main>
            <div class="main-1">
                <h1 class="pubFont" style="font-size: 30px; color: rgb(254, 228, 224); margin-top: 125px;">
                    <span>每一次记录都是一种收获</span>
                </h1>
            </div>
            <!-- 主内容部分 -->
            <div class="main-2">
                <div class="page-container">
                    <!-- <el-form-item label="简介" prop="introduce">
                                <el-input type="textarea" v-model="article.introduce" placeholder="请输入文章简介"></el-input>
                            </el-form-item>
                            <el-form-item label="封面">
                                <el-upload action="http://localhost:9000/files/coverupload" :on-success="coverUploadSuccess" :before-upload="beforeAvatarUpload" :limit="1">
                                    <el-button size="small" type="primary">点击上传</el-button>
                                    <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过10MB</div>
                                </el-upload>
                            </el-form-item>
                            <el-form-item>
                                <span>
                                    <img class="cus" :src="this.imgUrl" style="max-width: 200px; max-height: 130px;"/>
                                </span>
                            </el-form-item> -->
                    <el-card style="width: 75%;">
                        <div>
                            <el-form :inline="true" class="demo-form-inline">
                                <el-form-item label="标题" style="margin-right: 20px;">
                                    <el-input v-model="notes.title" placeholder="输入笔记标题"></el-input>
                                </el-form-item>
                                <el-form-item label="分类">
                                    <el-select v-model="notes.categoryName" placeholder="选择分类">
                                        <el-option v-for="item in category" :value="item.categoryName"
                                            :label="item.categoryName"></el-option>
                                    </el-select>
                                </el-form-item>
                                <el-form-item class="baocunPhone" style="margin-right: 0; float: right;">
                                    <el-popover placement="top" width="160" v-model="visible">
                                        <p>确定要清空内容吗？</p>
                                        <div style="text-align: center; margin: 0">
                                            <el-button type="primary" size="mini" @click="clean()">确定</el-button>
                                            <el-button size="mini" type="" @click="visible = false">取消</el-button>
                                        </div>
                                        <el-button class="qinkong" type="primary" slot="reference" plain>清空</el-button>
                                    </el-popover>
                                    <el-button type="primary" style="margin-left: 15px;" @click="save()">保存</el-button>
                                </el-form-item>
                            </el-form>
                        </div>
                        <div style="border: 1px solid #ccc;">
                            <Toolbar class="wangEdi" style="border-bottom: 1px solid #ccc" :editor="editor" :defaultConfig="toolbarConfig"
                                :mode="mode" />
                            <Editor style="min-height: 500px; overflow-y: hidden; font-size: 18px;" v-model="html"
                                :defaultConfig="editorConfig" :mode="mode" @onCreated="onCreated" />
                        </div>
                        <!-- <div style="text-align: center; padding-top: 15px;">
                            <el-button type="primary" @click="publish()">发布</el-button>
                        </div> -->
                    </el-card>
                </div>
            </div>
            <!-- 底部 -->
            <el-footer>Copyright © 2023 <span>violet蔡</span> All rights reserved</el-footer>
        </el-main>
    </el-container>
</template>
  
<script>
import Header from '../components/header.vue'
import Vue from 'vue'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import moment from 'moment'
import Cookies from 'js-cookie'

export default Vue.extend({
    components: {
        "p-header": Header,
        Editor,
        Toolbar
    },
    data() {
        return {
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
            imgUrl: "",
            //文章数据对象
            notes: {
                title: "",
                author: "",
                content: "",
                time: "",
                categoryName: "",
                userId: 0,
            },
            category: [],
            userId: 0,
            visible: false,
        };
    },
    created() {
        window.scrollTo(0, 0);
        this.userId = JSON.parse(Cookies.get("userId"));   //获取用户id
        this.loadCategory()  //加载类别
    },
    methods: {
        onCreated(editor) {
            this.editor = Object.seal(editor) // 一定要用 Object.seal() ，否则会报错
        },
        //保存笔记
        async save() {
            //当前时间获取
            // var nowTime = moment(new Date()).format("YYYY-MM-DD HH:mm:ss");  后端获取
            this.notes.author = JSON.parse(Cookies.get("user")).username;
            //this.notes.time = nowTime;
            this.notes.content = this.html;

            this.notes.userId = this.userId;

            if (this.notes.title == "") {
                this.$message.warning("标题不能为空！")
            } else {
                const { data: res } = await this.$http.post("/addArticle", this.notes)    //访问后台
                if (res == "success") {
                    this.$message.success("保存成功！")
                    //this.$router.go(-1)
                    this.$router.push({ path: "/index" });
                } else {
                    this.$message.error("保存失败！")
                }
            }
        },
        //清空
        clean() {
            this.html = "<p><br></p>"
            this.visible = false
        },
        //获取类型
        async loadCategory() {
            const { data: res } = await this.$http.get(`getCategory?userId=${this.userId}`);
            this.category = res.data;
            this.notes.categoryName = this.category[0].categoryName
        },
        //图片上传设置
        beforeAvatarUpload(file) {
            const isJPG = file.type === 'image/jpeg' || file.type === 'image/png';
            const isPNG = file.type === 'image/png';
            const isLt2M = file.size / 1024 / 1024 < 10;

            if (!isJPG && !isPNG) {
                this.$message.error('上传封面只能是 JPG 或 PNG 格式!');
            }
            if (!isLt2M) {
                this.$message.error('上传封面大小不能超过 10MB!');
            }
            return isJPG && isLt2M;
        },
        //封面上传成功
        coverUploadSuccess(response) {
            this.imgUrl = response
        },
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

<style src="@wangeditor/editor/dist/css/style.css"></style>
<style lang="less" scoped>
.el-container {
    padding: 0;
    margin: 0;
}

.el-main {
    padding: 0;
}

.main-1 {
    background-image: url(../assets/images/publish2.jpg);
    background-size: cover;
    position: fixed;
    display: flex;
    justify-content: center;
    height: 100%;
    width: 100%;
    z-index: -4;
    margin: 0;
}

.main-2 {
    background: linear-gradient(rgba(255, 255, 255, 0.8), rgba(237, 255, 237, 0.8));
    width: 100%;
    margin-top: 217px;
    z-index: -3;
}

.page-container {
    display: flex;
    justify-content: space-around;
    width: 90%;
    padding: 20px;
    height: 100%;
    margin: 0 auto;
}

/deep/ .el-textarea__inner {
    height: 100px;
}

// 封面上传
.avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
}

.avatar-uploader .el-upload:hover {
    border-color: #409EFF;
}

.avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
}

.avatar {
    width: 178px;
    height: 178px;
    display: block;
}

/* 手机端样式 */
@media screen and (max-width: 767px) {
  .main-2 {
    margin-top: 160px !important;
  }
  .page-container{
    padding: 8px 0 0 0 !important;
  }
  /deep/ .el-card__body{
    padding: 10px;
  }
  /deep/ .el-card.is-always-shadow {
    width: 98% !important;
  }
  .el-main {
    overflow: hidden !important;
  }
  .qinkong{
    display: none;
  }
  .wangEdi{
    display: none !important;
  }
  /deep/ .el-form-item__content{
    max-width: 100px;
  }
  .baocunPhone{
    position: fixed;
    right: 5px;
    z-index: 10;
    bottom: -5px;
  }
  .pubFont{
    font-size: 20px !important;
    margin-top: 100px !important;
  }
}
</style>