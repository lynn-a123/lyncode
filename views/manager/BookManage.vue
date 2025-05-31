<template>
  <div>
    
		<div class="card" style="margin-bottom: 5px">
			<el-row>
					   <el-col :span="5">
						   
			<el-input v-model="data.search.name" prefix-icon="Search" 
			style="width: 240px; margin-right: 10px; margin-bottom: 3px" placeholder="请输入书籍名称/作者"></el-input> 
			</el-col>
			
			<el-col :span="5">
			<el-select v-model="data.search.typeId"  >
							
				<el-option v-for="(item,index) in data.typeList"
				 :key="item.id" :value="item.id"
					:label="item.title">
					 
				</el-option>
							
			</el-select>
			</el-col>
			 <el-col :span="6">
			<el-button type="info" plain @click="load">查询</el-button>
			<el-button type="warning" plain style="margin-right: 10px" @click="reset">重置</el-button> 
			<el-button type="primary" plain @click="handleAdd">新增</el-button> 
			</el-col>
			</el-row>
		</div> 

    <div class="card" style="margin-bottom: 5px;">
      <el-table :data="data.tableData" strip @selection-change="handleSelectionChange">
				<el-table-column  type="selection" width="55" align="center"></el-table-column>
				<el-table-column min-width="120" label="书籍分类" prop="typeName"></el-table-column> 
				<el-table-column min-width="150"  label="书籍名称"  prop="name"></el-table-column>
				 <el-table-column min-width="120"  label="作者"  prop="author"></el-table-column>
				 <el-table-column min-width="150"  label="版权"  prop="version"></el-table-column> 
					 <el-table-column min-width="120"  label="章节" 
					  prop="nums">
					  <template v-slot="scope">
               <el-button size="small" type="primary" 
			   plain @click="showChapterWin(scope.row)">{{scope.row.nums}}</el-button>
					  					 
					  </template>
					  </el-table-column> 
			  <el-table-column min-width="150" label="创建时间"  prop="createTime"></el-table-column> 
        <el-table-column label="操作" align="center" width="230" fixed="right">
          <template v-slot="scope">
						<el-button size="small" type="primary" plain @click="handleEdit(scope.row)">编辑</el-button>
						<el-button size="small" type="success" plain @click="addChapterWin(scope.row)">添加章节</el-button>
						<el-button size="small" type="danger" plain @click="del(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="card" v-if="data.total">
      <el-pagination @current-change="load" background layout="total, prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total"/>
    </div>

    <el-dialog title="请填写信息" v-model="data.formVisible" width="70%" :close-on-click-modal="false" destroy-on-close>
      <el-form  ref="formRef" :model="data.form" :rules="data.rules" label-width="80px"  style="padding: 20px 30px">
				<el-form-item label="书籍分类"   prop="typeName">
					<el-select v-model="data.form.typeName"  
					@change="selType" >
				
						<el-option v-for="(item,index) in data.typeList" 
						:key="index" :value="index"
							:label="item.title">
							 
						</el-option>
				
					</el-select>
				</el-form-item>
				<el-form-item label="书籍名称" prop="name">
					<el-input v-model="data.form.name"
					 placeholder="请输入书籍名称"></el-input>
				</el-form-item>
				
				<el-form-item label="书籍作者" prop="author">
					<el-input v-model="data.form.author"
					 placeholder="请输入书籍作者"></el-input>
				</el-form-item>
				<el-form-item label="版权信息" prop="version">
					<el-input v-model="data.form.version"
					 placeholder="请输入版权信息"></el-input>
				</el-form-item>
				<el-form-item label="书籍简介" prop="content">
					<el-input type="textarea" v-model="data.form.content"
					 placeholder="请输入书籍简介"></el-input>
				</el-form-item>
				<el-form-item label="书籍书评" prop="comment">
					<el-input type="textarea" v-model="data.form.comment"
					 placeholder="请输入书籍书评"></el-input>
				</el-form-item>
				<el-form-item label="书籍封面" prop="logo">
					<el-upload class="avatar-uploader2" 
					:action="'http://localhost:9090/files/upload'" 
						 :show-file-list="false"
					:on-success="logoSuccessUpload" :limit="1"  >
					  <img v-if="data.form.logo" :src="data.form.logo" class="avatar">
					 <span v-else class="avatar-uploader-icon"  >上传封面</span>
					</el-upload>
				</el-form-item>
			<el-form-item label="电子文件" prop="fileUrl">
				<el-upload class="avatar-uploader" 
				:action="'http://localhost:9090/files/upload'" 
					  :file-list="data.realFileList"
				:on-success="fileUrlSuccessUpload" :limit="1"  >
					<el-button type="primary">点击上传</el-button>
				</el-upload>
			</el-form-item>
			<!-- <el-form-item label="听书文件" prop="listeningUrl">
				<el-upload class="avatar-uploader" 
				:action="'http://localhost:9090/files/upload'" 
					  :file-list="data.listeningFileList"
				:on-success="listeningUrlSuccessUpload" :limit="1"  >
					<el-button type="primary">点击上传</el-button>
				</el-upload>
			</el-form-item> -->
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="data.formVisible = false">取消</el-button>
          <el-button type="primary" @click="save">保存</el-button>
        </span>
      </template>
    </el-dialog>
	
	<el-dialog title="章节信息编辑" v-model="data.formChapterVisible" 
	width="70%" :close-on-click-modal="false" destroy-on-close>
	  <el-form  ref="formChapterRef" :model="data.chapterForm" 
	  :rules="data.rulesChapter" label-width="80px"  style="padding: 20px 30px">
				 
				<el-form-item label="章节标题" prop="title">
					<el-input v-model="data.chapterForm.title"
					 placeholder="请输入章节标题"></el-input>
				</el-form-item> 
				<el-form-item label="章节内容" prop="content">
		 
					<Editor
					    style="height: 300px; overflow-y: hidden;width: 700px;border: solid #323232 1px;"
					    v-model="data.chapterForm.content"
					    :defaultConfig="editorConfig"
					    mode="simple"  placeholder="请输入章节内容介"
					    @onCreated="onCreated"
					/> 
			 <el-form-item label="听书文件" style="margin-top: 10px;" prop="listeningUrl">
			 	<el-upload class="avatar-uploader" 
			 	:action="'http://localhost:9090/files/upload'" 
			 		  :file-list="data.listeningFileList"
					  accept=".mp3"
			 	:on-success="listeningUrlSuccessUpload" :limit="1"  >
			 		<el-button type="primary">点击上传</el-button>
			 	</el-upload>
			 </el-form-item> 
				</el-form-item>
				 
			 
	  </el-form>
	  <template #footer>
	    <span class="dialog-footer">
	      <el-button @click="data.formChapterVisible = false">取消</el-button>
	      <el-button type="primary" @click="saveChapter">保存</el-button>
	    </span>
	  </template>
	</el-dialog>
	
	
	<el-dialog :title="data.chapterTitle" v-model="data.formChapterListVisible"
	width="70%" :close-on-click-modal="false" destroy-on-close>
	
	<el-table :data="data.tableChapterData" strip @selection-change="handleSelectionChange">
					<el-table-column min-width="350" label="章节标题" prop="title"></el-table-column> 
					<el-table-column min-width="120"  label="排序"  prop="sort"></el-table-column>
				  <el-table-column min-width="150" label="创建时间"  prop="createTime"></el-table-column> 
	  <el-table-column label="操作" align="center" width="150" fixed="right">
	    <template v-slot="scope">
							<el-button size="small" type="primary" plain @click="editChapter(scope.row)">编辑</el-button>
							<el-button size="small" type="danger" plain @click="delChapter(scope.row.id)">删除</el-button>
	    </template>
	  </el-table-column>
	</el-table>
	
	</el-dialog>
  </div>
