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
                    <el-popover placement="right" trigger="hover" content="修改头像" v-show="contentShow">
                        <img slot="reference" class="cus" :src="this.headImg" width="75px" height="75px"
                            style="border-radius: 50%;" @click="changeTx" />
                    </el-popover>
                </span>
            </div>
            <div class="messPh" style="width: 40%;">
                <el-form ref="form" :model="form" :rules="editFormRules" label-width="80px">
                    <el-form-item label="用户名" prop="username">
                        <el-input v-model="form.username" placeholder="请输入用户名"></el-input>
                    </el-form-item>
                    <el-form-item label="密码" prop="password">
                        <el-input placeholder="请输入密码" :type="flag ? 'text' : 'password'" v-model="form.password">
                            <i slot="suffix" :class="[flag ? 'el-icon-minus' : 'el-icon-view']"
                                style="margin-top:8px;font-size:18px;" autocomplete="auto" @click="flag = !flag" />
                        </el-input>

                    </el-form-item>
                    <el-form-item label="邮箱">
                        <el-input v-model="form.email" placeholder="请输入邮箱"></el-input>
                    </el-form-item>
                    <!-- <el-form-item label="性别">
                        <el-radio-group v-model="form.gender">
                        <el-radio value="男" label="男"></el-radio>
                        <el-radio value="女" label="女"></el-radio>
                        </el-radio-group>
                    </el-form-item> -->
                    <!-- <el-form-item label="简介">
                        <el-input type="textarea" v-model="form.introduction"></el-input>
                    </el-form-item> -->
                    <el-form-item style="float: right;">
                        <el-button type="primary" @click="editorMessage()">立即修改</el-button>
                    </el-form-item>
                </el-form>
            </div>
        </el-card>
        <!-- 头像修改框 -->
        <el-dialog title="修改头像" class="xiugaiTou" :visible.sync="changeDialogVisible" style="text-align: center; padding-bottom: -10px;"
            width="30%">
            <!-- <el-upload ref="myUpload" class="upload-demo" drag action="http://47.108.66.150:9000/files/upload" :limit="1" -->
            <el-upload ref="myUpload" class="upload-demo" drag action="http://localhost:9000/files/upload" :limit="1"
                :show-file-list="false" :before-upload="beforeAvatarUpload" :on-success="filesUploadSuccess">
                <i class="el-icon-upload"></i>
                <div class="el-upload__text">将图片拖到此处，或<em>点击上传</em></div>
                <div class="el-upload__tip" slot="tip">只能上传jpg/png文件，且不超过2MB</div>
            </el-upload>
        </el-dialog>
    </div>
</template>

<script>
import Cookies from 'js-cookie'

