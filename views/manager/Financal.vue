<template>
  <div class="container">
    <div class="chart-container">
      <!-- 收支饼图 -->
      <div id="pieChart" class="chart"></div>
      <!-- 理财收益折线图 -->
      <div id="investmentChart" class="chart"></div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, reactive } from 'vue'
import * as echarts from 'echarts'
import request from "@/utils/request"
import dayjs from 'dayjs'

const data = reactive({
  // 收支数据
  incomeTotal: 0,
  expenseTotal: 0,
  // 投资数据
  investments: []
})

// 获取收支数据
const loadFinancialData = async () => {
  try {
    const res = await request.get('financialAccount/selectPage', {
      params: {
        pageNum: 1,
        pageSize: 1000
      }
    })

    if (res.data?.list) {
      data.incomeTotal = res.data.list
          .filter(item => item.type === '收入')
          .reduce((sum, item) => sum + parseFloat(item.balance), 0)

      data.expenseTotal = res.data.list
          .filter(item => item.type === '支出')
          .reduce((sum, item) => sum + parseFloat(item.balance), 0)

      initPieChart()
    }
  } catch (error) {
    console.error('加载收支数据失败:', error)
  }
}

// 获取投资数据
const loadInvestmentData = async () => {
  try {
    const res = await request.get('investment/selectPage', {
      params: {
        pageNum: 1,
        pageSize: 1000
      }
    })

    if (res.data?.list) {
      // 过滤出未到期的投资产品
      data.investments = res.data.list.filter(item =>
          dayjs(item.maturityDate).isAfter(dayjs())
      )
      initInvestmentChart()
    }
  } catch (error) {
    console.error('加载投资数据失败:', error)
  }
}

// 初始化饼图
const initPieChart = () => {
  const pieChart = echarts.init(document.getElementById('pieChart'))
  const option = {
    title: {
      text: '收支统计',
      left: 'center'
    },
    tooltip: {
      trigger: 'item',
      formatter: '{b}: {c} 元 ({d}%)'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [
      {
        name: '收支情况',
        type: 'pie',
        radius: '50%',
        data: [
          { value: data.incomeTotal, name: '收入' },
          { value: data.expenseTotal, name: '支出' }
        ],
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  }
  pieChart.setOption(option)
  return pieChart
}

// 初始化投资收益图表
const initInvestmentChart = () => {
  const chart = echarts.init(document.getElementById('investmentChart'))

  // 按预期收益率排序
  const sortedInvestments = [...data.investments].sort((a, b) =>
      parseFloat(b.expectedReturn) - parseFloat(a.expectedReturn)
  )

  const option = {
    title: {
      text: '理财产品收益分析',
      left: 'center'
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      },
      formatter: function(params) {
        const investment = sortedInvestments[params[0].dataIndex]
        const days = dayjs(investment.maturityDate).diff(dayjs(), 'day')
        const expectedReturn = (investment.amount * investment.expectedReturn / 100 * days / 365).toFixed(2)
        return `
          <div>
            <p><strong>${investment.productName}</strong></p>
            <p>投资金额：${investment.amount} 元</p>
            <p>预期收益率：${investment.expectedReturn}%</p>
            <p>到期日：${dayjs(investment.maturityDate).format('YYYY-MM-DD')}</p>
            <p>预期收益：${expectedReturn} 元</p>
          </div>
        `
      }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '15%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: sortedInvestments.map(item => item.productName),
      axisLabel: {
        interval: 0,
        rotate: 45
      }
    },
    yAxis: [
      {
        type: 'value',
        name: '预期收益率(%)',
        position: 'left',
        axisLabel: {
          formatter: '{value}%'
        }
      },
      {
        type: 'value',
        name: '投资金额(元)',
        position: 'right',
        axisLabel: {
          formatter: '{value}'
        }
      }
    ],
    series: [
      {
        name: '预期收益率',
        type: 'bar',
        data: sortedInvestments.map(item => parseFloat(item.expectedReturn)),
        itemStyle: {
          color: '#409EFF'
        },
        label: {
          show: true,
          position: 'top',
          formatter: '{c}%'
        }
      },
      {
        name: '投资金额',
        type: 'line',
        yAxisIndex: 1,
        data: sortedInvestments.map(item => parseFloat(item.amount)),
        itemStyle: {
          color: '#67C23A'
        },
        label: {
          show: true,
          position: 'top',
          formatter: '{c} 元'
        }
      }
    ],
    legend: {
      data: ['预期收益率', '投资金额'],
      top: 30
    }
  }

  chart.setOption(option)
  return chart
}

let pieChart = null
let investmentChart = null

// 初始化图表
const initCharts = async () => {
  await Promise.all([
    loadFinancialData(),
    loadInvestmentData()
  ])

  // 监听窗口大小变化
  window.addEventListener('resize', () => {
    pieChart?.resize()
    investmentChart?.resize()
  })
}

onMounted(() => {
  initCharts()
})
</script>

<style scoped>
.container {
  padding: 20px;
}

.chart-container {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
}

.chart {
  width: 48%;
  height: 400px;
  background: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 20px;
}

@media (max-width: 768px) {
  .chart-container {
    flex-direction: column;
  }

  .chart {
    width: 100%;
    margin-bottom: 20px;
  }
}
</style>