<template>
  <div>
    
		<div class="card" style="margin-bottom: 5px">
			<el-input v-model="data.search.medName" prefix-icon="Search" style="width: 240px; margin-right: 10px; margin-bottom: 3px" placeholder="请输入患者"></el-input>
			<el-input v-model="data.search.medicationName" prefix-icon="Search" style="width: 240px; margin-right: 10px; margin-bottom: 3px" placeholder="请输入药物名称"></el-input>
			<el-button type="info" plain @click="load">查询</el-button>
			<el-button type="warning" plain style="margin-right: 10px" @click="reset">重置</el-button>

			<el-upload v-if="data.user.role === 'admin'" :action="'http://localhost:9090/medicationReminder/upload?token=' + data.user.token" style="display: inline-block" :show-file-list="false" :on-success="impSuccessUpload">
				<el-button style="margin-right: 10px" type="success">批量导入</el-button>
			</el-upload>
			<el-button style="margin-right: 10px" type="info" v-if="data.user.role === 'admin'" @click="exp()">下载模板</el-button>

			<el-button style="margin-right: 10px" type="warning" @click="exp()">批量导出</el-button>
		</div>
    <div v-if="data.user.role === 'admin'" class="card" style="margin-bottom: 5px">
			<el-button v-if="data.user.role === 'admin'" type="primary" plain @click="handleAdd">新增</el-button>
			<el-button  type="danger" plain @click="delBatch">批量删除</el-button>
		</div>

    <div class="card" style="margin-bottom: 5px;">
      <el-table :data="data.tableData" strip @selection-change="handleSelectionChange">
				<el-table-column  type="selection" width="55" align="center"></el-table-column>
				<el-table-column label="患者" prop="medName"></el-table-column>
				<el-table-column label="药物名称" prop="medicationName"></el-table-column>
				<el-table-column label="剂量" prop="dosage"></el-table-column>
				<el-table-column label="服药频次" prop="frequency"></el-table-column>
				<el-table-column label="注意事项" prop="notice"></el-table-column>
        <el-table-column label="操作" align="center" width="200" fixed="right">
          <template v-slot="scope">
						<el-button v-if="data.user.role === 'admin'" type="primary" plain @click="handleEdit(scope.row)">编辑</el-button>
						<el-button  type="danger" plain @click="del(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="card" v-if="data.total">
      <el-pagination @current-change="load" background layout="total, prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total"/>
    </div>

    <el-dialog title="请填写信息" v-model="data.formVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
      <el-form :model="data.form" label-width="80px"  style="padding: 20px 30px">
				<el-form-item v-if="data.user.role === 'admin'" label="患者" prop="medName">
					<el-select style="width: 100%" v-model="data.form.userId">
						<el-option v-for="item in data.userData" :value="item.id" :label="item.name" :key="item.id"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="药物名称" prop="medicationName">
					<el-input v-model="data.form.medicationName" placeholder="请输入药物名称"></el-input>
				</el-form-item>
				<el-form-item label="剂量" prop="dosage">
					<el-input v-model="data.form.dosage" placeholder="请输入剂量"></el-input>
				</el-form-item>
				<el-form-item label="服药频次" prop="frequency">
					<el-input v-model="data.form.frequency" placeholder="请输入服药频次"></el-input>
				</el-form-item>
				<el-form-item label="注意事项" prop="notice">
					<el-checkbox-group v-model="data.form.noticeList">
						<el-checkbox value="定期体检" label="定期体检"></el-checkbox>
						<el-checkbox value="接种疫苗" label="接种疫苗"></el-checkbox>
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
const baseApi = 'medicationReminder'

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

// 打开编辑弹窗
const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
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
	location.href = 'http://localhost:9090/medicationReminder/export?token=' + data.user.token
}
const impSuccessUpload = () => {
	ElMessage.success('导入成功');
	load();
}
// 加载用户
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
// 加载用户
loadUser();

</script>
