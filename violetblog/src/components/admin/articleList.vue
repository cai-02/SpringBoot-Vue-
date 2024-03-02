<template>
    <div>
        <!-- 面包屑导航 -->
        <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ path: '/message' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>数据管理</el-breadcrumb-item>
            <el-breadcrumb-item>文章列表</el-breadcrumb-item>
        </el-breadcrumb>
        <!-- 用户列表主体区域 -->
        <el-card>
            <el-row :gutter="25">
                <!-- 搜索区域 -->
                <el-col :span="10">
                    <el-input placeholder="请输入文章标题" v-model="queryInfo.query" clearable @clear="getUserList">
                        <el-button slot="append" icon="el-icon-search" @click="getUserList"></el-button>
                    </el-input>
                </el-col>
            </el-row>

            <!-- 用户列表部分 stripe 隔行变色-->
            <el-table :data="articleList" :row-style="{ height: '60px' }" height="473px" border stripe>
                <el-table-column type="index"></el-table-column> <!-- 索引列 -->
                <el-table-column label="标题" prop="title"></el-table-column>
                <el-table-column label="简介">
                    <!-- 添加样式类名以限制行高和文本截断 -->
                    <template slot-scope="scope">
                        <div class="table-cell">
                            {{ scope.row.introduce }}
                        </div>
                    </template>
                </el-table-column>
                <el-table-column label="作者" prop="author"></el-table-column>
                <el-table-column label="类别" prop="type"></el-table-column>
                <el-table-column label="时间" prop="time" :formatter="formatter" width="110px"></el-table-column>
                <el-table-column label="点击量" prop="heat" width="80px"></el-table-column>
                <el-table-column label="收藏" prop="likes" width="70px"></el-table-column>
                <el-table-column label="封面" width="100px">
                    <template slot-scope="scope">
                        <img style="cursor: pointer;" class="zoom-effect" :src="scope.row.cover" width="100%" height="100%">
                    </template>
                </el-table-column>
                <el-table-column label="操作" width="175px" >
                    <template slot-scope="scope">
                        <!-- 查看 -->
                        <el-tooltip type="dark" content="查看" placement="top-start" :enterable="false">
                            <!-- 文字提示 enterable隐藏 -->
                            <el-button type="success" icon="el-icon-search" size="mini"></el-button>
                        </el-tooltip>
                        <!-- 修改 -->
                        <el-tooltip type="dark" content="修改" placement="top-start" :enterable="false">
                            <el-button type="primary" icon="el-icon-edit" size="mini"
                                @click="showEditDialog(scope.row.id)"></el-button>
                        </el-tooltip>
                        <!-- 删除 -->
                        <el-tooltip type="dark" content="删除" placement="top-start" :enterable="false">
                            <el-button type="danger" icon="el-icon-delete" size="mini"
                                @click="deleteUser(scope.row.id)"></el-button>
                        </el-tooltip>
                    </template>
                </el-table-column>
            </el-table>
            <!-- 分页组件 size-change 每页最大变化 current-change 当前最大变化 layout 功能组件 -->
            <div style="margin-top: 10px;">
                <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
                    :current-page="queryInfo.pageNum" :page-sizes="[10, 20]" :page-size="queryInfo.pageSize"
                    layout="total, sizes, prev, pager, next, jumper" :total="total">
                </el-pagination>
            </div>
        </el-card>
        <!-- 新增用户区域 -->
        <el-dialog title="添加用户" :visible.sync="addDialogVisible" wdith="50%" @close="addDialogClosed">
            <el-form :model="addForm" :rules="addFormRules" ref="addFormRef" label-width="70px">
                <el-form-item label="用户名" prop="username">
                    <el-input v-model="addForm.username"></el-input>
                </el-form-item>
                <el-form-item label="密码" prop="password">
                    <el-input v-model="addForm.password"></el-input>
                </el-form-item>
                <el-form-item label="邮箱" prop="email">
                    <el-input v-model="addForm.email"></el-input>
                </el-form-item>
            </el-form>
            <!-- 内容底部区域 -->
            <span slot="footer" class="dialog-footer">
                <el-button @click="addDialogVisible = false">取消</el-button>
                <el-button type="primary" @click="addUser">确定</el-button>
            </span>
        </el-dialog>
        <!-- 修改对话框 -->
        <el-dialog title="修改用户信息" :visible.sync="editDialogVisible" wdith="50%" @close="editDialogClosed">
            <el-form :model="editForm" :rules="editFormRules" ref="editFormRef" label-width="70px">
                <el-form-item label="用户名" prop="username">
                    <el-input v-model="editForm.username" disabled></el-input>
                </el-form-item>
                <el-form-item label="邮箱" prop="email">
                    <el-input v-model="editForm.email"></el-input>
                </el-form-item>
                <el-form-item label="性  别">
                    <el-radio-group v-model="editForm.gender">
                        <el-radio value="男" label="男"></el-radio>
                        <el-radio value="女" label="女"></el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="简  介" prop="introduction">
                    <el-input type="textarea" v-model="editForm.introduction"></el-input>
                </el-form-item>
            </el-form>
            <!-- 内容底部区域 -->
            <span slot="footer" class="dialog-footer">
                <el-button @click="editDialogVisible = false">取消</el-button>
                <el-button type="primary" @click="editUserInfo">确定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
