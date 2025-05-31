<template>
  <div>
    
		<div class="card" style="margin-bottom: 5px">
			<el-input v-model="data.search.userName" prefix-icon="Search" style="width: 240px; margin-right: 10px; margin-bottom: 3px" placeholder="请输入用户名称"></el-input>
			<el-input v-model="data.search.remName" prefix-icon="Search" style="width: 240px; margin-right: 10px; margin-bottom: 3px" placeholder="请输入任务名称"></el-input>
			<el-input v-model="data.search.remindTime" type="date" style="width: 240px; margin-right: 10px; margin-bottom: 3px" placeholder="请输入提醒时间"></el-input>
			<el-button type="info" plain @click="load">查询</el-button>
			<el-button type="warning" plain style="margin-right: 10px" @click="reset">重置</el-button>

			<el-upload v-if="data.user.role === 'admin'" :action="'http://localhost:9090/reminder/upload?token=' + data.user.token" style="display: inline-block" :show-file-list="false" :on-success="impSuccessUpload">
				<el-button style="margin-right: 10px" type="success">批量导入</el-button>
			</el-upload>

			<el-button style="margin-right: 10px" type="warning" v-if="data.user.role === 'admin'" @click="exp()">批量导出</el-button>
		</div>
    <div v-if="data.user.role === 'admin'" class="card" style="margin-bottom: 5px">
			<el-button v-if="data.user.role === 'admin'" type="primary" plain @click="handleAdd">新增</el-button>
			<el-button  type="danger" plain @click="delBatch">批量删除</el-button>
		</div>

    <div class="card" style="margin-bottom: 5px;">
      <el-table :data="data.tableData" strip @selection-change="handleSelectionChange">
				<el-table-column  type="selection" width="55" align="center"></el-table-column>
				<el-table-column label="用户名称" prop="userName"></el-table-column>
				<el-table-column label="任务名称" prop="remName"></el-table-column>
				<el-table-column label="任务状态" prop="remStatus"></el-table-column>
				<el-table-column label="截止时间" prop="remindTime"></el-table-column>
				<el-table-column label="任务进度" prop="progress">
					<template v-slot="scope">
						<el-progress :percentage="scope.row.progress || 0"></el-progress>
					</template>
				</el-table-column>
				<el-table-column label="提醒内容" prop="remindText"></el-table-column>
				<el-table-column label="提醒方式" prop="method"></el-table-column>
        <el-table-column label="操作" align="center" width="200" fixed="right">
          <template v-slot="scope">
						<el-button v-if="data.user.role === 'admin'" type="primary" plain @click="handleEdit(scope.row)">编辑</el-button>
						<el-button type="danger" plain @click="del(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="card" v-if="data.total">
      <el-pagination @current-change="load" background layout="total, prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total"/>
    </div>

    <el-dialog title="请填写信息" v-model="data.formVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
      <el-form :model="data.form" label-width="80px"  style="padding: 20px 30px">
				<el-form-item v-if="data.user.role === 'admin'" label="用户名称" prop="userName">
					<el-select style="width: 100%" v-model="data.form.userId">
						<el-option v-for="item in data.userData" :value="item.id" :label="item.name" :key="item.id"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="任务名称" prop="remName">
					<el-select style="width: 100%" v-model="data.form.taskId" @change="handleTaskChange">
						<el-option v-for="item in data.taskData" :value="item.id" :label="item.title" :key="item.id"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="任务状态" prop="remStatus">
					<el-input v-model="data.form.remStatus" disabled></el-input>
				</el-form-item>
				<el-form-item label="截止时间" prop="remindTime">
					<el-input v-model="data.form.remindTime" disabled></el-input>
				</el-form-item>
				<el-form-item label="任务进度" prop="progress">
					<el-progress :percentage="data.form.progress || 0"></el-progress>
				</el-form-item>
				<el-form-item label="提醒内容" prop="remindText">
					<el-input type="textarea" v-model="data.form.remindText" placeholder="请输入提醒内容"></el-input>
				</el-form-item>
				<el-form-item label="提醒方式" prop="method">
					<el-checkbox-group v-model="data.form.methodList">
						<el-checkbox value="邮件" label="邮件"></el-checkbox>
						<el-checkbox value="短信" label="短信"></el-checkbox>
						<el-checkbox value="应用内通知" label="应用内通知"></el-checkbox>
					</el-checkbox-group>
				</el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="data.formVisible = false">取消</el-button>
          <el-button type="primary" @click="save">保存</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive } from "vue"
