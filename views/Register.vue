<template>
  <div style="height: 100vh; overflow: hidden">
    <div style="display: flex; width: 80%; height: 100%; margin: 0 auto; align-items: center">

      <div style="flex: 1"><img src="@/assets/imgs/register%20.png" alt="" style="width: 130%;  margin-top: -150px; margin-left: -50px"></div>
      <div style="flex: 1; display: flex; justify-content: center">
        <div style="width: 50%">
          <h2 style="font-size: 30px">欢迎注册</h2>
          <div style="margin: 10px 0; color: #999">Welcome Register</div>
          <el-form :model="data.form" ref="formRef" :rules="data.rules">
            <el-form-item>
              <el-input size="large" :prefix-icon="User" placeholder="请输入账号" v-model="data.form.username"></el-input>
            </el-form-item>
            <el-form-item>
              <el-input size="large" :prefix-icon="Lock" show-password placeholder="请输入密码" v-model="data.form.password"></el-input>
            </el-form-item>
            <el-form-item>
              <el-input size="large" :prefix-icon="Lock" show-password placeholder="请确认密码" v-model="data.form.nmm"></el-input>
            </el-form-item>
            <el-form-item>
              <el-select size="large" style="width: 100%" placeholder="请选择角色" v-model="data.form.role">
								<el-option value="user" label="用户"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button size="large" type="primary" style="width: 100%; background-color: #5891aa; border-color: #5891aa" @click="register()">注 册</el-button>
            </el-form-item>
            <el-form-item class="regBox">
              已有账号？去 <a href="/login">登录</a>
            </el-form-item>
          </el-form>
        </div>
      </div>

    </div>
  </div>
</template>

<script setup>
  import {reactive, ref} from "vue";
  import {User, Lock} from "@element-plus/icons-vue";
  import request from "@/utils/request";
  import {ElMessage} from "element-plus";
  import router from "@/router";
  const data = reactive({
    form: {},
    roles: [],
    rules: {
      username: [
        {required: true, message: '请输入账号', trigger: 'blur'},
      ],
      password: [
        {required: true, message: '请输入密码', trigger: 'blur'},
      ],
      role: [
        {required: true, message: '请选择角色', trigger: 'blur'},
      ],
    }
  })

  const formRef = ref()

  // 点击登录按钮的时候会触发这个方法
  const register = () => {
    formRef.value.validate((valid => {
      if (valid) {
        // 调用后台的接口
        request.post('/register', data.form).then(res => {
          if (res.code === '200') {
            ElMessage.success("登录成功")
            router.push('/login')
          } else {
            ElMessage.error(res.msg)
          }
        })
      }
    })).catch(error => {
      console.error(error)
    })
  }
</script>

<style scoped>
body {
  height: 100vh;
  overflow: hidden;
}
h2 {
  margin: 0;
  padding: 0;
}
a {
  text-decoration: none;
  color: #5891aa;
}
.el-form-item {
  margin-bottom: 15px !important;
}
.regBox {
  text-align: right;
}
</style>