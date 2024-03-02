<template>
    <el-container>
        <!-- 头部引用 -->
        <p-header></p-header>
        <!-- 内容 -->
        <div class="back"></div>
        <el-main style="padding: 0; overflow: hidden;">
            <div style="width: 100%; text-align: center;">
                <span class="picFont"
                    style="font-size: 40px; color: rgb(78 85 87); margin-top: 90px; margin-bottom: 35px; display: block;">我的相册</span>
            </div>
            <div class="main-pictur">
                <div>
                    <el-card class="el-card-two carWid"
                        style="min-height: 700px; width: 75%; margin: 0 auto 30px auto; position: relative;">
                        <el-upload
                            style="width: 150px; float: left; margin-top: 10px; margin-left: 10px; margin-right: 10px;"
                            class="avatar-uploader upWid" action="http://localhost:9000/files/uploadAlbum"
                            :show-file-list="false" :on-success="handleAvatarSuccess" :before-upload="beforeAvatarUpload"
                            :multiple="true">
                            <img v-if="false" class="avatar">
                            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                        </el-upload>
                        <div class="demo-image__lazy" style="text-align: left;">
                            <div v-for="url in imageUrl" :key="url" class="image-container">
                                <el-image class="albumPhone zoom-effec" :src="url" :preview-src-list="imageUrl"
                                    style="max-width: 200px; max-height: 200px; margin: 10px;" lazy>
                                </el-image>
                                <el-checkbox v-if="showCheckboxes" v-model="checkedItems" :label="url"
                                    @change="handleCheckboxChange" class="card-checkbox"></el-checkbox>
                            </div>
                        </div>
                        <div>
                            <el-popover placement="top" trigger="hover">
                                <el-button type="warning" size="mini" plain @click="toggleCheckboxes">多选</el-button><br />
                                <div slot="reference" class="el-icon-more"
                                    style="color: red; position: absolute; top: 5px; right: 15px; font-size: 25px;">
                                </div>
                            </el-popover>
                        </div>
                        <div>
                            <el-button class="duox1" v-if="showDelete" type="danger" size="mini" @click="deletePictur()"
                                style="position: fixed; bottom: 60px; right: 15px;">删除</el-button>
                            <el-button class="duox2" v-if="showDelete" type="success" size="mini" plain @click="closeCheck"
                                style="position: fixed; bottom: 20px; right: 15px;">取消</el-button>
                        </div>
                    </el-card>
                </div>
            </div>
            <!-- 底部 -->
            <el-footer>Copyright © 2023 <span>violet蔡</span> All rights reserved</el-footer>
        </el-main>
    </el-container>
</template>
  
<script>
import { startSakura, stopp, staticx } from "@/assets/js/sakura"
import Vue from 'vue'
import Header from '../components/header.vue'
import Cookies from 'js-cookie'

export default Vue.extend({
    components: {
        "p-header": Header,
    },
    data() {
        return {
            userId: 0,
            imageUrl: [],  //图片地址集合
            showCheckboxes: false,   //多选框按钮显示
            showDelete: false,   //删除按钮显示
            checkedItems: [] // 用于存储选中复选框对应的项的ID
        };
    },
    async created() {
        this.userId = JSON.parse(Cookies.get("userId"));   //获取用户id
        const { data: rest } = await this.$http.post(`getPictur?userId=${this.userId}`);
        if (rest.data.length !== 0) {
            this.imageUrl = rest.data.map(element => element.picturUrl);
        }
    },
    methods: {
        //图片上传设置
        beforeAvatarUpload(file) {
            const isJPG = file.type === 'image/jpeg' || file.type === 'image/png';
            const isPNG = file.type === 'image/png';
            const isLt2M = file.size / 1024 / 1024 < 5;

            if (!isJPG && !isPNG) {
                this.$message.error('上传封面只能是 JPG 或 PNG 格式!');
            }
            if (!isLt2M) {
                this.$message.error('上传封面大小不能超过 5MB!');
            }
            return isJPG && isLt2M;
        },
        //封面上传成功
        async handleAvatarSuccess(response) {
            this.$message.success('上传成功!');
            const { data: rest } = await this.$http.post(`addPictur?picturUrl=${response}&userId=${this.userId}`);
            this.imageUrl = rest;
        },
        //多选显示
        toggleCheckboxes() {
            this.showCheckboxes = !this.showCheckboxes;
            this.showDelete = !this.showDelete;
            if (this.showCheckboxes === true) {
                this.checkedItems = [];
            }
        },
        //删除图片
        deletePictur() {
            if (this.checkedItems.length === 0) {
                this.$message.warning("未选中任何图片！")
            } else {
                this.$confirm('确定要删除该图片吗？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(async () => {
                    const { data: res } = await this.$http.delete("deletePictur", { data: this.checkedItems });
                    if (res == "success") {
                        this.$message.success("删除成功！")
                        //重载
                        const { data: rest } = await this.$http.post(`getPictur?userId=${this.userId}`);
                        if (rest.data.length !== 0) {
                            this.imageUrl = rest.data.map(element => element.picturUrl);
                        }

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
    //监听路由变化，刷新页面数据
    watch: {
        '$route'(to, from) {

        }
    },
    mounted() {

    },

})
</script>

<style style="less" scoped>
.back {
    width: 100%;
    height: 100%;
    position: fixed;
    background-image: url(../assets/images/albumBackground.jpg);
    background-size: cover;
    z-index: -5;
}

.avatar-uploader /deep/ .el-upload {
    border: 1px dashed #5c5c5c;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
}

.avatar-uploader /deep/ .el-upload:hover {
    border-color: #409EFF;
}

.avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 145px;
    height: 145px;
    line-height: 145px;
    text-align: center;
}

.avatar {
    width: 178px;
    height: 178px;
    display: block;
}

.avatar-uploader[data-v-17dc7ad7] .el-upload {
    width: 150px;
    height: 150px;
}

.zoom-effect {
    transition: transform 0.5s ease;
}

.zoom-effec:hover {
    transform: scale(1.12);
}

.image-container {
    position: relative;
    display: inline-block;
}

.card-checkbox {
    position: absolute;
    top: 10px;
    left: 13px;
}

/* 隐藏label内容 */
.card-checkbox /deep/ .el-checkbox__label {
    visibility: hidden;
}

/* 手机端样式 */
@media screen and (max-width: 767px) {
    .avatar-uploader-icon{
        width: 50px !important;
        height: 50px !important;
        line-height: 50px !important;
    }

    .demo-image__lazy .albumPhone {
        max-width: 85px !important;
        max-height: 75px !important;
        margin: 5px !important;
    }

    .upWid {
        width: 50px !important;
        margin-left: 5px !important;
        margin-right: 8px !important;
        margin-top: 4px !important;
    }

    .carWid {
        width: 95% !important;
    }

    .picFont {
        font-size: 30px !important;
    }

    .duox1 {
        position: fixed !important;
        bottom: 10px !important;
        right: 15px !important;
        top: initial !important
    }

    .duox2 {
        position: fixed !important;
        bottom: 10px !important;
        left: 5px !important;
        top: initial !important;
        right: initial !important;
    }

    .card-checkbox {
        top: 5px;
        left: 8px;
    }

}
</style>