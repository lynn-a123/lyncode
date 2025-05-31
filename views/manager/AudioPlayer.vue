<template>
  <div class="audio-player">
    <h2>{{ currentTitle }}</h2>
    
    <audio 
      ref="audioPlayer"
      :src="currentAudio"
      @timeupdate="updateProgress"
      @ended="onAudioEnd"
    ></audio>
    
    <div class="progress-container">
      <div class="progress-bar" :style="{ width: progress + '%' }"></div>
      <span class="time">{{ formatTime(currentTime) }} / {{ formatTime(duration) }}</span>
    </div>
    
    <div class="controls">
      <button @click="togglePlay">
        {{ isPlaying ? '暂停' : '播放' }}
      </button>
      
      <div class="speed-control">
        <label>倍速:</label>
        <select v-model="playbackRate" @change="changeSpeed">
          <option value="0.5">0.5x</option>
          <option value="0.75">0.75x</option>
          <option value="1">1x</option>
          <option value="1.25">1.25x</option>
          <option value="1.5">1.5x</option>
          <option value="2">2x</option>
        </select>
      </div>
      
      <div class="timer-control">
        <label>定时关闭:</label>
        <select v-model="selectedTimer" @change="setTimer">
          <option value="0">关闭</option>
		   <option value="1">1分钟</option>
          <option value="15">15分钟</option>
          <option value="30">30分钟</option>
          <option value="60">1小时</option>
          <option value="90">1.5小时</option>
          <option value="120">2小时</option>
        </select>
        <span v-if="timerCountdown">剩余: {{ formatTimer(timerCountdown) }}</span>
      </div>
    </div>
    
   
  </div>
</template>

<script>
import { ref, onMounted, onUnmounted } from 'vue';

export default {
  name: 'AudioPlayer',
  setup() {
    const audioPlayer = ref(null);
    const isPlaying = ref(false);
    const currentTime = ref(0);
    const duration = ref(0);
    const progress = ref(0);
    const playbackRate = ref(1);
    const selectedTimer = ref(0);
    const timerCountdown = ref(0);
    const timerInterval = ref(null);
    const currentBookIndex = ref(0);
    
    const books = ref([
      { 
        title: localStorage.getItem("listenerBookTitle"), 
        audio: localStorage.getItem("listenerBookUrl")
      }, 
      // 可以添加更多书籍
    ]); 
    const currentTitle = ref(books.value[0].title);
    const currentAudio = ref(books.value[0].audio);
    
    const togglePlay = () => {
      if (isPlaying.value) {
        audioPlayer.value.pause();
      } else {
        audioPlayer.value.play();
      }
      isPlaying.value = !isPlaying.value;
    };
    
    const updateProgress = () => {
      currentTime.value = audioPlayer.value.currentTime;
      duration.value = audioPlayer.value.duration;
      progress.value = (currentTime.value / duration.value) * 100;
    };
    
    const onAudioEnd = () => {
      isPlaying.value = false;
    };
    
    const changeSpeed = () => {
      audioPlayer.value.playbackRate = playbackRate.value;
    };
    
    const setTimer = () => {
      clearInterval(timerInterval.value);
      const minutes = parseInt(selectedTimer.value);
      
      if (minutes > 0) {
        timerCountdown.value = minutes * 60;
        
        timerInterval.value = setInterval(() => {
          timerCountdown.value--;
          
          if (timerCountdown.value <= 0) {
            clearInterval(timerInterval.value);
            audioPlayer.value.pause();
            isPlaying.value = false;
            selectedTimer.value = 0;
          }
        }, 1000);
      } else {
        timerCountdown.value = 0;
      }
    };
    
    const formatTime = (seconds) => {
      if (isNaN(seconds)) return '00:00';
      const mins = Math.floor(seconds / 60);
      const secs = Math.floor(seconds % 60);
      return `${mins.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`;
    };
    
    const formatTimer = (seconds) => {
      const mins = Math.floor(seconds / 60);
      const secs = Math.floor(seconds % 60);
      return `${mins}分${secs}秒`;
    };
    
    const selectBook = (index) => {
      currentBookIndex.value = index;
      currentTitle.value = books.value[index].title;
      currentAudio.value = books.value[index].audio;
      
      // 切换书籍时停止当前播放
      if (isPlaying.value) {
        audioPlayer.value.pause();
        isPlaying.value = false;
      }
      
      // 重置播放器
      setTimeout(() => {
        audioPlayer.value.load();
      }, 0);
    };
    
    onMounted(() => {
      audioPlayer.value.addEventListener('loadedmetadata', () => {
        duration.value = audioPlayer.value.duration;
      });
    });
    
    onUnmounted(() => {
      clearInterval(timerInterval.value);
    });
    
    return {
      audioPlayer,
      isPlaying,
      currentTime,
      duration,
      progress,
      playbackRate,
      selectedTimer,
      timerCountdown,
      currentBookIndex,
      books,
      currentTitle,
      currentAudio,
      togglePlay,
      updateProgress,
      onAudioEnd,
      changeSpeed,
      setTimer,
      formatTime,
      formatTimer,
      selectBook,
    };
  },
};
</script>

<style scoped>
.audio-player {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
  font-family: Arial, sans-serif;
}

.progress-container {
  height: 20px;
  background-color: #f0f0f0;
  margin: 20px 0;
  border-radius: 10px;
  position: relative;
}

.progress-bar {
  height: 100%;
  background-color: #42b983;
  border-radius: 10px;
  transition: width 0.1s;
}

.time {
  position: absolute;
  right: 10px;
  top: 0;
  line-height: 20px;
  font-size: 12px;
  color: #666;
}

.controls {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  flex-wrap: wrap;
  gap: 10px;
}

button {
  padding: 8px 16px;
  background-color: #42b983;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:hover {
  background-color: #3aa876;
}

.speed-control, .timer-control {
  display: flex;
  align-items: center;
  gap: 5px;
}

select {
  padding: 5px;
  border-radius: 4px;
  border: 1px solid #ddd;
}

.book-list {
  margin-top: 20px;
}

.book-list ul {
  list-style: none;
  padding: 0;
}

.book-list li {
  padding: 10px;
  border-bottom: 1px solid #eee;
  cursor: pointer;
}

.book-list li:hover {
  background-color: #f5f5f5;
}

.book-list li.active {
  background-color: #e0f7fa;
  font-weight: bold;
}
</style>