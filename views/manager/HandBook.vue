<template>
  <div class="container">
    <div class="left-panel">
      <div class="controls">
        <div class="color-picker">
          <div
              v-for="(color, index) in colors"
              :key="index"
              class="color-option"
              :style="{ backgroundColor: color }"
              :class="{
              'selected': selectedColor === color,
              'white-option': color === '#FFFFFF'
            }"
              @click="selectColor(color)"
          ></div>
        </div>

        <div class="tools">
          <button class="btn" @click="toggleEraser">
            {{ isErasing ? '画笔模式' : '橡皮模式' }}
          </button>
          <button class="btn" @click="clearCanvas">清空画布</button>
          <button class="btn" @click="saveCanvas">保存手账</button>
        </div>

        <div class="slider-container">
          <span class="slider-label">{{ isErasing ? '橡皮擦粗细' : '画笔粗细' }}</span>
          <div class="slider-wrapper">
            <input
                type="range"
                class="custom-slider"
                :value="isErasing ? eraserSize : brushSize"
                min="1"
                max="50"
                @input="onSliderChange"
            />
            <div class="indicator-container">
              <div
                  class="size-indicator"
                  :style="{
                  width: (isErasing ? eraserSize : brushSize) + 'px',
                  height: (isErasing ? eraserSize : brushSize) + 'px',
                  backgroundColor: isErasing ? '#FFFFFF' : selectedColor,
                  border: isErasing ? '1px solid #ddd' : 'none'
                }"
              ></div>
              <span class="slider-value">
                {{ (isErasing ? eraserSize : brushSize) + 'px' }}
              </span>
            </div>
          </div>
        </div>
      </div>

      <div class="canvas-container">
        <canvas
            ref="canvas"
            class="drawing-layer"
            @mousedown="startDrawing"
            @mousemove="draw"
            @mouseup="stopDrawing"
            @mouseleave="stopDrawing"
            @click="handleCanvasClick"
        ></canvas>
      </div>
    </div>

    <div class="right-panel">
      <h3 class="sticker-title">贴纸列表</h3>
      <div class="sticker-list">
        <div
            v-for="(sticker, index) in stickers"
            :key="index"
            class="sticker-item"
            @click="selectSticker(sticker)"
        >
          <img :src="sticker" class="sticker-preview" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const colors = [
  '#000000', '#FF0000', '#FF7F00', '#FFFF00',
  '#00FF00', '#00FFFF', '#0000FF', '#8B00FF',
  '#FFFFFF', '#C0C0C0', '#008080', '#800080'
]

// 画布相关
const canvas = ref(null)
const ctx = ref(null)
const isDrawing = ref(false)
const selectedColor = ref('#000000')
const isErasing = ref(false)
const brushSize = ref(5)
const eraserSize = ref(20)

// 贴纸相关
const stickers = [
  '/static/stickers/边牧.png',
  '/static/stickers/帆船.png',
  '/static/stickers/圣诞礼物.png',
  '/static/stickers/圣诞雪花.png',
  '/static/stickers/仓鼠.png',
  '/static/stickers/飞机.png',
  '/static/stickers/圣诞树.png',
  '/static/stickers/圣诞雪帽.png',
  '/static/stickers/桃花.png',
  '/static/stickers/樱花.png',
  '/static/stickers/轮船.png',
  '/static/stickers/索道.png',
  '/static/stickers/橘猫.png'
]
const stickersOnCanvas = ref([])
const selectedSticker = ref(null)

// 初始化画布
onMounted(() => {
  const canvasEl = canvas.value
  const container = canvasEl.parentElement
  canvasEl.width = container.clientWidth
  canvasEl.height = container.clientHeight
  ctx.value = canvasEl.getContext('2d')
  ctx.value.fillStyle = '#FFFFFF'
  ctx.value.fillRect(0, 0, canvasEl.width, canvasEl.height)
})

const getCanvasPosition = (clientX, clientY) => {
  const canvasEl = canvas.value
  const rect = canvasEl.getBoundingClientRect()
  // 计算画布缩放比例
  const scaleX = canvasEl.width / rect.width
  const scaleY = canvasEl.height / rect.height
  return {
    x: (clientX - rect.left) * scaleX,
    y: (clientY - rect.top) * scaleY
  }
}