export default {
    data() {
        return {
            form: {
                username: "",
                password: "",
                email: "",
                userId: 0,
            },
            originalPassword: '', // 用于保存初始密码
            originalName: '', // 用于保存初始用户名
            flag: false,  //密码显示
            userId: 0,
            headImg: "",
            changeDialogVisible: false,     //对话框状态
            contentShow: true,
            headFlag: "",          //旧头像标识
            //修改表单验证
            editFormRules: {
                //校验用户名
                username: [
                    { required: true, message: '用户名为必填项', trigger: 'blur' },  //必填项验证
                    { min: 1, max: 12, message: '长度在 1 到 12 个字符', trigger: 'blur' }  //长度验证
                ],
                //校验密码
                password: [
                    { required: true, message: '用户密码为必填项', trigger: 'blur' },  //必填项验证
                    { min: 6, max: 12, message: '长度在 6 ~ 12 个字符', trigger: 'blur' },  //长度验证
                ],
            },
        }
    },
    async created() {
        if (Cookies.get("userId") != null && Cookies.get("headimage") != null) {
            this.userId = Cookies.get("userId");   //获取用户id
            this.form.userId = Cookies.get("userId");
            this.headImg = Cookies.get("headimage");
            //获取用户信息
            const { data: res } = await this.$http.put(`getupdate?id=${this.userId}`);
            //console.log(res)
            this.form.username = res.username;
            this.form.email = res.email;
            this.form.password = res.password;
            // 在组件创建时保存初始密码
            this.originalPassword = this.form.password;
            this.originalName = this.form.username;
        } else {
            return;
        }

    },
    methods: {
        //打开窗口
        changeTx() {
            this.changeDialogVisible = true;
        },
        //更换头像
        async filesUploadSuccess(res) {
            this.headFlag = (Cookies.get("headimage")).split("/")[(Cookies.get("headimage")).split("/").length - 1]; //获取旧头像标识

            const { data: rest } = await this.$http.put(`gethead?img=${res}&id=${this.userId}&headFlag=${this.headFlag}`);
            if (rest != "success") return;
            this.headImg = res;
            Cookies.set("headimage", res, { expires: 7 });  //重新存储头像
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
        async editorMessage() {
            this.$refs.form.validate(async valid => {
                if (!valid) return;
                //检测用户名唯一性
                const { data: res } = await this.$http.get(`uniqueUser?username=${this.form.username}`);
                if (res != true) {   //用户已存在
                    if (this.form.username !== this.originalName) {  //是否做过更改
                        return this.$message.error("用户名已存在！");
                    } else {
                        // 在保存前检查密码是否被修改
                        if (this.form.password !== this.originalPassword) {
                            const comfirmResult = await this.$confirm("密码已更改将重新登录，是否继续？", "提示", {
                                confirmButtonText: '确定',
                                cancelButtonText: '取消',
                                type: 'warning'
                            }).catch(err => err)
                            if (comfirmResult != 'confirm') {   //取消
                                return this.$message.info("已取消修改");
                            }
                            //console.log("form: " + JSON.stringify(this.form))
                            const { data: res } = await this.$http.put("edituser", this.form);
                            if (res != "success") {
                                return this.$message.error("修改失败！");
                            }
                            this.$message.success("修改成功！");
                            //清除数据
                            this.$router.push("/login");
                            Cookies.remove('user');
                            Cookies.remove('rememberMe');
                            Cookies.remove('success');
                            Cookies.remove("user");
                            Cookies.remove("userId");
                            Cookies.remove("headimage");
                            Cookies.remove("activePath");
                            Cookies.remove("role");
                            sessionStorage.clear("success");
                        } else {
                            const comfirmResult = await this.$confirm("此操作将修改您的个人信息，是否继续？", "提示", {
                                confirmButtonText: '确定',
                                cancelButtonText: '取消',
                                type: 'warning'
                            }).catch(err => err)
                            if (comfirmResult != 'confirm') {   //取消
                                return this.$message.info("已取消修改");
                            }
                            //console.log("form: " + JSON.stringify(this.form))
                            const { data: res } = await this.$http.put("edituser", this.form);
                            if (res != "success") {
                                return this.$message.error("修改失败！");
                            }
                            this.$message.success("修改成功！");
                            //重新设置cookie
                            const { data: res2 } = await this.$http.put(`getupdate?id=${this.userId}`);
                            Cookies.set("user", JSON.stringify(res2), { expires: 7 });
                        }
                    }
                } else {
                    // 在保存前检查密码是否被修改
                    if (this.form.password !== this.originalPassword) {
                        const comfirmResult = await this.$confirm("密码已更改将重新登录，是否继续？", "提示", {
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            type: 'warning'
                        }).catch(err => err)
                        if (comfirmResult != 'confirm') {   //取消
                            return this.$message.info("已取消修改");
                        }
                        //console.log("form: " + JSON.stringify(this.form))
                        const { data: res } = await this.$http.put("edituser", this.form);
                        if (res != "success") {
                            return this.$message.error("修改失败！");
                        }
                        this.$message.success("修改成功！");
                        //清除数据
                        this.$router.push("/login");
                        Cookies.remove('user');
                        Cookies.remove('rememberMe');
                        Cookies.remove('success');
                        Cookies.remove("user");
                        Cookies.remove("userId");
                        Cookies.remove("headimage");
                        Cookies.remove("activePath");
                        Cookies.remove("role");
                        sessionStorage.clear("success");
                    } else {
                        const comfirmResult = await this.$confirm("此操作将修改您的个人信息，是否继续？", "提示", {
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            type: 'warning'
                        }).catch(err => err)
                        if (comfirmResult != 'confirm') {   //取消
                            return this.$message.info("已取消修改");
                        }
                        //console.log("form: " + JSON.stringify(this.form))
                        const { data: res } = await this.$http.put("edituser", this.form);
                        if (res != "success") {
                            return this.$message.error("修改失败！");
                        }
                        this.$message.success("修改成功！");
                        //重新设置cookie
                        const { data: res2 } = await this.$http.put(`getupdate?id=${this.userId}`);
                        Cookies.set("user", JSON.stringify(res2), { expires: 7 });
                    }
                }
            })
        }
    },
}
</script>

<style lang="less" scoped>
.el-breadcrumb {
    margin-bottom: 15px;
    font-size: 17px;
}

/deep/ .el-upload {
    width: 100%;
}

/deep/ .el-upload .el-upload-dragger {
    width: auto;
}

/* 手机端样式 */
@media screen and (max-width: 767px) {
  .messPh{
    width: 100% !important;
  }
  /deep/ .xiugaiTou .el-dialog{
    width: 80% !important;
  }
  
}
</style>