import { formatDate } from "../../main";
export default {
    created() {
        this.getUserList();
    },
    data() {
        return {
            //查询信息封装的实体
            queryInfo: {
                query: "",      //查询信息
                pageNum: 1,       //当前页
                pageSize: 10,      //每页最大数
            },
            articleList: [],  //文章列表
            total: 0,     //总记录数
            addDialogVisible: false,     //对话框状态
            //添加表单信息
            addForm: {
                username: '',
                password: '',
                email: ''
            },
            //修改文章信息
            editForm: {},
            //显示隐藏用户栏
            editDialogVisible: false,
            //添加表单验证
            addFormRules: {
                username: [
                    { required: true, message: '请输入用户名', trigger: 'blur' },
                    { min: 5, max: 12, message: '长度在 5 到 12 个字符', trigger: 'blur' }
                ],
                password: [
                    { required: true, message: '请输入密码', trigger: 'blur' },
                    { min: 6, max: 12, message: '长度在 6 到 12 个字符', trigger: 'blur' }
                ],
                email: [
                    { required: true, message: '请输入邮箱', trigger: 'blur' },
                    { min: 5, max: 16, message: '请输入正确邮箱地址', trigger: 'blur' }
                ]
            },
            //修改表单验证
            editFormRules: {
                email: [
                    { required: true, message: '请输入邮箱', trigger: 'blur' },
                    { min: 5, max: 16, message: '请输入正确邮箱地址', trigger: 'blur' }
                ]
            },
        }
    },
    methods: {
        //格式化时间
        formatter(row, column){
            return formatDate("YYYY-mm-dd HH:MM:SS", row[column.property]);
        },
        //获取所有文章
        async getUserList() {
            const { data: res } = await this.$http.get("allArticle", { params: this.queryInfo });
            this.articleList = res.data;
            console.log(this.articleList)
            this.total = res.number;
        },
        //最大数
        handleSizeChange(newSize) {
            this.queryInfo.pageSize = newSize;
            this.getUserList();
        },
        //pageNum的触发动作
        handleCurrentChange(newSPage) {
            this.queryInfo.pageNum = newSPage;
            this.getUserList();
        },
        
        //根据主建删除用户
        async deleteUser(id) {
            const comfirmResult = await this.$confirm("此操作将永久删除用户，是否继续？", "提示", {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).catch(err => err)
            if (comfirmResult != 'confirm') {   //取消
                return this.$message.info("已取消删除");
            }
            const { data: res } = await this.$http.delete("deleteuser?id=" + id);
            if (res != "success") {
                return this.$message.error("删除失败！");
            }
            this.$message.success("删除成功！");
            this.getUserList();
        },
        //显示对话框
        async showEditDialog(id) {
            const { data: res } = await this.$http.get("getupdate?id=" + id);
            this.editForm = res;   //查询出来的用户信息反填到编辑表单
            this.editDialogVisible = true;  //开启编辑对话框
        },
        editDialogClosed() {
            this.$refs.editFormRef.resetFields();   //重置信息
        },
        //确认修改
        editUserInfo() {
            this.$refs.editFormRef.validate(async valid => {
                if (!valid) return;
                //发起修改请求
                const { data: res } = await this.$http.put("edituser", this.editForm);
                if (res != "success") {
                    return this.$message.error("操作失败！");
                }
                this.$message.success("操作成功！");
                this.editDialogVisible = false;
                this.getUserList();
            })
        }
    }
}
</script>

<style lang="less" scoped>
.el-breadcrumb {
    margin-bottom: 15px;
    font-size: 17px;
}

.table-cell {
    max-height: 3.6em; /* 2行文本的高度，每行大约1.8em */
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: normal; /* 允许文字换行 */
    display: -webkit-box;
    -webkit-line-clamp: 2; /* 限制文本显示的行数 */
    -webkit-box-orient: vertical;
}
</style>