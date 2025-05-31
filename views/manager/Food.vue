<template>
  <div class="container">
    <el-tabs v-model="activeValue" class="demo-tabs" @tab-change="onTabsChange">
      <!-- 专属计划标签页 -->
      <el-tab-pane label="专属计划" name="0">
        <div class="health-card">
          <!-- 健康档案部分 -->
          <div class="user-info">
            <h2 class="title">健康档案</h2>
            <div class="info-item">
              <div class="editable-field" @click="editField('height')">
                <span>身高：{{ healthData.height }}cm </span>
                <el-icon><Edit /></el-icon>
              </div>
              <div class="editable-field" @click="editField('weight')">
                <span>体重：{{ healthData.weight }}kg</span>
                <el-icon><Edit /></el-icon>
              </div>
            </div>
            <div class="info-item">
              <p>BMI：{{ calculateBMI().toFixed(1) }}（{{ getBMICategory() }}）</p>
              <p>建议每日摄入：{{ dailyCalories }}大卡</p>
              <p class="health-tip">{{ healthAdvice }}</p>
            </div>
          </div>

          <!-- 编辑对话框 -->
          <el-dialog v-model="showEditDialog" :title="`编辑${editingField === 'height' ? '身高（cm）' : '体重（kg）'}`" width="30%">
            <el-input v-model="inputValue" type="number"
                      :placeholder="editingField === 'height' ? '请输入身高' : '请输入体重'"/>
            <template #footer>
              <el-button type="primary" @click="saveEdit">保存</el-button>
            </template>
          </el-dialog>

          <!-- 计划生成器 -->
          <div class="plan-generator">
            <h3 class="sub-title">选择计划类型</h3>
            <div class="plan-cards">
              <el-card
                  v-for="(item, index) in goalOptions"
                  :key="index"
                  class="plan-card"
                  :class="{ active: goalIndex === index }"
                  @click="selectPlan(index)"
              >
                <div class="card-icon">
                  <component :is="getPlanIcon(index)" style="width: 2em; height: 2em" />
                </div>
                <h4 class="card-title">{{ item }}</h4>
                <pre class="card-desc">{{ getPlanDesc(index) }}</pre>
              </el-card>
            </div>

            <div class="intensity-select">
              <h4>选择强度：{{ intensityText }}</h4>
              <div class="stars">
                <el-rate v-model="intensity" :max="3" :colors="['#99A9BF', '#F7BA2A', '#FF9900']" />
              </div>
            </div>

            <el-button type="primary" class="generate-btn" @click="generatePlan">生成专属计划</el-button>
          </div>

          <!-- 每日计划 -->
          <div class="daily-plan" v-if="dailyPlan">
            <h3 class="sub-title">每日饮食计划</h3>
            <div class="meal-plan">
              <el-card v-for="(value, key) in dailyPlan" :key="key" class="meal-item">
                <template #header>
                  <div class="meal-header">
                    <el-icon><Food /></el-icon>
                    <span class="meal-title">{{ mealMap[key] }}</span>
                  </div>
                </template>
                <span class="meal-calories">{{ value }}大卡</span>
              </el-card>
            </div>
          </div>
        </div>
      </el-tab-pane>

      <!-- 电子菜谱标签页 -->
      <el-tab-pane label="电子菜谱" name="1">
        <div class="recipe-list">
          <el-card
              v-for="(item, index) in breakfastRecipes"
              :key="index"
              class="recipe-card"
              shadow="hover"
          >
            <img :src="item.image" class="recipe-image" />
            <div class="recipe-info">
              <h4 class="recipe-title">{{ item.title }}</h4>
              <div class="meta-info">
                <span>🔥 {{ item.calories }}大卡</span>
                <span>⏱ {{ item.time }}分钟</span>
              </div>
              <div class="action-bar">
                <el-button size="small" @click="viewRecipeDetail(item)">查看做法</el-button>
                <el-button size="small" @click="shareRecipe(item)">分享</el-button>
              </div>
            </div>
          </el-card>
        </div>
      </el-tab-pane>
    </el-tabs>

    <!-- 食谱详情弹窗 -->
    <el-dialog v-model="showRecipeDetail" title="食谱详情" width="60%">
      <div class="detail-content" v-if="currentRecipe">
        <video :src="currentRecipe.video" controls autoplay class="video-player"></video>
        <el-divider />
        <div class="ingredients">
          <h4>所需材料：</h4>
          <ul>
            <li v-for="(ing, i) in currentRecipe.ingredients" :key="i">{{ ing }}</li>
          </ul>
        </div>
        <el-divider />
        <div class="steps">
          <h4>制作步骤：</h4>
          <div v-for="(step, j) in currentRecipe.steps" :key="j" class="step-item">
            <span class="step-index">{{ j+1 }}.</span>
            <span class="step-text">{{ step }}</span>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import {ref, computed, reactive, onMounted} from 'vue'
import { watch } from 'vue'
import {
  Edit,
  Food,
  IceCream,
  IceDrink,
  CoffeeCup
} from '@element-plus/icons-vue'
import {ElMessage, ElMessageBox} from 'element-plus'
import request from "@/utils/request.js";

