<template>
    <div>
        <!-- 面包屑导航 -->
        <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ path: '/message' }">我的后台</el-breadcrumb-item>
            <el-breadcrumb-item>个人中心</el-breadcrumb-item>
            <el-breadcrumb-item>我的文件</el-breadcrumb-item>
        </el-breadcrumb>
        <!-- 用户列表主体区域 -->
        <el-card>
            <el-row :gutter="25">
                <!-- 搜索区域 -->
                <el-col :span="10">
                    <el-input placeholder="请输入文件名" v-model="fileName" clearable @clear="getFileList()">
                        <el-button slot="append" icon="el-icon-search" @click="getFileList()"></el-button>
                    </el-input>
                </el-col>
                <!-- 上传按钮 -->
                <el-col :span="4">
                    <el-upload class="avatar-uploader upWid" action="http://localhost:9000/files/uploadFiles"
                        :show-file-list="false" :on-success="handleAvatarSuccess" :before-upload="beforeAvatarUpload"
                        :multiple="true">
                        <el-button type="primary">添加文件</el-button>
                    </el-upload>
                </el-col>
                <el-table :data="fileTableData"
                    style="width: 98%; margin: 12.5px; margin-top: 15px; margin-bottom: 0; display: inline-block;"
                    :row-style="{ height: '40px' }" height="510px">
                    <el-table-column prop="fileName" label="文件名" style="color: skyblue;">
                    </el-table-column>
                    <el-table-column prop="fileSize" label="大小" width="100">
                    </el-table-column>
                    <el-table-column prop="fileTime" label="上传时间" width="200">
                    </el-table-column>
                    <el-table-column label="操作" width="130px">
                        <template slot-scope="scope">
                            <!-- 下载 -->
                            <el-tooltip type="dark" content="下载" placement="top-start" :enterable="false">
                                <el-button type="primary" icon="el-icon-download" size="mini"
                                    @click="downLoadFile(scope.row.fileUrl)"></el-button>
                            </el-tooltip>
                            <!-- 删除 -->
                            <el-tooltip type="dark" content="删除" placement="top-start" :enterable="false">
                                <el-button type="danger" icon="el-icon-delete" size="mini"
                                    @click="deleteFiles(scope.row.fileUrl)"></el-button>
                            </el-tooltip>
                            <!-- 改名 -->
                            <!-- <el-tooltip type="dark" content="改名" placement="top-start" :enterable="false">
                                文字提示 enterable隐藏
                                <el-button type="warning" icon="el-icon-edit" size="mini"></el-button>
                            </el-tooltip> -->
                        </template>
                    </el-table-column>
                </el-table>
            </el-row>

        </el-card>
    </div>
</template>

<script>
import axios from 'axios';
import Cookies from 'js-cookie'

export default {
    data() {
        return {
            //查询信息封装的实体
            queryInfo: {
                query: "",      //查询信息
                pageNum: 1,       //当前页
                pageSize: 10,      //每页最大数
            },
            files: {
                fileId: 0,
                fileName: "",
                fileSize: "",
                fileTime: "",
                userId: 0,
                fileUrl: "",
            },
            userId: 0,
            fileName: "",
            fileUrl: [],
            fileTableData: []
        }
    },
    async created() {
        this.userId = Cookies.get("userId");   //获取用户id
        this.getFileList();
    },
    methods: {
        //获取所有文件信息
        async getFileList() {
            const { data: rest2 } = await this.$http.get(`getFiles?userId=${this.userId}&fileName=${this.fileName}`);
            this.fileTableData = rest2.data;
        },
        //文件上传设置
        beforeAvatarUpload(file) {
            //const fileSizeD = (file.size / 1024 / 1024).toFixed(2) + "MB"  //获取文件大小
            const isLt2M = file.size / 1024 / 1024 < 10;
            if (!isLt2M) {
                this.$message.error('上传文件大小不能超过 10MB!');
            }
            return isLt2M;
        },
        //文件上传成功
        async handleAvatarSuccess(response) {
            this.files.userId = this.userId;
            this.files.fileName = decodeURIComponent(response.fileNames);
            this.files.fileUrl = response.urls[0];
            this.files.fileSize = response.fileSize[0];
            const { data: rest } = await this.$http.post('addFiles', this.files);
            this.fileUrl = rest;
            //console.log(this.fileUrl);
            this.$message.success('上传成功!');
            this.getFileList();
        },
        //删除文件
        deleteFiles(fileUrl) {
            this.$confirm('确定要删除该文件吗？', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(async () => {
                const { data: res } = await this.$http.delete(`deleteFile?fileUrl=${fileUrl}`);
                if (res == "success") {
                    this.$message.success("删除成功！")
                    //重载
                    this.getFileList();
                } else {
                    this.$message.error("删除失败！")
                }
            }).catch(() => {
                // 用户点击了取消按钮，取消删除操作
            });
        },
        //下载文件
        async downLoadFile(fileUrl){
            // const url = fileUrl.split("/");
            // const flag = url[url.length - 1];
            window.location.href = fileUrl;
            this.$message.success("开始下载！")
        },
    },
}
</script>

<style lang="less" scoped>
.el-breadcrumb {
    margin-bottom: 15px;
    font-size: 17px;
}
</style>