import request from "@/utils/request";
import {ElMessage, ElMessageBox} from "element-plus";
const baseApi = 'reminder'

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  tableData: [],
  total: 0,
  pageNum: 1,  // 当前的页码
  pageSize: 5,  // 每页的个数
  formVisible: false,
  form: {},
  search: {},
	ids: [],
	taskData: [],
	userData: [],
})



// 加载表格数据
const load = () => {

  data.search.pageNum = data.pageNum
  data.search.pageSize = data.pageSize
  request.get(baseApi + '/selectPage', {
    params: data.search
  }).then(res => {
    data.tableData = res.data?.list || []
    data.total = res.data?.total
  })
}

// 打开新增弹窗
const handleAdd = () => {
  data.form = {}
  data.formVisible = true
}

// 处理任务选择变化
const handleTaskChange = (taskId) => {
  const selectedTask = data.taskData.find(task => task.id === taskId);
  if (selectedTask) {
    data.form.remStatus = selectedTask.status;
    data.form.remindTime = selectedTask.dueDate;
    data.form.progress = selectedTask.progress || 0;
  }
}

// 打开编辑弹窗
const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row));
  // 确保进度值正确
  if (data.form.progress === undefined || data.form.progress === null) {
    data.form.progress = 0;
  }
  data.formVisible = true;
}

// 新增
const add = () => {
  request.post(baseApi + '/add', data.form).then(res => {
    if (res.code === '200') {
      data.formVisible = false
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

// 更新
const update = () => {
  request.put(baseApi + '/update', data.form).then(res => {
    if (res.code === '200') {
      data.formVisible = false
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

// 删除
const del = (id) => {
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗?', '删除确认', { type: 'warning' }).then(res => {
    request.delete(baseApi + '/delete/' + id).then(res => {
      if (res.code === '200') {
        ElMessage.success('删除成功')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {
    console.error(err)
  })
}

const save = () => {
	if (data.user.role === 'user') {
		data.form.userId = data.user.id
	}

	let progress = data.form.progress
	let progressRegNum = /^[0-9]*$/
	if (progress && progress !== '') {
		if (!progressRegNum.test(progress)) {
			ElMessage.error("请输入正确的数字")
			return false
		}
	}

  data.form.id ? update() : add()
}

const reset = () => {
  data.search = {}
  load()
}


// 批量删除表格数据
const delBatch = () => {
	if (data.ids.length === 0) {
		ElMessage.warning('请选择数据');
		return;
	}
	ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗?', '删除确认', { type: 'warning' }).then(res => {
		request.delete(baseApi + "/delete/batch", {data: data.ids}).then(res => {
			if (res.code === '200') {
				ElMessage.success('批量删除成功');
				load();
			} else {
				ElMessage.error(res.msg);
			}
		})
	}).catch(err => console.log(err))
}
const handleSelectionChange = (rows) => {
	data.ids = rows.map(v => v.id)
}
// 导出
const exp = () => {
	location.href = 'http://localhost:9090/reminder/export?token=' + data.user.token
}
const impSuccessUpload = () => {
	ElMessage.success('导入成功');
	load();
}
// 加载任务记录
const loadTask = () => {
	request.get("/task/selectAll").then(res => {
		if (res.code === '200') {
			data.taskData = res.data;
		} else {
			ElMessage.error(res.msg);
		}
	})
}// 加载用户
const loadUser = () => {
	request.get("/user/selectAll").then(res => {
		if (res.code === '200') {
			data.userData = res.data;
		} else {
			ElMessage.error(res.msg);
		}
	})
}

// 加载模块数据
load()
// 加载任务记录
loadTask();// 加载用户
loadUser();

</script>