// 用户数据
const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
})
const userId = data.user.id
// 响应式数据
const activeValue = ref('0')
const healthData = ref({
  userId: userId,
  height: null,
  weight: null
})
const goalIndex = ref(0)
const goalOptions = ref(['减肥', '增肌', '维持'])
const intensity = ref(2)
const dailyCalories = ref(2000)
const showRecipeDetail = ref(false)
const currentRecipe = ref(null)
const dailyPlan = ref(null)
const showEditDialog = ref(false)
const editingField = ref('')
const inputValue = ref('')

const mealMap = {
  breakfast: '早餐',
  lunch: '午餐',
  dinner: '晚餐'
}
onMounted(async () => {
  try {

    const res = await request.get(`/healthInf/selectById/${userId}`)
    if (res.data) {
      healthData.value = res.data
    }

  } catch (error) {
    ElMessage.error('用户还没有健康数据')
  }
})
// 编辑相关方法
const editField = (field) => {
  editingField.value = field
  inputValue.value = healthData.value[field].toString()
  showEditDialog.value = true
}

const saveEdit = async () => {
  if (!inputValue.value || isNaN(inputValue.value)) {
    ElMessage.warning('请输入有效数字')
    return
  }
  const numericValue = parseFloat(inputValue.value)
  if (numericValue <= 0) {
    ElMessage.warning('数值必须大于0')
    return
  }

  healthData.value[editingField.value] = numericValue
  showEditDialog.value = false
  try {
    // 发送PUT请求更新数据
    const res = await request.put('/healthInf/update', healthData.value)
    if (res.code === '200') {
      showEditDialog.value = false
      ElMessage.success('修改成功')
    }
  } catch (error) {
    ElMessage.error('修改失败')
  }
  generatePlan()
}

// 健康计算
const calculateBMI = () => {
  return healthData.value.weight / ((healthData.value.height / 100) ** 2)
}

const getBMICategory = () => {
  const bmi = calculateBMI()
  if (bmi < 18.5) return '过轻'
  if (bmi < 24) return '正常'
  if (bmi < 28) return '过重'
  return '肥胖'
}

// 计划生成
const selectPlan = (index) => {
  goalIndex.value = index
}

const intensityText = computed(() => ['轻度', '中等', '强力'][intensity.value - 1])

const getPlanIcon = (index) => {
  const icons = [IceCream, IceDrink, CoffeeCup]
  return icons[index]
}

const getPlanDesc = (index) => {
  return [
    '每日热量缺口约300大卡\n\n搭配减脂运动计划',
    '高蛋白增肌饮食方案\n\n搭配增肌运动计划',
    '保持当前健康状态\n\n搭配健康维持运动计划'
  ][index]
}

const generatePlan = () => {
  const bmi = calculateBMI()
  let baseCalories = 2000

  // 根据BMI和目标计算基础热量
  switch (goalIndex.value) {
    case 0: // 减肥
      if (bmi < 18.5) {
        baseCalories = 1800
      } else if (bmi < 24) {
        baseCalories = healthData.value.weight * 22
      } else {
        baseCalories = healthData.value.weight * 20
      }
      break
    case 1: // 增肌
      if (bmi < 18.5) {
        baseCalories = healthData.value.weight * 35
      } else if (bmi < 24) {
        baseCalories = healthData.value.weight * 30
      } else {
        baseCalories = healthData.value.weight * 25
      }
      break
    case 2: // 维持
      baseCalories = healthData.value.weight * 28
      break
  }

  // 根据强度调整
  const intensityFactor = intensity.value * 50
  const total = goalIndex.value === 0 ?
      baseCalories - intensityFactor :
      goalIndex.value === 1 ?
          baseCalories + intensityFactor :
          baseCalories

  dailyCalories.value = Math.round(total)

  // 餐食分配比例
  let ratios = { breakfast: 0.3, lunch: 0.4, dinner: 0.3 }
  if (bmi > 24 && goalIndex.value === 0) {
    ratios = { breakfast: 0.35, lunch: 0.4, dinner: 0.25 }
  } else if (bmi < 18.5 && goalIndex.value === 1) {
    ratios = { breakfast: 0.25, lunch: 0.45, dinner: 0.3 }
  }

  dailyPlan.value = {
    breakfast: Math.round(total * ratios.breakfast),
    lunch: Math.round(total * ratios.lunch),
    dinner: Math.round(total * ratios.dinner)
  }

  ElMessage.success('计划生成成功')
}

const healthAdvice = computed(() => {
  const bmi = calculateBMI()
  if (bmi < 18.5) return '您的体重过轻，建议增加营养摄入'
  if (bmi < 24) return '您的体重在正常范围，请保持健康习惯'
  if (bmi < 28) return '您的体重过重，建议适当控制饮食'
  return '您的体重属于肥胖范围，建议咨询专业营养师'
})

// 食谱相关
const viewRecipeDetail = (recipe) => {
  currentRecipe.value = recipe
  showRecipeDetail.value = true
}

