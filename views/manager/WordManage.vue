<template>
  <div>
    
		<div class="card" style="margin-bottom: 5px">
			<el-input v-model="data.search.fileName" prefix-icon="Search" 
			style="width: 240px; margin-right: 10px; margin-bottom: 3px" placeholder="请输入文档名称"></el-input> 
			<el-button type="info" plain @click="load">查询</el-button>
			<el-button type="warning" plain style="margin-right: 10px" @click="reset">重置</el-button> 
			<el-button type="primary" plain @click="handleAdd">新增</el-button> 
		</div> 

    <div class="card" style="margin-bottom: 5px;">
      <el-table :data="data.tableData" strip @selection-change="handleSelectionChange">
				<el-table-column  type="selection" width="55" align="center"></el-table-column>
				<el-table-column min-width="120" label="文档分类" prop="typeName"></el-table-column> 
				<el-table-column min-width="150"  label="文档名称"  prop="fileName"></el-table-column> 
			  <el-table-column min-width="150" label="存储时间"  prop="createTime"></el-table-column> 
			 <el-table-column label="下载" align="center" width="100"  >
			   <template v-slot="scope">
				     <a  :href="scope.row.fileUrl" style="color: blue;" class="ml40 font-black">下载</a>
			  </template>
			</el-table-column>	   
				<el-table-column min-width="120"  label="文档大小(m)"  prop="fileSize"></el-table-column> 	   
			 <el-table-column label="用户" prop="uname"></el-table-column>
        <el-table-column label="操作" align="center" width="200" fixed="right">
          <template v-slot="scope">
						<el-button  type="primary" plain @click="handleEdit(scope.row)">编辑</el-button>
						<el-button type="danger" plain @click="del(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="card" v-if="data.total">
      <el-pagination @current-change="load" background layout="total, prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total"/>
    </div>

    <el-dialog title="请填写信息" v-model="data.formVisible" width="70%" :close-on-click-modal="false" destroy-on-close>
      <el-form  ref="formRef" :model="data.form" :rules="data.rules" label-width="80px"  style="padding: 20px 30px">
				<el-form-item label="文档名称" prop="fileName">
					<el-input v-model="data.form.fileName" placeholder="请输入文档名称"></el-input>
				</el-form-item>
				<el-form-item label="文档分类"   prop="typeName">
					<el-select v-model="data.form.typeName"  @change="selType" >
				
						<el-option v-for="(item,index) in data.typeList" :key="index" :value="index"
							:label="item.title">
							 
						</el-option>
				
					</el-select>
				</el-form-item>
			<el-form-item label="文件" prop="avatar">
				<el-upload class="avatar-uploader" 
				:action="'http://localhost:9090/files/upload'" 
					  :file-list="data.realFileList"
					   :before-upload="beforeUpload"
					   accept=".pdf,.doc,.docx,.txt,.png,.jpg,.zip,.rar"
				:on-success="avatarSuccessUpload" :limit="1"  >
					<el-button type="primary">点击上传</el-button>
				
				</el-upload>
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
const baseApi = 'wordInfo'

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  rules: {
    fileName: [
      { required: true, message: '请输入名称', trigger: 'blur' },
    ], 
	typeName: [
	  { required: true, message: '请选择类型', trigger: 'blur' },
	], 
  },
  realFileList: [],
  typeList:[],
  tableData: [],
  total: 0,
  pageNum: 1,  // 当前的页码
  pageSize: 5,  // 每页的个数
  formVisible: false,
  form: {},
  search: {},
	ids: [],

	
})

const beforeUpload = (file) => { 
	       const isLt10M = file.size / 1024 / 1024 < 10;
	 
	      
	      if (!isLt10M) {
	        ElMessage.error('上传文件大小不能超过 10MB!');
	        return false;
	      }
	      return isJPGorPNG && isLt10M;
}
const avatarSuccessUpload = (res,file) => {
	data.form.fileSize = (file.size / 1024/1024).toFixed(2) ;
	data.form.fileUrl = res.data;
	data.realFileList=[];
	 var fileObj = {
		      name:'文件',
	 		  url:res.data 
	 };
	 						 
	data.realFileList.push(fileObj);
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
  data.realFileList=[];
  data.formVisible = true
}

// 打开编辑弹窗
const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row)) 
  data.realFileList=[];
   var fileObj = {
	      name:'文件',
 		  url:data.form.fileUrl 
 		};
 						 
  data.realFileList.push(fileObj);
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

//选择类型
const selType=(val)=>{
   data.form.typeId=data.typeList[val].id;
   data.form.typeName=data.typeList[val].title;
}

// 加载模块数据
load()

const loadType = () => {

    var  form={};
  if(data.user.role!='admin'){
	  form.uid=data.user.id;
  }
  data.search.pageNum = 1
  data.search.pageSize = 100
  request.get('wordType/selectPage', {
    params: form
  }).then(res => {
    data.typeList = res.data?.list || [] 
  })
}

//加载文档分类
loadType()

</script>
 