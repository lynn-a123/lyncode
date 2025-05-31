<template>
  <div> 
   <div class="card" style="margin-bottom: 5px">
	   <el-row>
		   <el-col :span="5">
			   <el-input v-model="data.search.fileName" prefix-icon="Search" 
			   placeholder="请输入文档名称"></el-input> 
		   </el-col>
		   <el-col :span="5">
		   		<el-select   v-model="data.search.typeId"    placeholder="文档分类">
		   			<el-option v-for="(item,index) in data.typeList" :key="index" :value="item.id"
		   				:label="item.title">
		   				 
		   			</el-option>
		   						
		   		</el-select>	   
		   </el-col>
		   <el-col :span="5">
		   		<el-select   v-model="data.search.sort"    placeholder="排序">
		   			<el-option v-for="(item,index) in data.sortList" :key="index" :value="item.id"
		   				:label="item.name">
		   				 
		   			</el-option>
		   						
		   		</el-select>	   
		   </el-col>
		    <el-col :span="6">
				<el-button type="info" plain @click="load">查询</el-button>
			 	<el-button type="warning" plain style="margin-right: 10px" @click="reset">重置</el-button> 
			</el-col>
	   </el-row>
 

	
   </div> 
   
   <div class="mylist">
	   <div :class="data.tab==1?'curitem':'myitem'" @click="selList(1)">所有文档</div>
	  <div :class="data.tab==2?'curitem':'myitem'" @click="selList(2)">近期文档(7天)</div>
	  <div :class="data.tab==3?'curitem':'myitem'" @click="selList(3)">我的收藏</div>
	   <div :class="data.tab==4?'curitem':'myitem'" @click="selList(4)">我的保险柜</div>
   </div>
    
	<div class="card" style="margin-bottom: 5px;">
	  <el-table :data="data.tableData" strip @selection-change="handleSelectionChange">
				<el-table-column  type="selection" width="55" align="center"></el-table-column>
				<el-table-column min-width="120" label="文档分类" prop="typeName"></el-table-column> 
				<el-table-column min-width="150"  label="文档名称"  prop="fileName"></el-table-column> 
			  <el-table-column min-width="150" label="存储时间"  prop="createTime"></el-table-column> 
				<el-table-column min-width="120"  label="文档大小(m)"  prop="fileSize"></el-table-column> 
		   <el-table-column min-width="100"  label="下载量"  prop="downloadNums"></el-table-column> 	   
			 <el-table-column label="用户" prop="uname"></el-table-column>
	    <el-table-column label="操作" align="center" width="120" fixed="right">
	      <template v-slot="scope">
				 <el-button  type="primary" plain @click="handleEdit(scope.row)">查看详情</el-button>
					 
	      </template>
	    </el-table-column>
	  </el-table>
	</div>
	<div class="card" v-if="data.total">
	  <el-pagination @current-change="load" background layout="total, prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total"/>
	</div>
	
	<el-dialog title="查看文档详情" v-model="data.formVisible" width="70%" :close-on-click-modal="false" destroy-on-close>
	   
	<el-form  ref="formRef" :model="data.form"   label-width="120px"  style="padding: 20px 30px">
			<el-row>
				 <el-col :span="12">
				<el-form-item label="文档名称" prop="fileName">
					<el-input v-model="data.form.fileName" disabled ></el-input>
				</el-form-item>
				</el-col>
				<el-col :span="12">
				<el-form-item label="文档分类" prop="typeName">
					<el-input v-model="data.form.typeName" disabled ></el-input>
				</el-form-item>
				</el-col>
		 </el-row>
		 <el-row>
			 <el-col :span="12">
			<el-form-item label="文档大小(m)"  >
				<el-input v-model="data.form.fileSize" disabled ></el-input>
			</el-form-item>
			</el-col>
			<el-col :span="12">
			<el-form-item label="存储时间"  >
				<el-input v-model="data.form.createTime" disabled ></el-input>
			</el-form-item>
			</el-col>
		 </el-row>
		 <el-row>
			 <el-col :span="12">
			<el-form-item label="是否收藏"  >
				 <div v-if="data.form.isCollect==1" >是</div>
				 <div v-if="data.form.isCollect==0" >否</div>
				 <el-button v-if="data.form.isCollect==1"  type="info"
				 		  @click="collect(data.form.id,0)"   >取消收藏</el-button>
				  <el-button v-if="data.form.isCollect==0"  type="primary"
					 @click="collect(data.form.id,1)"   >收藏</el-button>
			</el-form-item>
			</el-col>
			<el-col :span="12">
			<el-form-item label="是否保险柜"  >
				 <div v-if="data.form.isPwd==1" >是</div>
				 <div v-if="data.form.isPwd==0" >否</div>
				 <el-button v-if="data.form.isPwd==1"  type="info"
				 		  @click="pwdWin(data.form.id,0)"   >取消</el-button>
				  <el-button v-if="data.form.isPwd==0"  type="primary"
				 					 @click="pwdWin(data.form.id,1)"   >加入</el-button>
			</el-form-item>
			</el-col>
		 </el-row>
		 
		 <div style="text-align: center;"> 
			 <a   @click="updateNums(data.form.id)"
			  :href="data.form.fileUrl" style="color: blue;" >下载文件</a>
		 </div>
		 
			
	</el-form>
	
	</el-dialog>
	
	
	<el-dialog title="文档密码" 
	v-model="data.formBoxPwdSetVisible" width="40%" 
	:close-on-click-modal="false" destroy-on-close>
	  <el-form  ref="formRef" :model="data.boxPwdSetForm" 
	    label-width="80px"  style="padding: 20px 30px">
				<el-form-item label="密码" prop="fileName">
					<el-input type="password" 
					v-model="data.boxPwdSetForm.pwd" 
					placeholder="请输入您的密码"></el-input>
				</el-form-item>
				
	 </el-form>
	 <template #footer>
	   <span class="dialog-footer">
	     <el-button @click="data.formBoxPwdSetVisible = false">取消</el-button>
	     <el-button type="primary" @click="saveBoxPwd">确认</el-button>
	   </span>
	 </template>
	</el-dialog>
	
	<el-dialog title="文档密码"
	v-model="data.formBoxPwdLookVisible" width="40%" 
	:close-on-click-modal="false" destroy-on-close>
	  <el-form  ref="formRef" :model="data.boxPwdSetForm" 
	    label-width="80px"  style="padding: 20px 30px">
				<el-form-item label="密码" prop="fileName">
					<el-input type="password" 
					v-model="data.boxPwdSetForm.pwd" 
					placeholder="请输入您的密码"></el-input>
				</el-form-item>
				
	 </el-form>
	 <template #footer>
	   <span class="dialog-footer">
	     <el-button @click="data.formBoxPwdLookVisible = false">取消</el-button>
	     <el-button type="primary" @click="lookBoxPwd">确认</el-button>
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
  realFileList: [],
  typeList:[],
  sortList:[{'id':1,'name':'按存储时间倒序'},{'id':2,'name':'按存储时间升序'},{'id':3,'name':'按下载量排序'}],
  tableData: [],
  tab:1,
  total: 0,
  pageNum: 1,  // 当前的页码
  pageSize: 5,  // 每页的个数
  formVisible: false,
  form: {},
  search: {isPwd:'0'},
	ids: [],
