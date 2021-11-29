<template>
  <div class="dashboard-container">
    <el-card class="box-card">
      <el-descriptions title="用户信息"  :column="4" >
        <template slot="extra">
          <el-button type="primary" size="small">修改</el-button>
        </template>
        <el-descriptions-item label="用户名">{{ user.nick }}</el-descriptions-item>
        <el-descriptions-item label="邮 箱">{{ user.email }}</el-descriptions-item>
        <el-descriptions-item label="性 别"> {{ user.sex }}</el-descriptions-item>
        <el-descriptions-item label="角 色">
          <el-tag size="small">{{ role.name }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="上传量">
          {{ user.upload }}
        </el-descriptions-item>
        <el-descriptions-item label="下载量">
          {{ user.download }}
        </el-descriptions-item>

        <el-descriptions-item label="真实上传量">
          {{ user.realUpload }}
        </el-descriptions-item>
        <el-descriptions-item label="真实下载量">
          {{ user.realDownload }}
        </el-descriptions-item>

        <el-descriptions-item label="魔力值">
          {{ user.intercal }}
        </el-descriptions-item>

        <el-descriptions-item label="状态">
          {{ user.status }}
        </el-descriptions-item>

        <el-descriptions-item label="头 像">
          <img :src="user.avatar+'?imageView2/1/w/80/h/80'"  class="user-avatar">
        </el-descriptions-item>
      </el-descriptions>
    </el-card>



  </div>
</template>

<script>
import {mapGetters} from 'vuex'

export default {
  name: 'Dashboard',
  computed: {
    ...mapGetters([
      'user',
      'role'
    ])
  }
}
</script>

<style lang="scss" scoped>
.dashboard-container {
  &-container {
    margin: 30px;
  }

  .box-card {
    margin: 20px;
    width: 1080px;
  }
  &-text {
    font-size: 30px;
    line-height: 46px;
  }
  .user-avatar{
    cursor: pointer;
    width: 40px;
    height: 40px;
    border-radius: 10px;
  }
}
</style>
