<template>
  <div class="home-container">
    <!-- 欢迎语和时间卡片 -->
    <div class="header-cards">
      <div class="greeting-card">
        <div class="greeting-content">
          <i class="el-icon-user avatar"></i>
          <div class="user-info">
            <h2>您好，{{ data.user?.name }}</h2>
            <p>欢迎使用 Daily个人助理</p>
          </div>
        </div>
        <div class="weather-info" v-if="data.weather">
          <el-tooltip :content="data.weather.tips">
            <div>
              <p>{{ data.weather.city }} {{ data.weather.temperature }}°C</p>
              <p>{{ data.weather.weather }}</p>
            </div>
          </el-tooltip>
        </div>
      </div>
      <div class="time-card">
        <div class="time">{{ data.currentTime }}</div>
        <div class="date">{{ data.currentDate }}</div>
      </div>
    </div>

    <!-- 轮播图 -->
    <div class="carousel-section">
      <el-carousel
          :interval="4000"
          height="300px"
          indicator-position="outside"
          :autoplay="true"
          type="card"
      >
        <el-carousel-item v-for="(item, index) in data.carouselItems" :key="index">
          <div class="carousel-card" @click="handleCarouselClick(item)">
            <div class="carousel-image" :style="{ backgroundImage: `url(${item.image})` }">
              <div class="carousel-content">
                <h3>{{ item.title }}</h3>
                <p>{{ item.desc }}</p>
              </div>
            </div>
          </div>
        </el-carousel-item>
      </el-carousel>
    </div>

    <!-- 快速操作卡片 -->
    <div class="quick-actions">
      <el-row :gutter="20">
        <el-col :span="6" v-for="(action, index) in data.quickActions" :key="index">
          <div class="action-card" @click="handleQuickAction(action.route)">
            <i :class="action.icon"></i>
            <h3>{{ action.title }}</h3>
            <p>{{ action.description }}</p>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 数据概览卡片 -->
    <div class="data-overview">
      <el-row :gutter="20">
        <el-col :span="8" v-for="(stat, index) in data.statistics" :key="index">
          <div class="stat-card">
            <div class="stat-icon" :style="{ background: stat.color }">
              <i :class="stat.icon"></i>
            </div>
            <div class="stat-info">
              <h4>{{ stat.title }}</h4>
              <div class="stat-value">{{ stat.value }}</div>
              <div class="stat-change" :class="stat.trend">
                <i :class="stat.trend === 'up' ? 'el-icon-top' : 'el-icon-bottom'"></i>
                {{ stat.changeRate }}%
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

  </div>
</template>

<script setup>
import { reactive, onMounted, onUnmounted } from "vue"
import { useRouter } from 'vue-router'
import * as echarts from "echarts"
import dayjs from 'dayjs'
import 'dayjs/locale/zh-cn'
import { ElCarousel, ElCarouselItem } from 'element-plus'
dayjs.locale('zh-cn')

const router = useRouter()

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  currentTime: '',
  currentDate: '',
  weather: null,
  quickActions: [
    {
      title: '待办事项',
      description: '查看和管理您的任务',
      icon: 'el-icon-check',
      route: '/manager/task'
    },
    {
      title: '健康记录',
      description: '记录您的健康数据',
      icon: 'el-icon-first-aid-kit',
      route: '/manager/healthProfile'
    },
    {
      title: '阅读管理',
      description: '管理您的阅读进度',
      icon: 'el-icon-money',
      // route: '/manager/financal'
    },
    {
      title: '运动记录',
      description: '设置运动计划',
      icon: 'el-icon-bell',
      // route: '/manager/reminder'
    }
  ],
  statistics: [
    {
      title: '待办任务',
      value: '5',
      changeRate: '12',
      trend: 'down',
      icon: 'el-icon-tickets',
      color: '#409EFF'
    },
    {
      title: '本月阅读量',
      value: '2,450',
      changeRate: '8',
      trend: 'up',
      icon: 'el-icon-wallet',
      color: '#67C23A'
    },
    {
      title: '健康指数',
      value: '92',
      changeRate: '5',
      trend: 'up',
      icon: 'el-icon-medal',
      color: '#E6A23C'
    }
  ],
  carouselItems: [
    {
      title: "待办事项提醒",
      desc: "您有3个高优先级任务待处理",
      image: "https://img.freepik.com/free-photo/checklist-checkbox-survey-plan_53876-123696.jpg",
      route: '/manager/task'
    },
    {
      title: "健康监测",
      desc: "今日运动目标已完成80%",
      image: "https://img.freepik.com/free-photo/medical-banner-with-heart-rate-line_53876-129275.jpg",
      route: '/manager/healthProfile'
    },
    {
      title: "财务概览",
      desc: "本月支出分析已生成",
      image: "https://img.freepik.com/free-photo/business-finance-employment-female-successful-entrepreneurs-concept_155003-1879.jpg",
      route: '/manager/financal'
    }
  ]
})

// 更新时间
let timeInterval
const updateTime = () => {
  const now = dayjs()
  data.currentTime = now.format('HH:mm:ss')
  data.currentDate = now.format('YYYY年MM月DD日 dddd')
}

