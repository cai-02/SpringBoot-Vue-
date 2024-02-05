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
                    <div class="cateFont" style="font-size: 40px; font-weight: bold; color: rgb(255, 209, 125);">
                        <span>类别</span>
                    </div>
                </div>
                <div class="page-container" style="justify-content: center; position: relative;">
                    <el-card class="overf"
                        style="width: 80%; background-color: rgb(255, 255, 255, 0.5); border-radius: 25px; padding: 0;">
                        <el-card v-for="(item, index) in category" :key="index" class="cate el-card-two"
                            :style="{ 'background-color': getRandomColor(), height: '80px', cursor: 'pointer' }"
                            style="max-width: 250px; border-radius: 50px; position: relative; border-top-left-radius: 0px;">
                            <el-checkbox v-if="showCheckboxes && item.categoryName != '默认'" v-model="checkedItems"
                                :label="item.categoryId" @change="handleCheckboxChange" class="card-checkbox"
                                style="position: absolute; top: 5px; left: 5px;"></el-checkbox>
                            <div class="cateNamee" @click="toCategory(item.categoryId)"
                                style="padding: 19px 35px; font-size: 25px; font-weight: bold; height: 50px; color: white; text-align: center; margin-top: 5px; white-space: normal; word-break: break-all; display: -webkit-box; -webkit-line-clamp: 1; -webkit-box-orient: vertical; overflow: hidden;">
                                <span class="category-name">{{ item.categoryName }}</span>
                            </div>
                        </el-card>
                    </el-card>
                    <el-popover placement="top" trigger="hover">
                        <el-button type="warning" size="mini" plain @click="toggleCheckboxes">多选</el-button><br />
                        <div slot="reference" class="el-icon-more"
                            style="color: red; margin-left: -38px; margin-top: 5px; font-size: 25px;">
                        </div>
                    </el-popover>
                    <el-popover placement="top" trigger="hover" content="添加">
                        <div class="tianjia" slot="reference" @click="addDialogVisible = true"
                            style="background: #39c5bb; width: 65px; color: white;
                        font-size: 19px; height: 65px; border-radius: 50%; position: absolute; right: 76px; cursor: pointer;">
                            <span class="el-icon-plus"
                                style="display: block; color: #ffffff; font-size: 35px; left: 15.4px; top: 13.2px; position: absolute;"></span>
                        </div>
                    </el-popover>
                    <div>
                        <el-button class="duox1" v-if="showDelete" type="danger" size="mini" @click="deleteCate()"
                            style="position: absolute; top: 100px; right: 80px;">删除</el-button>
                        <el-button class="duox2" v-if="showDelete" type="success" size="mini" plain @click="closeCheck"
                            style="position: absolute; top: 139px; right: 80px;">取消</el-button>
                    </div>
                    <!-- 新增类别区域 -->
                    <div>
                        <el-dialog class="wwid" title="添加分类" :visible.sync="addDialogVisible" @close="addDialogClosed()" width="35%">
                            <el-form :model="cate" label-width="65px" :rules="addFormRules" ref="cate">
                                <el-form-item label="类名" prop="categoryName">
                                    <el-input placeholder="请输入分类名" v-model="cate.categoryName"></el-input>
                                </el-form-item>
                            </el-form>
                            <span slot="footer" class="dialog-footer">
                                <el-button @click="addDialogVisible = false">取消</el-button>
                                <el-button type="primary" @click="addCate()">确定</el-button>
                            </span>
                        </el-dialog>
                    </div>
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
import Cookies from 'js-cookie'

export default Vue.extend({
    components: {
        "p-header": Header,
    },
    data() {
        return {
            category: [],
            userId: 0,
            addDialogVisible: false,     //对话框状态
            addFormRules: {
                categoryName: [
                    { required: true, message: '请输入分类名', trigger: 'blur' },
                    { min: 1, max: 12, message: '长度在 1 到 12 个字符', trigger: 'blur' }
                ],
            },
            cate: {
                categoryName: "",
                userId: 0,
            },
            showCheckboxes: false,   //多选框按钮显示
            showDelete: false,   //删除按钮显示
            checkedItems: [] // 用于存储选中复选框对应的项的ID
        };
    },
    created() {
        this.userId = JSON.parse(Cookies.get("userId"));   //获取用户id
        this.cate.userId = this.userId;
        this.loadCategory()  //加载类别
    },
    methods: {
        //获取类型
        async loadCategory() {
            const { data: res } = await this.$http.get(`getCategory?userId=${this.userId}`);
            this.category = res.data;
            //console.log(this.category)
        },
        //去到分类
        toCategory(id) {
            const herf = window.location.href.split("/");
            if (herf[herf.length - 1] == id) {
                location.reload();   //路由重复，刷新当前页
            } else {
                this.$router.push({ path: '/category/' + id });
            }
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
        //多选框及删除按钮显示隐藏
        toggleCheckboxes() {
            this.showCheckboxes = !this.showCheckboxes;
            this.showDelete = !this.showDelete;
            if (this.showCheckboxes === true) {
                this.checkedItems = [];
            }
        },
        //删除分类
        deleteCate() {
            if (this.checkedItems.length === 0) {
                this.$message.warning("未选中任何分类！")
            } else {
                this.$confirm('确定要删除该分类吗（笔记将会移入默认）？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(async () => {
                    const { data: res } = await this.$http.delete(`deleteCate/${this.checkedItems.join(',')}/${this.userId}`);
                    if (res == "success") {
                        this.$message.success("删除成功！")
                        this.loadCategory();
                        this.showCheckboxes = !this.showCheckboxes;
                        this.showDelete = !this.showDelete;
                    } else {
                        this.$message.error("删除失败！")
                        this.showCheckboxes = !this.showCheckboxes;
                        this.showDelete = !this.showDelete;
                    }
                }).catch(() => {
                    // 用户点击了取消按钮，取消删除操作
                });
            }
        },
        //关闭多选
        closeCheck() {
            this.showCheckboxes = false;
            this.showDelete = false;
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
        handleCheckboxChange() {
            //console.log(JSON.stringify(this.checkedItems))
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

/deep/ .el-card__body,
.el-main {
    padding: 0;
}

/deep/ .el-checkbox__label {
    display: none;
}

/* 手机端样式 */
@media screen and (max-width: 767px) {
    .tianjia {
        right: 8px !important;
        top: -50px !important
    }
    .duox1{
        top: 60px !important;
        right: 9px !important;
    }
    .duox2{
        top: 100px !important;
        right: 9px !important;
    }
    .overf {
        overflow: auto !important;
        width: 95% !important;
    }
    /deep/ .wwid .el-dialog{
        width: 75% !important;
    }
    .page-container{
        padding-left: 0 !important;
        padding-right: 0 !important;
    }
    .cateFont{
        font-size: 30px !important;
    }
    .cate{
        max-width: 170px !important;
        height: 60px !important;
    }
    .cateNamee{
        padding: 19px 20px !important;
        margin-top: 0px !important;
        font-size: 19px !important;
        height: 40px !important;        ;
    }
}
</style>