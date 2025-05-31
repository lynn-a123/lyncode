<template>
  <div class="container">
    <el-tabs v-model="activeValue" class="demo-tabs" @tab-change="onTabsChange">
      <!-- 运动计划标签页 -->
      <el-tab-pane label="运动计划" name="0">
        <!-- 个性化设置面板 -->
        <el-card class="settings-panel">
          <el-form label-width="120px">
            <el-form-item label="健身目标：">
              <el-select
                  v-model="userInfo.goal"
                  placeholder="请选择健身目标"
              >
                <el-option
                    v-for="(item, index) in goalOptions"
                    :key="index"
                    :label="item"
                    :value="item"
                />
              </el-select>
            </el-form-item>

            <el-form-item label="当前水平：">
              <el-select
                  v-model="userInfo.level"
                  placeholder="请选择当前水平"
              >
                <el-option
                    v-for="(item, index) in levelOptions"
                    :key="index"
                    :label="item"
                    :value="item"
                />
              </el-select>
            </el-form-item>

            <el-form-item>
              <el-button type="primary" @click="generatePlan" class="generate-btn">
                生成专属计划
              </el-button>
            </el-form-item>
          </el-form>
        </el-card>

        <!-- 生成的计划内容 -->
        <el-card class="plan-container" v-if="generatedPlan">
          <template #header>
            <div class="plan-title">您的个性化运动计划</div>
          </template>
          <div class="plan-content">{{ generatedPlan }}</div>

          <el-timeline class="schedule">
            <el-timeline-item
                v-for="(day, index) in personalizedPlan"
                :key="index"
                :timestamp="`第${index + 1}天`"
                placement="top"
            >
              <el-card>
                <h4>{{ day.workout }}</h4>
                <p>持续时间：{{ day.duration }}分钟</p>
              </el-card>
            </el-timeline-item>
          </el-timeline>
        </el-card>
      </el-tab-pane>

      <!-- 训练课程标签页 -->
      <el-tab-pane label="训练课程" name="1">
        <div class="course-list">
          <el-row :gutter="20">
            <el-col
                v-for="course in courses"
                :key="course.id"
                :xs="24"
                :sm="12"
                :md="8"
                :lg="6"
            >
              <el-card
                  class="course-card"
                  shadow="hover"
                  @click="playVideo(course)"
              >
                <video
                    class="course-video"
                    :poster="course.cover"
                    style="width:100%; border-radius:4px"
                >
                  <source :src="course.videoUrl" type="video/mp4">
                </video>
                <div class="course-info">
                  <h4 class="course-title">{{ course.title }}</h4>
                  <div class="course-meta">
                    <el-tag type="info">{{ course.type }}</el-tag>
                    <span style="margin-left:10px">{{ course.duration }}</span>
                  </div>
                </div>
              </el-card>
            </el-col>
          </el-row>

          <el-dialog
              v-model="showVideoDialog"
              :title="currentVideo.title"
              width="80%"
              @closed="handleVideoClose"
          >
            <video
                ref="videoPlayer"
                :key="currentVideo.id"
                controls
                autoplay
                style="width: 100%; border-radius: 8px"
            >
              <source :src="currentVideo.videoUrl" type="video/mp4">
              您的浏览器不支持视频播放
            </video>
            <template #footer>
              <span class="dialog-footer">
                <el-button @click="showVideoDialog = false">关闭</el-button>
              </span>
            </template>
          </el-dialog>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'

// 饮食建议配置
const dietSuggestions = {
  减脂: '饮食建议：建议搭配减脂饮食，根据饮食计划控制每日热量摄入，增加蛋白质摄入（每公斤体重1.2-1.6g），减少精制碳水，多食用瘦肉、鱼类和蔬菜。',
  增肌: '饮食建议：建议采用高蛋白饮食（每公斤体重1.6-2.2g），每日摄入足够碳水化合物（4-6g/kg）和健康脂肪，根据饮食计划保证热量盈余（300-500大卡/日），训练后及时补充快碳+蛋白质。',
  塑形: '饮食建议：建议均衡饮食（蛋白质1.2-1.8g/kg），适当控制碳水化合物（3-4g/kg），多食用膳食纤维，根据饮食计划保持轻度热量缺口（200-300大卡/日），注意营养均衡。',
  健康维持: '饮食建议：建议采用均衡膳食（碳水50%-55%、蛋白质15%-20%、脂肪25%-30%），保证各类营养素摄入，多吃天然食物，控制加工食品，适量补充维生素和矿物质。'
}