boxPwdSetForm:{},
formBoxPwdSetVisible:false,
formBoxPwdLookVisible:false,
})
 


// 加载表格数据
const load = () => {

 // if(data.user.role!='admin'){
	  data.search.uid=data.user.id;
  //}
  data.search.pageNum = data.pageNum
  data.search.pageSize = data.pageSize
  request.get(baseApi + '/selectPage', {
    params: data.search
  }).then(res => {
    data.tableData = res.data?.list || []
    data.total = res.data?.total
  })
}

 
// 打开编辑弹窗
const handleEdit = (row) => {
	
	
  data.form = JSON.parse(JSON.stringify(row));
 
  data.realFileList=[];
   var fileObj = {
	      name:'文件',
 		  url:data.form.fileUrl 
 		};
 						 
  data.realFileList.push(fileObj);
  if(data.form.isPwd==1){
  	  data.formBoxPwdLookVisible=true;
  	  data.boxPwdSetForm.id=data.form.id;
  	  data.boxPwdSetForm.pwd='';  
  	  retrun;
  }
  data.formVisible = true
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

//选择
const selList=(val)=>{
   data.tab=val; 
   data.search.isCollect='';
    data.search.flag=''; 
	 data.search.isPwd='';
	if(val==1){//所有
		data.search.isPwd=0;
	}
   else if(val==2){//近期
	   data.search.flag=1;
   }
   else if(val==3){//我的收藏
   	   data.search.isCollect=1;
   }
   else if(val==4){//我的保险柜
   	   data.search.isPwd=1;
   }
   
   load()
}

const collect= (id,flag) => {

    var  form={id:id}; 
  request.get('wordInfo/collectById', {
    params: form
  }).then(res => {
      data.form.isCollect=flag;
	  load();
  })
}

const pwdWin= (id,flag) => {
	data.formBoxPwdSetVisible=true;
	data.boxPwdSetForm.id=id; 
	data.boxPwdSetForm.pwd=''; 
	data.boxPwdSetForm.flag=flag; 
}

const saveBoxPwd= () => {

  request.get('wordInfo/pwdById', {
    params: data.boxPwdSetForm
  }).then(res => {
	  if(res.code==500){
		   ElMessage.error(res.msg);
		  return;
	  }
	  data.formBoxPwdSetVisible=false;
      data.form.isPwd=data.boxPwdSetForm.flag;
	  data.formVisible = false;
	  load();
  })
}

const lookBoxPwd= () => {

  request.get('wordInfo/boxById', {
    params: data.boxPwdSetForm
  }).then(res => {
	  if(res.code==500){
		   ElMessage.error(res.msg);
		  return;
	  }
	  data.formBoxPwdLookVisible=false;
	  data.formVisible = true; 
	  load();
  })
}


const updateNums= (id) => {

    var  form={id:id}; 
  request.get('wordInfo/updateNums', {
    params: form
  }).then(res => { 
	  load();
  })
}
 

</script>
<style>
	.mylist{
		display: flex;
		padding-top: 10px;
		padding-left: 20px;
		padding-bottom: 10px;
		border-bottom: 1px solid #c4c4c4;
		border-top: 1px solid #c4c4c4;
	}
	.myitem{
		width: 150px;
		cursor:pointer;
	}
	.curitem{
		width: 150px;
		font-weight: bold;
		color: blue;
		cursor:pointer;
		text-decoration:underline;
	}
</style>
 