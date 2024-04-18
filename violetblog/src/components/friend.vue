<template>
    <el-container>
        <!-- 头部引用 -->
        <p-header></p-header>
        <!-- 内容 -->
        <div class="back"></div>
        <el-main style="padding: 0; overflow: hidden;">
            <div style="width: 100%; text-align: center;">
                <span class="picFont"
                    style="font-size: 40px; color: rgb(78 85 87); margin-top: 90px; margin-bottom: 35px; display: block;">我的好友</span>
                <!-- 搜索框 -->
                <div style="margin-top: -20px; position: relative;">
                    <div>
                        <el-input placeholder="请输入用户名" v-model="username" style="width: 400px; margin-bottom: 10px;" clearable>
                            <el-button slot="append" icon="el-icon-search" style="color: black;"
                                @click="searchUser()"></el-button>
                        </el-input>
                    </div>
                    <el-dialog class="searchDio" title="相关用户" :visible.sync="centerDialogVisible" :modal="false" background="white" width="400px" height="100px" @close="cleanS()">
                        <div v-if="noneC">
                            <span style="font-size: 17px;">抱歉！没有找到该用户</span>
                        </div>
                        <div v-if="visC" style="text-align: left; font-size: 17px;">
                            <ul class="searchUl" style="list-style-type: none; padding-left: 0; margin: 0;">
                                <li v-for="(item, index) in searchUsers" :key="index">{{ item.username }}<el-button type="primary" size="mini" plain style="float: right;" @click="addFriendMes(item)">添加好友</el-button></li>
                            </ul>
                        </div>
                    </el-dialog>
                    <div class="el-icon-s-comment" @click="MesDio()" style="position: absolute; right: 9.5%; top: 45px; font-size: 40px; color: #57fdad;"></div>
                </div>
            </div>
            <div class="main-pictur">
                <div>
                    <el-card class="el-card-two carWid"
                        style="min-height: 700px; width: 75%; margin: 0 auto 30px auto; position: relative;">
                        <div class="demo-image__lazy" style="text-align: left;">
                            <div v-for="(user, index) in userList" :key="index" class="image-container"
                                style="width: 150px;">
                                <el-image class="albumPhone zoom-effec" :src="user.headimage"
                                    @click="toFriendNotes(user.userId)"
                                    style="width: 90px; height: 90px; margin: 10px 30px 10px 30px; border-radius: 50%;"
                                    lazy>
                                </el-image>
                                <div style="font-size: 20px; text-align: center; padding: 0 10px;"><span>{{ user.username }}</span></div>
                                <el-checkbox v-if="showCheckboxes" v-model="checkedItems" :label="user"
                                    @change="handleCheckboxChange" class="card-checkbox"></el-checkbox>
                            </div>
                        </div>
                        <div>
                            <el-popover placement="top" trigger="hover">
                                <el-button type="warning" size="mini" plain
                                    @click="toggleCheckboxes">多选</el-button><br />
                                <div slot="reference" class="el-icon-more"
                                    style="color: red; position: absolute; top: 5px; right: 15px; font-size: 25px;">
                                </div>
                            </el-popover>
                        </div>
                        <div>
                            <el-button class="duox1" v-if="showDelete" type="danger" size="mini" @click="deletePictur()"
                                style="position: fixed; bottom: 60px; right: 15px;">删除</el-button>
                            <el-button class="duox2" v-if="showDelete" type="success" size="mini" plain
                                @click="closeCheck" style="position: fixed; bottom: 20px; right: 15px;">取消</el-button>
                        </div>
                    </el-card>
                </div>
            </div>
            <!-- 消息框 -->
            <el-dialog class="MesDio" title="通知" :modal="true" :visible.sync="dialogVisible" width="35%">
                <div>
                    <ul class="searchUl" style="list-style-type: none; padding-left: 0; margin: 0; font-size: 17px; padding-top: 10px;">
                        <!-- 信息加载 -->
                        <li class="mesLi" v-for="(item, index) in friendAddList" :key="index" v-html="item.content" style="height: 40px;">
                            
                        </li>
                    </ul>
                </div>
            </el-dialog>
            <!-- 底部 -->
            <el-footer>Copyright © 2023 <span>violet蔡</span> All rights reserved</el-footer>
        </el-main>
    </el-container>
</template>