</template>

<script setup> 
import { reactive, ref, shallowRef, onMounted  } from "vue"
import request from "@/utils/request";
import {ElMessage, ElMessageBox} from "element-plus";
 import { Editor} from '@wangeditor/editor-for-vue'
const baseApi = 'bookInfo'


 

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  rules: {
    name: [
      { required: true, message: '请输入名称', trigger: 'blur' },
    ], 
	typeName: [
	  { required: true, message: '请选择类型', trigger: 'blur' },
	], 
	author: [
	  { required: true, message: '请输入作者', trigger: 'blur' },
	], 
	version: [
	  { required: true, message: '请输入版权信息', trigger: 'blur' },
	], 
	logo: [
	  { required: true, message: '请上传封面图', trigger: 'blur' },
	], 
	fileUrl: [
	  { required: true, message: '请上传电子书文件', trigger: 'blur' },
	], 
  },
  rulesChapter: {
    title: [
      { required: true, message: '请输入标题', trigger: 'blur' },
    ], 
  	content: [
  	  { required: true, message: '请输入内容', trigger: 'blur' },
  	] 
  },
  realFileList: [],//电子书籍
  listeningFileList: [],//听书文件
  typeList:[],
  tableData: [],
  total: 0,
  pageNum: 1,  // 当前的页码
  pageSize: 5,  // 每页的个数
  formVisible: false,
  formChapterVisible:false,
  formChapterListVisible:false,
  form: {},
   chapterForm: {},
   chapterTitle:'',
  search: {},
  tableChapterData:[],
  searchChapter: {},
  editor: null,
   
	ids: [],
})


