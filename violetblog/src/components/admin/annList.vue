<template>
    <div>
        <!-- 面包屑导航 -->
        <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ path: '/message' }">我的后台</el-breadcrumb-item>
            <el-breadcrumb-item>数据管理</el-breadcrumb-item>
            <el-breadcrumb-item>公告管理</el-breadcrumb-item>
        </el-breadcrumb>
        <!-- 用户列表主体区域 -->
        <el-card>
            <el-row :gutter="25">
                <!-- 添加按钮 -->
                <el-col :span="4">
                    <el-button type="primary" @click="addDialogVisible = true">新增公告</el-button>
                </el-col>
            </el-row>

            <!-- 公告列表部分 stripe 隔行变色-->
            <el-table :data="annList" :row-style="{ height: '60px' }" height="473px" stripe>
                <el-table-column type="index"></el-table-column> <!-- 索引列 -->
                <el-table-column label="内容">
                    <!-- 添加样式类名以限制行高和文本截断 -->
                    <template slot-scope="scope">
                        <div class="table-cell">
                            {{ scope.row.content }}
                        </div>
                    </template>
                </el-table-column>
                <el-table-column label="管理员编号" prop="userId" width="110px"></el-table-column>
                <el-table-column label="时间" prop="time" width="200px"></el-table-column>
                <el-table-column label="操作" width="75x">
                    <template slot-scope="scope">
                        <!-- 删除 -->
                        <el-button type="danger" icon="el-icon-delete" size="mini"
                            @click="deleteAnn(scope.row.annId)"></el-button>
                    </template>
                </el-table-column>
            </el-table>
            <!-- 分页组件 size-change 每页最大变化 current-change 当前最大变化 layout 功能组件 -->
            <div style="margin-top: 10px;">
                <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="pageNum"
                    :page-sizes="[10, 20]" :page-size="pageSize" layout="total, sizes, prev, pager, next, jumper"
                    :total="total">
                </el-pagination>
            </div>
        </el-card>
        <!-- 新增公告区域 -->
        <el-dialog title="添加用户" :visible.sync="addDialogVisible" wdith="50%" @close="addDialogClosed">
            <el-form :model="annForm" ref="addFormRef" label-width="65px">
                <!-- 用户名 -->
                <el-form-item label="公告" prop="content">
                    <el-input type="textarea" v-model="annForm.content"></el-input>
                </el-form-item>
            </el-form>
            <!-- 内容底部区域 -->
            <span slot="footer" class="dialog-footer">
                <el-button @click="addDialogVisible = false">取消</el-button>
                <el-button type="primary" @click="addAnn">确定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
import Cookies from 'js-cookie'

export default {
    created() {
        this.getAnnList();
        this.annForm.userId = Cookies.get("userId");   //获取用户id
    },
    data() {
        return {
            pageNum: 1,       //当前页
            pageSize: 10,      //每页最大数
            annList: [],  //用户列表
            total: 0,     //总记录数
            addDialogVisible: false,     //对话框状态
            //添加表单信息
            annForm: {
                content: "",
                userId: 0,
            },
            //显示隐藏用户栏
            editDialogVisible: false,
        }
    },
    methods: {
        //获取所有用户
        async getAnnList() {
            const { data: res } = await this.$http.get(`getAllAnn?pageStart=${this.pageNum}&pageSize=${this.pageSize}`);
            this.annList = res.data;
            this.total = res.number;
        },
        //最大数
        handleSizeChange(newSize) {
            this.pageSize = newSize;
            this.getAnnList();
        },
        //pageNum的触发动作
        handleCurrentChange(newSPage) {
            this.pageNum = newSPage;
            this.getAnnList();
        },
        //监听添加用户操作，看是否关闭
        addDialogClosed() {
            this.$refs.addFormRef.resetFields();
        },
        async addAnn() {
            //检测用户名唯一性
            const { data: res } = await this.$http.post('addAnn', this.annForm);
            if (res != "success") {
                return this.$message.error("添加失败！");
            } else {
                this.$refs.addFormRef.resetFields();  //清空表单
                this.addDialogVisible = false;     //关闭对话框
                this.getAnnList();     //刷新数据
                return this.$message.success("添加成功！");
            }
        },
        //根据主建删除用户
        async deleteAnn(id) {
            const comfirmResult = await this.$confirm("永久删除公告，是否继续？", "提示", {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).catch(err => err)
            if (comfirmResult != 'confirm') {   //取消
                return this.$message.info("已取消删除");
            }
            const { data: res } = await this.$http.delete("deleteAnn?annId=" + id);
            if (res != "success") {
                return this.$message.error("删除失败！");
            }
            this.$message.success("删除成功！");
            this.getAnnList();
        },
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

/deep/ .el-table__body-wrapper {
    overflow-y: auto;
}
</style>