<script>
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
            username: "",
            username2: "",
            userList: [],  //图片地址集合
            showCheckboxes: false,   //多选框按钮显示
            showDelete: false,   //删除按钮显示
            checkedItems: [], // 用于存储选中复选框对应的项的ID
            searchUsers: [],  //用户搜索列表
            noneC: false,
            visC: false,
            //搜索框隐藏显示
            centerDialogVisible: false,
            dialogVisible: false,
            friendState: {
                userId: 0,
                username: "",
                friId: 0,
                friName: "",
                content: "",
                time: "",
                state: "",
            },
            friendAddList: [],
        };
    },
    async created() {
        this.userId = JSON.parse(Cookies.get("userId"));   //获取用户id
        this.username2 = JSON.parse(Cookies.get("user")).username;   //获取用户id
        this.loadFriend();
        this.loadFriendAdd();
    },
    methods: {
        //去到好友主页
        toFriendNotes(id) {
            this.$router.push({ path: '/friendnotes/' + id });
        },
        //获取好友列表
        async loadFriend() {
            const { data: rest } = await this.$http.get(`getFriend?id=${this.userId}`);
            if (rest.data.length !== 0) {
                this.userList = rest.data;
                //console.log(this.userList)
            }
        },
        //加载好友信息
        async loadFriendAdd() {
            const { data: res } = await this.$http.get(`getFriendMes?friId=${this.userId}`);
            this.friendAddList = res.data;
            //console.log(this.friendAddList)
        },
        //多选显示
        toggleCheckboxes() {
            this.showCheckboxes = !this.showCheckboxes;
            this.showDelete = !this.showDelete;
            if (this.showCheckboxes === true) {
                this.checkedItems = [];
            }
        },
        //搜索非好友用户
        async searchUser() {
            if (this.username === "") {
                this.$message.warning('搜索内容不能为空！');
            } else {
                const { data: res } = await this.$http.get(`getFriendExSelf?id=${this.userId}&username=${this.username}`);
                this.searchUsers = res.data;
                this.centerDialogVisible = true;
                //console.log(this.searchUsers)
                if (this.searchUsers.length === 0) {
                    this.noneC = true;
                    this.visC = false;
                } else {
                    this.visC = true;
                    this.noneC = false;
                }
            }
        },
        //搜索框关闭回调
        cleanS() {
            this.searchUsers = [];
            this.noneC = false;
            this.visC = false;
        },
        //发送添加好友信息
        async addFriendMes(item) {
            this.friendState.userId = this.userId;
            this.friendState.username = this.username2;
            this.friendState.friId = item.userId;
            this.friendState.friName = item.username;
            this.friendState.content = "<span style=\"display: block; float: left;margin-left: -10px;\"><span style=\"color: #FF5722;\">" + this.username2 + "</span>请求添加你为好友</span>" +
                            "<span class=\"meshov\" style=\"float: right; color: orange; cursor: pointer;\" onclick=\"jujue('"+ this.friendState.username + "'," + this.friendState.userId + "," + this.friendState.friId + ")\">拒绝</span>" + 
                            "<span class=\"meshov\" style=\"float: right; color: limegreen; cursor: pointer; margin-right: 5px;\" onclick=\"tongyi('"+ this.friendState.username + "'," + this.friendState.userId + "," + this.friendState.friId + ")\">同意</span";
            this.friendState.state = "待确认";
            const { data: res } = await this.$http.put("addFriendState", this.friendState);
            if (res == "success") {
                this.$message.success("请求已发送");
                this.searchUsers = [];
                this.noneC = false;
                this.visC = false;
                this.centerDialogVisible = false;
                //console.log(item);
            }
        },
        //打开消息框
        MesDio() {
            this.dialogVisible = true;
        },
        //同意请求
        async tongyi(username, userId, friId) {
            const { data: res } = await this.$http.put(`addFriendRelate?userId=${this.userId}&username=${this.username2}&friId=${userId}`);
            const { data: res2 } = await this.$http.put(`addFriendRelate?userId=${userId}&username=${username}&friId=${friId}`);
            if (res == "success") {
                this.$message.success("添加成功！")
                this.dialogVisible = false
                this.loadFriend();
                //删除通知
                const { data: res3 } = await this.$http.delete(`deleteFriendRelate?userId=${userId}&friId=${friId}`);
                //重载信息
                this.loadFriendAdd();
            }
        },
        //拒绝请求
        async jujue(username, userId, friId) {
            this.$message.warning("已拒绝！")
            this.dialogVisible = false
            const { data: res3 } = await this.$http.delete(`deleteFriendRelate?userId=${userId}&friId=${friId}`);
            //重载信息
            this.loadFriendAdd();
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
    },
    //监听路由变化，刷新页面数据
    watch: {
        '$route'(to, from) {

        }
    },
    mounted() {
        window.tongyi = this.tongyi;
        window.jujue = this.jujue;
    },

})
</script>

<style style="less" scoped>
.back {
    width: 100%;
    height: 100%;
    position: fixed;
    background-image: url(../assets/images/randomImage/6.jpg);
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

.searchUl li {
    line-height: 40px;
    margin-left: 15px;
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

.el-icon-s-comment:hover {
    color: red !important;
    cursor: pointer !important;
}

.zoom-effect {
    transition: transform 0.5s ease;
}

.zoom-effec:hover {
    cursor: pointer;
}

.MesDio /deep/ .el-dialog__body {
    padding-top: 0;
    min-height: 150px;
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

.searchDio /deep/ .el-dialog__body {
    padding: 0px 20px 20px 20px;
    max-height: 400px;
    overflow: auto;
}

/* 隐藏label内容 */
.card-checkbox /deep/ .el-checkbox__label {
    visibility: hidden;
}

/* 手机端样式 */
@media screen and (max-width: 767px) {
    .avatar-uploader-icon {
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