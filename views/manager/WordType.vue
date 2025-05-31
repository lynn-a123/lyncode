<template>
  <div>
    
		<div class="card" style="margin-bottom: 5px">
			<el-input v-model="data.search.title" prefix-icon="Search" 
			style="width: 240px; margin-right: 10px; margin-bottom: 3px" placeholder="请输入文档类型名称"></el-input> 
			<el-button type="info" plain @click="load">查询</el-button>
			<el-button type="warning" plain style="margin-right: 10px" @click="reset">重置</el-button> 
			<el-button type="primary" plain @click="handleAdd">新增</el-button> 
		</div> 

    <div class="card" style="margin-bottom: 5px;">
      <el-table :data="data.tableData" strip @selection-change="handleSelectionChange">
				<el-table-column  type="selection" width="55" align="center"></el-table-column>
				<el-table-column label="文档类型名称" prop="title"></el-table-column> 
				<el-table-column  label="排序"  prop="sort"></el-table-column> 
			 <el-table-column label="用户" prop="uname"></el-table-column>
        <el-table-column label="操作" align="center" width="200" fixed="right">
          <template v-slot="scope">
						<el-button type="primary" plain @click="handleEdit(scope.row)">编辑</el-button>
						<el-button type="danger" plain @click="del(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="card" v-if="data.total">
      <el-pagination @current-change="load" background layout="total, prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total"/>
    </div>

    <el-dialog title="请填写信息" v-model="data.formVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
      <el-form  ref="formRef" :model="data.form" :rules="data.rules" label-width="80px"  style="padding: 20px 30px">
				<el-form-item label="名称" prop="title">
					<el-input v-model="data.form.title" placeholder="请输入文档类型名称"></el-input>
				</el-form-item>
				<el-form-item label="排序" prop="description">
					<el-input type="number" v-model="data.form.sort" placeholder="请输入排序"></el-input>
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
import { reactive, ref } from "vue"
import request from "@/utils/request";
import {ElMessage, ElMessageBox} from "element-plus";
const baseApi = 'wordType'

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  rules: {
    title: [
      { required: true, message: '请输入类型名称', trigger: 'blur' },
    ], 
  },
  tableData: [],
  total: 0,
  pageNum: 1,  // 当前的页码
  pageSize: 5,  // 每页的个数
  formVisible: false,
  form: {},
  search: {},
	ids: [],
})

 const sortSort=(a, b)=> { 
    return a.sort - b.sort;
  }

// 加载表格数据
const load = () => {

  if(data.user.role!='admin'){
	  data.search.uid=data.user.id;
  }
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

const formRef = ref()
// 新增
const add = () => { 
	formRef.value.validate((valid => {
	  if (valid) {
		  data.form.uid=data.user.id;
		  data.form.uname=data.user.username;
		  request.post(baseApi + '/add', data.form).then(res => {
		  if (res.code === '200') {
		    data.formVisible = false
		    load()
		  } else {
		    ElMessage.error(res.msg)
		  }
		  })
	  }
	}))	  

}

// 更新
const update = () => {
	
	formRef.value.validate((valid => {
	  if (valid) {
		  
		  request.put(baseApi + '/update', data.form).then(res => {
			if (res.code === '200') {
			  data.formVisible = false
			  load()
			} else {
			  ElMessage.error(res.msg)
			}
		  })
	}
	}))	  
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
  data.form.id ? update() : add()
}

const reset = () => {
  data.search = {}
  load()
}

 
const handleSelectionChange = (rows) => {
	data.ids = rows.map(v => v.id)
} 

// 加载模块数据
load()

</script>
 