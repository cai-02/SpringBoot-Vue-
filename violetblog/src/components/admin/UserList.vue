<template>
    <div>
        <!-- 面包屑导航 -->
        <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ path: '/message' }">我的后台</el-breadcrumb-item>
            <el-breadcrumb-item>数据管理</el-breadcrumb-item>
            <el-breadcrumb-item>用户列表</el-breadcrumb-item>
        </el-breadcrumb>
        <!-- 用户列表主体区域 -->
        <el-card>
            <el-row :gutter="25">
                <!-- 搜索区域 -->
                <el-col :span="10">
                    <el-input placeholder="请输入用户名" v-model="queryInfo.query" clearable @clear="getUserList">
                        <el-button slot="append" icon="el-icon-search" @click="getUserList"></el-button>
                    </el-input>
                </el-col>
                <!-- 搜索按钮 -->
                <el-col :span="4">
                    <el-button type="primary" @click="addDialogVisible = true">添加用户</el-button>
                </el-col>
            </el-row>

            <!-- 用户列表部分 stripe 隔行变色-->
            <el-table :data="userList" :row-style="{ height: '60px' }" height="473px" stripe>
                <el-table-column type="index"></el-table-column> <!-- 索引列 -->
                <el-table-column label="用户名" prop="username"></el-table-column>
                <el-table-column label="邮箱" prop="email"></el-table-column>
                <el-table-column label="角色" prop="role"></el-table-column>
                <el-table-column label="状态" width="120px" prop="state">
                    <!-- 作用域插槽 -->
                    <template slot-scope="scope">
                        <!-- {{scope.row}} 每一行封存的数据 -->
                        <el-switch v-model="scope.row.state" @change="userStateChanged(scope.row)"></el-switch>
                    </template>
                </el-table-column>
                <el-table-column label="头像" width="100px">
                    <template slot-scope="scope">
                        <img style="cursor: pointer;" class="zoom-effect" :src="scope.row.headimage" width="100%"
                            height="100%">
                    </template>
                </el-table-column>
                <el-table-column label="操作" width="200px">
                    <template slot-scope="scope">
                        <!-- 修改 -->
                        <el-tooltip type="dark" content="修改信息" placement="top-start" :enterable="false">
                            <el-button type="primary" icon="el-icon-edit" size="mini"
                                @click="showEditDialog(scope.row.userId)"></el-button>
                        </el-tooltip>
                        <!-- 删除 -->
                        <el-tooltip type="dark" content="删除" placement="top-start" :enterable="false">
                            <el-button type="danger" icon="el-icon-delete" size="mini"
                                @click="deleteUser(scope.row.userId)"></el-button>
                        </el-tooltip>
                        <!-- 权限 -->
                        <el-tooltip type="dark" content="分配权限" placement="top-start" :enterable="false">
                            <!-- 文字提示 enterable隐藏 -->
                            <el-button type="warning" icon="el-icon-setting" size="mini"></el-button>
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
            <el-form :model="registForm" :rules="addFormRules" ref="addFormRef" label-width="100px">
                <!-- 用户名 -->
                <el-form-item label="用户名" prop="username">
                    <el-input placeholder="请输入用户名" v-model="registForm.username" prefix-icon="el-icon-user"></el-input>
                </el-form-item>
                <!-- 密码 -->
                <el-form-item label="密码" prop="password">
                    <el-input placeholder="请输入密码" v-model="registForm.password" prefix-icon="el-icon-lock"
                        type="password"></el-input>
                </el-form-item>
                <el-form-item label="确认密码" prop="checkPassword">
                    <el-input placeholder="请确认密码" v-model="registForm.checkPassword" prefix-icon="el-icon-lock"
                        type="password"></el-input>
                </el-form-item>
                <!-- 邮箱 -->
                <el-form-item label="邮箱" prop="email">
                    <el-input placeholder="请输入邮箱" v-model="registForm.email" prefix-icon="el-icon-message"></el-input>
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
export default {
    created() {
        this.getUserList();
    },
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
            //查询信息封装的实体
            queryInfo: {
                query: "",      //查询信息
                pageNum: 1,       //当前页
                pageSize: 10,      //每页最大数
            },
            userList: [],  //用户列表
            total: 0,     //总记录数
            addDialogVisible: false,     //对话框状态
            //添加表单信息
            registForm: {
                username: "",
                password: "",
                checkPassword: "",
                email: "",
            },
            //修改用户信息
            editForm: {},
            //显示隐藏用户栏
            editDialogVisible: false,
            //添加表单验证
            addFormRules: {
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
                    { required: true, validator: validatePass2, trigger: 'blur' }
                ],
                email: [
                    { required: true, message: '邮箱为必填项', trigger: 'blur' },  //必填项验证
                ],
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
        //获取所有用户
        async getUserList() {
            const { data: res } = await this.$http.get("alluser", { params: this.queryInfo });
            this.userList = res.data;
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
        //修改用户状态
        async userStateChanged(userInfo) {
            const { data: res } = await this.$http.put(`userstate?id=${userInfo.userId}&state=${userInfo.state}`);
            if (res != "success") {
                userInfo.id = !userInfo.id;
                return this.$message.error("操作失败！");
            }
            this.$message.success("操作成功！");
        },
        //监听添加用户操作，看是否关闭
        addDialogClosed() {
            this.$refs.addFormRef.resetFields();
        },
        async addUser() {
            //验证校验规则
            this.$refs.addFormRef.validate(async valid => {
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
                    this.$refs.addFormRef.resetFields();  //清空表单
                    this.addDialogVisible = false;     //关闭对话框
                    this.getUserList();     //刷新数据
                }
            })
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
    max-height: 3.6em;
    /* 2行文本的高度，每行大约1.8em */
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: normal;
    /* 允许文字换行 */
    display: -webkit-box;
    -webkit-line-clamp: 2;
    /* 限制文本显示的行数 */
    -webkit-box-orient: vertical;
}
/deep/ .el-table__body-wrapper{
    overflow-y: auto;
}
</style>