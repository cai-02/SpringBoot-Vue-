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
                <div class="page-container" style="justify-content: center; position: relative; overflow: hidden;">
                    <!-- 留言弹幕显示区域 -->
                    <div class="move-area" style="width: 100%; height: 585px;">
                        <div class="item-area" ref="area1">
                        </div>
                        <div class="item-area" ref="area2">
                        </div>
                        <div class="item-area" ref="area3">
                        </div>
                        <div class="item-area" ref="area4">
                        </div>
                        <div class="item-area" ref="area5">
                        </div>
                        <div class="item-area" ref="area6">
                        </div>
                        <div class="item-area" ref="area7">
                        </div>
                        <div class="item-area" ref="area8">
                        </div>
                        <div class="item-area" ref="area9">
                        </div>
                        <div class="item-area" ref="area10">
                        </div>
                        <div class="item-area" ref="area11">
                        </div>
                        <div class="item-area" ref="area12">
                        </div>
                        <div class="item-area" ref="area13">
                        </div>
                    </div>
                    <div class="message_sub" style="position: absolute; bottom: 10px;">
                        <el-row :gutter="25">
                            <!-- 编辑留言 -->
                            <el-col :span="20">
                                <el-input placeholder="留下足迹" v-model="message">
                                </el-input>
                            </el-col>
                            <!-- 发送留言 -->
                            <el-col :span="2">
                                <el-button class="me-but" plain @click="subLiuyan"
                                    style="border-radius: 50px; margin-left: -20px; background: rgb(0, 0 , 0, 0.3); color: white;">发射</el-button>
                            </el-col>
                        </el-row>
                    </div>
                </div>
            </div>
        </el-main>
    </el-container>
</template>

<script>
import Header from '../components/header.vue'
import Vue from 'vue'
import Cookies from 'js-cookie'

