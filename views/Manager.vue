<template>
  <div class="manager-container">
    <div class="manager-header">
      <!-- 头部保持不变 -->
      <div class="manager-header-left"  >
        <img style="border-radius: 50%" src="@/assets/imgs/logo.png" alt="">
        <div class="title">Daily个人助理</div>
      </div>
      <div class="manager-header-center">
        <el-breadcrumb separator-class="el-icon-arrow-right">
          <el-breadcrumb-item to="/manager/home">首页</el-breadcrumb-item>
          <el-breadcrumb-item>{{ router.currentRoute.value.meta.name }}</el-breadcrumb-item>
        </el-breadcrumb>
      </div>
      <div class="manager-header-right">
        <el-dropdown style="cursor: pointer;">
          <div style="padding-right: 20px; display: flex; align-items: center;">
            <img v-if="data.user.avatar" :src="data.user?.avatar" alt="" style="width: 40px; height: 40px; display: block; border-radius: 50%">
            <img v-else src="@/assets/imgs/avatar.png" alt="" style="width: 40px; height: 40px; display: block; border-radius: 50%">
            <span style="margin-left: 5px">{{ data.user?.name }}</span>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click.native="goToPerson">个人资料</el-dropdown-item>
              <el-dropdown-item @click.native="router.push('/manager/password')">修改密码</el-dropdown-item>
              <el-dropdown-item @click.native="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>

    <div style="display: flex">
      <!-- 修改左侧菜单结构 -->
      <div class="manager-main-left">
        <el-menu
            :default-active="router.currentRoute.value.path"
            :default-openeds="['task']"
            router
        >
          <el-menu-item index="/manager/home">
            <el-icon><home-filled /></el-icon>
            <span>系统首页</span>
          </el-menu-item>

          <!-- 事务管理 -->
          <el-sub-menu index="task">
            <template #title>
              <el-icon><Menu /></el-icon>
              <span>事务管理</span>
            </template>
            <el-menu-item index="/manager/task">任务记录</el-menu-item>
            <el-menu-item  index="/manager/reminder">任务提醒</el-menu-item>
          </el-sub-menu>

          <!-- 健康管理 -->
          <el-sub-menu index="health">
            <template #title>
              <el-icon><FirstAidKit /></el-icon>
              <span>健康管理</span>
            </template>
            <el-menu-item index="/manager/healthProfile">健康档案</el-menu-item>
            <el-menu-item index="/manager/medicalReport">检查报告</el-menu-item>
            <el-menu-item index="/manager/medicationReminder">服药提醒</el-menu-item>
          </el-sub-menu>

          <!-- 理财管理 -->
          <el-sub-menu index="finance">
            <template #title>
              <el-icon><Money /></el-icon>
              <span>理财管理</span>
            </template>
            <el-menu-item index="/manager/financial">财富报告</el-menu-item>
            <el-menu-item index="/manager/financialAccount">每日记账</el-menu-item>
            <el-menu-item index="/manager/investment">理财产品</el-menu-item>
          </el-sub-menu>

          <!-- 电子手账，饮食管理，运动管理 -->
          <el-sub-menu index="Food">
            <template #title>
              <el-icon><FirstAidKit /></el-icon>
              <span>饮食运动管理</span>
            </template>
            <el-menu-item index="/manager/healthInf">个人信息</el-menu-item>
            <el-menu-item index="/manager/Food">饮食管理</el-menu-item>
            <el-menu-item index="/manager/Sports">运动管理</el-menu-item>
            <el-menu-item index="/manager/HandBook">电子手账</el-menu-item>
          </el-sub-menu>
		  
		  <!-- 文档管理 -->
		  <el-sub-menu index="word">
		    <template #title>
		      <el-icon><Money /></el-icon>
		      <span>文档管理</span>
		    </template>
		    <el-menu-item index="/manager/wordType">文档分类管理</el-menu-item>
		    <el-menu-item index="/manager/wordManage">文档存储管理</el-menu-item>
			  <el-menu-item index="/manager/wordSelect">文档查询</el-menu-item>
		  </el-sub-menu>
		  
		  <!-- 读书管理 -->
		  <el-sub-menu index="book">
		    <template #title>
		      <el-icon><Money /></el-icon>
		      <span>书籍管理</span>
		    </template>
		    <el-menu-item index="/manager/bookType" v-if="data.user.role === 'admin'" >书籍分类管理</el-menu-item>
		    <el-menu-item index="/manager/bookManage"   >书籍信息管理</el-menu-item>
		    <el-menu-item index="/manager/bookSelect" v-if="data.user.role === 'user'">书籍阅读</el-menu-item>
		  </el-sub-menu>
		  
		  

          <!-- 用户信息 (仅管理员可见) -->
          <el-sub-menu index="user" v-if="data.user.role === 'admin'">
            <template #title>
              <el-icon><UserFilled /></el-icon>
              <span>用户信息</span>
            </template>
            <el-menu-item index="/manager/admin">管理员</el-menu-item>
            <el-menu-item index="/manager/user">用户</el-menu-item>
          </el-sub-menu>

          <!-- 系统管理 -->
          <el-sub-menu index="sys">
            <template #title>
              <el-icon><Setting /></el-icon>
              <span>系统管理</span>
            </template>
            <el-menu-item index="/manager/password">修改密码</el-menu-item>
            <el-menu-item @click.native="logout">退出登录</el-menu-item>
          </el-sub-menu>
        </el-menu>
      </div>

      <!-- 右侧内容区域保持不变 -->
      <div class="manager-main-right">
        <router-view @updateUser="updateUser" />
      </div>
    </div>
  </div>
</template>

<script setup>
import {HomeFilled, UserFilled} from "@element-plus/icons-vue";
import {reactive} from "vue";
import router from "@/router";

const data = reactive({
  user:  JSON.parse(localStorage.getItem('xm-user') || '{}')
})
const goToPerson = () => {
	if (data.user.role === 'admin') {
		router.push("/manager/pAdmin")
	}
	if (data.user.role === 'user') {
		router.push("/manager/pUser")
	}

}
const logout = () => {
  localStorage.removeItem('user')
  router.push('/login')
}

const updateUser = () => {
  data.user = JSON.parse(localStorage.getItem('xm-user') || '{}')   // 重新获取下用户的最新信息
}
</script>

<style scoped>
@import '@/assets/css/manager.css';
</style>
