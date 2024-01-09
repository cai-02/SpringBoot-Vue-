<template>
    <div>
        <!-- 面包屑导航 -->
        <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>个人中心</el-breadcrumb-item>
            <el-breadcrumb-item>信息管理</el-breadcrumb-item>
        </el-breadcrumb>
        <!-- 用户列表主体区域 -->
        <el-card>
            <div style="margin-left: 20px; margin-bottom: 20px;">
                <span>
                    <img class="cus" :src="this.headImg" width="75px" height="75px" style="border-radius: 50%;" @click="changeTx" />
                </span>
            </div>
            <div class="" style="width: 40%;">
                <el-form ref="form" :model="form" :rules="editFormRules" label-width="80px">
                    <el-form-item label="用户名" prop="username">
                        <el-input v-model="form.username" placeholder="请输入内容"></el-input>
                    </el-form-item>
                    <el-form-item label="邮箱" prop="email">
                        <el-input v-model="form.email" placeholder="请输入内容"></el-input>
                    </el-form-item>
                    <el-form-item label="性别">
                        <el-radio-group v-model="form.gender">
                        <el-radio value="男" label="男"></el-radio>
                        <el-radio value="女" label="女"></el-radio>
                        </el-radio-group>
                    </el-form-item>
                    <el-form-item label="简介">
                        <el-input type="textarea" v-model="form.introduction"></el-input>
                    </el-form-item>
                    <el-form-item style="float: right;">
                        <el-button type="primary" @click="editorMessage()">立即修改</el-button>
                    </el-form-item>
                </el-form>
            </div>
        </el-card>
        <!-- 头像修改框 -->
        <el-dialog title="修改头像" :visible.sync="changeDialogVisible" style="text-align: center; padding-bottom: -10px;" width="30%">
            <el-upload ref="myUpload" class="upload-demo" drag action="http://localhost:9000/files/upload" :limit="1" :show-file-list="false" :before-upload="beforeAvatarUpload" :on-success="filesUploadSuccess">
                <i class="el-icon-upload"></i>
                <div class="el-upload__text">将图片拖到此处，或<em>点击上传</em></div>
                <div class="el-upload__tip" slot="tip">只能上传jpg/png文件，且不超过2MB</div>
            </el-upload>
        </el-dialog>
    </div>
</template>

<script>
export default {
    data() {
        return {
            form: {
                username: "",
                email: "",
                gender: "",
                id: 0,
                introduction: "",
            },
            userId: 0,
            headImg: "",
            changeDialogVisible: false,     //对话框状态
            //修改表单验证
            editFormRules: {
                username: [
                    { required: true, message: '请输入用户名', trigger: 'blur' },
                    { min: 5, max: 12, message: '长度在 5 到 12 个字符', trigger: 'blur' }
                ],
                email: [
                    { required: true, message: '请输入邮箱', trigger: 'blur' },
                    { min: 5, max: 16, message: '请输入正确邮箱地址', trigger: 'blur' }
                ]
            },
        }
    },
    async created() {
        if(sessionStorage.getItem("userId") != null && sessionStorage.getItem("headimage") != null){
            this.userId = sessionStorage.getItem("userId");   //获取用户id
            this.form.id = sessionStorage.getItem("userId");
            this.headImg = sessionStorage.getItem("headimage");
            //获取用户信息
            const {data:res} = await this.$http.put(`getupdate?id=${this.userId}`);
            this.form.username = res.username;
            this.form.email = res.email;
            this.form.gender = res.gender;
            this.form.introduction = res.introduction;
        }else{
            return;
        }
    },
    methods: {
        //打开窗口
        changeTx() {
            this.changeDialogVisible = true;
        },
        //更换头像
        async filesUploadSuccess(res){
            const {data:rest} = await this.$http.put(`gethead?img=${res}&id=${this.userId}`);
            if(rest != "success") return;
            this.headImg = res;
            window.sessionStorage.setItem("headimage", res);  //重新存储头像
            this.$refs.myUpload.clearFiles();
            this.changeDialogVisible = false;
            this.$message.success("修改成功！");
        },
        beforeAvatarUpload(file) {
            const isJPG = file.type === 'image/jpeg' || file.type === 'image/png';
            const isLt2M = file.size / 1024 / 1024 < 5;

            if (!isJPG && !isPNG) {
            this.$message.error('上传头像图片只能是 JPG 或 PNG 格式!');
            }
            if (!isLt2M) {
            this.$message.error('上传头像图片大小不能超过 5MB!');
            }
            return isJPG && isLt2M;
        },
        async editorMessage(){
            this.$refs.form.validate(async valid => {
                if(!valid) return;
                const comfirmResult = await this.$confirm("此操作将修改您的个人信息，是否继续？", "提示", {
                    confirmButtonText:'确定',
                    cancelButtonText:'取消',
                    type:'warning'
                }).catch(err => err)
                if(comfirmResult != 'confirm'){   //取消
                    return this.$message.info("已取消修改");
                }
                console.log("form: " + JSON.stringify(this.form))
                const {data:res} = await this.$http.put("edituser", this.form);
                if(res != "success"){
                    return this.$message.error("修改失败！");
                }
                this.$message.success("修改成功！");
            }
        )}
    }
}
</script>

<style lang="less" scoped>
.el-breadcrumb {
    margin-bottom: 15px;
    font-size: 17px;
}
/deep/ .el-upload{
    width: 100%;
}
/deep/ .el-upload .el-upload-dragger{
    width: auto;
}
</style>