// 计划模板
const planTemplates = {
  减脂: {
    初级: [
      { workout: "开合跳+慢跑", duration: 30 },
      { workout: "跳绳+核心训练", duration: 30 },
      { workout: "休息日", duration: 0 },
      { workout: "HIIT训练", duration: 25 },
      { workout: "有氧舞蹈", duration: 35 }
    ],
    中级: [
      { workout: "开合跳+慢跑", duration: 45 },
      { workout: "跳绳+核心训练", duration: 45 },
      { workout: "休息日", duration: 0 },
      { workout: "HIIT训练", duration: 40 },
      { workout: "有氧舞蹈", duration: 50 }
    ],
    高级: [
      { workout: "开合跳+慢跑", duration: 60 },
      { workout: "跳绳+核心训练", duration: 60 },
      { workout: "休息日", duration: 0 },
      { workout: "HIIT训练", duration: 50 },
      { workout: "有氧舞蹈", duration: 60 }
    ]
  },
  增肌: {
    初级: [
      { workout: "胸部训练+三头", duration: 45 },
      { workout: "背部训练+二头", duration: 45 },
      { workout: "腿部训练", duration: 50 },
      { workout: "肩部训练", duration: 40 },
      { workout: "休息日", duration: 0 }
    ],
    中级: [
      { workout: "胸部超级组+负重训练", duration: 60 },
      { workout: "背部复合动作+划船", duration: 60 },
      { workout: "腿部力量训练", duration: 65 },
      { workout: "肩部推举+三角肌孤立", duration: 50 },
      { workout: "手臂专项训练", duration: 45 },
      { workout: "主动恢复（瑜伽）", duration: 30 }
    ],
    高级: [
      { workout: "胸部递减组训练", duration: 75 },
      { workout: "背部巨人组训练", duration: 75 },
      { workout: "腿部极限力量训练", duration: 80 },
      { workout: "肩部复合金字塔训练", duration: 60 },
      { workout: "全身弱点突破", duration: 90 },
      { workout: "泡沫轴放松", duration: 20 }
    ]
  },
  塑形: {
    初级: [
      { workout: "全身循环训练", duration: 35 },
      { workout: "核心塑形", duration: 25 },
      { workout: "低强度有氧", duration: 30 },
      { workout: "基础瑜伽", duration: 40 },
      { workout: "休息日", duration: 0 }
    ],
    中级: [
      { workout: "上下肢分化训练", duration: 45 },
      { workout: "HIIT核心强化", duration: 30 },
      { workout: "普拉提训练", duration: 50 },
      { workout: "战绳循环训练", duration: 35 },
      { workout: "动态拉伸", duration: 20 }
    ],
    高级: [
      { workout: "功能性复合训练", duration: 60 },
      { workout: "芭蕾把杆塑形", duration: 45 },
      { workout: "TRX全身训练", duration: 50 },
      { workout: "高低强度间歇", duration: 40 },
      { workout: "平衡训练+冥想", duration: 30 }
    ]
  },
  健康维持: {
    初级: [
      { workout: "快走+拉伸", duration: 30 },
      { workout: "基础力量训练", duration: 25 },
      { workout: "八段锦", duration: 20 },
      { workout: "休闲游泳", duration: 40 },
      { workout: "自由活动日", duration: 0 }
    ],
    中级: [
      { workout: "骑行+核心", duration: 45 },
      { workout: "太极训练", duration: 30 },
      { workout: "自重循环训练", duration: 35 },
      { workout: "舞蹈训练", duration: 40 },
      { workout: "筋膜放松", duration: 15 }
    ],
    高级: [
      { workout: "综合体能训练", duration: 50 },
      { workout: "户外徒步", duration: 120 },
      { workout: "交叉训练组合", duration: 60 },
      { workout: "动态瑜伽流", duration: 45 },
      { workout: "团体运动日", duration: 90 }
    ]
  }
}

