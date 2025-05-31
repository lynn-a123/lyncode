<template>
  <div> 
   <div class="card" style="margin-bottom: 5px">
	   <el-row v-if="data.tab==1 || data.tab==3 || data.tab==5">
		   <el-col :span="5"  v-if="data.tab==1 || data.tab==3">
			   <el-input v-model="data.search.name" prefix-icon="Search" 
			   placeholder="请输入书籍名称/作者"></el-input> 
		   </el-col> 
 
		   <el-col :span="5">
		   		<el-select   v-model="data.search.typeId"    placeholder="分类">
		   			<el-option v-for="(item,index) in data.typeList" :key="index" :value="item.id"
		   				:label="item.title">
		   				 
		   			</el-option>
		   						
		   		</el-select>	   
		   </el-col>
		    <el-col :span="6">
				<el-button type="info" v-if="data.tab==1  || data.tab==5" plain @click="load">查询</el-button>
				<el-button type="info" v-if="data.tab==3" plain @click="loadShelf">查询</el-button>
			 	<el-button type="warning" plain style="margin-right: 10px" @click="reset">重置</el-button> 
			</el-col>
	   </el-row>
 

	
   </div> 
   
   <div class="mylist">
	   <div :class="data.tab==1?'curitem':'myitem'" @click="selList(1)">所有书籍</div>
	    <div :class="data.tab==2?'curitem':'myitem'" @click="selList(2)">书籍推荐(协同)</div>
	  <div :class="data.tab==3?'curitem':'myitem'" @click="selList(3)">我的书架</div>
	  <div :class="data.tab==4?'curitem':'myitem'" @click="selList(4)">近期阅读(7天)</div>
	   <div :class="data.tab==5?'curitem':'myitem'" @click="selList(5)">书籍排行榜</div>
   </div>
    
	<!-- 所有书籍  推荐书籍 -->
	<div class="card" style="margin-bottom: 5px;"  v-if="data.tab==1 || data.tab==2  || data.tab==5">
	  <el-table :data="data.tableData" strip @selection-change="handleSelectionChange">
				<el-table-column  type="selection" width="55" align="center"></el-table-column>
				<el-table-column min-width="120" label="分类" prop="typeName"></el-table-column> 
				<el-table-column min-width="150"  label="书籍名称"  prop="name"></el-table-column>
				  <el-table-column min-width="120" label="作者"  prop="author"></el-table-column> 
			  <el-table-column min-width="150" label="上传时间"  prop="createTime"></el-table-column> 
		   <el-table-column min-width="100"  label="阅读量"  prop="readNums"></el-table-column> 	   
	    <el-table-column label="操作" align="center" width="120" fixed="right">
	      <template v-slot="scope">
				 <el-button  type="primary" plain @click="handleBookDetail(scope.row)">查看详情</el-button>
					 
	      </template>
	    </el-table-column>
	  </el-table>
	</div>
	<div class="card" v-if="data.total && (data.tab==1)"  >
	  <el-pagination @current-change="load" background layout="total, prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total"/>
	</div>
	
	<el-dialog title="查看书籍详情" v-model="data.formVisible" width="70%" :close-on-click-modal="false" destroy-on-close>
	   
	<el-form  ref="formRef" :model="data.form"   label-width="120px"  style="padding: 20px 30px">
			<el-row>
								
						      <img v-if="data.form.logo" :src="data.form.logo" class="avatar">
						 
			</el-row>
			<el-row>
				 <el-col :span="12">
				<el-form-item label="书籍名称:" prop="name">
							<div>{{data.form.name}}</div>
				</el-form-item>
				</el-col>
				<el-col :span="12">
				<el-form-item label="书籍分类:" prop="typeName">
							<div>{{data.form.typeName}}</div>
				</el-form-item>
				</el-col>
		 </el-row>
		 <el-row>
			 <el-col :span="12">
			<el-form-item label="书籍作者:"  >
							<div>{{data.form.author}}</div>
			</el-form-item>
			</el-col>
			<el-col :span="12">
			<el-form-item label="书籍版权:"  >
					<div>{{data.form.version}}</div>
			</el-form-item>
			</el-col>
		 </el-row>
		 <el-row>
			 <el-col :span="24">
			<el-form-item label="书籍简介:"  >
				<div>{{data.form.content}}</div>
			</el-form-item>
			</el-col> 
		 </el-row>
		 <el-row>
			 <el-col :span="24">
			<el-form-item label="书籍书评:"  >
				<div>{{data.form.comment}}</div>
			</el-form-item>
			</el-col> 
		 </el-row>
		 <el-row>
			 <el-col :span="24">
			 <el-card class="box-card"  >
			   <div slot="header" class="clearfix">
			     <span style="font-weight: bold;">读书笔记</span> 
			   </div>
			 <el-timeline style="padding-top: 20px;" v-if="data.form.recordList.length>0"  >
			    <el-timeline-item
			      v-for="(log, index) in data.form.recordList"
			      :key="index"
			      :timestamp="log.createTime">
			      <span style="font-weight: bold;">{{log.uname}}:</span>{{log.content}}
			    </el-timeline-item>
			  </el-timeline>
			  <div v-else style="text-align: center;">暂无记录</div>
			 </el-card>
			  </el-col> 
		  </el-row>
		 <el-row  >
	      	<el-button  type="primary" style="margin: auto;margin-top: 10px;" v-if="data.form.isShelf==0"
						 					 @click="addShelf(data.form)"   >加入书架</el-button>
		<el-button  type="success" style="margin: auto;margin-top: 10px;" v-if="data.form.isShelf==1"   >已加入书架</el-button>
		 </el-row>
		 
		 <div style="text-align: center;padding-top: 10px;">
			 <a  
			  :href="data.form.fileUrl" style="color: blue;" >下载电子书</a>
		 </div>
		 
			
	</el-form>
	
	</el-dialog>
	
	<!--我的书架-->
	<div class="card" style="margin-bottom: 5px;"  v-if="data.tab==3 || data.tab==4">
	  <el-table :data="data.tableBookShelfData" strip  >
				<el-table-column min-width="150"  label="书籍名称"  prop="bookName"></el-table-column>
			 <el-table-column min-width="120"  label="书籍类型"  prop="bookTypeName"></el-table-column>
			 <el-table-column min-width="150" label="加入时间"  prop="createTime"></el-table-column> 
			  <el-table-column min-width="150" label="最近阅读时间"  prop="updateTime"></el-table-column> 