// 绘图逻辑
let lastX = 0
let lastY = 0

const startDrawing = (e) => {
  if (selectedSticker.value) return
  isDrawing.value = true
  const pos = getCanvasPosition(e.clientX, e.clientY)
  lastX = pos.x
  lastY = pos.y
}

const draw = (e) => {
  if (!isDrawing.value || selectedSticker.value) return

  const pos = getCanvasPosition(e.clientX, e.clientY)
  const currentX = pos.x
  const currentY = pos.y

  ctx.value.beginPath()
  ctx.value.moveTo(lastX, lastY)
  ctx.value.lineTo(currentX, currentY)
  ctx.value.strokeStyle = isErasing.value ? '#FFFFFF' : selectedColor.value
  ctx.value.lineWidth = isErasing.value ? eraserSize.value : brushSize.value
  ctx.value.lineCap = 'round'
  ctx.value.stroke()

  lastX = currentX
  lastY = currentY
}

const stopDrawing = () => {
  isDrawing.value = false
}

// 修复问题3：贴纸功能实现
const selectSticker = (src) => {
  selectedSticker.value = src
}

const handleCanvasClick = (e) => {
  if (!selectedSticker.value) return

  const pos = getCanvasPosition(e.clientX, e.clientY)
  const img = new Image()
  img.src = selectedSticker.value
  img.onload = () => {
    ctx.value.drawImage(img, pos.x - 20, pos.y - 20, 40, 40)
    stickersOnCanvas.value.push({
      img: img,
      x: pos.x,
      y: pos.y
    })
    selectedSticker.value = null
  }
}

// 其他工具方法
const selectColor = (color) => {
  selectedColor.value = color
  isErasing.value = false
}

const toggleEraser = () => {
  isErasing.value = !isErasing.value
  selectedSticker.value = null
}

const onSliderChange = (e) => {
  const value = parseInt(e.target.value)
  if (isErasing.value) {
    eraserSize.value = value
  } else {
    brushSize.value = value
  }
}

const clearCanvas = () => {
  ctx.value.clearRect(0, 0, canvas.value.width, canvas.value.height)
  ctx.value.fillStyle = '#FFFFFF'
  ctx.value.fillRect(0, 0, canvas.value.width, canvas.value.height)
  stickersOnCanvas.value = []
}

const saveCanvas = () => {
  const dataUrl = canvas.value.toDataURL('image/png')
  const link = document.createElement('a')
  link.download = 'my-canvas.png'
  link.href = dataUrl
  link.click()
}
</script>

<style>
.container {
  display: flex;
  height: 100vh;
  padding: 20px;
  background: #f0f2f5;
}

.left-panel {
  flex: 1;
  display: flex;
  flex-direction: column;
  margin-right: 20px;
}

.controls {
  background: white;
  padding: 15px;
  border-radius: 8px;
  margin-bottom: 20px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.color-picker {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-bottom: 15px;
}

.color-option {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  cursor: pointer;
  transition: transform 0.2s;
  border: 2px solid #eee;
}

.color-option.selected {
  transform: scale(1.2);
  border-color: #1890ff;
}

.tools {
  display: flex;
  gap: 10px;
  margin: 15px 0;
}

.btn {
  padding: 8px 15px;
  border: none;
  border-radius: 4px;
  background: #1890ff;
  color: white;
  cursor: pointer;
  transition: opacity 0.3s;
}

.btn:hover {
  opacity: 0.8;
}

.slider-container {
  display: flex;
  flex-direction: column;
  gap: 8px;
  padding: 10px 0;
}

.custom-slider {
  width: 200px;
}

.sticker-title {
  margin: 0 0 15px 0;
}

.right-panel {
  width: 200px;
  background: white;
  padding: 15px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.sticker-list {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 10px;
}

.sticker-item {
  cursor: pointer;
  padding: 5px;
  border-radius: 4px;
  transition: background 0.3s;
}

.sticker-item:hover {
  background: #f5f5f5;
}

.sticker-preview {
  width: 100%;
  height: auto;
}

.canvas-container {
  flex: 1;
  background: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.drawing-layer {
  width: 100%;
  height: 100%;
}

.size-indicator {
  border-radius: 50%; /* 改为圆形 */
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}
</style>