// 获取天气信息
const fetchWeather = async () => {
  try {
    // 这里应该调用实际的天气API
    data.weather = {
      city: '北京市',
      temperature: '25',
      weather: '晴',
      tips: '今天天气不错，适合外出活动'
    }
  } catch (error) {
    console.error('获取天气信息失败:', error)
  }
}

// 初始化任务完成情况图表
const initTaskChart = () => {
  const chart = echarts.init(document.getElementById('task-chart'))
  const option = {
    tooltip: {
      trigger: 'axis'
    },
    xAxis: {
      type: 'category',
      data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
    },
    yAxis: {
      type: 'value',
      name: '完成任务数'
    },
    series: [{
      data: [5, 7, 3, 9, 4, 6, 8],
      type: 'line',
      smooth: true,
      areaStyle: {
        opacity: 0.3
      },
      lineStyle: {
        width: 3
      },
      itemStyle: {
        borderWidth: 2
      }
    }]
  }
  chart.setOption(option)
  return chart
}

// 初始化健康指标图表
const initHealthChart = () => {
  const chart = echarts.init(document.getElementById('health-chart'))
  const option = {
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['步数', '心率']
    },
    xAxis: {
      type: 'category',
      data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
    },
    yAxis: [
      {
        type: 'value',
        name: '步数',
        min: 0,
        max: 10000
      },
      {
        type: 'value',
        name: '心率',
        min: 60,
        max: 100
      }
    ],
    series: [
      {
        name: '步数',
        type: 'bar',
        data: [8000, 7500, 9000, 6800, 8500, 7000, 9500]
      },
      {
        name: '心率',
        type: 'line',
        yAxisIndex: 1,
        data: [75, 72, 78, 74, 77, 73, 76]
      }
    ]
  }
  chart.setOption(option)
  return chart
}

// 处理快速操作点击
const handleQuickAction = (route) => {
  router.push(route)
}

// 处理轮播图点击
const handleCarouselClick = (item) => {
  if (item.route) {
    router.push(item.route)
  }
}

let taskChart = null
let healthChart = null

onMounted(() => {
  updateTime()
  timeInterval = setInterval(updateTime, 1000)
  fetchWeather()

  // taskChart = initTaskChart()
  // healthChart = initHealthChart()

  // window.addEventListener('resize', () => {
  //   taskChart?.resize()
  //   healthChart?.resize()
  // })
})

onUnmounted(() => {
  if (timeInterval) {
    clearInterval(timeInterval)
  }
  // taskChart?.dispose()
  // healthChart?.dispose()
})
</script>

<style scoped>
.home-container {
  padding: 20px;
  background: #f5f7fa;
  min-height: calc(100vh - 60px);
}

/* 头部卡片样式 */
.header-cards {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
}

.greeting-card {
  flex: 3;
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.greeting-content {
  display: flex;
  align-items: center;
  gap: 20px;
}

.avatar {
  font-size: 48px;
  color: #409EFF;
}

.user-info h2 {
  margin: 0;
  font-size: 24px;
  color: #303133;
}

.user-info p {
  margin: 5px 0 0;
  color: #909399;
}

.weather-info {
  text-align: right;
  color: #606266;
}

.time-card {
  flex: 1;
  background: #409EFF;
  border-radius: 12px;
  padding: 20px;
  color: white;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.time {
  font-size: 32px;
  font-weight: bold;
  margin-bottom: 10px;
}

.date {
  font-size: 14px;
}

/* 快速操作卡片样式 */
.quick-actions {
  margin-bottom: 20px;
}

.action-card {
  background: white;
  border-radius: 12px;
  padding: 20px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.action-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
}

.action-card i {
  font-size: 32px;
  color: #409EFF;
  margin-bottom: 10px;
}

.action-card h3 {
  margin: 10px 0;
  color: #303133;
}

.action-card p {
  color: #909399;
  font-size: 14px;
}

/* 数据统计卡片样式 */
.stat-card {
  background: white;
  border-radius: 12px;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.stat-icon i {
  font-size: 24px;
  color: white;
}

.stat-info h4 {
  margin: 0;
  color: #909399;
  font-weight: normal;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
  margin: 5px 0;
}

.stat-change {
  font-size: 14px;
  display: flex;
  align-items: center;
  gap: 5px;
}

.stat-change.up {
  color: #67C23A;
}

.stat-change.down {
  color: #F56C6C;
}

/* 轮播图样式 */
.carousel-section {
  margin: 20px 0;
}

.carousel-card {
  height: 100%;
  border-radius: 12px;
  overflow: hidden;
  cursor: pointer;
}

.carousel-image {
  height: 100%;
  background-size: cover;
  background-position: center;
  position: relative;
}

.carousel-content {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 20px;
  background: linear-gradient(transparent, rgba(0, 0, 0, 0.7));
  color: white;
}

.carousel-content h3 {
  margin: 0 0 10px;
  font-size: 24px;
}

.carousel-content p {
  margin: 0;
  opacity: 0.8;
}

/* 图表区域样式 */
.charts-section {
  margin-top: 20px;
}

.chart-card {
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.chart-card h3 {
  margin: 0 0 20px;
  color: #303133;
}

.chart {
  height: 300px;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .header-cards {
    flex-direction: column;
  }

  .time-card {
    height: 100px;
  }

  .el-col {
    margin-bottom: 20px;
  }
}
</style>