<!-- 		   <el-table-column min-width="100"  label="总时长(分)"  prop="totalTime"></el-table-column> -->	   
	    <el-table-column label="操作" align="center" width="160" fixed="right">
	      <template v-slot="scope">
				 <el-button size="small"  type="primary" plain @click="handleShelfDetail(scope.row.id)">详情</el-button>
				  <el-button  size="small"  type="danger" plain @click="handleShelfDel(scope.row.id)">移除</el-button>
	      </template>
	    </el-table-column>
	  </el-table>
	</div>
	
	<el-dialog title="查看我书架书籍详情" v-model="data.formShelfVisible" width="70%" :close-on-click-modal="false" destroy-on-close>
	   
	<el-form  ref="formRef" :model="data.form"   label-width="120px"  style="padding: 20px 30px">
			<el-row>
			  <img v-if="data.form.logo" :src="data.form.logo" class="avatar">
						 
			</el-row>
			<el-row>
				 <el-col :span="12">
				<el-form-item label="书籍名称:" prop="name">
						<div>{{data.form.name}}</div>
				</el-form-item>
				</el-col>
				<el-col :span="12">
				<el-form-item label="书籍分类:" prop="typeName">
						<div>{{data.form.typeName}}</div>
				</el-form-item>
				</el-col>
		 </el-row>
		 <el-row>
			 <el-col :span="12">
			<el-form-item label="书籍作者:"  >
					<div>{{data.form.author}}</div>
			</el-form-item>
			</el-col>
			<el-col :span="12">
			<el-form-item label="书籍版权:"  >
					<div>{{data.form.version}}</div>
			</el-form-item>
			</el-col>
		 </el-row>
		 <el-row>
			 <el-col :span="24">
			<el-form-item label="书籍简介:"  >
								<div>{{data.form.content}}</div>
			</el-form-item>
			</el-col> 
		 </el-row>
		 <el-row>
			 <el-col :span="24">
			<el-form-item label="书籍书评:"  >
								<div>{{data.form.comment}}</div>
			</el-form-item>
			</el-col> 
		 </el-row>
		 <el-row>
		 			 <el-col :span="24">
		 			<el-form-item label="阅读时长:"  >
		 								<div>{{data.form.bookShelf.totalTime}}分钟</div>
		 			</el-form-item>
		 			</el-col> 
		 </el-row>
		 <el-row  v-if="data.form.nums>0">
			  <el-col :span="6">
			  </el-col>
		 			 <el-col :span="12">
		 	         <div>阅读进度</div>
		 			 <el-progress :text-inside="true" :stroke-width="16" 
					 :percentage="data.form.bookShelf.curNums*100/data.form.nums"></el-progress>
		 			</el-col> 
		 </el-row>
		
		 
		 <div  class="bottomBox">
			 <el-button size="small"  type="primary" plain
			 @click="showChapterWin(data.form)">目录</el-button>
		 				   <el-button size="small"  type="primary" plain 
		 				   @click="handleReadBook()">阅读</el-button>
		 				   <el-button size="small"  type="primary" plain
		 				   @click="handleBookSet()">设置</el-button> 
						   
		 </div>
		 
		 <el-row>
			 <el-col :span="24">
			 <el-card class="box-card"  >
			   <div slot="header" class="clearfix">
			     <span style="font-weight: bold;">读书笔记</span> 
			   </div>
			 <el-timeline style="padding-top: 20px;" v-if="data.form.recordList.length>0"  >
			    <el-timeline-item
			      v-for="(log, index) in data.form.recordList"
			      :key="index"
			      :timestamp="log.createTime">
			      <span style="font-weight: bold;">{{log.uname}}:</span>{{log.content}}
			    </el-timeline-item>
			  </el-timeline>
			  <div v-else style="text-align: center;">暂无记录</div>
			 </el-card>
			  </el-col> 
		  </el-row>
		
		 
			
	</el-form>
	
	</el-dialog>
	
	
	<el-dialog :title="data.chapterTitle" v-model="data.formChapterListVisible"
	width="70%" :close-on-click-modal="false" destroy-on-close>
	
	<el-table :data="data.tableChapterData" strip @selection-change="handleSelectionChange">
					<el-table-column min-width="350" label="章节标题" prop="title"></el-table-column> 
	  <el-table-column label="操作" align="center" width="150" fixed="right">
	    <template v-slot="scope">
				 <el-button size="small" type="primary" 
				 plain    @click="handleReadBook(scope.row.sort)">阅读</el-button>
	    </template>
	  </el-table-column>
	</el-table>
	
	</el-dialog>
	
	<el-dialog title="阅读设置" v-model="data.formBookSetVisible"
	width="50%" :close-on-click-modal="false" destroy-on-close>
	<el-form  ref="formBookSetRef" :model="data.bookSetForm"
	 label-width="80px"  style="padding: 20px 30px">
					 
			<el-form-item label="字体颜色" prop="color">
				 <el-select v-model="data.bookSetForm.color"  >
				 				
				 	<el-option v-for="(item,index) in data.colorList" 
					:key="item.id" :value="item.value"
				 		:label="item.name">
				 		 
				 	</el-option>
				 				
				 </el-select>
			</el-form-item>
			
			<el-form-item label="背景颜色" prop="bgcolor">
				 <el-select v-model="data.bookSetForm.bgcolor"  >
				 				
				 	<el-option v-for="(item,index) in data.bgcolorList" 
					:key="item.id" :value="item.value"
				 		:label="item.name">
				 		 
				 	</el-option>
				 				
				 </el-select>
			</el-form-item>
		 
			<el-form-item label="字体大小" prop="sort">
				<el-input type="number" v-model="data.bookSetForm.fontSize"
				 placeholder="请输入字体大小"></el-input>
			</el-form-item>
		 
			
			<el-form-item label="字体加重" prop="fontBold">
				 <el-select v-model="data.bookSetForm.fontBold"  >
				 				
				 	<el-option v-for="(item,index) in data.boldList" 
					:key="item.id" :value="item.value"
				 		:label="item.name">
				 		 
				 	</el-option>
				 				
				 </el-select>
			</el-form-item>		 
				 
	</el-form>
	<template #footer>
	  <span class="dialog-footer">
	    <el-button @click="data.formBookSetVisible = false">取消</el-button>
	    <el-button type="primary" @click="saveBookSet">保存</el-button>
	  </span>
	</template>
	</el-dialog>
	
	
	
	<el-dialog title="阅读" v-model="data.formReadBookVisible"
	width="90%" :close-on-click-modal="false" >
	<el-form  ref="formReadBookRef" :model="data.readBookForm"
	 label-width="80px"  style="padding: 20px 30px">
	 <div style="text-align: center;font-weight: bold;padding-bottom: 10px;">
	 	 {{data.readBookForm.title}}
	 </div>
	    <div v-html="data.readBookForm.content" :style="{'color':data.readBookForm.bookSet.color,'backgroundColor':data.readBookForm.bookSet.bgcolor,'fontSize':data.readBookForm.bookSet.fontSize+'px','fontWeight':data.readBookForm.bookSet.fontBold}">
		</div>
		<div  class="bottomBox">
					   <el-button size="small"  type="primary" plain  v-if="data.readBookForm.nextNum>0"
						   @click="handleReadBook(data.readBookForm.nextNum)">下一章</el-button>
						   <el-button size="small"  type="primary" plain
						   @click="handleBookReadClose()">关闭阅读</el-button>
					  <el-button   size="small"  type="primary" plain
								   @click="handleNote()">笔记</el-button>
					<el-button v-if="data.readBookForm.listeningUrl!=null" size="small"  type="primary" plain
					@click="handleListening()">听书</el-button>			   
		</div>

		 <el-card class="box-card"  >
		   <div slot="header" class="clearfix">
			 <span style="font-weight: bold;">读书笔记</span> 
		   </div>
		 <el-timeline style="padding-top: 20px;" v-if="data.readBookForm.recordList.length>0"  >
			<el-timeline-item
			  v-for="(log, index) in data.readBookForm.recordList"
			  :key="index"
			  :timestamp="log.createTime">
			  <span style="font-weight: bold;">{{log.uname}}:</span>{{log.content}}
			</el-timeline-item>
		  </el-timeline>
		  <div v-else style="text-align: center;">暂无记录</div>
		 </el-card>
	 </el-form>
	</el-dialog>
	
	
	<el-dialog title="阅读笔记" v-model="data.formBookNoteVisible"
	width="30%" :close-on-click-modal="false" destroy-on-close>
	<el-form  ref="formBookSetRef" :model="data.bookNoteForm"
	 label-width="80px"  style="padding: 20px 30px"> 
		 
			<el-form-item label="笔记内容" prop="sort">
				<el-input type="textarea" v-model="data.bookNoteForm.content"
				 placeholder="请输入笔记内容"></el-input>
			</el-form-item>
		   
				 
	</el-form>
	<template #footer>
	  <span class="dialog-footer">
	    <el-button @click="data.formBookNoteVisible = false">取消</el-button>
	    <el-button type="primary" @click="saveBookNote">保存</el-button>
	  </span>
	</template>
	</el-dialog>
	
	<el-dialog title="听书" v-model="data.formBookListeningVisible"
	width="60%" :close-on-click-modal="false" destroy-on-close>
	<AudioPlayer />
	
	</el-dialog>
	
	
  </div>
