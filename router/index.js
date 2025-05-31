import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', redirect: '/login'},
    {
      path: '/manager',
      name: 'Manager',
      component: () => import('@/views/Manager.vue'),
      redirect: '/manager/login',
      children: [
        { path: 'password', meta: { name: '修改密码' }, component: () => import('@/views/manager/Password.vue')},
        { path: 'home', meta: { name: '系统首页' }, component: () => import('@/views/manager/Home.vue')},
		{ path: 'admin', meta: { name: '管理员' }, component: () => import("@/views/manager/Admin.vue") },
		{ path: 'user', meta: { name: '用户' }, component: () => import("@/views/manager/User.vue") },
		{ path: 'task', meta: { name: '任务记录' }, component: () => import("@/views/manager/Task.vue") },
		{ path: 'reminder', meta: { name: '任务提醒' }, component: () => import("@/views/manager/Reminder.vue") },
		{ path: 'healthProfile', meta: { name: '健康档案' }, component: () => import("@/views/manager/HealthProfile.vue") },
		{ path: 'medicalReport', meta: { name: '检查报告' }, component: () => import("@/views/manager/MedicalReport.vue") },
		{ path: 'medicationReminder', meta: { name: '服药提醒' }, component: () => import("@/views/manager/MedicationReminder.vue") },
        { path: 'financial', meta: { name: '财富档案' }, component: () => import("@/views/manager/Financal.vue") },
		{ path: 'financialAccount', meta: { name: '记账日记' }, component: () => import("@/views/manager/FinancialAccount.vue") },
		{ path: 'investment', meta: { name: '理财产品' }, component: () => import("@/views/manager/Investment.vue") },
		{ path: 'pAdmin', meta: { name: '个人信息' }, component: () => import("@/views/manager/pAdmin.vue")  },
		{ path: 'pUser', meta: { name: '个人信息' }, component: () => import("@/views/manager/pUser.vue")  },

          { path: 'wordType', meta: { name: '文档分类管理' }, component: () => import("@/views/manager/WordType.vue") },
          { path: 'wordManage', meta: { name: '文档存储管理' }, component: () => import("@/views/manager/WordManage.vue") },
          { path: 'WordSelect', meta: { name: '文档查询' }, component: () => import("@/views/manager/WordSelect.vue") },
          { path: 'bookType', meta: { name: '书籍分类管理' }, component: () => import("@/views/manager/BookType.vue") },
          { path: 'BookManage', meta: { name: '书籍信息管理' }, component: () => import("@/views/manager/BookManage.vue") },
          { path: 'BookSelect', meta: { name: '书籍阅读' }, component: () => import("@/views/manager/BookSelect.vue") },
          { path: 'HandBook', meta: { name: '电子手账' }, component: () => import("@/views/manager/HandBook.vue")  },
          { path: 'HealthInf', meta: { name: '健康信息' }, component: () => import("@/views/manager/HealthInf.vue")  },
          { path: 'Food', meta: { name: '饮食管理' }, component: () => import("@/views/manager/Food.vue")  },
          { path: 'Sports', meta: { name: '运动管理' }, component: () => import("@/views/manager/Sports.vue")  }

      ]
    },
    { path: '/login', component: () => import('@/views/Login.vue')},
	{ path: '/register', component: () => import("@/views/Register.vue") },
    { path: '/404', component: () => import('@/views/404.vue')},
    { path: '/:pathMatch(.*)', redirect: '/404', hidden: true }
  ]
})

export default router
