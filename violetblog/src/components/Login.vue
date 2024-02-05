<template>
    <div class="login_container">
        <div v-show="show2" class="lo_re">
            <!-- 登录块 -->
            <transition name="el-zoom-in-top">
                <div class="login_box" style="background-color: rgb(255, 255, 255, 0.9);">
                    <div class="avatar_box">
                        <img src="../assets/logo.jpg" alt />
                    </div>
                    <div style="font-size: 30px; width: 75px; margin: auto; margin-top: 85px;">登 录</div>
                    <!-- 表单区域 -->
                    <el-form ref="loginFormRef" :model="loginForm" :rules="loginRules" class="login_form" label-width="0"
                        style="top: 140px;">
                        <!-- 用户名 -->
                        <el-form-item prop="username">
                            <el-input placeholder="请输入用户名" v-model="loginForm.username"
                                prefix-icon="el-icon-user"></el-input>
                        </el-form-item>
                        <!-- 密码 -->
                        <el-form-item prop="password">
                            <el-input placeholder="请输入密码" v-model="loginForm.password" prefix-icon="el-icon-lock"
                                type="password" show-password></el-input>
                        </el-form-item>
                        <!-- 记住我 -->
                        <el-form-item class="btns" style="margin-top: -15px; margin-bottom: 0; float: right;">
                            <input type="checkbox" id="rememberMe" v-model="rememberMe" />
                            <label for="rememberMe">记住我</label>
                        </el-form-item>
                        <!-- 按钮 -->
                        <el-form-item class="btns" style="margin-top: 47px;">
                            <div>
                                <el-button type="primary" @click="login()">提交</el-button>
                                <el-button type="warning" @click="resetLoginForm()">重置</el-button>
                            </div>
                        </el-form-item>
                    </el-form>
                </div>
            </transition>
            <!-- 注册提示 -->
            <transition name="el-zoom-in-top">
                <div class="regist_box" style="background-color: rgba(247 214 214 / 90%);">
                    <div class="quzc" style=" margin-top: 78%;">
                        <div style="margin-left: 8px;">
                            <span style="font-size: 25px;">没有账号？</span><br>
                        </div>
                        <div style="text-align: center;">
                            <span style="color: #4bacda;line-height: 40px; cursor: pointer; font-size: 18px;"
                            @click="show2 = !show2, show = !show">
                            去注册</span>
                        </div>
                    </div>
                </div>
            </transition>
        </div>
        <div v-show="show" class="lo_re2">
            <!-- 注册块 -->
            <transition name="el-zoom-in-bottom">
                <div class="registBox" style="background-color: rgb(255, 255, 255, 0.9);">
                    <div style="font-size: 30px; width: 75px; margin: auto; margin-top: 6%;">注 册</div>
                    <!-- 表单区域 -->
                    <el-form ref="registFormRef" :model="registForm" :rules="loginRules" class="regist_form"
                        label-width="0">
                        <!-- 用户名 -->
                        <el-form-item prop="username">
                            <el-input placeholder="请输入用户名" v-model="registForm.username"
                                prefix-icon="el-icon-user"></el-input>
                        </el-form-item>
                        <!-- 密码 -->
                        <el-form-item prop="password">
                            <el-input placeholder="请输入密码" v-model="registForm.password" prefix-icon="el-icon-lock"
                                type="password"></el-input>
                        </el-form-item>
                        <el-form-item prop="checkPassword">
                            <el-input placeholder="请确认密码" v-model="registForm.checkPassword" prefix-icon="el-icon-lock"
                                type="password"></el-input>
                        </el-form-item>
                        <!-- 邮箱 -->
                        <el-form-item prop="email">
                            <el-input placeholder="请输入邮箱" v-model="registForm.email"
                                prefix-icon="el-icon-message"></el-input>
                        </el-form-item>
                        <!-- 验证码 -->
                        <!-- <el-form-item prop="yzCode">
                            <el-input placeholder="请输入验证码" prefix-icon="el-icon-chat-dot-square"></el-input>
                        </el-form-item> -->
                        <!-- 按钮 -->
                        <el-form-item class="btns">
                            <el-button type="primary" @click="regist()">提交</el-button>
                            <el-button type="warning" @click="resetRegistForm()">重置</el-button>
                        </el-form-item>
                    </el-form>
                    <div style="color: #4bacda; cursor: pointer; float: right;position: absolute; bottom: 15px; right: 10px;"
                        @click="show = !show, show2 = !show2">返回登录></div>
                </div>
            </transition>
        </div>
        <div class="settings" style="position: fixed; bottom: 20px; right: 15px;" @click="sakuraChange" text="设置">
            <svg class="icon" aria-hidden="true" style="width: 2.2em; height: 2.2em;">
                <use xlink:href="#icon-shezhitianchong"></use>
            </svg>
        </div>
    </div>
</template>

<script>
import { startSakura, stopp, staticx } from "@/assets/js/sakura"
import Cookies from 'js-cookie'

