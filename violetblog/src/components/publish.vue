<template>
    <el-container>
        <!-- 头部引用 -->
        <p-header></p-header>
        <!-- 内容 -->
        <el-main>
            <div class="main-1">
                <h1 style="font-size: 30px; color: rgb(251 244 160); margin-top: 125px;">
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
                    <el-card style="width: 75%; margin-left: 8px;">
                        <div>
                            <el-form :inline="true" class="demo-form-inline">
                                <el-form-item label="标题" style="margin-right: 20px;">
                                    <el-input v-model="notes.title" placeholder="输入笔记标题"></el-input>
                                </el-form-item>
                                <el-form-item label="类型">
                                    <el-select v-model="notes.categoryName" placeholder="选择类型">
                                        <el-option label="默认" value="默认"></el-option>
                                        <el-option label="学习人生" value="学习人生"></el-option>
                                        <el-option label="笔记" value="笔记"></el-option>
                                    </el-select>
                                </el-form-item>
                                <el-form-item style="margin-right: 0; float: right;">
                                    <el-popover placement="top" width="160" v-model="visible">
                                        <p>确定要清空内容吗？</p>
                                        <div style="text-align: center; margin: 0">
                                            <el-button type="primary" size="mini" @click="clean()">确定</el-button>
                                            <el-button size="mini" type="" @click="visible = false">取消</el-button>
                                        </div>
                                        <el-button type="primary" slot="reference" plain>清空</el-button>
                                    </el-popover>
                                    <el-button type="primary" style="margin-left: 15px;" @click="save()">保存</el-button>
                                </el-form-item>
                            </el-form>
                        </div>
                        <div style="border: 1px solid #ccc;">
                            <Toolbar style="border-bottom: 1px solid #ccc" :editor="editor" :defaultConfig="toolbarConfig"
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
        <div class="settings" style="position: fixed; bottom: 20px; right: 15px;" @click="sakuraChange" text="设置">
            <svg class="icon" aria-hidden="true" style="width: 2.2em; height: 2.2em;">
                <use xlink:href="#icon-shezhitianchong"></use>
            </svg>
        </div>
    </el-container>
</template>
  
<script>
import { startSakura, stopp, staticx } from "@/assets/js/sakura"
import Header from '../components/header.vue'
import Vue from 'vue'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import moment from 'moment'

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
            editorConfig: { placeholder: '请输入内容...' },
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
            visible: false,
            //验证对象
            // publishRules: {
            //     //校验用户名
            //     title: [
            //         { required: true, message: '标题不能为空', trigger: 'blur' },  //必填项验证
            //         { min: 1, max: 20, message: '长度必须在 1 到 20 个字符之间', trigger: 'blur' }  //长度验证
            //     ],
            //     type: [
            //         { required: true, message: '类型不能为空', trigger: 'change' },  //必填项验证
            //     ],
            // },
        };
    },
    created() {
        window.scrollTo(0, 0);
    },
    methods: {
        onCreated(editor) {
            this.editor = Object.seal(editor) // 一定要用 Object.seal() ，否则会报错
        },
        //保存笔记
        async save() {
            //当前时间获取
            var nowTime = moment(new Date()).format("YYYY-MM-DD HH:mm:ss");
            this.notes.author = JSON.parse(sessionStorage.getItem("user")).username;
            this.notes.time = nowTime;
            this.notes.content = this.html;

            this.notes.userId = sessionStorage.getItem("userId");

            const { data: res } = await this.$http.post("/addArticle", this.notes)    //访问后台
            if (res == "success") {
                this.$message.success("保存成功！")
                this.$router.push({ path: "/index" });
            } else {
                this.$message.error("保存失败！")
            }
        },
        //清空
        clean() {
            this.html = "<p><br></p>"
            this.visible = false
        },
        //图片上次设置
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
        sakuraChange() {  //落樱效果切换
            if (staticx) {
                stopp();
            } else {
                startSakura();
            }
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
    background-image: url(../assets/images/publishbj.jpg);
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
    background: linear-gradient(rgba(255, 255, 255, 0.7), rgba(237, 255, 237, 0.7));
    width: 100%;
    margin-top: 15%;
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
</style>