const fileUrlSuccessUpload = (res,file) => {
	data.form.fileUrl = res.data;
	data.realFileList=[];
	 var fileObj = {
		      name:'电子文件',
	 		  url:res.data 
	 };
	 						 
	data.realFileList.push(fileObj);
}

const listeningUrlSuccessUpload = (res,file) => {
	data.chapterForm.listeningUrl = res.data;
	data.listeningFileList=[];
	 var fileObj = {
		      name:'音频文件',
	 		  url:res.data 
	 };
	 						 
	data.listeningFileList.push(fileObj);
}

const logoSuccessUpload = (res,file) => {
	data.form.logo = res.data;
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
  if(data.form.fileUrl!=null){
	  var fileObj = {
	  	      name:'电子书文件',
	   		  url:data.form.fileUrl 
	 };
	   data.realFileList.push(fileObj);
  }
 
  data.formVisible = true
}

const formRef = ref()
// 新增 书籍
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

// 更新 书籍
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

// 删除 书籍
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

//保存书籍
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

//选择书籍类型
const selType=(val)=>{
   data.form.typeId=data.typeList[val].id;
   data.form.typeName=data.typeList[val].title;
}

// 加载模块数据
load()

const loadType = () => {

    var  form={};
  data.search.pageNum = 1
  data.search.pageSize = 100
  request.get('bookType/selectPage', {
    params: form
  }).then(res => {
    data.typeList = res.data?.list || [] 
  })
}

//加载书籍分类
loadType();

//显示章节列表 
const showChapterWin = (info) => { 
  data.searchChapter.bookId=info.id;
  data.chapterTitle=info.name;
  loadChapter();
  data.formChapterListVisible = true;
}

//加载书籍章节内容
const loadChapter = () => {
    
  request.get(  'bookChapter/selectAll', {
    params: {
		bookId:data.searchChapter.bookId
	}
  }).then(res => {
    data.tableChapterData = res.data || [] 
  })
}


//添加章节
const addChapterWin = (info) => {
  data.chapterForm = {} 
  data.chapterForm.bookId=info.id;
   data.chapterForm.bookName=info.name;
  data.formChapterVisible = true
}



const saveChapter = () => { 
  data.chapterForm.id ? updateChapter() : addChapter()
}

const formChapterRef = ref()
// 新增 章节
const addChapter = () => { 
	formChapterRef.value.validate((valid => {
	  if (valid) {
		  data.chapterForm.uid=data.user.id;
		  data.chapterForm.uname=data.user.username;
		  request.post( 'bookChapter/add', data.chapterForm).then(res => {
		  if (res.code === '200') {
		    data.formChapterVisible = false
		    load()
		  } else {
		    ElMessage.error(res.msg)
		  }
		  })
	  }
	}))	  

}

// 更新 章节
const updateChapter = () => {
	
	request.put(  'bookChapter/update', data.chapterForm).then(res => {
				if (res.code === '200') {
				  data.formChapterVisible = false
				  load()
				} else {
				  ElMessage.error(res.msg)
				}
	})
}

//删除章节
const delChapter = (id) => {
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除该章节吗?', '删除确认', { type: 'warning' }).then(res => {
    request.delete( 'bookChapter/delete/' + id).then(res => {
      if (res.code === '200') {
        ElMessage.success('删除成功')
		 loadChapter();
        load();
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {
    console.error(err)
  })
}

//修改章节
const editChapter = (row) => {
  data.chapterForm = JSON.parse(JSON.stringify(row)) 
  data.listeningFileList=[];
  if(data.chapterForm.listeningUrl!=null){
  	  var fileObj = {
  	  	      name:'音频文件',
  	   		  url:data.chapterForm.listeningUrl 
  	 };
  	   data.listeningFileList.push(fileObj);
  }
   
  data.formChapterVisible = true
}
 
 // 编辑器实例，必须用 shallowRef
 const editorRef = shallowRef()
 
const onCreated = (editor) => {
 editorRef.value  = editor  
}
const editorConfig = { 
placeholder: '请输入内容...',
MENU_CONF: {}
}
</script>
<style src="@wangeditor/editor/dist/css/style.css"></style>
 <style>
.avatar-uploader2 .el-upload {
 border: 1px dashed #d9d9d9;
 border-radius: 6px;
 cursor: pointer;
 position: relative;
 overflow: hidden;
}
.avatar-uploader2 .el-upload:hover {
 border-color: #409EFF;
}
.avatar-uploader-icon {
 font-size: 12px;
 color: #8c939d;
 width: 128px;
 height: 128px;
 line-height: 128px;
 text-align: center;
}
.avatar {
 width: 128px;
 height: 128px;
 display: block;
}
.avatar-uploader{
	width: 168px;
}
	 
 </style>