export default {
    data() {
        var validatePass2 = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('请再次输入密码'));
            } else if (value !== this.registForm.password) {
                callback(new Error('两次输入密码不一致!'));
            } else {
                callback();
            }
        };
        return {
            //表单数据对象
            loginForm: {
                username: "",
                password: "",
            },
            registForm: {
                username: "",
                password: "",
                checkPassword: "",
                email: "",
            },
            rememberMe: false,  //是否记住用户
            //验证对象
            loginRules: {
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
                //校验确认密码
                checkPassword: [
                    { validator: validatePass2, trigger: 'blur' }
                ],
                email: [
                    { required: true, message: '邮箱为必填项', trigger: 'blur' },  //必填项验证
                ],
            },
            //展开动画
            show2: true,
            show: false,
            userCookie: {
                username: "",
                password: ""
            },
        };
    },
    created() {
        this.autoLogin();
    },
    methods: {
        //重置表单内容
        resetLoginForm() {
            this.$refs.loginFormRef.resetFields();
        },
        resetRegistForm() {
            this.$refs.registFormRef.resetFields();
        },
        //cookie存在自动登录
        async autoLogin() {
            const rememberMe = Cookies.get('rememberMe');
            if (rememberMe) {
                const user = JSON.parse(Cookies.get('user'));
                this.userCookie.username = user.username;
                this.userCookie.password = user.password;
                const { data: res } = await this.$http.post("login", this.userCookie)    //访问后台
                if (res.flag == "ok") {
                    this.$message.success("登录成功！")
                    this.$router.push({ path: "/index" });      //页面路由跳转
                } else {
                    this.$message.error("登录失败！")
                }
            }
        },
        //登录
        login() {
            //验证校验规则
            this.$refs.loginFormRef.validate(async valid => {
                if (!valid) return;   //校验失败，验证用户名和密码合法性
                const { data: res } = await this.$http.post("login", this.loginForm)    //访问后台
                if (res.flag == "ok") {
                    // 设置cookie，有效期为7天
                    Cookies.set("user", JSON.stringify(res.user), { expires: 7 });
                    Cookies.set("userId", res.userId, { expires: 7 });
                    Cookies.set("headimage", res.headimage, { expires: 7 });
                    //保存登录状态
                    if (this.rememberMe) {
                        Cookies.set('rememberMe', true, { expires: 7 });
                    }
                    window.sessionStorage.setItem('success', true);
                    this.$message.success("登录成功！")
                    this.$router.push({ path: "/index" });      //页面路由跳转
                    //存储用户角色
                    const { data: res2 } = await this.$http.get(`getRole?userId=${Cookies.get("userId")}`)    //访问后台
                    Cookies.set("role", res2, { expires: 7 })
                    //后台信息页
                    Cookies.set('activePath', "/message", { expires: 7 });
                } else {
                    this.$message.error("登录失败！")
                }
            })
        },
        //注册
        regist() {
            //验证校验规则
            this.$refs.registFormRef.validate(async valid => {
                if (!valid) return;   //校验失败，验证用户名和密码合法性
                //检测用户名唯一性
                const { data: res } = await this.$http.get(`uniqueUser?username=${this.registForm.username}`);
                if (res != true) {
                    return this.$message.error("用户名已存在！");
                } else {
                    const { data: res } = await this.$http.post("adduser", this.registForm);
                    if (res != "success") {
                        return this.$message.error("注册失败！");
                    }
                    this.$message.success("注册成功！");
                    this.$refs.registFormRef.resetFields();  //清空表单
                    this.show = !this.show;   //返回登录
                    this.show2 = !this.show2
                }
            })
        },
        sakuraChange() {  //落樱效果切换
            if (staticx) {
                stopp();
            } else {
                startSakura();
            }
        },
    },
}
</script>

<style lang="less" scoped>
.login_container {
    background-image: url("../assets/images/fengmian.jpg");
    background-size: cover;
    height: 100%;
}

.lo_re,
.lo_re2 {
    display: flex;
    left: 50%;
    top: 50%;
    transform: translate(-50%, -50%);
    position: absolute;
    border-radius: 3px;
}

.login_box {
    width: 430px;
    height: 370px;
    position: relative;
}

.registBox {
    width: 430px;
    height: 420px;
    position: relative;
}

.regist_box {
    width: 200px;
    height: 370px;
    position: relative;

    div {
        width: 125px;
        margin: auto;
    }
}

.avatar_box {
    width: 130px;
    height: 130px;
    border: 1px solid #eee;
    border-radius: 50%;
    padding: 5px;
    box-shadow: 0 0 2px #ddd;
    position: absolute;
    left: 50%;
    transform: translate(-50%, -50%);
    background-color: cornsilk;

    img {
        width: 100%;
        height: 100%;
        border-radius: 50%;
        background-color: #eee;
    }
}

.btns {
    display: flex;
    justify-content: center;
}

.login_form,
.regist_form {
    position: absolute;
    bottom: 3%;
    width: 100%;
    padding: 0 10px;
    box-sizing: border-box;
}

.regist_form {
    bottom: 4%;
}

/deep/ .el-form-item__content {
    font-size: 17px;
    display: flex;
    color: #e6a23c;
}

/* 手机端样式 */
@media screen and (max-width: 767px) {
  .lo_re, .lo_re2 {
    width: 100%;
    left: 0;
    top: 19%;
    height: 50%;
    transform: none;
    display: grid;
  }
  .login_box, .registBox, .regist_box {
    width: 100%;
  }
  .regist_box {
    height: 89px !important;
  }
  .quzc {
    margin-top: 2% !important;
  }
  
}
</style>