const shareRecipe = (recipe) => {
  navigator.clipboard.writeText(`${window.location.href}?recipe=${recipe.id}`)
  ElMessage.success('已复制分享链接到剪贴板')
}

watch(showRecipeDetail, (newVal) => {
  if (!newVal) {
    // 关闭弹窗时暂停所有视频
    const videos = document.querySelectorAll('video')
    videos.forEach(video => {
      video.pause()
      video.currentTime = 0
    })
    currentRecipe.value = null
  }
})
//食谱数据
const breakfastRecipes = ref([
  {
    id: 1,
    title: '藤椒味全麦鸡胸三明治',
    image: '/static/images/breakfast1.jpg',
    calories: 350,
    time: 3,
    ingredients: ['全麦面包2片', '鸡胸肉100g', '生菜2片', '番茄3片'],
    steps: ['鸡胸肉煎熟', '蔬菜洗净', "调味", '组合食材'],
    video: '/static/videos/1.mp4'
  },
  {
    id: 2,
    title: '懒人焖饭',
    image: '/static/images/breakfast2.jpg',
    calories: 600,
    time: 1,
    ingredients: ['米饭300g', '各种配料若干'],
    steps: ['放入米饭、配料', '加水', '调味', '按下电饭煲开始煮饭'],
    video: '/static/videos/2.mp4'
  }, {
    id: 3,
    title: '疙瘩汤',
    image: '/static/images/breakfast3.jpg',
    calories: 400,
    time: 4,
    ingredients: ['鸡蛋1个', '面粉500g', '调料配料若干'],
    steps: ['全蛋打散倒入面粉中搅拌出面疙瘩', '炝锅、水煮', '调味', '甩两个全蛋'],
    video: '/static/videos/3.mp4'
  }, {
    id: 4,
    title: '鱼香肉丝',
    image: '/static/images/breakfast4.jpg',
    calories: 800,
    time: 6,
    ingredients: ['猪通脊400g、美人椒碎40g、60g笋丝、30g木耳', '火腿肠100g', '1g盐、1g味精，80g水、10g淀粉、封40g油',
      '10g番茄酱、30g酱油、38g醋、40g白糖、5g盐、2g味精、1g胡椒粉、封10g油'
    ],
    steps: ['猪肉切丝', "腌制猪肉", '炒肉', '调味'],
    video: '/static/videos/4.mp4'
  }, {
    id: 5,
    title: '番茄土豆炖牛腩',
    image: '/static/images/breakfast5.jpg',
    calories: 1000,
    time: 4,
    ingredients: ['牛肉500g', '番茄2个', ' 土豆2个', '洋葱半个', '调料若干'],
    steps: ['牛腩泡水半小时后切块', '番茄土豆洋葱调料切好', "牛肉焯水", '炒番茄土豆', '牛腩炒糖色', '牛肉和菜一起炖'],
    video: '/static/videos/5.mp4'
  }])
</script>

<style>
.container {
  padding: 20px;
  max-width: 100vw;
  margin: 0 auto;
  min-width: 1200px;
}

.health-card {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.title {
  color: #333;
  margin-bottom: 20px;
}

.info-item {
  margin: 15px 0;
  padding: 15px;
  background: #f8f8f8;
  border-radius: 6px;
}

.editable-field {
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 0;
  transition: background 0.3s;
}

.editable-field:hover {
  background: #f0f0f0;
}

.plan-cards {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
  margin: 20px 0;
}

.plan-card {
  cursor: pointer;
  transition: all 0.3s;
  text-align: center;
  padding: 20px;
}

.plan-card.active {
  border-color: #ff9933;
  background: #fff9f0;
  transform: scale(0.98);
}

.card-icon {
  margin: 10px 0;
}

.card-title {
  color: #333;
  margin: 10px 0;
}

.card-desc {
  color: #666;
  font-size: 0.9em;
}

.intensity-select {
  margin: 30px 0;
  text-align: center;
}

.generate-btn {
  width: 200px;
  margin: 20px auto;
  display: block;
}

.recipe-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.recipe-card {
  transition: transform 0.3s;
}

.recipe-card:hover {
  transform: translateY(-5px);
}

.recipe-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
  border-radius: 4px 4px 0 0;
}

.meta-info {
  display: flex;
  justify-content: space-between;
  margin: 10px 0;
  color: #666;
}

.video-player {
  width: 100%;
  height: 400px;
  border-radius: 8px;
}

.meal-plan {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
  margin-top: 20px;
}

.meal-item {
  text-align: center;
}

.meal-calories {
  font-size: 1.2em;
  color: #ff9933;
  font-weight: bold;
}

.health-tip {
  color: #e6a23c;
  margin-top: 15px;
  padding: 10px;
  background: #fdf6ec;
  border-radius: 4px;
}

.detail-content {
  max-height: 70vh;
  overflow-y: auto;
}

.step-item {
  display: flex;
  margin: 10px 0;
  padding: 8px;
  background: #f8f8f8;
  border-radius: 4px;
}

.step-index {
  color: #e6a23c;
  min-width: 30px;
  font-weight: bold;
}
</style>