// 响应式数据
const activeValue = ref('0')
const userInfo = reactive({
  goal: "减脂",
  level: "初级",
  time: 9999
})
const goalOptions = ["减脂", "增肌", "塑形", "健康维持"]
const levelOptions = ["初级", "中级", "高级"]
const personalizedPlan = ref([])
const generatedPlan = ref("")

// 课程相关数据
const courses = ref([
  {
    id: 1,
    title: "15分钟跳跃有氧",
    duration: "15分钟",
    type: "有氧训练",
    videoUrl: "/static/videos/11.mp4"
  },
  {
    id: 2,
    title: "上肢力量入门训练",
    duration: "15分钟",
    type: "力量训练",
    videoUrl: "/static/videos/12.mp4"
  },
  {
    id: 3,
    title: "腰腹核心力量训练",
    duration: "20分钟",
    type: "核心训练",
    videoUrl: "/static/videos/13.mp4"
  },
  {
    id: 4,
    title: "健身气功八段锦",
    duration: "12分钟",
    type: "传统养生",
    videoUrl: "/static/videos/14.mp4"
  }
])
const showVideoDialog = ref(false)
const currentVideo = reactive({
  id: null,
  title: '',
  videoUrl: ''
})
const videoPlayer = ref(null)

// 方法
const generatePlan = () => {
  const plan = planTemplates[userInfo.goal]?.[userInfo.level]
  if (plan) {
    personalizedPlan.value = adjustPlanDuration(plan)
    const dietAdvice = dietSuggestions[userInfo.goal] || ''
    generatedPlan.value =
        `根据您的${userInfo.goal}目标（${userInfo.level}水平），我们为您推荐以下计划：\n${dietAdvice}\n每日训练安排：`
  }
}

const adjustPlanDuration = (plan) => {
  return plan.map(day => ({
    ...day,
    duration: day.duration > 0 ? Math.min(day.duration, userInfo.time) : 0
  }))
}

const playVideo = (course) => {
  currentVideo.id = course.id
  currentVideo.title = course.title
  currentVideo.videoUrl = course.videoUrl
  showVideoDialog.value = true
}

const handleVideoClose = () => {
  if (videoPlayer.value) {
    videoPlayer.value.pause()
    videoPlayer.value.currentTime = 0
  }
}
</script>

<style scoped>
.container {
  margin: 20px auto;
  padding: 20px;
  max-width: 100vw;
  min-width: 1200px;
}

.settings-panel {
  margin-bottom: 20px;
  background: linear-gradient(145deg, #f8f9fa 0%, #e9ecef 100%);
  border-radius: 12px;
  padding: 20px;
}

.generate-btn {
  width: 100%;
  margin-top: 20px;
  height: 45px;
  font-size: 16px;
  letter-spacing: 1px;
}

.plan-container {
  margin-top: 30px;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}

.plan-title {
  font-size: 24px;
  font-weight: 600;
  color: #2c3e50;
  padding: 10px 0;
  text-align: center;
}

.plan-content {
  white-space: pre-line;
  font-size: 16px;
  color: #34495e;
  margin-bottom: 30px;
  padding: 0 20px;
  line-height: 1.8;
  background: #f8f9fa;
  border-radius: 8px;
  padding: 20px;
}

.schedule {
  margin-top: 25px;
}

:deep(.el-timeline-item__timestamp) {
  color: #2980b9;
  font-weight: 500;
}

.course-card {
  margin-bottom: 20px;
  transition: transform 0.3s;
  border-radius: 10px;
  overflow: hidden;
}

.course-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
}

.course-video {
  height: 180px;
  object-fit: cover;
  background: #000;
}

.course-title {
  margin: 10px 0;
  font-size: 16px;
  color: #2c3e50;
  height: 50px;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.course-meta {
  display: flex;
  align-items: center;
  margin-top: 10px;
  color: #666;
}

:deep(.el-dialog__header) {
  border-bottom: 1px solid #eee;
}

:deep(.el-dialog__body) {
  padding: 20px;
}
</style>