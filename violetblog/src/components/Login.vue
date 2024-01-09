<template>
    <div class="login_container">
        <div class="lo_re">
            <!-- 登录块 -->
            <transition name="el-zoom-in-top">
                <div class="login_box" v-show="show2" style="background-color: rgb(255, 255, 255, 0.9);">
                    <div class="avatar_box">
                        <img src="../assets/logo.jpg" alt />
                    </div>
                    <div style="font-size: 30px; width: 75px; margin: auto; margin-top: 20%;">登 录</div>
                    <!-- 表单区域 -->
                    <el-form ref="loginFormRef" :model="loginForm" :rules="loginRules" class="login_form" label-width="0">
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
                        <!-- 按钮 -->
                        <el-form-item class="btns">
                            <el-button type="primary" @click="login()">提交</el-button>
                            <el-button type="warning" @click="resetLoginForm()">重置</el-button>
                        </el-form-item>
                    </el-form>
                </div>
            </transition>
            <!-- 注册提示 -->
            <transition name="el-zoom-in-top">
                <div class="regist_box" v-show="show2" style="background-color: rgba(247 214 214 / 90%);">
                    <div style=" margin-top: 75%;">
                        <span style="font-size: 25px;">没有账号？</span><br>
                        <span style="color: #4bacda;line-height: 40px; cursor: pointer; font-size: 18px;"
                            @click="show2 = !show2, show = !show">&emsp;
                            去注册</span>
                    </div>
                </div>
            </transition>
        </div>
        <div class="lo_re2">
            <!-- 注册块 -->
            <transition name="el-zoom-in-bottom">
                <div class="registBox" v-show="show" style="background-color: rgb(255, 255, 255, 0.9);">
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
                        <!-- 邮箱 -->
                        <el-form-item prop="email">
                            <el-input placeholder="请输入邮箱" v-model="registForm.email"
                                prefix-icon="el-icon-message"></el-input>
                        </el-form-item>
                        <!-- 验证码 -->
                        <el-form-item prop="yzCode">
                            <el-input placeholder="请输入验证码" prefix-icon="el-icon-chat-dot-square"></el-input>
                        </el-form-item>
                        <!-- 按钮 -->
                        <el-form-item class="btns">
                            <el-button type="primary" @click="regist()">提交</el-button>
                            <el-button type="warning" @click="resetRegistForm()">重置</el-button>
                        </el-form-item>
                    </el-form>
                    <div style="color: #4bacda; cursor: pointer; float: right;position: absolute; bottom: 15px; right: 10px;" @click="show = !show, show2 = !show2">返回登录></div>
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

export default {
    data() {
        return {
            //表单数据对象
            loginForm: {
                username: "",
                password: ""
            },
            registForm: {
                username: "",
                password: "",
                email: ""
            },
            //验证对象
            loginRules: {
                //校验用户名
                username: [
                    { required: true, message: '用户名为必填项', trigger: 'blur' },  //必填项验证
                    { min: 2, max: 8, message: '长度在 2 到 8 个字符', trigger: 'blur' }  //长度验证
                ],
                //校验密码
                password: [
                    { required: true, message: '用户密码为必填项', trigger: 'blur' },  //必填项验证
                    { min: 6, max: 12, message: '长度在 6 ~ 12 个字符', trigger: 'blur' }  //长度验证
                ],
                email: [
                    { required: true, message: '邮箱为必填项', trigger: 'blur' },  //必填项验证
                    { min: 6, max: 18, message: '长度在 6 ~ 18 个字符', trigger: 'blur' }  //长度验证
                ],
            },
            //展开动画
            show2: true,
            show: false
        };
    },
    methods: {
        //重置表单内容
        resetLoginForm() {
            this.$refs.loginFormRef.resetFields();
        },
        resetRegistForm() {
            this.$refs.registFormRef.resetFields();
        },
        //登录
        login() {
            //验证校验规则
            this.$refs.loginFormRef.validate(async valid => {
                if (!valid) return;   //校验失败，验证用户名和密码合法性
                const { data: res } = await this.$http.post("login", this.loginForm)    //访问后台
                if (res.flag == "ok") {
                    this.$message.success("登录成功！")
                    this.$router.push({ path: "/index" });      //页面路由跳转
                    //console.log(res.user)
                    window.sessionStorage.setItem("user", JSON.stringify(res.user));   //存储user对象
                    window.sessionStorage.setItem("userId", res.userId);
                    window.sessionStorage.setItem("headimage", res.headimage);
                    //后台信息页
                    window.sessionStorage.setItem('activePath', "/message");
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
                const {data:res} = await this.$http.post("adduser", this.registForm);
                if(res != "success"){
                    return this.$message.error("注册失败！");
                }
                this.$message.success("注册成功！");
                this.show = !this.show;   //返回登录
                this.show2 = !this.show2
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

.login_box{
    width: 430px;
    height: 350px;
    position: relative;
}
.registBox {
    width: 430px;
    height: 420px;
    position: relative;
}

.regist_box {
    width: 200px;
    height: 350px;
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
</style>