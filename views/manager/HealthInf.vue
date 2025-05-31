<template>
  <div class="health-Inf-container">
    <!-- 健康档案卡片 -->
    <el-card class="Inf-card">
      <template #header>
        <div class="card-header">
          <span class="title">个人健康档案</span>
          <el-icon :size="40" color="#ff9933"><User /></el-icon>
        </div>
      </template>

      <!-- 健康数据模块 -->
      <div class="health-metrics">
        <!-- 身高 -->
        <div class="metric-item" @click="editField('height')">
          <div class="metric-label">
            <el-icon :size="32"><Sort /></el-icon>
            <span>身高 (cm)</span>
          </div>
          <div class="metric-value">
            <span>{{ healthData.height || '--' }}</span>
            <el-icon :size="28" color="#666"><Edit /></el-icon>
          </div>
        </div>

        <!-- 体重 -->
        <div class="metric-item" @click="editField('weight')">
          <div class="metric-label">
            <el-icon :size="32"><DataLine /></el-icon>
            <span>体重 (kg)</span>
          </div>
          <div class="metric-value">
            <span>{{ healthData.weight || '--' }}</span>
            <el-icon :size="28" color="#666"><Edit /></el-icon>
          </div>
        </div>

        <!-- BMI状态指示器 -->
        <div class="bmi-indicator" :class="bmiStatusClass">
          <div class="bmi-value">
            <span class="number">{{ bmiValue }}</span>
            <span class="unit">BMI</span>
          </div>
          <div class="bmi-info">
            <span class="category">{{ bmiCategory }}</span>
            <span class="range">{{ healthyRange }}</span>
          </div>
        </div>
      </div>
    </el-card>

    <!-- 编辑弹窗 -->
    <el-dialog
        v-model="showEditDialog"
        :title="`编辑${editingField === 'height' ? '身高' : '体重'}`"
        width="400px"
        center
    >
      <div class="edit-dialog-content">
        <el-input
            v-model.number="inputValue"
            type="number"
            :placeholder="`当前${editingField === 'height' ? '身高' : '体重'}：${healthData[editingField] || '未填写'}`"
            style="width: 200px; margin-right: 10px"
        />
        <span class="unit">{{ editingField === 'height' ? 'cm' : 'kg' }}</span>
      </div>

      <template #footer>
        <el-button @click="showEditDialog = false">取消</el-button>
        <el-button type="primary" @click="saveEdit">确认修改</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {ref, computed, onMounted, reactive} from 'vue'
import {ElMessage, ElMessageBox} from 'element-plus'
import {
  User,
  Edit,
  Sort,
  DataLine
} from '@element-plus/icons-vue'
import request from "@/utils/request.js";

const data = reactive({
  user:  JSON.parse(localStorage.getItem('xm-user') || '{}'),
})
const userId = data.user.id
// 健康数据
const healthData = ref({
  userId: userId,
  height: null,
  weight: null
})

// 编辑相关状态
const showEditDialog = ref(false)
const editingField = ref('height')
const inputValue = ref('')

// 计算BMI
const bmiValue = computed(() => {
  if (!healthData.value.height || !healthData.value.weight) return 0
  return (healthData.value.weight / ((healthData.value.height / 100) ** 2)).toFixed(1)
})

// BMI分类
const bmiCategory = computed(() => {
  const bmi = parseFloat(bmiValue.value)
  if (bmi < 18.5) return '体重过轻'
  if (bmi < 24) return '健康体重'
  if (bmi < 28) return '超重'
  return '肥胖'
})

// 健康范围提示
const healthyRange = computed(() => {
  if (!healthData.value.height) return '请先填写身高'
  const min = (18.5 * (healthData.value.height / 100) ** 2).toFixed(1)
  const max = (24 * (healthData.value.height / 100) ** 2).toFixed(1)
  return `健康范围：${min}kg - ${max}kg`
})

// BMI状态样式
const bmiStatusClass = computed(() => {
  const bmi = parseFloat(bmiValue.value)
  return {
    'underweight': bmi < 18.5,
    'healthy': bmi >= 18.5 && bmi < 24,
    'overweight': bmi >= 24 && bmi < 28,
    'obese': bmi >= 28
  }
})

// 初始化加载数据
onMounted(async () => {
  try {
    // // 模拟API调用
    // const mockData = {
    //   Userid: 1,
    //   height: 175,
    //   weight: 65
    // }
    //
    // healthData.value = mockData


    const res = await request.get(`/healthInf/selectById/${userId}`)
    if (res.data) {
      healthData.value = res.data
    }

  } catch (error) {
    ElMessage.error('数据加载失败')
    // 无数据时提示创建
    if (!healthData.value.height || !healthData.value.weight) {
      ElMessageBox.confirm('尚未创建健康档案，是否现在填写？', '提示', {
        confirmButtonText: '立即填写',
        cancelButtonText: '稍后再说'
      }).then(() => {
        editField('height')
      })
    }
  }
})

// 打开编辑对话框
const editField = (field) => {
  editingField.value = field
  inputValue.value = healthData.value[field] || ''
  showEditDialog.value = true
}

// 保存编辑
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
}
</script>

<style>
.health-Inf-container {
  max-width: 800px;
  margin: 40px auto;
  padding: 20px;
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.title {
  font-size: 24px;
  font-weight: 600;
}

.health-metrics {
  padding: 20px 0;
}

.metric-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 0;
  border-bottom: 1px solid #eee;
  cursor: pointer;
  transition: background 0.3s;
}

.metric-item:hover {
  background: #f8f8f8;
}

.metric-label {
  display: flex;
  align-items: center;
  gap: 12px;
  color: #666;
}

.metric-value {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 18px;
  font-weight: 500;
}

.bmi-indicator {
  margin-top: 30px;
  padding: 24px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  background: #f8f8f8;
  border-left: 8px solid;
}

.bmi-indicator.underweight {
  border-color: #1890ff;
}

.bmi-indicator.healthy {
  border-color: #52c41a;
}

.bmi-indicator.overweight {
  border-color: #faad14;
}

.bmi-indicator.obese {
  border-color: #ff4d4f;
}

.bmi-value {
  text-align: center;
  margin-right: 40px;
}

.bmi-value .number {
  font-size: 36px;
  font-weight: 700;
  display: block;
  line-height: 1.2;
}

.bmi-value .unit {
  font-size: 16px;
  color: #666;
}

.bmi-info .category {
  display: block;
  font-size: 18px;
  margin-bottom: 8px;
  font-weight: 500;
}

.bmi-info .range {
  font-size: 14px;
  color: #666;
}

.edit-dialog-content {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px 0;
}

.edit-dialog-content .unit {
  color: #666;
  margin-left: 10px;
}
</style>