export default Vue.extend({
    components: {
        "p-header": Header,
    },
    data() {
        return {
            userId: 0,
            message: "",
            headImg: "",
            liuyan: {
                liuyanContent: "",
                userId: 0,
            },
            liuyanitem: [],
            liuyanitemNew: [],
            usedIndices: [],
        };
    },
    mounted() {
    },
    created() {
        // this.userId = JSON.parse(Cookies.get("userId"));   //获取用户id
        // this.cate.userId = this.userId;
        this.getAllLiuyan()  //加载留言
        this.liuyan.userId = JSON.parse(Cookies.get("userId"));   //获取用户id
        if (JSON.parse(Cookies.get("user")) != null && Cookies.get("headimage") != null) {
            this.headImg = Cookies.get("headimage");
        } else {
            return;
        }
    },
    methods: {
        // 获取弹幕
        async getAllLiuyan() {
            const { data: res } = await this.$http.get('getAllLiuyan');
            this.liuyanitem = res.data;
            // 获取弹幕显示区总数
            const itemAreas = Array.from(document.querySelectorAll('.item-area'));
            // 定义每批添加弹幕的数量和延迟时间
            const danmuCountPerBatch = 6;
            const batchDelay = 3000; // 3秒延迟
            let danmuCount = 0;
            //let usedIndices = []; // 记录已使用的区域索引
            // 定义添加弹幕的函数
            const addDanmuBatch = () => {
                // 迭代每批弹幕
                this.liuyanitem.slice(danmuCount, danmuCount + danmuCountPerBatch).forEach(liuyanItem => {
                    // 随机选择一个不重复的区域
                    let randomIndex;
                    // 避免重复选择已经使用的索引
                    do {
                        randomIndex = Math.floor(Math.random() * itemAreas.length);
                    } while (this.usedIndices.includes(randomIndex));
                    this.usedIndices.push(randomIndex); // 记录已使用的索引
                    const selectedArea = itemAreas[randomIndex];
                    // 创建弹幕元素
                    const moveItemDiv = document.createElement('div');
                    moveItemDiv.classList.add('move-item', 'move-right-to-left');
                    moveItemDiv.innerHTML = `
                        <img class="headImage" width="35px" height="35px"/>
                        <div class="move-text">
                        <span>${liuyanItem.liuyanContent}</span>
                        </div>`;
                    const imgElement = moveItemDiv.querySelector('.headImage');
                    imgElement.src = liuyanItem.headImage;
                    selectedArea.appendChild(moveItemDiv);
                });
                danmuCount += danmuCountPerBatch;
                // 如果还有剩余弹幕，则设置延迟并继续添加下一批弹幕
                if (danmuCount < this.liuyanitem.length) {
                    setTimeout(addDanmuBatch, batchDelay);
                }
                // 重置已使用的索引数组，以便下一批弹幕
                this.usedIndices = [];
            };
            // 初始添加第一批弹幕
            addDanmuBatch();
        },
        //发射弹幕留言
        async subLiuyan() {
            if (this.message === "") {
                this.$message.warning("留言不能为空!")
            } else {
                this.liuyan.liuyanContent = this.message;
                const { data: res } = await this.$http.post('addLiuyan', this.liuyan);
                if (res != "success") {
                    return this.$message.error("发射失败！");
                } else {
                    this.message = "";
                    const { data: res2 } = await this.$http.get('getNewLiuyan');
                    this.liuyanitemNew = res2.data;
                    // 获取弹幕显示区总数
                    const itemAreas = Array.from(document.querySelectorAll('.item-area'));
                    // 随机选择一个不重复的区域
                    let randomIndex;
                    // 避免重复选择已经使用的索引
                    do {
                        randomIndex = Math.floor(Math.random() * itemAreas.length);
                    } while (this.usedIndices.includes(randomIndex));
                    const selectedArea = itemAreas[randomIndex];
                    // 创建弹幕元素
                    const moveItemDiv = document.createElement('div');
                    moveItemDiv.classList.add('move-item', 'move-right-to-left');
                    moveItemDiv.style.background = 'rgb(220 94 94 / 72%)';
                    moveItemDiv.innerHTML = `
                        <img class="headImage" width="35px" height="35px"/>
                        <div class="move-text">
                        <span>${this.liuyanitemNew[0].liuyanContent}</span>
                        </div>`;
                    const imgElement = moveItemDiv.querySelector('.headImage');
                    imgElement.src = this.liuyanitemNew[0].headImage;
                    selectedArea.appendChild(moveItemDiv);
                    return this.$message.success("发射成功！");
                }
            }
        },
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

.main-1 {
    background-image: url(../assets/images/liuyan.jpg);
    background-size: cover;
    position: fixed;
    height: 100%;
    width: 100%;
    z-index: -4;
    margin: 0;
}

.main-2 {
    width: 100%;
    margin-top: 60px;
    height: 90%;
}

.page-container {
    width: 100%;
    padding: 10px;
    padding-left: 0;
    padding-right: 0;
    padding-top: 0;
    height: 100%;
    margin: 0 auto;
}

/deep/ .el-input__inner {
    background-color: rgba(0, 0, 0, 0.3) !important;
    border-radius: 50px !important;
    color: white;
}

.el-input {
    width: 300px;
}

.me-but:hover {
    color: skyblue !important;
}

.item-area {
    height: 35px;
    margin: 10px 0 10px 0;
    //background-color: skyblue;
}

/deep/ .move-right-to-left {
    animation: moveRightToLeft 16s linear 1;
    animation-fill-mode: forwards; /* 保持最后一帧的状态 */
    position: absolute;
    right: -100px;
}

@keyframes moveRightToLeft {
    from {
        transform: translateX(100%);
        /* 初始位置在右边界外 */
    }
    to {
        transform: translateX(-110vw);
        /* 最终位置在左边界外 */
    }
}

/* 手机端样式 */
@media screen and (max-width: 767px) {}
</style>