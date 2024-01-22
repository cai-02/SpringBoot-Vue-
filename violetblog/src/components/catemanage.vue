<template>
    <el-container style="height: 100%;">
        <!-- 头部引用 -->
        <p-header></p-header>
        <!-- 内容 -->
        <el-main>
            <div class="main-1">
            </div>
            <!-- 主内容部分 -->
            <div class="main-2">
                <div class="page-title" style="text-align: center; margin-bottom: 10px;">
                    <div style="font-size: 40px; font-weight: bold; color: rgb(255, 209, 125);">
                        <span>类别</span>
                    </div>
                </div>
                <div class="page-container" style="justify-content: center; position: relative;">
                    <el-card
                        style="width: 80%; background-color: rgb(255, 255, 255, 0.5); border-radius: 25px; padding: 0;">
                        <el-card class="cate el-card-two"
                            :style="{ 'background-color': getRandomColor(), height: '80px', cursor: 'pointer' }"
                            style="max-width: 250px; border-radius: 30px;">
                            <div
                                style="padding: 0 15px; font-size: 25px; font-weight: bold; color: white; text-align: center; margin-top: 5px;">
                                <span class="category-name">学习人生</span>
                            </div>
                        </el-card>
                        <el-card class="cate el-card-two"
                            :style="{ 'background-color': getRandomColor(), height: '80px', cursor: 'pointer' }"
                            style="max-width: 250px; border-radius: 30px;">
                            <div
                                style="padding: 0 15px; font-size: 25px; font-weight: bold; color: white; text-align: center; margin-top: 5px;">
                                <span class="category-name">学习人生</span>
                            </div>
                        </el-card>
                        <el-card class="cate el-card-two"
                            :style="{ 'background-color': getRandomColor(), height: '80px', cursor: 'pointer' }"
                            style="max-width: 250px; border-radius: 30px;">
                            <div
                                style="padding: 0 15px; font-size: 25px; font-weight: bold; color: white; text-align: center; margin-top: 5px;">
                                <span class="category-name">学习</span>
                            </div>
                        </el-card>
                        <el-card class="cate el-card-two"
                            :style="{ 'background-color': getRandomColor(), height: '80px', cursor: 'pointer' }"
                            style="max-width: 250px; border-radius: 30px;">
                            <div
                                style="padding: 0 15px; font-size: 25px; font-weight: bold; color: white; text-align: center; margin-top: 5px;">
                                <span class="category-name">学习</span>
                            </div>
                        </el-card>
                        <el-card class="cate el-card-two"
                            :style="{ 'background-color': getRandomColor(), height: '80px', cursor: 'pointer' }"
                            style="max-width: 250px; border-radius: 30px;">
                            <div
                                style="padding: 0 15px; font-size: 25px; font-weight: bold; color: white; text-align: center; margin-top: 5px;">
                                <span class="category-name">学习</span>
                            </div>
                        </el-card>
                        <el-card class="cate el-card-two"
                            :style="{ 'background-color': getRandomColor(), height: '80px', cursor: 'pointer' }"
                            style="max-width: 250px; border-radius: 30px;">
                            <div
                                style="padding: 0 15px; font-size: 25px; font-weight: bold; color: white; text-align: center; margin-top: 5px;">
                                <span class="category-name">学习</span>
                            </div>
                        </el-card>
                    </el-card>
                    <el-popover placement="top" trigger="hover">
                        <el-button type="danger" size="mini" plain @click="">删除</el-button><br />
                        <el-button type="warning" size="mini" plain @click="" style="margin-top: 6px;">多选</el-button>
                        <div slot="reference" class="el-icon-more"
                            style="position: absolute; color: red; right: 12.5%; top: 30px; font-size: 25px;">
                        </div>
                    </el-popover>
                    <el-popover placement="top" trigger="hover" content="添加">
                        <div slot="reference"
                            style="background: #39c5bb; width: 65px; color: white;
                        font-size: 19px; height: 65px; border-radius: 50%; right: 73px; position: absolute; cursor: pointer;">
                            <span class="el-icon-plus"
                                style="display: block; color: #ffffff; font-size: 35px; margin-top: 14px; margin-left: 14.8px;"></span>
                        </div>
                    </el-popover>
                </div>
            </div>
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

export default Vue.extend({
    components: {
        "p-header": Header,
    },
    data() {
        return {
            category: [],
            userId: 0,
        };
    },
    created() {
        this.userId = JSON.parse(sessionStorage.getItem("userId"));   //获取用户id
        this.loadCategory()  //加载类别
    },
    methods: {
        //获取类型
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
    height: 100%;
}

.cate:hover .category-name {
    color: pink;
}

.main-1 {
    background-image: url(../assets/images/publishbj.jpg);
    background-size: cover;
    position: fixed;
    height: 100%;
    width: 100%;
    z-index: -4;
    margin: 0;
}

.main-2 {
    width: 100%;
    margin-top: 100px;
    height: 70%;
}

.page-container {
    display: flex;
    justify-content: space-around;
    width: 90%;
    padding: 20px;
    height: 100%;
    margin: 0 auto;
}

.cate {
    display: inline-block;
    margin: 15px 15px;
    width: auto;
}
</style>