</template>

<script setup> 
import { reactive, ref } from "vue"
import request from "@/utils/request";
import {ElMessage, ElMessageBox} from "element-plus";
import AudioPlayer from './AudioPlayer.vue';
const baseApi = 'bookInfo'

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'), 
  realFileList: [],
  typeList:[],
  tableData: [],
  tableBookShelfData:[],
  tab:1,
  total: 0,
  pageNum: 1,  // 当前的页码
  pageSize: 5,  // 每页的个数
  formVisible: false,
  formShelfVisible:false,
  form: {},
  search: {},
	ids: [],
formChapterListVisible:false,
tableChapterData:[],
searchChapter: {},
formBookSetVisible:false,
readBookForm:{},
formReadBookVisible:false,
bookSetForm:{},
formBookNoteVisible:false,
bookNoteForm:{},
formBookListeningVisible:false,
colorList:[{value:'#DC143C',name:'红色'},{value:'#32CD32',name:'绿色'},{value:'#000000',name:'黑色'},{value:'#fff',name:'白色'}],
bgcolorList:[{value:'#DC143C',name:'红色'},{value:'#32CD32',name:'绿色'},{value:'#000000',name:'黑色'},{value:'#fff',name:'白色'}],
boldList:[{value:'100',name:'100'},{value:'200',name:'200'},{value:'bold',name:'加粗'}],
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

 
// 打开查看书籍弹窗
const handleBookDetail = (row) => {
 var info= JSON.parse(JSON.stringify(row));
 console.log(info)
  var  form={id:info.id,uid:data.user.id};
 request.get('bookInfo/selectById',{
	 params: form
 }).then(res => {
	 data.form = res.data.books;
	 data.form.isShelf = res.data.isShelf;
	  data.form.recordList = res.data.recordList;
      data.formVisible = true
 })
 
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

//加载书籍分类
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
loadType()

//选择
const selList=(val)=>{
   data.tab=val;  
    data.search.sort=''; 
   if(val==1){
	    data.pageNum=1;
		  data.pageSize=10;
	     load();
   }
   else if(val==2){//推荐
	  loadRecommend();
   }else if(val==3){//我的书架
   	   loadShelf();
   }else if(val==4){//我的近期阅读
   	   loadLastShelf();
   }
   else if(val==5){//排行榜
   	   data.search.sort=1; 
	   data.pageNum=1;
	    data.pageSize=6;
	   load();
   }
   
 
}



// 加载表格数据  推荐协同
const loadRecommend = () => {
    
  request.get(  'bookInfo/getRecommend', {
    params: {
		uid:data.user.id,
		count:3, 
	}
  }).then(res => {
    data.tableData = res.data || [] 
  })
}

// 加载表格数据 我的书架信息
const loadShelf = () => {
    
  request.get(  'bookShelf/selectAll', {
    params: {
		uid:data.user.id,
		bookName:data.search.name,
		bookType:data.search.type,
	}
  }).then(res => {
    data.tableBookShelfData = res.data || [] 
  })
}

// 加载表格数据  我近期阅读 
const loadLastShelf = () => {
    
  request.get(  'bookShelf/selectAll', {
    params: {
		uid:data.user.id,
		flag:1,
		sort:1
	}
  }).then(res => {
    data.tableBookShelfData = res.data || [] 
  })
}
 
//加入书架
const addShelf= (info) => {

    var  form={bookId:info.id,
	    bookName:info.name,
		 bookAuthor:info.author,
		bookTypeId:info.typeId,
		bookTypeName:info.typeName,
		uid:data.user.id,
		uname:data.user.username}; 
  request.post('bookShelf/add', form).then(res => {
	   ElMessage.success('加入成功')
      data.form.isShelf=1; 
  })
}

//修改阅读数
const updateReadNums= (id) => {

    var  form={id:id}; 
  request.get('bookInfo/updateReadNums', {
    params: form
  }).then(res => { 
	  load();
  })
}
//移除书架
const handleShelfDel = (id) => {
  ElMessageBox.confirm('确定移除书架?', '确认', { type: 'warning' }).then(res => {
     deleteShelf(id)
	 
  }).catch(err => {
    console.error(err)
  })
}
//移除书架
const deleteShelf = (id) => { 
  request.delete('bookShelf/delete/'+id).then(res => { 
	  loadShelf();
  })
}

//查看书架书籍 
const handleShelfDetail = (id) => {  
  var  form={id:id,uid:data.user.id};
 request.get('bookShelf/selectById',{
	 params: form
 }).then(res => {
	 data.form = res.data.books; 
	  data.form.recordList = res.data.recordList;
	    data.form.bookShelf = res.data.bookShelf;
      data.formShelfVisible = true
 })
 
}

//显示书籍章节列表 
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

//阅读设置 
const handleBookSet = () => {  
	
	request.get('bookSet/selectById/'+data.user.id).then(res => {
		 data.bookSetForm = res.data|| {} ; 
	      data.formBookSetVisible = true;
	})

}

//保存阅读设置
const formBookSetRef = ref();
const saveBookSet = () => { 
  data.bookSetForm.id ? updateBookSet() : addBookSet()
}

//新增阅读设置
const addBookSet = () => { 
	 
  data.bookSetForm.uid=data.user.id;
  data.bookSetForm.uname=data.user.username;
  request.post( 'bookSet/add', data.bookSetForm).then(res => {
  if (res.code === '200') {
	data.formBookSetVisible = false
	load()
  } else {
	ElMessage.error(res.msg)
  }
  })

}
//修改阅读设置
const updateBookSet = () => { 
	  
  request.put( 'bookSet/update', data.bookSetForm).then(res => {
  if (res.code === '200') {
	data.formBookSetVisible = false
	load()
  } else {
	ElMessage.error(res.msg)
  }
  })

}

const formReadBookRef = ref();
//阅读
const handleReadBook = (num) => {  
	request.get('bookInfo/readBook', {
		params: {
			shelfId:data.form.bookShelf.id,
			num:num
		}
	}
	).then(res => {
		
		if(res.code==500){
				   ElMessage.error(res.msg);
				  return;
		}
		 data.readBookForm = res.data.curChapter|| {} ; 
		   data.readBookForm.recordList = res.data.recordList;
		    data.readBookForm.nextNum = res.data.nextNum;
		data.readBookForm.bookSet = res.data.bookSet|| {} ;
			
	      data.formReadBookVisible = true;
		  handleShelfDetail(data.form.bookShelf.id);
	})

}

//笔记 handleNote
const handleNote = () => {   
	   data.formBookNoteVisible = true; 
	   
	   data.bookNoteForm={
			bookId:data.form.id,
		   bookName:data.form.name,
		   content:'',
			uid:data.user.id,
			uname:data.user.username,
		}; 
}

//笔记新增
const saveBookNote= () => {

  request.post('bookRecord/add', data.bookNoteForm).then(res => {
	   ElMessage.success('操作成功')
      data.formBookNoteVisible=false; 
	  handleReadBook()
  })
}

//关闭阅读  
const handleBookReadClose= () => {

  request.get('bookInfo/closeReadBook', {
		params: {
			shelfId:data.form.bookShelf.id 
		}}).then(res => {
	   ElMessage.success('操作成功')
      data.formReadBookVisible=false;  
	  handleShelfDetail(data.form.bookShelf.id);
	})

}

//听书
   const handleListening= () => {
		  localStorage.setItem("listenerBookTitle",data.readBookForm.title)
		 localStorage.setItem("listenerBookUrl",data.readBookForm.listeningUrl)
         data.formBookListeningVisible=true;   
   }
</script>
<style>
	.avatar {
	 width: 128px;
	 height: 128px;
	 display: block;
	 margin: auto;
	 margin-bottom: 10px;
	}
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
	.bottomBox{
		width: 30%;
		display: flex;
		justify-content: space-between;
		margin: auto;
		margin-top: 10px;
		margin-bottom: 20px;
	}
</style>
 